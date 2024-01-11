import java.util.Scanner; // Import the Scanner class

public class ScannerExample2
{
	private Scanner input;

	public ScannerExample2()
	{
		input = new Scanner(System.in);
	}
	
	public String getPassword()
	{
		System.out.println("Enter password");
		return input.next();	// Read user input
	}
	
	
	
	
	
	
	public static void main(String[] args)
	{
		ScannerExample2 example = new ScannerExample2();
		
		String pwd = example.getPassword();
		System.out.println("You typed in: "+pwd);
		
	}
	
}