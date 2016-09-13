/**
 * Node.java
 *
 * CS4341: Project 2
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

public class Node {

	public final double value;
	public final Operation operation; // NULL if this is root
	public final Node parent;	      // Parent node, NULL if this is root
	public final int depth;
	public Node[] children;           // Children, determined by operators list from parsing.
	                                  //  NULL if not expanded

	public Node(double value, Operation operation, Node parent) {
		this.value = value;
		this.operation = operation;
		this.parent = parent;
		this.depth = parent == null ? 0 : parent.depth + 1;
		this.children = null;
	}
	/**
	 * Expands the given node of operation, giving it children
	 * @param the node operations to be expanded into atree
	 */
	public void expand(Operation[] operations) {
		this.children = new Node[operations.length];
		for (int i = 0; i < operations.length; i++) {
			this.children[i] = new Node(operations[i].applyTo(this.value), operations[i], this);
		}
	}

	/** 
	 * Set node's references to its children to NULL, allowing them to be garbage collected
	 * 
	 */
	public void collapse() {
		for (int i = 0; i < children.length; i++) {
			this.children[i] = null;
		}
		this.children = null;
	}
}
