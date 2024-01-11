import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Shot extends SpriteTopDown
{
	private Sprite shooter;
	private double timeout;
	private int damage;
	private boolean knockback;

	public Shot(double x, double y,
			double collision_radius,
			int i_am_bitmask,
			int i_hit_bitmask,
			Sprite shooter,
			double timeout,
			int damage,
			boolean knockback,
			double speed)
	{
		super(x, y,
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

		this.setVelocity(
				Math.cos(shooter.getAngle())*speed,
				Math.sin(shooter.getAngle())*speed);
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		if(this.timeout > 0)
		{
			this.timeout -= elapsed_seconds;
			if(this.timeout < 0){ this.setRemoveMeTrue(); }
		}
		//Move
		this.move(elapsed_seconds);
	}

	/* Draw this sprite relative to the player based on 
	 * the given offset. The offset may be constant
	 * zero which means we are using the still cam. */
	public void draw(Board b, Graphics g)
	{
		//Determine whether or not to draw the sprite
		if(this.isVisible())
		{
			Graphics2D g2d = (Graphics2D) g;
			//Update the on_screen status of this sprite.
			boolean is_on_screen = this.OnScreen((int)this.getX(), (int)this.getY());
			if(is_on_screen)
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
		((SpriteTopDown)other).changeHealth(-this.damage);
		//Remove self
		this.setRemoveMeTrue();
		//Knockback other
		if(this.knockback)
		{
			double angle_to_other = this.angleToDestination(other.getXCenter(), other.getYCenter());
			other.setX(other.getX()+Math.cos(angle_to_other)*100);
			other.setY(other.getY()+Math.sin(angle_to_other)*100);
		}
	}
}