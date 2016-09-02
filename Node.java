/**
 * Node.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

public class Node {
	Operator operator; 
	Node parent;	   // Parent node, NULL if this is root
	Node[] children;   // Children, determined by operators list from parsing

	public Node(Operator operator, Node parent, Node[] children) {
		this.operator = operator;
		this.parent = parent;
		this.children = children;
	}
}
