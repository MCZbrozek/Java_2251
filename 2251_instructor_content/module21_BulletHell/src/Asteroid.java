
public class Asteroid extends Sprite
{
	private Board board_reference;
	private int size;
	
	public Asteroid(double x, double y,
			int size,
			int collision_radius,
			String image_file,
			Board board_reference)
	{
		super(x, y, 
			0, //angle,
			collision_radius, //collision_radius, 
			image_file,
			Board.asteroid_bitmask);
		this.board_reference = board_reference;
		this.size = size;
		//load the image
		//String image_id, double angle, int custom_width, int custom_height, boolean use_custom
		this.setImage(this.image_file, 
				0); //angle
		this.dx = -150;
		//Keep the asteroid on screen.
		if(y+height > Main.ScreenHeight)
		{
			this.setY(Main.ScreenHeight-height/2);
		}
		this.moveToCenter(x, y);
	}

	@Override
	void update(double elapsed_seconds)
	{
		this.move(elapsed_seconds);
		if(this.x+this.width < 0)
		{
			this.destroyMe();
		}
	}

	@Override
	void handleCollision(Sprite other)
	{
		this.destroyMe();
		//Add a smaller asteroid
		String size_text = "tiny";
		int collision_radius = 5;
		if(size==3)
		{
			size_text = "med";
			collision_radius = 22;
		}
		else if(size==2)
		{
			size_text = "small";
			collision_radius = 10;
		}
		if(size > 0)
		{
			this.board_reference.addTangible(
				new Asteroid(this.getXCenter(),
						this.getYCenter(),
						size-1,
						collision_radius,
						"meteorBrown_"+size_text+Integer.toString(Utils.random_generator.nextInt(2)+1)+".png",
						board_reference)
				);
		}
	}
	
}