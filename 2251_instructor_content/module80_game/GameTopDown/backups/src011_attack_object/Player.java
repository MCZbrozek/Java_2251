import java.awt.Color;

public class Player extends SpriteTopDown
{
	private KeyManager key_setter;
	private Board board_reference;
	private Attack[] attacks = new Attack[3];
	//Bind the attacks to the keys 1, 2, 3
	private int[] key_bindings = {49, 50, 51};

	public Player(double x, double y, 
			double collision_radius, 
			KeyManager key_setter,
			int i_am_bitmask,
			int i_hit_bitmask,
			Board board)
	{
		super(x, y,
			10,
			0, //initial heading
			collision_radius,
			"",//image file
			i_am_bitmask,
			i_hit_bitmask);
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
			speed = 100;
		}else if(key_setter.ascii_input[40] || key_setter.ascii_input[83]){//down key 40 or s key 83
			speed = -100;
		}
		//Set dx and dy
		this.setVelocity(
				Math.cos(this.getAngle())*speed,
				Math.sin(this.getAngle())*speed);
		//Move
		this.move(elapsed_seconds);

		//Strafe
		if(key_setter.ascii_input[37] || key_setter.ascii_input[65]){//left key 37 or a key 65
			speed = 100;
			this.setVelocity(
					Math.cos(this.getAngle()-Math.PI/2)*speed,
					Math.sin(this.getAngle()-Math.PI/2)*speed);
		}else if(key_setter.ascii_input[39] || key_setter.ascii_input[68]){//right key 39 or d key 68
			speed = 100;
			this.setVelocity(
					Math.cos(this.getAngle()+Math.PI/2)*speed,
					Math.sin(this.getAngle()+Math.PI/2)*speed);
		}
		//Move
		this.move(elapsed_seconds);

		//Cooldown the attacks
		for (Attack attack : this.attacks)
		{
			attack.update(elapsed_seconds);
		}
		//Use attacks
		for (int i = 0; i < this.key_bindings.length; i++)
		{
			if(key_setter.ascii_input[this.key_bindings[i]])
			{
				this.attacks[i].cast(this);
			}
		}
	}
		
	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{

	}

	public void bindAttack(Attack a, int index)
	{
		a.setBitmasks(
				BoardTopDown.player_bullet_bitmask,//i_am_bitmask
				BoardTopDown.enemy_bitmask);//i_hit_bitmask
		this.attacks[index] = a;
	}
}