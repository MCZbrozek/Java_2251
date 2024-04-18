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

// Imports example from Module50_networking/025_client_server_pass_object
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    // Instance variable declaration

    // Scanner
    private final Scanner user_input = new Scanner(System.in);
    // Output stream to server
    private ObjectOutputStream outputStream;
    // Input stream from server
    private ObjectInputStream inputStream;
    // String - message from server
    private String message = "";
    // Host server for the application
    private String chatServer;
    // Socket which will communicate with the server
    private Socket client;

    // Initialize a chatServer
    public Client(String host) {
        // declare the server to connect to
        chatServer = host;
    }

    // connect to the server and process messages
    public void runClient() {
        // connect, get streams, process connection
        try {
            // Create a socket to make the connection
            connectToServer();
            // get the input and output streams
            getStreams();
            // Process the connection
            processConnection();
        } catch (EOFException eofException) {
            System.out.println("\nClient terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    // Method to connect to the server. This method creates the socket, and prints
    // the connection information.
    private void connectToServer() throws IOException {
        // Print status
        System.out.println("Attempting to connect\n");

        // create a Socket to make the connection to the server
        client = new Socket(InetAddress.getByName(chatServer), (55555));

        // print the connection information so that we can feel good about this code
        System.out.println("Connected to: " + client.getInetAddress().getHostName());
    }

    // getStreams will send and receive data
    private void getStreams() throws IOException {
        // Output stream for objects
        outputStream = new ObjectOutputStream(client.getOutputStream());
        outputStream.flush();
        ; // Flushes the output buffer to send header information

        // Set up input stream for the objects
        inputStream = new ObjectInputStream(client.getInputStream());

        // Print status
        System.out.println("\nGot I/O streams \n");
    }

    // Process connection with server
    private void processConnection() throws IOException {
        String clientName = "";
        int clientNumber;
        int[] clientNumbers = new int[3];
        // variable to store terminate message from server
        String terminateConnection = "";
        // process the messages from the great beyond (aka server).
        // Let user send their name
        System.out.println("Type a name (Last name first) to send? ");
        clientName = user_input.nextLine();
        // if string is not blank, send to the great one.
        if (!clientName.equals("")) {
            sendClientName(clientName);
        }
        // Let user send their numbers
        System.out.println("Type three numbers to send the great beyond: ");
        for (int i = 0; i < clientNumbers.length; i++) {
            System.out.println("Type your number here: ");
            clientNumber = user_input.nextInt();
            clientNumbers[i] = clientNumber;
        }
        // if the array is not empty, send to the great one.
        if ((clientNumbers[0] != 0) && (clientNumbers[2] != 0)) {
            sendClientNumbers(clientNumbers);
        }

        // read message and display it
        do {
            try {

                int secretNumber;
                // receive a message from the great beyond (aka server)
                System.out.println("\nWaiting on a message from the great beyond (aka server)...");

                secretNumber = (int) inputStream.readObject();
                System.out.println("Secret number from the great beyond: ");
                // print the Secret number
                System.out.println(secretNumber);
                terminateConnection = (String) inputStream.readObject();
                System.out.println("\nThe server says: " + terminateConnection);

            } catch (ClassNotFoundException classNotFoundException) {
                System.out.println("\nUnknown object type was received. What cruel fate does this portend? ");
            }
        } while (!terminateConnection.equals("TERMINATE"));
    }

    // Method to close the streams
    private void closeConnection() {
        System.out.println("Closing the portal to the great beyond! ");
        try {
            // Close the output stream
            outputStream.close();
            // Close input stream
            inputStream.close();
            // close the socket
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendClientName(String clientName) {
        // try your best to send an object to the server
        try {
            System.out.println("\n*** Sending:" + "\"" + clientName + "\"" + " to the great beyond ***");
            System.out.println();
            outputStream.writeObject(clientName);
            outputStream.flush(); // Flush that outputstream
        } catch (IOException ioException) {
            System.out.println("\nError writing object from sendClientName method");
        }
    }

    // method to send data to the great beyond (aka server)
    private void sendClientNumbers(int[] clientNumbers) {
        // try your best to send an object to the server
        try {
            System.out.print("*** Sending ");
            for (int i : clientNumbers) {
                System.out.printf("%-4d", i);
            }
            System.out.print(" to the great beyond ***");
            System.out.println();
            outputStream.writeObject(clientNumbers);
            outputStream.flush(); // Flush that outputstream
        } catch (IOException ioException) {
            System.out.println("\nError writing object from sendClientNumbers method");
        }
    }
}
