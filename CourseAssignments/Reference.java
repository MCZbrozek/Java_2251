import java.util.Scanner;

public class Reference
{

	public static void main(String[] args)
	{
		// ===PRINTING===
		System.out.println("Automatically put a new line at the end of this text.");
		
		System.out.print("No new line at the end of this text.");
		
		System.out.println("Here are some special characters!\nA quotation mark: \"\n These\twords\tare\ttab\tseparated.\n And of course backslash n is used throughout for new lines.");
		
		
		// ===PRINTF===
		double pi = 3.14159;
		System.out.printf("This is pi, 8 total spaces wide, and to decimal digits displayed: %8.2f. Got it?\n", pi);
		
		System.out.printf("Here's pi 12 total spaces wide and 3 decimal digits: %12.3f. Got it?\n", pi);
		
		
		// ===VARIABLES===
		/* Variables come in a variety
		of types. The type must be
		declared before the variable
		name. */
		boolean greaterThan = true;
		int height = 62;
		double radius = 40.98;
		char letter = 'y';
		String phrase = "Veni, vidi, vici!";
		
		int taller = height + 1;
		height = height + 1;
		taller = 724;
		
		
		// ===INPUT===
		Scanner input = new Scanner(System.in);

		System.out.print("Enter circumference of sphere: ");
		double circumference = input.nextDouble();
		System.out.printf("Volume is %f%n", circumference);
		
		//This next line flushes a newline
		//character out of the input buffer.
		//It is necessary to use after a
		//call to nextInt or nextDouble.
		input.nextLine();
		
		System.out.print("Enter your full name: ");
		String full_name = input.nextLine();
		System.out.println("Pleased to meet you, "+full_name);
		
		
		// ===BRANCHING===
		boolean rainy = false;
		if(rainy)
		{
			System.out.println("Get an umbrella.");
		}
		
		if(rainy)
		{
			System.out.println("Get an umbrella.");
		}
		else
		{
			System.out.println("No umbrella.");
		}
		
		int temperature = 65;
		if(temperature < 55)
		{
			System.out.println("Put on a coat.");
		}
		else if(temperature < 80)
		{
			System.out.println("Feels nice.");
		}
		
		
		// ===LOOPS===
		int[] values = {7, 23, 567, 2, 6, 7};
		System.out.printf("%8s %12s\n", "Index", "Value");
		for(int i=0; i<values.length; i++)
		{
			System.out.printf("%8d %12d\n", i, values[i]);
		}
		
		System.out.println("Should we stop?");
		String response = input.nextLine();
		while(! (response.equals("yes") || response.equals("Yes")) )
		{
			System.out.println("Should we stop?");
			response = input.nextLine();
		}
		
		
		// ===ARRAYS===
		int[] myNewArray = {0,3,1,3,9,9,0,4,67,5};
		for(int i=0; i<myNewArray.length; i++)
		{
			System.out.print(myNewArray[i]+", ");
		}
		
		int[] alternatingRepeats = new int[200];
		int sign = 1;
		for(int i=0; i<100; i++)
		{
			alternatingRepeats[i] = sign;
			alternatingRepeats[i+100] = 2*sign;
			sign = sign * -1;
		}
		System.out.println(); //Print a new line to end the section
		

		// ===STATIC METHODS===
		System.out.print("Enter the radius of a sphere: ");

		radius = input.nextDouble();
		System.out.printf("Radius is %f%n", radius);

		//See below for the code of the
		//sphereVolume method.
		double v = sphereVolume(radius);
		System.out.printf("Volume is %f%n", v);
		
		//See below for the code of the
		//selectOne method.
		int result = selectOne(greaterThan, height, taller);
		System.out.println("selectOne selected "+result);
	}


	// calculate and return sphere volume
	public static double sphereVolume(double radius)
	{
		double volume = (4.0 / 3.0) * Math.PI * Math.pow(radius, 3);
		return volume;
	} // end method sphereVolume
	
	
	// Return one of the two inputs
	public static int selectOne(boolean condition, int x, int y)
	{
		if(condition)
		{
			return x;
		}
		return y;
	} // end method selector
	
}
