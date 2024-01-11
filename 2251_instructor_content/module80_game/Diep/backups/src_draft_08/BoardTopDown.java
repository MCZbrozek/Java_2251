import java.awt.*;
import java.util.ArrayList;

public class BoardTopDown extends Board
{
	//Original code came largely from this tutorial:
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	private static final long serialVersionUID = 1L;
	//List of collision bitmasks so only the 
	//things we want to collide do collide.
	static final int ship_bitmask =       1;
	static final int bullet_bitmask =             2;
	static final int xp_brick_bitmask =   4;
	static final int everything_bitmask = 1+2+4+8+16+32;
	//Reference to player 1
	private Player player1;
	//Reference to the ship the camera is focused on
	private Ship focused;

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

		//Choose game mode:
		//setupFFA();
		setupBomberman();
		//setupSurvivalMode();
	}

	private void setupFFA()
	{
		//Create the player
		player1 = new Player(
				Constants.getRandomX(),
				Constants.getRandomY(),
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
		Color[] colors = {Color.white, Color.red, Color.blue, Color.cyan,
				Color.pink, Color.yellow, Color.green, Color.gray,
				Color.orange, Color.magenta};
		int[] strategies = {Enemy.LEVELUP_RAM, Enemy.LEVELUP_RAM, //Better than other rammer, I think.
				Enemy.LEVELUP_RAM_B, Enemy.LEVELUP_RAM_B,
				Enemy.LEVELUP_RANDOM, Enemy.LEVELUP_RANDOM,
				Enemy.LEVELUP_SHOOTER_B, Enemy.LEVELUP_SHOOTER_B, //Better than other shooter, I think.
				Enemy.LEVELUP_SHOOTER, Enemy.LEVELUP_SHOOTER};
		for(int i=0; i<npc_names.length; i++)
		{
			Enemy e = new Enemy(
					Constants.getRandomX(),
					Constants.getRandomY(),
					npc_names[i],
					colors[i],
					20,
					ship_bitmask,//I am
					everything_bitmask,//I hit
					strategies[i],
					this);
			this.addSprite(e);
			ranking.add(e);
		}

		//Center the camera on the player
		this.centerOnSprite(player1, 0);
		focused = player1;
		levelui = new LevelUpInterface(focused, this);

		//Start the game off with the world populated with bricks
		Constants.loadExperienceBricks(this,
				Constants.low_exp_brick_count,
				3, 10, 1, Color.YELLOW,
				Constants.SHAPE_TRIANGLE, false, 1, true);
		Constants.loadExperienceBricks(this,
				Constants.med_exp_brick_count,
				5, 15, 2, Color.GREEN,
				Constants.SHAPE_RECT, false, 1, true);
		Constants.loadExperienceBricks(this,
				Constants.high_exp_brick_count,
				20, 25, 10, Color.CYAN,
				Constants.SHAPE_RECT, false, 1, true);
		Constants.loadExperienceBricks(this,
				Constants.suicide_brick_count,
				1, 10, 5, Color.PINK,
				Constants.SHAPE_TRIANGLE, true, 1, true);

		//Make some powerups and add them to the game
		for (int i = 0; i < 2; i++)
		{
			PowerUp p = new PowerUp(
					Constants.getRandomX(),
					Constants.getRandomY(),
					1,
					0,
					"images/powerup.png",
					BoardTopDown.xp_brick_bitmask,
					BoardTopDown.everything_bitmask,
					this);
			this.addSprite(p);
		}
	}

	private void setupBomberman()
	{
		//make a smaller arena
		Constants.boundary_right = 2000;
		Constants.boundary_down = 2000;

		//Bullets deal no default damage but are faster.
		//No regeneration
		for (int i = 0; i < Constants.bullet_damage_levels.length; i++)
		{
			Constants.bullet_damage_levels[i] = 0;
			Constants.bullet_speed_levels[i] = 400;
			Constants.regen_levels[i] = Double.MAX_VALUE;
		}
		//But bullets explode by default
		Constants.bullet_explodes_default = true;
		Constants.explosion_distance = 100;
		Constants.explosion_impulse = 100;

		//Create the player
		player1 = new Player(
				Constants.getRandomX(),
				Constants.getRandomY(),
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
		Color[] colors = {Color.white, Color.red, Color.blue, Color.cyan,
				Color.pink, Color.yellow, Color.green, Color.gray,
				Color.orange, Color.magenta};
		int[] strategies = {Enemy.LEVELUP_RAM, Enemy.LEVELUP_RAM, //Better than other rammer, I think.
				Enemy.LEVELUP_RAM_B, Enemy.LEVELUP_RAM_B,
				Enemy.LEVELUP_RANDOM, Enemy.LEVELUP_RANDOM,
				Enemy.LEVELUP_SHOOTER_B, Enemy.LEVELUP_SHOOTER_B, //Better than other shooter, I think.
				Enemy.LEVELUP_SHOOTER, Enemy.LEVELUP_SHOOTER};
		for(int i=0; i<npc_names.length; i++)
		{
			Enemy e = new Enemy(
					Constants.getRandomX(),
					Constants.getRandomY(),
					npc_names[i],
					colors[i],
					20,
					ship_bitmask,//I am
					everything_bitmask,//I hit
					strategies[i],
					this);
			this.addSprite(e);
			ranking.add(e);
		}

		//Center the camera on the player
		this.centerOnSprite(player1, 0);
		focused = player1;
		levelui = new LevelUpInterface(focused, this);

		//Start the game off with the world populated with bricks
		Constants.loadExperienceBricks(this,
				550,
				200, 25, 10, Color.CYAN,
				Constants.SHAPE_RECT, false, 0, true);

		//Make some powerups and add them to the game
		for (int i = 0; i < 2; i++)
		{
			PowerUp p = new PowerUp(
					Constants.getRandomX(),
					Constants.getRandomY(),
					1,
					0,
					"images/powerup.png",
					BoardTopDown.xp_brick_bitmask,
					BoardTopDown.everything_bitmask,
					this);
			this.addSprite(p);
		}
	}

	private void setupSurvivalMode()
	{
		//Create the player
		player1 = new Player(
				Constants.getRandomX(),
				Constants.getRandomY(),
				20,
				key_setter,
				ship_bitmask,//I am
				everything_bitmask,//I hit
				this);
		this.addSprite(player1);
		ranking.add(player1);

		this.addSpriteIntangible(new SurvivalModeManager(this, player1));

		//Center the camera on the player
		this.centerOnSprite(player1, 0);
		focused = player1;
		levelui = new LevelUpInterface(focused, this);

		//Start the game off with the world populated with bricks
		Constants.loadExperienceBricks(this,
				Constants.low_exp_brick_count,
				3, 10, 1, Color.YELLOW,
				Constants.SHAPE_TRIANGLE, false, 1, true);
		Constants.loadExperienceBricks(this,
				Constants.med_exp_brick_count,
				5, 15, 2, Color.GREEN,
				Constants.SHAPE_RECT, false, 1, true);
		Constants.loadExperienceBricks(this,
				Constants.high_exp_brick_count,
				20, 25, 10, Color.CYAN,
				Constants.SHAPE_RECT, false, 1, true);

		//Make some powerups and add them to the game
		for (int i = 0; i < 2; i++)
		{
			PowerUp p = new PowerUp(
					Constants.getRandomX(),
					Constants.getRandomY(),
					1,
					0,
					"images/powerup.png",
					BoardTopDown.xp_brick_bitmask,
					BoardTopDown.everything_bitmask,
					this);
			this.addSprite(p);
		}
	}

	protected void doDrawHUD(Graphics g)
	{
		//Draw boundaries if character is near one
		g.setColor(Color.DARK_GRAY);
		if(focused.getX()-Main.ScreenWidth/2<0)
		{
			g.fillRect(0,0,(int)Math.abs(focused.getX()-Main.ScreenWidth/2),Main.ScreenHeight);
		}else if(focused.getX()+focused.getCollisionRadius()*2+Main.ScreenWidth/2>Constants.boundary_right)
		{
			int width = (int)(focused.getX()+focused.getCollisionRadius()*2+Main.ScreenWidth/2)-Constants.boundary_right;
			g.fillRect(Main.ScreenWidth-width,0,width,Main.ScreenHeight);
		}
		if(focused.getY()-Main.ScreenHeight/2<0)
		{
			g.fillRect(0,0,Main.ScreenWidth, (int)Math.abs(focused.getY()-Main.ScreenHeight/2));
		}else if(focused.getY()+focused.getCollisionRadius()*2+Main.ScreenHeight/2>Constants.boundary_down)
		{
			int height = (int)(focused.getY()+focused.getCollisionRadius()*2+Main.ScreenHeight/2)-Constants.boundary_down;
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
			while(focused.getHealth()<=0)
			{
				centered_on = (centered_on+1)%ranking.size();
				focused = ranking.get(centered_on);
			}
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
}