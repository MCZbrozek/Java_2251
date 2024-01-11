import java.awt.Color;

public class Enemy extends Sprite
{
	private double cooldown = 0;
	private Player player;
	private Board board;
	
	public Enemy(double x, double y,
			double collision_radius,
			int i_am_bitmask,
			int i_hit_bitmask,
			Player p,
			Board b)
	{
		super(x, y,
			0, //initial heading.
			collision_radius,
			"",//image file
			i_am_bitmask,
			i_hit_bitmask);
		this.color = Color.YELLOW;
		this.setHealth(5);
		this.setMaxHealth(10);
		this.player = p;
		this.board = b;
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		/*
		//Orient toward mouse
		this.setAngle(this.angleToDestination(key_setter.getMouseX(), key_setter.getMouseY()));
		//Set speed
		double speed = 0;
		this.setVelocity(
				Math.cos(this.getAngle()+Math.PI/2)*speed,
				Math.sin(this.getAngle()+Math.PI/2)*speed);
		//Move
		this.move(elapsed_seconds);
		*/
		double angle_to_target = this.angleToDestination(player.getXCenter(), player.getYCenter());
		//Utils.normalizeAngle(angle_to_target);
		if(Math.abs(angle_to_target) < Math.PI/16  &&  cooldown<0)
		{
			//shoot
			board.addSprite(
					new Shot(this.getXCenter(), this.getYCenter(),
							5,//collision_radius
							Board.enemy_bullet_bitmask,
							0,//i_hit_bitmask
							this,//shooter
							3.0,//timeout
							1,//damage
							false,//knockback
							500)//speed
					);
			cooldown = 0.7;
		}
		cooldown -= elapsed_seconds;
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(Sprite other)
	{

	}
}