class NPC_HoltShooterB extends NPC_Personality
{
    //The only differences between this and ShooterB are the upgrades sequences.
    NPC_HoltShooterB(Board b)
    {
        super(b);
        String[] temp_upgrade_sequence = {"Bullet Damage",
                "Bullet Damage", "Bullet Speed", "Refire Rate",
                "Bullet Speed", "Refire Rate", "Refire Rate",
                "Bullet Longevity", "Penetration", "Refire Rate",
                "Speed", "Speed", "Penetration","Penetration",
                "Bullet Damage","Bullet Damage","Bullet Damage",
                "Bullet Damage","Bullet Damage",
                "Refire Rate","Refire Rate","Refire Rate",
                "Penetration","Penetration","Penetration",
                "Health Regen","Health Regen","Health Regen",
                "Health","Health","Health"};
        this.upgrade_sequence = temp_upgrade_sequence;

        this.ship_upgrade_sequence = new int[3];
        this.ship_upgrade_sequence[0] = Constants.SHAPE_FLANKER;
        this.ship_upgrade_sequence[1] = Constants.SHAPE_CROSS;
        this.ship_upgrade_sequence[2] = Constants.SHAPE_All_DIRECTIONS;
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {   /*If nearest ship is close either flee it or attack it.
        * If at level 20 or above, be more aggressive toward ships
        * Otherwise attack the nearest experience brick. */
        if(s.nearest_ship!=null && (s.distanceTo(s.nearest_ship)<500 || s.getLevel()>19))
        {
            if(s.getLevel()>19 && s.getHealth()>40)
            {   //Maintain distance
                if(s.distanceTo(s.nearest_ship)<300)
                {
                    this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "");
                }
                else
                {   //Attack!
                    this.shootAttack(elapsed_seconds, s.nearest_ship, s, "");
                }
            }
            else if((s.getHealth() >= 25 && s.nearest_ship.getHealth()<10) || s.getHealth() > s.nearest_ship.getHealth()*5)
            {   //Attack!
                this.shootAttack(elapsed_seconds, s.nearest_ship, s, "");
            }
            else
            {   //Run away from the nearest ship
                this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "");
            }
        }
        else if(s.nearest_powerup!=null && s.distanceTo(s.nearest_powerup)<450)
        {   //Ram the powerup
            this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "");
        }
        else if(s.nearest_xp!=null)
        {   //Otherwise attack the nearest experience brick.
            //Maintain safe distance from target
            if(s.distanceTo(s.nearest_xp)<120)
            {
                this.fleeShoot(elapsed_seconds, s.nearest_xp, s, "");
            }
            else
            {
                this.shootAttack(elapsed_seconds, s.nearest_xp, s, "");
            }
        }
        //Shoot as often as possible.
        s.attack();
    }
}