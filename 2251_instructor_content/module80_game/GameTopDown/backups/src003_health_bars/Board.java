import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class Board extends JPanel 
{
	//Original code came largely from this tutorial:
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	private static final long serialVersionUID = 1L;
	//List of collision bitmasks so only the 
	//things we want to collide do collide.
	public static final int player_bitmask =    1;
	public static final int enemy_bitmask =     2;
	//Reference to player 1's craft
	private Player player1;
	//Reference to all the sprites on the screen.
	private ArrayList<Sprite> sprite_list = new ArrayList<Sprite>();
	//Frames per second
	private int fps = 60;
	//Object to track user input
	private KeyManager key_setter = new KeyManager();
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
		//Create the player
		player1 = new Player(100, 100, //x,y 
					20.0, //collision_radius
					key_setter,
					Board.player_bitmask,//I am
					0); //I hit
		sprite_list.add(player1);
		//Create an enemy
		Enemy e = new Enemy(400, 400, //x,y
				15.0, //collision
				enemy_bitmask,//i am
				0);//I hit
		sprite_list.add(e);
	}

	@Override
	public void paintComponent(Graphics g) 
	{
		doDrawing(g);
		doDrawHUD(g);
	}

	private void doDrawHUD(Graphics g)
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
		//Print x,y coordinates of the ship
		g.drawString("Coordinates: "+Integer.toString((int)this.player1.getX())+", "+Integer.toString((int)this.player1.getY()),
				550, Main.ScreenHeight - 50);
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
	{	//Draw the rest of the sprites
		Sprite s;
		for(int i=0; i<sprite_list.size(); i++)
		{
			s = sprite_list.get(i);
			s.draw(this, g);
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

	public void checkCollisions() 
	{	//This is more efficient with a sorted list.
		Collections.sort(sprite_list);
		//Check collisions between all sprites
		for (int i = 0; i < sprite_list.size()-1; i++) 
		{
			for (int j = i+1; j < sprite_list.size(); j++)
			{
				Sprite s1 = sprite_list.get(i);
				Sprite s2 = sprite_list.get(j);
				//Check the sprites collision bitmasks then
				//check if they collided.
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
				else if(s1.getX()+s1.width < s2.getX())
				{
					break;
				}
			}
		}
	}

	private void updateSprites(double elapsed_seconds)
	{	//Loop backwards over sprites so that bullets appear under ships
		//and removal of sprites is safer.
		for (int i = sprite_list.size()-1; i >= 0; i--)
		{	//Get the current sprite
			Sprite s = sprite_list.get(i);
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

	public void setFPS(int fps)
	{
		this.fps = fps;
	}

	
	private class TAdapter extends KeyAdapter 
	{
		@Override
		public void keyReleased(KeyEvent e) 
		{
			//System.out.println(e.getKeyCode());
			key_setter.keyReleased(e.getKeyCode());
		}

		@Override
		public void keyPressed(KeyEvent e) 
		{
			key_setter.keyPressed(e.getKeyCode());
		}
	}
	
	
	private class MAdapter implements MouseInputListener
	{
		public MAdapter() 
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
					MouseInfo.getPointerInfo().getLocation().x,
					MouseInfo.getPointerInfo().getLocation().y);
		}

		@Override
		public void mouseMoved(MouseEvent arg0)
		{
			key_setter.mouseMoved(
					MouseInfo.getPointerInfo().getLocation().x,
					MouseInfo.getPointerInfo().getLocation().y);
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