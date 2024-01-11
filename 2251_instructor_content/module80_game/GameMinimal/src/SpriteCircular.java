import java.awt.Rectangle;
import java.awt.Point;

public abstract class SpriteCircular extends SpritePhysics
{
	//Detect collisions in a circle around the sprite.
	private double collision_radius = 0;

	public SpriteCircular(double x, double y,
			double angle,
			double radius,
			String image_file,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super(x, y,
			angle,
			image_file,
			i_am_bitmask, i_hit_bitmask);
		this.setCollisionRadius(radius);
	}
	
	/* Returns a rectangle around this sprite */
	public Rectangle getBoundingRectangle() 
	{
		return new Rectangle((int)(getXCenter() - collision_radius),
				(int)(getYCenter() - collision_radius), 
				(int)collision_radius*2, (int)collision_radius*2);
	}
	
	public double getCollisionRadius()
	{
		return collision_radius;
	}
	public void setCollisionRadius(double new_radius)
	{
		this.collision_radius = new_radius;
		this.setWidth((int)(new_radius*2));
		this.setHeight((int)(new_radius*2));
	}

	/* Return the depth of penetration of  one sprite into the other. */
	private double depthOfPenetration(SpriteCircular other)
	{
		double[] other_center = other.getCenter();
		double distance = distanceTo(other_center[0], other_center[1]);
		return (this.collision_radius + ((SpriteCircular)other).getCollisionRadius()) - distance;
	}

	/* Returns true if this sprite collided with other. */
	public boolean checkCollided(SpritePhysics other)
	{
		/* Rectangular collision is the default so only
		use a circular collision check if the other
		sprite is also circular, otherwise defer to the 
		other sprite. */
		if(other instanceof SpriteCircular)
		{
			return depthOfPenetration((SpriteCircular)other) > 0;
		}
		else
		{
			return other.checkCollided(this);
		}
	}

	/* Bounce off the other sprite off of this using vectors
	 * http://stackoverflow.com/questions/573084/how-to-calculate-bounce-angle
	 */
	public void bounceOff(Sprite other)
	{
		//Get the dy and dx components of the line from 
		//this.center to other.center
		double[] normal_vector = 
			{
				this.getXCenter() - other.getXCenter(),
				this.getYCenter() - other.getYCenter()
			};
		//Separate other's velocity into the part perpendicular
		//to other, u, and the part parallel to other, w.
		double v_dot_n = this.getdx()*normal_vector[0] + this.getdy()*normal_vector[1];
		double square_of_norm_length = Math.pow(normal_vector[0],2) + Math.pow(normal_vector[1],2);
		double multiplier = v_dot_n / square_of_norm_length;
		double[] u_vector = 
			{
				normal_vector[0]*multiplier,
				normal_vector[1]*multiplier
			};
		double[] w_vector = 
			{
					this.getdx() - u_vector[0],
					this.getdy() - u_vector[1]
			};
		//Determine the velocity post collision while 
		//factoring in the elasticity and friction of
		//the wall.
		this.setVelocity(
				w_vector[0]*(1-this.getBounceFriction())-u_vector[0]*this.getElasticity(),
				w_vector[1]*(1-this.getBounceFriction())-u_vector[1]*this.getElasticity());
		//Get the collision angle mainly to see if we were hit 
		//from behind
		double angle_difference = getCollisionAngle(other);
		//Change direction from having bounced out.
		if(this.getFlipDirection())
		{	//Don't do this if this sprite struck other from behind.
			if(Math.abs(angle_difference) < Math.PI/2)
			{
				this.setAngle(Math.atan2(this.getdy(),this.getdx()));
			}
		}
	}

	/* Moves the other sprite out of collision with this sprite */
	public void shoveOut(SpritePhysics other)
	{
		//Get angle to other
		double angle = this.angleToDestination(other.getXCenter(), other.getYCenter());
		//get dx and dy towards other.
		double temp_dx = Math.cos(angle);
		double temp_dy = Math.sin(angle);
		/* Rectangular collision is the default so only
		use a circular collision check if the other
		sprite is also circular, otherwise defer to the 
		other sprite. */
		if(other instanceof SpriteCircular)
		{	//Get the amount of overlap.
			double overlap = depthOfPenetration((SpriteCircular)other);
			//Move other out by the appropriate amount
			other.setX(other.getX() + temp_dx*overlap);
			other.setY(other.getY() + temp_dy*overlap);
			//Bounce other out
			((SpriteCircular) other).bounceOff(this);
		}
		else
		{
			System.out.println("WARNING: in SpriteCircular. Shoving a rectangular object out of a circular object has not been implemented.");
		}
	}

	/** Check to see if the beam/line
	runs through this sprite.
	Outputs: Point of intersection or 
	null if none. */
	public Point beamIntersects(
		double beam_width,
		double beam_y_intercept,
		double beam_slope,
		double negative_reciprocal)
	{
		double sprite_y_intercept = this.getYCenter() - this.getXCenter() * negative_reciprocal;
		Point intercept = Utils.getIntercept(
			beam_slope, beam_y_intercept,
			negative_reciprocal, sprite_y_intercept);
		//Then get the distance from the point 
		//of intersection to the sprite center
		//to see if there was a hit.
		double dist = Utils.distance(
			this.getXCenter(), this.getYCenter(),
			intercept.x, intercept.y);
		if(dist < this.getCollisionRadius()+beam_width)
			return intercept;
		else
			return null;
	}

	/** Check to see if the vertical 
	line runs through this sprite.
	Outputs: Point of intersection or 
	null if none. */
	public Point vertBeamIntersects(
		double beam_width,
		double beam_x)
	{
		//Get whichever edge of the beam is closer to the center of the circle and use that as the x value
		double x = beam_x+beam_width;
		if(Math.abs(beam_x-beam_width - getXCenter()) < Math.abs(beam_x+beam_width - getXCenter()))
			x = beam_x-beam_width;

		double[] intercepts = getYGivenX(x);
		System.out.println("Made it to GameMinimal's SpriteCircular.vertBeamIntersects");
		for(double d:intercepts)
			System.out.println(d);
		System.out.println();
//TODO LEFT OFF HERE
		/*
		Point intercept = Utils.getIntercept(
			beam_slope, beam_y_intercept,
			negative_reciprocal, sprite_y_intercept);
		//Then get the distance from the point 
		//of intersection to the sprite center
		//to see if there was a hit.
		double dist = Utils.distance(
			this.getXCenter(), this.getYCenter(),
			intercept.x, intercept.y);
		if(dist < this.getCollisionRadius()+beam_width)
			return intercept;
		else*/
			return null;
	}

	/* Get the zero, one, or two y 
	values on the circle's edge given 
	an x value. Useful for finding
	collisions with vertical lines. */
	public double[] getYGivenX(double x)
	{
		double val = Math.sqrt(collision_radius*collision_radius - Math.pow(x-getXCenter(),2)) + getYCenter();
		double[] toReturn = {-val,val};
		return toReturn;
	}
	/* Get the zero, one, or two x 
	values on the circle's edge given 
	a y value. Useful for finding
	collisions with horizontal lines. */
	public double[] getXGivenY(double y)
	{
		double val = Math.sqrt(collision_radius*collision_radius - Math.pow(y-getYCenter(),2)) + getXCenter();
		double[] toReturn = {-val,val};
		return toReturn;
	}
}