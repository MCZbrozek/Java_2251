import java.awt.Color;

public class Shot extends SpriteTopDown
{
	private Ship shooter;
	private double timeout;

	Shot(int i_am_bitmask,
		 int i_hit_bitmask,
		 Ship shooter,
		 double timeout,
		 int damage,
		 double speed,
		 int health,
		 Board b)
	{
		super(0,0,
			health,
			0,
			6,
			0,
			Constants.SHAPE_CIRCLE,
			i_am_bitmask,
			i_hit_bitmask,
				b);
		this.color = Color.RED;
		this.moveToCenter(
				shooter.getXCenter() + Math.cos(shooter.getAngle()),
				shooter.getYCenter() + Math.sin(shooter.getAngle()));
		this.shooter = shooter;
		this.timeout = timeout;
		this.setBodyDamage(damage);

		this.setVelocity(
				Math.cos(shooter.getAngle())*speed,
				Math.sin(shooter.getAngle())*speed);
		this.no_health_draw = true;
		this.no_direction_draw = true;
	}

	/* This method is intended to be called once per frame and will
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		if(this.timeout > 0)
		{
			this.timeout -= elapsed_seconds;
		}
		else
		{
			this.setRemoveMeTrue();
		}
		//Move
		this.move(elapsed_seconds);
	}

	void giveXpToShooter(int xp_amount)
	{
		shooter.giveXP(xp_amount);
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{	//Prevent self-damage
		if(other==shooter){return;}
		//Impulse the opposite direction, damage other, and give experience if other died.
		super.handleCollision(other);
	}

	public boolean getRemoveMe()
	{
		return super.getRemoveMe() || this.getHealth() <= 0;
	}

	Ship getShooter(){return shooter;}
}