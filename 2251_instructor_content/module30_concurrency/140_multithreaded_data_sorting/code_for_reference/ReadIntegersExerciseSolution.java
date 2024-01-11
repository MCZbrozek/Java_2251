import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReadIntegersExerciseSolution
{
	public static void main(String[] args)
	{
		String file_name = args[0];

		//Alternative 
		//Scanner scnr = new Scanner(System.in);
		//System.out.print("Enter the file you would like to open: ");
		//String file_name = scnr.next();

		//open file using Path and Scanner
		Path p = Paths.get(file_name);
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(p);
		}
		catch (java.io.IOException e) {
			System.out.println("Input Output Exception");
			e.printStackTrace();
			System.exit(2);
		}

		//read the number of rows from the file
		//using the nextInt method of Scanner
		String line;
		int row_count = 0;
		while(fileIn.hasNextLine())
		{
			row_count++;
			line = fileIn.nextLine();
		}
		System.out.println("The file has "+row_count+" rows.");
		
		//read the number of columns from the file
		//using the nextInt method of Scanner
		try { //IMPORTANT: Reset the scanner
			fileIn = new Scanner(p);
		}
		catch (java.io.IOException e) {
			System.out.println("Input Output Exception");
			e.printStackTrace();
			System.exit(2);
		}
		int column_count = 0;
		int word_count = 0;
		while(fileIn.hasNext())
		{
			word_count++;
			line = fileIn.next();
		}
		column_count = word_count / row_count;
		System.out.println("The file has "+column_count+" columns.");
	
		//create a 2d integer array using the number of rows
		//and the number of columns as the size of the array
		int[][] matrix = new int[row_count][column_count];
		
		//Using two nested for loops, outer loop for the number of
		//rows and the inner loop for the number of columns.
		//Read the array data from the file using the nextInt
		//method of Scanner and put it into the 2d integer array
		try { //IMPORTANT: Reset the scanner
			fileIn = new Scanner(p);
		}
		catch (java.io.IOException e) {
			System.out.println("Input Output Exception");
			e.printStackTrace();
			System.exit(2);
		}
		try{
			for(int row=0; row<row_count; row++)
			{
				for(int column=0; column<column_count; column++)
				{
					matrix[row][column] = fileIn.nextInt();
				}
			}
		}catch (java.util.InputMismatchException e) {
			System.out.println("Input does not match an integer");
			System.exit(3);
		}
		
		//Using two nested for loops, same as above print out the
		//values of the 2d array. Make sure the values printed are 
		//the same values that are in the file.
		for(int row=0; row<row_count; row++)
		{
			for(int column=0; column<column_count; column++)
			{
				System.out.printf("%3d\t", matrix[row][column]);
			}
			System.out.println();
		}
	}

}
