/**
 * Operation.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

public class Operation {
	public final Operator type;
	public final double value;

	public Operation(Operator type, double value) {
		this.type = type;  
		this.value = value;
	}

	public double applyTo(double operand) {
		switch (this.type) {
		case ADD: return operand + this.value;
		case SUBTRACT: return operand - this.value;
		case MULTIPLY: return operand * this.value;
		case DIVIDE: return operand / this.value;
		case POWER: return Math.pow(operand, this.value);
		}
		throw new IllegalStateException("bad operator: " + this.type);
	}
}
