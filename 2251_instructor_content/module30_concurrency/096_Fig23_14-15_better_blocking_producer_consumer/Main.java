/* Literally the only difference between this example and the previous example is the use of the ArrayBlockingQueue data structue in BlockingBuffer.java

See here for more information:
https://www.geeksforgeeks.org/arrayblockingqueue-class-in-java/

This program should correctly add up the values 1 to 10 resulting in 55 each time.
*/

// Fig. 23.15: BlockingBufferTest.java
// Two threads manipulating a blocking buffer that properly 
// implements the producer/consumer relationship.
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{
		// create new thread pool
		ExecutorService executorService = Executors.newCachedThreadPool();

		// create BlockingBuffer to store ints
		Buffer sharedLocation = new BlockingBuffer();

		executorService.execute(new Producer(sharedLocation));
		executorService.execute(new Consumer(sharedLocation));

		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES); 
	}
}


/****************************************
 * (C) Copyright 1992-2015 by Deitel & Associates, Inc. and			*
 * Pearson Education, Inc. All Rights Reserved.
 *
 * DISCLAIMER: The authors and publisher of this book have used their	  *
 * best efforts in preparing the book. These efforts include the			 *
 * development, research, and testing of the theories and programs		  *
 * to determine their effectiveness. The authors and publisher make		 *
 * no warranty of any kind, expressed or implied, with regard to these	 *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or		 *
 * consequential damages in connection with, or arising out of, the		 *
 * furnishing, performance, or use of these programs.	*
 *************************************/