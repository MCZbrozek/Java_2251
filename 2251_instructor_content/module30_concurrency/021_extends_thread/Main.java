/*Source: 
www.tutorialspoint.com/java/java_multithreading.htm
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