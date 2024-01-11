import java.awt.*;

public class Enemy extends Ship
{
    static final int LEVELUP_RANDOM = 0;
    static final int LEVELUP_SHOOTER = 1;
    static final int LEVELUP_RAM = 2;
    static final int LEVELUP_SHOOTER_B = 3;
    static final int LEVELUP_RAM_B = 4;
    static final int TERMINATOR_SHOOTER = 5; //This AI does not level up
    private int levelup_strategy;

    private final String[] levelup_sequence_shooter = {"Bullet Damage",
            "Bullet Damage", "Bullet Longevity", "Penetration",
            "Refire Rate", "Refire Rate", "Penetration", "Bullet Speed",
            "Bullet Speed", "Penetration", "Health", "Health",
            "Bullet Damage","Bullet Damage","Bullet Damage","Bullet Damage",
            "Refire Rate", "Refire Rate", "Refire Rate", "Refire Rate"};
    //An alternative levelup sequence for a shooter
    private final String[] levelup_sequence_shooter_b = {"Bullet Damage",
            "Bullet Damage", "Bullet Speed", "Refire Rate",
            "Bullet Speed", "Refire Rate", "Refire Rate",
            "Bullet Longevity", "Penetration", "Refire Rate",
            "Speed", "Speed", "Penetration","Penetration",
            "Bullet Damage","Bullet Damage","Bullet Damage","Bullet Damage",
            "Refire Rate","Refire Rate"};
    private final String[] levelup_sequence_ram = {"Collision Damage",
            "Collision Damage", "Health Regen", "Health Regen", "Health Regen",
            "Health Regen", "Health Regen", "Health", "Speed", "Speed",
            "Health", "Collision Damage", "Collision Damage",
            "Collision Damage", "Collision Damage", "Speed",
            "Speed", "Speed", "Speed", "Health"};
    private final String[] levelup_sequence_ram_b = {"Health Regen", "Health Regen",
            "Health Regen", "Health Regen", "Health Regen", "Health",
            "Collision Damage", "Collision Damage", "Speed", "Speed",
            "Collision Damage", "Collision Damage",
            "Collision Damage", "Collision Damage", "Health", "Speed",
            "Speed", "Speed", "Speed", "Health"};

    //Cooldown before next checking for new nearest
    private double targeting_cooldown = 0;
    //Refresh nearest sprites every targeting_cooldown_reset seconds
    private final double targeting_cooldown_reset = 0.1;
    //Whether or not to switch target
    private boolean lock_target = false;

    public Enemy(double x, double y,
                 String name,
                 Color color,
                 double collision_radius,
                 int i_am_bitmask,
                 int i_hit_bitmask,
                 int levelup_strategy,
                 Board board)
    {
        super(x, y,
                name,
                collision_radius,
                i_am_bitmask,
                i_hit_bitmask,
                BoardTopDown.bullet_bitmask,//I am
                BoardTopDown.everything_bitmask,//I hit
                board);
        this.color = color;
        this.levelup_strategy = levelup_strategy;
    }

    /* This method is intended to be called once per frame and will
     * update this sprite. */
    public void update(double elapsed_seconds)
    {   //Check if it's time to get new targets
        targeting_cooldown -= elapsed_seconds;
        if (targeting_cooldown < 0 && !lock_target)
        {   //Update nearest ship, shot, and brick variables
            this.updateTarget();
            targeting_cooldown = targeting_cooldown_reset;
        }
        //AI
        if(this.levelup_strategy == LEVELUP_RAM || this.levelup_strategy == LEVELUP_RAM_B)
        {
            this.rammerBehavior(elapsed_seconds);
        }
        else if(this.levelup_strategy == TERMINATOR_SHOOTER)
        {
            this.terminatorShooter(elapsed_seconds);
        }
        else
        {
            this.shooterBehavior(elapsed_seconds);
        }
        this.attack();
        //Apply friction, move, and stay in bounds.
        super.update(elapsed_seconds);
        //Level up if possible
        this.levelUp();
    }

    /** AI for shooters and random bots. */
    private void shooterBehavior(double elapsed_seconds)
    {
        //Skip if any nearests are null to avoid errors
        if(nearest_shot == null || nearest_ship == null || nearest_xp == null){return;}
        /*If nearest ship is close either flee it or attack it.
        * If at level 20 or above, be more aggressive toward ships
        * Otherwise attack the nearest experience brick. */
        if(this.distanceTo(nearest_ship)<500 || this.getLevel()>19)
        {
            //Orient gun toward the ship
            this.setAngle(this.angleToSprite(nearest_ship));
            if(this.getLevel()>19 && this.getHealth()>40)
            {   //Maintain distance
                if(this.distanceTo(nearest_ship)<300){this.moveAwayFrom(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());}
                else{this.moveToward(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());}
            }
            else if((this.getHealth() >= 25 && nearest_ship.getHealth()<10) || this.getHealth() > nearest_ship.getHealth()*5)
            {   //Attack!
                this.moveToward(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
            }
            else
            {   //Run away from the nearest ship
                this.moveAwayFrom(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
            }
        }
        else
        {   //Otherwise attack the nearest experience brick.
            //Orient toward target
            this.setAngle(this.angleToSprite(nearest_xp));
            //Maintain safe distance from target
            double dist = this.distanceTo(nearest_xp);
            if(dist<120)
            {
                this.moveAwayFrom(elapsed_seconds, nearest_xp.getXCenter(), nearest_xp.getYCenter());
            }
            else
            {
                this.moveToward(elapsed_seconds, nearest_xp.getXCenter(), nearest_xp.getYCenter());
            }
        }
    }

    /** AI that shoots target exclusively, ignoring all else. */
    private void terminatorShooter(double elapsed_seconds)
    {
        //Orient gun toward the ship
        this.setAngle(this.angleToSprite(nearest_ship));
        //Move in for the kill
        this.moveToward(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
    }

    /** AI for ram bots. */
    private void rammerBehavior(double elapsed_seconds)
    {
        //Skip if any nearests are null to avoid errors
        if(nearest_shot == null || nearest_ship == null || nearest_xp == null){return;}
        //If health is low, or level is low, avoid everything and wait for regen
        if(this.getHealth()<25 || this.getLevel()<3)
        {
            SpriteTopDown nearest = nearest_ship;
            if(this.distanceTo(nearest_xp) < this.distanceTo(nearest))
            {
                nearest = nearest_xp;
            }
            if(this.distanceTo(nearest_shot) < this.distanceTo(nearest))
            {
                nearest = nearest_shot;
            }
            //Move away from nearest object
            this.moveAwayFrom(elapsed_seconds, nearest.getXCenter(), nearest.getYCenter());
            //Orient gun toward the nearest xp brick so that the rammer
            //can continue to gain experience.
            this.setAngle(this.angleToSprite(nearest_xp));
        }
        /*If nearest ship is close either flee it or attack it.
        * If at level 20 or above, be more aggressive toward ships
        * Otherwise attack the nearest experience brick. */
        else if(this.distanceTo(nearest_ship)<450 || this.getLevel()>19)
        {
            if((this.getHealth() >= 25 && nearest_ship.getHealth()<10) || this.getHealth() > nearest_ship.getHealth()*5 || (this.getLevel()>19 && this.getHealth() > nearest_ship.getHealth()))
            {   //Attack!
                //Orient gun away from the ship for extra recoil thrust!
                this.setAngle(nearest_ship.angleToSprite(this));
                this.moveToward(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
            }
            else
            {   //Run away from the nearest shot
                //Orient gun toward the ship
                this.setAngle(this.angleToSprite(nearest_ship));
                this.moveAwayFrom(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
            }
        }
        else
        {   //Otherwise attack the nearest experience brick.
            //Orient gun away from the xp for extra recoil thrust!
            this.setAngle(nearest_xp.angleToSprite(this));
            this.moveToward(elapsed_seconds, nearest_xp.getXCenter(), nearest_xp.getYCenter());
        }
    }

    private void levelUp()
    {
        if(availableLevelUpPoints()>0 && levelup_strategy != TERMINATOR_SHOOTER)
        {
            String name;
            if(levelup_strategy == LEVELUP_RANDOM)
            {
                name = Constants.getRandomAttribute();
            }
            else if(levelup_strategy == LEVELUP_SHOOTER)
            {   //-1 because the array is indexed starting at zero,
                //but the first level up happens at level 1.
                int temp = (getLevel()-1)%levelup_sequence_shooter.length;
                name = levelup_sequence_shooter[temp];
            }
            else if(levelup_strategy == LEVELUP_SHOOTER_B)
            {
                //-1 because the array is indexed starting at zero,
                //but the first level up happens at level 1.
                int temp = (getLevel()-1)%levelup_sequence_shooter_b.length;
                name = levelup_sequence_shooter_b[temp];
            }
            else if(levelup_strategy == LEVELUP_RAM_B)
            {
                //-1 because the array is indexed starting at zero,
                //but the first level up happens at level 1.
                int temp = (getLevel()-1)%levelup_sequence_ram_b.length;
                name = levelup_sequence_ram_b[temp];
            }
            else //if(levelup_strategy == LEVELUP_RAM)
            {   //-1 because the array is indexed starting at zero,
                //but the first level up happens at level 1.
                int temp = (getLevel()-1)%levelup_sequence_ram.length;
                name = levelup_sequence_ram[temp];
            }
            levelUpAttribute(name);
            //System.out.println("Enemy leveled up "+name);
        }
    }

    /** Alert this enemy that it was damaged by another ship so it can respond. */
    public void takeDamage(int damage, Sprite damage_dealer)
    {
        super.takeDamage(damage, damage_dealer);
        //Remember the responsible Ship
        if(!lock_target)
        {
            if (damage_dealer instanceof Ship)
            {
                nearest_ship = (Ship) damage_dealer;
            }
            else if (damage_dealer instanceof Shot)
            {
                nearest_ship = ((Shot) damage_dealer).getShooter();
            }
        }
    }

    void lockTarget(Ship s)
    {
        nearest_ship = s;
        lock_target = true;
    }
}