/**
 * Organism.java
 *
 * CS4341: Project 2
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class Organism {
	public final List<Operation> operations;
	public final int numOperations;
	public final double resultValue;
	private static final Random rand = new Random();

	public Organism(List<Operation> operations, double startValue) {
		this.operations = operations;
		this.numOperations = operations.size();
		double res = startValue;
		for (Operation op : this.operations) {
			res = op.applyTo(res);
		}
		this.resultValue = res;
	}

	/**
	 * Returns a new organism with a mutation. The mutation is equally likely
	 * to be removing an operation, adding a new operation, or changing an
	 * operation.
	 *
	 * @param availableOps set of operations that can be used
	 * @param startValue   start value of equation. Required so that new
	 *                     Organism can calculate its resultValue
	 */
	public Organism mutate(Operation[] availableOps, double startValue) {
		List<Operation> newOps = new LinkedList<Operation>(this.operations);
		int i = rand.nextInt(this.numOperations);
		Operation newOp = availableOps[rand.nextInt(availableOps.length)];
		switch (rand.nextInt(3)) {
			case 0:
				if (this.numOperations > 1) {					
					newOps.remove(i);
					break;
				} else {
					// Fall-through to case 1!
				}
			case 1:
				newOps.add(i, newOp);
				break;
			case 2:
				newOps.set(i, newOp);
				break;
		}
		return new Organism(newOps, startValue);
	}
} 