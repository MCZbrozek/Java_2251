import java.util.Scanner; // Import the Scanner class

public class ScannerExample
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);	// Create a Scanner object

		//Prompt
		System.out.println("Enter username");

		String userName = input.nextLine();	// Read user input
		System.out.println("Username is: " + userName);	// Output user input

		String pwd = getPassword(input);
		System.out.println("Password is: *******");	// Output user input
		
		System.out.println("Zip code: ");
		int zip = input.nextInt();
		System.out.println("Your zip is "+zip);
		
		input.close();
	}
	
	public static String getPassword(Scanner in)
	{
		System.out.println("Enter password");
		return in.next();	// Read user input
	}
}