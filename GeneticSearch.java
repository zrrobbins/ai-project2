/**
 * GeneticSearch.java
 *
 * CS4341: Project 2
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List; 
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Implementation of Genetic search
 */
public class GeneticSearch extends Search {

	final boolean DEBUG = false;
	final int INIT_POPULATION_SIZE = 10;
	final int NUMBER_OF_PARENTS = 10;
	final double REPRODUCTION_RATE = 0.75;
	final double MUTATION_RATE = 0.1;
	final int MIN_INIT_ORGANISM_LENGTH = 1;
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
	private Organism generateNewOrganism() {
		Random rand = new Random();
		int len = rand.nextInt(
			MAX_INIT_ORGANISM_LENGTH - MIN_INIT_ORGANISM_LENGTH + 1
		) + MIN_INIT_ORGANISM_LENGTH;
		List<Operation> ops = new LinkedList<Operation>();
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
			ops.add(toAdd);
			val = toAdd.applyTo(val);
		}
		return new Organism(ops, this.startValue);
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
	private List<ParentPair> selectParents(PriorityQueue<Organism> population){
		List<Organism> parentList = new ArrayList<Organism>();
		Random randomizer = new Random();

		//This while loop will run until we have filled the parentList
		while(parentList.size() < NUMBER_OF_PARENTS) {
			//currentProbNumerator is the numerator for the probability calculations(ie in a 10 item list the chance for the first item being selected will be 9/10)
			//Similarly probDenominator is the denominator for these calculations
			int currentProbNumerator = population.size() - 1;
			int probDenominator = population.size();

			//Run through the population list. -Note we will keep doing this until
			for(Organism organism : population){

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

		List<ParentPair> pairList = new ArrayList<ParentPair>();//List of pairs
		Collections.shuffle(parentList);
		while (parentList.size() >= 2) {
			Organism parent1 = parentList.remove(0);
			for (int i = 0; i < parentList.size(); i++) {
				Organism parent2 = parentList.get(i);
				if (parent1 != parent2) {
					parentList.remove(i);
					pairList.add(new ParentPair(parent1, parent2));
					break;
				}
			}
		}
		return pairList;
	}

	/**
	 * Given a pair of parent organisms, return a two children as the result of
	 * a possible crossover operation that occurs at rate REPRODUCTION_RATE. If
	 * crossover doesn't occur, return an empty list.
	 *
	 * @param parents pair of parents from which children will be derived
	 * @return        a list of offspring organisms
	 */
	private List<Organism> reproduce(ParentPair parents) {
		List<Organism> result = new LinkedList<Organism>();
		if (Math.random() >= REPRODUCTION_RATE) return result;

		Organism p1 = parents.parentOne;
		Organism p2 = parents.parentTwo;
		Random rand = new Random();
		int cut1 = p1.numOperations <= 2 ? 1 : rand.nextInt(p1.numOperations - 2) + 1;
		int cut2 = p2.numOperations <= 2 ? 1 : rand.nextInt(p2.numOperations - 2) + 1;
		List<Operation> c1 = new LinkedList<Operation>();
		List<Operation> c2 = new LinkedList<Operation>();
		for (int i = 0; i < cut1; i++) {
			c1.add(p1.operations.get(i));
		}
		for (int i = cut2; i < p2.numOperations; i++) {
			c1.add(p2.operations.get(i));
		}
		for (int i = 0; i < cut2; i++) {
			c2.add(p2.operations.get(i));
		}
		for (int i = cut1; i < p1.numOperations; i++) {
			c2.add(p1.operations.get(i));
		}
		result.add(new Organism(c1, this.startValue));
		result.add(new Organism(c2, this.startValue));

		if (DEBUG) {
			System.out.println();
			System.out.println("------------- reproduction -------------");
			debugPrintOrganism(p1);
			debugPrintOrganism(p2);
			debugPrintOrganism(result.get(0));
			debugPrintOrganism(result.get(1));
		}

		return result;
	}

	/**
	 * Removes AMOUNT_TO_CULL organisms if the population size is at least 
	 * SIZE_REQUIRED_FOR_CULL.
	 *
	 * @param population list of organisms to cull
	 * @return           population after potential culling
	 */
	public PriorityQueue<Organism> cull(PriorityQueue<Organism> population) {
		int popSize = population.size();
		if (popSize < SIZE_REQUIRED_FOR_CULL) {
			return population;	
		}
		int newPopSize = popSize - AMOUNT_TO_CULL;
		PriorityQueue<Organism> result = new PriorityQueue<Organism>(
			newPopSize, new OrganismComparator(this.targetValue)
		);
		for (int i = 0; i < popSize - AMOUNT_TO_CULL; i++) {
			result.add(population.poll());
		}
		return result;
	}

	/**
	 * Goes through population and mutates at a rate of MUTATION_RATE.
	 *
	 * @param population organisms to potentially mutate; is emptied by this
	 *                   this function
	 * @return           population after mutation
	 */
	public PriorityQueue<Organism> mutate(PriorityQueue<Organism> population) {
		PriorityQueue<Organism> result = new PriorityQueue<Organism>(
			64, new OrganismComparator(this.targetValue)
		);
		while (!population.isEmpty()) {
			Organism org = population.poll();
			if (Math.random() < MUTATION_RATE) {
				Organism newOrg = org.mutate(this.operations, this.startValue);
				if (DEBUG) {
					System.out.println();
					System.out.println("-------------- mutation --------------");
					debugPrintOrganism(org);
					debugPrintOrganism(newOrg);
				}
				result.add(newOrg);
			} else {
				result.add(org);
			}
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
		PriorityQueue<Organism> population = new PriorityQueue<Organism>(
			64, new OrganismComparator(this.targetValue)
		);
		for (int i = 0; i < INIT_POPULATION_SIZE; i++) {
			population.add(this.generateNewOrganism());
		}

		while (true) {

			if (!DEBUG && System.currentTimeMillis() - startTimeMillis >= timeLimit * 1000) break;

			if (DEBUG) {
				System.out.println();
				System.out.println("---------------- population ----------------");
				PriorityQueue<Organism> dbgPop = new PriorityQueue<Organism>(population);
				while (!dbgPop.isEmpty()) {
					debugPrintOrganism(dbgPop.poll());
				}
				System.console().readLine();				
			}

			List<ParentPair> parentPairs = selectParents(population);
			for (ParentPair parentPair : parentPairs) {
				for (Organism child : reproduce(parentPair)) {
					population.add(child);
				}
			}
			population = mutate(cull(population));
		}

		// TODO: printing results
		debugPrintOrganism(population.poll());
	}

	private static class OrganismComparator implements Comparator<Organism> {
		
		public final double targetValue;

		public OrganismComparator(double targetValue) {
			this.targetValue = targetValue;
		}

		@Override
		public int compare(Organism a, Organism b) {
			double aDiff = Math.abs(a.resultValue - this.targetValue);
			double bDiff = Math.abs(b.resultValue - this.targetValue);
			return (
				aDiff == bDiff ? 
				Integer.compare(a.numOperations, b.numOperations) : 
				(aDiff < bDiff ? -1 : 1)
			);
		}
	}


	/* DEBUGGING METHODS */

	public void testMutators(){
		for (int i = 0; i < INIT_POPULATION_SIZE; i++) {
			Organism organism = this.generateNewOrganism();
			debugPrintOrganism(organism);
			Organism mutated = organism.mutate(this.operations, this.startValue);
			debugPrintOrganism(mutated);
		}
	}

	private void testReproduce(Organism mother, Organism father) {
		System.out.println("\nCROSSOVER/REPRODUCTION TEST:\nBefore swap:");
		debugPrintOrganism(mother);
		debugPrintOrganism(father);
		List<Organism> swappedOrganisms = reproduce(new ParentPair(mother, father));
		if (swappedOrganisms.size() > 0) {
			System.out.println("\nAfter swap:");
			debugPrintOrganism(swappedOrganisms.get(0));
			debugPrintOrganism(swappedOrganisms.get(1));
		} else {
			System.out.println("No crossover");
		}
	}

	private void debugPrintOrganism(Organism organism) {
		System.out.print("[");
		boolean firstIter = true;
		for (Operation op : organism.operations) {
			if (!firstIter) {
				System.out.print(", ");
			}
			System.out.print(op + "");
			firstIter = false;
		}
		System.out.println("]: " + organism.resultValue);
	}
}
