import java.awt.Color;

public class Enemy extends SpriteTopDown
{
	private Player player;
	private Board board;

	private double speed;
	private double damage;
	private double range;
	private boolean shy = true; //Whether or not this unit avoids clumping.

	//Below minimum distance, separate from other enemies.
	private double minimum_distance;

	private double turn_rate = Math.PI/2;
	//Cooldown for shooting
	private double cooldown = 0;

	public Enemy(double x, double y,
			double collision_radius,
			int health,
			double speed,
			double damage,
			Color color,
			double range,
			boolean shy,
			int i_am_bitmask,
			int i_hit_bitmask,
			Player p,
			Board b)
	{
		super(x, y,
			health,
			0, //initial heading.
			collision_radius,
			"",//image file
			i_am_bitmask,
			i_hit_bitmask);
		this.player = p;
		this.board = b;
		this.minimum_distance = collision_radius*1.5;
		this.speed = speed;
		this.damage = damage;
		this.color = color;
		this.range = range;
		this.shy = shy;
	}

	/** This is some flocking code that I modified to prevent the
	 * enemies from clumping so much. */
	private void avoidClumping(double elapsed_seconds)
	{
		SpritePhysics recent_collision = board.getNearest(this);
		//If the nearest enemy is too close,
		//orient heading away from the other Enemy.
		if(shy && distanceTo(recent_collision.getXCenter(),recent_collision.getYCenter()) < minimum_distance)
		{
			this.turnTowardAngle(Math.PI+this.angleToDestination(recent_collision.getX(),recent_collision.getY()),
					turn_rate,elapsed_seconds);
		}
		else
		{   //Orient toward player
			this.turnTowardAngle(this.angleToDestination(player.getXCenter(), player.getYCenter()),
					turn_rate,elapsed_seconds);
		}
	}
	/** This method is intended to be called once per frame and will
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		this.avoidClumping(elapsed_seconds);
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
		else if(distance_to_player > this.range)
		{	//Move
			this.move(elapsed_seconds);
		}
		else //Player is in range to shoot.
		{	//If the enemy is in a vision cone and cooldown is low
			//then shoot
			double angle_to_target = this.angleToDestination(player.getXCenter(), player.getYCenter());
			double angle_diff = Utils.getAngleDifference(this.getAngle(), angle_to_target);
			//System.out.println(angle_to_target);
			if (Math.abs(angle_diff) < Math.PI / 16 && cooldown < 0)
			{    //shoot
				board.addSprite(new Shot(this.getXCenter(), this.getYCenter(), 5,//collision_radius
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
	{	//The wider sprite shoves the smaller sprite.
		//Equal width sprites randomly decide who shoves
		//out of whom.
		if(this.getWidth() > other.getWidth()){
			this.shoveOut(other);
		}else if(this.getWidth() < other.getWidth()){
			other.shoveOut(this);
		}else if(Math.random() > 0.5){
			this.shoveOut(other);
		}else{
			other.shoveOut(this);
		}
	}
}