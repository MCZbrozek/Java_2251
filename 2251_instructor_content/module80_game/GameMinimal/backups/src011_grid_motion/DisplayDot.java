import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/* This class displays a dot on the screen.
This was initially unused and then I mucked around with it to use it as an indicator that the player had "walked" over a specific area. So now, this object is created in ExampleBoardClearSpace.java */
public class DisplayDot extends SpriteCircular
{
	private double radius;
	//How long to draw the dot in seconds before removing it from the screen
	private double draw_timeout;
	private Color color;
	//If set to true, the dot only starts to timeout
	//when collided with by another object.
	private boolean countdownOnCollide = false;

	public DisplayDot(double xcenter, double ycenter,
					  double draw_countdown,
					  double radius,
					  Color c,
					  boolean countdownOnCollide,
					  int i_am_bitmask,
					  int i_hit_bitmask)
	{
		this(xcenter, ycenter, draw_countdown, radius, c);
		this.countdownOnCollide = countdownOnCollide;
		this.setIamBitmask(i_am_bitmask);
		this.setIhitBitmask(i_hit_bitmask);
	}
	public DisplayDot(double xcenter, double ycenter,
					  double draw_countdown,
					  double radius,
					  Color c)
	{
		super(xcenter, ycenter,
				0, // initial heading
				radius, // collision_radius
				"", //image_file
				0, //i_am_bitmask
				0); //i_hit_bitmask
		this.draw_timeout = draw_countdown;
		this.radius = radius;
		this.color = c;
	}

	/* Override sprite's draw method. */
	@Override
	public void draw(Board b, Graphics g,
					 int offset_x, int offset_y)
	{
		//Determine whether or not to draw the sprite
		if(this.vis)
		{	//Calculate the location on the 
			//screen at which to draw this sprite.
			int draw_x = (int)(this.x + offset_x);
			int draw_y = (int)(this.y + offset_y);
			Graphics2D g2d = (Graphics2D) g;
			//Update the on_screen status of this sprite.
			this.setIsOnScreen(draw_x, draw_y);
			if(this.on_screen)
			{
				g2d.setColor(this.color);
				g2d.fillOval(draw_x,draw_y,
					(int)(radius*2),
					(int)(radius*2));
			}
		}
	}

	public void update(double elapsed_seconds)
	{
		//Countdown to removal from the screen
		if(!countdownOnCollide)
		{
			draw_timeout -= elapsed_seconds;
			if(draw_timeout < 0)
			{
				this.setRemoveMeTrue();
			}
		}
	} //public void update(Craft craft, ArrayList<Sprite> sprite_list, int index)

	public void handleCollision(SpritePhysics other)
	{
		if(countdownOnCollide)
		{
			countdownOnCollide = false;
		}
	}
}