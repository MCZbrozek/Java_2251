public class ExampleEnemy extends SpriteCircular
{
	//This is the target that this sprite
	//will pursue
	private ExampleShip target;
	
	private double speed = 20; //pixels per second
	
	private double turn_rate = Math.PI/2; //radians per second

	//delta_angle is used for less jerky random motion, swing the change in angle
	// gradually back and forth between turn_rate and -turn_rate
	private double delta_angle = 0;

	public ExampleEnemy(double x, double y,
					   double collision_radius,
					   String image_file,
						ExampleShip target,
					   int i_am_bitmask,
					   int i_hit_bitmask)
	{
		super(x, y,
				Math.PI/2, //initial heading. It makes the ship fly left.
				collision_radius,
				image_file,
				i_am_bitmask,
				i_hit_bitmask);
		this.target = target;
		//String image_id, double angle, int custom_width, int custom_height, boolean use_custom
		this.setImage(image_file, this.getAngle());
	}

	/* This method is intended to be called once per frame and will
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		//Normalize this sprite's angle
		this.setAngle(Utils.normalizeAngle(this.getAngle()));
		
		//Get the goal angle, the angle to the target
		double goal_angle = this.angleToDestination(target.getXCenter(), target.getYCenter());

		//Calculate the difference between
		//current angle and goal angle.
		//Also calculate distance to target.
		double angle = Utils.getAngleDifference(this.getAngle(), goal_angle);
		double distance = this.distanceTo(target.getXCenter(), target.getYCenter());
		
		//If angle to target is less than
		//a maximum (currently pi/16)...
		if (Math.abs(angle) < Math.PI/16) // || distance < 600)
		{	//...move toward the target...
			this.turnTowardAngle(goal_angle, turn_rate, elapsed_seconds);
			this.setVelocity(
				Math.cos(this.getAngle())*this.speed,
				Math.sin(this.getAngle())*this.speed);
			this.move(elapsed_seconds);
		}
		else
		{	//...otherwise wander randomly.
			this.randomDirectedMotion(elapsed_seconds);
		}
	}

	private void randomDirectedMotion(double elapsed_seconds)
	{
		if(Math.random() > 0.5 && delta_angle < Math.PI/64.0)
		{
			delta_angle += turn_rate;
		}
		else if(delta_angle > -Math.PI/64.0)
		{
			delta_angle -= turn_rate;
		}
		this.turnAmount(delta_angle, elapsed_seconds);
		this.setVelocity(Math.cos(this.getAngle())*speed,
				Math.sin(this.getAngle())*speed);
		this.move(elapsed_seconds);
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{

	}
}