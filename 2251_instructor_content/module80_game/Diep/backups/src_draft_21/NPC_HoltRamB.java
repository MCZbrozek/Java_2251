public class NPC_HoltRamB extends NPC_Personality
{
    //The only differences between this and RamA are the upgrades sequences.
    public NPC_HoltRamB(Board b)
    {
        super(b);
        String[] temp_upgrade_sequence = {"Health Regen", "Health Regen",
                "Health Regen", "Health Regen", "Health Regen", "Health Regen",
                "Health Regen", "Health", "Collision Damage", "Collision Damage",
                "Health", "Health", "Health", "Health", "Health", "Health",
                "Collision Damage", "Collision Damage",
                "Collision Damage", "Collision Damage",
                "Collision Damage", "Speed",
                "Speed", "Speed", "Speed",
                "Speed", "Speed", "Speed",
                "Bullet Damage","Bullet Damage","Bullet Damage"};
        this.upgrade_sequence = temp_upgrade_sequence;

        int[] temp_ship_upgrade_sequence = {Constants.SHAPE_RAMMER,
                Constants.SHAPE_GRAVITY,
                Constants.SHAPE_EXPLODER};
        this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        //Skip if any nearests are null to avoid errors
        if(s.nearest_shot == null || s.nearest_ship == null || s.nearest_xp == null){return;}
        //If health is low avoid everything and wait for regen
        if(s.getHealth()<25)
        {
            SpriteTopDown nearest = s.nearest_ship;
            if(s.distanceTo(s.nearest_xp) < s.distanceTo(nearest))
            {
                nearest = s.nearest_xp;
            }
            if(s.distanceTo(s.nearest_shot) < s.distanceTo(nearest))
            {
                nearest = s.nearest_shot;
            }
            //Move away from nearest object
            this.fleeShoot(elapsed_seconds, nearest, s, "");
        }
        /*If nearest ship is close either flee it or attack it.
        * Be more likely to farm at lower levels. */
        else if(s.distanceTo(s.nearest_ship) < 20*s.getLevel() && s.getHealth() > 50)
        {   //Attack nearest_ship!
            this.ramAttack(elapsed_seconds, s.nearest_ship, s, "");
        }
        else if(s.nearest_powerup!=null && s.distanceTo(s.nearest_powerup)<450)
        {   //Ram nearest powerup
            this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "");
        }
        else
        {   //Otherwise ram the nearest experience brick.
            this.ramAttack(elapsed_seconds, s.nearest_xp, s, "");
        }
        //Shoot as often as possible.
        s.attack();
    }
}