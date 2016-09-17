/**
 * ParentPair.java
 *
 * CS4341: Project 2
 * Group: Zachary Robbins, Kyle McCormick, Elijah Gonzalez, Peter Raspe
 */

import java.util.List; 

public class ParentPair{
	public final Organism parentOne;
	public final Organism parentTwo;

	public ParentPair(Organism organismOne, Organism organismTwo){
		this.parentOne = organismOne;
		this.parentTwo = organismTwo;
	}
}