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
	private static final int EXPECTING_STRING = 0;
	private static final int EXPECTING_INT_ARRAY = 1;
	private static final int EXPECTING_DOUBLE_ARRAY = 2;
	private static final int FINISHED = 3;
	
	private int state = EXPECTING_STRING;
	
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
					waitForConnection(); // wait for a connection
					getStreams(); // get input & output streams
					processConnection(); // process connection
				}
				catch (EOFException eofException)
				{
					System.out.println("\nServer terminated connection in state "+state);
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
		do // process messages sent from client
		{
			try // read message and display it
			{
				// read new message
				if(state == EXPECTING_STRING)
				{
					//Get data from input stream
					String data_from_client = (String)input.readObject();
					//display message on the server side
					System.out.println("\nData from Client: "+data_from_client);
					//Send acknowledgement of receipt to the client
					sendData("String recieved.");
					//Transition into next state
					state = EXPECTING_INT_ARRAY;
				}
				else if(state == EXPECTING_INT_ARRAY)
				{
					//Get data from input stream
					int[] data_from_client = (int[])input.readObject();
					//display message on the server side
					System.out.print("\nData from Client: ");
					for(int i:data_from_client)
						System.out.printf("%6d",i);
					System.out.println();
					//Send acknowledgement of receipt to the client
					sendData("int[] recieved.");
					//Transition into next state
					state = EXPECTING_DOUBLE_ARRAY;
				}
				else if(state == EXPECTING_DOUBLE_ARRAY)
				{
					//Get data from input stream
					double[] data_from_client = (double[])input.readObject();
					//display message on the server side
					System.out.println("\nData from Client: ");
					for(double i:data_from_client)
						System.out.printf("%8.2f",i);
					System.out.println();
					//Send acknowledgement of receipt to the client
					sendData("double[] recieved.");
					//Transition into next state
					state = FINISHED;
				}
				else
				{
					//Maybe put an error check here.
				}
			}
			catch (ClassNotFoundException classNotFoundException) 
			{
				System.out.println("\nUnknown object type received");
			}
			catch (EOFException eofException) 
			{
				//readObject() throws an EOFException when the stream is empty. We don't want that to terminate the connection. We want to instead terminate only when all the data has been transmitted, so this catch ignores the exception and allows the do-while loop to continue.
			}
		} while(state != FINISHED);//Terminate when finished state is reached

		//Send terminate message to the client
		sendData("TERMINATE");
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
	private void sendData(String message)
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