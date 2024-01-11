public class ExampleWall extends SpriteRectangular
{
	public ExampleWall(int x, int y,
			int width, int height)
	{
		super (x, y,
				0,//angle
				"images/wall.jpg");
		//Use custom dimensions, not image dimensions.
		this.setImage("images/wall.jpg", 
				0, //angle
				width, height);
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