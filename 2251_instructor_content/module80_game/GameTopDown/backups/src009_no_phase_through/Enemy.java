import java.awt.Color;

public class Enemy extends SpriteTopDown
{
	private Player player;
	private Board board;
	private double speed = 100;
	private double turn_rate = Math.PI/2;
	//Cooldown for shooting
	private double cooldown = 0;
	//Use recent_collision to avoid clumping of enemies.
	private SpritePhysics recent_collision;
	//Below minimum distance, separate from other enemies.
	private double minimum_distance;

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
		this.minimum_distance = collision_radius*1.5;
	}
	
	/** This method is intended to be called once per frame and will
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		recent_collision = board.getNearest(this);
		//If the nearest enemy is too close,
		//orient heading away from the other Enemy.
		if(distanceTo(recent_collision.getXCenter(),recent_collision.getYCenter()) < minimum_distance)
		{
			this.turnTowardAngle(Math.PI+this.angleToDestination(recent_collision.getX(),recent_collision.getY()),
					turn_rate,elapsed_seconds);
		}
		else
		{   //Orient toward player
			this.turnTowardAngle(this.angleToDestination(player.getXCenter(), player.getYCenter()),
					turn_rate,elapsed_seconds);
		}
		//Set speed
		this.setVelocity(
				Math.cos(this.getAngle())*speed,
				Math.sin(this.getAngle())*speed);
		//If the enemy is very far from the player, remove the enemy
		double distance_to_player = this.distanceTo(player.getXCenter(), player.getYCenter());
		if(distance_to_player > 6000)
		{	//Remove off screen bullets
			this.setRemoveMeTrue();
		}
		//Move toward player if the player is not too close.
		else if(distance_to_player > 70)
		{	//Move
			this.move(elapsed_seconds);
		}
		//If the enemy is in a vision cone and cooldown is low
		//then shoot
		double angle_to_target = this.angleToDestination(player.getXCenter(), player.getYCenter());
		double angle_diff = Utils.getAngleDifference(this.getAngle(), angle_to_target);
		//System.out.println(angle_to_target);
		if(Math.abs(angle_diff) < Math.PI/16  &&  cooldown<0)
		{
			//shoot
			board.addSprite(
					new Shot(this.getXCenter(), this.getYCenter(),
							5,//collision_radius
							BoardTopDown.enemy_bullet_bitmask,//i_am_bitmask
							BoardTopDown.player_bitmask,//i_hit_bitmask
							this,//shooter
							3.0,//timeout
							1,//damage
							false,//knockback
							500)//speed
					);
			cooldown = 1.2;
		}
		cooldown -= elapsed_seconds;
	}
	
	public boolean getRemoveMe() 
	{
		return super.getRemoveMe() || this.getHealth() <= 0;
	}

	/* Handle a collision between this sprite and other.
	* This prevents enemies from phasing through each other or the
	* player by shoving each other out of the same space.*/
	public void handleCollision(SpritePhysics other)
	{	//Randomly decide who shoves out of whom
		//so that it isn't decided based on who is more
		//to the left on the screen.
		if(Math.random() > 0.5){
			this.shoveOut(other);
		}else{
			other.shoveOut(this);
		}
	}
}