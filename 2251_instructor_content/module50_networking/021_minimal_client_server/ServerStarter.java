import java.io.IOException;

public class ServerStarter
{
	public static void main(String[] args)
	{
		Server my_server = new Server();
		try {
			my_server.runServer();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}