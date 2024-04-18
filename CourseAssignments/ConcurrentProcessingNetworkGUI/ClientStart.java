
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

// Adapted from clientSide assignment 
public class ClientStart {
    public static void main(String[] args) {
        // Client application
        Client client;

        // if no command line args are passed then just connect to the local host
        if (args.length == 0) {
            client = new Client("127.0.0.1");
        } else {
            // or use args to connect
            client = new Client(args[0]);
        }
        MatrixGUI appGui = new MatrixGUI(client);
        // once we know where are connecting, run that client!
        client.runClient();

    }

}
