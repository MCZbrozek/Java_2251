//THIS FILE WAS COPIED OVER FROM GAMEMINIMAL ON 4/1/2023

public abstract class SpritePhysics extends Sprite
{
	//This is true if this sprite flips its own orientation when an
	//object collides with it
	private boolean flip_direction = false;
	//The percentage of momentum kept by an object
	//bouncing off of this sprite.
	//A value over 1.0 means that this object increases
	//the momentum of objects that hit it.
	private double elasticity = 1.0;
	//The percentage of perpendicular momentum 
	//lost by an object bouncing off of this sprite.
	private double bounce_friction = 0.0;
	//Whether or not you hit the floor (or are on the floor)
	//in the most recent frame.
	//This is only used for platformers.
	private boolean on_floor = false;
	//velocity of the sprite
	private double dx = 0;
	private double dy = 0;

	public SpritePhysics(double x, double y,
			double angle,
			String image_file,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super(x, y, angle, image_file, i_am_bitmask, i_hit_bitmask);
	}

	public void move(double elapsed_seconds) 
	{
		this.setX(this.getX() + this.dx*elapsed_seconds);
		this.setY(this.getY() + this.dy*elapsed_seconds);
	}

	/** This may only be getting used to have gravity 
	affect non-ballistic objects. */
	public void move(double angle, double magnitude)
	{
		this.setX(this.getX() + Math.cos(angle) * magnitude);
		this.setY(this.getY() + Math.sin(angle) * magnitude);
	}

	/* Move a fixed amount in the current direction.
	This is used for incremental movement along a
	grid. */
	public void moveFixed(int magnitude)
	{
		this.setX(this.getX() + Math.cos(this.getAngle()) * magnitude);
		this.setY(this.getY() + Math.sin(this.getAngle()) * magnitude);
	}
	
	public void ballisticAccelerate(double accel, double elapsed_seconds)
	{
		this.changeVelocity(
			Math.cos(this.getAngle())*accel*elapsed_seconds,
			Math.sin(this.getAngle())*accel*elapsed_seconds);
	}

	/* Reduce velocity as if something like friction
	were applied. decel should be a number between
	zero and one. */
	public void ballisticDecelerate(double decel, double elapsed_seconds)
	{
		/* Example: If we want to reduce velocity by
		10% per second, then decel should be 0.1
		and we subtract off 10% * elapsed seconds
		worth of velocity.
		dx = dx - dx*decel*elapsed_seconds
		or simply
		dx = dx*(1 - decel*elapsed_seconds)
		*/
		this.setVelocity(
			dx*(1-decel*elapsed_seconds),
			dy*(1-decel*elapsed_seconds));
	}

	/* Reduce velocity on a smaller time scale
	(milliseconds) to make possible faster 
	deceleration. decel should be a number between
	zero and one. */
	public void ballisticDecelerateMillis(double decel, double elapsed_seconds)
	{
		/* Example: If we want to reduce
		velocity by 10% per millisecond,
		then decel should be 0.1 */
		while(elapsed_seconds > 0.001)
		{
			this.setVelocity(
				dx*(1-decel),
				dy*(1-decel));
			elapsed_seconds -= 0.001;
		}
		if(elapsed_seconds > 0)
		{
			this.setVelocity(
				dx*(1-decel*elapsed_seconds),
				dy*(1-decel*elapsed_seconds));
		}
	}

	public void changeVelocity(double change_dx, double change_dy)
	{
		dx += change_dx;
		dy += change_dy;
	}

	public void setVelocity(double new_dx, double new_dy)
	{
		dx = new_dx;
		dy = new_dy;
	}

	/** Return the magnitude of this sprite's velocity vector. */
	public double getSpeed()
	{
		return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
	}

	public double getdx(){ return dx; }
	public void setdx(double new_dx){ dx=new_dx; }
	public double getdy(){ return dy; }
	public void setdy(double new_dy){ dy=new_dy; }
	public boolean getFlipDirection(){ return flip_direction; }
	public void setFlipDirection(boolean flip_direction){ this.flip_direction=flip_direction; }
	public double getElasticity(){ return elasticity; }
	public void setElasticity(double elasticity){ this.elasticity=elasticity; }
	public double getBounceFriction(){ return bounce_friction; }
	public void setBounceFriction(double bounce_friction){ this.bounce_friction=bounce_friction; }
	public boolean getOnFloor(){ return on_floor; }
	public void setOnFloor(boolean on_floor){ this.on_floor=on_floor; }

	/** The takeDamage method does nothing but
	should be over-ridden by any sprites that
	want to implement damage taking. */
	public void takeDamage(int damage, Sprite responsible_sprite)
	{
		System.out.println("WARNING: Sprite.takeDamage(int damage, Sprite responsible_sprite) called. This should be overridden.");
		System.exit(0);
	}

	/** This empty function is useful for some inheritors
	 * of sprite to implement. If this is not overridden
	 * then it throws an error and exits. */
	public void takeDamage(int damage_amount)
	{
		System.out.println("WARNING: Sprite.takeDamage(int damage_amount) called. This should be overridden.");
		System.exit(0);
	}

	/** Get a deep copy of this sprite. If this is
	not overridden then it throws a error and exits. */
	public SpritePhysics deepCopy()
	{
		System.out.println("WARNING: Sprite.deepCopy called. This should be overridden.");
		System.exit(0);
		return null;
	}

	/* Handle a collision between this sprite and other. */
	abstract void handleCollision(SpritePhysics other);
	
	/* Returns true if this sprite collided with other. */
	abstract boolean checkCollided(SpritePhysics other);
	
	/* Moves the other sprite out of collision with this sprite */
	abstract void shoveOut(SpritePhysics other);
}