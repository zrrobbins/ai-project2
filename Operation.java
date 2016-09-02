/**
 * Operation.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

public class Operation {
	Operator type; // If root node, this should be null
	double value;  // Value of node

	public Operation(Operator type, double value) {
		this.type = type;  
		this.value = value;
	}
}
