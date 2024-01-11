import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.security.SecureRandom;

public class BoardTopDown extends Board
{
	//Original code came largely from this tutorial:
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	private static final long serialVersionUID = 1L;
	
	private static final int PICKUP_COUNT = 50;

	private long startTime;
	private double finalTime;

	//Reference to player 1
	private Player player1;

	public BoardTopDown()
	{
		//key_setter.print_key_code = true;

		//Create the player
		player1 = new PlayerNeal(this);
		//player1 = new PlayerExample(this);
		
		this.addSprite(player1);

		//Center camera on the player.
		this.centerOnSprite(player1);
		
		this.createPickups(
			-200, // x/y minimum
			600, // x/y maximum
			PICKUP_COUNT, Color.GREEN);
		
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
		/*
		//Draw player attack cooldowns
		int cooldowns_x = 20;
		int cooldowns_y = 35;
		int spacing = 30;
		int width = 100;
		int height = 20;
		Attack[] attacks = player1.getAttacks();
		//Draw cooldown bars
		g.setColor(Color.BLUE);
		for (int i = 0; i < attacks.length; i++)
		{
			g.fillRect(cooldowns_x,
					cooldowns_y+spacing*i,
					(int)(width*attacks[i].getPercentCooldown()), height);
		}
		//Draw labels on the cooldown bars
		g.setColor(Color.WHITE);
		for (int i = 0; i < attacks.length; i++)
		{
			g.drawString(attacks[i].getName(),
					cooldowns_x, cooldowns_y + spacing * i + 16);
		}*/
		
		//Display pickup count
		g.setColor(Color.WHITE);
		g.drawString(
			"Pickups acquired: "+Integer.toString(player1.getCollisionCount())+" / "+Integer.toString(PICKUP_COUNT),
			50, 80);
		
		//Display elapsed time
		String s = "";
		if(player1.getCollisionCount() != PICKUP_COUNT){
			long elapsedTime = System.nanoTime() - startTime;
			double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
			s = String.format("Elapsed time: %.0f seconds\n", elapsedTimeInSecond);
			finalTime = elapsedTimeInSecond;
		}else{
			s = String.format("Final time: %.2f seconds\n", finalTime);
		}
		g.drawString(s, 50, 120);
	}

	protected void updateSprites(double elapsed_seconds)
	{
		super.updateSprites(elapsed_seconds);
	}
	
	private void createPickups(int x_min, int x_max, int count, Color color)
	{
		SecureRandom random = new SecureRandom();
		for(int i=0; i<count; i++)
		{
			Pickup p = new Pickup(
				random.nextInt(x_max-x_min)+x_min, // x
				random.nextInt(x_max-x_min)+x_min, // y
				10, //radius
				color,
				player1,
				this);
			this.addSprite(p);
		}
	}
	
}