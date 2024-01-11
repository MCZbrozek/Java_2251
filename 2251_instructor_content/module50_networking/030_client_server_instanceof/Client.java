// Fig. 28.5: Client.java
// Client portion of a stream-socket connection between client and server.
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
	private final Scanner user_input = new Scanner(System.in);

	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	private String message = ""; // message from server
	private String chatServer; // host server for this application
	private Socket client; // socket to communicate with server

	// initialize chatServer
	public Client(String host)
	{
		chatServer = host; // set server to which this client connects
	}

	// connect to server and process messages from server
	public void runClient()
	{
		try // connect to server, get streams, process connection
		{
			connectToServer();
			getStreams(); // get the io streams
			processConnection();
		}
		catch (EOFException eofException)
		{
			System.out.println("\nClient terminated connection");
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
		finally
		{
			closeConnection(); // close connection
		}
	}

	// connect to server
	private void connectToServer() throws IOException
	{
		System.out.println("Attempting connection\n");

		// create Socket to make connection to server
		client = new Socket(InetAddress.getByName(chatServer), 12345);

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
		String message = "";
		do // process messages sent from server
		{
			System.out.print("Choose String, int, or double to send data of the corresponding type. Choose String then TERMINATE to close connection: ");
			String choice = user_input.nextLine();
			
			if(choice.equals("String"))
			{
				//Let user send messages of their own
				System.out.print("Give me a string message to send to the server: ");
				String to_send = user_input.nextLine();
				sendData(to_send);
			}
			else if(choice.equals("int"))
			{
				System.out.print("Enter 5 integers to send to the server: ");
				int[] to_send = new int[5];
				for(int i=0; i<5; i++)
				{
					int temp = user_input.nextInt();
					to_send[i] = temp;
				}
				sendData(to_send);
				user_input.nextLine(); //Flush newline out of input buffer
			}
			else if(choice.equals("double"))
			{
				System.out.print("Enter 3 doubles to send to the server: ");
				double[] to_send = new double[3];
				for(int i=0; i<3; i++)
				{
					double temp = user_input.nextDouble();
					to_send[i] = temp;
				}
				sendData(to_send);
				user_input.nextLine(); //Flush newline out of input buffer
			}
			else
			{
				//Error checking code should go here
				System.out.println("Did not recognize data type. User entered '"+choice+"'");
			}

			try // read message and display it
			{
				//receive message from the server
				System.out.println("Waiting on message from server...");
				message = (String)input.readObject();
				System.out.println("Message from Server: "+message);
			}
			catch (ClassNotFoundException classNotFoundException)
			{
				System.out.println("\nUnknown object type received");
			}
			catch (EOFException eofException)
			{
				//readObject() throws an EOFException when the stream is empty. We don't want that to terminate the connection. We want to instead terminate only when all the data has been transmitted, so this catch ignores the exception and allows the do-while loop to continue.
				System.out.println("End of File exception was thrown.");
			}
		} while(!message.equals("TERMINATE"));//Terminate if server sends TERMINATE
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
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

	// send message to server
	private void sendData(Object message)
	{
		try // send object to server
		{
			output.writeObject(message);
			output.flush(); // flush data to output
		}
		catch (IOException ioException)
		{
			System.out.println("\nError writing object");
			System.out.println(ioException);
			System.exit(0);
		}
	}
}

/***************************************************
 * (C) Copyright 1992-2018 by Deitel & Associates, Inc. and
 * Pearson Education, Inc. All Rights Reserved.
 * DISCLAIMER: The authors and publisher of this book have used their	  *
 * best efforts in preparing the book. These efforts include the			 *
 * development, research, and testing of the theories and programs		  *
 * to determine their effectiveness. The authors and publisher make		 *
 * no warranty of any kind, expressed or implied, with regard to these	 *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or		 *
 * consequential damages in connection with, or arising out of, the		 *
 * furnishing, performance, or use of these programs.
 ***************************************************/