import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/* Finish "line" for completing the race course or maze. */
public class Finish extends SpriteCircular
{
	private ArrayList<String> winners = new ArrayList<String>();
	
	public Finish(int x, int y,
			double radius,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super (x, y,
				0,//angle
				radius,//radius
				"", //image
				i_am_bitmask, i_hit_bitmask);
		moveToCenter(x, y);
	}
	
	@Override
	public void draw(Board b, Graphics g, int offset_x, int offset_y)
	{
		//Determine whether or not to draw the sprite
		if(this.isVisible())
		{
			Graphics2D g2d = (Graphics2D) g;
			//Update the on_screen status of this sprite.
			int draw_x = (int)(this.x + offset_x);
			int draw_y = (int)(this.y + offset_y);
			this.setIsOnScreen(draw_x, draw_y);
			if(this.getIsOnScreen())
			{   //Draw sprite as a circle.
				g2d.setColor(Color.GREEN);
				g2d.fillOval(draw_x,draw_y,
						(int)this.getCollisionRadius()*2,
						(int)this.getCollisionRadius()*2);
			}
		} //if(this.isVisible())

		//Display winners on the screen
		g.setFont(ExampleBoard.FONT);
		g.setColor(Color.GREEN);
		g.drawString("WINNERS:",
			(int)(Main.ScreenWidth*0.9),
			(int)(Main.ScreenHeight*0.1));
		for(int i=0; i<winners.size(); i++)
		{
			g.drawString(
				(i+1)+": "+winners.get(i),
				(int)(Main.ScreenWidth*0.9),
				(int)(Main.ScreenHeight*0.1)+20*(i+1)
				);
		}
	} //public void draw
	
	public void update(double elapsed_seconds)
	{
		//Do nothing
	} //public void update(Ship player1, ArrayList<Sprite> sprite_list, int index)

	public void handleCollision(SpritePhysics other)
	{
		//Grab the name of whoever completed the course.
		winners.add( ((Ship)other).getName() );
		//Then remove the sprite from the world.
		other.setRemoveMeTrue();
	} //public void handleCollision(Sprite other)

}