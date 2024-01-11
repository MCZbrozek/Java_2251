//This object flies up to a location where the pickup is displayed
//on the HUD then vanishes with a flash.
public class PickupAnimation extends Sprite
{
	private double target_x;
	private double target_y;

	public PickupAnimation(double x, double y,
			double target_x, double target_y,
			String image_file)
	{
		super(x, y,
				0, //angle
				0, //radius
				image_file,
				0); //collision bitmask
		this.target_x = target_x;
		this.target_y = target_y;
		this.setImage(this.image_file, //String image_id
				0); //double angle
		moveToCenter(this.getX(), this.getY());
		//Do trig to get angle to destination
		double angle = this.angleToDestination(target_x, target_y);
		double speed = 1500;
		this.setVelocity(speed*Math.cos(angle),
				speed*Math.sin(angle));
	}

	@Override
	void update(double elapsed_seconds)
	{
		this.move(elapsed_seconds);
		if(this.distanceTo(target_x, target_y) < 20 || (!this.on_screen))
		{
			this.destroyMe();
		}
	}

	@Override
	void handleCollision(Sprite other)
	{
	}
}