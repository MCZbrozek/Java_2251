/*Source: 
www.tutorialspoint.com/java/java_multithreading.htm
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
		for(int i = 4; i > 0; i--)
		{
			System.out.println("Thread: " + this.getName() + ", " + i);
		}
		System.out.println("Thread " +  this.getName() + " exiting.");
	}
}