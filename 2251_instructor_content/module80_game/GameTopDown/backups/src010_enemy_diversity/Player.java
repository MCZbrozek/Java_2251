import java.awt.Color;

public class Player extends SpriteTopDown
{
	private KeyManager key_setter;
	private Board board_reference;
	private double hit_cooldown=0;
	private double shoot_cooldown=0;
	private double bomb_cooldown=0;

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
			speed = 200;
		}else if(key_setter.ascii_input[40] || key_setter.ascii_input[83]){//down key 40 or s key 83
			speed = -200;
		}
		//Set dx and dy
		this.setVelocity(
				Math.cos(this.getAngle())*speed,
				Math.sin(this.getAngle())*speed);
		//Move
		this.move(elapsed_seconds);

		//Strafe
		if(key_setter.ascii_input[37] || key_setter.ascii_input[65]){//left key 37 or a key 65
			speed = 200;
			this.setVelocity(
					Math.cos(this.getAngle()-Math.PI/2)*speed,
					Math.sin(this.getAngle()-Math.PI/2)*speed);
		}else if(key_setter.ascii_input[39] || key_setter.ascii_input[68]){//right key 39 or d key 68
			speed = 200;
			this.setVelocity(
					Math.cos(this.getAngle()+Math.PI/2)*speed,
					Math.sin(this.getAngle()+Math.PI/2)*speed);
		}
		//Move
		this.move(elapsed_seconds);

		//Shoot, hit, or bomb
		hit_cooldown -= elapsed_seconds;
		shoot_cooldown -= elapsed_seconds;
		bomb_cooldown -= elapsed_seconds;
		if(key_setter.ascii_input[49] && hit_cooldown<0){//1 key
			//hit
			Shot temp_shot = new Shot(this.getXCenter()+Math.cos(this.getAngle())*40,
					this.getYCenter()+Math.sin(this.getAngle())*40,
					5,//collision_radius
					BoardTopDown.player_bullet_bitmask,//i_am_bitmask
					BoardTopDown.enemy_bitmask,//i_hit_bitmask
					this,//shooter
					0.1,//timeout
					1,//damage
					false,//knockback
					0);//speed
			board_reference.addSprite(temp_shot);
			hit_cooldown = 0.1;
		}else if(key_setter.ascii_input[50] && shoot_cooldown<0){//2 key
			//shoot
			Shot temp_shot = new Shot(this.getXCenter()+Math.cos(this.getAngle())*40,
							this.getYCenter()+Math.sin(this.getAngle())*40,
							5,//collision_radius
							BoardTopDown.player_bullet_bitmask,//i_am_bitmask
							BoardTopDown.enemy_bitmask,//i_hit_bitmask
							this,//shooter
							2.5,//timeout
							1,//damage
							false,//knockback
							500);//speed
			board_reference.addSprite(temp_shot);
			shoot_cooldown = 0.2;
		}else if(key_setter.ascii_input[51] && bomb_cooldown<0){//3 key
			//shoot
			Shot temp_shot = new Shot(this.getXCenter(),
							this.getYCenter(),
							100,//collision_radius
							BoardTopDown.player_bullet_bitmask,//i_am_bitmask
							BoardTopDown.enemy_bitmask,//i_hit_bitmask
							this,//shooter
							0.1,//timeout
							1,//damage
							true,//knockback
							0);//speed
			board_reference.addSprite(temp_shot);
			bomb_cooldown = 3.0;
		}
	}
		
	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{

	}
}