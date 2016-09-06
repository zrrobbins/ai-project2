/**
 * GreedySearch.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

import java.util.Comparator;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Arrays;

/**
 * Implementation of Greedy Search.
 */
public class GreedySearch extends Search {

	public GreedySearch(int startValue, int targetValue, double timeLimit, Operation[] operations) {
		super(startValue, targetValue, timeLimit, operations);
	}

	/**
	 * Perform the search.
	 */
	@Override
	public void performSearch() {
		long startTimeMillis = System.currentTimeMillis();
		Node root = new Node(this.startValue, null, null);
		Node curr = root;
		int lastVal;
		long elapsedTime;
		int nodesExpanded = 0;
		int maxSearchDepth = 0;

		PriorityQueue<Node> queue = new PriorityQueue<Node>(64, new GreedySearchComparator(this.targetValue));
		while (
			curr.value != targetValue && 
			System.currentTimeMillis() - startTimeMillis < timeLimit * 1000
		) {
			curr.expand(this.operations);
			nodesExpanded += this.operations.length;
			for (Node n : curr.children) {
				queue.add(n);
			}
			if (queue.isEmpty()) break;

			// TODO: Need a more efficient way to keep track of max search depth
			// This will backtrace through parents up to the root node (i.e. the current path) and save the length every time
			// a node is expanded other than one of the last node's children
			if (!Arrays.asList(curr.children).contains(queue.peek()))
				maxSearchDepth = updateDepth(maxSearchDepth, curr);

			curr = queue.poll();
		}

		// Capture elapsed time
		elapsedTime = System.currentTimeMillis() - startTimeMillis;
		lastVal = curr.value;

		// Output
		Stack<String> steps = new Stack<String>();
		maxSearchDepth = updateDepth(maxSearchDepth, curr); // See if goal is deepest depth in search
		int stepsRequired = 0;
		while (curr != root) {
			steps.push(curr.parent.value + " " + curr.operation + " = " + curr.value);
			curr = curr.parent;
		}

		stepsRequired = steps.size();
		while (!steps.empty()) {
			System.out.println(steps.pop());
		}		

		System.out.println("Error: " + (targetValue - lastVal));
		System.out.println("Number of steps required: " + stepsRequired);
		System.out.println("Search required: " + elapsedTime + " milliseconds");
		System.out.println("Nodes expanded: " + nodesExpanded);
		System.out.println("Maximum search depth: " + maxSearchDepth);

	}

	/**
	 * Given the current max depth, determines if the given node is located deeper, and if so, returns its depth.
	 * Otherwise, returns the currentMaxDepth.
	 */
	private int updateDepth(int currentMaxDepth, Node node) {
		int counter = 0;
		while (node.parent != null) {
			counter++;
			node = node.parent;
		}
		return Math.max(currentMaxDepth, counter);
	}

	/**
	 * Implementation of Comparator for use in Greedy Search
	 */
	private static class GreedySearchComparator implements Comparator<Node> {

		public final int targetValue;

		public GreedySearchComparator(int targetValue) {
			this.targetValue = targetValue;
		}

		@Override
		public int compare(Node a, Node b) {
			int aDiff = Math.abs(a.value - this.targetValue);
			int bDiff = Math.abs(b.value - this.targetValue);
			return aDiff == bDiff ? 0  :
			       aDiff <  bDiff ? -1 : 1;
		}
	}
}
