import java.awt.*;

public class BoardTopDown extends Board
{
	//Original code came largely from this tutorial:
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	private static final long serialVersionUID = 1L;
	//List of collision bitmasks so only the 
	//things we want to collide do collide.
	public static final int player_bitmask =      1;
	public static final int player_bullet_bitmask=2;
	public static final int enemy_bitmask =       4;
	public static final int enemy_bullet_bitmask =8;
	//Reference to player 1's craft
	private Player player1;
	//How long until next enemy cluster spawns
	private double spawn_cooldown = 0;
	private double spawn_reset = 10.0;

	public BoardTopDown()
	{
		//key_setter.print_key_code = true;
		//Create the player
		player1 = new Player(100, 100, //x,y 
					20.0, //collision_radius
					key_setter,
					player_bitmask,//I am
					0,//I hit
					this);
		this.addSprite(player1);
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
	}

	protected void updateSprites(double elapsed_seconds)
	{
		super.updateSprites(elapsed_seconds);
		//Spawn some enemies
		this.spawnEnemies(elapsed_seconds);
	}
	
	private void spawnEnemies(double elapsed_seconds)
	{
		spawn_cooldown -= elapsed_seconds;
		if(spawn_cooldown < 0)
		{
			for(int i=0 ; i<5; i++)
			{
				//Create an enemy
				Enemy e = new Enemy(350+Math.random()*100,//x
						350+Math.random()*100,//y
						15.0, //collision
						enemy_bitmask,//i am
						player_bitmask+enemy_bitmask,//I hit
						player1,
						this);
				this.addSprite(e);
			}
			spawn_cooldown = spawn_reset;
		}
	}

}