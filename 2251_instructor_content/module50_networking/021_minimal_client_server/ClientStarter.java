import java.io.IOException;

public class ClientStarter
{
	public static void main(String[] args)
	{
		Client c = new Client();
		try {
			c.runClient();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}