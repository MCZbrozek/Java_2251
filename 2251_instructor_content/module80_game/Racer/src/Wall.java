public class Wall extends SpriteRectangular
{
	public Wall(int x, int y,
			int width, int height,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super (x, y,
				0,//angle
				"", //image
				i_am_bitmask, i_hit_bitmask);
		setWidth(width);
		setHeight(height);
	} //public Wall(int x, int y, int width, int height, int collision_bitmask)
	
	public void update(double elapsed_seconds)
	{
		//Do nothing
	} //public void update(Ship player1, ArrayList<Sprite> sprite_list, int index)

	public void handleCollision(SpritePhysics other)
	{	//Shove the other sprite out
		this.shoveOut(other);
	} //public void handleCollision(Sprite other)

}