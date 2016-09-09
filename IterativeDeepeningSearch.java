/**
 * IterativeDeepeningSearch.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */
import java.util.Stack;
import java.util.Arrays;

/**
 * Implementation of Iterative Deepening Search.
 */
public class IterativeDeepeningSearch extends Search {

	int nodesExpanded = 0;

	public IterativeDeepeningSearch(int startValue, int targetValue, double timeLimit, Operation[] operations) {
		super(startValue,  targetValue, timeLimit, operations);
	}

	/**
	 * Perform the search.
	 */
	@Override
	public void performSearch() {
		long startTimeMillis = System.currentTimeMillis();
		int maxDepth = 0;
		Node closestNode = null;

		// Increment depth until target is found, or we run out of time
		while ((closestNode == null || closestNode.value != this.targetValue) && System.currentTimeMillis() - startTimeMillis < timeLimit * 1000) {
			maxDepth++;
			closestNode = depthLimitedSearch(maxDepth, new Node(startValue, null, null), startTimeMillis);
		}

		long elapsedTime = System.currentTimeMillis() - startTimeMillis;
		Stack<String> path = new Stack<String>();
		Node currentNode = closestNode;

		// Assemble and print best path found
		while (currentNode.parent != null) {
			path.push(currentNode.parent.value + " " + currentNode.operation + " = " + currentNode.value);
			currentNode = currentNode.parent;
		}
		int stepsRequired = path.size();
		while (!path.empty()) {
			System.out.println(path.pop());
		}

		// Print statistics
		System.out.println("\nError: " + (targetValue - closestNode.value));
		System.out.println("Number of steps required: " + stepsRequired);
		System.out.println("Search required: " + elapsedTime + " milliseconds");
		System.out.println("Nodes expanded: " + nodesExpanded);
		System.out.println("Maximum search depth: " + maxDepth);
	}

	/**
	 * Depth limited search. Performs DFS to a specified depth. 
	 */
	private Node depthLimitedSearch(int maxDepth, Node node, long startTimeMillis) {
		// End search if target value found, max depth reached, or time runs out
		boolean done = (
			node.value == this.targetValue || 
			node.depth >= maxDepth || 
			System.currentTimeMillis() - startTimeMillis >= timeLimit * 1000
		);
		if (done) return node;

		node.expand(this.operations);
		nodesExpanded++;
		Node closestNode = null;
		int closestNodeDiff = Integer.MAX_VALUE;
		for (Node child : node.children) {
			Node c = depthLimitedSearch(maxDepth, child, startTimeMillis);
			int cDiff = Math.abs(c.value - this.targetValue);
			// Keep track of node closest to goal
			if (cDiff < closestNodeDiff) {
				closestNode = c;
				closestNodeDiff = cDiff;
			}
		}

		// Free memory
		node.collapse();

		// Return best node found (node closest to goal or target node if found)
		return closestNodeDiff < Math.abs(node.value - this.targetValue) ? closestNode : node;
	}
}
	