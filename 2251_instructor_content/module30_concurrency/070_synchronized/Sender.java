//Source: www.geeksforgeeks.org/synchronized-in-java/
// A Class used to send a message. This 
// is the shared resource.
public class Sender
{
	public void send(String msg)
	{
		System.out.println("Sending\t"  + msg );
		//Simulate sending a message, perhaps 
		//over a network, with this delay
		try
		{
			Thread.sleep(1000);
		}
		catch (Exception e)
		{
			System.out.println("Thread interrupted.");
		}
		//Confirm to user that the message was sent
		System.out.println("\n" + msg + "Sent");
	}
}