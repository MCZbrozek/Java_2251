//This program should be invoked with this:
//   java ReadingFile randomtext.txt
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import java.io.FileWriter; //Needed for writing to a file.

public class ReadingFile
{
	public static void main(String[] args)
	{
		Path path = Paths.get(args[0]);
		Scanner fileIn = null;
		
		// Initialize scanner and check for invalid path
		try {
			fileIn = new Scanner(path);
		} catch (IOException e) {
			System.out.println("error IO exception: "+e.getMessage());
			System.exit(1);
		}

		String month = "";
		int day=0, year=0;
		
		//Skip the first line of the input file
		fileIn.nextLine();
		
		try {
			while(fileIn.hasNextLine())
			{
				month = fileIn.next();
				day = fileIn.nextInt();
				year = fileIn.nextInt();
				System.out.println("Read "+month+" "+day+", "+year);
			}
		}
		catch (java.util.InputMismatchException e) {
			System.out.println("Input did not match\n");
			e.printStackTrace();
			System.out.println("\n"+e+"\n");
		}
		fileIn.close();
		

		//Writing to a file
		try {
			FileWriter myWriter = new FileWriter("filename.txt");
			
			myWriter.write("Most recently I saw: "+month+" "+day+", "+year+"\n");
			myWriter.write("Does that mean anything to you?\n\n\n\n\n");
			
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		}
		catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}
