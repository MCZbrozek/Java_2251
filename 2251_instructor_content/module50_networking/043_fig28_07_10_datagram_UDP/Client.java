// Fig. 28.9: Client.java
// Client side of connectionless client/server computing with datagrams.
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.util.Scanner;

public class Client
{
	private DatagramSocket socket; // socket to connect to server
	private Scanner user_input = new Scanner(System.in);

	// set up GUI and DatagramSocket
	public Client()
	{
		try // create DatagramSocket for sending and receiving packets
		{
			socket = new DatagramSocket();
		} 
		catch (SocketException socketException)
		{
			socketException.printStackTrace();
			System.exit(1);
		}
	}

	// wait for packets to arrive from Server, display packet contents
	public void waitForPackets()
	{
		while(true)
		{
			getAndSend();
			
			try // receive packet and display contents
			{
				byte[] data = new byte[100]; // set up packet
				DatagramPacket receivePacket = 
					new DatagramPacket(data,data.length);

				socket.receive(receivePacket); // wait for packet

				// display packet contents
				System.out.println("\nPacket received:"+
					"\nFrom host: "+
					receivePacket.getAddress()+
					"\nHost port: "+
					receivePacket.getPort()+
					"\nLength: "+
					receivePacket.getLength()+
					"\nContaining:\n\t"+
					new String(receivePacket.getData(),
						0,receivePacket.getLength()));
			}
			catch (IOException exception)
			{
				System.out.println(exception + "\n");
				exception.printStackTrace();
			}
		}
	}

	private void getAndSend()
	{
		try // create and send packet
		{
			// get message from textfield
			System.out.println("Enter message to send to Server: ");
			String message = user_input.nextLine();
			System.out.println("\nSending packet containing: "+message+"\n");

			byte[] data = message.getBytes(); // convert to bytes
			
			// create sendPacket
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 5000);

			socket.send(sendPacket); // send packet
			System.out.println("Packet sent\n");
		}
		catch (IOException ioException)
		{
			System.out.println(ioException + "\n");
			ioException.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		Client application = new Client();
		application.waitForPackets();
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