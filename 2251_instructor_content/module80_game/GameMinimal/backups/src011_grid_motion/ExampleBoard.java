import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;

/* Example board-extending class, which
creates game elements, places them in the
world and has a custom drawing method to
display relevant info. */
public class ExampleBoard extends Board
{
    //List of collision bitmasks so only the
    //things we want to collide do collide.
    public static final int player_bitmask =    1;
    public static final int wall_bitmask =      2;
    //Reference to player 1's craft
    private ExampleShip player1;

    public ExampleBoard()
    {
        super();
        //Create the player
        player1 = new ExampleShip(100, 100,
                40.0,
                "images/fighter_hull02.gif",
                key_setter,
                player_bitmask,
                wall_bitmask,
                this);
        this.addSprite(player1);
        //Add an example enemy
        ExampleEnemy enemy = new ExampleEnemy(1000, 50,
                20,
                "images/fighter_hull02.gif",
                player1,
                player_bitmask,
                0);
        this.addSprite(enemy);
        //Add a wall small square wall
        ExampleWall wall = new ExampleWall(400,140,//x,y
                150,150,//width, height
                wall_bitmask,//I am
                player_bitmask);//I hit
        this.addSprite(wall);
        //Add a longer, thinner wall
        wall = new ExampleWall(600,550,//x,y
                500,50,//width, height
                wall_bitmask,//I am
                player_bitmask);//I hit
        this.addSprite(wall);
        //Add a round wall
        ExampleRoundWall rwall = new ExampleRoundWall(800,250,//x,y
                100,//radius
                wall_bitmask,//I am
                player_bitmask);//I hit
        this.addSprite(rwall);
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