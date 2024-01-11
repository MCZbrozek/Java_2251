//THIS FILE WAS COPIED OVER FROM GAMEMINIMAL ON 4/1/2023

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class GameFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private Board board;

	public GameFrame(Board b)
	{
		//These are used to adjust the game frame inward from the corner of the monitor.
		//Useful for certain monitor dimensions.
        int screenChangeXBy = 0;
        int screenChangeYBy = 0;
		this.board = b;
		add(this.board);
        //Add the minimize, maximize, and close buttons
		setUndecorated(false);
        setLocation(screenChangeXBy, screenChangeYBy);
        setVisible(true);
        setSize(Main.ScreenWidth, Main.ScreenHeight);
		//Don't allow the user to change the screen size.
		setResizable(false);
		setTitle("My Java Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        toFront();		
		//This is a finnicky little annoying method.
		//It has to be called after the frame is visible and board has been
		//added and other stuff.
		this.createBufferStrategy(2);
	} //public GameFrame()

	/* Calls render with Graphics2D context and takes care 
	 * of double buffering.
	 * I took this from here:
	 * http://www.java-gaming.org/index.php?topic=24220.0
	 */
	public void internalUpdateGraphicsInterpolated(int fps, float interpolation)
	{
		this.board.setFPS(fps);
		BufferStrategy bf = this.getBufferStrategy();
		Graphics2D g = null;
		try 
		{
			g = (Graphics2D) bf.getDrawGraphics();
			g.setBackground(Color.BLACK);
			g.clearRect(0, 0, getWidth(), getHeight());
			this.board.paintComponent(g);
		} 
		finally 
		{
			g.dispose();
		}
		// Shows the contents of the backbuffer on the screen.
		bf.show();
		//Tell the System to do the Drawing now, otherwise it can take a few extra ms until 
		//Drawing is done which looks very jerky
		Toolkit.getDefaultToolkit().sync();
	} //public void internalUpdateGraphicsInterpolated(int fps, float interpolation)
	
	public void GameUpdate(double elapsed_seconds)
	{
		this.board.update(elapsed_seconds);
	}

	public Board getBoard(){ return board; }
}