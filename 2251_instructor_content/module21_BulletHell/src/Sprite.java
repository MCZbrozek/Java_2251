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
	//Whether or not this sprite is tangible
	private boolean tangible = true;
	//Coordinates
	protected double x;
	protected double y;
	//Dimensions
	protected int width;
	protected int height;
	//Whether or not the sprite is visible
	protected boolean vis = true;
	//Whether or not the sprite should be removed from the screen
	protected boolean remove_me = false;
	//Whether or not the sprite should be destroyed
	private boolean destroy_me = false;
	//Image of the sprite
	private BufferedImage image;
	//velocity of the sprite
	protected double dx = 0;
	protected double dy = 0;
	//Angle to draw the image at.
	private double angle = 0.0;
	//Last angle that this craft was drawn at.
	//We use this to rotate the image the minimum number
	//of times possible. Certainly we should only rotate 
	//the image when the sprite is on the screen.
	protected double last_draw_angle = 0.0;
	//Reference to the ship image file
	protected String image_file = "";
	//Detect collisions in a circle around the sprite.
	private double collision_radius = 0;
	//Whether this sprite prefers circular or rectangular collisions
	protected boolean collision_circular = true;
	//Bitmask for seeing what this sprite should collide with.
	private int collision_bitmask = 0;

	//Constructor
	public Sprite(double x, double y,
			double angle, 
			double collision_radius,
			String image_file,
			int collision_bitmask)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.last_draw_angle = angle;
		this.collision_radius = collision_radius;
		this.image_file = image_file;
		this.collision_bitmask = collision_bitmask;
	}
	
	protected void setImage(String image_id, double angle)
	{
		this.image_file = image_id;
		this.image = Main.image_manager.getImage(
				this.image_file,
				angle);
		this.setImageDimensions();
	}
	
	protected void setImage(BufferedImage image, double angle)
	{
		this.image = Main.image_manager.getTransformedImage(image, angle);
		this.setImageDimensions();
	}

	//Set / update image dimensions
	private void setImageDimensions()
	{
		width = image.getWidth(null);
		height = image.getHeight(null);
	}    

	public BufferedImage getImage() 
	{
		return image;
	}

	public double getX() 
	{
		return x;
	}

	public double getY() 
	{
		return y;
	}

	public void setX(double x) 
	{
		this.x = x;
	}

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
	}
	
	public boolean isDestroyed()
	{
		return destroy_me;
	}
	
	public void destroyMe()
	{
		this.destroy_me = true;
		this.remove_me = true;
		this.vis = false;
		this.on_screen = false;
	}

	public void move(double elapsed_seconds) 
	{
		this.x += this.dx * elapsed_seconds;
		this.y += this.dy * elapsed_seconds;
	}
	
	protected void turnToward( //TODO
			double x, double y,
			double turn_rate,
			double elapsed_seconds)
	{	
		double goal_angle = this.angleToDestination(x, y);
		double angle_diff = Utils.getAngleDifference(goal_angle, this.getAngle());
		//If we are close to our goal angle, snap to target
		if (Math.abs(angle_diff) < turn_rate*elapsed_seconds)
		{
			this.setAngle(goal_angle);
		}
		//Turn counter clockwise if the angle difference is negative
		else if (angle_diff<0)
		{
			this.setAngle(this.getAngle()-turn_rate*elapsed_seconds);
		}
		//Turn clockwise if the angle difference is positive
		else
		{
			this.setAngle(this.getAngle()+turn_rate*elapsed_seconds);
		}
	}

	/* Detect if the sprite is at least partially 
	 * visible on the screen. This works with a player-
	 * centered camera. 
	 * draw_x and draw_y are the adjusted draw locations of 
	 * this sprite. 
	 * If a still camera is in use then draw_x and draw_y are 
	 * the same as the current location of the sprite. */
	public boolean OnScreen(int draw_x, int draw_y) 
	{
		if(draw_x > Main.ScreenWidth || 
				draw_y > Main.ScreenHeight ||
				draw_x + width < 0 ||
				draw_y + height < 0)
		{
			return false;
		}
		return true;
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
	
	/* Checks for collision with other.
	 * Uses the preferred collision detection method.
	 * If preferences conflict, rectangular is used. */
	public boolean checkCollided(Sprite other)
	{
		if(this.collision_circular && other.collision_circular)
		{
			return this.CollidedRadius(other);
		}
		else
		{
			return this.CollidedRectangle(other);
		}
	}

	public double distanceTo(double destination_x, double destination_y)
	{
		return Utils.distance(getXCenter(), getYCenter(),
				destination_x, destination_y);
	}

	//Returns angle from current location to given coordinates.
	public double angleToDestination(double x_destination, double y_destination)
	{
		double xTheTriangle = x_destination - this.getXCenter();
		double yTheTriangle = y_destination - this.getYCenter();
		return Math.atan2(yTheTriangle, xTheTriangle);
	}

	/* Draw this sprite relative to the player based on 
	 * the given offset. The offset may be constant
	 * zero which means we are using the still cam. */
	public void draw(Board b, Graphics g, int offset_x, int offset_y)
	{
		//TODO GameLoopExample does this. Is it more efficient?
		/*
    	//BS way of clearing out the old rectangle to save CPU.
		g.setColor(getBackground());
		g.fillRect(lastDrawX-1, lastDrawY-1, ballWidth+2, ballHeight+2);
		g.fillRect(5, 0, 75, 30);
		 */
		//Determine whether or not to draw the sprite
		if(this.vis)
		{	//Calculate the location on the screen at which to draw
			//this sprite. Also update last_x and last_y.
			int draw_x = (int)(this.x + offset_x);
			int draw_y = (int)(this.y + offset_y);
			//Update the on_screen status of this sprite.
			this.on_screen = this.OnScreen(draw_x, draw_y);
			if(this.on_screen)
			{
				//Update the image orientation if needed
				if(this.last_draw_angle != this.angle)
				{
					this.updateImageOrientation();
				}
				//Draw this sprite's image
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(this.image, draw_x, draw_y, b);
				//TESTING COllision
				if(Main.SHOW_COLLISION)
				{
					g2d.setColor(Color.CYAN);
					if(this.collision_circular)
					{
						g2d.drawOval((int)(draw_x + (this.width/2) - this.collision_radius),
								(int)(draw_y + (this.height/2) - this.collision_radius),
								(int)this.collision_radius*2, (int)this.collision_radius*2);
						
					}
					else
					{
						g2d.drawRect(draw_x, draw_y,
								this.width, this.height);
					}
				}
			}
		}
	}

	public void takeDamage(int damage, Sprite responsible_sprite)
	{
	}
	
	public void setTangible(Boolean is_tangible)
	{
		this.tangible = is_tangible;
	}
	
	public Boolean isTangible()
	{
		return this.tangible;
	}

	//Update this sprite's image's angle to the current angle.
	protected void updateImageOrientation()
	{
		//Remember the current center
		double[] center = getCenter();
		//Reload the image.
		this.image = Main.image_manager.getImage(this.image_file, this.angle);
		//Update the image dimensions.
		this.setImageDimensions();
		//Move image to the old center to prevent rotation wobble.
		this.moveToCenter(center[0], center[1]);
	}

	//Check circular collision
	private boolean CollidedRadius(Sprite other)
	{
		double[] other_center = other.getCenter();
		double distance = distanceTo(other_center[0], other_center[1]);
		return distance < (this.collision_radius + other.collision_radius);
	}

	//Check rectangular collision
	private boolean CollidedRectangle(Sprite other)
	{
		//Get the rectangle around the sprite.
		Rectangle my_rect = this.getBoundingRectangle();
		Rectangle other_rect = other.getBoundingRectangle();
		return my_rect.intersects(other_rect);
	}

	private Rectangle getBoundingRectangle() 
	{
		//If this sprite prefers circular collisions, 
		//then get a bounding rectangle based on radius
		//rather than width and height
		if(this.collision_circular)
		{
			return new Rectangle((int)(getXCenter() - collision_radius),
					(int)(getYCenter() - collision_radius), 
					(int)collision_radius*2, (int)collision_radius*2);
		}
		else
		{
			return new Rectangle((int)x, (int)y, width, height);
		}
	}

	public int getArea()
	{
		return this.width * this.height;
	}
	
	public void setCollisionRadius(double radius)
	{
		collision_radius = radius;
	}

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
	
	public int getBitmask()
	{
		return collision_bitmask;
	}

	/* This method is intended to be called once per frame and 
	 * will update this sprite. The arguments include a 
	 * reference to player 1's craft, the list of all sprites, 
	 * and this sprite's current index in the sprite list,
	 * which can be useful to more efficiently find other 
	 * nearby sprites.
	 * double elapsed_seconds is the number of seconds that
	 * elapsed since the previous update.
	 */
	abstract void update(double elapsed_seconds);

	/* Handle a collision between this sprite and other.
	 */
	abstract void handleCollision(Sprite other);
}



