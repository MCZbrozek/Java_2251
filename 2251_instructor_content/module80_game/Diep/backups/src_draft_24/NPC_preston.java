import java.util.Random;

//Demo code useful for sharing with students.
public class NPC_m͏̺͓̲̥̪í͇͔̠ś̷͎̹̲̻̻̘̝t̞̖͍͚̤k̥̞à̸͕̮͍͉̹̰͚̰ẹ̶̢̪s͏̨͈̙̹̜͚̲ extends NPC_Personality
{

    public static int deaths = 0;
    private Random rng = new Random();

    public NPC_m͏̺͓̲̥̪í͇͔̠ś̷͎̹̲̻̻̘̝t̞̖͍͚̤k̥̞à̸͕̮͍͉̹̰͚̰ẹ̶̢̪s͏̨͈̙̹̜͚̲(Board b)
    {
        super(b);

        //Turn on debugging. Could be very useful.
        this.debugging = true;

        //Select a random upgrade sequence
       // this.upgrade_sequence = new String[Constants.xp_per_level.length];
        //for (int i = 0; i < Constants.xp_per_level.length; i++)
       // {
            //int rand_choice = rng.nextInt(Constants.attribute_names.length);
           // this.upgrade_sequence[i] = Constants.attribute_names[rand_choice];
        //}
        String[] temp_upgrade_sequence = {"Bullet Damage",
                "Bullet Damage", "Refire Rate", "Refire Rate",
                "Refire Rate", "Refire Rate", "Bullet Damage", "Penetration",
                "Bullet Speed", "Bullet Speed", "Refire Rate", "Refire Rate",
                "Penetration","Bullet Speed","Bullet Damage","Bullet Damage",
                "Health", "Refire Rate", "Bullet Damage", "Bullet Speed","Bullet Damage","Bullet Speed","Bullet Speed",
                "Penetration","Penetration","Penetration","Penetration","Health","Health","Health","Health"};
        this.upgrade_sequence = temp_upgrade_sequence;

        //Select a random ship upgrade sequence from one of the available sequences
        int rand_choice = 2;
        if(rand_choice==0)
        {
            int[] temp_ship_upgrade_sequence = {Constants.SHAPE_RAMMER,
                    Constants.SHAPE_GRAVITY,
                    Constants.SHAPE_EXPLODER};
            this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
        }
        else if(rand_choice==1)
        {
            int[] temp_ship_upgrade_sequence = {Constants.SHAPE_RAMMER,
                    Constants.SHAPE_MIMIC,
                    Constants.SHAPE_CREEPER,
                    Constants.SHAPE_CLOAK};
            this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
        }
        else if(rand_choice==2)
        {
            int[] temp_ship_upgrade_sequence = {Constants.SHAPE_FLANKER,
                    Constants.SHAPE_CROSS, Constants.SHAPE_All_DIRECTIONS};
            this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
        }
        else if(rand_choice==3)
        {
            int[] temp_ship_upgrade_sequence = {Constants.SHAPE_TWIN,
                    Constants.SHAPE_SNIPER, Constants.SHAPE_SEEKER};
            this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
        }
        else if(rand_choice==4)
        {
            int[] temp_ship_upgrade_sequence = {Constants.SHAPE_TWIN,
                    Constants.SHAPE_SNIPER, Constants.SHAPE_CANNON};
            this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
        }
        else if(rand_choice==5)
        {
            int[] temp_ship_upgrade_sequence = {Constants.SHAPE_MACHINE_GUN,
                    Constants.SHAPE_CARRIER};
            this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
        }
        else
        {
            int[] temp_ship_upgrade_sequence = {Constants.SHAPE_TWIN,
                    Constants.SHAPE_SNIPER, Constants.SHAPE_LASER};
            this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
        }
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        /* If nearest ship is within 200 pixels, respond to it. */
        if(s.nearest_ship!=null && s.distanceTo(s.nearest_ship)<=200)
        {
            //If it is weak or my level is high, kill it!
            if(s.nearest_ship.getHealth() < 30 || s.getLevel()>15)
            {
                this.shootAttack(elapsed_seconds, s.nearest_ship, s, "I smell blood in the water!");
            }
            else
            {   //Otherwise, run away!
                this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "Tactical retreat!");
            }
        }

        //If a powerup is nearby, go get it
        else if(s.nearest_powerup!=null && s.distanceTo(s.nearest_powerup)<1000)
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
        //s.nearest_ship.getLevel()

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

    void died(Ship s)
    {
        deaths = deaths + 1;

        if (deaths == 2)
        {
            int[] temp_ship_upgrade_sequence = {Constants.SHAPE_TWIN,
                    Constants.SHAPE_SNIPER, Constants.SHAPE_CANNON};
            this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
        }
    }
}