
/*
Name: Michael Zbrozek
Date: 3/2/2024
Purpose: Read in hr.txt, store Persons in PersonSet(), print to console
Sources:
ChatGPT - See prompts in comments
GeeksforGeeks - See links in comments
file_io_example	

Files: 
Main.Java
Person.java
PersonList.Java
PersonSet.java

*/
/*
This code is provided to give you a
starting place. It should be modified.
No further imports are needed.
To earn full credit, you must also
answer the following questions:

Q1: Car and Engine are related
by which, Inheritance or Composition?
	-- Inheritance - "Has a ..."

Q2: Color and Red are related
by which, Inheritance or Composition?
	-- Composition - "Is a ..."

Q3: Shirt and Clothing are related
by which, Inheritance or Composition?
	-- Composition - "Is a ..."

Q4: Furniture and Desk are related
by which, Inheritance or Composition?
	-- Composition - "Is a ..."

Q5: CellPhone and Battery are related
by which, Inheritance or Composition?
	-- Inheritance - "Has a ..."

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
				String name = fileReader.next();
				double height = fileReader.nextDouble();
				double weight = fileReader.nextDouble();

				Person nextPerson = new Person(name, height, weight);

				// Instantiate a new PersonSet object, and add each "nextPerson" object to the
				// ArrayList
				personSet.add(nextPerson);
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

		// A. Instantiate a single Person object as a test. You can make up the data
		// Create a Person object passed to the constructor.
		System.out.println("Instantiate a single Person object as a test.");
		Person person = new Person("Tula dog", 20.5, 44);
		System.out.println(person.toString());

		// B. Instantiate a PersonSet object as a test.
		System.out.println("Print complete PersonSet - ");
		personSet.printPersonSet();
		// C. Read data in from the file hr.txt and display it on the command prompt.

	}
}
