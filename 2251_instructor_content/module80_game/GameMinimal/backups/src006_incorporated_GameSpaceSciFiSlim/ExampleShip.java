public class ExampleShip extends SpriteCircular
{
	private KeyManager key_setter;

	public ExampleShip(double x, double y, 
			double collision_radius, 
			String image_file,
			KeyManager key_setter,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super(x, y, 
			0, //initial heading. It makes the ship fly right.
			collision_radius, 
			image_file, 
			i_am_bitmask,
			i_hit_bitmask);
		this.key_setter = key_setter;
		//String image_id, double angle, int custom_width, int custom_height, boolean use_custom
		this.setImage(image_file, this.getAngle());
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		this.ballisticMotion(elapsed_seconds);
		//TODO this.gridMotion(elapsed_seconds);
	}
	
	private void ballisticMotion(double elapsed_seconds)
	{	//Turning
		if(key_setter.ascii_input[37]){//left key
			this.turnCounterClockwise(2*Math.PI,//turn rate in radians per second
					elapsed_seconds);
		}else if(key_setter.ascii_input[39]){//right key
			this.turnClockwise(2*Math.PI,//turn rate in radians per second
					elapsed_seconds);
		}
		//Forward and backward motion
		if(key_setter.ascii_input[38]){//up key
			double speed = 300;
			this.changeVelocity(
					Math.cos(this.getAngle())*speed*elapsed_seconds,
					Math.sin(this.getAngle())*speed*elapsed_seconds);
		}else if(key_setter.ascii_input[40]){//down key
			//Braking
			this.setVelocity(
					this.getdx()*0.5*(1-elapsed_seconds),
					this.getdy()*0.5*(1-elapsed_seconds)
					);
			}
		this.move(elapsed_seconds);
	}
	
	private void gridMotion(double elapsed_seconds)
	{
		//Horizontal motion
		double temp_dx = 0;
		if(key_setter.ascii_input[37]){//left key
			temp_dx = -250;
		}else if(key_setter.ascii_input[39]){//right key
			temp_dx = 250;
		}
		//Vertical motion
		double temp_dy = 0;
		if(key_setter.ascii_input[38]){//up key
			temp_dy = -250;
		}else if(key_setter.ascii_input[40]){//down key
			temp_dy = 250;
		}
		this.setVelocity(temp_dx, temp_dy);
		this.move(elapsed_seconds);
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{

	}
}