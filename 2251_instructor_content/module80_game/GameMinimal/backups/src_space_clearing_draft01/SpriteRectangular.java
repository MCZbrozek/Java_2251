import java.awt.Rectangle;

public abstract class SpriteRectangular extends SpritePhysics
{
	public SpriteRectangular(double x, double y,
			double angle,
			String image_file)
	{
		super(x, y,
			angle,
			image_file);
	}

	/* Returns a rectangle around this sprite */
	public Rectangle getBoundingRectangle() 
	{
		return new Rectangle(
			(int)this.getX(), (int)this.getY(),
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

	//Return the percentage of this that is above other.
	//Used for collisions with rectangular sprites.
	private double percentAbove(Sprite other)
	{
		return ((double)(this.getY() - other.getY())) / (double)other.getHeight();
	}

	//Return the percentage of this that is below other.
	//Used for collisions with rectangular sprites.
	private double percentBelow(Sprite other)
	{
		double other_bottom = other.getY() + other.getHeight();
		double this_bottom = this.getY() + this.getHeight();
		return (other_bottom - this_bottom) / (double)other.getHeight();
	}

	//Return the percentage of this that is left of other.
	//Used for collisions with rectangular sprites.
	private double percentLeft(Sprite other)
	{
		return ((double)(this.getX() - other.getX())) / (double)other.getWidth();
	}

	//Return the percentage of this that is right of other.
	//Used for collisions with rectangular sprites.
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

}