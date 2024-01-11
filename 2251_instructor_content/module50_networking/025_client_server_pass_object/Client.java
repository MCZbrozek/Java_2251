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
			connectToServer(); // create a Socket to make connection
			getStreams(); // get the input and output streams
			processConnection(); // process connection
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
			closeConnection();
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
		String client_message = "";
		int[] message = null;
		do // process messages sent from server
		{
			//Let user send messages of their own
			System.out.print("Give me a number: ");
			client_message = user_input.nextLine();
			if(!client_message.equals("")) {
				sendData(client_message);
			}
			try // read message and display it
			{
				//receive message from the server
				System.out.print("Waiting on message from server: ");
				message = (int[]) input.readObject();
				System.out.print("Message from Server: ");
				for(int i:message) //for each
					System.out.printf("%4d",i);
				System.out.println();
			} 
			catch (ClassNotFoundException classNotFoundException) 
			{
				System.out.println("\nUnknown object type received");
			}
		} while(!client_message.equals("-1")); //Terminate if client sends -1
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
	private void sendData(String message)
	{
		try // send object to server
		{
			//Take the user's input
			int x = Integer.parseInt(message);
			System.out.println("\nSending to Server: odd numbers up to "+x);
			int[] odds = {x};
			if(x!=-1)
			{
				odds = new int[x/2];
				for(int i=0; i<x/2; i++)
				{
					odds[i] = i*2+1;
					System.out.printf("%5d", odds[i]);
				}
			}
			System.out.println();
			output.writeObject(odds);
			output.flush(); // flush data to output
		}
		catch (IOException ioException)
		{
			System.out.println("\nError writing object");
		}
	}
}

/*****************************************************
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
 ******************************************************/