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
	private Socket client;

	//Use local host and port 12345
	private final String host = "127.0.0.1";
	private final int portNumber = 12345;

	// connect to server and process messages from server
	public void runClient()
	{
		try
		{
			connectToServer();
			getStreams();
			processConnection();
		}
		catch (EOFException e) 
		{
			System.out.println("\nClient terminated connection");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}
	}

	// connect to server
	private void connectToServer() throws IOException
	{
		System.out.println("Attempting connection\n");

		// create Socket to make connection to server
		client = new Socket(InetAddress.getByName(host), portNumber);

		// display connection information
		System.out.println("Connected to: " + client.getInetAddress().getHostName());
	}

	// get streams to send and receive data
	private void getStreams() throws IOException
	{
		// set up output stream for objects
		output = new ObjectOutputStream(client.getOutputStream());
		
		output.flush(); // flush output buffer to send header information

		// set up input stream for objects
		input = new ObjectInputStream(client.getInputStream());

		System.out.println("\nGot I/O streams\n");
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
				message = (String)input.readObject();
				System.out.println("\nSERVER>>>" + message);
			}
			catch (ClassNotFoundException e) 
			{
				System.out.println("\nUnknown object type received");
			}

		} while (!message.equals("TERMINATE"));
	}

	// close streams and socket
	private void closeConnection()
	{
		System.out.println("\nClosing connection");
		try
		{
			output.close(); // close output stream
			input.close(); // close input stream
			client.close(); // close socket
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// send message to server
	private void sendData(String message)
	{
		try
		{
			output.writeObject(message);
			output.flush();
		}
		catch (IOException e)
		{
			System.out.println("\nError writing object");
		}
	}
}