/**
 * Search.java
 *
 * CS4341: Project 2
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

public abstract class Search {

	double startValue;
	double targetValue;
	double timeLimit;
	Operation[] operations;

	public Search(double startValue, double targetValue, double timeLimit, Operation[] operations) {
		this.startValue = startValue;
		this.targetValue = targetValue;
		this.timeLimit = timeLimit;
		this.operations = operations;
	}
	
	public abstract void performSearch();
}
