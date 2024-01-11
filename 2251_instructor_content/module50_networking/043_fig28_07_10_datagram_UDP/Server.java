// Fig. 28.7: Server.java
// Server side of connectionless client/server computing with datagrams.
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server
{
	private DatagramSocket socket; // socket to connect to client

	// set up GUI and DatagramSocket
	public Server()
	{
		try // create DatagramSocket for sending and receiving packets
		{
			socket = new DatagramSocket(5000);
		}
		catch (SocketException socketException)
		{
			socketException.printStackTrace();
			System.exit(1);
		}
	}

	// wait for packets to arrive, display data and echo packet to client
	public void waitForPackets()
	{
		System.out.println("Server up and running and waiting for packets.");
		while (true)
		{
			try // receive packet, display contents, return copy to client
			{
				byte[] data = new byte[100]; // set up packet
				DatagramPacket receivePacket = 
					new DatagramPacket(data, data.length);

				socket.receive(receivePacket); // wait to receive packet

				// display information from received packet 
				System.out.println("\nPacket received:" + 
					"\nFrom host: " + 
					receivePacket.getAddress() + 
					"\nHost port: " + 
					receivePacket.getPort() + 
					"\nLength: " + 
					receivePacket.getLength() + 
					"\nContaining:\n\t" + 
					new String(receivePacket.getData(), 
						0, receivePacket.getLength()));

				sendPacketToClient(receivePacket); // send packet to client
			}
			catch (IOException ioException)
			{
				System.out.println(ioException + "\n");
				ioException.printStackTrace();
			}
		}
	}

	// echo packet to client
	private void sendPacketToClient(DatagramPacket receivePacket)
		throws IOException
	{
		System.out.println("\n\nEcho data to client...");

		// create packet to send
		DatagramPacket sendPacket = new DatagramPacket(
			receivePacket.getData(),
			receivePacket.getLength(),
			receivePacket.getAddress(),
			receivePacket.getPort());

		socket.send(sendPacket); // send packet to client
		System.out.println("Packet sent\n");
	}


	public static void main(String[] args)
	{
		Server application = new Server(); // create server
		application.waitForPackets(); // run server application
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