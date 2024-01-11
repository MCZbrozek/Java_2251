import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TheCounter extends Thread
{
	private int count;
	private String file_name;

	public TheCounter(String file_name)
	{
		this.file_name = file_name;
		this.count = 0;
	}

	@Override
	public void run()
	{
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
	}

	public int getCount(){return count;}
}