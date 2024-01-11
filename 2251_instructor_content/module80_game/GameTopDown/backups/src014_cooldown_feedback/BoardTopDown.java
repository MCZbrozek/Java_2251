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
	//Reference to player 1
	private Player player1;
	//How long until next enemy cluster spawns
	private double spawn_cooldown = 0;
	private double spawn_reset = 10.0;

	private AttackManager attacks;

	public BoardTopDown()
	{
		//key_setter.print_key_code = true;

		attacks = new AttackManager(this);

		//Create the player
		player1 = new Player(100, 100,
				10000,
				200,
				20.0,
				key_setter,
				player_bitmask,//I am
				0,//I hit
				this);
		player1.bindAttack(attacks.getAttack("punch"),0);
		player1.bindAttack(attacks.getAttack("shoot").getCopy(),1);
		player1.bindAttack(attacks.getAttack("bomb"),2);
		player1.bindAttack(attacks.getAttack("health steal bomb"),3);
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
			g.fillRect(cooldowns_x, cooldowns_y+spacing*i,
					(int)(width*attacks[i].getPercentCooldown()), height);
		}
		//Draw labels on the cooldown bars
		g.setColor(Color.WHITE);
		for (int i = 0; i < attacks.length; i++)
		{
			g.drawString(attacks[i].getName(),
					cooldowns_x, cooldowns_y + spacing * i + 16);
		}
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
						100,
						100,//pixels per second
						Color.YELLOW,
						40,
						true,
						attacks.getAttack("punch"),
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
						50,
						80,//pixels per second
						Color.GREEN,
						300,
						true,
						attacks.getAttack("shoot"),
						enemy_bitmask,//i am
						player_bitmask+enemy_bitmask,//I hit
						player1,
						this);
				this.addSprite(e);
			}
			//Create a big bruiser enemy
			Enemy e = new Enemy(350,350,
					30.0,
					400,
					40,//pixels per second
					Color.RED,
					50,
					false,//do not avoid clumping. this makes the bruiser shoves others out of the way.
					attacks.getAttack("clobber"),
					enemy_bitmask,//i am
					player_bitmask+enemy_bitmask,//I hit
					player1,
					this);
			this.addSprite(e);

			spawn_cooldown = spawn_reset;
		}
	}

}