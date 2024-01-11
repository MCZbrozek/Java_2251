import java.awt.Color;

public class Pickup extends SpriteTopDown
{
	private Player player;
	private Board board;

	public Pickup(double x, double y,
				double collision_radius,
				Color color,
				Player p,
				Board b)
	{
		super(x, y,
			1, //health
			0, //initial heading.
			collision_radius,
			"",//image file
			1, //i_am_bitmask,
			2); //i_hit_bitmask);
		this.player = p;
		this.board = b;
		this.color = color;
		this.drawOrientation = false;
	}

	/** This method is intended to be called once per frame and will
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{

	}
	
	public boolean getRemoveMe() 
	{
		return super.getRemoveMe() || this.getHealth() <= 0;
	}

	/* Handle a collision between this sprite and other.
	* This prevents enemies from phasing through each other or the
	* player by shoving each other out of the same space.*/
	public void handleCollision(SpritePhysics other)
	{
		setHealth(0);
	}

}