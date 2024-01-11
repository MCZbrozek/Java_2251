/* The following demo is a combination of code from here
www.geeksforgeeks.org/java-thread-priority-multithreading/
and here
www.tutorialspoint.com/java/java_multithreading.htm
*/
import java.lang.*; 

class ThreadDemo extends Thread
{
	private String threadName;
	
	//Constructor
	ThreadDemo( String name)
	{
		threadName = name;
		System.out.println("Creating " +  threadName );
	}

	public void run()
	{
		System.out.println("Running " +  threadName );
		for(int i = 4; i > 0; i--)
		{
			System.out.println("Thread: " + threadName + ", " + i);
		}
		System.out.println("Thread " +  threadName + " exiting.");
	}
}
