import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

public class Board extends JPanel 
{
	//Original code came largely from this tutorial:
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	private static final long serialVersionUID = 1L;
	//Reference to all the tangible sprites on the screen.
	private ArrayList<SpritePhysics> sprite_list = new ArrayList<>();
	//Reference to all the intangible sprites on the screen.
	private ArrayList<SpritePhysics> sprite_list_intangible = new ArrayList<>();
	//Frames per second
	private int fps = 60;
	//Object to track user input
	protected KeyManager key_setter = new KeyManager();
	//If the game is paused, draw, but do not update.
	private boolean paused = false;

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

	//Draw all the sprites
	private void doDrawing(Graphics g) 
	{
		//Draw the sprites
		for (SpritePhysics aSprite_list : sprite_list_intangible)
		{
			aSprite_list.draw(this, g);
		}
		for (SpritePhysics aSprite_list : sprite_list)
		{
			aSprite_list.draw(this, g);
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
		for (SpritePhysics aSprite_list : sprite_list)
		{   //Don't collision check on self.
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

	/** Adds the given sprite to sprite_list. */
	public void addSprite(SpritePhysics s){ sprite_list.add(s); }

	/** Adds the given sprite to sprite_list_intangible. */
	public void addSpriteIntangible(SpritePhysics s){ sprite_list_intangible.add(s); }

	/** Set the Frames Per Second. This is mainly used for testing. */
	public void setFPS(int fps)
	{
		this.fps = fps;
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
		public void mouseClicked(MouseEvent arg0)
		{
		}

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