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

	private AttackManager attacks;
	private ScenarioManager scenario;

	public BoardTopDown(boolean centerOnPlayer)
	{
		//key_setter.print_key_code = true;

		attacks = new AttackManager(this);

		//Create the player
		player1 = new Player(100, 100,
				10000,
				200,
				20.0,
				attacks.getAttack("shoot"),//default attack
				key_setter,
				player_bitmask,//I am
				0,//I hit
				this);
		player1.bindAttack(attacks.getAttack("bomb"),0);
		player1.bindAttack(attacks.getAttack("health steal bomb"),1);
		player1.bindAttack(attacks.getAttack("dash"),2);
		this.addSprite(player1);

		EnemyManager enemies = new EnemyManager(this, attacks, player1);
		scenario = new ScenarioManager(this, enemies);
		this.addSpriteIntangible(scenario);
		
		//Center camera on the player.
		if(centerOnPlayer)
			this.centerOnSprite(player1);
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
			g.fillRect(cooldowns_x,
					cooldowns_y+spacing*i,
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
		//Check mouse down to perform default player attack
		if(key_setter.getMousePressed()){player1.attack();}
	}
}