import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/* Example circular obstacle. */
public class ExampleRoundWall extends SpriteCircular
{
	public ExampleRoundWall(int x, int y,
			double radius,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super (x, y,
				0,//angle
				radius,//radius
				"images/wall.jpg",
				i_am_bitmask, i_hit_bitmask);
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