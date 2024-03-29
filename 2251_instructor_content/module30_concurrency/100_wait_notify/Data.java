//Source: www.baeldung.com/java-wait-notify
public class Data
{
	private String packet;
	
	// True if receiver should wait
	// False if sender should wait
	private boolean transfer = true;
 
	public synchronized void send(String packet)
	{
		while (!transfer) //while sender should wait
		{
			try { 
				wait();
			} catch (InterruptedException e)  {
				Thread.currentThread().interrupt();
			}
		}
		transfer = false; //sender should wait. receiver should receive
		
		this.packet = packet;
		notifyAll();
	}
 
	public synchronized String receive()
	{
		while (transfer)
		{
			try {
				wait();
			} catch (InterruptedException e)  {
				Thread.currentThread().interrupt();
			}
		}
		transfer = true;

		notifyAll();
		return packet;
	}
}