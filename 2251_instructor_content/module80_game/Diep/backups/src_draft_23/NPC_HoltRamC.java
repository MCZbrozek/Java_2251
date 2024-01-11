import java.util.ArrayList;

class NPC_HoltRamC extends NPC_Personality
{
    private boolean recovery_state = false;

    NPC_HoltRamC(Board b)
    {
        super(b);
        this.debugging=true;
        /* rammer strat
        -aggressively seek powerups
        -regen regularly to stay near peak health
        -focus down high value bricks. ignore low value bricks
        -low health, don't move except to avoid ships
        -upgrade all regen, 1 health,
         2 damage (that should make you 1 shot intermediate bricks),
         2 health, then max damage, put some more in speed and health.
        -wait near center for ambush*/
        String[] temp_upgrade_sequence = {
                "Bullet Damage",
                "Penetration",
                "Refire Rate","Refire Rate","Refire Rate",
                "Bullet Damage","Bullet Damage","Bullet Damage",
                "Penetration", "Penetration",
                "Bullet Speed", "Bullet Longevity",
                "Refire Rate","Refire Rate","Refire Rate",
                "Refire Rate",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Damage","Bullet Damage","Bullet Damage",
                "Health", "Health",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Speed", "Bullet Longevity",
                "Health Regen",
                "Health Regen", "Health Regen",
                "Health Regen", "Health Regen",
                "Health Regen"};
        this.upgrade_sequence = temp_upgrade_sequence;

        this.ship_upgrade_sequence = new int[1];
        this.ship_upgrade_sequence[0] = Constants.SHAPE_MACHINE_GUN;

    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        if(s.shape == Constants.SHAPE_CIRCLE || s.shape == Constants.SHAPE_MACHINE_GUN)
        {
            gunBehavior(elapsed_seconds,s);
        }
        else
        {
            ramBehavior(elapsed_seconds,s);
        }
    }

    private void gunBehavior(double elapsed_seconds, Ship s)
    {
        //If nearest ship is invincible and at all near, run away
        if(s.nearest_ship!=null && s.nearest_ship.getPowerups()[Constants.POW_INVINCIBILITY] && s.distanceTo(s.nearest_ship)<600)
        {
            this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "invincible ship. Run away!");
            s.attack();
            return;
        }
        //When low level, just shoot the nearest xp bricks until you level up.
        //This will speed up initial level
        if(s.getLevel()<2 && s.nearest_xp!=null)
        {
            if(s.distanceTo(s.nearest_xp)<150)
            {
                this.fleeShoot(elapsed_seconds, s.nearest_xp, s, "maintain distance");
            }
            else
            {
                this.shootAttack(elapsed_seconds, s.nearest_xp, s, "get xp");
            }
            //Shoot as often as possible.
            s.attack();
            return;
        }
        //When high level, focus on killing other ships
        else if(s.getLevel()>=31 && s.nearest_ship!=null)
        {
            this.shootAttack(elapsed_seconds, s.nearest_ship, s, "Maxed out. Attack!");
        }
        //Get nearest big xp brick
        ExperienceBrick memory_brick = this.getNearestBigXp(s, 11);
        //Seek nearest powerup if it is anywhere close
        if(s.nearest_powerup!=null && s.distanceTo(s.nearest_powerup)<800)
        {
            this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "Powerup!");
        }
        //Shoot nearest ship if it gets too close
        else if(s.nearest_ship!=null && s.distanceTo(s.nearest_ship)<250)
        {
            this.shootMove(elapsed_seconds, s.nearest_ship, memory_brick, s, "shoot ship. get xp");
        }
        //Opportunistically attack very weak ships nearby
        else if(s.nearest_ship!=null && s.nearest_ship.getHealth()/s.getBulletDamage()<5)
        {
            this.shootAttack(elapsed_seconds, s.nearest_ship, s, "Blood in the water");
        }
        //Shoot nearest brick while moving toward biggest xp brick
        else
        {
            //Shoot nearest big brick if it's close but maintain distance
            if(s.distanceTo(memory_brick)<150)
            {
                this.fleeShoot(elapsed_seconds, memory_brick, s, "maintain distance");
            }
            else if(s.distanceTo(memory_brick)<450)
            {
                this.shootAttack(elapsed_seconds, memory_brick, s, "get big xp");
            }
            else if(s.shape == Constants.SHAPE_MACHINE_GUN)
            {
                //Gently spin and shoot
                s.turnAmount(Math.PI, elapsed_seconds);
                //Move toward big exp brick
                s.moveToward(elapsed_seconds, memory_brick.getXCenter(), memory_brick.getYCenter());
            }
            else
            {   //Shoot nearest brick. Move toward nearest big xp brick
                this.shootMove(elapsed_seconds, s.nearest_xp, memory_brick, s, "shoot xp. get xp");
            }
        }
        //Shoot as often as possible.
        s.attack();
    }

    private void ramBehavior(double elapsed_seconds, Ship s)
    {
        //If nearest ship is invincible and at all near, run away
        if(s.nearest_ship!=null && s.nearest_ship.getPowerups()[Constants.POW_INVINCIBILITY] && s.distanceTo(s.nearest_ship)<600)
        {
            this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "invincible ship. Run away!");
            s.attack();
            return;
        }
        /*
        rammer strat
        -aggressively seek powerups
        -regen regularly to stay near peak health
        -focus down high value bricks. ignore low value bricks
        -low health, don't move except to avoid ships
        -upgrade all regen, 1 health,
         2 damage (that should make you 1 shot intermediate bricks),
         2 health, then max damage, put some more in speed and health.
        -wait near center for ambush*/
        //Get nearest high value brick and killable enemy
        ArrayList<SpritePhysics> list = s.board_reference.getSpriteList();
        SpriteTopDown nearest_big_xp = null;
        double dist = Double.MAX_VALUE;
        SpriteTopDown nearest_weak_enemy = null;
        double dist_enemy = Double.MAX_VALUE;
        for (SpritePhysics sp : list)
        {
            if(sp instanceof ExperienceBrick &&
                    ((ExperienceBrick)sp).getXpReward()==11 &&
                    s.distanceTo(sp)<dist)
            {
                nearest_big_xp = (SpriteTopDown)sp;
                dist = s.distanceTo(sp);
            }
            //Other is a ship, not me, and killable in 3 or fewer hits
            else if(sp instanceof Ship && sp != s &&
                ((Ship)sp).getHealth()/s.body_damage <= 3.0 &&
                s.distanceTo(sp)<dist_enemy)
            {
                nearest_weak_enemy = (SpriteTopDown)sp;
                dist_enemy = s.distanceTo(sp);
            }
        }
        //if low health: enter recovery state.
        if(s.getHealth()<50)
        {
            recovery_state = true;
        }
        //if health is above 90%: exit recovery state
        else if(s.getPercentHealth()>=0.9)
        {
            recovery_state = false;
        }
        //if recovery state:
        //avoid nearest shot or ship (whichever is closer)
        //stand still if none is close
        //shoot nearest xp brick
        if(recovery_state)
        {
            SpriteTopDown nearest = s.nearest_ship;
            if(s.nearest_shot != null && (s.nearest_ship==null || s.distanceTo(s.nearest_shot)<s.distanceTo(s.nearest_ship)))
            {
                nearest = s.nearest_shot;
            }
            int min_flee_dist = 500;
            //Be more likely to hide by holding still if this ship cloaks
            if(s.shape == Constants.SHAPE_MIMIC ||
                    s.shape == Constants.SHAPE_CREEPER ||
                    s.shape == Constants.SHAPE_CLOAK)
            {
                min_flee_dist = 200;
            }
            //Flee nearest ship or shot
            if(nearest!=null && s.distanceTo(nearest) < min_flee_dist)
            {
                this.fleeShoot(elapsed_seconds, nearest, s, "low health, flee");
            }
            //Remain motionless
            else if(s.nearest_xp!=null)
            {
                this.fleeShoot(0, s.nearest_xp, s, "low health. waiting");
            }
        }
        //elif my level > 27: wait for ambush
        else if(s.getLevel()>27)
        {
            if(s.nearest_powerup!=null)
            {
                this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "Get powerup!");
            }
            else if(s.nearest_ship.getHealth() < s.getHealth() || s.distanceTo(s.nearest_ship)<350)
            {   //Attack
                this.ramAttack(elapsed_seconds, s.nearest_ship, s, "Attack ship!");
            }
            else if(s.distanceTo(s.nearest_ship)>500)
            {   //Wait in ambush
                this.fleeShoot(0, s.nearest_xp, s, "ambush. waiting");
            }
            else
            {   //run away
                this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "flee");
            }
        }
        //nearest enemy is near and I have lots of health. attack them
        else if(s.distanceTo(s.nearest_ship)<500 && s.getPercentHealth()>0.6)
        {
            this.ramAttack(elapsed_seconds, s.nearest_ship, s, "Attack ship!");
        }
        //else: find nearest powerup, highest value brick, enemy I can kill in 5 or fewer hits
	    //seek whichever is closest
        else
        {   //Get nearest of big xp brick, powerup, killable enemy.
            SpriteTopDown nearest = nearest_big_xp;
            String text = "Get XP";
            if(nearest==null || (nearest_weak_enemy!=null && s.distanceTo(nearest_weak_enemy) < s.distanceTo(nearest)))
            {
                nearest = nearest_weak_enemy;
                text = "Blood in the water!";
            }
            if(nearest==null || (s.nearest_powerup!=null && s.distanceTo(s.nearest_powerup) < s.distanceTo(nearest)))
            {
                nearest = s.nearest_powerup;
                text = "Get Powerup";
            }
            //Ram nearest
            this.ramAttack(elapsed_seconds, nearest, s, text);
        }
        //Shoot as often as possible.
        s.attack();
    }

    void died(Ship s)
    {
        if(s.getLives()<2 || s.getLevel()>20)
        {
            //Switch to rammer:
            String[] temp_upgrade_sequence = {
                    "Health Regen", "Health Regen",
                    "Health Regen", "Health Regen",
                    "Health Regen", "Health Regen",
                    "Health Regen",
                    "Collision Damage", "Collision Damage",
                    "Collision Damage",
                    "Health", "Health",
                    "Speed", "Speed",
                    "Health",
                    "Collision Damage", "Collision Damage",
                    "Collision Damage", "Collision Damage",
                    "Health", "Health",
                    "Speed", "Speed",
                    "Health", "Health",
                    "Speed", "Speed", "Speed",
                    "Bullet Damage", "Bullet Damage", "Bullet Damage"};
            this.upgrade_sequence = temp_upgrade_sequence;

            this.ship_upgrade_sequence = new int[4];
            this.ship_upgrade_sequence[0] = Constants.SHAPE_RAMMER;
            this.ship_upgrade_sequence[1] = Constants.SHAPE_MIMIC;
            this.ship_upgrade_sequence[2] = Constants.SHAPE_CREEPER;
            this.ship_upgrade_sequence[3] = Constants.SHAPE_CLOAK;
        }
        super.died(s);
    }
}