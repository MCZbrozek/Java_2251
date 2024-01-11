//Source: www.geeksforgeeks.org/synchronized-in-java/
// A Java program to demonstrate working of synchronized. 
// Driver class
public class Main
{
	public static void main(String args[])
	{
		Sender snd = new Sender();
		ThreadedSend S1 = new ThreadedSend( " Hi " , snd );
		ThreadedSend S2 = new ThreadedSend( " Bye " , snd );

		// Start two threads of ThreadedSend type
		S1.start();
		S2.start();

		// wait for threads to end
		try
		{
			//When we invoke the join() method on a thread, the calling thread goes into a waiting state. It remains in a waiting state until the referenced thread terminates.
			//Source: www.baeldung.com/java-thread-join
			S1.join();
			S2.join();
		}
		catch(Exception e)
		{
			System.out.println("Interrupted");
		}
	}
}