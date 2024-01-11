public class NPC_HoltShooterA extends NPC_Personality
{
    public NPC_HoltShooterA(Board b)
    {
        super(b);
        String[] temp_upgrade_sequence = {"Bullet Damage",
                "Bullet Damage", "Bullet Longevity", "Penetration",
                "Refire Rate", "Refire Rate", "Penetration", "Bullet Speed",
                "Bullet Speed", "Penetration", "Health", "Health",
                "Bullet Damage","Bullet Damage","Bullet Damage","Bullet Damage",
                "Refire Rate", "Refire Rate", "Refire Rate", "Refire Rate"};
        this.upgrade_sequence = temp_upgrade_sequence;

        int[] temp_ship_upgrade_sequence = {Constants.SHAPE_MACHINE_GUN,
                Constants.SHAPE_CARRIER};
        this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        //Skip if any nearests are null to avoid errors
        if(s.nearest_shot == null || s.nearest_ship == null || s.nearest_xp == null){return;}
        /*If nearest ship is close either flee it or attack it.
        * If at level 20 or above, be more aggressive toward ships
        * Otherwise attack the nearest experience brick. */
        if(s.distanceTo(s.nearest_ship)<500 || s.getLevel()>19)
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
        else if(s.distanceTo(s.nearest_powerup)<450)
        {   //Ram the powerup
            this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "");
        }
        else
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