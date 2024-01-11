import java.util.Scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//https://www.w3schools.com/java/java_arraylist.asp
import java.util.ArrayList;

//https://stackoverflow.com/questions/10631715/how-to-split-a-comma-separated-string
import java.util.Arrays;
import java.util.List;

//https://www.javatpoint.com/how-to-sort-arraylist-in-java
import java.util.Collections;


public class Main
{

	public static void main(String[] args)
	{
		//11 year's worth of data is in the file
		int[] year = new int[11];
		double[] median = new double[11];

		try
		{
			File f = new File("unemployment_stats.csv");
			Scanner s = new Scanner(f);
			
			//Strip first 11 lines
			for(int i=0; i<11; i++)
			{
				//TESTING
				System.out.println(s.nextLine());
			}
			
			//Use ArrayList since not all rows have the
			//same number of values, and because it
			//makes sorting easier.
			ArrayList<Double> data = null;
			
			int index = 0;
			
			System.out.println("Made it here 1"); //TESTING
			while(s.hasNext())
			{
				//Reset the value of data
				data = new ArrayList<Double>();
				
				String line = s.nextLine();
				String[] values = line.split(",");
				
				year[index] = Integer.parseInt(values[0]);

				for(int i=1; i<values.length; i++)
				{
					//https://www.journaldev.com/18392/java-convert-string-to-double
					double d = Double.parseDouble(values[i]);
					data.add(d);
				}
				//https://www.javatpoint.com/how-to-sort-arraylist-in-java
				Collections.sort(data);
				
				median[index] = data.get(data.size()/2);
				
				index++;
			}
			s.close();
		}
		catch(Exception FileNotFoundException) 
		{
			System.out.println("ERROR!");
			System.out.println(FileNotFoundException);
		}
		
		
		//write output file
		try
		{
			FileWriter fw = new FileWriter("output.txt");
			String temp = String.format("%s %12s\n", "Year", "Median");
			fw.write(temp);
			for(int i=0; i<year.length; i++)
			{
				temp = String.format("%d %12.2f\n", year[i], median[i]);
				fw.write(temp);
			}
			fw.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

}
