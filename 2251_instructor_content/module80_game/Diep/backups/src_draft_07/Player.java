import java.awt.Color;

public class Player extends Ship
{
	private KeyManager key_setter;
	private boolean autofire = false;

	Player(double x, double y,
				  double collision_radius,
				  KeyManager key_setter,
				  int i_am_bitmask,
				  int i_hit_bitmask,
				  Board board)
	{
		super(x, y,
				"Player",
				collision_radius,
				i_am_bitmask,
				i_hit_bitmask,
				BoardTopDown.bullet_bitmask,//I am
				BoardTopDown.everything_bitmask,//I hit
				board);
		this.color = Color.BLUE;
		this.key_setter = key_setter;
	}

	/* This method is intended to be called once per frame and will
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		//Orient toward mouse
		this.setAngle(this.angleToDestination(
				key_setter.getMouseX()+this.getXCenter()-Main.ScreenWidth/2,
				key_setter.getMouseY()+this.getYCenter()-Main.ScreenHeight/2));
		//Impulse in any direction indicated by arrow keys
		if(key_setter.ascii_input[38] || key_setter.ascii_input[87])//up key 38 or w key 87
		{	//up
			this.changeVelocity(0, -speed*elapsed_seconds);
		}
		if(key_setter.ascii_input[40] || key_setter.ascii_input[83])//down key 40 or s key 83
		{	//down
			this.changeVelocity(0, speed*elapsed_seconds);
		}
		if(key_setter.ascii_input[37] || key_setter.ascii_input[65])//left key 37 or a key 65
		{	//left
			this.changeVelocity(-speed*elapsed_seconds, 0);
		}
		if(key_setter.ascii_input[39] || key_setter.ascii_input[68])//right key 39 or d key 68
		{	//right
			this.changeVelocity(speed*elapsed_seconds, 0);
		}
		//Apply friction, move, and stay in bounds.
		super.update(elapsed_seconds);

		if(this.autofire || this.triple_shot_count>0){this.attack();}
	}

	void toggleAutofire()
	{
		this.autofire = !this.autofire;
	}
}