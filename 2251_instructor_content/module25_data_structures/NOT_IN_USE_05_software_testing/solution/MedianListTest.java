import java.util.ArrayList;

public class MedianListTest
{
	public static void main(String[] args)
	{
		assert testCorrectMedian() : "Failed test correct median";
		assert testCorrectString() : "Failed test correct string";
	}

	public static boolean isMedian(int x, ArrayList<Integer> values)
	{
		int count_copies = 0; //How many instances of x are in values?
		int count_below_x = 0;
		int count_above_x = 0;

		for(int i=0; i<values.size(); i++)
		{
			if(values.get(i) == x)
				count_copies++;
			else if(values.get(i) < x)
				count_below_x++;
			else
				count_above_x++;
		}
		return Math.abs(count_below_x - count_above_x) <= count_copies;
	}

	// Test method for finding the expected median
	public static boolean testCorrectMedian()
	{
		MedianList myList = new MedianList();

		try {
			myList.push(1);		// median 1
			myList.push(345);	// median 345
			myList.pop();		// median 1
			myList.push(562);	// median 562
			myList.push(95);	// median 95
			myList.pop();		// median 562
			myList.push(474);	// median 474
			myList.pop();		// median 562
			myList.push(6);		// median 6
			myList.push(100);	// median 100
		} catch(Exception e) {
			e.printStackTrace();
		}
		return (isMedian(100, myList.getList()));
	}

	// Test method for checking the expected output from MedianList.toString()
	public static boolean testCorrectString()
	{
		String expectedString = "";
		MedianList myList = new MedianList();

		try {
			myList.push(13);	// 13
			myList.push(315);	// 13,315
			myList.push(50);	// 13,315,50
			myList.push(963);	// 13,315,50,963
			myList.pop();		// (315) -> 13,50,963
			myList.pop();		// (50) -> 13,963
			myList.push(566);	// 13,963,566
			myList.push(474);	// 13,963,566,474
			myList.pop();		// (566) -> 13,963,474
			myList.push(9087);	// 13,963,474,9087
			myList.push(100);	// 13,963,474,9087,100

			expectedString = "13,963,474,9087,100";
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return expectedString.equals(myList.toString());
	}

}
