/**
 * GreedySearch.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

public class GreedySearch extends Search {

	public GreedySearch(double startValue, double targetValue, double timeLimit, Operation[] operations) {
		super(startValue, targetValue, timeLimit, operations);
	}

	@Override
	public Operation[] performSearch() {
		long startTimeMillis = System.currentTimeMillis();
		Node root = new Node(this.startValue, null, null);
		Node curr = root;

		while (
			curr.value != targetValue && 
			System.currentTimeMillis() - startTimeMillis < timeLimit * 1000
		) {

		}

		// TODO
		return null;
	}
}
