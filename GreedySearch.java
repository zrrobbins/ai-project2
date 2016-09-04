/**
 * GreedySearch.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

import java.util.Comparator;
import java.util.Stack;
import java.util.PriorityQueue;

public class GreedySearch extends Search {

	public GreedySearch(double startValue, double targetValue, double timeLimit, Operation[] operations) {
		super(startValue, targetValue, timeLimit, operations);
	}

	@Override
	public void performSearch() {
		long startTimeMillis = System.currentTimeMillis();
		Node root = new Node(this.startValue, null, null);
		Node curr = root;

		PriorityQueue<Node> queue = new PriorityQueue<Node>(64, new GreedySearchComparator(this.targetValue));
		while (
			curr.value != targetValue && 
			System.currentTimeMillis() - startTimeMillis < timeLimit * 1000
		) {
			curr.expand(this.operations);
			for (Node n : curr.children) {
				queue.add(n);
			}
			curr = queue.poll();
		}

		Stack<String> steps = new Stack<String>();
		while (curr != root) {
			steps.push(curr.parent.value + " " + curr.operation + " = " + curr.value);
			curr = curr.parent;
		}
		while (!steps.empty()) {
			System.out.println(steps.pop());
		}		

		// TODO: give more detailed output
	}

	private static class GreedySearchComparator implements Comparator<Node> {

		public final double targetValue;

		public GreedySearchComparator(double targetValue) {
			this.targetValue = targetValue;
		}

		@Override
		public int compare(Node a, Node b) {
			double aDiff = Math.abs(a.value - this.targetValue);
			double bDiff = Math.abs(b.value - this.targetValue);
			return aDiff == bDiff ? 0  :
			       aDiff <  bDiff ? -1 : 1;
		}
	}
}
