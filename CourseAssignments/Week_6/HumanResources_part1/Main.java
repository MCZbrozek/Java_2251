
/*
This code is provided to give you a
starting place. It should be modified.
No further imports are needed.
To earn full credit, you must also
answer the following questions:

Q1: Car and Engine are related
by which, Inheritance or Composition?

Q2: Color and Red are related
by which, Inheritance or Composition?

Q3: Shirt and Clothing are related
by which, Inheritance or Composition?

Q4: Furniture and Desk are related
by which, Inheritance or Composition?

Q5: CellPhone and Battery are related
by which, Inheritance or Composition?

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

		// Initialize scanner and check for a valid path
		try {
			fileReader = new Scanner(filePath);
		} catch (IOException e) {
			System.out.println("Error - I/O Exception" + e.getMessage());
			System.exit(1);
		}

		try {
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
			}
		} catch (java.util.InputMismatchException e) {
			System.out.println("Input does not match\n");
			e.printStackTrace();
			System.out.println("\n" + e + "\n");
		}
		fileReader.close();
		System.out.println();

		/*
		 * // Don't overcomplicate the data
		 * // reading. After skipping the
		 * // first row, you can use the
		 * // following to read a row of
		 * // character info, assuming your
		 * // Scanner is named "fileReader"
		 * String name = fileReader.next();
		 * double height = fileReader.nextDouble();
		 * double weight = fileReader.nextDouble();
		 */

		/*
		 * try
		 * {
		 * FileWriter fileWriterOrder = new FileWriter("outputfile.txt");
		 * fileWriterOrder.write("testing");
		 * fileWriterOrder.close();
		 * }
		 * catch(IOException e)
		 * {
		 * e.printStackTrace();
		 * System.out.println(e);
		 * System.exit(1);
		 * }
		 */

		// A. Instantiate a single Person object as a test. You can make up the data
		// passed to the constructor.
		// B. Instantiate a PersonSet object as a test.
		// C. Read data in from the file hr.txt and display it on the command prompt.

	}
}
