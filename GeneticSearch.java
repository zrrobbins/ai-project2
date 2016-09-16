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

/**
 * Implementation of Iterative Deepening Search.
 */
public class GeneticSearch extends Search {

	int nodesExpanded = 0;
	final int INIT_POPULATION_SIZE = 10;
	final double CROSSOVER_RATE = .75;

	public GeneticSearch(double startValue, double targetValue, double timeLimit, Operation[] operations) {
		super(startValue,  targetValue, timeLimit, operations);
	}


	/*
		Organism: A path of operators 
		
		0.) Reproduction function (takes two array of operators and creates
			a new population)
		1.) Initial population
		2.) Apply fitness function
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

	/**
	 * Given two parent organisms, return a two children as the result of a possible crossover operation
	 * that occurs at rate CROSSOVER_RATE. If crossover doesn't occur, return nothing.
	 */
	private List<List<Operation>> reproduce(List<Operation> mother, List<Operation> father) {

		List<Operation> child1 = new LinkedList<Operation>(mother);
		List<Operation> child2 = new LinkedList<Operation>(father);

		double crossover = Math.random();
		List<List<Operation>> result = new LinkedList<List<Operation>>();

		if (crossover < CROSSOVER_RATE) { // Crossover 
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

	private void testReproduce(List<Operation> mother, List<Operation> father) {
		System.out.println("\nCROSSOVER/REPRODUCTION TEST:\nBefore swap:");
		System.out.println(mother);
		System.out.println(father);
		List<List<Operation>> swappedOrganisms = reproduce(mother, father);
		if (swappedOrganisms.size() > 0) {
			System.out.println("\nAfter swap:");
			System.out.println(swappedOrganisms.get(0));
			System.out.println(swappedOrganisms.get(1));
		} else {
			System.out.println("No crossover");
		}
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

		// This pulls the top two organisms off of the priority queue and simulates crossover
		testReproduce(population.poll(), population.poll());

		// TODO: finish
	}

	private static class OrganismComparator implements Comparator<List<Operation>> {
		
		public final double startValue, targetValue;

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
