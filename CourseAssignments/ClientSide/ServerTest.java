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
public class ServerTest {
	public static void main(String[] args) {
		Server myServer = new Server();
		myServer.runServer();
	}
}