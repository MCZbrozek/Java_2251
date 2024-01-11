class QuicksortThread extends Thread
{
	private DateValue[] arr;
	private int low;
	private int high;
	
	public QuicksortThread(DateValue[] arr, int low, int high)
	{
		this.arr = arr;
		this.low = low;
		this.high = high;
	}
	
	@Override
	public void run()
	{
		if(high <= low)
			return;
		
		DateValue pivot = arr[high];
		int i = (low-1); // index of smaller element
		for (int j=low; j<high; j++)
		{
			// If current element is smaller than or 
			// equal to pivot
			if (arr[j].compareTo(pivot) <= 0)
			{
				i++;
				// swap arr[i] and arr[j] 
				DateValue temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}

		// swap arr[i+1] and arr[high] (or pivot) 
		DateValue temp = arr[i+1];
		arr[i+1] = arr[high];
		arr[high] = temp;


		//Recursively sort each half
		
		//Pass data to sort to thread and run it
		QuicksortThread lower_half = new QuicksortThread(arr, low, i);
		QuicksortThread upper_half = new QuicksortThread(arr, i+1, high);

		lower_half.start();
		upper_half.start();

		try{
			lower_half.join();
			upper_half.join();
		}catch(InterruptedException e){
			System.out.println(e);
		}
		
	}
}