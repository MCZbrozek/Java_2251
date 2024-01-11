//Source: www.baeldung.com/java-wait-notify
/*
Object.wait() – to suspend a thread
Object.notify() – to wake a thread up

when we call wait() – this forces the current thread to wait until some other thread invokes notify() or notifyAll() on the same object.

For this, the current thread must own the object's monitor. According to Javadocs, this can happen when:
-we've executed synchronized instance method for the given object
-we've executed the body of a synchronized block on the given object
-by executing synchronized static methods for objects of type Class

The following example:
-The Sender is supposed to send a data packet to the Receiver
-The Receiver cannot process the data packet until the Sender is finished sending it
-Similarly, the Sender mustn't attempt to send another packet unless the Receiver has already processed the previous packet
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