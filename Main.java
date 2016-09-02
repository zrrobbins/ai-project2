/**
 * Main.java
 *
 * CS4341: Project 1
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */
//SearchMethod is True for Iterative and false for Greedy
//Set to False by Defauls 
Boolean searchMethod = new Boolean();
searchMethod = false;
public class Main {
	public static void main(String[] args){
		// file reading + parsing
		//	return a search object
		/** File Parse
		 * @param args the information from the command line
		 * @return a search object parsed from the file 
		 */

		Search fileParse(String[] args){
			FileInputStream stream = new FileInputStream();
			Operation() operations = new Operation();
			double startValue = new double();
			double targetValue = new double();
			double timeValue = new double(); 
			stream = stream.split("\n");
			for( int i = 0, i < args.length(), i++){
				if (stream[i].contains("iterative")){
					searchMethod = true;
				}
				else if( i <=4){
					//parse the startvalue
					startValue = Integer.parseInt(stream[2]);
					//parse the targetvalue
					targetValue = Integer.parseInt(stream[3]);
					//parse the timevalue
					timeValue = Integer.parseInt(stream[4]);
				}

				else if (stream[i].contrains(" ")){
					operations[i] = stream[i];
				}
			}

			
		}
		// choose a search method

		// give output in correct format 
		// 	this probably should happen inside the performSearch method to a degree

		// make two implementations of Search (greedy and iterative)

		// make tests
	}
}
