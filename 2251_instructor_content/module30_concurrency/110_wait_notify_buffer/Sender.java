//Source: www.baeldung.com/java-wait-notify
import java.util.concurrent.ThreadLocalRandom;

public class Sender extends Thread
{
	private Data mydata;
	private final int PACKET_COUNT = 100;
	private String packets[] = new String[PACKET_COUNT];
	
	public Sender(Data data)
	{
		mydata = data;
		
		//Populate the packet array
		for(int i=0; i<PACKET_COUNT; i++)
		{
			this.packets[i] = String.valueOf(i)+"th packet";
		}
	}

	@Override
	public void run()
	{
		for (String packet : this.packets)
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