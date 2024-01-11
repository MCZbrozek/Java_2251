import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//This class is inherited from by Enemy and player
public abstract class Ship extends Sprite
{
	protected BufferedImage shield_image;
	protected int shield_level;
	//Assorted parameters
	private int health;
	private int max_health;
	//reference to Board so sprites can be added to it by
	//explosions and fired guns.
	protected Board board_reference;
	
	private double refire_rate = 0.2;
	protected double refire_countdown = 0;
	private int bullet_type = Pickup.RAPID_SHOT;

	private boolean invincible = false;
	private double invincibility_countdown = 0;
	private double invincibility_reset = 10;
	
	public Ship(double x, double y,
			double angle, //initial heading
			String image_file,
			int max_health,
			int shield_level,
			Board board_reference,
			int collision_bitmask)
	{
		super(x, y,
			angle,
			0, //collision_radius,
			image_file,
			collision_bitmask);
		this.health = max_health;
		this.max_health = max_health;
		this.board_reference = board_reference;
		this.setImage(image_file, this.getAngle());
		this.shield_level = shield_level;
		//The following if-else is a little hack to get 
		//the ship radius setup correctly.
		if(this.shield_level > 0)
		{
			this.shield_level--;
			this.changeShield(1);
		}
		else
		{
			this.shield_level++;
			this.changeShield(-1);			
		}
	}

	/* Override sprite's draw to draw the ship shield. */
	public void draw(Board b, Graphics g, int offset_x, int offset_y)
	{
		super.draw(b, g, offset_x, offset_y);
		//Determine whether or not to draw the sprite
		if(this.vis)
		{
			if(invincible)
			{
				//Add sparkle on the player
				this.board_reference.addIntangible(
						new Explosion(offset_x+this.getX()+Utils.random_generator.nextInt(this.width),
									  offset_y+this.getY()+Utils.random_generator.nextInt(this.height),
									  0, //angle
									  0.03, //timeout
									  "star1.png")
						);
			}
			if(shield_level>0)
			{
				//Calculate the location on the screen at which to draw
				//this sprite. Also update last_x and last_y.
				int draw_x = (int)(this.x + offset_x);
				int draw_y = (int)(this.y + offset_y);
				//Update the image orientation if needed
				if(this.last_draw_angle != this.getAngle())
				{
					shield_image = Main.image_manager.getImage("shield"+shield_level+".png", -this.getAngle());
				}
				//Draw this sprite's image
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(shield_image,
						draw_x - (shield_image.getWidth()-this.width)/2,
						draw_y - (shield_image.getHeight()-this.height)/2,
						b);
			}
		}
	}
	
	public void update(double elapsed_seconds)
	{
		//Countdown invincibility
		if(invincible)
		{
			invincibility_countdown -= elapsed_seconds;
			if(invincibility_countdown < 0)
			{
				this.setInvincibility(false);
			}
		}
		//Cooldown the guns
		refire_countdown -= elapsed_seconds;
	}

	public void fire(int missile_bitmask, double flight_angle)
	{
		if(refire_countdown <= 0)
		{
			switch(this.bullet_type)
			{
			case Pickup.PIERCING_SHOT:
				//just layer 5 bullets
				for(int i=0; i<5; i++)
				{
					this.board_reference.addTangible(
						new Missile("laserBlue16.png",
								this.getXCenter()+10*i,
								this.getYCenter(),
								this.getAngle(),
								500, //double speed,
								1, //int damage,
								this.board_reference,
								flight_angle, //flight angle
								missile_bitmask)
						);
					//Second argument is the volume reduction.
					Utils.playSound("audio/laser_blue.wav", 5.0f);
				}
				break;
			case Pickup.TRIPLE_SHOT:
				this.board_reference.addTangible(
						new Missile("laserGreen14.png",
								this.getXCenter(), this.getYCenter(),
								this.getAngle(),
								500, //double speed,
								1, //int damage,
								this.board_reference,
								flight_angle, //flight angle
								missile_bitmask)
						);
				this.board_reference.addTangible(
						new Missile("laserGreen14.png",
								this.getXCenter(), this.getYCenter(),
								this.getAngle(),
								500, //double speed,
								1, //int damage,
								this.board_reference,
								flight_angle-Math.PI/4, //flight angle
								missile_bitmask)
						);
				this.board_reference.addTangible(
						new Missile("laserGreen14.png",
								this.getXCenter(), this.getYCenter(),
								this.getAngle(),
								500, //double speed,
								1, //int damage,
								this.board_reference,
								flight_angle+Math.PI/4, //flight angle
								missile_bitmask)
						);
				//Second argument is the volume reduction.
				Utils.playSound("audio/laser_green.wav", 5.0f);
				break;
			case Pickup.RAPID_SHOT:
				this.board_reference.addTangible(
						new Missile("laserRed01.png",
								this.getXCenter(), this.getYCenter(),
								this.getAngle(),
								500, //double speed,
								1, //int damage,
								this.board_reference,
								flight_angle, //flight angle
								missile_bitmask)
						);
				//Second argument is the volume reduction.
				Utils.playSound("audio/laser_red.wav", 5.0f);
				break;
			case Pickup.TWIN_SHOT:
				this.board_reference.addTangible(
						new Missile("laserRed06.png",
								this.getXCenter(), this.getY(),
								this.getAngle(),
								500, //double speed,
								1, //int damage,
								this.board_reference,
								flight_angle, //flight angle
								missile_bitmask)
						);
				this.board_reference.addTangible(
						new Missile("laserRed06.png",
								this.getXCenter(), this.getY()+this.height,
								this.getAngle(),
								500, //double speed,
								1, //int damage,
								this.board_reference,
								flight_angle, //flight angle
								missile_bitmask)
						);
				//Second argument is the volume reduction.
				Utils.playSound("audio/laser_yellow.wav", 5.0f);
				break;
			}
			refire_countdown = refire_rate;
		}
	}

	protected void setRefireRate(double new_rate)
	{
		refire_rate = new_rate;
	}
	
	public void changeHealth(int change)
	{
		this.health += change;
		if(this.health > this.max_health)
		{
			this.health = this.max_health;
		}
		else if(this.health < 0)
		{
			this.health = 0;
		}
	}
	
	public int getHealth()
	{
		return this.health;
	}
	
	public double getPercentHealth()
	{
		return (double)this.health/this.max_health;
	}

	public void setInvincibility(boolean invincible)
	{
		this.invincible = invincible;
	}

	public void changeShield(int amount)
	{	int old_shield = this.shield_level;
		this.shield_level = Math.max(0, Math.min(this.shield_level+amount, 3));
		//If there was a change, update image and radius.
		if(old_shield != this.shield_level)
		{
			if(shield_level==0)
			{
				this.setCollisionRadius(34);
			}
			else
			{
				shield_image = Main.image_manager.getImage("shield"+shield_level+".png", -this.getAngle());
				//Enlarge the collision radius
				this.setCollisionRadius(62);
			}
		}
	}
	
	public boolean isInvincible()
	{
		return invincible;
	}

	public double getInvincibilityCountdown()
	{
		return invincibility_countdown;
	}
	
	public void setInvincibility()
	{
		invincible = true;
		invincibility_countdown = invincibility_reset;
	}
	
	public void setBullet(int type)
	{
		switch(type)
		{
		case Pickup.PIERCING_SHOT:
			refire_rate = 0.5;
			break;
		case Pickup.TRIPLE_SHOT:
			refire_rate = 0.4;
			break;
		case Pickup.RAPID_SHOT:
			refire_rate = 0.2;
			break;
		case Pickup.TWIN_SHOT:
			refire_rate = 0.3;
			break;
		}
		this.bullet_type = type;
	}
	
	/* Handle a collision between this sprite and other. */
	public void handleCollision(Sprite other)
	{
		if(! (other instanceof Pickup) && !invincible)
		{
			//Lose shield or health.
			if(shield_level > 0)
			{
				this.changeShield(-1);
			}
			else
			{
				this.changeHealth(-1);
			}
		}
	}
}