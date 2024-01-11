/* The following code is based on: www.baeldung.com/java-wait-notify
The following code is similar to Chapter 23 figures 18 and 19 in the book.
*/
public class Main
{
	public static void main(String[] args)
	{
		Data data = new Data();
		Sender sender = new Sender(data);
		Receiver receiver = new Receiver(data);
		
		sender.start();
		receiver.start();
	}
}