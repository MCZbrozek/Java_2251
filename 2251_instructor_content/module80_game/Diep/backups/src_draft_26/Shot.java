import java.awt.Color;
import java.util.ArrayList;

public class Shot extends SpriteTopDown
{
	private Ship shooter;
	protected double timeout;

	private double explosion_distance = Constants.explosion_distance;
	private int explosion_damage = Constants.explosion_damage;
	private double explosion_impulse = Constants.explosion_impulse;

	//Variable to set whether or not shots explode
	private boolean does_explode = false;
	//Make sure to only explode once
	private boolean has_exploded = false;
	//Whether or not this is a ricochet shot
	private boolean ricochet = false;
	//Amount damage increases with each bounce of a ricochet shot.
	private int damage_increase=1;

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
		 boolean ricochet,
		 Board b)
	{
		super(0,0,
			health,
			angle,
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
		this.ricochet = ricochet;
		this.setVelocity(
				Math.cos(angle)*speed,
				Math.sin(angle)*speed);
		this.no_health_draw = true;
		this.no_direction_draw = true;
	}

	void countdown(double elapsed_seconds)
	{
		if(this.timeout > 0)
		{
			this.timeout -= elapsed_seconds;
		}
		else
		{
			this.setRemoveMeTrue();
		}
	}

	/* This method is intended to be called once per frame and will
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		this.countdown(elapsed_seconds);
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
		//bounce off
		if(this.ricochet)
		{
			this.bounceOff(other);
			//Increase damage
			this.setBodyDamage(this.body_damage+damage_increase);
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

	void setExplosionParameters(double explosion_distance,
								int explosion_damage,
								double explosion_impulse)
	{
		this.explosion_distance = explosion_distance;
		this.explosion_damage = explosion_damage;
		this.explosion_impulse = explosion_impulse;
	}

	private void explode()
	{
		has_exploded = true;
		ArrayList<SpritePhysics> hit_by_blast = board_reference.getAllWithinDistance(this.getXCenter(), this.getYCenter(), this.explosion_distance);
		//Push sprites away from center of the blast
		double angle;
		for (SpritePhysics s:hit_by_blast)
		{
			//Don't push away the shooter
			if(s!=this.shooter)
			{
				angle = this.angleToSprite(s);
				s.changeVelocity(Math.cos(angle)*this.explosion_impulse,
						Math.sin(angle)*this.explosion_impulse);
				s.takeDamage(this.explosion_damage, this);
			}
		}
		//Add an explosion
		this.board_reference.addSpriteIntangible(
				new Explosion(this.getXCenter(),
						this.getYCenter(),
						this.explosion_distance, //radius
						0.2)
		);
	}
}