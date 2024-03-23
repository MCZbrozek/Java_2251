import java.util.Scanner;

public class Main
{
	public static final Scanner input = new Scanner(System.in);

	public static void main(String args[])
	{
		System.out.println("Think of an animal, vegetable, or mineral.\n");

		//Create starting question
		Question starting_question = new Question("Are you thinking of an animal?");

		String answer = "continue";
		while(answer.equals("continue"))
		{
			playOneRound(starting_question);
			System.out.println("Type 'continue' to keep playing or anything else to quit.");
			answer = input.nextLine();
		}
		input.close();
	}
	
	public static void playOneRound(Question question)
	{
		//Could make this a recursive method.
		
		int limit = 5; //Not actually 20 questions, at most 5.
		int count = 0;
		while(true)
		{
			//Ask first question
			question.ask();
			count++;
			//Get response
			String answer = input.nextLine();
			System.out.println("You said: "+answer);
			
			//Check for game over computer wins:
			if(question.isTerminatingQuestion() && answer.equals("yes"))
			{
				System.out.println("I guessed your thing correctly! I win.");
				break;
			}
			//Check for game over user wins:
			if(count > limit)
			{
				System.out.println("I have no clue! You win.");
				break;
			}
			
			//Get next question
			Question next = question.getNext(answer);
			if(next == null)
			{
				System.out.println("I'm stumped.");
				System.out.println("What question should I ask next?");
				String q = input.nextLine();
				question = question.setNextQuestion(q, answer);
				System.out.println();
			}
			else
			{
				question = next;
			}
		} //while(true)
	} //public static void playOneRound(Question question)
	
}
