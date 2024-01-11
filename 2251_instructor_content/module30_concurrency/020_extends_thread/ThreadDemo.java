/*Source: 
www.tutorialspoint.com/java/java_multithreading.htm

NOTE: Commenting out the sleep and try
catch in run below makes it so the order
of the print statements in the running
threads is more variable.
(This is done in the next example.)
*/
public class ThreadDemo extends Thread
{
	//Constructor
	public ThreadDemo(String name)
	{
		//Threads have their own names
		this.setName(name);
		System.out.println("Creating " +  this.getName() );
	}
	
	@Override
	public void run()
	{
		System.out.println("Running " +  this.getName() );
		try {
			for(int i = 4; i > 0; i--)
			{
				System.out.println("Thread: " + this.getName() + ", " + i);
				// Let the thread sleep for a while.
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			System.out.println("Thread " +  this.getName() + " interrupted.");
		}
		System.out.println("Thread " +  this.getName() + " exiting.");
	}
}