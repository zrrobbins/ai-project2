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
		int maxDepth = 1;
		boolean isDone = false;
		while(!isDone && System.currentTimeMillis() - startTimeMillis < timeLimit * 1000){
			isDone = depthLimitedSearch(maxDepth, startValue, targetValue, timeLimit, operations, startTimeMillis);
			maxDepth++;
		}
	}

	//A depth limited search that will search until it reaches the maximum depth for all branches
	private boolean depthLimitedSearch(int maxDepth, int startValue, int targetValue, double timeLimit, Operation[] operations, long startTime ){
		Node startNode = new Node(startValue, null, null);
		Node currentNode = startNode;
		int localDepth = 0;

		//Metrics initialization

		//The frontier is the the stack for nodes to explore
		Stack<Node> frontier = new Stack<Node>();

		frontier.push(startNode);

		//Runs a while loop until the frontier is exhausted
		while(!frontier.isEmpty() && (System.currentTimeMillis() - startTime < timeLimit * 1000) ){
			//If we reach the target node, then we print out the path from the start node to the target
			if(currentNode.value == targetValue){
				System.out.println("We're done!");
				Stack<String> path = new Stack<String>();

				while (currentNode != startNode) {
				path.push(currentNode.parent.value + " " + currentNode.operation + " = " + currentNode.value);
				currentNode = currentNode.parent;
				}

				while (!path.empty()) {
				System.out.println(path.pop());
				}	

				return true;
			}
			//This function checks how deep the current node is. If it is deeper than the max depth then we simply dont expand any further and pop the next node off the frontier
			int currentDepth = countPathDepth(currentNode, startNode);
			if(currentDepth <= maxDepth){
				currentNode.expand(operations);
				for(Node node: currentNode.children){
					frontier.push(node);
				}
				currentNode = frontier.pop();
			}
			else{
				currentNode = frontier.pop();
			}
		}
		return false;
	}

	//Returns the depth of the current node
	private int countPathDepth(Node thisNode, Node start){
		int pathDepth = 0;
		while (thisNode != start) {
			pathDepth++;
			thisNode = thisNode.parent;
		}
		return pathDepth;
	}

}

	