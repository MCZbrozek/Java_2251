
public class MainTopDown
{
	public static void main(String[] args)
	{
		System.out.println("Run from the classes folder with this:");
		System.out.println("> java -cp ..\\..\\GameMinimal\\classes; MainTopDown");
		
		BoardTopDown b = new BoardTopDown();
		//https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		Main m = new Main(b, true);
		Main.SHOW_COLLISION = false;
		m.runGameLoop();
	}
}
