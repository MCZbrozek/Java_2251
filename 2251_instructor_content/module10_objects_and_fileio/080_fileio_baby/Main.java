import java.util.Scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//https://www.w3schools.com/java/java_arraylist.asp
import java.util.ArrayList;

public class Main
{

	public static void main(String[] args)
	{
		ArrayList<NameData> data = new ArrayList<NameData>();

		try
		{
			File f = new File("Most_Popular_Baby_Boy_Names__1980-2013.csv");
			Scanner s = new Scanner(f);
			
			//Strip first line
			s.nextLine();
			
			while(s.hasNext())
			{
				String line = s.nextLine();
				String[] values = line.split(",");
				
				//Rank,Year,Name,Frequency
				NameData temp = new NameData(
					Integer.parseInt(values[0]),
					Integer.parseInt(values[1]),
					values[2],
					Integer.parseInt(values[3])
					);
				
				//System.out.print(temp);
				
				data.add(temp);
			}
			s.close();
		}
		catch(Exception e) 
		{
			System.out.println("ERROR!");
			e.printStackTrace();
		}
		
		
		//write output file
		try
		{
			FileWriter fw = new FileWriter("output.txt");
			String temp = String.format("%10s %10s %10s %10s\n", "Rank", "Year", "Name", "Frequency");
			fw.write(temp);
			for(int i=0; i<data.size(); i++)
			{
				NameData nd = data.get(i);
				fw.write(nd.toString());
			}
			fw.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		
		
		findTrendingName("LUCAS", data);
		findTrendingName("NATHAN", data);

	}
	
	
	public static int getNameRank(String name, int year, ArrayList<NameData> data)
	{
		NameData nd = null;
		for(int i=0; i<data.size(); i++)
		{
			nd = data.get(i);
			if(nd.getYear() == year &&
			name.equalsIgnoreCase(nd.getName()))
			{
				return nd.getRank();
			}
		}
		return -1;
	}
	
	
	public static void findTrendingName(String name, ArrayList<NameData> data)
	{
		int rank2012 = getNameRank(name, 2012, data);
		int rank2013 = getNameRank(name, 2013, data);
		System.out.println(rank2012);
		System.out.println(rank2013);
		if(rank2013 < rank2012)
		{
			System.out.println("The name "+name+" is more popular.");
		}
		else
		{
			System.out.println("The name "+name+" is less popular.");
		}
	}

}
