/*
Source:
https://www.geeksforgeeks.org/main-thread-java/
Java program to demonstrate deadlock using
Main thread.
*/
public class Test2
{
	public static void main(String[] args)
	{
		try
		{
			System.out.println("Entering into Deadlock");
			Thread t = Thread.currentThread();
			/* join forces the main thread to wait
			until thread t finishes before 
			continuing onward, but thread t IS MAIN
			so main is waiting on itself to finish
			before it can finish!
			A paradox!
			This is bad.
			Use Control-C to terminate the process.
			*/
			t.join();
			// the following statement will never execute
			System.out.println("This statement will never execute");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}