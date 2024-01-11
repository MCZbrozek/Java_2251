/**
 * Created by nealh_000 on 5/22/2017.
 */
public class MainTopDown
{
	public static void main(String[] args)
	{
		boolean centerOnPlayer = args.length>0 && args[0].equals("center");
		
		BoardTopDown b = new BoardTopDown(centerOnPlayer);
		//https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		Main m = new Main(b, true);
		m.SHOW_COLLISION = false;
		m.runGameLoop();
	}
}
