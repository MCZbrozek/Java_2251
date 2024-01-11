import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.Font;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Extendable class that manages all sprite
drawing, updating, and collisions. */
public class Board extends JPanel 
{
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	//Reference to all the tangible sprites on the screen.
	private ArrayList<SpritePhysics> sprite_list = new ArrayList<>();
	//Object to track user input
	protected KeyManager key_setter = new KeyManager();
	//If the game is paused, draw, but do not update.
	private boolean paused = false;

	//The sprite for the camera to center on
	private SpritePhysics center_on = null;
	private double camera_distance = 0;

	public static int defaultDistance = 30;


	public Board(String fileName)
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
		
		WorldObject[][] world = readInMap(fileName);
		
		//Create an array of DisplayDots
		for(int i=0; i<world.length; i++)
		{
			for(int j=0; j<world[i].length; j++)
			{
				if(world[i][j] == WorldObject.DOT)
				{
					//The +5 on the x,y coordinates
					//is adding half the radius, 
					//otherwise the dots are placed
					//slightly off.
					DisplayDot temp = new DisplayDot(
						j*defaultDistance+5, // x
						i*defaultDistance+5, // y
						0, // Draw countdown (unused)
						10, // radius
						Color.GREEN,
						true); //won't disappear until collided
					this.addSprite(temp);
				}
				else if(world[i][j] == WorldObject.WALL)
				{
					ExampleWall temp = new ExampleWall(
						j*defaultDistance, // x
						i*defaultDistance, // y
						defaultDistance, // width
						defaultDistance); // height
					this.addSprite(temp);
				}
			}
		}
	}

	/** Calls to this method empty sprite_list. */
	public void reset()
	{
		sprite_list = new ArrayList<>();
	}

	@Override
	public void paintComponent(Graphics g) 
	{
		doDrawing(g);
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
		//Use the "space bar" spacebar to toggle pause
		//NOTE: re-pausing now has no effect in this game
		if(this.key_setter.checkKeyReleased(32))
		{
			this.paused = !this.paused;
		}
		//Check collisions and update all sprites
		this.checkCollisions();
		this.updateSprites(elapsed_seconds);
	}

	//This was modified so that only the centered
	//sprite collides with other sprites
	//for simplicity
	private void checkCollisions()
	{
		for (int i = 0; i < sprite_list.size(); i++) 
		{
			SpritePhysics s2 = sprite_list.get(i);
			if(center_on.checkCollided(s2))
			{
				s2.handleCollision(center_on);
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

	/** Returns the all the sprites within the given distance to the
	 * given location. */
	public ArrayList<SpritePhysics> getAllWithinDistance(double x, double y, double distance)
	{
		ArrayList<SpritePhysics> to_return = new ArrayList<>();
		double temp_distance;
		//Loop over all the sprites
		SpritePhysics aSprite_list;
		for (int i=sprite_list.size()-1; i>=0; i--)
		{   aSprite_list = sprite_list.get(i);
			//Check distance to this sprite
			temp_distance = Utils.distance(x, y, aSprite_list.getXCenter(), aSprite_list.getYCenter());
			if (temp_distance < distance)
			{
				to_return.add(aSprite_list);
			}
		}
		return to_return;
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
			//Check if the sprites collided
			if(s.checkCollided(aSprite_list) && s != aSprite_list)
			{
				to_return.add(aSprite_list);
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
	 * sprite list.
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

	protected ArrayList<SpritePhysics> getSpriteList(){ return sprite_list; }

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
	}

	public boolean isPaused(){ return paused; }
	public void setPaused(boolean value){ paused = value; }

	/** Adds the given sprite to sprite_list. */
	public void addSprite(SpritePhysics s){ sprite_list.add(s); }

	/** Adds the given sprites to sprite_list. */
	public void addSprites(ArrayList<SpritePhysics> sprite_list){ sprite_list.addAll(sprite_list); }

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
	
	
	enum WorldObject{
		BLANK,
		DOT,
		WALL
	}
	private WorldObject[][] readInMap(String fileName)
	{
		java.nio.file.Path p = java.nio.file.Paths.get(fileName);
		java.util.Scanner fileIn = null;
		try {
			fileIn = new java.util.Scanner(p);
		} catch (java.io.IOException e) {
			System.out.println("error IO exception: "+e.getMessage());
			System.exit(1);
		}
		int rows = fileIn.nextInt();
		int columns = fileIn.nextInt();
		fileIn.nextLine(); //flush line remainder
		WorldObject[][] world = new WorldObject[rows][columns];
		String line = null;
		int i=0, j=0;
		try
		{
			for(i=0; i<rows; i++)
			{
				line = fileIn.nextLine();
				for(j=0; j<columns; j++)
				{
					switch(line.charAt(j))
					{
						case 'x':
							world[i][j]=WorldObject.DOT;
							break;
						case 'w':
							world[i][j]=WorldObject.WALL;
							break;
						default:
							world[i][j]=WorldObject.BLANK;
					}
				}
			}
		}
		catch(java.lang.StringIndexOutOfBoundsException e)
		{
			System.out.println("\nFailed on line: '"+line+"'");
			System.out.println("i = "+i+". j = "+j);
			System.out.println(e);
			e.printStackTrace();
			System.exit(0);
		}
		
		fileIn.close();
		return world;
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