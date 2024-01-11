import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection;

	private final int portNumber = 12345;
	private final int backlogLimit = 100;

	// set up and run server
	public void runServer() throws IOException
	{
		//backlog is how many clients can
		//wait in the queue
		ServerSocket server = new ServerSocket(
			portNumber, backlogLimit);

		while (true)
		{
			try
			{
				//This is a blocking call
				connection = server.accept();

				output = new ObjectOutputStream(
					connection.getOutputStream());
				output.flush();

				input = new ObjectInputStream(
					connection.getInputStream());
		
				processConnection();
			} 
			catch (EOFException e) 
			{
				System.out.println("\nServer terminated connection");
			}
			finally
			{
				output.close();
				input.close();
				connection.close();
			}
		}
	}

	// process connection with client
	private void processConnection() throws IOException
	{
		String message = "";

		do // process messages sent from client
		{
			try // read message and display it
			{
				//This is a blocking call
				message = (String) input.readObject();
				
				System.out.println("\nCLIENT>>>" + message);
			} 
			catch (ClassNotFoundException e) 
			{
				System.out.println("\nUnknown object type received");
			}
			
			//Unless client requests terminate, reply.
			if(!message.equals("TERMINATE"))
			{
				/* Send the same message back to 
				the client every time. This
				prevents the Client from hanging
				on this line of code:
				message = (String) input.readObject(); */
				sendData("OK, Client\n");
			}
		} while (!message.equals("TERMINATE"));
		
		/* Send this so the client has
		confirmation that the connection
		should be terminated. */
		sendData("TERMINATE");
	}

	// send message to client
	private void sendData(String message) throws IOException
	{
		output.writeObject(message);
		output.flush();
	}
}