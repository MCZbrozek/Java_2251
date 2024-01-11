import java.util.Stack;
import java.lang.Math;
import java.util.Scanner;

public class Stage03Bin2Dec
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		int dec_num;
		String bin_num;
		/* convert binary to decimal */
		System.out.printf("Enter your binary number: ");
		bin_num = input.next();
		/* B2D your conversion code, print the result using printf()*/

		//Testing
		System.out.println("Read in "+bin_num);
		
		
		//If I had started here, then this would be an example of bottom-up design. Starting from the details.
		int x = 0;
		int exponent = 0;
		//Loop from least significant digits to most
		for(int i=bin_num.length()-1; i>=0; i--)
		{
			if(bin_num.charAt(i)=='1')
			{
				x += Math.pow(2,exponent);
			}
			exponent++;
		}
		System.out.println("Integer (decimal) representation: "+Integer.toString(x));
		
		
		input.close();
	}

}
