/**
 * GeneticSearch.java
 *
 * CS4341: Project 2
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */
import java.util.Stack;
import java.util.Arrays;

/**
 * Implementation of Iterative Deepening Search.
 */
public class GeneticSearch extends Search {

	int nodesExpanded = 0;

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





	/**
	 * Perform the search.
	 */
	@Override
	public void performSearch() {
		System.out.println("LOL");

	}

	
}
	