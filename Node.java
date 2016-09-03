/**
 * Node.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

public class Node {
	Operation operation;
	Node parent;	   // Parent node, NULL if this is root
	Node[] children;   // Children, determined by operators list from parsing

	public Node(Operation operation, Node parent, Node[] children) {
		this.operation = operation;
		this.parent = parent;
		this.children = children;
	}
}
