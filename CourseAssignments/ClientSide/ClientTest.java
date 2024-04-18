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

public class ClientTest {
    public static void main(String[] args) {
        Client client; // Declaration of client application instance

        // Checks for command line args, or runs the default (will always run default
        // for the purpose of this exercise)
        if (args.length == 0) {
            client = new Client("127.0.0.1"); // Connects to localhost
        } else {
            client = new Client(args[0]); // use args to define IP
        }
        // run the client application
        client.runClient();
    }
}
