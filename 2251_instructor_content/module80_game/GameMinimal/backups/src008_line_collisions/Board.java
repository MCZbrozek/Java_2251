import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board extends JPanel 
{
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	//Reference to all the tangible sprites on the screen.
	private List<SpritePhysics> sprite_list = Collections.synchronizedList(new ArrayList<>());
	//Reference to all the intangible sprites on the screen.
	private List<SpritePhysics> sprite_list_intangible = Collections.synchronizedList(new ArrayList<>());
	//Frames per second
	private int fps = 60;
	//Object to track user input
	protected KeyManager key_setter = new KeyManager();
	//If the game is paused, draw, but do not update.
	private boolean paused = false;

	//Add variables for a bullet time effect.
	private boolean bullet_time_on = false;
	private double bullet_time_modifier = 0.5;
	private double bullet_time_countdown = 0;
	private double bullet_time_reset = 10;

	//The sprite for the camera to center on
	private SpritePhysics center_on = null;
	private double camera_distance = 0;

	public Board()
	{
		addKeyListener(new TAdapter());
		MAdapter myMouseEventListener = new MAdapter();
		this.addMouseListener(myMouseEventListener);
		this.addMouseMotionListener(myMouseEventListener);
		setFocusable(true);
		setBackground(Color.BLACK);
		//Double buffer gives efficiency gains. See here:
		//http://docs.oracle.com/javase/tutorial/extra/fullscreen/doublebuf.html
		setDoubleBuffered(true);
	}

	/** Calls to this method effectively empty sprite_list and
	 * sprite_list_intangible. Also reset bullet time variables. */
	public void reset()
	{
		sprite_list = new ArrayList<>();
		sprite_list_intangible = new ArrayList<>();
		bullet_time_on = false;
		bullet_time_countdown = 0;
	}

	@Override
	public void paintComponent(Graphics g) 
	{
		doDrawing(g);
		doDrawHUD(g);
	}

	protected void doDrawHUD(Graphics g)
	{
		//Display the framerate
		Font font = new Font("Serif", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.YELLOW);
		g.drawString("Framerate:"+Integer.toString(fps), 
				25, Main.ScreenHeight - 50);
		//Display the timing profile
		if(Main.USE_PROFILER)
		{
			for(int i=0; i<Main.broad_profiler_labels.length; i++)
			{
				g.drawString(Main.profiler.getProfileText(i), 
						225, Main.ScreenHeight - 20 - 30*i);
			}
		}
		//Print the word "Paused" if the game is paused.
		if(paused)
		{
			g.setColor(Color.white);
			font = new Font("Serif", Font.BOLD, 80);
			g.setFont(font);
			g.drawString("Paused",
					Main.ScreenWidth/2-200, Main.ScreenHeight/2-30);
		}
	}

	/** Draw all the sprites */
	protected void doDrawing(Graphics g)
	{
		int offset_x = 0;
		int offset_y = 0;
		//Center the camera on a particular sprite
		if(this.center_on != null)
		{
			offset_x = (int)(Main.ScreenWidth/2 + Math.cos(center_on.getAngle())*camera_distance - center_on.getXCenter());
			offset_y = (int)(Main.ScreenHeight/2 + Math.sin(center_on.getAngle())*camera_distance - center_on.getYCenter());
		}
		this.doDrawing(g, offset_x, offset_y);
	}

	/** Draw all the sprites */
	protected void doDrawing(Graphics g, int offset_x, int offset_y)
	{
		//Draw the sprites
		for (SpritePhysics aSprite_list : sprite_list_intangible)
		{
			aSprite_list.draw(this, g, offset_x, offset_y);
		}
		for (SpritePhysics aSprite_list : sprite_list)
		{
			aSprite_list.draw(this, g, offset_x, offset_y);
		}
	}

	//Takes the number of seconds that we would like 
	//to elapse between each game update.
	public void update(double elapsed_seconds)
	{
		//Use escape key to exit the game
		if(this.key_setter.ascii_input[27])
		{
			System.exit(0);
		}
		//Use the "p" key to toggle pause
		if(this.key_setter.checkKeyReleased(80))
		{
			this.paused = !this.paused;
		}		
		if(!paused)
		{
			//Check for bullet time
			if(bullet_time_on)
			{
				elapsed_seconds = elapsed_seconds * bullet_time_modifier;
				bullet_time_countdown -= elapsed_seconds;
				bullet_time_on = bullet_time_countdown>0;
			}
			//Check collisions and update all sprites
			this.checkCollisions();
			this.updateSprites(elapsed_seconds);
		} //if(!paused)
	}

	private void checkCollisions()
	{	//This is more efficient with a sorted list.
		Collections.sort(sprite_list);
		//Check collisions between all sprites
		for (int i = 0; i < sprite_list.size()-1; i++) 
		{
			for (int j = i+1; j < sprite_list.size(); j++)
			{
				SpritePhysics s1 = sprite_list.get(i);
				SpritePhysics s2 = sprite_list.get(j);
				//Check if the sprites collided, then check their collision bitmasks
				if(s1.checkCollided(s2))
				{
					//Now check if the sprites interact with each other
					if((s1.getIhitBitmask() & s2.getIamBitmask())!=0)
					{	//s1 hits s2
						s1.handleCollision(s2);						
					}
					if((s2.getIhitBitmask() & s1.getIamBitmask())!=0)
					{	//s2 hits s1
						s2.handleCollision(s1);						
					}
				}
				//Stop comparison early if s1's rightmost edge
				//is left of s2's leftmost edge.
				//Since the list is sorted, no further collisions
				//are possible.
				else if(s1.getX()+s1.getWidth() < s2.getX())
				{
					break;
				}
			}
		}
	}

	/** Returns the sprite nearest to s.
	 * Will not return s itself. */
	public SpritePhysics getNearest(SpritePhysics s)
	{
		SpritePhysics nearest = null;
		double distance = Double.MAX_VALUE;
		double sx = s.getXCenter();
		double sy = s.getYCenter();
		double temp_distance;
		//Loop over all the sprites
		SpritePhysics aSprite_list;
		for (int i=sprite_list.size()-1; i>=0; i--)
		{   aSprite_list = sprite_list.get(i);
			//Don't collision check on self.
			if(s != aSprite_list)
			{
				//Check distance to this sprite
				temp_distance = Utils.distance(sx, sy, aSprite_list.getXCenter(), aSprite_list.getYCenter());
				if (temp_distance < distance)
				{    //Update nearest
					nearest = aSprite_list;
					distance = temp_distance;
				}
			}
		}
		return nearest;
	}

	/** Returns the sprite nearest to the given location. */
	public SpritePhysics getNearest(double x, double y)
	{
		SpritePhysics nearest = null;
		double distance = Double.MAX_VALUE;
		double temp_distance;
		//Loop over all the sprites
		SpritePhysics aSprite_list;
		for (int i=sprite_list.size()-1; i>=0; i--)
		{   aSprite_list = sprite_list.get(i);
			//Check distance to this sprite
			temp_distance = Utils.distance(x, y, aSprite_list.getXCenter(), aSprite_list.getYCenter());
			if (temp_distance < distance)
			{    //Update nearest
				nearest = aSprite_list;
				distance = temp_distance;
			}
		}
		return nearest;
	}

	/* Returns list of sprites that collide with the given line. */
	public ArrayList<SpritePhysics> getLineCollisions(
			double origin_x, double origin_y,
			double angle, double length,
			double beam_width)
	{
		//Store the sprites that hit the line here.
		ArrayList<SpritePhysics> to_return = new ArrayList<>();
		//Calculate the change in x and change in y.
		double dx = Math.cos(angle);
		double dy = Math.sin(angle);
		//Cut our losses if the beam is perfectly vertical or horizontal
		if(dy == 0 || dx == 0)
		{
			System.out.println("WARNING in Board.getLineCollisions: Perfectly vertical or horizontal beam. Aborting beam in order to avoid divide by zero errror.");
			return to_return;
		}
		//Calculate slope and other useful variables
		double end_x = origin_x+Math.cos(angle)*length;
		double end_y = origin_y+Math.sin(angle)*length;
		double slope = dy/dx;
		double negative_reciprocal = -dx/dy;
		double y_intercept = origin_y - origin_x*slope;
		Point point_of_intersection;
		//Loop over all the sprites
		//for (SpritePhysics sp : sprite_list)
		for (int i = sprite_list.size()-1; i >= 0; i--)
		{
			SpritePhysics sp = sprite_list.get(i);
			point_of_intersection = null;
			if(sp instanceof SpriteCircular)
			{
				point_of_intersection = Utils.beamIntersectsSpriteCirc(
						(SpriteCircular)sp, beam_width, y_intercept, slope,
						negative_reciprocal);

				//TODO TESTING START
				/*if(sp instanceof ExampleRoundWall)
				{	//Draw player ship line
					DisplayLine l = new DisplayLine(origin_x,origin_y,
							0.1, end_x, end_y, (int)beam_width, Color.WHITE);
					this.addSprite(l);
					//Draw perpendicular line
					double alt_angle = Math.atan2(-dx,dy);
					double alt_end_x = sp.getXCenter()+Math.cos(alt_angle)*length;
					double alt_end_y = sp.getYCenter()+Math.sin(alt_angle)*length;
					l = new DisplayLine(sp.getXCenter(),sp.getYCenter(),
							0.1, alt_end_x, alt_end_y, (int)beam_width, Color.WHITE);
					this.addSprite(l);
					//Draw intersection
					if(point_of_intersection != null)
					{
						System.out.println(Integer.toString(point_of_intersection.x)+","+Integer.toString(point_of_intersection.y));
						DisplayDot d = new DisplayDot(point_of_intersection.x, point_of_intersection.y,
							0.1, 5, Color.RED);
						this.addSprite(d);
					}
				}*/
				//TODO TESTING END
			}
			else
			{
				point_of_intersection = Utils.beamIntersectsSpriteRect(
						sp, slope, y_intercept,
						origin_x, origin_y);
			}
			//Since the sprite collided. Check if the sprite is within
			// the length of the line.
			if(point_of_intersection != null)
			{
				//This condition prevents the collision from occurring behind the line
				if(this.isBetween(origin_x, end_x, point_of_intersection.x) &&
						this.isBetween(origin_y, end_y, point_of_intersection.y))
				{
					/*TODO TESTING
					DisplayDot d = new DisplayDot(point_of_intersection.x, point_of_intersection.y,
							0.1, 5, Color.RED);
					this.addSprite(d);*/
					to_return.add(sp);
				}
			}
		}
		return to_return;
	}

	private boolean isBetween(double side1, double side2, double tween)
	{
		//Reverse the sides if they are out of order.
		if(side1 > side2)
		{
			double temp = side1;
			side1 = side2;
			side2 = temp;
		}
		return side1 < tween && tween < side2;
	}

	/** Pre: sprite_list is sorted.
	 * Returns all the sprites that s collides with. */
	public ArrayList<SpritePhysics> getCollisionsWith(SpritePhysics s)
	{
		ArrayList<SpritePhysics> to_return = new ArrayList<>();
		//Loop over all the sprites
		for (SpritePhysics aSprite_list : sprite_list)
		{
			//Check if the sprites collided then check their collision bitmasks.
			if(s.checkCollided(aSprite_list) && s != aSprite_list)
			{
				//Now check if the sprites interact with each other
				if((s.getIhitBitmask() & aSprite_list.getIamBitmask())!=0)
				{
					to_return.add(aSprite_list);
				}
			}
			//Stop comparison early if aSprite_list's rightmost edge
			//is left of s's leftmost edge.
			//Since the list is sorted, no further collisions
			//are possible.
			else if(aSprite_list.getX()+aSprite_list.getWidth() < s.getX())
			{
				break;
			}
		}
		return to_return;
	}

	/** Pre: Nothing. This method does not assume that the sprite_list
	 * is sorted.
	 * Post: Returns true if s collides with anything in the tangible
	 * sprite list. This ignores bitmasks so objects that might not
	 * otherwise collide can be said to collide by this method.
	 * This is typically to prevent spawning objects from spawning
	 * sprites over top of each other. */
	public boolean anyCollisions(SpritePhysics s)
	{	//Loop over all the sprites
		for (SpritePhysics aSprite_list : sprite_list)
		{
			if(s.checkCollided(aSprite_list))
			{
				return true;
			}
		}
		return false;
	}

	protected List<SpritePhysics> getSpriteList(){ return sprite_list; }
	protected List<SpritePhysics> getSpriteListIntangible(){ return sprite_list_intangible; }

	protected void updateSprites(double elapsed_seconds)
	{	//Loop backwards over sprites so that bullets appear under ships
		//and removal of sprites is safer.
		Sprite s;
		for (int i = sprite_list.size()-1; i >= 0; i--)
		{	//Get the current sprite
			s = sprite_list.get(i);
			//Decide to update or remove sprite
			if(s.getRemoveMe())
			{
				sprite_list.remove(i);
			}
			else
			{
				s.update(elapsed_seconds);
			}
		}
		//Do the same for intangible sprites
		for (int i = sprite_list_intangible.size()-1; i >= 0; i--)
		{	//Get the current sprite
			s = sprite_list_intangible.get(i);
			//Decide to update or remove sprite
			if(s.getRemoveMe())
			{
				sprite_list_intangible.remove(i);
			}
			else
			{
				s.update(elapsed_seconds);
			}
		}
	}

	public int getTotalSpriteCount(){ return sprite_list.size()+sprite_list_intangible.size(); }

	public boolean bulletTimeIsSet(){ return bullet_time_on; }
	public double getBulletTimeModifier() { return bullet_time_modifier; }
	public void setBulletTimeModifier(double modifier){ bullet_time_modifier = modifier; }
	public double getBulletTimeCountdown() { return bullet_time_countdown; }
	public void activateBulletTime()
	{
		bullet_time_on = true;
		bullet_time_countdown = bullet_time_reset;
	}

	public boolean isPaused(){ return paused; }
	public void setPaused(boolean value){ paused = value; }

	/** Adds the given sprite to sprite_list. */
	public void addSprite(SpritePhysics s){ sprite_list.add(s); }

	/** Adds the given sprites to sprite_list. */
	public void addSprites(ArrayList<SpritePhysics> sprite_list){ sprite_list.addAll(sprite_list); }

	/** Adds the given sprite to sprite_list_intangible. */
	public void addSpriteIntangible(SpritePhysics s){ sprite_list_intangible.add(s); }

	/** Set the Frames Per Second. This is mainly used for testing. */
	public void setFPS(int fps)
	{
		this.fps = fps;
	}

	/** Set the sprite to center the camera on. */
	public void centerOnSprite(SpritePhysics center)
	{
		this.center_on = center;
	}

	/** Set the sprite to center the camera on with angle and distance
	 * modifiers. */
	public void centerOnSprite(SpritePhysics center, double distance)
	{
		this.camera_distance = distance;
		this.center_on = center;
	}


	/** Private object used for keeping track of keyboard inputs. */
	private class TAdapter extends KeyAdapter 
	{
		@Override
		public void keyReleased(KeyEvent e) 
		{
			key_setter.keyReleased(e.getKeyCode());
		}

		@Override
		public void keyPressed(KeyEvent e) 
		{
			key_setter.keyPressed(e.getKeyCode());
		}
	}


	/** Private object used for keeping track of mouse inputs. */
	private class MAdapter implements MouseInputListener
	{
		MAdapter()
		{
			super();
		}

		@Override
		public void mouseClicked(MouseEvent arg0){ key_setter.mouseClicked(); }

		@Override
		public void mousePressed(MouseEvent arg0)
		{
			key_setter.mousePressed();
		}

		@Override
		public void mouseReleased(MouseEvent arg0)
		{
			key_setter.mouseReleased();
		}

		@Override
		public void mouseDragged(MouseEvent arg0)
		{
			key_setter.mousePressed();
			key_setter.mouseMoved(
					arg0.getXOnScreen(),
					arg0.getYOnScreen());
		}

		@Override
		public void mouseMoved(MouseEvent arg0)
		{
			key_setter.mouseMoved(
					arg0.getXOnScreen(),
					arg0.getYOnScreen());
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
		}
	}

}