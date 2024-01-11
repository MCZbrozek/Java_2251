import java.util.Scanner;

public class Stage02Main //Top Down - framework
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		int selection;
		
		do {
			System.out.printf("Enter 1 to convert decimal to binary\n");
			System.out.printf("      2 to convert binary to decimal\n");
			selection = input.nextInt();
			if (selection == 1) {
				/* convert decimal to binary */
				System.out.println("\nconvert decimal to binary\n");
			} else if (selection == 2) {
				/* convert binary to decimal */
				System.out.println("\nconvert binary to decimal\n");
			}
		} while(selection==1 || selection==2);
		
		System.out.printf("Bye Bye!\n");
		input.close();
	}

}
