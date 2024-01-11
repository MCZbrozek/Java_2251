import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Main
{
	//The JPanel that the game runs in
	private GameFrame game_frame;	
	//If the game should be running or not.
	private boolean running = true;
	//Make image loading available and easy
	public static ImageManager image_manager = new ImageManager();
	//Set screen dimensions
	public static int ScreenWidth = 1200;
	public static int ScreenHeight = 1000;
	//If FULLSCREEN is true, the previous dimensions 
	//will be overwritten
	public static boolean FULLSCREEN = true;
	//Use show_collision for debugging to check collisions.
	public static boolean SHOW_COLLISION = false;

	//The time it takes for one action to occur
	private static long ACTION_TIME = 500_000_000;

	//Reference to player 1's craft
	private static ExampleGridRobot player1;

	private static Board b;

	public static void main(String[] args)
	{
		System.out.println("Enter the filename to load the map from when you run the program, for example:");
		System.out.println("> java Main map01_L.txt");
		System.out.println("Escape - exit game.");
		System.out.println("Press the space bar to begin running. The game will remain paused until that point.");
		
		b = new Board(args[0]);
		
		b.setPaused(true);
		
		//Create the player
		player1 = new ExampleGridRobot(0,0, //x, y
			40.0,
			"images/fighter_hull02.gif");
		b.addSprite(player1);
		b.centerOnSprite(player1);
		
		boolean fullscreen = true;
		Main m = new Main(fullscreen);
		m.SHOW_COLLISION = true;
		m.runGameLoop();
	}


	public Main()
	{
		game_frame = new GameFrame(b);
	}

	//Overload Main to take fullscreen argument
	public Main(boolean fullscreen)
	{
		this.FULLSCREEN = fullscreen;
		if(FULLSCREEN)
		{
			//Get computer screen size
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Main.ScreenWidth = (int)screenSize.getWidth();
			Main.ScreenHeight = (int)screenSize.getHeight();
		}
		game_frame = new GameFrame(b);
	}

	//Overload Main to take dimension arguments
	public Main(int width, int height)
	{
		Main.ScreenWidth = width;
		Main.ScreenHeight = height;
		game_frame = new GameFrame(b);
	}

	//Starts a new thread and runs the game loop in it.
	public void runGameLoop()
	{
		Thread loop = new Thread()
		{
			public void run()
			{
				while (b.isPaused())
				{	//looping until unpause
					basicGameUpdates(1_000_000);
					//System.out.println("A");
				}
				gameLoop();
				while (true)
				{	//Loop until escape key pressed
					basicGameUpdates(1_000_000);
					//System.out.println("B");
				}
			}
		};
		loop.start();
	}

	public void gameLoop()
	{
		//The following is MOST of the way to a solution, but not entirely.
		/* //SOLUTION to map03_islands.txt
		for(int j=0; j<4; j++){
			//forward until wall
			while(!isWallAhead())
				forward();
			//loop around wall
			//for(int i=0;i<8;i++){
			while(isPelletAhead()){
				turnLeft();
				forward();
				while(!isWallAhead()){
					turnRight();
					forward();
				}
			}
			//face wall
			while(!isWallAhead())
				turnRight();
			//face away from wall
			turnRight();
			turnRight();
			//repeat 3 more times
		}
		*/
		
		/* //SOLUTION to map02_snake.txt
		for(int i=0; i<25; i++)
		{
			forward();
			System.out.println("Wall ahead? "+isWallAhead());
			while(isWallAhead()){
				turnRight();
			}
			forward();
			System.out.println("Wall ahead? "+isWallAhead());
			while(!isWallAhead()){
				turnLeft();
				forward();
			}
		}*/
		
		
		
		/* //SOLUTION to map02_neck.txt
		for(int i=0; i<9; i++)
		{
			forward();
		}
		turnRight();
		forward();
		forward();
		forward();
		turnRight();
		for(int i=0; i<9; i++)
			forward();
		turnLeft();
		turnLeft();
		for(int i=0; i<4; i++)
			forward();
		turnRight();
		for(int i=0; i<7; i++)
			forward();
		turnRight();
		for(int i=0; i<4; i++)
			forward();
		turnLeft();
		forward();
		forward();
		turnLeft();
		for(int i=0; i<9; i++)
			forward();
		turnLeft();
		forward();
		forward();
		turnLeft();
		for(int i=0; i<3; i++)
			forward();
		turnRight();
		for(int i=0; i<4; i++)
			forward();
		*/
		
	} //public void gameLoop()



	private void turnLeft()
	{
		System.out.println("turning left");
		//Loop for smoother movement
		int loopLimit = 5;
		double[] angles = player1.getLeftTurnInterp(loopLimit);
		for(int i=0; i<loopLimit; i++)
		{
			player1.setAngle(angles[i]);
			basicGameUpdates(ACTION_TIME/loopLimit);
		}
	}
	private void turnRight()
	{
		System.out.println("turning right");
		//Loop for smoother movement
		int loopLimit = 5;
		double[] angles = player1.getRightTurnInterp(loopLimit);
		for(int i=0; i<loopLimit; i++)
		{
			player1.setAngle(angles[i]);
			basicGameUpdates(ACTION_TIME/loopLimit);
		}
	}
	private void forward()
	{
		System.out.println("forward");
		//Loop for smoother movement
		int loopLimit = 5;
		for(int i=0; i<loopLimit; i++)
		{
			player1.moveFixed(player1.getHeight()/loopLimit);
			basicGameUpdates(ACTION_TIME/loopLimit);
		}
	}
	
	//Returns true if there's a wall ahead of the player
	private boolean isWallAhead()
	{
		double[] xy = player1.getPositionAhead();
		ArrayList<SpritePhysics> collisions = b.getAllWithinDistance((int)xy[0], (int)xy[1], Board.defaultDistance*2); //double distance for better coverage
		for(SpritePhysics sp : collisions)
		{
			if(sp instanceof ExampleWall)
				return true;
		}
		//System.out.println("Size: "+collisions.size());
		return false;
	}

	//Returns true if there's a pellet ahead of the player
	private boolean isPelletAhead()
	{
		double[] xy = player1.getPositionAhead();
		ArrayList<SpritePhysics> collisions = b.getAllWithinDistance((int)xy[0], (int)xy[1], Board.defaultDistance*2); //double distance for better coverage
		for(SpritePhysics sp : collisions)
		{
			if(sp instanceof DisplayDot)
				return true;
		}
		//System.out.println("Size: "+collisions.size());
		return false;
	}
	
	private void basicGameUpdates(long yield_time)
	{
		//Update movement and check collisions
		b.update(1.0);
		//draw game
		game_frame.internalUpdateGraphicsInterpolated();
		//yield
		long now = System.nanoTime();
		while(System.nanoTime() - now < yield_time)
		{
			Thread.yield();
		}
	}
}