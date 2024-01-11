import java.awt.Color;

public class Player extends Sprite
{
	private KeyManager key_setter;

	public Player(double x, double y, 
			double collision_radius, 
			KeyManager key_setter,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super(x, y,
			0, //initial heading. It makes the ship fly right.
			collision_radius,
			"",
			i_am_bitmask,
			i_hit_bitmask);
		this.key_setter = key_setter;
		this.color = Color.BLUE;
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		//Orient toward mouse
		this.setAngle(this.angleToDestination(key_setter.getMouseX(), key_setter.getMouseY()));
		//Set speed
		double speed = 0;
		if(key_setter.ascii_input[38]){//up key
			speed = 200;
		}else if(key_setter.ascii_input[40]){//down key
			speed = -200;
		}
		//Set dx and dy
		this.setVelocity(
				Math.cos(this.getAngle())*speed,
				Math.sin(this.getAngle())*speed);
		//Move
		this.move(elapsed_seconds);

		//Strafe
		if(key_setter.ascii_input[37]){//left key
			speed = 200;
			this.setVelocity(
					Math.cos(this.getAngle()-Math.PI/2)*speed,
					Math.sin(this.getAngle()-Math.PI/2)*speed);
		}else if(key_setter.ascii_input[39]){//right key
			speed = 200;
			this.setVelocity(
					Math.cos(this.getAngle()+Math.PI/2)*speed,
					Math.sin(this.getAngle()+Math.PI/2)*speed);
		}
		//Move
		this.move(elapsed_seconds);
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(Sprite other)
	{

	}
}