import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.security.SecureRandom;

public class BoardTopDown extends Board
{
	//Original code came largely from this tutorial:
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	private static final long serialVersionUID = 1L;
	
	private static final int DOT_RADIUS = 30;
	
	private static final int PICKUP_COUNT = 50;

	private long startTime;
	private double finalTime;

	//Reference to player 1
	private Player player1;
	
	private boolean obstacleMode;
	private int timeLimit; //seconds

	//Constructor
	public BoardTopDown(long seed, boolean obstacleMode, int timeLimit)
	{
		this.obstacleMode = obstacleMode;
		this.timeLimit = timeLimit;
		
		//key_setter.print_key_code = true;

		//Create the player
		//player1 = new PlayerNeal(this);
		player1 = new PlayerExample(this);
		
		this.addSprite(player1);

		//Center camera on the player.
		this.centerOnSprite(player1);
		
		this.createPickups(
			-200, // x/y minimum
			600, // x/y maximum
			PICKUP_COUNT, Color.GREEN,
			seed);
		
		if(obstacleMode)
		{
			this.createPickups(
				-200, // x/y minimum
				600, // x/y maximum
				PICKUP_COUNT, Color.RED,
				seed+1);
		}
		
		//Start game timer
		startTime = System.nanoTime();
	}

	protected void doDrawHUD(Graphics g)
	{
		super.doDrawHUD(g);
		//Print x,y coordinates of the ship
		Font font = new Font("Serif", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.YELLOW);
		g.drawString("Coordinates: "+
			Integer.toString((int)this.player1.getX())+", "+
			Integer.toString((int)this.player1.getY()),
				550, Main.ScreenHeight - 50);
		
		//Display pickup count
		g.setColor(Color.WHITE);
		if(obstacleMode)
		{
			int green = player1.getGreenCollisionCount();
			int red = player1.getRedCollisionCount();
			g.drawString(
				"Points == "+green+" - "+red+" == "+(green - red),
				50, 80);
		}
		else
		{
			g.drawString(
				"Pickups acquired: "+player1.getGreenCollisionCount()+" / "+PICKUP_COUNT,
				50, 80);
		}
		
		//Display elapsed time until
		//all green circles are collected
		//or time limit is reached
		String s = "";
		if(player1.getGreenCollisionCount() != PICKUP_COUNT && (timeLimit!=-1 && getElapsedSeconds() < timeLimit)){
			double elapsedTimeInSecond = getElapsedSeconds();
			s = String.format("Elapsed time: %.0f seconds\n", elapsedTimeInSecond);
			finalTime = elapsedTimeInSecond;
		}else{
			s = String.format("Final time: %.2f seconds\n", finalTime);
		}
		g.drawString(s, 50, 120);
	}

	protected void updateSprites(double elapsed_seconds)
	{
		//Keep playing until time runs out
		if(timeLimit==-1 || getElapsedSeconds() < timeLimit)
			super.updateSprites(elapsed_seconds);
	}
	
	private void createPickups(int x_min, int x_max, int count, Color color, long seed)
	{
		SecureRandom random = new SecureRandom();
		if(seed!=-1)
			random.setSeed(seed);

		for(int i=0; i<count; i++)
		{
			Pickup p = new Pickup(
				random.nextInt(x_max-x_min)+x_min, // x
				random.nextInt(x_max-x_min)+x_min, // y
				DOT_RADIUS, //radius
				color,
				player1,
				this);
			this.addSprite(p);
		}
	}
	
	private double getElapsedSeconds()
	{
		long elapsedTime = System.nanoTime() - startTime;
		return (double) elapsedTime / 1_000_000_000;
	}
	
}