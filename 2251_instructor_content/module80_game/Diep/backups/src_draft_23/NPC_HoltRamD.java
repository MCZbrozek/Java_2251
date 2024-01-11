import java.util.ArrayList;
import java.util.Random;

class NPC_HoltRamD extends NPC_Personality
{
    private boolean recovery_state = false;

    NPC_HoltRamD(Board b)
    {
        super(b);
        this.debugging=true;
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

        //Select a random ship upgrade sequence from one of the available sequences
        Random rng = new Random();
        int rand_choice = rng.nextInt(3);
        if(rand_choice==0)
        {
            this.ship_upgrade_sequence = new int[3];
            this.ship_upgrade_sequence[0] = Constants.SHAPE_RAMMER;
            this.ship_upgrade_sequence[1] = Constants.SHAPE_GRAVITY;
            this.ship_upgrade_sequence[2] = Constants.SHAPE_EXPLODER;
        }
        else if(rand_choice==1)
        {
            this.ship_upgrade_sequence = new int[4];
            this.ship_upgrade_sequence[0] = Constants.SHAPE_RAMMER;
            this.ship_upgrade_sequence[1] = Constants.SHAPE_MIMIC;
            this.ship_upgrade_sequence[2] = Constants.SHAPE_CREEPER;
            this.ship_upgrade_sequence[3] = Constants.SHAPE_CLOAK;
        }
        else //if(rand_choice==2)
        {
            this.ship_upgrade_sequence = new int[2];
            this.ship_upgrade_sequence[0] = Constants.SHAPE_RAMMER;
            this.ship_upgrade_sequence[1] = Constants.SHAPE_RICOCHET;
        }
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        //If nearest ship is invincible and at all near, run away
        if(s.nearest_ship!=null && s.nearest_ship.getPowerups()[Constants.POW_INVINCIBILITY] && s.distanceTo(s.nearest_ship)<600)
        {
            this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "invincible ship. Run away!");
            s.attack();
            return;
        }
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
            if(s.body_damage<4)
            {
                nearest = s.nearest_xp;
            }
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
}