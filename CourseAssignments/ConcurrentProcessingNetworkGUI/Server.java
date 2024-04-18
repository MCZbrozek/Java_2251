
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
// Imports adapted from ClientSide Assignment
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // output the stream to the client
    private ObjectOutputStream outputStream;

    // input stream from the client
    private ObjectInputStream inputStream;

    // server socket
    private ServerSocket server;

    // connection to the client
    private Socket connection;

    // counter for the number of connections
    private int counter = 1;

    // Data we expect to receive from the client -
    private int[][] matrix1;
    private int[][] matrix2;

    // Set up and run the server
    public void runServer() {
        try {
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

    // wait for the connection to arrive, and then display the connection info to
    // the console
    private void waitForConnection() throws IOException {
        System.out.println("Waiting for connection to client\n");

        // allow the server to accept the connection
        connection = server.accept();

        System.out.println("Connection " + counter + " received from: " + connection.getInetAddress().getHostName());
    }

    // getStreams establishes the output streams for writing bytes to this socket
    // and throws IOException
    private void getStreams() throws IOException {
        // set up output stream for Objects
        outputStream = new ObjectOutputStream(connection.getOutputStream());

        // Flush that buffer
        outputStream.flush();

        // set up the input stream to receive bytes from client
        inputStream = new ObjectInputStream(connection.getInputStream());

        // Display status
        System.out.println("\nEstablished I/O streams\n");
    }

    // Process the connection with the client
    private void processConnection() {

        try {
            // Read the rows and cols
            int rows = (int) inputStream.readObject();
            System.out.println("Rows = " + rows);
            int cols = (int) inputStream.readObject();
            System.out.println("Cols = " + cols);

            // Read the first array
            matrix1 = (int[][]) inputStream.readObject();

            // Read the second array
            matrix2 = (int[][]) inputStream.readObject();

            if (rows != 0) {
                System.out.println("\nReceived rows: " + rows);
            } else {
                System.out.println("Expected ROWS value from MatrixGUI. ERR ln 115 Server.java");
            }

            if (cols != 0) {
                System.out.println("\nReceived cols: " + cols);
            } else {
                System.out.println("Expected COLS value from MatrixGUI. ERR ln 121 Server.java");
            }

            if (matrix1 != null) {
                System.out.println("\nFirst array received: \n");
                print2dArray(matrix1);
            } else {
                System.out.println("Line 92 of Server. Matrix is null");
            }
            if (matrix2 != null) {
                System.out.println("\nSecond array received: \n");
                print2dArray(matrix2);
            } else {
                System.out.println("Line 92 of Server. Matrix is null");
            }

            // send a message saying the the matrices were received
            String message = "Server received matrix object.";
            sendData(message);

            // Add our threading calls
            // Our matrix to hold the result
            int[][] matrixResult = new int[rows][cols];

            matrixResult = runThreads(matrix1, matrix2, matrixResult);
            // Test print result matrix
            System.out.println("");
            MatrixFileProcessor.print2dArray(matrixResult);
            sendObject(matrixResult);

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

    // Send message to the client
    private void sendData(Object message) {
        try {
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
                    "This is the result matrix - it should look the same on the other side of the void --> \n");
            MatrixFileProcessor.print2dArray(object);
            System.out.println();
            outputStream.writeObject(object);
            // Flush that outputStream
            outputStream.flush();
        } catch (IOException ioException) {
            System.out.println("\nError writing object from sendClientName method");
        }
    }

    // Close the streams and socket
    private void closeConnection() {
        System.out.println("\nTerminating connection\n");
        try {
            outputStream.close();
            inputStream.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // print2dArray takes a two-dimensional array as input and prints it out with
    // the rows and columns lined up. You must use System.out.printf.
    public static void print2dArray(int[][] array) {
        // code to print 2dArray
        // source - method I wrote for paintball.java in 1152
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                System.out.printf("%-2d", array[row][col]);
            }
            System.out.print("\n");
        }
    }

    public static int[][] runThreads(int[][] matrix1, int[][] matrix2, int[][] matrixResult) {

        // Instantiate the four ThreadOperationObjecst
        ThreadOperations thread1 = new ThreadOperations(matrix1, matrix2, matrixResult, 1);

        ThreadOperations thread2 = new ThreadOperations(matrix1, matrix2, matrixResult, 2);

        ThreadOperations thread3 = new ThreadOperations(matrix1, matrix2, matrixResult, 3);

        ThreadOperations thread4 = new ThreadOperations(matrix1, matrix2, matrixResult, 4);

        // start them
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        // and join them.
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted exception");
            e.printStackTrace();
            System.exit(1);
        }

        return matrixResult;
    }

}
