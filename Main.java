/**
 * Main.java
 *
 * CS4341: Project 2
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

		} catch (IOException e) { // Catch failed file reading
			System.err.println("failed to load file: " + e.getMessage());
			return null;
		}
		//do not accept files without a minimum length of 5
		if (lines.length < 5) return null;

		// 0 = iterative
		// 1 = greedy
		// 2 = genetic
		int searchType;


		//check whether file should be solved with iterative or greedy search algorithm
		if (lines[0].equals("iterative")) { //run iterative search
			searchType = 0;
		} else if (lines[0].equals("greedy")) { //run greedy search
			searchType = 1;
		} else if (lines[0].equals("genetic")) { //run greedy search
			searchType = 2;
		} else { //catch if there is not a specified algorithm
			System.err.println("expected iterative, greedy, or genetic, got " + lines[0]);
			return null;
		}

		double startValue, targetValue;
		double timeLimit;
		try {
			startValue = Double.parseDouble(lines[1]); // catch the startint targetvalue and timelimit from the friest few lines
			targetValue = Double.parseDouble(lines[2]);
			timeLimit = Double.parseDouble(lines[3]);
		} catch (NumberFormatException e) { //catch issues with the initial numbers
			System.err.println("either start value, target value, or time limit was not a valid number"); 
			return null;
		}

		ArrayList<Operation> ops = new ArrayList<Operation>(); //make an arraylist for the given operations
		for (int i = 4; i < lines.length; i++) {
			if (lines[i].length() == 0) continue;
			char opChar = lines[i].charAt(0); //find the character at the start of the line
			Operator op;
			switch (opChar) {  //filter the character into the different operations
			case '+': op = Operator.ADD; break;
			case '-': op = Operator.SUBTRACT; break;
			case '*': op = Operator.MULTIPLY; break;
			case '/': op = Operator.DIVIDE; break;
			case '^': op = Operator.POWER; break;
			default:  //else not a proper operation
				System.err.println("invalid operator: " + opChar);
				return null;
			}

			String opNumString = lines[i].substring(2); //cut to the number of each line

			try {
				ops.add(new Operation(op, Double.parseDouble(opNumString))); //add the numbers to their respective operations
			} catch (NumberFormatException e) { //catch number issues
				System.err.println("failed to parse operation number: " + opNumString);
				return null;
			}
		}
		if (ops.size() == 0) { //must have at least one operation to be solved
			System.err.println("must specify at least one operation");
			return null;
		}
		Operation[] opsArray = ops.toArray(new Operation[ops.size()]); //create the array of operations from what has been parsed

		// Return respective search type
		switch(searchType) {
			case 0:
				return new IterativeDeepeningSearch(startValue, targetValue, timeLimit, opsArray);
			case 1:
				return new GreedySearch(startValue, targetValue, timeLimit, opsArray);
			case 2:
				return new GeneticSearch(startValue, targetValue, timeLimit, opsArray);
			default:
				throw new IllegalStateException("Invalid search type!");
		}
	}

	//Check the command line for the command to be run and make sure it is run properly
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
		//begin running the search
		search.performSearch();
	}
}
