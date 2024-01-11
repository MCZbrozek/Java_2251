import java.awt.Color;
import java.util.ArrayList;

public abstract class NPC_Personality
{
	boolean debugging = false;
	private Board board_reference;
	String[] upgrade_sequence;
	int[] ship_upgrade_sequence;

	public NPC_Personality(Board board_reference)
	{
		this.board_reference = board_reference;
	}

	//Implement the behavior of this personality.
	abstract void update(double elapsed_seconds, Ship s);

	//Level up the given ship's attributes if possible
	void levelUp(Ship s)
	{
		if(s.availableLevelUpPoints()>0 && this.upgrade_sequence.length > s.spentLevelUpPoints())
		{
			String name = this.upgrade_sequence[s.spentLevelUpPoints()];
			s.levelUpAttribute(name);
		}
		else if(debugging && this.upgrade_sequence.length <= s.spentLevelUpPoints())
		{   //Error checking/debugging
			System.out.println(s.getName()+" tried to level up but has spent "+s.spentLevelUpPoints()+" level up points when the upgrade sequence is only "+this.upgrade_sequence.length+" long.");
			System.out.println("Upgrade sequence follows:");
			for (String s1 : this.upgrade_sequence)
			{
				System.out.println(s1);
			}
			System.out.println("\n");
		}
	}

	//Level up the given ship's ship type if possible
	void shipLevelUp(Ship s)
	{
		for (int i : this.ship_upgrade_sequence)
		{
			Constants.levelUpShip(s, i);
		}
	}

	/** Approach and shoot the target whether its a brick, player, powerup.
	 * Just like Ram Attack, but does not use gun recoil as thrust. */
	void shootAttack(double elapsed_seconds, SpritePhysics target, Ship s, String debug_info)
	{
		if(target==null)
			return;

		//Orient gun toward the target
		s.setAngle(s.angleToSprite(target));
		
		s.moveToward(elapsed_seconds, target.getXCenter(), target.getYCenter());
		
		s.attack();
		
		//Useful for debugging ship behaviors
		if(debugging)
		{
			s.strategy = "shoot: "+debug_info;
			board_reference.addSpriteIntangible(
					new DisplayLine(s.getXCenter(), s.getYCenter(),
							0.05,
							target.getXCenter(),
							target.getYCenter(),
							2,
							Color.YELLOW));
		}
	}

	/** Ram the target whether its a brick, player, powerup.
	 * Use gun recoil as thrust. */
	void ramAttack(double elapsed_seconds, SpritePhysics target, Ship s, String debug_info)
	{
		if(target==null)
			return;

		//Orient gun away from the target for extra recoil thrust!
		s.setAngle(target.angleToSprite(s));
		
		s.moveToward(elapsed_seconds, target.getXCenter(), target.getYCenter());
		
		//Useful for debugging ship behaviors
		if(debugging)
		{
			s.strategy = "ram: "+debug_info;
			board_reference.addSpriteIntangible(
					new DisplayLine(s.getXCenter(), s.getYCenter(),
							0.05,
							target.getXCenter(),
							target.getYCenter(),
							2,
							Color.YELLOW));
		}
	}

	/** Flee the target while shooting at it. */
	void fleeShoot(double elapsed_seconds, SpritePhysics target, Ship s, String debug_info)
	{
		if(target==null)
			return;

		//Orient gun toward the target
		s.setAngle(s.angleToSprite(target));
		
		s.moveAwayFrom(elapsed_seconds, target.getXCenter(), target.getYCenter());
		
		s.attack();
		
		//Useful for debugging ship behaviors
		if(debugging)
		{
			s.strategy = "flee: "+debug_info;
			board_reference.addSpriteIntangible(
					new DisplayLine(s.getXCenter(), s.getYCenter(),
							0.05,
							target.getXCenter(),
							target.getYCenter(),
							2,
							Color.YELLOW));
		}
	}

	/** Shoot first target, but move towards second target. */
	void shootMove(double elapsed_seconds, SpritePhysics target1, SpritePhysics target2, Ship s, String debug_info)
	{
		if(target1==null || target2==null)
			return;

		//Orient gun toward the target
		s.setAngle(s.angleToSprite(target1));
		s.moveToward(elapsed_seconds, target2.getXCenter(), target2.getYCenter());
		//Useful for debugging ship behaviors
		if(debugging)
		{
			s.strategy = "shoot move: "+debug_info;
			board_reference.addSpriteIntangible(
					new DisplayLine(s.getXCenter(), s.getYCenter(),
							0.05,
							target1.getXCenter(),
							target1.getYCenter(),
							2,
							Color.YELLOW));
			board_reference.addSpriteIntangible(
					new DisplayLine(s.getXCenter(), s.getYCenter(),
							0.05,
							target2.getXCenter(),
							target2.getYCenter(),
							2,
							Color.YELLOW));
		}
	}

	/**Get nearest xp brick with given xp level or more*/
	ExperienceBrick getNearestBigXp(Ship s, int xp_amount)
	{
		ArrayList<SpritePhysics> list = s.board_reference.getSpriteList();
		SpriteTopDown nearest_big_xp = null;
		double dist = Double.MAX_VALUE;
		for (SpritePhysics sp : list)
		{
			if(sp instanceof ExperienceBrick &&
					((ExperienceBrick)sp).getXpReward()>=xp_amount &&
					s.distanceTo(sp)<dist)
			{
				nearest_big_xp = (SpriteTopDown)sp;
				dist = s.distanceTo(sp);
			}
		}
		return (ExperienceBrick)nearest_big_xp;
	}

	//Override this function to trigger behavior when a ship dies
	void died(Ship s)
	{
		//Announce death
		System.out.println(s.getName()+" died and has "+s.getLives()+" lives remaining.");
		//Upgrade back up to current level
		for (int i = 0; i <= s.getLevel(); i++)
		{
			this.levelUp(s);
			this.shipLevelUp(s);
		}
	}
}
