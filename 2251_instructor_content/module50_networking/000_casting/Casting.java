/*
Casting

What is it? What is it good for?

Converting from one type to another type!
*/
public class Casting
{
	public static void main(String[] args)
	{
		//What if I want only the decimal 
		//part of a number, what can I do?
		double d = 23.8775; //I just want 0.8775
		
		//Solution:
		//Casting to int removes the decimal
		int temp = (int)d;
		//Then subtract the whole number part
		//from the original to get the decimal
		double result = d - temp;
		System.out.println(result);
		
		
		System.out.println("\n=====================\n");
		
		
		//place your bets, what prints out in this example?
		int x = 10;
		int y = 7;
		result = x/y;
		System.out.println(result);
		
		//How can I get a more accurate result to print?
		//Solution: cast either x or y to double.
		//The cast happens before division.
		result = (double)x/y;
		System.out.println(result);
		
		
		
		System.out.println("\n=====================\n");
		
		
		//Polymorphism example!
		//String object
		String name = new String("James");
		//Integer object
		Integer number = new Integer(x);
		//Casting object
		Casting c = new Casting();
		
		//All these objects can be put in the
		//same array if the array is type Object!
		Object[] stuff = new Object[3];
		stuff[0] = name;
		stuff[1] = number;
		stuff[2] = c;
		//They will be printed based on their
		//custom toString methods
		for(Object my_object : stuff)
			System.out.println(my_object);
		// instanceof command can be used to 
		//identify the "true" type of a variable.
		String new_string = "";
		if(stuff[0] instanceof String) {
			new_string = (String)stuff[0];
		}
		System.out.println(new_string.length());

		// stuff[0] is cast as an object so
		// We cannot ask it its length
		//System.out.println(stuff[0].length());
		
		// Unless it is recast as a String:
		System.out.println( ((String)stuff[0]).length() );
	}
}