/*Source: 
www.tutorialspoint.com/java/java_multithreading.htm

Same as the previous example, but creating
threads by creating a new class that extends
Thread class.

This approach provides more flexibility in
handling multiple threads created using
available methods in Thread class.
*/

public class Main
{
	public static void main(String args[])
	{
		//Create new ThreadDemo variables
		ThreadDemo T1 = new ThreadDemo("Thread-A");
		ThreadDemo T2 = new ThreadDemo("Thread-B");

		//Start the variables running concurrently.
		T1.start();
		T2.start();
	}
}