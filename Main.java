/**
 * Main.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

public class Main {

	/** parseFile
	 * @param fpath the path to the configuration file
	 * @return a search object parsed from the file or null if the file is malformed
	 */
 	private static Search parseFile(String fpath) {

		String[] lines;
		try {
			List<String> linesList = Files.readAllLines(Paths.get(fpath), StandardCharsets.UTF_8);
			lines = linesList.toArray(new String[linesList.size()]);
		} catch (IOException e) {
			System.err.println("failed to load file: " + e.getMessage());
			return null;
		}
		if (lines.length < 5) return null;

		boolean iterative;
		if (lines[0].equals("iterative")) {
			iterative = true;
		} else if (lines[0].equals("greedy")) {
			iterative = false;
		} else {
			System.err.println("expected iterative or greedy, got " + lines[0]);
			return null;
		}

		int startValue, targetValue;
		double timeLimit;
		try {
			startValue = Integer.parseInt(lines[1]);
			targetValue = Integer.parseInt(lines[2]);
			timeLimit = Double.parseDouble(lines[3]);
		} catch (NumberFormatException e) {
			System.err.println("either start value, target value, or time limit was not a valid number");
			return null;
		}

		ArrayList<Operation> ops = new ArrayList<Operation>();
		for (int i = 4; i < lines.length; i++) {
			if (lines[i].length() == 0) continue;
			char opChar = lines[i].charAt(0);
			Operator op;
			switch (opChar) {  
			case '+': op = Operator.ADD; break;
			case '-': op = Operator.SUBTRACT; break;
			case '*': op = Operator.MULTIPLY; break;
			case '/': op = Operator.DIVIDE; break;
			case '^': op = Operator.POWER; break;
			default: 
				System.err.println("invalid operator: " + opChar);
				return null;
			}
			String opNumString = lines[i].substring(2);
			try {
				ops.add(new Operation(op, Integer.parseInt(opNumString)));
			} catch (NumberFormatException e) {
				System.err.println("failed to parse operation number: " + opNumString);
				return null;
			}
		}
		if (ops.size() == 0) {
			System.err.println("must specify at least one operation");
			return null;
		}
		Operation[] opsArray = ops.toArray(new Operation[ops.size()]);

		return iterative ?
			new IterativeDeepeningSearch(startValue, targetValue, timeLimit, opsArray) :
			new GreedySearch(startValue, targetValue, timeLimit, opsArray);
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("error: expected one argument: path to configuration file");
			return;
		}

		Search search = parseFile(args[0]);
		if (search == null) {
			System.err.println("error: configuration file either non-existent or incorrectly formatted");
			return;
		}

		search.performSearch();
	}
}
