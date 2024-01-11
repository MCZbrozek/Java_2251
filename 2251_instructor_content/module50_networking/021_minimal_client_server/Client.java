import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
	private final Scanner userInput = 
					new Scanner(System.in);

	private ObjectOutputStream output;
	private ObjectInputStream input;

	//Use local host and port 12345
	private final String host = "127.0.0.1";
	private final int portNumber = 12345;

	// connect to server and process messages from server
	public void runClient() throws IOException
	{
		Socket client = new Socket(
			InetAddress.getByName(host), portNumber);
			
		output = new ObjectOutputStream(
			client.getOutputStream());
		output.flush();
			
		input = new ObjectInputStream(
			client.getInputStream());
		
		try
		{
			processConnection();
		}
		catch (EOFException e) 
		{
			System.out.println("\nClient terminated connection");
		}
		finally
		{
			output.close();
			input.close();
			client.close();
		}
	}

	// process connection with server
	private void processConnection() throws IOException
	{
		String message;
		do
		{
			System.out.print("Type a message: ");
			message = userInput.nextLine();
			if(!message.equals("")) {
				sendData(message);
			}
			try // read message and display it
			{
				//This is a blocking call
				message = (String)input.readObject();
				
				System.out.println("\nSERVER>>>" + message);
			}
			catch (ClassNotFoundException e) 
			{
				System.out.println("\nUnknown object type received");
			}

		} while (!message.equals("TERMINATE"));
	}

	// send message to server
	private void sendData(String message) throws IOException
	{
		output.writeObject(message);
		output.flush();
	}
}