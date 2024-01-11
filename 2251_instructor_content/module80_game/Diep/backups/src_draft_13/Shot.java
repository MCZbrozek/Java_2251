import java.awt.Color;
import java.util.ArrayList;

public class Shot extends SpriteTopDown
{
	private Ship shooter;
	private double timeout;

	//Variable to set whether or not shots explode
	private boolean does_explode = false;
	//Make sure to only explode once
	private boolean has_exploded = false;

	Shot(double x, double y,
		 int i_am_bitmask,
		 int i_hit_bitmask,
		 Ship shooter,
		 double angle,
		 double timeout,
		 int damage,
		 double speed,
		 int health,
		 int radius,
		 boolean does_explode,
		 Board b)
	{
		super(0,0,
			health,
			0,
			radius,
			0,
			Constants.SHAPE_CIRCLE,
			i_am_bitmask,
			i_hit_bitmask,
				b);
		this.color = Color.RED;
		this.moveToCenter(x,y);
		this.shooter = shooter;
		this.timeout = timeout;
		this.setBodyDamage(damage);
		this.does_explode = does_explode;
		this.setVelocity(
				Math.cos(angle)*speed,
				Math.sin(angle)*speed);
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
	{	//Prevent self-damage and damage from bullets fired by same shooter
		if(other==shooter || (other instanceof Shot && ((Shot)other).getShooter()==shooter))
		{
			return;
		}
		//Impulse the opposite direction, damage other, and give experience if other died.
		super.handleCollision(other);
	}

	public boolean getRemoveMe()
	{
		return super.getRemoveMe() || this.getHealth() <= 0;
	}

	Ship getShooter(){return shooter;}

	/** Override so we can explode when the bullet dies. */
	@Override
	public void takeDamage(int damage, Sprite damage_dealer)
	{
		super.takeDamage(damage, damage_dealer);
		//If shot is dead, explode
		if(this.getHealth() <= 0 && does_explode && !has_exploded)
		{
			this.explode();
		}
	}

	/** Override so we can explode when the bullet dies. */
	@Override
	public void setRemoveMeTrue()
	{
		super.setRemoveMeTrue();
		if(does_explode && !has_exploded)
		{
			this.explode();
		}
	}

	private void explode()
	{
		has_exploded = true;
		ArrayList<SpritePhysics> hit_by_blast = board_reference.getAllWithinDistance(this.getXCenter(), this.getYCenter(), Constants.explosion_distance);
		//Push sprites away from center of the blast
		double angle;
		for (SpritePhysics s:hit_by_blast)
		{
			angle = this.angleToSprite(s);
			s.changeVelocity(Math.cos(angle)*Constants.explosion_impulse,
					Math.sin(angle)*Constants.explosion_impulse);
			s.takeDamage(Constants.explosion_damage, this.shooter);
		}
		//Add an explosion
		this.board_reference.addSpriteIntangible(
				new Explosion(this.getXCenter(),
						this.getYCenter(),
						0, //angle
						0.1, //timeout
						"images/red_hot_cross.png")
		);
	}
}