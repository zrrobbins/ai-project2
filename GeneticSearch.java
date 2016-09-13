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
		return null; // TODO
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
			population.add(this.generateNewOrganism());
		}
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
