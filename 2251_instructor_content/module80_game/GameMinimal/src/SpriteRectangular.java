import java.awt.Rectangle;
import java.awt.Point;

public abstract class SpriteRectangular extends SpritePhysics
{
	public SpriteRectangular(double x, double y,
			double angle,
			String image_file,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super(x, y,
			angle,
			image_file,
			i_am_bitmask, i_hit_bitmask);
	}

	/* Returns a rectangle around this sprite */
	public Rectangle getBoundingRectangle() 
	{
		return new Rectangle((int)this.getX(), (int)this.getY(),
				this.getWidth(), this.getHeight());
	}
	
	/* Returns true if this sprite collided with other. */
	public boolean checkCollided(SpritePhysics other)
	{
		//Get the rectangle around the sprite.
		Rectangle my_rect = this.getBoundingRectangle();
		Rectangle other_rect = other.getBoundingRectangle();
		return my_rect.intersects(other_rect);
	}

	/* Return the percentage of this that
	is above other. Used for collisions
	with rectangular sprites. */
	private double percentAbove(Sprite other)
	{
		return ((double)(this.getY() - other.getY())) / (double)other.getHeight();
	}

	/* Return the percentage of this that
	is below other. Used for collisions
	with rectangular sprites. */
	private double percentBelow(Sprite other)
	{
		double other_bottom = other.getY() + other.getHeight();
		double this_bottom = this.getY() + this.getHeight();
		return (other_bottom - this_bottom) / (double)other.getHeight();
	}

	/* Return the percentage of this that
	is left of other. Used for collisions
	with rectangular sprites. */
	private double percentLeft(Sprite other)
	{
		return ((double)(this.getX() - other.getX())) / (double)other.getWidth();
	}

	/* Return the percentage of this that
	is right of other. Used for collisions
	with rectangular sprites. */
	private double percentRight(Sprite other)
	{
		double other_right = other.getX() + other.getWidth();
		double this_right = this.getX() + this.getWidth();
		return (other_right - this_right) / (double)other.getHeight();
	}

	//Disentangle this and other by shoving other out.
	private void shoveOutRight(SpritePhysics other)
	{
		//Place other to the right of this sprite
		other.setX(this.getX()+this.getWidth());
		//Reverse x velocity
		other.setVelocity(-other.getdx(),other.getdy());
		//Make minor corrections:
		while(other.checkCollided(this))
		{
			other.setX(other.getX() + 1.0);
		}
	}

	//Disentangle this and other by shoving other out.
	private void shoveOutLeft(SpritePhysics other)
	{
		//Place other to the left of this sprite
		other.setX(this.getX()-other.getWidth());
		//Reverse x velocity
		other.setVelocity(-other.getdx(), other.getdy());
		//Make minor corrections:
		while(other.checkCollided(this))
		{
			other.setX(other.getX() - 1.0);
		}
	}
	
	//Disentangle this and other by shoving other out.
	private void shoveOutUp(SpritePhysics other)
	{
		//Place other above this wall
		other.setY(this.getY()-other.getHeight());
		//Reverse y velocity
		other.setVelocity(other.getdx(), -other.getdy());
		//Make minor corrections:
		while(other.checkCollided(this))
		{
			other.setY(other.getY() - 1.0);
		}
	}
	
	//Disentangle this and other by shoving other out.
	private void shoveOutDown(SpritePhysics other)
	{
		//Place other below this wall
		other.setY(this.getY()+this.getHeight());
		//Reverse y velocity
		other.setVelocity(other.getdx(), -other.getdy());
		//Make minor corrections:
		while(other.checkCollided(this))
		{
			other.setY(other.getY() + 1.0);
		}
	}
	
	/* Moves the other sprite out of collision with this sprite */
	public void shoveOut(SpritePhysics other)
	{
		//Get percent of this that is above other sprite
		double percent_above = this.percentAbove(other);
		//Get percent of this that is below other sprite
		double percent_below = this.percentBelow(other);
		//Get percent of this that is left of other sprite
		double percent_left = this.percentLeft(other);
		//Get percent of this that is right of other sprite
		double percent_right = this.percentRight(other);

		//vertical_move is true if other is bouncing
		//off a floor or ceiling.
		boolean vertical_move = true;

		if(percent_above > percent_below)
		{
			if(percent_above > percent_left)
			{
				if(percent_above > percent_right)
				{	//Move up
					this.shoveOutUp(other);
				}
				else
				{	//Move right
					vertical_move = false;
					this.shoveOutRight(other);
				}
			}
			else
			{
				vertical_move = false;
				if(percent_left > percent_right)
				{	//Move left
					this.shoveOutLeft(other);
				}
				else
				{	//Move right
					this.shoveOutRight(other);
				}
			}
		}
		else
		{
			if(percent_below > percent_left)
			{
				if(percent_below > percent_right)
				{	//Move down
					this.shoveOutDown(other);
				}
				else
				{	//Move right
					vertical_move = false;
					this.shoveOutRight(other);
				}
			}
			else
			{
				vertical_move = false;
				if(percent_left > percent_right)
				{	//Move left
					this.shoveOutLeft(other);
				}
				else
				{	//Move right
					this.shoveOutRight(other);
				}
			}
		}//if(percent_above > percent_below)
		if(vertical_move)
		{
			//Apply bounce to dy and friction to dx
			other.setVelocity(
				other.getdx() - other.getdx()*this.getBounceFriction(),
				other.getdy() * this.getElasticity());
		}
		else
		{
			//Apply bounce to dx and friction to dy
			other.setVelocity(
					other.getdx() * this.getElasticity(),
					other.getdy() - other.getdy()*this.getBounceFriction());
		}
		if(other.getFlipDirection())
		{
			//flip direction
			other.setAngle(Math.atan2(other.getdy(), other.getdx()));
		}
	}//public void shoveOut(Sprite other)

	/** Check to see if the beam/line
	runs through this sprite and if so
	return the point of intersection
	nearest to the beam origin.
	Otherwise return null. */
	public Point beamIntersects(
		double slope, 
		double y_intercept,
		double beam_x_origin, 
		double beam_y_origin)
	{
		Point nearest = null;
		double dist = Double.MAX_VALUE;
		double distTemp;
		Point temp;
		
		//Top
		temp = beamIntersectsTop(slope, y_intercept, beam_x_origin, beam_y_origin);
		if(temp!=null){
			distTemp = Utils.distance(
				beam_x_origin, beam_y_origin,
				temp.x, temp.y);
			if(distTemp < dist){
				dist = distTemp;
				nearest = temp;
			}
		}
		//Bottom
		temp = beamIntersectsBottom(slope, y_intercept, beam_x_origin, beam_y_origin);
		if(temp!=null){
			distTemp = Utils.distance(
				beam_x_origin, beam_y_origin,
				temp.x, temp.y);
			if(distTemp < dist){
				dist = distTemp;
				nearest = temp;
			}
		}
		//Left
		temp = beamIntersectsLeft(slope, y_intercept, beam_x_origin, beam_y_origin);
		if(temp!=null){
			distTemp = Utils.distance(
				beam_x_origin, beam_y_origin,
				temp.x, temp.y);
			if(distTemp < dist){
				dist = distTemp;
				nearest = temp;
			}
		}
		//Right
		temp = beamIntersectsRight(slope, y_intercept, beam_x_origin, beam_y_origin);
		if(temp!=null){
			distTemp = Utils.distance(
				beam_x_origin, beam_y_origin,
				temp.x, temp.y);
			if(distTemp < dist){
				dist = distTemp;
				nearest = temp;
			}
		}
		return nearest;
	}
	/** Check to see if the beam/line
	runs through this sprite's top side.
	Outputs: Point of intersection or 
	null if none. */
	public Point beamIntersectsTop(
		double slope, 
		double y_intercept,
		double beam_x_origin, 
		double beam_y_origin)
	{
		//Check top horizontal line
		Point intercept = Utils.getIntercept(
			slope,y_intercept,0,this.getY());
		//Make sure the intercept is between
		//the horizontal start and end of the
		//rectangle.
		if(betweenX(intercept.x))
			return intercept;
		return null;
	}
	/** Check to see if the beam/line
	runs through this sprite's bottom side.
	Outputs: Point of intersection or 
	null if none. */
	public Point beamIntersectsBottom(
		double slope, 
		double y_intercept,
		double beam_x_origin, 
		double beam_y_origin)
	{
		//Check bottom horizontal line
		Point intercept = Utils.getIntercept(
			slope, y_intercept,
			0, this.getY()+this.getHeight());
		//Make sure the intercept is between
		//the horizontal start and end of the
		//rectangle.
		if(betweenX(intercept.x))
			return intercept;
		return null;
	}
	/** Check to see if the beam/line
	runs through this sprite's left side.
	Outputs: Point of intersection or 
	null if none. */
	public Point beamIntersectsLeft(
		double slope,
		double y_intercept,
		double beam_x_origin,
		double beam_y_origin)
	{
		//Check left vertical line
		Point intercept = Utils.getInterceptVertical(
			slope, y_intercept,
			this.getX());
		//Make sure the intercept is between
		//the vertical start and end of the
		//rectangle.
		if(betweenY(intercept.y))
			return intercept;
		return null;
	}
	/** Check to see if the beam/line
	runs through this sprite's right side.
	Outputs: Point of intersection or 
	null if none. */
	public Point beamIntersectsRight(
		double slope,
		double y_intercept,
		double beam_x_origin,
		double beam_y_origin)
	{
		//Check right vertical line
		Point intercept = Utils.getInterceptVertical(
			slope, y_intercept,
			this.getX()+this.getWidth());
		//Make sure the intercept is between
		//the vertical start and end of the
		//rectangle.
		if(betweenY(intercept.y))
			return intercept;
		return null;
	}

	//Returns true if given value is between the left and right sides of this rectangle.
	public boolean betweenX(int x)
	{return this.getX()<x && x<this.getX()+this.getWidth();}
	//Returns true if given value is between the top and bottom sides of this rectangle.
	public boolean betweenY(int y)
	{return this.getY()<y && y<this.getY()+this.getHeight();}

}