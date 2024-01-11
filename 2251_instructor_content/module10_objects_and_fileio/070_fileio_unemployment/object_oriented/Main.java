import java.util.Scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main
{

	public static void main(String[] args)
	{
		Unemployment u = new Unemployment();

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
			
			System.out.println("Made it here 1"); //TESTING
			while(s.hasNext())
			{
				String line = s.nextLine();
				String[] values = line.split(",");
				
				int year = Integer.parseInt(values[0]);
				u.addYear(year);

				for(int i=1; i<values.length; i++)
				{
					//https://www.journaldev.com/18392/java-convert-string-to-double
					double d = Double.parseDouble(values[i]);
					u.addData(d);
				}
				u.incrementYear();
			} // ends while(s.hasNext())
			s.close();
		} // ends try
		catch(Exception e)
		{
			System.out.println("ERROR!");
			System.out.println(e);
			e.printStackTrace();
			System.exit(1);
		}
		
		//write output file
		try
		{
			FileWriter fw = new FileWriter("output.txt");
			fw.write(u.toString());
			fw.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}

}
