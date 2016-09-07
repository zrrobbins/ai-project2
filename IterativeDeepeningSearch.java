/**
 * IterativeDeepeningSearch.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */
import java.util.Stack;
import java.util.Arrays;

public class IterativeDeepeningSearch extends Search {

	public IterativeDeepeningSearch(int startValue, int targetValue, double timeLimit, Operation[] operations) {
		super(startValue,  targetValue, timeLimit, operations);
	}

	@Override
	public void performSearch() {
		long startTimeMillis = System.currentTimeMillis();
		int maxDepth = 0;
		Node closestNode = null;
		while ((closestNode == null || closestNode.value != this.targetValue) && System.currentTimeMillis() - startTimeMillis < timeLimit * 1000) {
			maxDepth++;
			closestNode = depthLimitedSearch(maxDepth, new Node(startValue, null, null), startTimeMillis);
		}

		Stack<String> path = new Stack<String>();
		Node currentNode = closestNode;
		while (currentNode.parent != null) {
			path.push(currentNode.parent.value + " " + currentNode.operation + " = " + currentNode.value);
			currentNode = currentNode.parent;
		}
		while (!path.empty()) {
			System.out.println(path.pop());
		}	
	}

	private Node depthLimitedSearch(int maxDepth, Node node, long startTimeMillis) {
		boolean done = (
			node.value == this.targetValue || 
			node.depth >= maxDepth || 
			System.currentTimeMillis() - startTimeMillis >= timeLimit * 1000
		);
		if (done) return node;

		node.expand(this.operations);
		Node closestNode = null;
		int closestNodeDiff = Integer.MAX_VALUE;
		for (Node child : node.children) {
			Node c = depthLimitedSearch(maxDepth, child, startTimeMillis);
			int cDiff = Math.abs(c.value - this.targetValue);
			if (cDiff < closestNodeDiff) {
				closestNode = c;
				closestNodeDiff = cDiff;
			}
		}
		node.collapse();

		return closestNodeDiff < Math.abs(node.value - this.targetValue) ? closestNode : node;
	}
}
	