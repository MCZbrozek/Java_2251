
public class Main
{
	public static void main(String[] args)
	{
		//Create an empty array
		Vector my_vector = new Vector(0);
		
		int points = 0;
		
		//Try to add to the empty array!
		my_vector.add("ferret");

		if(!my_vector.isEmpty()){ points++; }
		else{ System.out.println("Is empty now? Should be false: "+Boolean.toString(my_vector.isEmpty())); }
		
		System.out.print(points);
		System.out.println(" / 1");
		
		System.exit(0); //TODO. next year automate the grading.
		
		System.out.println("Size? Should be 1: "+Integer.toString(my_vector.size()));
		
		System.out.println("First element is? Should be ferret: "+((String)my_vector.get(2)));

		//Ok, make the array empty again.
		//Test the other constructor 
		my_vector = new Vector();

		System.out.println("Is empty? Should be true: "+Boolean.toString(my_vector.isEmpty()));

		//Add 4 animals to the vector.
		my_vector.add("cat");
		my_vector.add("dog");
		my_vector.add("pig");
		my_vector.add("bird");
		
		System.out.println("Is empty now? Should be false: "+Boolean.toString(my_vector.isEmpty()));
		
		System.out.println("Size? Should be 4: "+Integer.toString(my_vector.size()));
		
		System.out.println("Third element is? Should be pig: "+((String)my_vector.get(2)));
		
		//Add 10 numbers to the vector.
		//This should trigger a size increase in the vector.
		for(int i=0; i<10; i++)
		{
			my_vector.add(Integer.toString(i));
		}

		System.out.println("Size? Should be 14: "+Integer.toString(my_vector.size()));
		
		System.out.println("Seventh element is? Should be 2: "+((String)my_vector.get(6)));

		//Set the seventh element to fish.
		my_vector.set(7, "fish");

		System.out.println("Size? Should be 14: "+Integer.toString(my_vector.size()));
		
		System.out.println("Seventh element is? Should be fish: "+((String)my_vector.get(6)));

		//Remove fish
		String my_pet = (String)my_vector.remove("fish");
		
		System.out.println("Element removed? Should be fish: "+my_pet);

		System.out.println("Size? Should be 13: "+Integer.toString(my_vector.size()));
		
		System.out.println("Seventh element is? Should be 4: "+((String)my_vector.get(6)));
		
		//Add 10 more numbers to the vector.
		//This should trigger another size increase in the vector.
		for(int i=10; i<20; i++)
		{
			my_vector.add(Integer.toString(i));
		}
		
		System.out.println("Fifteenth element is? Should be ???: "+((String)my_vector.get(6)));

		//Remove the first element
		my_pet = (String)my_vector.remove(0);

		System.out.println("Element removed? Should be cat: "+my_pet);
		
		System.out.println("Size? Should be 12: "+Integer.toString(my_vector.size()));
		
		System.out.println("Seventh element is? Should be 5: "+((String)my_vector.get(6)));

		//Print out the contents of the vector
		my_vector.print();
	}

}
