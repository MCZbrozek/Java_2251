/**
 * Created by nealh_000 on 5/22/2017.
 */
public class MainTopDown
{
	public static void main(String[] args)
	{
		BoardTopDown b = new BoardTopDown();
		//https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		Main m = new Main(b, true);
		m.SHOW_COLLISION = false;
		m.runGameLoop();
	}
}
