import java.util.ArrayList;

public class Main
{
	public static void main(String[] args)
	{
		//Source for the following:
		//w3schools.com/java/java_arraylist.asp
		ArrayList<String> cars = new ArrayList<String>();
		//As values are added, the ArrayList 
		//expands to the necessary size 
		//automatically.
		cars.add("Volvo");
		cars.add("BMW");
		cars.add("Ford");
		cars.add("Mazda");
		//ArrayList length is accessed
		//with .size() unlike traditional
		//arrays which use .length
		for(int i=0; i<cars.size(); i++)
		{
			//ArrayList values are accessed
			//with get(index) where standard
			//arrays would use [index]
			System.out.println(cars.get(i));
		}
		System.out.println();
		//Alternatively you can loop through
		//with "for each" loops
		for (String car : cars)
		{
			System.out.println(i);
		}
		
		//More ArrayList examples can be
		//found throughout this page:
		// geeksforgeeks.org/arraylist-in-java/

	}
}