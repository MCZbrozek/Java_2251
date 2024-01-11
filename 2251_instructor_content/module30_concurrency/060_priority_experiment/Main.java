/* The following demo is a combination of code from here
www.geeksforgeeks.org/java-thread-priority-multithreading/
and here
www.tutorialspoint.com/java/java_multithreading.htm
*/
public class Main
{
	public static void main(String[] args)
	{
		ThreadDemo t1 = new ThreadDemo("Thread A");
		ThreadDemo t2 = new ThreadDemo("Thread B");
		ThreadDemo t3 = new ThreadDemo("Thread C");

		// Default 5
		System.out.println("\nDefault thread priorities:");
		System.out.println("t1 thread priority: "+ t1.getPriority());
		System.out.println("t2 thread priority: "+ t2.getPriority());
		System.out.println("t3 thread priority: "+ t3.getPriority());

		//t1.setPriority(2);
		//t2.setPriority(5);
		//t3.setPriority(8);
		
		t1.setPriority(Thread.MIN_PRIORITY);
		t2.setPriority(Thread.NORM_PRIORITY);
		t3.setPriority(Thread.MAX_PRIORITY);

		//t3.setPriority(21); //will throw IllegalArgumentException 
		
		System.out.println("\nModified thread priorities:");
		System.out.println("t1 thread priority: "+ t1.getPriority());
		System.out.println("t2 thread priority: "+ t2.getPriority());
		System.out.println("t3 thread priority: "+ t3.getPriority());


		// Main thread
		
		// Displays the name of 
		// currently executing Thread
		Thread main_thread = Thread.currentThread();
		System.out.println("\nCurrently Executing Thread : "+ main_thread.getName());
		
		System.out.println("Main thread priority: "+ main_thread.getPriority());

		// Main thread priority is set to 10 
		main_thread.setPriority(10);
		System.out.println("Main thread priority: "+ main_thread.getPriority());
		
		//Actually run the threads
		System.out.println("\n\tStarting all three threads.");
		t1.start(); //A
		t2.start(); //B
		t3.start(); //C
		System.out.println("\tAll three threads started.");
	}
}
