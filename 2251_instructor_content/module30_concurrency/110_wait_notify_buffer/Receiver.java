//Source: www.baeldung.com/java-wait-notify
import java.util.concurrent.ThreadLocalRandom;

public class Receiver extends Thread
{
	private Data load;
	
	public Receiver(Data data)
	{
		this.load = data;
	}

	@Override
	public void run()
	{
		//for(int i=0; i<something; i++)
		for(String receivedMessage = load.receive(); //int i=0;
			!"End".equals(receivedMessage); //i<something
			receivedMessage = load.receive()) //i++
		{
			System.out.println("Receiver received: "+receivedMessage);

			//Randomly sleep between 0 and 2 seconds.
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(0, 2000));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}