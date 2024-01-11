import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args) 
	{
		long start_time = System.nanoTime();

		int dickens_count = countThe("GreatExpectations.txt");
		int melville_count = countThe("MobyDick.txt");
		int shelley_count = countThe("Frankenstein.txt");

		long duration = System.nanoTime() - start_time;
		System.out.printf("Computation took %.4f ms.%n", duration / 1000000.0);
		
		//Print out results
		System.out.println("Great Expectations has "+dickens_count+" 'the's");
		System.out.println("Moby Dick has "+melville_count+" 'the's");
		System.out.println("Frankenstein has "+shelley_count+" 'the's");

	}	

	public static int countThe(String file_name)
	{
		int count = 0;
		//Open the file
		Path path = Paths.get(file_name);
		Scanner in_file = null;
		try
		{
			in_file =  new Scanner(path);
		}
		catch(IOException e)
		{
			System.out.println("IOException occured when attempting to open "+file_name);
			System.exit(1);
		}
		
		String line;
		String[] word_array;
		while(in_file.hasNextLine())
		{
			line = in_file.nextLine();
			line = line.toLowerCase();
			word_array = line.split(" ");
			for(String word : word_array)
				if(word.equals("the"))
					count++;
		}
		return count;
	}	
}