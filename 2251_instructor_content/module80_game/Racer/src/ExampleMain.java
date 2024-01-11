/* Example of how to create the main method
to set up a game. */
public class ExampleMain
{
	public static void main(String[] args)
	{
		System.out.println("Escape - exit game");
		System.out.println("Arrow keys - control ship");
		
		if(args.length < 1)
		{
			System.out.println("You must type in a map to load. For instance: ");
			System.out.println("java -cp ..\\..\\GameMinimal\\classes; ExampleMain mapTest");
			System.exit(1);
		}
		if(!args[0].startsWith("..\\maps\\"))
		{
			args[0] = "..\\maps\\"+args[0];
		}
		if(!args[0].endsWith(".txt"))
		{
			args[0] = args[0]+".txt";
		}

		Board b = new ExampleBoard(args[0]);
		((ExampleBoard)b).centerOnPlayer();
		//https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		boolean fullscreen = true;
		Main m = new Main(b, fullscreen);
		m.SHOW_COLLISION = true;
		m.runGameLoop();
	}
}
