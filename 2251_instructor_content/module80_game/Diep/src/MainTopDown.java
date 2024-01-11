/**
 * Created by nealh_000 on 5/22/2017.
 */
public class MainTopDown
{
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("\nRun as:\n> java MainTopDown [game mode]\nPlease select a particular game mode from among the following options:\nffa - free for all\nteam - team game\nbomb - bomberman\nsurvive - survival mode\npool - billiards mode\nsoccer\ntest - testing/tutorial mode.\nA second input can be used to use spectator mode:\n> java MainTopDown ffa camera");
			System.exit(1);
		}
		BoardTopDown b = new BoardTopDown(args);
		//https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		Main m = new Main(b, true);
		Main.SHOW_COLLISION = false;
		m.runGameLoop();
	}
}
