import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		//Arithmetic calculator = new Arithmetic();
		
		Arithmetic int_calculator = new IntegerArithmetic();
		Arithmetic clock_calculator = new ClockArithmetic();
		
		System.out.println("Enter two numbers:");
		int x = input.nextInt();
		int y = input.nextInt();
		
		System.out.println("The integer sum of "+x+" and "+y+" is ");
		System.out.println(int_calculator.add(x,y));


		System.out.println("The clock sum of "+x+" and "+y+" is ");
		System.out.println(clock_calculator.add(x,y));
		
		
		
		Arithmetic[] calculators = {int_calculator, clock_calculator};
		for(Arithmetic a : calculators)
		{
			System.out.println("Enter two numbers:");
			x = input.nextInt();
			y = input.nextInt();
			
			System.out.println("The difference between "+x+" and "+y+" is ");
			System.out.println(a.subtract(x,y));
		}
	}
}