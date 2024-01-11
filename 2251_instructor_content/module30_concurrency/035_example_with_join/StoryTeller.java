import java.security.SecureRandom;

public class StoryTeller extends Thread
{
	private static final SecureRandom generator = new SecureRandom();
	
	private String[] storyElements;
	
	//Constructor
	public StoryTeller(String[] words)
	{
		storyElements = words;
	}

	public void run()
	{
		try {
			for(int i=0; i<storyElements.length; i++)
			{
				System.out.print(storyElements[i]+" ");
				// random sleep for up to one second
				Thread.sleep(generator.nextInt(1000));
			}
		}
		catch (InterruptedException e) {
			System.out.println("EXCEPTION " + e);
			Thread.currentThread().interrupt();
		}
	}
}
