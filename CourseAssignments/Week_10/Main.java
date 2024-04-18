/*
Name: Michael Zbrozek
Date: 3/28/2024
Purpose: Read in matrixes, split them into 2 matrixes and further subdivide them into quadrants that will be added together by 4 threads and output as a separate matrix print to console.
Sources:
ChatGPT - See prompts in comments
CSCI1152 - Paintball assignment for 2d array scanner and print
GeeksforGeeks - See links in comments


Files: 
Main.Java
ThreadOperations.java
*/

/*
This code is provided to give you a
starting place. It should be modified.
No further imports are needed.
To earn full credit, you must also
answer the following question:

Q1: One of the goals of multi-threading
is to minimize the resource usage, such
as memory and processor cycles. In three
sentences, explain how multi-threaded
code accomplishes this goal. Consider
writing about blocking on I/O, multicore 
machines, how sluggish humans are,
threads compared to processes, etcetera,
and connect these issues to 
multi-threading.

ANSWER - 
Multi-threading minimizes resource usage by utilizing available processing resources on multi-core machines by allowing multiple threads to execute concurrently utilizing idle CPU cycles. This prevents unnecessary waits and maximizes throughput. Compared to separate processes, threads share basically sharing the same memory, which can reduce the overhead of memory allocation and context switching. 
*/

// Java Libraries
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

// Import from user defined file path - as used in Human Resource exercises
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) {

		// The filename is given through the command prompt and passed into main
		// via String[] args
		// set path to args[0]
		Path filePath = Paths.get(args[0]);

		// instantiate scanner
		Scanner fileReader = null;

		// rows and cols
		int rows = 0;
		int cols = 0;

		// Empty 2d array objects to be passed to threadOperations
		int[][] firstHalf = null;
		int[][] secondHalf = null;

		// Initialize scanner and check for a valid path
		try {
			fileReader = new Scanner(filePath);
		} catch (IOException e) {
			System.out.println("Fatal Error!! - jk your file isn't coming in quite right, check the path an try again! "
					+ e.getMessage());
			System.exit(1);
		}

		try {
			System.out.println("READING FROM FILE: " + filePath + '\n');

			// Read in first line from file, and set vars to create array
			// Initialize 2d array with row and column length
			rows = fileReader.nextInt();
			cols = fileReader.nextInt();
			System.out.println("col =" + cols);
			System.out.println("row =" + rows);

			// call the populate array function
			firstHalf = populateTargetArray(fileReader, rows, cols);
			secondHalf = populateTargetArray(fileReader, rows, cols);

			// Print each half
			System.out.println("The first half of the file looks like -");
			print2dArray(firstHalf);
			System.out.println("\n\n");
			System.out.println("The second half of the file looks like -");
			print2dArray(secondHalf);

		} catch (java.util.InputMismatchException e) {
			System.out.println("Input does not match\n");
			e.printStackTrace();
			System.out.println("\n" + e + "\n");
		}
		fileReader.close();

		int[][] matrixResult = new int[rows][cols];

		// instantiate four ThreadOperation objects
		ThreadOperations thread1 = new ThreadOperations(firstHalf, secondHalf, matrixResult, 1);

		ThreadOperations thread2 = new ThreadOperations(firstHalf, secondHalf, matrixResult, 2);

		ThreadOperations thread3 = new ThreadOperations(firstHalf, secondHalf, matrixResult, 3);

		ThreadOperations thread4 = new ThreadOperations(firstHalf, secondHalf, matrixResult, 4);

		// start them
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();

		// and join them.
		try {
			thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted exception");
			e.printStackTrace();
			System.exit(1);
		}

		// Each ThreadOperation will take as input (through the
		// constructor) two matrices and a quadrant indicator. The indicator could be a
		// String, an int, an enum or a set of indexes. It’s up to you.

		// Instantiate a test 2d array with any values you like in main and use it to
		// verify that print2dArray works.
		// int[][] test2d = {
		// { 0, 1, 2, 3, 3, 4, 6, 7 },
		// { 1, 1, 2, 3, 3, 4, 6, 7 },
		// { 2, 2, 3, 3, 3, 4, 6, 7 },
		// { 3, 4, 4, 1, 3, 4, 6, 7 } };
		// System.out.println("\nThe test array looks like - \n");
		System.out.println("\nThe resulting array looks like -");
		print2dArray(matrixResult);
		// System.out.println("\n\n");

	}

	// -$-$- METHOD DEFINITIONS -$-$-

	// populateTargetArray takes in the scanner and and the instantiated array and
	// read next int to populate the array
	public static int[][] populateTargetArray(Scanner fileReader, int rows, int cols) {
		int[][] tempMatrix = new int[rows][cols];
		// There is difference between row(i) and rows(int read from file).
		// Similarly col(j) is a counter, cols is an int read from file
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				tempMatrix[row][col] = fileReader.nextInt();
			}
		}
		return tempMatrix;
	}

	// print2dArray takes a two-dimensional array as input and prints it out with
	// the rows and columns lined up. You must use System.out.printf.
	public static void print2dArray(int[][] test2d) {
		// code to print 2dArray
		// source - method I wrote for paintball.java in 1152
		for (int row = 0; row < test2d.length; row++) {
			for (int col = 0; col < test2d[row].length; col++) {
				System.out.printf("%-2d", test2d[row][col]);
			}
			System.out.print("\n");
		}
	}

}