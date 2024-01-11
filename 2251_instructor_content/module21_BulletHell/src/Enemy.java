
public class Enemy extends Ship
{
	private boolean moves_vertically = false;
	//Variables for random motion
	private boolean headed_down = true; //Positive dy
	private int height_threshold;
	private double speed;

	public Enemy(double x, double y,
			Board b,
			String image_file,
			boolean moves_vertically,
			int health,
			int shield_level)
	{
		super(x, y, 
			Math.PI/2, //angle,
			image_file,
			health,
			shield_level,
			b,
			Board.enemy_bitmask);
		this.setBullet(Pickup.RAPID_SHOT);
		this.setRefireRate(0.5);
		this.speed = 250;
		this.setVelocity(-speed, 0);
		if(moves_vertically)
		{
			this.setVelocity(-speed, speed);
			this.height_threshold = Utils.random_generator.nextInt(Math.max(1, Main.ScreenHeight-this.height));
			if(this.y > this.height_threshold)
			{
				this.setY(this.height_threshold-200);
			}
		}
		this.moves_vertically = moves_vertically;
	}

	public void update(double elapsed_seconds)
	{	super.update(elapsed_seconds);
		//change direction
		if(this.moves_vertically)
		{
			if(headed_down && this.y > height_threshold)
			{
				headed_down = !headed_down;
				this.setVelocity(-speed, -speed);
				height_threshold = Utils.random_generator.nextInt((int)this.y);
			}
			else if(!headed_down && this.y < height_threshold)
			{
				headed_down = !headed_down;
				this.setVelocity(-speed, speed);
				height_threshold = (int)this.y + Utils.random_generator.nextInt(Main.ScreenHeight - (int)(this.y+this.height+1));
			}
		}
		this.move(elapsed_seconds);
		if(this.x+this.width < 0)
		{
			this.destroyMe();
		}
		this.fire(Board.enemy_bullet_bitmask, Math.PI);
	}

	public void handleCollision(Sprite other)
	{
		super.handleCollision(other);
		if(this.getHealth() <= 0)
		{
			this.destroyMe();
			//Second argument is the volume reduction.
			Utils.playSound("audio/explosion.wav", 5.0f);
			//Display explosion
			this.board_reference.addIntangible(
					new Explosion(this.getXCenter(),
								  this.getYCenter(),
								  Utils.random_generator.nextDouble()*2*Math.PI, //angle
								  0.03, //timeout
								  "0"+Integer.toString(1+Utils.random_generator.nextInt(2))+"firey_explosion", //get 1 of 3 random explosions
								  12)
					);
		}
	}

}