//https://docs.oracle.com/javase/8/docs/api/java/util/Random.html#nextInt-int-
import java.util.Random;

//https://www.geeksforgeeks.org/java-math-abs-method-examples/
import java.lang.Math;

public class GuessingGame
{
	private static final Random RNG = new Random();
	private int secretNumber;
	private int mostRecentGuess = Integer.MAX_VALUE;
	
	public GuessingGame()
	{
		//Pick a number between 1 and 100 inclusive
		secretNumber = RNG.nextInt(100) +1;
	}

	public String submitAGuess(int guess)
	{
		if(guess == secretNumber)
		{
			return "Correct";
		}
		int diffNew = getDifference(guess, secretNumber);
		int diffOld = getDifference(mostRecentGuess, secretNumber);
		/* TODO: Write an if else. If diffNew is
		less than diffOld return "Warmer", othewise
		return "Colder". Delete return ""; */
		return ""; //Placeholder
	}
	
	private int getDifference(int a, int b)
	{
		return Math.abs(a - b);
	}
}
