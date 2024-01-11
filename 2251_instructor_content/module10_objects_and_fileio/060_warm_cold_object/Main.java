public class Main
{
	public static void main(String[] args) //starting point
	{
		GuessingGame game = new GuessingGame();
		
		/* TODO: This version of the program 
		is inefficient. It simply counts up
		from 1. Can you make it more efficient?
		A basic first step is to start at 50
		and use an if statement to count up or
		down depending on whether 51 is "Warmer"
		or "Colder", but there are other ways. */
		int guess = 1;
		String result = game.submitAGuess(guess);
		while(!result.equals("Correct"))
		{
			System.out.println("You guessed "+guess);
			//Try again
			guess++;
			result = game.submitAGuess(guess);
		}
		System.out.println("You got it! The secret number was "+guess);
	}
}