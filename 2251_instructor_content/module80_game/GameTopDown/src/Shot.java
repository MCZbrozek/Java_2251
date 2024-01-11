import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Shot extends SpriteTopDown
{
	private SpriteTopDown shooter;
	private double timeout;
	private int damage;
	private boolean knockback;
	private boolean penetrating;
	private boolean player_follow;
	//Damage dealt is given to shooter as health.
	private boolean health_steal = false;

	public Shot(double x, double y,
				double collision_radius,
				int i_am_bitmask,
				int i_hit_bitmask,
				SpriteTopDown shooter,
				double timeout,
				int damage,
				boolean knockback,
				double speed,
				boolean penetrating,
				boolean player_follow)
	{
		super(x, y,
			1,
			0, //initial heading. It makes the ship fly right.
			collision_radius,
			"",//image file
			i_am_bitmask,
			i_hit_bitmask);
		this.color = Color.RED;
		this.moveToCenter(x, y);
		this.shooter = shooter;
		this.timeout = timeout;
		this.damage = damage;
		this.knockback = knockback;
		this.penetrating = penetrating;
		this.player_follow = player_follow;

		this.setVelocity(
				Math.cos(shooter.getAngle())*speed,
				Math.sin(shooter.getAngle())*speed);
	}

	//Activate or deactivate health stealing on this shot
	public void setHealthSteal(boolean health_steal){this.health_steal = health_steal;}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		if(this.timeout > 0)
		{
			this.timeout -= elapsed_seconds;
		}
		else
		{
			this.setRemoveMeTrue();
		}
		//Move
		this.move(elapsed_seconds);
		//If player_follow, then center the player on this shot's position.
		//Used for things like dash attack.
		if(player_follow)
		{
			shooter.moveToCenter(this.getXCenter(), this.getYCenter());
		}
	}

	/**  */
	public void draw(Board b, Graphics g)
	{	//Determine whether or not to draw the sprite
		if(this.isVisible())
		{
			Graphics2D g2d = (Graphics2D) g;
			//Update the on_screen status of this sprite.
			this.setIsOnScreen((int)this.getX(), (int)this.getY());
			if(this.getIsOnScreen())
			{	//Draw sprite as a circle.
				g2d.setColor(color);
				g2d.fillOval((int)(this.getX()),
							(int)(this.getY()),
							(int)this.getCollisionRadius()*2,
							(int)this.getCollisionRadius()*2);
			}
			else
			{	//Remove off screen bullets
				this.setRemoveMeTrue();
			}
		}
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{
		//Damage the other
		other.takeDamage(this.damage);
		//Give health to shooter
		if(health_steal){shooter.takeDamage(-this.damage);}
		//Remove self upon collision if not penetrating
		if(!penetrating){this.setRemoveMeTrue();}
		//Knockback other
		if(this.knockback)
		{
			double angle_to_other = this.angleToDestination(other.getXCenter(), other.getYCenter());
			other.setX(other.getX()+Math.cos(angle_to_other)*100);
			other.setY(other.getY()+Math.sin(angle_to_other)*100);
		}
	}
}