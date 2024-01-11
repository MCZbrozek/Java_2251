import java.awt.Color;

public class Enemy extends Sprite
{
	public Enemy(double x, double y,
			double collision_radius,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super(x, y,
			0, //initial heading. It makes the ship fly right.
			collision_radius,
			"",
			i_am_bitmask,
			i_hit_bitmask);
		this.color = Color.YELLOW;
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
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(Sprite other)
	{

	}
}