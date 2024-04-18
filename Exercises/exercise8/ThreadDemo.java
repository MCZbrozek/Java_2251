/*
Name: Michael Zbrozek
Date: 3/26/2024
Purpose: Write a class that extends thread. The class should take as input an integer to start counting from and a String to use as a name. The run method of the threaded class should print out all the integers from start to 10,000 alongside the thread's name (given as a string input in the constructor).

Sources: 	

Files: 
Main.Java
ThreadDemo.java
*/

public class ThreadDemo extends Thread {
	private int start;
	private String identifier;

	public ThreadDemo(int start, String identifier) {
		this.start = start;
		this.identifier = identifier;
	}

	public void run() {
		for (int i = start; i < 10000; i++)
			System.out.println(identifier + ": " + (i));
	}
}