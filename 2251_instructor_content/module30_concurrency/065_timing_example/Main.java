public class Main
{
	public static void main(String args[])
	{
		//Create threads and pass in data involved in the calculation
		ThreadDemo T1 = new ThreadDemo("Thread A");
		ThreadDemo T2 = new ThreadDemo("Thread B");
		
		
		//Start timing
		long startTime = System.nanoTime();


		//Start the threads
		T1.start();
		T2.start();

		//Wait on threads to finish.
		//try {
			while(T1.isAlive() || T2.isAlive())
			{
				//Thread.sleep(1);
			}
		//} catch (InterruptedException ex) {
		//	System.out.println("Interrupted");
		//}
		
		//Alternative way to wait on threads to finish.
		//Both ways to wait come from here:
		//www.tutorialspoint.com/javaexamples/thread_check.htm
		/* try{
			T1.join();
			T2.join();
		}catch(InterruptedException e){
			System.out.println("Interrupted");
		} */
		
		
		//End timing
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		
		//Source: https://mkyong.com/java/java-how-to-convert-system-nanotime-to-seconds/
		// 1 second = 1_000_000_000 nano seconds
		double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
		System.out.printf("\nTotal Runtime: %.6f sec\n", elapsedTimeInSecond);
	}
	
}
