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

	//Enemy attacks
	private Attack punch;
	private Attack shoot;
	private Attack clobber;
	private Attack bomb;

	public BoardTopDown()
	{
		//key_setter.print_key_code = true;

		//Create 4 attacks
		punch = new Attack(
				40,//pixels
				5,//pixels
				0,//seconds
				1,
				false,
				0,//pixels per second
				0.1,//seconds
				this);

		clobber = new Attack(
				40,//pixels
				5,//pixels
				0,//seconds
				1,
				true,
				0,//pixels per second
				1.0,//seconds
				this);

		shoot = new Attack(
				40,//pixels
				5,//pixels
				2.5,//seconds
				1,
				false,
				400,//pixels per second
				0.2,//seconds
				this);

		this.bomb = new Attack(
				0,//pixels
				100,//pixels
				0,//seconds
				1,
				true,
				0,//pixels per second
				3.0,//seconds
				this);

		Attack health_steal_bomb = new Attack(
				0,//pixels
				100,//pixels
				0,//seconds
				10,
				true,
				0,//pixels per second
				3.0,//seconds
				this);
		health_steal_bomb.setHealthSteal(true);

		//Create the player
		player1 = new Player(100, 100,
				10,
				200,
				20.0,
				key_setter,
				player_bitmask,//I am
				0,//I hit
				this);
		player1.bindAttack(punch.getCopy(),0);
		player1.bindAttack(shoot.getCopy(),1);
		player1.bindAttack(bomb.getCopy(),2);
		player1.bindAttack(health_steal_bomb,3);
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
		//Check mouse down to shoot
		if(key_setter.getMousePressed())
		{
			player1.castAttack();
		}
	}
	
	private void spawnEnemies(double elapsed_seconds)
	{
		spawn_cooldown -= elapsed_seconds;
		if(spawn_cooldown < 0)
		{
			//Create a short range punchy enemy
			for(int i=0 ; i<3; i++)
			{
				Enemy e = new Enemy(350,350,
						10.0,
						3,
						100,//pixels per second
						1,
						Color.YELLOW,
						40,
						true,
						this.punch.getCopy(),
						enemy_bitmask,//i am
						player_bitmask+enemy_bitmask,//I hit
						player1,
						this);
				this.addSprite(e);
			}
			//Create a long range shooter enemy
			for(int i=0 ; i<3; i++)
			{
				Enemy e = new Enemy(350,350,
						15.0,
						2,
						80,//pixels per second
						1,
						Color.GREEN,
						300,
						true,
						this.shoot.getCopy(),
						enemy_bitmask,//i am
						player_bitmask+enemy_bitmask,//I hit
						player1,
						this);
				this.addSprite(e);
			}
			//Create a big bruiser enemy
			Enemy e = new Enemy(350,350,
					30.0,
					6,
					40,//pixels per second
					2,
					Color.RED,
					50,
					false,//do not avoid clumping. this makes the bruiser shoves others out of the way.
					this.clobber.getCopy(),
					enemy_bitmask,//i am
					player_bitmask+enemy_bitmask,//I hit
					player1,
					this);
			this.addSprite(e);

			spawn_cooldown = spawn_reset;
		}
	}

}