import java.util.Stack;
import java.lang.Math;
import java.util.Scanner;

public class Stage04Integration
{
	private static final Scanner input = new Scanner(System.in);


	public static void main(String[] args)
	{
		int selection;
		
		do {
			System.out.printf("Enter 1 to convert decimal to binary\n");
			System.out.printf("      2 to convert binary to decimal\n");
			selection = input.nextInt();
			if (selection == 1) {
				/* convert decimal to binary */
				decimal2binary();
			} else if (selection == 2) {
				/* convert binary to decimal */
				binary2decimal();
			}
		} while(selection==1 || selection==2);
		
		System.out.printf("Bye Bye!\n");
		input.close();
	}

	private static void decimal2binary()
	{
		int dec_num;
		String bin_num;
		/* convert decimal to binary */
		System.out.printf("Enter your decimal number: ");
		dec_num = input.nextInt();
		/* D2B your conversion code, print the result using printf()*/
		String result = "";
		int remainder;
		while(dec_num>0)
		{
			remainder = dec_num%2; //a zero or one
			result = remainder + result;
			dec_num = dec_num/2;
		}
		System.out.println("Binary representation: "+result);
	}
	
	
	private static void binary2decimal()
	{
		int dec_num;
		String bin_num;
		/* convert binary to decimal */
		System.out.printf("Enter your binary number: ");
		bin_num = input.next();
		/* B2D your conversion code, print the result using printf()*/
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
		System.out.println("Integer (decimal) representation: "+Integer.toString(x)+"\n");
	}
}
