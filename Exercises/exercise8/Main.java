/*
Name: Michael Zbrozek
Date: 3/26/2024
Purpose: Write a class that extends thread. The class should take as input an integer to start counting from and a String to use as a name. The run method of the threaded class should print out all the integers from start to 10,000 alongside the thread's name (given as a string input in the constructor).

Sources: 	

Files: 
Main.Java
ThreadDemo.java
*/

public class Main {
	public static void main(String args[]) {
		// Create new instances of ThreadDemo:
		ThreadDemo T1 = new ThreadDemo(8470, "thread1");
		ThreadDemo T2 = new ThreadDemo(8901, "thread2");

		// Start up both ThreadDemo objects:
		T1.start();
		T2.start();

		// Wait on the threads to finish.
		try {
			T1.join();
			T2.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		}

		System.out.println("\nFINISHED\n");
	}
}