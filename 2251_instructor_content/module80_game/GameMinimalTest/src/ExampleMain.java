/* Example of how to create the main method
to set up a game. */
public class ExampleMain
{
	public static void main(String[] args)
	{
		System.out.println("Escape - exit game");
		System.out.println("Arrow keys - control ship");
		System.out.println("l \"el\" key - shoot invisible line and print what it runs into. This code is in ExampleShip.");
		
		System.out.println("\nRun with flag -center for a center camera, as in\njava ExampleMain -center");
		
		System.out.println("\nRun with flag -clear for a dot-clearing \"game\", as in\njava ExampleMain -clear");

		Board b = null;
		
		if(args.length > 0 && args[0].equals("-clear"))
		{
			b = new ExampleBoardClearSpace();
			((ExampleBoardClearSpace)b).centerOnPlayer();
		}
		else
		{
			b = new ExampleBoard();
			if(args.length > 0 && args[0].equals("-center"))
			{
				((ExampleBoard)b).centerOnPlayer();
			}
		}

		//https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		boolean fullscreen = true;
		Main m = new Main(b, fullscreen);
		m.SHOW_COLLISION = true;
		m.runGameLoop();
	}
}
