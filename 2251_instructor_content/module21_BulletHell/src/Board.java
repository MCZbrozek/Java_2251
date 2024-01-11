import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class Board extends JPanel 
{	//Original code came largely from this tutorial:
	//http://zetcode.com/tutorials/javagamestutorial/movingsprites/
	private static final long serialVersionUID = 1L;	
	//Energy and life locations so powerups can fly to this 
	//location when hit
	public final int ENERGY_Y = 20;
	public final int ENERGY_X = Main.ScreenWidth-380;
	public final int LIFE_Y = 20;
	public final int LIFE_X = Main.ScreenWidth-100;
	public final int SCORE_Y = 20;
	public final int SCORE_X = Main.ScreenWidth-490;
	
	private boolean bullet_time_on = false;
	private double bullet_time_countdown = 0;
	private double bullet_time_reset = 10;
	//Reference to player's craft
	private Player player = null;
	//Reference to all the sprites on the screen.
	private ArrayList<Sprite> tangible_sprites;
	private ArrayList<Sprite> intangibles;
	//List of collision bitmasks so only the 
	//things we want to collide do collide.
	public static final int player_bitmask =          1+  4+8+16+32;
	public static final int player_bullet_bitmask =     2            +128;
	public static final int enemy_bitmask =             2+4;
	public static final int enemy_bullet_bitmask =          8;
	public static final int asteroid_bitmask =                16+     128;
	public static final int pickup_bitmask =                     32;
	public static final int seeker_missile_bitmask =          16+   64;

	private int fps = 60;
	private KeyManager key_setter = new KeyManager();
	//If the game is paused, draw, but do not update.
	private boolean paused = false;
	private Spawner spawner;
	private double score = 0;
	private String user_name = "";
	private boolean high_scores_loaded = false;
	//Stores username first and score second.
	private String[][] high_score_list = null;
	private final int MAX_HIGH_SCORES = 8;

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

		this.reset();
	}
	
	private void reset()
	{
		user_name = "";
		score = 0;
		high_scores_loaded = false;
		high_score_list = null;
		bullet_time_on = false;
		bullet_time_countdown = 0;
		tangible_sprites = new ArrayList<Sprite>();
		intangibles = new ArrayList<Sprite>();
		spawner = new Spawner(this);
		//Create player1
		player = new Player(0, 0, //x,y
				-Math.PI/2, //angle
				"playerShip1_blue.png", //image_file
				null, //Sprite target,
				1, //int max_health,
				this, //Board board_reference
				key_setter);
		tangible_sprites.add(player);
	}
		
	@Override
	public void paintComponent(Graphics g) 
	{
		drawGame(g);
		drawHUD(g);
	}

	private void drawHUD(Graphics g)
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
			for(int i=0; i<=Profiler.YIELD_INDEX; i++)
			{
				g.drawString(Main.profiler.getProfileText(i), 
						225, Main.ScreenHeight - 20 - 30*i);
			}
		}
		//Display bullet time indicator
		if(bullet_time_on)
		{
			g.setColor(Color.white);
			font = new Font("Serif", Font.BOLD, 80);
			g.setFont(font);
			g.drawString("Bullet Time! "+Integer.toString((int)bullet_time_countdown),
					Main.ScreenWidth/2-200, Main.ScreenHeight/2-30);
		}
		//Display invincibility indicator
		if(player.isInvincible())
		{
			g.setColor(Color.white);
			font = new Font("Serif", Font.BOLD, 80);
			g.setFont(font);
			g.drawString("Invincible! "+Integer.toString((int)player.getInvincibilityCountdown()),
					Main.ScreenWidth/2-200, Main.ScreenHeight/2-30);
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
		//Display score
		g.setColor(Color.white);
		font = new Font("Serif", Font.BOLD, 30);
		g.setFont(font);
		g.drawString("Score:", SCORE_X-50, SCORE_Y+22);		
		g.drawString(Utils.formatDecimal(this.score), SCORE_X, SCORE_Y+22);		
		//Display energy.
		int total_width = (int)(150*this.player.getMaxEnergyPercent());
		g.setColor(Color.DARK_GRAY);
		g.fillRect(this.ENERGY_X, this.ENERGY_Y, total_width, 20);
		int width = (int)(total_width*this.player.getEnergyPercent());
		g.setColor(Color.CYAN);
		g.fillRect(this.ENERGY_X, this.ENERGY_Y, width, 20);
		g.setColor(Color.GRAY);
		font = new Font("Serif", Font.BOLD, 20);
		g.setFont(font);
		g.drawString("ENERGY", this.ENERGY_X+5, this.ENERGY_Y+18);
		//Display lives or game over.
		if(this.player.getLives() > 0)
		{
			BufferedImage life_image = Main.image_manager.getImage(
					"playerLife1_blue.png",
					0); //angle
			Graphics2D g2d = (Graphics2D) g;
			for(int i = 0; i<this.player.getLives(); i++)
			{
				g2d.drawImage(life_image,
						this.LIFE_X-(50*i), //x
						this.LIFE_Y, //draw_y, 
						this);
			}
		}
		else
		{
			//If the high scores have not yet been loaded,
			//read them in from file.
			if(!high_scores_loaded)
			{	
				loadHighScores();
			}
			g.setColor(Color.white);
			font = new Font("Serif", Font.BOLD, 60);
			g.setFont(font);
			g.drawString("Game Over",
					Main.ScreenWidth/2-200,
					Main.ScreenHeight/2-300);
			g.drawString("High scores",
					Main.ScreenWidth/2-200,
					Main.ScreenHeight/2-250);
			//Paint all the high scores on the screen
			font = new Font("Serif", Font.BOLD, 32);
			g.setFont(font);
			for(int i=0; i<high_score_list.length; i++)
			{
				if(high_score_list[i][0] == null){ break; }
				g.drawString(high_score_list[i][0]+"  -  "+high_score_list[i][1],
						Main.ScreenWidth/2-300,
						Main.ScreenHeight/2-200+50*i);
			}
			int high_score_index = getNewScoreIndex();
			if(high_score_index < this.MAX_HIGH_SCORES)
			{
				g.drawString("Congratulations! A High Score! Enter your name: "+user_name,
					Main.ScreenWidth/2-400,
					Main.ScreenHeight/2+200);
			}
			font = new Font("Serif", Font.BOLD, 30);
			g.setFont(font);
			g.drawString("Press [enter] to restart",
					Main.ScreenWidth/2-230,
					Main.ScreenHeight/2+250);
		}
	}

	//Draw all the sprites
	private void drawGame(Graphics g)
	{	//Calculate offsets relative to player 1
		int offset_x = 0; //Main.ScreenWidth/2 - (int)player1.getXCenter();
		int offset_y = 0; //Main.ScreenHeight/2 - (int)player1.getYCenter();
		//Draw the sprites
		Sprite s;
		for(int i=0; i<tangible_sprites.size(); i++)
		{
			s = (Sprite)tangible_sprites.get(i);
			s.draw(this, g, offset_x, offset_y);
		}
		player.draw(this, g, offset_x, offset_y);
		for(int i=0; i<intangibles.size(); i++)
		{
			s = (Sprite)intangibles.get(i);
			s.draw(this, g, offset_x, offset_y);
		}
	}

	//Takes the number of seconds that we would like 
	//to elapse between each game update.
	public void update(double elapsed_seconds)
	{
		//Use escape key to exit the game
		if(this.key_setter.ascii_input[27]) //27 escape key
		{
			System.exit(0);
		}
		//Use the "p" key to toggle pause
		if(this.key_setter.checkKeyReleased(80))
		{
			this.paused = !this.paused;
		}
		if(!paused && this.player.getLives() > 0)
		{
			if(bullet_time_on)
			{
				elapsed_seconds = elapsed_seconds/2;
			}
			score += elapsed_seconds;
			//Check collisions and update all sprites
			this.checkCollisions();
			this.updateSprites(elapsed_seconds);
			this.spawner.update(elapsed_seconds);
		}
		//Save high score then restart.
		if(this.player.getLives() <= 0 && this.key_setter.ascii_input[10]) //10 enter key
		{
			this.saveHighScore();
			this.reset();
		}
	}

	private void checkCollisions() 
	{	//This can be made more efficient with a sorted list.
		Collections.sort(tangible_sprites);
		//Check collisions between all sprites
		for (int i = 0; i < tangible_sprites.size()-1; i++) 
		{
			for (int j = i+1; j < tangible_sprites.size(); j++)
			{
				Sprite s1 = tangible_sprites.get(i);
				Sprite s2 = tangible_sprites.get(j);
				//Check the sprites' collision bitmasks then
				//check if the bitmasks are identical.
				//For now, same type objects do not collide.
				//Then check if the sprites collided.
				if ((s1.getBitmask() & s2.getBitmask())!=0 &&
						s1.getBitmask() != s2.getBitmask() &&
						s1.checkCollided(s2))
				{
					s1.handleCollision(s2);
					s2.handleCollision(s1);
				}
				//Stop comparison early if s1's rightmost edge
				//is left of s2's leftmost edge.
				//Since the list is sorted, no further collisions
				//are possible.
				if(s1.getX()+s1.width < s2.getX())
				{
					break;
				}
			}
		}
	}

	private void updateSprites(double elapsed_seconds)
	{
		Sprite s;
		//Loop backwards over sprites so that removal of sprites is safer.
		for(int i=tangible_sprites.size()-1; i>=0; i--)
		{
			s = (Sprite)tangible_sprites.get(i);
			//Decide to update or remove sprite
			if(s.getRemoveMe())
			{
				this.tangible_sprites.remove(i);
			}
			else
			{
				if(bullet_time_on && s instanceof Player)
				{
					bullet_time_countdown -= elapsed_seconds*2;
					if(bullet_time_countdown < 0)
					{
						bullet_time_on = false;
					}
					s.update(elapsed_seconds*2);
				}
				else
				{
					s.update(elapsed_seconds);
				}
			}
		}
		for(int i=intangibles.size()-1; i>=0; i--)
		{
			s = (Sprite)intangibles.get(i);
			//Decide to update or remove sprite
			if(s.getRemoveMe())
			{
				this.intangibles.remove(i);
			}
			else
			{
				s.update(elapsed_seconds);
			}
		}
	}
	
	public void boostScore(double amount)
	{
		this.score += amount;
	}
	
	public void setBulletTime()
	{
		bullet_time_on = true;
		bullet_time_countdown = bullet_time_reset;
	}
	
	public void setFPS(int fps)
	{
		this.fps = fps;
	}
	
	/* Pre: ArrayList<Sprite> tangible_sprites has been initialized. 
	 * Post: adds Sprite s to tangible_sprites.  */
	public void addTangible(Sprite s){ this.tangible_sprites.add(s); }
	public void addIntangible(Sprite s){ this.intangibles.add(s); }
	
	public Player getPlayer()
	{
		return this.player;
	}

	private void enterUserName(KeyEvent e)
	{
		if(e.getKeyCode() == 32 || (e.getKeyCode() > 36 && e.getKeyCode() < 41))
		{
			return;
		}
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
		{
			user_name = user_name.substring(0, user_name.length()-1);
		}
		else
		{
			user_name += e.getKeyChar();
		}
	}
	
	private void loadHighScores()
	{	//Fix the high score list length at MAX_HIGH_SCORES.
		high_score_list = new String[MAX_HIGH_SCORES][2];
		int score_index = 0;
		//http://www.mkyong.com/java/how-to-read-file-from-java-bufferedreader-example/
		BufferedReader br = null;
		try
		{
			String sCurrentLine;
			br = new BufferedReader(new FileReader("high_scores.txt"));
			while ((sCurrentLine = br.readLine()) != null)
			{
				//Error check
				if(score_index >= MAX_HIGH_SCORES){ break; }
				String[] temp = sCurrentLine.split(",");
				high_score_list[score_index][0] = temp[0];
				high_score_list[score_index][1] = temp[1];
				score_index++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		high_scores_loaded = true;
	}
	
	private void saveHighScore()
	{	//http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/
		try{
			File file = new File("high_scores.txt");
			// if file doesnt exists, then create it
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//Get the index of the new high score.
			int high_score_index = getNewScoreIndex();
			//Get the last index to write so we know when 
			//to stop using newlines.
			int last_index = 0;
			for(int i=high_score_list.length-1; i>=0; i--)
			{
				if(high_score_list[i][1] != null)
				{
					last_index = i;
					break;
				}
			}
			//Write all the scores to file.
			String content;
			for(int i=0; i<high_score_list.length; i++)
			{
				if(high_score_index == i)
				{	//then write the newest high score
					content = this.user_name+","+Double.toString(this.score);
					bw.write(content);
					if(i <= last_index){ bw.write("\n"); }
				}
				//If there are no more scores to save to file, break out
				if(high_score_list[i][0] == null){ break; }
				content = high_score_list[i][0]+","+high_score_list[i][1];
				bw.write(content);
				if(i < last_index){ bw.write("\n"); }
			}
			bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private int getNewScoreIndex()
	{
		double current_high_score = Double.MAX_VALUE;
		double temp_score;
		for(int i=0; i<high_score_list.length; i++)
		{	//If the latest high score is smaller than the 
			//current high score, but greater than the next high 
			//score, then we have the index.
			if(high_score_list[i][1] == null)
				{ temp_score = 0; }
			else
				{ temp_score = Double.valueOf(high_score_list[i][1]); }
			if(current_high_score > this.score &&
					this.score > temp_score)
			{
				return i;
			}
			current_high_score = temp_score;
		}
		return high_score_list.length;
	}

	private class TAdapter extends KeyAdapter 
	{
		@Override
		public void keyReleased(KeyEvent e) 
		{
			//System.out.println(e.getKeyCode());
			key_setter.keyReleased(e.getKeyCode());
			//When the game is over do this.
			if(player.getLives() <= 0)
			{
				enterUserName(e);
			}
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
			//Auto-generated method stub
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
			//Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			//Auto-generated method stub
		}
	}

}