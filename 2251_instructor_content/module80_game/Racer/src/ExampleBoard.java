import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;

/* Example board-extending class, which
creates game elements, places them in the
world and has a custom drawing method to
display relevant info. */
public class ExampleBoard extends Board
{
	public static final Font FONT = new Font("Serif", Font.BOLD, 20);

	//List of collision bitmasks so only the
	//things we want to collide do collide.
	public static final int player_bitmask=1;
	public static final int wall_bitmask = 2;
	//Reference to player 1's craft
	private Ship player1;

	public ExampleBoard(String mapFile)
	{
		super();

		double destinationX = 0;
		double destinationY = 0;
		
		//Load in all the walls from file
		Scanner s = null;
		try
		{
			File f = new File(mapFile);
			s = new Scanner(f);
		}
		catch(IOException e) 
		{
			System.out.println("ERROR! '"+mapFile+"' not found");
			e.printStackTrace();
			System.exit(1);
		}
		int x,y,width,height,radius;
		while(s.hasNext())
		{
			String line = s.nextLine();
			String[] values = line.split(",");
			x = Integer.parseInt(values[1]);
			y = Integer.parseInt(values[2]);
			if(values[0].equals("rect"))
			{
				width = Integer.parseInt(values[3]);
				height = Integer.parseInt(values[4]);
				Wall wall = new Wall(
					x,y,width,height,
					wall_bitmask,//I am
					player_bitmask);//I hit
				this.addSprite(wall);
			}
			else if(values[0].equals("circ"))
			{
				radius = Integer.parseInt(values[3]);
				RoundWall rwall = new RoundWall(
					x,y,radius,
					wall_bitmask,//I am
					player_bitmask);//I hit
				this.addSprite(rwall);
			}
			else if(values[0].equals("finish"))
			{
				destinationX = x;
				destinationY = y;

				radius = Integer.parseInt(values[3]);
				Finish f = new Finish(
					x,y,radius,
					wall_bitmask,//I am
					player_bitmask);//I hit
				this.addSprite(f);
			}
			else
			{
				System.out.println("ERROR! '"+mapFile+"' contains unrecognized shape specifier: "+values[0]);
				System.exit(1);
			}
		} //while(s.hasNext()) //Reading in map
		s.close();
		
		//Create the player
		player1 = new Ship("Neal",
				0,0, //x,y,
				40.0,
				key_setter,
				player_bitmask,
				wall_bitmask,
				this,
				destinationX,
				destinationY);
		this.addSprite(player1);

	}

	protected void doDrawHUD(Graphics g)
	{
		super.doDrawHUD(g);
		//Print x,y coordinates of the ship
		g.setFont(FONT);
		g.setColor(Color.YELLOW);
		g.drawString("Coordinates: "+Integer.toString((int)this.player1.getX())+", "+Integer.toString((int)this.player1.getY()),550, Main.ScreenHeight - 50);
	}
	
	public void centerOnPlayer()
	{
		this.centerOnSprite(player1);
	}
}