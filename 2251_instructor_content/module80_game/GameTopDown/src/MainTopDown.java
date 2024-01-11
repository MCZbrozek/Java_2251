
public class MainTopDown
{
	public static void main(String[] args)
	{
		System.out.println("Run with from the classes folder with either this:");
		System.out.println("> java -cp ..\\..\\GameMinimal\\classes; MainTopDown -center");
		System.out.println("or this:");
		System.out.println("> java -cp ..\\..\\GameMinimal\\classes; MainTopDown");
		
		boolean centerOnPlayer = args.length>0 && args[0].equals("-center");
		
		BoardTopDown b = new BoardTopDown(centerOnPlayer);
		//https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		Main m = new Main(b, true);
		Main.SHOW_COLLISION = false;
		m.runGameLoop();
	}
}
