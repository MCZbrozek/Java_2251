import java.awt.Dimension;
import java.awt.Toolkit;

public class Main
{
	//The JPanel that the game runs in
	private GameFrame game_frame;	
	//If the game should be running or not.
	private boolean running = true;
	private boolean paused = false;
	//Frames per second and frame count
	private int fps = 60;
	private int frame_count = 0;
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
	//Set this true to measure where the program's
	//time is being spent.
	public static boolean USE_PROFILER = true;
	//Here we create a large scale profiler to look generally
	//at time spent drawing, computing, and yielding.
	private static final int DRAW_INDEX = 0;
	private static final int COMPUTE_INDEX = 1;
	private static final int YIELD_INDEX = 2;
	public static final String[] broad_profiler_labels = {"Drawing    ", "Computing", "Yielding    "};
	public static Profiler profiler = new Profiler(broad_profiler_labels);

	public Main() 
	{
		if(FULLSCREEN)
		{
			//Get computer screen size
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Main.ScreenWidth = (int)screenSize.getWidth();
			Main.ScreenHeight = (int)screenSize.getHeight();
		}
		game_frame = new GameFrame();
	}
	
    public void goBackToGame()
    {
		this.paused = false;
		//ReCreate the game frame and board within it
		game_frame.dispose();
		game_frame = new GameFrame();
		game_frame.toFront();
    }
    
	//Starts a new thread and runs the game loop in it.
	public void runGameLoop()
	{
		Thread loop = new Thread()
		{
			public void run()
			{
				gameLoop();
			}
		};
		loop.start();
	}

	/* Game loop
	 * The following is the fixed timestep loop from
	 * http://www.java-gaming.org/index.php?topic=24220.0
	 * with modifications to fix a ConcurrentModificationException
	 * which you can read more about here:
	 * http://stackoverflow.com/questions/4097217/the-java-concurrent-modification-exception-debacle-of-2010
	 * http://stackoverflow.com/questions/16620005/concurrent-modification-exception-cause-by-painting
	 * http://stackoverflow.com/questions/17588831/why-is-paintcomponent-continuously-and-asynchronously-being-called-without-exp
	 */
	public void gameLoop() 
	{
		/* This value would probably be stored elsewhere.
		GAME_HERTZ is the number of updates to
		perform per second.
		I believe this needs to equal the number of frames
		per second since I am not using interpolation. */
		final double GAME_HERTZ = 60.0;
		//Calculate how many ns each frame should take for our 
		//target game hertz.
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		//At the very most we will update the game this 
		//many times before a new render.
		//If you're worried about visual hitches more 
		//than perfect timing, set this to 1.
		final int MAX_UPDATES_BEFORE_RENDER = 5;
		//We will need the last update time.
		double lastUpdateTime = System.nanoTime();
		//Store the last time we rendered.
		double lastRenderTime = System.nanoTime();

		//If we are able to get as high as this FPS, don't render again.
		final double TARGET_FPS = 60.0;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		//Simple way of finding FPS.
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while (running)
		{
			//Get current time.
			double now = System.nanoTime();
			int updateCount = 0;

			if (!paused)
			{
				if(USE_PROFILER){ profiler.start(); }
				//Do as many game updates as we need to, potentially playing catchup.
				while(now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER)
				{
					/* Update the game. Pass the time between updates
					 * So that all timings can be based on real-world
					 * time.
					 * If this causes weirdness, we might need to pass 
					 * the actual time between this update and the previous
					 * rather than the desired time between updates,
					 * which is what we are passing here.
					 */
					game_frame.GameUpdate(1.0/GAME_HERTZ);
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}
				if(USE_PROFILER){ profiler.end(Main.COMPUTE_INDEX); }

				//If for some reason an update takes forever, we don't want to do an insane number of catchups.
				//If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
				if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
				{
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				/* I am removing interpolation permanently because of 
				this comment on the page where I got this code from:
				
				http://www.java-gaming.org/index.php?topic=24220.0
				"Say you introduce acceleration.  Suddenly, 
				linearly interpolating the drawx and drawy 
				doesn't make any sense, since the velocity 
				of your ball has changed between frames A and B."
				
				Render. To do so, we need to calculate interpolation 
				for a smooth render.
				This should be a number between 0 and 1.
				interpolation is the percentage of TIME_BETWEEN_UPDATES
				that has elapsed since the last rendering. */
				//float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES) );
				if(USE_PROFILER){ profiler.start(); }
				drawGame(1.0f);
				if(USE_PROFILER){ profiler.end(Main.DRAW_INDEX); }
				lastRenderTime = now;

				//Update the frames we got.
				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if (thisSecond > lastSecondTime)
				{
					//System.out.println("Main: NEW SECOND " + thisSecond + " " + frame_count);
					fps = frame_count;
					frame_count = 0;
					lastSecondTime = thisSecond;
				}

				//Yield until it has been at least the target time between renders. This saves the CPU from hogging.
				if(USE_PROFILER){ profiler.start(); }
				while(now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES)
				{
					Thread.yield();

					//This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
					//You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
					//FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
					//What's all this then?
					//http://www.java-gaming.org/index.php?topic=24220.0
					//try {Thread.sleep(1);} catch(Exception e) {} 
					//Commenting above line increases frame rate dramatically.

					now = System.nanoTime();
				}
				if(USE_PROFILER){ profiler.end(Main.YIELD_INDEX); }
			} //if (!paused)
		} //while (running)
	} //public void gameLoop()

	private void drawGame(float interpolation)
	{
		/* The old way spawns a nasty extra thread. 
		 * This new way ought not to according to 
		 * http://www.java-gaming.org/index.php?topic=24220.0
		 * There is more information about this, just search for
		 * ConcurrentModificationException
		 * in this file.
		 */
		//OLD WAY START
		//game_frame.board.setFPS(fps);
		//game_frame.board.setInterpolation(interpolation);
		//game_frame.board.repaint();
		//OLD WAY END
		game_frame.internalUpdateGraphicsInterpolated(fps, interpolation);
		frame_count++;
	}

	public static void main(String[] args) 
	{
		//https://docs.oracle.com/javase/tutorial/essential/concurrency/runthread.html
		Main m = new Main();
		m.runGameLoop();
	}
}