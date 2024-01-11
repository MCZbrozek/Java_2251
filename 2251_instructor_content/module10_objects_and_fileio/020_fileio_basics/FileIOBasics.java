/*
Review File IO Basics for the upcoming Travel Data assignment.
*/

//Needed for input from file
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

//Needed for output to file
import java.io.FileWriter;

public class FileIOBasics
{

	public static void main(String[] args)
	{		
		String textForOutput = "";
		
		// ===FILE IO===
		try
		{
			File f = new File("file_for_input.txt");
			Scanner s = new Scanner(f);
			//String test = s.nextLine();
			//System.out.println(test);
			
			while(s.hasNext())
			{
				//Print integers
				for(int i=0; i<6; i++)
				{
					int x = s.nextInt();
					System.out.println("Number: "+x);
					textForOutput += "Number: "+x+"\n";
				}
				//Print doubles
				for(int i=0; i<4; i++)
				{
					double x = s.nextDouble();
					System.out.println("Double: "+x);
					textForOutput += "Double: "+x+"\n";
				}
				//Print Strings
				while(s.hasNextLine())
				{
					String temp = s.nextLine();
					System.out.println("Line: "+temp);
					textForOutput += "Line: "+temp+"\n";
				}
			} // end while(s.hasNext())
			s.close();
			
		} //end try
		catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		
		
		//write output
		try
		{
			FileWriter fw = new FileWriter("output.txt");
			fw.write( textForOutput );
			fw.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}

	}

}
