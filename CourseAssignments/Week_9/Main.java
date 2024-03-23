
/*
Name: Michael Zbrozek
Date: 3/13/2024
Purpose: Read in hr.txt, store Persons in PersonSet(), print to console
Sources:
ChatGPT - See prompts in comments
GeeksforGeeks - See links in comments
file_io_example	
Tree.java - instructor examples

Files: 
Main.Java
Person.java
SortedTreeSet.java
SortedTreeSetInterface.java
*/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// import from user defined file path
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) {

		Path filePath = Paths.get(args[0]);
		Scanner fileReader = null;
		// Initialize a personSet to fill
		PersonSet personSet = new PersonSet();
		// Initialize a SortedTreeSet to fill
		SortedTreeSet sortedTreeSet = new SortedTreeSet();

		// Initialize scanner and check for a valid path
		try {
			fileReader = new Scanner(filePath);
		} catch (IOException e) {
			System.out.println("Error - I/O Exception" + e.getMessage());
			System.exit(1);
		}

		try {
			System.out.println("READING FROM FILE: " + filePath + '\n');
			System.out.println("ADDING PERSON DATA TO PERSON SET, AND CHECKING FOR DUPLICATES...");
			// Skip first line of file
			String skipLine = fileReader.nextLine();
			// System.out.println(skipLine);
			while (fileReader.hasNextLine()) {
				String line = fileReader.nextLine();
				// This is a solution that I got from ChatGPT, even though this file worked
				// great in the Human Resources directory, when I ran it in the new directory I
				// would get a 'NoSuchElementException' when it reached the end of the .txt
				// file.
				/*
				 * Explanation from ChatGPT - It looks like the issue might be related to mixing
				 * calls to next() and nextDouble() without proper synchronization with the
				 * input file. When working with a file using Scanner, it's crucial to ensure
				 * that you're reading the correct type of data based on the actual content of
				 * the file.
				 */
				Scanner lScanner = new Scanner(line);
				String name = lScanner.next();
				double height = lScanner.nextDouble();
				double weight = lScanner.nextDouble();
				lScanner.close();

				Person nextPerson = new Person(name, height, weight);

				// Instantiate a new PersonSet object, and add each "nextPerson" object to the
				// SortedTreeSet.
				sortedTreeSet.add(nextPerson);
			}
		} catch (java.util.InputMismatchException e) {
			System.out.println("Input does not match\n");
			e.printStackTrace();
			System.out.println("\n" + e + "\n");
		}
		fileReader.close();
		System.out.println();

		try {
			FileWriter fileWriterOrder = new FileWriter("outputfile.txt");
			fileWriterOrder.write("testing");
			fileWriterOrder.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
			System.exit(1);
		}

		// C. Read data in from the file hr.txt and display it on the command prompt.
		System.out.println(sortedTreeSet.toString());

	}
}
