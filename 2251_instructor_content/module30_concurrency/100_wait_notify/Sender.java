//Source: www.baeldung.com/java-wait-notify
import java.util.concurrent.ThreadLocalRandom;

public class Sender extends Thread
{
	private Data mydata;
	
	public Sender(Data data)
	{
		mydata = data;
	}

	@Override
	public void run()
	{
		String packets[] = {
		  "First packet",
		  "Second packet",
		  "Third packet",
		  "Fourth packet",
		  "End"
		};
 
		for (String packet : packets)
		{
			mydata.send(packet);

			// Thread.sleep() to mimic heavy server-side processing
			try {
				//Randomly sleep between 0 and 2 seconds.
				Thread.sleep(ThreadLocalRandom.current().nextInt(0, 2000));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}