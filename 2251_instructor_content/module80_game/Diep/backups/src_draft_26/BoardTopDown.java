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

	//Pushy walls push players back in bounds.
	//The alternative is walls that are hard and the players will bounce off of.
	boolean pushy_walls = true;

	BoardTopDown()
	{
		//key_setter.print_key_code = true;
		//Choose game mode:
		//setupFFA();
		//setupTeamGame();
		//setupBomberman();
		//setupSurvivalMode();
		setupBilliardsMode();
		//setupSoccerMode();
		//setupTutorialMode();
	}

	private void setupFFA()
	{
		this.addSpriteIntangible(new FFAManager(this));

		//Create the player
		player1 = new Player(
				Constants.getRandomX(),
				Constants.getRandomY(),
				25,
				key_setter,
				Color.blue,
				1,
				ship_bitmask,//I am
				everything_bitmask,//I hit
				this);

		this.addSprite(player1);
		//For spectator mode uncomment below and comment the previous line.
		/*this.addSpriteIntangible(player1);
		player1.speed = 500;
		player1.shape = Constants.SHAPE_MIMIC;
		player1.setCollisionRadius(1);*/

		ranking.add(player1);

		//Create enemies
		String[] npc_names = {"Montoya","Fishman","Summers","Hillskemper",
				"Kite",
				"Martinek",
				"Friedman",
				//"Tenorio",
				"Butler",
				"Holtschulte"};
		Color[] colors = {Color.white, Color.red, Color.blue, Color.cyan,
				Color.pink, Color.yellow, Color.green, Color.orange, Color.magenta};//Color.gray
		NPC_Personality[] strategies = {
				new NPC_A_slightly_larger_can_of_WD40(this),//Montoya
				new NPC_All_Directions(this),//Fishman //FishTheBishBoi
				new NPC_Dylan(this),//Summers
				new NPC_SelfLoathingBot(this),//Hillskemper //IAMTHEFAST
				new NPC_preston(this),//Kite
				new NPC_Smuggley(this),//Martinek
				new NPC_BONNER(this),//Friedman
				//new NPC_HoltShooterB(this),//Tenorio
				new NPC_Clowns_3_The_Return(this),//Butler
				new NPC_HoltRamC(this)};//Holtschulte
		/*NPC_Personality[] strategies = {
				new NPC_HoltRamA(this),//Montoya
				new NPC_HoltRamB(this),//Fishman
				new NPC_HoltRamC(this),//Summers
				new NPC_HoltRamC(this),//Hillskemper
				new NPC_HoltRamD(this),//Kite
				new NPC_Random(this),//Martinek
				new NPC_HoltShooterA(this),//Friedman
				new NPC_HoltShooterB(this),//Tenorio
				new NPC_HoltShooterC(this),//Butler
				new NPC_HoltShooterD(this)};//Holtschulte*/
		/*NPC_Personality[] strategies = {
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this)};*/
		for(int i=0; i<npc_names.length; i++)
		{
			Enemy e = new Enemy(
					Constants.getRandomX(),
					Constants.getRandomY(),
					npc_names[i],
					colors[i],
					2,
					25,
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
				2, 18, 1, Color.YELLOW,
				Constants.SHAPE_TRIANGLE, false, 1, true,
				Constants.default_friction);
		Constants.loadExperienceBricks(this,
				Constants.med_exp_brick_count,
				5, 30, 2, Color.GREEN,
				Constants.SHAPE_RECT, false, 1, true,
				Constants.default_friction);
		Constants.loadExperienceBricks(this,
				Constants.high_exp_brick_count,
				22, 50, 11, Color.CYAN,
				Constants.SHAPE_RECT, false, 1, true,
				Constants.default_friction);
	}

	private void setupTeamGame()
	{
		this.addSpriteIntangible(new FFAManager(this));

		//Create the player
		player1 = new Player(
				Constants.getRandomX(),
				Constants.getRandomY(),
				25,
				key_setter,
				Color.blue,
				1,
				ship_bitmask,//I am
				everything_bitmask,//I hit
				this);

		//this.addSprite(player1);
		//ranking.add(player1);
		//For spectator mode uncomment below and comment the previous lines.
		this.addSpriteIntangible(player1);
		player1.speed = 500;
		player1.shape = Constants.SHAPE_MIMIC;
		player1.setCollisionRadius(1);

		//Create enemies
		String[] npc_names = {
				"Montoya","Fishman","Summers","Hillskemper", "Kite",
				"Martinek", "Friedman", "Tenorio", "Butler", "Holtschulte"};
		Color[] colors = {Color.red, Color.red, Color.red, Color.red,Color.red,
				Color.blue, Color.blue, Color.blue, Color.blue, Color.blue};
		int[] team_ids = {1,1,1,1,1,2,2,2,2,2};
		NPC_Personality[] strategies = {
				new NPC_A_slightly_larger_can_of_WD40(this),//Montoya
				new NPC_All_Directions(this),//Fishman //FishTheBishBoi
				new NPC_Dylan(this),//Summers
				new NPC_SelfLoathingBot(this),//Hillskemper //IAMTHEFAST
				new NPC_preston(this),//Kite
				new NPC_Smuggley(this),//Martinek
				new NPC_BONNER(this),//Friedman
				new NPC_Random(this),//Tenorio
				new NPC_Clowns_3_The_Return(this),//Butler
				new NPC_HoltRamC(this)};//Holtschulte
		for(int i=0; i<npc_names.length; i++)
		{
			Enemy e = new Enemy(
					Constants.getRandomX(),
					Constants.getRandomY(),
					npc_names[i],
					colors[i],
					team_ids[i],
					25,
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
				2, 18, 1, Color.YELLOW,
				Constants.SHAPE_TRIANGLE, false, 1, true,
				Constants.default_friction);
		Constants.loadExperienceBricks(this,
				Constants.med_exp_brick_count,
				5, 30, 2, Color.GREEN,
				Constants.SHAPE_RECT, false, 1, true,
				Constants.default_friction);
		Constants.loadExperienceBricks(this,
				Constants.high_exp_brick_count,
				22, 50, 11, Color.CYAN,
				Constants.SHAPE_RECT, false, 1, true,
				Constants.default_friction);
	}

	private void setupTutorialMode()
	{
		this.addSpriteIntangible(new FFAManager(this));

		//Create the player
		player1 = new Player(
				Constants.getRandomX(),
				Constants.getRandomY(),
				25,
				key_setter,
				Color.blue,
				1,
				ship_bitmask,//I am
				everything_bitmask,//I hit
				this);
		player1.giveXP(999999);
		this.addSprite(player1);
		ranking.add(player1);

		//Center the camera on the player
		this.centerOnSprite(player1, 0);
		focused = player1;
		levelui = new LevelUpInterface(focused, this);

		//Start the game off with the world populated with bricks
		Constants.loadExperienceBricks(this,
				Constants.low_exp_brick_count,
				2, 18, 1, Color.YELLOW,
				Constants.SHAPE_TRIANGLE, false, 1, true,
				Constants.default_friction);
		Constants.loadExperienceBricks(this,
				Constants.med_exp_brick_count,
				5, 30, 2, Color.GREEN,
				Constants.SHAPE_RECT, false, 1, true,
				Constants.default_friction);
		Constants.loadExperienceBricks(this,
				Constants.high_exp_brick_count,
				22, 50, 11, Color.CYAN,
				Constants.SHAPE_RECT, false, 1, true,
				Constants.default_friction);

		//Make some powerups and add them to the game
		for (int i = 0; i < Constants.NUM_POWERUPS; i++)
		{
			PowerUp p = new PowerUp(
					200+i*150,
					200+i*150,
					1,
					0,
					"images/powerup.png",
					BoardTopDown.xp_brick_bitmask,
					BoardTopDown.everything_bitmask,
					this,
					i);
			this.addSprite(p);
		}
	}

	private void setupSoccerMode()
	{
		//Smaller world
		Constants.boundary_down = 1200;
		Constants.boundary_right = 1600;
		//Make walls bouncy
		this.pushy_walls = false;
		//Turn off friction
		//Constants.default_friction = 0;
		//Turn off suicide bricks
		Constants.suicide_brick_count = 0;
		//Make bricks invulnerable
		Constants.brick_vulnerability = false;

		//Create the player
		player1 = new Player(
				Constants.getRandomX(),
				Constants.getRandomY(),
				25,
				key_setter,
				Color.blue,
				2,
				ship_bitmask,//I am
				everything_bitmask,//I hit
				this);
		this.addSprite(player1);
		ranking.add(player1);

		//Center the camera on the player
		this.centerOnSprite(player1, 0);
		focused = player1;
		levelui = new LevelUpInterface(focused, this);

		Ship[] blue_team = new Ship[1];
		int blue_index = 0;
		blue_team[blue_index++] = player1;
		Ship[] red_team = new Ship[1];
		int red_index = 0;

		//Create enemies
		String[] npc_names = {
				"Montoya"};//,"Fishman","Summers","Hillskemper", "Kite", "Martinek", "Friedman", "Tenorio", "Butler", "Holtschulte"};
		Color[] colors = {Color.red, Color.blue};
		int[] team_ids = {1,2};
		NPC_Personality[] strategies = {
				new NPC_Soccer(this),//Montoya
				new NPC_Soccer(this),//Fishman //FishTheBishBoi
				new NPC_Soccer(this),//Summers
				new NPC_Soccer(this),//Hillskemper //IAMTHEFAST
				new NPC_Soccer(this),//Kite
				new NPC_Soccer(this),//Martinek
				new NPC_Soccer(this),//Friedman
				new NPC_Soccer(this),//Tenorio
				new NPC_Soccer(this),//Butler
				new NPC_Soccer(this)};//Holtschulte
		for(int i=0; i<npc_names.length; i++)
		{
			Enemy e = new Enemy(
					Constants.getRandomX(),
					Constants.getRandomY(),
					npc_names[i],
					colors[i],
					team_ids[i],
					25,
					ship_bitmask,//I am
					everything_bitmask,//I hit
					strategies[i],
					this);
			if(team_ids[i]==1){red_team[red_index++] = e;}
			else{blue_team[blue_index++] = e;}
			this.addSprite(e);
			ranking.add(e);
		}

		//Add brick basket to the game on the far right middle of the world
		// for the blue team.
		int brick_basket_radius = 40;
		BrickBasket blue_basket = new BrickBasket(
				Constants.boundary_right-brick_basket_radius*2,
				Constants.boundary_down/2-brick_basket_radius,
				brick_basket_radius,
				Color.blue,
				0,
				xp_brick_bitmask,
				this,
				blue_team
		);
		this.addSprite(blue_basket);

		//Add brick basket to the game on the far left middle of the world
		// for the red team.
		BrickBasket red_basket = new BrickBasket(
				0,
				Constants.boundary_down/2-brick_basket_radius,
				brick_basket_radius,
				Color.red,
				0,
				xp_brick_bitmask,
				this,
				red_team
		);
		this.addSprite(red_basket);

		//Set the baskets for the AIs
		for(int i=0; i<team_ids.length; i++)
		{
			if(team_ids[i]==1){((NPC_Soccer)strategies[i]).team_basket = red_basket;}
			else{((NPC_Soccer)strategies[i]).team_basket = blue_basket;}
		}

		//Put one big round experience brick in middle of screen
		double ball_radius = 100;
		ExperienceBrick e = new ExperienceBrick(
					Constants.boundary_right/2-ball_radius,
					Constants.boundary_down/2-ball_radius,
					ball_radius,
					1,
					1,
					Color.white,
					Constants.SHAPE_CIRCLE,
					false,
					BoardTopDown.xp_brick_bitmask,
					BoardTopDown.everything_bitmask,
					0,
					true,
					this);
			e.friction = -0.2;
			this.addSprite(e);
		}

	private void setupBilliardsMode()
	{
		//Make walls bouncy
		this.pushy_walls = false;
		//Turn up friction (default used to be -1.1)
		Constants.default_friction = -5;
		//Change base speed so ships can still move fast
		Constants.speed_levels[0] = 800;
		//Turn off suicide bricks
		Constants.suicide_brick_count = 0;
		//Make bricks invulnerable
		Constants.brick_vulnerability = false;

		//Create the player
		player1 = new Player(
				Constants.getRandomX(),
				Constants.getRandomY(),
				25,
				key_setter,
				Color.blue,
				2,
				ship_bitmask,//I am
				everything_bitmask,//I hit
				this);
		this.addSprite(player1);
		ranking.add(player1);

		//Center the camera on the player
		this.centerOnSprite(player1, 0);
		focused = player1;
		levelui = new LevelUpInterface(focused, this);

		Ship[] blue_team = new Ship[6];
		int blue_index = 0;
		blue_team[blue_index++] = player1;
		Ship[] red_team = new Ship[5];
		int red_index = 0;

		//Create enemies
		String[] npc_names = {
				"Montoya","Fishman","Summers","Hillskemper", "Kite",
				"Martinek", "Friedman", "Tenorio", "Butler", "Holtschulte"};
		Color[] colors = {Color.red, Color.red, Color.red, Color.red,Color.red,
				Color.blue, Color.blue, Color.blue, Color.blue, Color.blue};
		int[] team_ids = {1,1,1,1,1,2,2,2,2,2};
		NPC_Personality[] strategies = {
				new NPC_Soccer(this),//Montoya
				new NPC_Soccer(this),//Fishman //FishTheBishBoi
				new NPC_Soccer(this),//Summers
				new NPC_Soccer(this),//Hillskemper //IAMTHEFAST
				new NPC_Soccer(this),//Kite
				new NPC_Soccer(this),//Martinek
				new NPC_Soccer(this),//Friedman
				new NPC_Soccer(this),//Tenorio
				new NPC_Soccer(this),//Butler
				new NPC_Soccer(this)};//Holtschulte
		for(int i=0; i<npc_names.length; i++)
		{
			Enemy e = new Enemy(
					Constants.getRandomX(),
					Constants.getRandomY(),
					npc_names[i],
					colors[i],
					team_ids[i],
					25,
					ship_bitmask,//I am
					everything_bitmask,//I hit
					strategies[i],
					this);
			if(team_ids[i]==1){red_team[red_index++] = e;}
			else{blue_team[blue_index++] = e;}
			this.addSprite(e);
			ranking.add(e);
		}

		//Add brick basket to the game on the far right middle of the world
		// for the blue team.
		int brick_basket_radius = 40;
		BrickBasket blue_basket = new BrickBasket(
				Constants.boundary_right-brick_basket_radius*2,
				Constants.boundary_down/2-brick_basket_radius,
				brick_basket_radius,
				Color.blue,
				0,
				xp_brick_bitmask,
				this,
				blue_team
		);
		this.addSprite(blue_basket);

		//Add brick basket to the game on the far left middle of the world
		// for the red team.
		BrickBasket red_basket = new BrickBasket(
				0,
				Constants.boundary_down/2-brick_basket_radius,
				brick_basket_radius,
				Color.red,
				0,
				xp_brick_bitmask,
				this,
				red_team
		);
		this.addSprite(red_basket);

		//Set the baskets for the AIs
		for(int i=0; i<team_ids.length; i++)
		{
			if(team_ids[i]==1){((NPC_Soccer)strategies[i]).team_basket = red_basket;}
			else{((NPC_Soccer)strategies[i]).team_basket = blue_basket;}
		}

		//Start the game off with the world populated with bricks
		Constants.loadExperienceBricks(this,
				Constants.low_exp_brick_count,
				2, 18, 1, Color.YELLOW,
				Constants.SHAPE_TRIANGLE, false, 1, false,
				-0.3);
		Constants.loadExperienceBricks(this,
				Constants.med_exp_brick_count,
				5, 30, 2, Color.GREEN,
				Constants.SHAPE_RECT, false, 1, false,
				-0.3);
		Constants.loadExperienceBricks(this,
				Constants.high_exp_brick_count,
				22, 50, 11, Color.CYAN,
				Constants.SHAPE_RECT, false, 1, false,
				-0.3);
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
				25,
				key_setter,
				Color.blue,
				1,
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
		/*NPC_Personality[] strategies = {
				new NPC_HoltRamA(this), //Better than other rammer, I think.
				new NPC_HoltRamB(this),
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this),
				new NPC_HoltShooterB(this), //Better than other shooter, I think.
				new NPC_HoltShooterB(this), new NPC_HoltShooterA(this),
				new NPC_HoltShooterA(this)};*/
		NPC_Personality[] strategies = {
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this),
				new NPC_Random(this), new NPC_Random(this)};
		for(int i=0; i<npc_names.length; i++)
		{
			Enemy e = new Enemy(
					Constants.getRandomX(),
					Constants.getRandomY(),
					npc_names[i],
					colors[i],
					2,
					25,
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
				350,
				200, 50, 10, Color.CYAN,
				Constants.SHAPE_RECT, false, 0, true,
				Constants.default_friction);

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
				25,
				key_setter,
				Color.blue,
				1,
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
				2, 18, 1, Color.YELLOW,
				Constants.SHAPE_TRIANGLE, false, 1, true,
				Constants.default_friction);
		Constants.loadExperienceBricks(this,
				Constants.med_exp_brick_count,
				5, 30, 2, Color.GREEN,
				Constants.SHAPE_RECT, false, 1, true,
				Constants.default_friction);
		Constants.loadExperienceBricks(this,
				Constants.high_exp_brick_count,
				22, 50, 11, Color.CYAN,
				Constants.SHAPE_RECT, false, 1, true,
				Constants.default_friction);

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
		//Adjust boundary for sniper camera
		int x_adjust = 0;
		int y_adjust = 0;
		if((focused.shape == Constants.SHAPE_SNIPER ||
				focused.shape == Constants.SHAPE_SEEKER ||
				focused.shape == Constants.SHAPE_CANNON)
				&& focused instanceof Player)
		{
			x_adjust = (int)(Math.cos(focused.getAngle())*Constants.SNIPER_LOOK_AHEAD);
			y_adjust = (int)(Math.sin(focused.getAngle())*Constants.SNIPER_LOOK_AHEAD);
		}
		//Draw boundaries if character is near one
		g.setColor(Color.DARK_GRAY);
		if(focused.getXCenter()+x_adjust-Main.ScreenWidth/2<0)
		{
			g.fillRect(0,0,(int)Math.abs(focused.getXCenter()-x_adjust-Main.ScreenWidth/2),Main.ScreenHeight);
		}
		else if(focused.getXCenter()+x_adjust+focused.getCollisionRadius()*2+Main.ScreenWidth/2>Constants.boundary_right)
		{
			int width = (int)(focused.getXCenter()-x_adjust+Main.ScreenWidth/2)-Constants.boundary_right;
			g.fillRect(Main.ScreenWidth-width,0,width,Main.ScreenHeight);
		}
		if(focused.getYCenter()+y_adjust-Main.ScreenHeight/2<0)
		{
			g.fillRect(0,0,Main.ScreenWidth, (int)Math.abs(focused.getYCenter()-y_adjust-Main.ScreenHeight/2));
		}
		else if(focused.getYCenter()+y_adjust+focused.getCollisionRadius()*2+Main.ScreenHeight/2>Constants.boundary_down)
		{
			int height = (int)(focused.getYCenter()-y_adjust+Main.ScreenHeight/2)-Constants.boundary_down;
			g.fillRect(0,Main.ScreenHeight-height,Main.ScreenWidth,height);
		}
		//Draw the super hud
		super.doDrawHUD(g);
		//Print x,y coordinates of the ship
		Font font = new Font("Serif", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Coordinates: "+
			Integer.toString((int)focused.getX())+", "+
			Integer.toString((int)focused.getY()),
				550, Main.ScreenHeight - 50);
		//Print lives
		g.drawString(Integer.toString(focused.getLives())+" Lives",
				950, Main.ScreenHeight - 50);
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
			if(s1.getXP() < s2.getXP())
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
				if(s.getHealth()>0){g.setColor(s.color);}
				else{g.setColor(Color.GRAY);}
				g.drawString("("+s.getLives()+") "+s.getName()+" "+s.getXP(),x,y+(buffer + height) * (1 + i));
			}
		}
	}

	protected void updateSprites(double elapsed_seconds)
	{
		super.updateSprites(elapsed_seconds);
		//Check if a level up plus sign was clicked.
		if(key_setter.checkMouseClicked())
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
			//If the ship is a sniper and a player then it has a look-ahead gun
			if((focused.shape == Constants.SHAPE_SNIPER ||
					focused.shape == Constants.SHAPE_SEEKER ||
					focused.shape == Constants.SHAPE_CANNON)
					&& focused instanceof Player)
			{
				this.centerOnSprite(focused, Constants.SNIPER_LOOK_AHEAD);
			}
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