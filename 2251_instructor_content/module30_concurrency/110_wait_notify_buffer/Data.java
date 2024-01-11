//Source: www.baeldung.com/java-wait-notify
//Modified by Neal Holtschulte
public class Data
{
	//Change this to get a larger or smaller buffer
	private static final int BUFFER_SIZE = 3;

	//How many spaces are free in the buffer
	private int buffer_free_space = BUFFER_SIZE;

	//String buffer
	private String[] packet_buffer = new String[BUFFER_SIZE];
	
	//Index of the most recent item taken from the buffer.
	//Is -1 before the buffer has been used so that it can be 
	//incremented to zero.
	private int take_index = -1;

	//Index of the most recent item put into the buffer.
	//Is -1 before the buffer has been used so that it can be 
	//incremented to zero.
	private int put_index = -1;
	

	public synchronized void send(String packet)
	{
		//Make the sender wait to send the packet while the buffer is full
		while (buffer_free_space == 0)
		{
			//Status
			System.out.printf("Blocking send. Buffer full.\n");

			try {
				wait();
			} catch (InterruptedException e)  {
				Thread.currentThread().interrupt();
			}
		}
		
		//Status
		System.out.printf("Free space: %d. Sending.\n", buffer_free_space);


		//Increment the index to the next location
		put_index = (put_index+1)%packet_buffer.length;
		//Put the packet in the buffer
		packet_buffer[put_index] = packet;
		//Decrease the free space
		buffer_free_space--;
		//Notify any waiting threads that they can wake up and access the packet buffer.
		notifyAll();
	}
 
	public synchronized String receive()
	{
		//Make the reciever wait to receive packets while the buffer is empty.
		while (buffer_free_space == packet_buffer.length)
		{
			//Status
			System.out.printf("Blocking receive. Buffer empty.\n");

			try {
				wait();
			} catch (InterruptedException e)  {
				Thread.currentThread().interrupt();
			}
		}
		
		//Status
		System.out.printf("Free space: %d. Receiving.\n", buffer_free_space);
		
		//Increment the index to the next location
		take_index = (take_index+1)%packet_buffer.length;
		//Retrieve the packet from the buffer
		String packet = packet_buffer[take_index];
		//Increase the amount of free space
		buffer_free_space++;
		//Notify any waiting threads that they can wake up and access the packet buffer.
		notifyAll();
		
		return packet;
	}
}