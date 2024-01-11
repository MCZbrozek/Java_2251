class NPC_HoltRam extends NPC_Personality
{
	NPC_HoltRam(Board b)
	{
		super(b);
		String[] temp_upgrade_sequence = {
				"Health Regen", "Health Regen",
				"Health Regen", "Health Regen",
				"Health Regen", "Health Regen",
				"Health Regen", "Health", "Speed", "Speed",
				"Health", "Collision Damage", "Collision Damage",
				"Collision Damage", "Collision Damage", "Speed",
				"Speed", "Speed", "Speed", "Health", "Collision Damage",
				"Collision Damage", "Health", "Health", "Health",
				"Health", "Collision Damage","Speed","Speed",
				"Bullet Damage","Bullet Damage","Bullet Damage"};
		this.upgrade_sequence = temp_upgrade_sequence;

		this.ship_upgrade_sequence = new int[4];
		this.ship_upgrade_sequence[0] = Constants.SHAPE_RAMMER;
		this.ship_upgrade_sequence[1] = Constants.SHAPE_MIMIC;
		this.ship_upgrade_sequence[2] = Constants.SHAPE_CREEPER;
		this.ship_upgrade_sequence[3] = Constants.SHAPE_CLOAK;
	}

	@Override
	void update(double elapsed_seconds, Ship s)
	{
		//If health is low avoid everything and wait for regen, but shoot at nearest_xp brick.
		if(s.getHealth()<25)
		{
			//Figure out the truly nearest sprite...
			SpriteTopDown nearest = s.nearest_ship;
			if(s.nearest_xp != null && (nearest==null || s.distanceTo(s.nearest_xp) < s.distanceTo(nearest)))
			{
				nearest = s.nearest_xp;
			}
			if(s.nearest_shot != null && (nearest == null || s.distanceTo(s.nearest_shot) < s.distanceTo(nearest)))
			{
				nearest = s.nearest_shot;
			}
			//Move away from nearest sprite
			if(nearest != null && nearest != s.nearest_xp) {
				this.fleeShoot(elapsed_seconds, nearest, s, "");
			}
			else if(s.nearest_xp != null && s.distanceTo(s.nearest_xp) > 300){
				this.shootAttack(elapsed_seconds, s.nearest_xp, s, "");
			}
			else if(s.nearest_xp != null){
				this.fleeShoot(elapsed_seconds, s.nearest_xp, s, "");
			}
		}
		/*If nearest ship is close either flee it 
		or attack it.
		Be more likely to farm at lower levels. */
		//If distance to nearest ship is less than 20 times my level and my health is above 50 then attack!
		else if(s.nearest_ship != null && (
		s.distanceTo(s.nearest_ship) < 20*s.getLevel() && s.getHealth() > 50))
		{   //Attack nearest_ship!
			this.ramAttack(elapsed_seconds, s.nearest_ship, s, "");
		}
		else if(s.nearest_powerup!=null && s.distanceTo(s.nearest_powerup)<450)
		{   //Ram nearest powerup
			this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "");
		}
		else if(s.nearest_xp!=null)
		{   //Otherwise ram the nearest experience brick.
			this.ramAttack(elapsed_seconds, s.nearest_xp, s, "");
		}
		//Shoot as often as possible.
		s.attack();
	}
}
