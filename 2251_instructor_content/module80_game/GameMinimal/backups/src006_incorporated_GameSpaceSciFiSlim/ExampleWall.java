public class ExampleWall extends SpriteRectangular
{
	public ExampleWall(int x, int y,
			int width, int height,
			int i_am_bitmask,
			int i_hit_bitmask)
	{
		super (x, y,
				0,//angle
				"images/wall.jpg",
				i_am_bitmask, i_hit_bitmask);
		//Use custom dimensions, not image dimensions.
		this.setImage("images/wall.jpg", 
				0, //angle
				width, height);
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