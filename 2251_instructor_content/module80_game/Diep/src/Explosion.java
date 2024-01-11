import java.awt.*;

public class Explosion extends SpriteCircular
{
	private double timeout; //seconds before vanishing
	private double current_radius=0;
	private double yellow_radius=0;
	private double red_radius=0;
	private double step_size=0;
	private double step_size_yellow=0;
	private double step_size_red=0;
	
    Explosion(double x, double y,
    		double radius,
    		double timeout)
    {
        super(x, y, 0,
        		radius,
        		"",
        		0, //I am Collision bitmask
        		0); //I hit Collision bitmask
        this.timeout = timeout;
        this.moveToCenter(x, y);
        this.setVisible(true);
		this.step_size = radius/timeout;
		this.step_size_yellow = (2.0/3.0)*radius/timeout;
		this.step_size_red = (1.0/3.0)*radius/timeout;
    }

    public void update(double elapsed_seconds)
    {
		this.timeout -= elapsed_seconds;
		this.current_radius += this.step_size*elapsed_seconds;
		this.yellow_radius += this.step_size_yellow*elapsed_seconds;
		this.red_radius += this.step_size_red*elapsed_seconds;
    	if (this.timeout < 0)
    	{
			this.setRemoveMeTrue();
    	}
    }

	/**
	 * Override parent class's draw.
	 */
	public void draw(Board b, Graphics g, int offset_x, int offset_y)
	{
		//Determine whether or not to draw the sprite
		if (this.isVisible())
		{
			int draw_x = (int)(this.getXCenter()-current_radius+offset_x);
			int draw_y = (int)(this.getYCenter()-current_radius+offset_y);
			Graphics2D g2d = (Graphics2D) g;
			//Update the on_screen status of this sprite.
			this.setIsOnScreen(draw_x, draw_y);
			if (this.getIsOnScreen())
			{   //Draw sprite
				g2d.setColor(Color.WHITE);
				g2d.fillOval(draw_x, draw_y, (int)(current_radius*2), (int)(current_radius*2));
				draw_x = (int)(this.getXCenter()-yellow_radius+offset_x);
				draw_y = (int)(this.getYCenter()-yellow_radius+offset_y);
				g2d.setColor(Color.YELLOW);
				g2d.fillOval(draw_x, draw_y, (int)(yellow_radius*2), (int)(yellow_radius*2));
				draw_x = (int)(this.getXCenter()-red_radius+offset_x);
				draw_y = (int)(this.getYCenter()-red_radius+offset_y);
				g2d.setColor(Color.RED);
				g2d.fillOval(draw_x, draw_y, (int)(red_radius*2), (int)(red_radius*2));
			}
		}
	}

	@Override
	public void handleCollision(SpritePhysics spritePhysics){}

	@Override
	public boolean checkCollided(SpritePhysics spritePhysics){return false;}

	@Override
	public void shoveOut(SpritePhysics spritePhysics){}
}