import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/* Example circular obstacle. */
public class ExampleRoundWall extends SpriteCircular
{
	public ExampleRoundWall(int x, int y,
			double radius)
	{
		super (x, y,
				0,//angle
				radius,//radius
				"images/wall.jpg");
	}
	
	public void draw(Board b, Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GRAY);
		int radius = (int)((SpriteCircular)this).getCollisionRadius();
		g2d.fillOval((int)(this.getX() + (this.getWidth()/2) - radius),
				(int)(this.getY() + (this.getHeight()/2) - radius),
				radius*2, radius*2);
	}
	
	public void update(double elapsed_seconds)
	{
		//Do nothing
	} //public void update(Ship player1, ArrayList<Sprite> sprite_list, int index)

	public void handleCollision(SpritePhysics other)
	{	//Shove the other sprite out
		this.shoveOut(other);
	} //public void handleCollision(Sprite other)

}