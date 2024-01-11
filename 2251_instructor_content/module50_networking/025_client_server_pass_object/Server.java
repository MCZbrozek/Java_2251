// Fig. 28.3: Server.java
// Server portion of a client/server stream-socket connection.
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
	private ObjectOutputStream output; // output stream to client
	private ObjectInputStream input; // input stream from client
	private ServerSocket server; // server socket
	private Socket connection; // connection to client
	private int counter = 1; // counter of number of connections

	// set up and run server
	public void runServer()
	{
		try // set up server to receive connections; process connections
		{
			//ServerSocket(int port, int backlog)
			//backlog is how many clients can wait in the queue
			server = new ServerSocket(12345, 100); // create ServerSocket

			while (true)
			{
				try
				{
					waitForConnection();
					getStreams(); // get io streams
					processConnection();
				}
				catch (EOFException eofException)
				{
					System.out.println("\nServer terminated connection");
				}
				finally
				{
					closeConnection(); //  close connection
					++counter;
				}
			}
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

	// wait for connection to arrive, then display connection info
	private void waitForConnection() throws IOException
	{
		System.out.println("Waiting for connection\n");
		connection = server.accept(); // allow server to accept connection
		System.out.println("Connection " + counter + " received from: " +
			connection.getInetAddress().getHostName());
	}

	// get streams to send and receive data
	private void getStreams() throws IOException
	{
		// set up output stream for objects
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush(); // flush output buffer to send header information

		// set up input stream for objects
		input = new ObjectInputStream(connection.getInputStream());

		System.out.println("\nGot I/O streams\n");
	}

	// process connection with client
	private void processConnection() throws IOException
	{
		//Temporary variable to hold data from client.
		int[] data_from_client = null;

		do // process messages sent from client
		{ 
			try // read message and display it
			{
				// read new message
				data_from_client = (int[]) input.readObject();
				//display message
				System.out.print("\nData from Client: ");
				for(int i:data_from_client)
					System.out.printf("%4d",i);
				System.out.println();
			}
			catch (ClassNotFoundException classNotFoundException) 
			{
				System.out.println("\nUnknown object type received");
			}
			
			
			//Send even numbers back to the client
			int x = data_from_client[data_from_client.length-1]+1;
			int[] evens = new int[x/2];
			System.out.print("Sending back to Client: ");
			for(int i=0; i<x/2; i++)
			{
				evens[i] = i*2;
				System.out.printf("%5d", evens[i]);
			}
			sendData(evens);
			

		} while(data_from_client[0] != -1);//Terminate if client sends -1
	}

	// close streams and socket
	private void closeConnection()
	{
		System.out.println("\nTerminating connection\n");
		try
		{
			output.close(); // close output stream
			input.close(); // close input stream
			connection.close(); // close socket
		}
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

	// send message to client
	private void sendData(int[] message)
	{
		try // send object to client
		{
			output.writeObject(message);
			output.flush(); // flush output to client
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