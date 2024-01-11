import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		String file_name = args[0];

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
		//using the nextLine method of Scanner
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


		//Skip the header row
		row_count--;
		fileIn.nextLine();

		DateValue[] data = new DateValue[row_count];

		//Read in and parse the csv data
		for(int i=0; i<row_count; i++)
		{
			line = fileIn.nextLine();
			String[] values = line.split(",");
			int month = Integer.parseInt(values[0]);
			int day = Integer.parseInt(values[1]);
			int year = Integer.parseInt(values[2]);
			double value = Double.parseDouble(values[3]);

			data[i] = new DateValue(month, day, year, value);
			
			//System.out.println(data[i]);
		}
		fileIn.close();
		
		
		
		//Print first 10 test values unsorted
		for(int i=0; i<10; i++)
		{
			System.out.println(data[i]);
		}



		
		//Pass data to sort to thread and run it
		QuicksortThread T1 = new QuicksortThread(data, 0, data.length-1);
		T1.start();
		try{
			T1.join();
		}catch(InterruptedException e){
			System.out.println(e);
		}




		//Print first 10 test values
		System.out.println();
		for(int i=0; i<10; i++)
		{
			System.out.println(data[i]);
		}

		//Print middle 10 test values
		System.out.println();
		for(int i=5000; i<5010; i++)
		{
			System.out.println(data[i]);
		}

		//Print last 10 test values
		System.out.println();
		for(int i=data.length - 11; i<data.length -1; i++)
		{
			System.out.println(data[i]);
		}
	}

}
