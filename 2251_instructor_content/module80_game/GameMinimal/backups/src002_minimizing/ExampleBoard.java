import java.awt.*;

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
        player1 = new ExampleShip(100, 100, //x,y
                40.0, //collision_radius
                "images/fighter_hull02.gif", //image_file
                key_setter,
                player_bitmask,//I am
                wall_bitmask); //I hit
        this.addSprite(player1);
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
}