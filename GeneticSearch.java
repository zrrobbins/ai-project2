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

	int nodesExpanded = 0;
	final int INIT_POPULATION_SIZE = 10;
	final int NUMBER_OF_PARENTS = 10;

	public GeneticSearch(double startValue, double targetValue, double timeLimit, Operation[] operations) {
		super(startValue,  targetValue, timeLimit, operations);
	}


	/*
		Organism: A path of operators 
		
		0.) Reproduction function (takes two array of operators and creates
			a new population)
		1.) Initial population
		2   Apply fitness function
		3.) Selection 
		4.) Crossover
		5.) Mutation
		6.) PROFIT
	 */

	private List<Operation> generateNewOrganism() {
		Random rand = new Random();
		int len = rand.nextInt(26) + 5;
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

	//Generates a list of paired organisms
	private List<ParentPair> selection(PriorityQueue<List<Operation>> population){
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

				//Runs a for loop currentProbNumerator number of times, where each time there is a 1/probDenominator chance of being seleted
				for(int i = 0; i<currentProbNumerator;i++){
					int value = randomizer.nextInt(probDenominator);
					if(value == 1){
						parentList.add(organism);
						break;
					}
				}

				//Make sure that the probability numerator never equals 0
				if(currentProbNumerator != 1){
					currentProbNumerator--;
				}
			}
		}

		int parent1Index = 0;
		int parent2Index = 0;
		List<ParentPair> pairList = new ArrayList<ParentPair>();//List of pairs
		//While the parentList has two or more organisms in it randomly pair up two organism and add them to the pairList
		while(parentList.size() > 1){
			parent1Index = randomizer.nextInt(parentList.size() - 1);
			List<Operation> organism1 = parentList.get(parent1Index);
			parentList.remove(parent1Index);

			parent2Index = randomizer.nextInt(parentList.size() - 1);
			List<Operation> organism2 = parentList.get(parent2Index);
			parentList.remove(parent2Index);

			ParentPair pair = new ParentPair(organism1, organism2);
			pairList.add(pair);
		}
		return pairList;
	}

	private List<Operation> reproduce(List<Operation> mother, List<Operation> father) {
		return null; // TODO
	}

	private void mutate(List<Operation> organism) {
		// TODO
	}

	/**
	 * Perform the search.
	 */
	@Override
	public void performSearch() {
		PriorityQueue<List<Operation>> population = new PriorityQueue<List<Operation>>(
			64, new OrganismComparator(this.startValue, this.targetValue)
		);
		for (int i = 0; i < INIT_POPULATION_SIZE; i++) {
			List<Operation> organism = this.generateNewOrganism();
			debugPrintOrganism(organism);
			population.add(organism);
		}

		//selection(population);

		// TODO: finish
	}

	private static class OrganismComparator implements Comparator<List<Operation>> {
		
		public final double it, targetValue, startValue;

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
}
