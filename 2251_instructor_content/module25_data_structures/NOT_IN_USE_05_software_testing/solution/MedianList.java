import java.util.ArrayList;
import java.util.Collections;

public class MedianList
{
	private ArrayList<Integer> values;

	// Constructor
	public MedianList()
	{
		values = new ArrayList<Integer>();
	}

	// Add another int value to end of array list
	public void push(int x)
	{
		final int oldMedian = peek();

		values.add(x);

		final int newMedian = peek();

		assert !isEmpty();
		if(x <= oldMedian)
		{
			assert newMedian <= oldMedian;
		}
		else if(x > oldMedian)
		{
			assert newMedian >= oldMedian;
		}
	}

	// Remove and return the median value
	public int pop() throws Exception
	{
		int medianValue = 0;

		if(isEmpty()) throw new Exception("Array is empty!");

		medianValue = peek();

		// Remove the first instance of median integer
		values.remove((Integer) medianValue); // Cast to Integer object to match value, not index #

		return medianValue;
	}

	// Reveal the next median value to be popped
	public int peek() throws Exception
	{
		if(isEmpty()) throw new Exception("Array is empty!");

		final ArrayList<Integer> temp = sort();

		// Find the median (or greater number from middle pair if even # of ints)
		int medianIndex = values.size() / 2;

		return temp.get(medianIndex);
	}

	public boolean isEmpty()
	{
		return values.isEmpty();
	}

	@Override
	public String toString()
	{
		String output = "";
		for(int value : values)
		{
			output += String.format("%d,", value);
		}
		//Trim the final comma if there is one
		if(output.length() > 0)
			output = output.substring(0, output.length() - 1);

		return output;
	}

	// Sorts alphabetically ascending
	private ArrayList<Integer> sort()
	{
		//Create a deep copy of the ArrayList
		ArrayList<Integer> temp = new ArrayList<Integer>(values);
		Collections.sort(temp);
		return temp;
	}
	
	public ArrayList<Integer> getList()
	{
		return values;
	}
}