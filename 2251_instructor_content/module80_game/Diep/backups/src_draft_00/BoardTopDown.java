import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class BoardTopDown extends Board
{
	//Original code came largely from this tutorial:
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	private static final long serialVersionUID = 1L;
	//List of collision bitmasks so only the 
	//things we want to collide do collide.
	private static final int ship_bitmask =       1;
	static final int bullet_bitmask =             2;
	private static final int xp_brick_bitmask =   4;
	static final int everything_bitmask = 1+2+4+8+16+32;
	//Reference to player 1
	private Player player1;
	//Reference to the ship the camera is focused on
	private Ship focused;
	//How long until next experience brick spawns
	private double exp_cooldown = 0;
	private double exp_reset = 1.0;

	private Random randnum = new Random();

	//Top and left boundaries default to zero.
	static final int boundary_right = 3000;
	static final int boundary_down = 3000;

	private LevelUpInterface levelui;

	//Create a duplicate copy of list of all ships,
	//enemies and player alike for listing on leaderboard,
	//and for having camera follow different ships.
	private ArrayList<Ship> ranking = new ArrayList<>();
	//Index of the ship the camera is currently centered on
	private int centered_on = 0;

	private boolean display_leaderboard = true;

	BoardTopDown()
	{
		//key_setter.print_key_code = true;

		//Create the player
		player1 = new Player(
				randnum.nextDouble()*boundary_right,
				randnum.nextDouble()*boundary_down,
				20,
				key_setter,
				ship_bitmask,//I am
				everything_bitmask,//I hit
				this);
		this.addSprite(player1);
		ranking.add(player1);

		//Create enemies
		String[] npc_names = {"Montoya","Fishman","Summers","Hillskemper",
				"Kite","Martinek","Friedman","Tenorio","Butler","Holtschulte"};
		for (String npc_name : npc_names)
		{
			Enemy e = new Enemy(
					randnum.nextDouble() * boundary_right,
					randnum.nextDouble() * boundary_down, npc_name,
					20,
					ship_bitmask,//I am
					everything_bitmask,//I hit
					this);
			this.addSprite(e);
			ranking.add(e);
		}

		//Center the camera on the player
		this.centerOnSprite(player1, 0);
		focused = player1;
		levelui = new LevelUpInterface(focused, this);

		//Start the game off with the world populated with bricks
		for (int i = 0; i < 100; i++)
		{
			this.addSprite(
					new ExperienceBrick(randnum.nextDouble()*boundary_right,
							randnum.nextDouble()*boundary_down,
							10,
							3,
							Color.white,
							xp_brick_bitmask,
							everything_bitmask)
			);
		}
	}

	protected void doDrawHUD(Graphics g)
	{
		//Draw boundaries if character is near one
		g.setColor(Color.DARK_GRAY);
		if(focused.getX()-Main.ScreenWidth/2<0)
		{
			g.fillRect(0,0,(int)Math.abs(focused.getX()-Main.ScreenWidth/2),Main.ScreenHeight);
		}else if(focused.getX()+focused.getCollisionRadius()*2+Main.ScreenWidth/2>boundary_right)
		{
			int width = (int)(focused.getX()+focused.getCollisionRadius()*2+Main.ScreenWidth/2)-boundary_right;
			g.fillRect(Main.ScreenWidth-width,0,width,Main.ScreenHeight);
		}
		if(focused.getY()-Main.ScreenHeight/2<0)
		{
			g.fillRect(0,0,Main.ScreenWidth, (int)Math.abs(focused.getY()-Main.ScreenHeight/2));
		}else if(focused.getY()+focused.getCollisionRadius()*2+Main.ScreenHeight/2>boundary_down)
		{
			int height = (int)(focused.getY()+focused.getCollisionRadius()*2+Main.ScreenHeight/2)-boundary_down;
			g.fillRect(0,Main.ScreenHeight-height,Main.ScreenWidth,height);
		}
		//Draw the super hud
		super.doDrawHUD(g);
		//Print x,y coordinates of the ship
		Font font = new Font("Serif", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.YELLOW);
		g.drawString("Coordinates: "+
			Integer.toString((int)focused.getX())+", "+
			Integer.toString((int)focused.getY()),
				550, Main.ScreenHeight - 50);
		//Draw player's experience meter
		g.setColor(Color.PINK);
		g.drawRect(Main.ScreenWidth/2-100, 40, 300, 20);
		g.fillRect(Main.ScreenWidth/2-100, 40, (int)(300*focused.getPercentExperience()), 20);
		g.setColor(Color.WHITE);
		g.drawString("Level: "+focused.getLevel(),
				Main.ScreenWidth/2-94, 57);
		//Draw level up interface
		levelui.draw(g);
		//Sort ranking by points ascending using one pass of
		//bubble sort each time doDrawHUD is called.
		for(int i=0; i<ranking.size()-1; i++)
		{
			Ship s1 = ranking.get(i);
			Ship s2 = ranking.get(i+1);
			//Check for out of order rankings
			if(s1.getCumulativeXP() < s2.getCumulativeXP())
			{	//Swap them
				ranking.set(i, s2);
				ranking.set(i+1, s1);
			}
		}
		//Draw the leaderboard
		if(display_leaderboard)
		{
			font = new Font("Serif", Font.BOLD, 20);
			g.setFont(font);
			int x = Main.ScreenWidth - 170;
			int y = 40;
			int buffer = 5;
			int height = 20;
			Ship s;
			g.drawString("LEADERBOARD", x, y);
			for (int i = 0; i < ranking.size(); i++)
			{
				s = ranking.get(i);
				//Gray out the dead on the leaderboard
				if(s.getHealth()>0){g.setColor(Color.CYAN);}
				else{g.setColor(Color.GRAY);}
				g.drawString(s.getName() + " " + s.getCumulativeXP(), x, y + (buffer + height) * (1 + i));
			}
		}
	}

	protected void updateSprites(double elapsed_seconds)
	{
		super.updateSprites(elapsed_seconds);
		//Spawn exp bricks
		this.spawnExp(elapsed_seconds);
		//Check if a level up plus sign was clicked.
		if(key_setter.checkMouseClicked() && player1.availableLevelUpPoints()>0)
		{
			levelui.levelUp(key_setter.getMouseX(), key_setter.getMouseY());
		}
		//Turn on or off autofire
		if(key_setter.checkKeyReleased(69))
		{
			player1.toggleAutofire();
		}
		//Check mouse down to perform default player attack
		//or level up an attribute
		if(key_setter.getMousePressed())
		{
			player1.attack();
		}
		//if space bar gets pressed, center camera on a new ship
		if(key_setter.checkKeyReleased(32))
		{
			centered_on = (centered_on+1)%ranking.size();
			focused = ranking.get(centered_on);
			this.centerOnSprite(focused, 0);
			levelui.setFocusedShip(focused);
		}
		//r key to toggle display of level up interface
		if(key_setter.checkKeyReleased(82))
		{
			levelui.toggleDisplay();
		}
		//t key to toggle display of leaderboard
		if(key_setter.checkKeyReleased(84))
		{
			display_leaderboard = !display_leaderboard;
		}
	}

	private void spawnExp(double elapsed_seconds)
	{
		exp_cooldown -= elapsed_seconds;
		if(exp_cooldown < 0)
		{
			this.addSprite(
					new ExperienceBrick(randnum.nextDouble()*boundary_right,
								randnum.nextDouble()*boundary_down,
							10,
							3,
							Color.white,
							xp_brick_bitmask,
							everything_bitmask)
			);
			exp_cooldown = exp_reset;
		}
	}
}