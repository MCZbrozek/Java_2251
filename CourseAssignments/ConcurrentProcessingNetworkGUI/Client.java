
/*
Name: Michael Zbrozek
Date: 4/18/2024
Assignment: Networking Part 2
Purpose: Read in matrixes at clientside GUI, split them into 2 matrixes and send to server further subdivide them into quadrants that will be added together by 4 threads and output as a separate matrix print to console.
Sources:
ChatGPT - See prompts in comments
CSCI1152 - Paintball assignment for 2d array scanner and print
GeeksforGeeks - See links in comments
Neal Hotshulte helped me to Debug and gave me some key tricks for making this gizmo run! 


Files: 
Client.java
ClientStart.java
Server.java
ServerStart.java
MatrixFileProcessor.java
MatrixGUI.java
ThreadOperations.java
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
    /* Instance Variables for Client Class */

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

    // Result matrix
    private int[][] resultMatrix;

    // This is the constructor for the class, it takes in the host IP from
    // ClientStart.java to initialize a chatServer
    public Client(String host) {
        // declare the server to connect to
        chatServer = host;
    }

    // Setter and getter for the resultMatrix
    public void setResultMatrix(int[][] resultMatrix) {
        this.resultMatrix = resultMatrix;
    }

    public int[][] getResultMatrix() {
        return resultMatrix;
    }

    public void runClient() {
        // connect, get streams, and process the connection
        try {
            // Create the socket to make the connection
            connectToServer();

            // get the input and output streams
            getStreams();

            // Process the connection - do the exciting functionality that we have planned
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
        // Print the status
        System.out.println("Attempting to connect...\n");

        // create a socket to make the connection to the server
        client = new Socket(InetAddress.getByName(chatServer), (55555));

        // print the connection information so that we can feel good
        System.out.println("Connected to: " + client.getInetAddress().getHostName());
    }

    // getStreams will send and receive our data
    private void getStreams() throws IOException {
        // Output stream for the objects
        outputStream = new ObjectOutputStream(client.getOutputStream());
        // Flushes that output buffer to send header information
        outputStream.flush();

        // set up input stream for objects (to receive the matrix calc)
        inputStream = new ObjectInputStream(client.getInputStream());

        // Print the status
        System.out.println("\nGot I/O streams \n");
    }

    // This is where the magic happens and where we should try to pass our matrix
    // objects
    private void processConnection() throws IOException {
        int[][] matrix1;
        int[][] matrix2;
        Object my_object = null;
        // Variable to terminate connection from console for now, but perhaps from the
        // GUI later?
        String terminateConnection = "";

        /* Read the response from the server */
        do {
            try {
                // receive and print the message
                System.out.println("\nWaiting on a message from the great beyond (aka server)...");

                my_object = inputStream.readObject();

                System.out.println("Processing the message...");

                if (my_object instanceof String) {
                    message = (String) my_object;

                    System.out.println(message);
                    if (!message.equals("TERMINATE")) {
                        sendData("String received");
                    }
                } else if (my_object instanceof int[][]) {
                    // Get Data from input stream
                    resultMatrix = (int[][]) my_object;

                    // Display a message on the Client side
                    System.out.println("\nData from server: \n");
                    MatrixFileProcessor.print2dArray(resultMatrix);

                }

            } catch (ClassNotFoundException classNotFoundException) {
                System.out.println("\nUnknown object type was received. What cruel fate does this portend? ");
            }
        } while (!message.equals("TERMINATE"));
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

    // sendData and sendObject are different, one allows me to send the rows and
    // cols and the other handles the sending to the int[][] matrices
    public void sendData(Object message) {
        try {
            System.out.println("Sending -- " + message);
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("\nError writing object");
        }
    }

    // Send the object to the server
    public void sendObject(int[][] object) {
        // Let's try our darndest to send this matrix
        try {
            System.out.println(
                    "Sending the following matrix - it should look the same on the other side of the void --> \n");
            MatrixFileProcessor.print2dArray(object);
            System.out.println();
            outputStream.writeObject(object);
            // Flush that outputStream
            outputStream.flush();
        } catch (IOException ioException) {
            System.out.println("\nError writing object from sendClientName method");
        }
    }
}
