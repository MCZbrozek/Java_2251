import java.util.Stack;
import java.lang.Math;
import java.util.Scanner;

public class Stage03Dec2Bin
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		int dec_num;
		String bin_num;
		/* convert decimal to binary */
		System.out.printf("Enter your decimal number: ");
		dec_num = input.nextInt();
		/* D2B your conversion code, print the result using printf()*/


		//Testing
		System.out.println("Read in "+dec_num);
		
		
		
		//If I had started here, then this would be an example of bottom-up design. Starting from the details.
		String result = "";
		int remainder;
		while(dec_num>0)
		{
			remainder = dec_num%2; //a zero or one
			result = remainder + result;
			dec_num = dec_num/2;
		}
		System.out.println("Binary representation: "+result);
		
		
		
		input.close();
	}

}
