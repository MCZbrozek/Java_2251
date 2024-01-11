import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

public class ExampleBoardClearSpace extends Board
{
	//List of collision bitmasks so only the
	//things we want to collide do collide.
	public static final int player_bitmask =	1;
	public static final int wall_bitmask =	  2;

	//Reference to player 1's craft
	//private ExampleShip player1;
	private ExampleGridMover player1;

	private DisplayDot[][] dotGrid;
	
	public ExampleBoardClearSpace()
	{
		super();
		//Create the player
		//player1 = new ExampleShip(100, 100,
		player1 = new ExampleGridMover(100, 100,
				40.0,
				"images/fighter_hull02.gif",
				key_setter,
				player_bitmask,
				wall_bitmask,
				this);
		this.addSprite(player1);
		
		//Create an array of DisplayDots
		//TODO: This array is not actually used or needed at present, simply adding the dots as sprites is all that's needed.
		dotGrid = new DisplayDot[10][10];
		for(int i=0; i<dotGrid.length; i++)
		{
			for(int j=0; j<dotGrid[i].length; j++)
			{
				dotGrid[i][j] = new DisplayDot(
					j*30, // x
					i*30, // y
					0, // Draw countdown (unused)
					10, // radius
					Color.GREEN,
					true, //won't disappear until collided
					wall_bitmask, //uses "I am a wall" bitmask so player hits it
					player_bitmask); //uses "I hit player" so it hits the player
					
				this.addSprite(dotGrid[i][j]);
			}
		}
	}

	protected void doDrawHUD(Graphics g)
	{
		super.doDrawHUD(g);
		//Print x,y coordinates of the ship
		Font font = new Font("Serif", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.YELLOW);
		g.drawString("Coordinates: "+Integer.toString((int)this.player1.getX())+", "+Integer.toString((int)this.player1.getY()),
				550, Main.ScreenHeight - 50);	
	}
	
	public void centerOnPlayer()
	{
		this.centerOnSprite(player1);
	}
}