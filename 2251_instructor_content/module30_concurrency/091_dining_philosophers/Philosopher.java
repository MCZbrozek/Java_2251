import java.security.SecureRandom;

public class Philosopher extends Thread
{
	private static final SecureRandom generator = new SecureRandom();
	private Tableware t;
	
	public Philosopher(Tableware t)
	{
		this.t = t;
	}

	public void run()
	{
		boolean got_knife = false;
		boolean got_fork = false;
		
		for(int i=0; i<1000; i++)
		{
			if(!got_knife)
				got_knife = t.pickupKnife();
			if(!got_fork)
				got_fork = t.pickupFork();
			boolean success = t.eat();
			if(success && got_knife)
			{
				t.putDownKnife();
				got_knife = false;
			}
			if(success && got_fork)
			{
				t.putDownFork();
				got_fork = false;
			}
			/*try { // random sleep for up to one second
				Thread.sleep(generator.nextInt(1000));
			}
			catch (InterruptedException exception) {
				Thread.currentThread().interrupt();
			}*/
		}
	}
}