public class PrimeFinder extends Thread
{
	private String threadName;
	
	//Constructor
	public PrimeFinder(String name)
	{
		threadName = name;
		System.out.println("Creating " +  threadName );
	}

	public void run()
	{
		System.out.println("Running " +  threadName );
		
		//Start timing
		long startTime = System.nanoTime();
		
		int largest_prime = 2;
		// I was thinking about looping up to
		// Integer.MAX_VALUE but it was taking too
		// long. Looping to 7999999 takes 3-4 sec
		for(int i=3; i<7999999; i+=2)
		{
			if(isPrime(i))
				largest_prime = i;
		}
		
		//End timing
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		
		//Source: https://mkyong.com/java/java-how-to-convert-system-nanotime-to-seconds/
		// 1 second = 1_000_000_000 nano seconds
		double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
		
		System.out.println("Thread " +  threadName + " exiting. Found largest integer prime: "+largest_prime+" with a total runtime of "+ elapsedTimeInSecond+" seconds.");
	}
	
	private boolean isPrime(int x)
	{
		if(x==2)
			return true;
		if(x%2==0)
			return false;
		for(int i=3; i<Math.sqrt(x); i+=2)
		{
			if(x%i==0)
				return false;
		}
		return true;
	}
}
