import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Shot extends Sprite
{
	private Sprite shooter;
	private double timeout;
	private int damage;
	private boolean knockback;
	private double speed;
	
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
		this.moveToCenter(x, y);
		this.color = Color.RED;
		this.shooter = shooter;
		this.timeout = timeout;
		this.damage = damage;
		this.knockback = knockback;
		this.speed = speed;

		this.setVelocity(
				Math.cos(shooter.getAngle())*speed,
				Math.sin(shooter.getAngle())*speed);
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		//Move
		this.move(elapsed_seconds);
	}

	/* Draw this sprite relative to the player based on 
	 * the given offset. The offset may be constant
	 * zero which means we are using the still cam. */
	public void draw(Board b, Graphics g)
	{
		//Determine whether or not to draw the sprite
		if(this.vis)
		{
			Graphics2D g2d = (Graphics2D) g;
			//Update the on_screen status of this sprite.
			boolean is_on_screen = this.OnScreen((int)this.getX(), (int)this.getY());
			if(is_on_screen)
			{	//Draw sprite as a circle.
				g2d.setColor(color);
				g2d.fillOval((int)(this.getX()),
							(int)(this.getY()),
							(int)this.collision_radius*2,
							(int)this.collision_radius*2);
			}
			else
			{	//Remove off screen bullets
				this.setRemoveMeTrue();
			}
		}
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(Sprite other)
	{
		//Damage the other and remove self.
		other.changeHealth(-this.damage);
		this.setRemoveMeTrue();
	}
}