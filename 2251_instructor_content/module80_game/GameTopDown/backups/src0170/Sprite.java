//THIS FILE WAS COPIED OVER FROM GAMEMINIMAL ON 4/1/2023

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//Make the sprite comparable so we can sort it for 
//efficient collision detection.
public abstract class Sprite implements Comparable<Sprite>
{	//Track whether or not the sprite is on the screen
	protected boolean on_screen = true;
	//Coordinates
	protected double x;
	protected double y;
	//Dimensions
	private int width;
	private int height;
	//Whether or not the sprite is visible
	protected boolean vis = true;
	//Whether or not the sprite should be removed from the screen
	private boolean remove_me = false;
	//Image of the sprite
	private BufferedImage image;
	//Angle to draw the image at.
	private double angle = 0.0;
	//Last angle that this sprite was drawn at.
	//We use this to rotate the image the minimum number
	//of times possible. Certainly we should only rotate 
	//the image when the sprite is on the screen.
	private double last_draw_angle = 0.0;
	//Reference to the sprite image file
	private String image_file = "";
	/* The bitmask system uses 2 bitmasks
	 * per sprite: an I_am_bitmask and an I_hit_bitmask.
	 * For instance, you can do things like:
	 *  I_am_bitmask = player_bitmask+ship_bitmask
	 *  Check "my" I_hit versus your I_am, then I 
	 *  handle the collision if there is a hit. */
	private int i_am_bitmask = 0;
	private int i_hit_bitmask = 0;
	
	/** Constructor */
	public Sprite(double x, double y, double angle, 
			String image_file, 
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.last_draw_angle = angle;
		this.image_file = image_file;
		this.i_am_bitmask = i_am_bitmask;
		this.i_hit_bitmask = i_hit_bitmask;
	}

	/** Sets the image for this sprite to the given buffered image. */
	protected void setImage(BufferedImage image, double angle)
	{
		this.image = Main.image_manager.getTransformedImage(
						image,
						"",
						angle,
						false);
		this.setImageDimensions();
	}

	/** Sets the image for this sprite based on the file referred
	 * to by image_id. */
	protected void setImage(String image_id, double angle)
	{
		this.image_file = image_id;
		this.image = Main.image_manager.getImage(
				this.image_file,
				angle);
		this.setImageDimensions();
	}

	/** Sets the image for this sprite based on
	the file referred to by image_id. Also sets
	the dimensions of this sprite based on the
	image or custom settings if they are
	included. */
	protected void setImage(
			String image_id, double angle,
			int custom_width, int custom_height)
	{
		this.image_file = image_id;
		this.image = Main.image_manager.getImage(
				this.image_file,
				angle,
				custom_width, custom_height);
		this.setImageDimensions();
	}

	public BufferedImage getImage(){ return this.image; }
	public String getImageString(){ return this.image_file; }

	/** Set or update image dimensions to be based on the image file. */
	private void setImageDimensions()
	{
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	/** Return the x value of this sprite. */
	public double getX() 
	{
		return x;
	}
	/** Return the y value of this sprite. */
	public double getY() 
	{
		return y;
	}
	/** Set the x value of this sprite. */
	public void setX(double x) 
	{
		this.x = x;
	}
	/** Set the y value of this sprite. */
	public void setY(double y)
	{
		this.y = y;
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void setAngle(double angle)
	{
		this.angle = angle;
	}

	public void turnClockwise(double turn_rate, double elapsed_seconds)
	{
		this.angle += turn_rate * elapsed_seconds;
	}
	
	public void turnCounterClockwise(double turn_rate, double elapsed_seconds){ this.angle -= turn_rate * elapsed_seconds; }

	public void turnAmount(double turn_rate, double elapsed_seconds)
	{
		this.angle += turn_rate*elapsed_seconds;
	}

	/** Rotate this sprite toward the given x,y coordinates at a rate of
	 * turn_rate radians per second. */
	public void turnToward(
			double x_destination,
			double y_destination,
			double turn_rate,
			double elapsed_seconds)
	{
		//After this line, our angle will be in the range -pi to pi
		this.angle = Utils.normalizeAngle(this.angle);
		double goal_angle = angleToDestination(x_destination,
				y_destination);
		this.turnTowardAngle(goal_angle, turn_rate, elapsed_seconds);
	}

	public void turnAwayFrom(
			double x_destination,
			double y_destination,
			double turn_rate,
			double elapsed_seconds)
	{	//Very similar to turnToward, but turns 180
		//degrees from the turnTowards goal angle

		//After this line, our angle will be in the range -pi to pi
		this.angle = Utils.normalizeAngle(this.angle);
		double goal_angle = angleToDestination(
				x_destination,
				y_destination);
		//Get 180 degree angle from destination
		this.turnTowardAngle(goal_angle + Math.PI, turn_rate, elapsed_seconds);
	}

	public void turnTowardAngle(
			double goal_angle,
			double turn_rate,
			double elapsed_seconds)
	{
		double angle_diff = Utils.getAngleDifference(this.angle, goal_angle);
		//If we are close to our goal angle, snap to target
		if (Math.abs(angle_diff) < turn_rate*elapsed_seconds)
		{
			this.angle=goal_angle;
		}
		//Turn clockwise if the angle difference is negative
		else if (angle_diff<0)
		{
			this.turnClockwise(turn_rate, elapsed_seconds);
		}
		//Turn counter clockwise if the angle difference is positive
		else
		{
			this.turnCounterClockwise(turn_rate, elapsed_seconds);
		}
	}

	public double getXCenter() 
	{
		return x + width /2.0;
	}

	public double getYCenter() 
	{
		return y + height /2.0;
	}

	public double[] getCenter() 
	{
		double[] center = {x + (double)width /2.0, y + (double)height /2.0};
		return center;
	}

	//Center the sprite on the given x and y coordinates.
	public void moveToCenter(double x, double y)
	{
		this.x = x - (double)width/2.0;
		this.y = y - (double)height/2.0;
	}

	public void setIamBitmask(int bitmask)
	{
		i_am_bitmask = bitmask;
	}

	public int getIamBitmask()
	{
		return i_am_bitmask;
	}
	
	public void setIhitBitmask(int bitmask)
	{
		i_hit_bitmask = bitmask;
	}

	public int getIhitBitmask()
	{
		return i_hit_bitmask;
	}

	public boolean isVisible() 
	{
		return vis;
	}

	public void setVisible(Boolean visible) 
	{
		vis = visible;
	}

	public boolean getRemoveMe() 
	{
		return remove_me;
	}

	public void setRemoveMeTrue()
	{
		remove_me = true;
		vis = false;
	}

	public void resurrect()
	{
		remove_me = false;
		vis = true;
	}

	public boolean getIsOnScreen(){ return on_screen; }

	/* Detect if the sprite is at least partially 
	 * visible on the screen. This works with a player-
	 * centered camera. 
	 * draw_x and draw_y are the adjusted draw locations of 
	 * this sprite. 
	 * If a still camera is in use then draw_x and draw_y are 
	 * the same as the current location of the sprite. */
	public void setIsOnScreen(int draw_x, int draw_y)
	{
		this.on_screen = (draw_x < Main.ScreenWidth && draw_x + width > 0) ||
				(draw_y < Main.ScreenHeight && draw_y + height > 0);
	}
	
	public double distanceTo(double destination_x, double destination_y)
	{
		return Utils.distance(getXCenter(), getYCenter(),
				destination_x, destination_y);
	}

	public double distanceTo(Sprite s)
	{
		return Utils.distance(getXCenter(), getYCenter(),
				s.getXCenter(), s.getYCenter());
	}

	//Returns angle from current location to given coordinates.
	public double angleToDestination(double x_destination, double y_destination)
	{
		double xTheTriangle = x_destination - this.getXCenter();
		double yTheTriangle = y_destination - this.getYCenter();
		return Math.atan2(yTheTriangle, xTheTriangle);
	}

	//Returns angle from current location to given Sprite.
	public double angleToSprite(Sprite s)
	{
		return angleToDestination(s.getXCenter(), s.getYCenter());
	}

	/* Pre: other collided with this.
	 * Post: Return the angle of collision relative to the current heading.
	 * Returns an angle between -pi and pi.
	 * -pi is a counter-clockwise rotation from this sprite's current heading.
	 * pi is a clockwise rotation from this sprite's current heading.
	 */
	public double getCollisionAngle(Sprite other)
	{
		double angle_to_other = this.angleToDestination(other.getXCenter(), other.getYCenter());
		return Utils.getAngleDifference(angle_to_other, this.angle);
	}

	/* Draw this sprite. */
	public void draw(Board b, Graphics g)
	{
		this.draw(b, g, 0, 0);
	}

	/* Draw this sprite relative to the player based on
	 * the given offset. The offset may be constant
	 * zero which means we are using the still cam. */
	public void draw(Board b, Graphics g, int offset_x, int offset_y)
	{
		//Determine whether or not to draw the sprite
		if(this.vis)
		{	//Calculate the location on the screen at which to draw
			//this sprite.
			int draw_x = (int)(this.x + offset_x);
			int draw_y = (int)(this.y + offset_y);
			Graphics2D g2d = (Graphics2D) g;
			//Update the on_screen status of this sprite.
			this.setIsOnScreen(draw_x, draw_y);
			if(this.on_screen)
			{
				//TESTING COLLISION
				if(Main.SHOW_COLLISION)
				{
					g2d.setColor(Color.GRAY);
					if(this instanceof SpriteCircular)
					{
						int radius = (int)((SpriteCircular)this).getCollisionRadius();
						g2d.fillOval(draw_x + (this.width/2) - radius,
								draw_y + (this.height/2) - radius,
								radius*2, radius*2);
					}
					else
					{
						g2d.fillRect(draw_x, draw_y,
								this.width, this.height);
					}
				}
				//Update the image orientation if needed
				if(this.last_draw_angle != this.angle)
				{
					this.updateImageOrientation();
				}
				//Draw this sprite's image
				g2d.drawImage(this.image, draw_x, draw_y, b);
			}
		}
	}

	public double getDrawAngle()
	{
		return last_draw_angle;
	}
	public void setDrawAngle(double angle)
	{
		last_draw_angle=angle;
	}

	/** Sprites are compared ONLY based on their x values. A sprite that is
	 * further to the left is considered less than a sprite that is further
	 * to the right. The reason for this is that collision detection can be
	 * made more efficient if sprites can be sorted based on position so we
	 * only need to check if sprites that are near each other have collided. */
	@Override
	public int compareTo(Sprite other) 
	{
		/* For Ascending order*/
		if(this.x < other.x)
		{
			return -1;
		}
		else if(this.x > other.x)
		{
			return 1;
		}
		else
		{
			return 0;
		}
		/* For Descending order do like this */
		/*
		if(this.x < other.x)
		{
			return 1;
		}
		else if(this.x > other.x)
		{
			return -1;
		}
		else
		{
			return 0;
		}
		 */
	}

	//Update this sprite's image's angle to the current angle.
	private void updateImageOrientation()
	{
		//Remember the current center
		double[] center = getCenter();
		//Update the last draw angle
		this.last_draw_angle = this.angle;
		//Reload the image.
		this.image = Main.image_manager.getImage(this.image_file, this.angle);
		//Update the image dimensions.
		this.setImageDimensions();
		//Move image to the old center to prevent rotation wobble.
		this.moveToCenter(center[0], center[1]);
	}
	
	public int getArea()
	{
		return this.width * this.height;
	}
	
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	public void setWidth(int new_width){ this.width = new_width; }
	public void setHeight(int new_height){ this.height = new_height; }

	/* This method is intended to be called once per frame and 
	 * will update this sprite.
	 * double elapsed_seconds is the number of seconds that
	 * elapsed since the previous update. */
	abstract void update(double elapsed_seconds); 

	/* Returns a rectangle around this sprite */
	abstract Rectangle getBoundingRectangle();
}