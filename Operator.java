/**
 * Operator.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

public enum Operator {
	ADD('+'), SUBTRACT('-'), MULTIPLY('*'), DIVIDE('/'), POWER('^');

	public final char symbol;

	private Operator(char symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return this.symbol + "";
	}
} 
