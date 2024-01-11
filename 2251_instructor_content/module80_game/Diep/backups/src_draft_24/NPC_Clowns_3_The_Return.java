//Demo code useful for sharing with students.
public class NPC_Clowns_3_The_Return extends NPC_Personality
{
    public NPC_Clowns_3_The_Return(Board b)
    {
        super(b);

        //Turn on debugging. Could be very useful.
        //this.debugging = true;

        //Here is an example of how to select an explicit upgrade sequence:
        String[] temp_upgrade_sequence = {"Bullet Damage",
                "Bullet Damage", "Penetration", "Penetration",
                "Refire Rate", "Refire Rate", "Refire Rate", "Refire Rate",
                "Refire Rate", "Refire Rate", "Refire Rate", "Bullet Speed",
                "Bullet Speed","Bullet Speed","Bullet Speed","Bullet Speed",
                "Bullet Speed", "Bullet Speed", "Bullet Damage", "Bullet Damage", "Bullet Damage",
                "Bullet Damage", "Bullet Damage", "Penetration", "Penetration", "Penetration", "Penetration", "Penetration", "Speed", "Bullet Longevity", "Bullet Longevity"};
        this.upgrade_sequence = temp_upgrade_sequence;

        //Select a random ship upgrade sequence from one of the available sequences

        int[] temp_ship_upgrade_sequence = {Constants.SHAPE_FLANKER,
                Constants.SHAPE_CROSS, Constants.SHAPE_All_DIRECTIONS};
        this.ship_upgrade_sequence = temp_ship_upgrade_sequence;

    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        /* If nearest ship is within 200 pixels, respond to it. */
        if(s.nearest_ship!=null && s.distanceTo(s.nearest_ship)<200)
        {
            //If it is weak or my level is high, kill it!
            if(s.nearest_ship.getHealth() < 20 || s.getLevel()>25)
            {
                this.shootAttack(elapsed_seconds, s.nearest_ship, s, "I smell blood in the water!");
            }
            else
            {   //Otherwise, run away!
                this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "Tactical retreat!");
            }
        }
        //If a powerup is nearby, go get it
        else if(s.nearest_powerup!=null && s.distanceTo(s.nearest_powerup)<450)
        {   //Ram the powerup
            this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "I wants it!");
        }
        //If an enemy bullet is very close, try to shoot it down and avoid it.
        else if(s.nearest_shot!=null && s.distanceTo(s.nearest_shot)<100)
        {
            this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "I can dodge bullets.");
        }
        else if(s.nearest_xp!=null)
        {   //Otherwise attack the nearest experience brick.
            if(s.distanceTo(s.nearest_xp)<120)
            {
                this.fleeShoot(elapsed_seconds, s.nearest_xp, s, "Caution!");
            }
            else
            {
                this.shootAttack(elapsed_seconds, s.nearest_xp, s, "Delicious XP!");
            }
        }
        //Shoot as often as possible.
        s.attack();


        //Other data to make use of:

        //Whether or not self is cloaked
        //s.cloaked()

        //How much health the nearest xp brick has
        //s.nearest_xp.getHealth()

        //How much xp the nearest xp brick provides
        //s.nearest_xp.getXpReward()

        //How much collision damage the nearest ship deals
        //s.nearest_ship.body_damage

        //The level of the nearest ship
        s.nearest_ship.getLevel();
        if (s.nearest_ship.getLevel() >= s.getLevel())
            this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "Scary Monsters");

        //What powerups are active for self or nearest ship
        //s.nearest_ship.getPowerups() //boolean array of what powerups are active
        //s.getPowerups()
        //For example
        //boolean[] enemy_powerups = s.nearest_ship.getPowerups();
        //if(enemy_powerups[Constants.POW_INVINCIBILITY])

        //What the type of a ship is.
        //s.shape
        //s.nearest_ship.shape
        //For example:
        //if(s.nearest_ship.shape == Constants.SHAPE_RAMMER)

        //Stamina level of mimic and cloak ships
        //This is used for temporary bursts of speed.
        //Stamina is recovered while not moving
        //s.getStamina()

        //Note:
        //You can remember ships or xp bricks by creating a new variable to reference
        //them. Otherwise the nearest variables get reset every 1/10th of a second.
    }
}