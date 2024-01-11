import java.util.Scanner;
/* Find the square root. */
public class SquareRoot
{	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		System.out.print("Give me a number: ");
		int x = input.nextInt(); //Trying to find square root of this number
		
		double guess = 2; //Guess of square root
		
		double square = guess*guess;
		double diff = Math.abs(square - x);
		System.out.println("diff = "+diff+" square = "+square);
	
		double close_enough = .001;
		
		while(diff > close_enough)
		{
			guess = (guess+x/guess)/2.0; //adjustment to our guess
			//Feedback on how well we are doing:
			System.out.println("g = "+guess);
			square = guess*guess;
			diff = Math.abs(square - x);
			System.out.println("diff = "+diff+" square = "+square);
		}
		System.out.println("Square root estimate is: "+guess);
	}
}
