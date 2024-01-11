
public class Player extends Ship
{	
	private int lives = 3;
	private double energy = 500000000;
	private double energy_max = 500000000;
	private int energy_decrease = -100000000;
	private KeyManager key_setter;
	
	public Player(double x, double y,
			double angle, //initial heading
			String image_file,
			Sprite target,
			int max_health,
			Board board_reference,
			KeyManager key_setter)
	{
		super(x, y,
			angle,
			image_file,
			max_health,
			3, //shield_level,
			board_reference,
			Board.player_bitmask);
		this.key_setter = key_setter;
	}

	/* This method is intended to be called once per frame and 
	 * will update this sprite. The arguments include a 
	 * reference to player 1's craft, the list of all sprites, 
	 * and this sprite's current index in the sprite list,
	 * which can be useful to more efficiently find other 
	 * nearby sprites. */
	public void update(double elapsed_seconds)
	{	super.update(elapsed_seconds);
		//Move the player
		double speed = 300;
		//Vertical motion
		if (key_setter.ascii_input[38] && this.energy>0) //38 up arrow key
		{ //up key
			this.y -= speed * elapsed_seconds;
			//Stay on screen
			if(this.y < 0){ this.y = 0; }
		}
		else if (key_setter.ascii_input[40] && this.energy>0) //40 down arrow key
		{ //down key
			this.y += speed * elapsed_seconds;
			//Stay on screen
			if(this.y+this.height >Main.ScreenHeight){ this.y = Main.ScreenHeight - this.height; }
		}
		//Horizontal motion
		if (key_setter.ascii_input[37] && this.energy>0) //37 left arrow key
		{ //left key
			this.x -= speed * elapsed_seconds;
			//Stay on screen
			if(this.x < 0){ this.x = 0; }
		}
		else if (key_setter.ascii_input[39] && this.energy>0) //39 right arrow key
		{ //right key
			this.x += speed * elapsed_seconds;
			//Stay on screen
			if(this.x+this.width > Main.ScreenWidth){ this.x = Main.ScreenWidth - this.width; }
		}
		//Shoot
		if(key_setter.ascii_input[32] && this.energy>0) //space bar
		{
			this.fire(Board.player_bullet_bitmask, 0);
		}
		//Spend energy
		this.changeEnergy(energy_decrease*elapsed_seconds);
	} //public void update(Craft craft, ArrayList<Sprite> sprite_list, int index)

	/* Handle a collision between this sprite and other. */
	public void handleCollision(Sprite other)
	{
		super.handleCollision(other);
		if(this.getHealth() == 0)
		{	//Reduce lives
			this.changeLives(-1);
			//Reset health
			this.changeHealth(1);
			//Second argument is the volume reduction.
			Utils.playSound("audio/player_hurt.wav", 5.0f);
		}
	}

	public void changeLives(int amount)
	{
		this.lives += amount;
		if(this.lives > 3)
		{
			this.lives = 3;
		}
		else if(this.lives < 0)
		{
			this.lives = 0;
		}		
	}
	
	public int getLives(){ return this.lives; }
	
	public void changeEnergy(double amount)
	{
		this.energy += amount;
		if(this.energy > this.energy_max)
		{
			this.energy = this.energy_max;
		}
		else if(this.energy < 0)
		{
			this.energy = 0;
		}		
	}
	
	public void boostMaxEnergy()
	{
		energy_max = Math.min(1000000000, energy_max+50000000);
	}
	
	public double getMaxEnergyPercent()
	{
		return energy_max / 1000000000.0;
	}

	public double getEnergyPercent()
	{
		return this.energy / this.energy_max;
	}

}