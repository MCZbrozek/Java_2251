import java.util.Scanner;
/*
This class will use a Scanner and a while loop to 
repeatedly ask the user for a sentence until the 
user types QUIT.

The TextAccumulator and TextVector classes are
both designed to store all of the sentences typed
in as well as a count of how many total sentences
were typed in.

The two classes have the same public facing 
interface but different internal mechanics.
*/
public class Main
{
	public static void main(String[] args)
	{
		TextAccumulator text = new TextAccumulator();
		//TextVector text = new TextVector();
		//Encapsulation
		
		
		Scanner user_input = new Scanner(System.in);
		
		String response = "";
		
		//repeatedly ask the user for a sentence until the user types QUIT
		while(!response.equals("QUIT"))
		{
			System.out.println("Gimme a sentence:");
			response = user_input.nextLine();
			System.out.println("Your sentence is "+response);
			/* TODO: Call the text variable's append
			method and pass to it the String response.
			Run the program to test that your code
			works. At the end, after typing QUIT,
			all the input should be printed by the
			last print statement in this file. */
			
		}
		
		System.out.print(text.getCount() + " Strings have been added. The complete current String is: ");
		
		System.out.println(text.getText());
	}
}

