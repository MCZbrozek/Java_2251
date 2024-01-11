import java.security.SecureRandom;
import java.util.Scanner;

public class Pig
{
	private int player1_score = 0;
	private int player2_score = 0;
	private boolean is_player1s_turn = true;
	private SecureRandom die = new SecureRandom();
	private int score_limit;
	private Scanner user_input = new Scanner(System.in);
	
	/* TODO: Write a constructor that takes an int
	as input and sets the score_limit instance 
	variable equal to the input. */

	
	public void play()
	{
		//until a player reaches the score_limit
		while( true ) //TODO: Fix this once the gameOver method has been written.
		{
			//current player plays
			//they get their points
			if(is_player1s_turn)
			{
				System.out.println("\nPlayer 1's points: "+player1_score);
				System.out.println("Player 1 go:");
				player1_score += takeTurn();
			}
			else
			{
				System.out.println("\nPlayer 2's points: "+player2_score);
				System.out.println("Player 2 go:");
				player2_score += takeTurn();
			}
			
			//then we switch players
			is_player1s_turn = !is_player1s_turn;
		}
		
		/* TODO: We need to declare who won the game.
		Write an if-else to do so. */
		
		System.out.println("Game over with player 1's score");
		System.out.println(player1_score);
	}
	
	/* takeTurn returns the number of points earned by
	the current player. */
	private int takeTurn()
	{
		//immediately roll
		int points = 0;
		int most_recent_roll = rollDie();
		
		//until the player chooses hold or rolls 1
		//	they keep gaining points
		while(most_recent_roll != 1)
		{
			//Accumulate points
			points = points + most_recent_roll;
			
			System.out.println("Rolled: "+most_recent_roll+". At risk points: "+points);

			//player chooses to hold or roll
			System.out.println("roll or hold?");
			String choice = user_input.nextLine();
			if(choice.equals("hold"))
			{
				break;
			}
			
			//Roll again
			most_recent_roll = rollDie();
		}
		
		//if they roll 1 the turn is over and they lose all points
		if(most_recent_roll == 1){ points = 0; }
		
		System.out.println("Turn is over.\nRolled: "+most_recent_roll+". Points gained: "+points);

		return points;
	}
	
	/* TODO: Write a private method named gameOver
	that uses the instance variables player1_score,
	player2_score, and score_limit to determine if
	the game is finished or not. The method should
	return true or false. */


	private int rollDie()
	{
		//Returns a random number between 1 and 6 inclusive
		return die.nextInt(6)+1;
	}
	
	public void testRandomNumbers()
	{
		int upperBound = 6;
		int randomInt = rollDie();
		for(int i=0; i<100; i++)
		{
			randomInt = rollDie();
			System.out.println(randomInt);
		}
	}
}