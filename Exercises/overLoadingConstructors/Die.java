/*
Name: Michael Zbrozek
Date: 2/16/2024
Purpose: Create an overloaded constructor use it to create Player1 and Player 2 Dice
Sources:
Brightspace video
	

Files: 
Main.Java
Game.java
Die.java

*/

import java.security.SecureRandom;

/* The difference between 
java.security.SecureRandom
and
java.util.Random
is described here
https://www.baeldung.com/java-secure-random
*/
public class Die {
	private SecureRandom rand = new SecureRandom();
	private int sides;

	// TODO: Create an overloaded constructor for the Die that takes no arguments
	// and creates a 6 sided Die by default.

	public Die() {
		// rand.setSeed(System.currentTimeMillis());
		// this.sides = 6;
		this(6);
	}

	public Die(int sides) {
		rand.setSeed(System.currentTimeMillis());
		this.sides = sides;
	}

	public int roll() {
		/*
		 * This part...
		 * rand.nextInt() & Integer.MAX_VALUE
		 * ...is needed to ensure that nextInt
		 * returns a positive value.
		 * 
		 * MAX_VALUE looks something like this
		 * in memory:
		 * 01111111111111111111111111111111111
		 * so logically anding another integer
		 * with it preserves all of the original
		 * integer's 1 bits except for the
		 * negative bit.
		 */
		return ((rand.nextInt() & Integer.MAX_VALUE) % this.sides) + 1;
	}
}