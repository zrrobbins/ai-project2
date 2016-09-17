/**
 * GeneticSearch.java
 *
 * CS4341: Project 2
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List; 
import java.util.PriorityQueue;
import java.util.Random;
import java.util.ArrayList;

/**
 * Implementation of Iterative Deepening Search.
 */
public class GeneticSearch extends Search {

	final int INIT_POPULATION_SIZE = 10;
	final int NUMBER_OF_PARENTS = 10;
	final double CROSSOVER_RATE = 0.75;
	final double MUTATION_RATE = 0.15;
	final int MIN_INIT_ORGANISM_LENGTH = 5;
	final int MAX_INIT_ORGANISM_LENGTH = 30;
	final int SIZE_REQUIRED_FOR_CULL = 1000;
	final int AMOUNT_TO_CULL = 500;

	public GeneticSearch(double startValue, double targetValue, double timeLimit, Operation[] operations) {
		super(startValue,  targetValue, timeLimit, operations);
	}

	/**
	 * Creates a new organism with length in range [MIN_INIT_ORGANISM_LENGTH,
	 * MAX_INIT_ORGANISM LENGTH]. Operations are selected randomly or greedily.
	 *
	 * @return the new organism
	 */
	private List<Operation> generateNewOrganism() {
		Random rand = new Random();
		int len = rand.nextInt(
			MAX_INIT_ORGANISM_LENGTH - MIN_INIT_ORGANISM_LENGTH + 1
		) + MIN_INIT_ORGANISM_LENGTH;
		List<Operation> result = new LinkedList<Operation>();
		double val = this.startValue;
		for (int i = 0; i < len; i++) {
			boolean useGreedy = rand.nextInt(2) == 0;
			Operation toAdd = null;
			if (useGreedy) {
				double bestVal = Double.MAX_VALUE;
				for (Operation op : this.operations) {
					double v = op.applyTo(val);
					if (toAdd == null || Math.abs(v - this.targetValue) < Math.abs(bestVal - this.targetValue)) {
						bestVal = v;
						toAdd = op;
					}
				}
			} else {
				toAdd = this.operations[rand.nextInt(this.operations.length)];
			}
			result.add(toAdd);
			val = toAdd.applyTo(val);
		}
		return result;
	}

	/**
	 * Selects pairs of parents to be used for reproduction. Parents with high
	 * fitness function values (i.e. are closer to targetValue) are more likely
	 * to be selected. A single organism can be selected to be a parent multiple
	 * times, but it will not be paired with itself.
	 * 
	 * @param population the population from which the parents will be drawn
	 * @return           a list of pairs of parents, with a maximum length
	 *                   of NUMBER_OF_PARENTS
	 */
	private List<ParentPair> selectParents(PriorityQueue<List<Operation>> population){
		List<List<Operation>> parentList = new ArrayList<List<Operation>>();
		Random randomizer = new Random();

		//This while loop will run until we have filled the parentList
		while(parentList.size() < NUMBER_OF_PARENTS){
			//currentProbNumerator is the numerator for the probability calculations(ie in a 10 item list the chance for the first item being selected will be 9/10)
			//Similiarly probDenominator is the denominator for these calculations
			int currentProbNumerator = population.size() - 1;
			int probDenominator = population.size();

			//Run through the population list. -Note we will keep doing this until
			for(List<Operation> organism : population){

				//Make sure that the parent list isn't too big
				if(parentList.size() == NUMBER_OF_PARENTS){
					break;
				}

				double prob = currentProbNumerator / (double) probDenominator / 2;
				if (randomizer.nextDouble() < prob) {
					parentList.add(organism);
				}

				//Make sure that the probability numerator never equals 0
				if(currentProbNumerator != 1){
					currentProbNumerator--;
				}
			}
		}

		// TODO make this work in all cases
		int parent1Index = 0;
		int parent2Index = 0;
		List<ParentPair> pairList = new ArrayList<ParentPair>();//List of pairs
		//While the parentList has two or more organisms in it randomly pair up two organism and add them to the pairList
		while(parentList.size() >= 2) {
			parent1Index = randomizer.nextInt(parentList.size());
			List<Operation> organism1 = parentList.get(parent1Index);
			parentList.remove(parent1Index);

			parent2Index = randomizer.nextInt(parentList.size());
			List<Operation> organism2 = parentList.get(parent2Index);

			while(organism1 == organism2 && parentList.size() !=2){
				parent2Index = randomizer.nextInt(parentList.size());
				organism2 = parentList.get(parent2Index);
			}
			parentList.remove(parent2Index);

			ParentPair pair = new ParentPair(organism1, organism2);
			pairList.add(pair);
		}
		return pairList;
	}



	/**
	 * Given a pair of parent organisms, return a two children as the result of
	 * a possible crossover operation that occurs at rate CROSSOVER_RATE. If
	 * crossover doesn't occur, return an empty list.
	 *
	 * @param parents pair of parents from which children will be derived
	 * @return        a list of offspring organisms
	 */
	private List<List<Operation>> reproduce(ParentPair parents) {

		List<Operation> child1 = new LinkedList<Operation>(parents.parentOne);
		List<Operation> child2 = new LinkedList<Operation>(parents.parentTwo);

		double crossover = Math.random();
		List<List<Operation>> result = new LinkedList<List<Operation>>();

		if (crossover < CROSSOVER_RATE) {
			int shortestLength = Math.min(child1.size(), child2.size());
			Random rand = new Random();
			int crossoverPoint = rand.nextInt(shortestLength);

			LinkedList<Operation> child1Front = new LinkedList<Operation>();
			LinkedList<Operation> child2Front = new LinkedList<Operation>();
			
			for (int i = 0; i < crossoverPoint; i++) {
				child1Front.add(child1.remove(0));
				child2Front.add(child2.remove(0));
			}

			child1Front.addAll(child2);
			child2Front.addAll(child1);
			result.add(child1Front);
			result.add(child2Front);
		}
		return result;
	}

	/**
	 * Makes a random mutation with a chance of MUTATION_RATE. If a mutation
	 * occurs, it is equally likely to be the removal of an operation, the
	 * addition of an operation, or the changing of an operation.
	 *
	 * @param organism the organism to be potentially mutated
	 */
	public void mutate(List<Operation> organism) {
		Random rand = new Random();
		double r = rand.nextDouble();
		int j = rand.nextInt(organism.size());
		int i = rand.nextInt(this.operations.length);
		if (r < MUTATION_RATE / 3) {
			organism.remove(j);
		} else if (r < MUTATION_RATE / 3 * 2) {
			organism.add(j, this.operations[i]);
		} else if (r < MUTATION_RATE) {
			organism.set(j, this.operations[i]);
		}
	}

	/**
	 * Removes AMOUNT_TO_CULL organisms if the population size is at least 
	 * SIZE_REQUIRED_FOR_CULL.
	 *
	 * @param population list of organisms to cull
	 * @return           population after potential culling
	 */
	public PriorityQueue<List<Operation>> cull(PriorityQueue<List<Operation>> population) {
		int popSize = population.size();
		if (popSize < SIZE_REQUIRED_FOR_CULL) return population;
		int newPopSize = popSize - AMOUNT_TO_CULL;
		PriorityQueue<List<Operation>> result = new PriorityQueue<List<Operation>>(
			newPopSize, new OrganismComparator(this.startValue, this.targetValue)
		);
		for (int i = 0; i < popSize - AMOUNT_TO_CULL; i++) {
			result.add(population.poll());
		}
		return result;
	}

	/**
	 * Perform the genetic search.
	 */
	@Override
	public void performSearch() {

		long startTimeMillis = System.currentTimeMillis();

		// Initialize population
		PriorityQueue<List<Operation>> population = new PriorityQueue<List<Operation>>(
			64, new OrganismComparator(this.startValue, this.targetValue)
		);
		for (int i = 0; i < INIT_POPULATION_SIZE; i++) {
			List<Operation> organism = this.generateNewOrganism();
			population.add(organism);
		}

		while (System.currentTimeMillis() - startTimeMillis < timeLimit * 1000) {
			
			List<ParentPair> parentPairs = selectParents(population);
			for (ParentPair parentPair : parentPairs) {
				for (List<Operation> child : reproduce(parentPair)) {
					population.add(child);
				}
			}
			population = cull(population);
			// TODO: mutate
		}

		// TODO: printing results
		debugPrintOrganism(population.poll());
	}

	private static class OrganismComparator implements Comparator<List<Operation>> {
		
		public final double targetValue, startValue;

		public OrganismComparator(double startValue, double targetValue) {
			this.startValue = startValue;
			this.targetValue = targetValue;
		}
		@Override
		public int compare(List<Operation> a, List<Operation> b) {
			double aVal = startValue;
			for (Operation op : a) {
				aVal = op.applyTo(aVal);
			}
			double bVal = startValue;
			for (Operation op : b) {
				bVal = op.applyTo(bVal);
			}
			double aDiff = Math.abs(aVal - this.targetValue);
			double bDiff = Math.abs(bVal - this.targetValue);
			return aDiff == bDiff ? 0  :
			       aDiff <  bDiff ? -1 : 1;
		}	
	}


	/* DEBUGGING METHODS */

	/**
	 * Tests Mutation functions
	 */
	public void testMutators(){
		PriorityQueue<List<Operation>> population = new PriorityQueue<List<Operation>>(
			64, new OrganismComparator(this.startValue, this.targetValue)
		);
		for (int i = 0; i < INIT_POPULATION_SIZE; i++) {
			List<Operation> organism = this.generateNewOrganism();
			debugPrintOrganism(organism);
			population.add(organism);
			mutate(organism);
			debugPrintOrganism(organism);
		}
	}

	private void testReproduce(List<Operation> mother, List<Operation> father) {
		System.out.println("\nCROSSOVER/REPRODUCTION TEST:\nBefore swap:");
		System.out.println(mother);
		System.out.println(father);
		List<List<Operation>> swappedOrganisms = reproduce(new ParentPair(mother, father));
		if (swappedOrganisms.size() > 0) {
			System.out.println("\nAfter swap:");
			System.out.println(swappedOrganisms.get(0));
			System.out.println(swappedOrganisms.get(1));
		} else {
			System.out.println("No crossover");
		}
	}

	private void debugPrintOrganism(List<Operation> organism) {
		System.out.print("[");
		boolean firstIter = true;
		for (Operation op : organism) {
			if (!firstIter) {
				System.out.print(", ");
			}
			System.out.print(op + "");
			firstIter = false;
		}
		System.out.println("]");
	}
}
