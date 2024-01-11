public class HailstoneSolution
{

	public static void main(String[] args)
	{
		/* In this example I'm going to write a
		method to compute the length of the
		hailstone sequence for every positive
		whole number from 1 through 50. */
		System.out.printf("%3s %10s\n", "Value", "Steps");
		for(int i=1; i<=50; i++)
		{
			int length = hailstoneLength(i);
			System.out.printf("%3d %10d\n", i, length);
		}
	} // end of main

	//A method to calculate hailstone
	//sequence length
	public static int hailstoneLength(int x)
	{
		int length = 0;
		while(x != 1) //until x == 1
		{
			//System.out.println(x);
			if(x%2==0) //if x is even
			{
				x = x/2; //integer division
			}
			else //x is odd
			{
				x = 3*x + 1;
			}
			length = length + 1; //length += 1;
		} // end while
		//System.out.println(x);
		return length;
	} // end hailstoneLength

} //end of class
