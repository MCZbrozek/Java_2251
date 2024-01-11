
public class Pickup extends Sprite
{	//Pickup images
	public static final String[] PICKUP_IMAGES = {
		"powerupBlue_bolt.png",
		"playerLife1_blue.png",
		"pill_blue.png",
		"pill_green.png",
		"pill_red.png",
		"pill_yellow.png",
		"cursor.png",
		"numeral9.png",
		"bolt_gold.png",
		"star_silver.png",
		"shield_silver.png"
	};
	//Types of pickups
	public static final int BOLT = 0;
	public static final int LIFE = 1;
	public static final int PIERCING_SHOT = 2;
	public static final int TRIPLE_SHOT = 3;
	public static final int RAPID_SHOT = 4;
	public static final int TWIN_SHOT = 5;
	public static final int BULLET_TIME = 6;
	public static final int SCORE_BOOST = 7;
	public static final int ENERGY_MAX = 8;
	public static final int INVINCIBILITY = 9;
	public static final int SHIELD = 10;
	
	//Variables for random motion
	private boolean direction = true;
	private int height_threshold = Utils.random_generator.nextInt(Main.ScreenHeight);
	private double speed;
	
	private Board board_reference;
	
	//Get the image associated with the given pickup type
	public static String getPickupImage(int pickup_type)
	{
		return PICKUP_IMAGES[pickup_type];
	}
	
	//Return a pickup object specified by pickup_type
	public static Pickup getPickup(
			int pickup_type,
			int x, int y,
			Board board_reference)
	{
		return new Pickup(x, y,
				0, //angle,
				getPickupImage(pickup_type),
				200, //speed
				pickup_type,
				board_reference);
	}
	
	public static Pickup getRandomPickup(
			int x, int y,
			Board board_reference)
	{
		return Pickup.getPickup(
				Utils.random_generator.nextInt(SHIELD+1),
				x, y,
				board_reference);
	}

	private int pickup_type;
	
	private boolean took_damage = false;
	
	public Pickup(double x,
			double y,
			double angle,
			//double radius,
			String image_file,
			double speed,
			int type,
			Board board_reference)
	{
		super(x, y, 
				angle, 
				16, //radius, 
				image_file,
				Board.pickup_bitmask);
		this.speed = speed;
		this.pickup_type = type;
		this.board_reference = board_reference;
		this.setVelocity(-speed, speed);
		//load the image
		//String image_id, double angle, int custom_width, int custom_height, boolean use_custom
		this.setImage(this.image_file, 
				angle); //angle
	}

	@Override
	void update(double elapsed_seconds)
	{	//Uncomment this to make pickups respond to gravity
		this.move(elapsed_seconds);
		
		//change direction
		if(direction && this.y > height_threshold)
		{
			direction = !direction;
			this.setVelocity(-speed, -speed);
			height_threshold = Utils.random_generator.nextInt((int)this.y);
		}
		else if(!direction && this.y < height_threshold)
		{
			direction = !direction;
			this.setVelocity(-speed, speed);
			height_threshold = (int)this.y + Utils.random_generator.nextInt(Math.max(1, Main.ScreenHeight - (int)(this.y+this.height)));
		}
		
		if(this.x+this.width < 0)
		{
			this.destroyMe();
		}
		//There was weirdness with the previous implementation.
		//I think putting this code in update fixes it.
		//Depending on the order of the collision the pickup may die to a bullet or not.
		//If the bullet handles its collision first, then it will deal damage to the pickup
		//then the pickup will detect the collision and die.
		//If the pickup handles its collision first, then the pickup will ignore the collision
		//and then take damage, which it will ignore until a subsequent collision at which
		//point the pickup will puff smoke.
		//The issue is that beams collide with lots of things without dealing damage to them
		//so we can't just kill the pikcup on any old collision.
		if(took_damage)
		{	//Remove self
			this.destroyMe();
		}
	}

	@Override
	void handleCollision(Sprite other)
	{
		//Apply the pickup to the ship.
		switch(this.pickup_type)
		{
		case BOLT:
			((Player)other).changeEnergy(300000000);
			//Create pickup animation
			this.board_reference.addIntangible(
					new PickupAnimation(
							this.getXCenter(),
							this.getYCenter(),
							this.board_reference.ENERGY_X,
							this.board_reference.ENERGY_Y,
							this.image_file)
					);
			break;
		case LIFE: //Gain a life
			((Player)other).changeLives(1);
			//Create pickup animation
			this.board_reference.addIntangible(
					new PickupAnimation(
							this.getXCenter(),
							this.getYCenter(),
							this.board_reference.LIFE_X,
							this.board_reference.LIFE_Y,
							this.image_file)
					);
			//Second argument is the volume reduction.
			Utils.playSound("audio/life_pickup.wav", 5.0f);
			break;
		case PIERCING_SHOT:
			((Player)other).setBullet(PIERCING_SHOT);
			break;
		case TRIPLE_SHOT:
			((Player)other).setBullet(TRIPLE_SHOT);
			break;
		case RAPID_SHOT:
			((Player)other).setBullet(RAPID_SHOT);
			break;
		case TWIN_SHOT:
			((Player)other).setBullet(TWIN_SHOT);
			break;
		case BULLET_TIME:
			this.board_reference.setBulletTime();
			break;
		case SCORE_BOOST:
			this.board_reference.boostScore(9.0);
			//Create pickup animation
			this.board_reference.addIntangible(
					new PickupAnimation(
							this.getXCenter(),
							this.getYCenter(),
							this.board_reference.LIFE_X,
							this.board_reference.LIFE_Y,
							this.image_file)
					);
			break;
		case ENERGY_MAX:
			((Player)other).changeEnergy(300000000);
			((Player)other).boostMaxEnergy();
			//Create pickup animation
			this.board_reference.addIntangible(
					new PickupAnimation(
							this.getXCenter(),
							this.getYCenter(),
							this.board_reference.ENERGY_X+150,
							this.board_reference.ENERGY_Y,
							this.image_file)
					);
			break;
		case INVINCIBILITY:
			((Player)other).setInvincibility();
			break;
		case SHIELD:
			((Player)other).changeShield(1);
			break;
		}
		//Remove self
		this.destroyMe();
	}

	//Override
	public void takeDamage(int damage, Sprite responsible_sprite)
	{
		took_damage = true;
	}	
}