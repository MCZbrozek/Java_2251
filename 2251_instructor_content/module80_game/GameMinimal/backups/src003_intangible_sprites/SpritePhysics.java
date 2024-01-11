
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
		this.setX(this.getX() + this.dx * elapsed_seconds);
		this.setY(this.getY() + this.dy * elapsed_seconds);
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

	/* Handle a collision between this sprite and other. */
	abstract void handleCollision(SpritePhysics other);
	
	/* Returns true if this sprite collided with other. */
	abstract boolean checkCollided(SpritePhysics other);
	
	/* Moves the other sprite out of collision with this sprite */
	abstract void shoveOut(SpritePhysics other);
}