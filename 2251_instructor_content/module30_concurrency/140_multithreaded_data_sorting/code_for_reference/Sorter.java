import java.util.concurrent.Callable;

public class Sorter implements Callable<Integer> {

	@Override
	public int[] call(int[] arr, int low, int high) throws Exception
	{	//Source: www.geeksforgeeks.org/java-program-for-quicksort/
		int pivot = arr[high];
		int i = (low-1); // index of smaller element
		for (int j=low; j<high; j++)
		{
			// If current element is smaller than or 
			// equal to pivot
			if (arr[j] <= pivot)
			{
				i++;
				// swap arr[i] and arr[j] 
				int temp = arr[i]; 
				arr[i] = arr[j]; 
				arr[j] = temp; 
			}
		}

		// swap arr[i+1] and arr[high] (or pivot) 
		int temp = arr[i+1];
		arr[i+1] = arr[high];
		arr[high] = temp;

		return new int[]{low, i+1, high};
	}

}