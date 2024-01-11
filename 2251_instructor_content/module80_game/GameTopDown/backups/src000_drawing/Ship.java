public class Ship extends Sprite
{
	private KeyManager key_setter;

	public Ship(double x, double y, 
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
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		//Horizontal motion
		if(key_setter.ascii_input[37]){//left key
			this.dx = -200;
		}else if(key_setter.ascii_input[39]){//right key
			this.dx = 200;
		}else{
			this.dx = 0;
		}
		//Vertical motion
		if(key_setter.ascii_input[38]){//up key
			this.dy = -200;
		}else if(key_setter.ascii_input[40]){//down key
			this.dy = 200;
		}else{
			this.dy = 0;
		}
		this.move(elapsed_seconds);
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(Sprite other)
	{

	}
}