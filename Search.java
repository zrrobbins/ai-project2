public abstract class Search {

	double startValue;
	double targetValue;
	double timeLimit;
	Operation[] operations;

	Search(double startValue, double targetValue, double timeLimit, Operation[] operations) {
		this.startValue = startValue;
		this.targetValue = targetValue;
		this.timeLimit = timeLimit;
		this.operations = operations;
	}

	public abstract Operation[] performSearch();
}