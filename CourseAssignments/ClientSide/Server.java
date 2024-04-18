
/*
Name: Michael Zbrozek
Date: 04/09/2024
Purpose: Establish a connection and pass data to server, retrieve a response
Sources:
Modeled after Instructor_content/module50_networking/025_client_server_pass_object 

Finally, an assignment where I didn't use ChatGPT! woohoo!

Files: 
Main.Java
ClientTest.java
Client.java
ServerTest.java
Server.java

*/
// Server portion of a client/server stream-socket connection.
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.FileWriter;

public class Server {
	// output stream to client
	private ObjectOutputStream output;
	// input stream from client
	private ObjectInputStream input;
	// server socket
	private ServerSocket server;
	// connection to client
	private Socket connection;
	// counter of number of connections
	private int counter = 1;

	// Data we expect to receive from client:
	private String clientName = null;
	private int[] clientNumbers = null;

	// set up and run server
	public void runServer() {
		try // set up server to receive connections; process connections
		{
			// ServerSocket(int port, int backlog)
			// backlog is how many clients can wait in the queue
			server = new ServerSocket(55555, 100);

			while (true) {
				try {
					waitForConnection();
					getStreams();
					processConnection();
				} catch (EOFException eofException) {
					System.out.println("\nServer terminated connection");
				} finally {
					closeConnection();
					++counter;
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// wait for connection to arrive, then display connection info
	private void waitForConnection() throws IOException {
		System.out.println("Waiting for connection\n");

		// allow server to accept connection
		connection = server.accept();

		System.out.println("Connection " + counter + " received from: " + connection.getInetAddress().getHostName());
	}

	// get streams to send and receive data
	private void getStreams() throws IOException {
		// set up output stream for objects
		output = new ObjectOutputStream(connection.getOutputStream());

		// flush output buffer to send header information
		output.flush();

		// set up input stream for objects
		input = new ObjectInputStream(connection.getInputStream());

		System.out.println("\nGot I/O streams\n");
	}

	// process connection with client
	private void processConnection() {
		try // read message and display it
		{
			// Read client's name
			clientName = (String) input.readObject();

			// Read client's numbers
			clientNumbers = (int[]) input.readObject();

			// Save the data to file and return the secret number
			int secretNumber = processClientData();

			// Send secret number back to client.
			sendData(secretNumber);
		} catch (ClassNotFoundException classNotFoundException) {
			System.out.println("\nUnknown object type received");
			sendData("ERROR: Unknown object type received by the server.");
		} catch (ClassCastException classCastException) {
			System.out.println("\nClass cast exception");
			sendData("ERROR: Class cast exception occurred on server side.");
		} catch (EOFException eofException) {
			// readObject() throws an EOFException when the stream is empty.
			System.out.println("eofException occurred.");
			sendData("ERROR: EOFException caught by the server.");
		} catch (Exception e) {
			System.out.println("Generic Exception occurred.");
			sendData("ERROR: Exception occurred on server side.");
		}
		// Send terminate message to the client
		sendData("TERMINATE");
	}

	// close streams and socket
	private void closeConnection() {
		System.out.println("\nTerminating connection\n");
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// send message to client
	private void sendData(Object message) {
		try // send object to client
		{
			output.writeObject(message);
			output.flush(); // flush output to client
		} catch (IOException ioException) {
			System.out.println("\nError writing object");
		}
	}

	// SUPER SECRET CALCULATION
	// The calculation shown here
	// will NOT be the actual calculation
	// used for the assignment
	private int secretCalculation() {
		return 666;
		// return clientNumbers[2];
	}

	private int processClientData() {
		int secretNumber = secretCalculation();
		// write output file
		try {
			FileWriter fw = new FileWriter(clientName + ".txt");
			fw.write(clientName + "'s secret number is " + secretNumber + "\n");
			fw.write("Based on array numbers:\n");
			for (int x : clientNumbers) {
				fw.write(String.format("%5d\n", x));
			}
			fw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		return secretNumber;
	}
}