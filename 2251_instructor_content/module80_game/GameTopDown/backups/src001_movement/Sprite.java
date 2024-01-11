import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

//Make the sprite comparable so we can sort it for 
//efficient collision detection.
public abstract class Sprite implements Comparable<Sprite>
{
	//Track whether or not the sprite is on the screen
	private boolean on_screen = true;
	//Coordinates
	private double x;
	private double y;
	//Dimensions
	protected int width;
	protected int height;
	//Whether or not the sprite is visible
	protected boolean vis = true;
	//Whether or not the sprite should be removed from the screen
	protected boolean remove_me = false;
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
	protected double collision_radius = 0;
	//Whether this sprite prefers circular or rectangular collisions
	protected boolean collision_circular = true;
	/* The bitmask system is going to change into 2 bitmasks
	 * per sprite: an I_am_bitmask and an I_hit_bitmask.
	 * This will make things more readable and simplify
	 * collision handling code.
	 * For instance, you can do things like:
	 *  I_am_bitmask = player_bitmask+ship_bitmask
	 *  Check "my" I_hit versus your I_am, then I 
	 *  handle the collision if there is a hit. */
	private int i_am_bitmask = 0;
	private int i_hit_bitmask = 0;
	
	//Constructor
	public Sprite(double x, double y, double angle, 
			double radius, String image_file, 
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.last_draw_angle = angle;
		this.collision_radius = radius;
		this.image_file = image_file;
		this.i_am_bitmask = i_am_bitmask;
		this.i_hit_bitmask = i_hit_bitmask;
	}
	
	protected void setImage(String image_id, double angle,
			int custom_width, int custom_height, boolean use_custom)
	{
		this.image_file = image_id;
		this.image = Main.image_manager.getImage(
				this.image_file,
				angle,
				custom_width, custom_height, use_custom);
		this.setImageDimensions();
	}

	//Set / update image dimensions
	private void setImageDimensions()
	{
		width = image.getWidth(null);
		height = image.getHeight(null);
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

	public int getIamBitmask()
	{
		return i_am_bitmask;
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
	}

	public void move(double elapsed_seconds) 
	{
		this.x += this.dx * elapsed_seconds;
		this.y += this.dy * elapsed_seconds;
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
		if(draw_x > Main.ScreenWidth)
		{
			return false;
		}
		if(draw_y > Main.ScreenHeight)
		{
			return false;
		}
		if(draw_x + width < 0)
		{
			return false;
		}
		if(draw_y + height < 0)
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
	public void draw(Board b, Graphics g)
	{
		//Determine whether or not to draw the sprite
		if(this.vis)
		{
			Graphics2D g2d = (Graphics2D) g;
			//Update the on_screen status of this sprite.
			this.on_screen = this.OnScreen((int)this.x, (int)this.y);
			if(this.on_screen)
			{
				//TESTING COllision
				/*if(Main.SHOW_COLLISION)
				{
					g2d.setColor(Color.GRAY);
					if(this.collision_circular)
					{
						g2d.fillOval((int)(this.x + (this.width/2) - this.collision_radius),
								(int)(this.y + (this.height/2) - this.collision_radius),
								(int)this.collision_radius*2, (int)this.collision_radius*2);
					}
					else
					{
						g2d.fillRect((int)this.x, (int)this.y,
								this.width, this.height);
					}
				}*/
				//Update the image orientation if needed
				//if(this.last_draw_angle != this.angle)
				//{
				//	this.updateImageOrientation();
				//}
				//Draw this sprite's image
				//g2d.drawImage(this.image, (int)this.x, (int)this.y, b);
				g2d.setColor(Color.BLUE);
				g2d.fillOval((int)(this.x - this.collision_radius),
							(int)(this.y - this.collision_radius),
							(int)this.collision_radius*2, (int)this.collision_radius*2);
				g2d.setColor(Color.WHITE);
				double heading_adjust = 30;
				double dx = Math.cos(this.angle)*heading_adjust;
				double dy = Math.sin(this.angle)*heading_adjust;
				double heading_radius = this.collision_radius/3;
				g2d.fillOval((int)(this.x+dx - heading_radius),
						(int)(this.y+dy - heading_radius),
						(int)heading_radius*2, (int)heading_radius*2);
			}
		}
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

	//Update this sprite's image's angle to the current angle.
	private void updateImageOrientation()
	{
		//Remember the current center
		double[] center = getCenter();
		//Update the last draw angle
		this.last_draw_angle = this.angle;
		//Reload the image.
		this.image = Main.image_manager.getImage(this.image_file, this.angle, 0,0,false);
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

	/* This method is intended to be called once per frame and 
	 * will update this sprite.
	 * double elapsed_seconds is the number of seconds that
	 * elapsed since the previous update.
	 */
	abstract void update(double elapsed_seconds); 

	/* Handle a collision between this sprite and other.
	 */
	abstract void handleCollision(Sprite other);
}