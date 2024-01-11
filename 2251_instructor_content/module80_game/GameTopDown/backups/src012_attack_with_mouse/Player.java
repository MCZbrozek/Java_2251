import java.awt.Color;

public class Player extends SpriteTopDown
{
	private KeyManager key_setter;
	private Board board_reference;
	private Attack[] attacks = new Attack[3];
	//Bind the attacks to the keys 1, 2, 3
	private int[] key_bindings = {49, 50, 51};

	//Change currently selected attack with this index into attacks array.
	private int current_attack = 0;

	private double speed;

	public Player(double x, double y,
				  int health,
				  double speed,
				  double collision_radius,
				  KeyManager key_setter,
				  int i_am_bitmask,
				  int i_hit_bitmask,
				  Board board)
	{
		super(x, y,
			health,
			0, //initial heading
			collision_radius,
			"",//image file
			i_am_bitmask,
			i_hit_bitmask);
		this.speed = speed;
		this.color = Color.BLUE;
		this.key_setter = key_setter;
		board_reference = board;
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		//Orient toward mouse
		this.setAngle(this.angleToDestination(key_setter.getMouseX(), key_setter.getMouseY()));
		//Set speed
		double speed = 0;
		if(key_setter.ascii_input[38] || key_setter.ascii_input[87]){//up key 38 or w key 87
			speed = this.speed;
		}else if(key_setter.ascii_input[40] || key_setter.ascii_input[83]){//down key 40 or s key 83
			speed = -this.speed;
		}
		//Set dx and dy
		this.setVelocity(
				Math.cos(this.getAngle())*speed,
				Math.sin(this.getAngle())*speed);
		//Move
		this.move(elapsed_seconds);

		//Strafe
		if(key_setter.ascii_input[37] || key_setter.ascii_input[65]){//left key 37 or a key 65
			this.setVelocity(
					Math.cos(this.getAngle()-Math.PI/2)*this.speed,
					Math.sin(this.getAngle()-Math.PI/2)*this.speed);
		}else if(key_setter.ascii_input[39] || key_setter.ascii_input[68]){//right key 39 or d key 68
			this.setVelocity(
					Math.cos(this.getAngle()+Math.PI/2)*this.speed,
					Math.sin(this.getAngle()+Math.PI/2)*this.speed);
		}
		//Move
		this.move(elapsed_seconds);

		//Cooldown all the attacks
		for (Attack attack : this.attacks)
		{
			attack.update(elapsed_seconds);
		}
		//Set attack based on keypress
		for (int i = 0; i < this.key_bindings.length; i++)
		{
			if(key_setter.ascii_input[this.key_bindings[i]])
			{
				current_attack = i;
			}
		}
	}

	//Activate currently selected attack if it has cooled down.
	public void castAttack()
	{
		this.attacks[current_attack].cast(this);
	}
		
	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other){}

	//Bind given attack to index of a keyboard key.
	public void bindAttack(Attack a, int index)
	{
		a.setBitmasks(
				BoardTopDown.player_bullet_bitmask,//i_am_bitmask
				BoardTopDown.enemy_bitmask);//i_hit_bitmask
		this.attacks[index] = a;
	}
}