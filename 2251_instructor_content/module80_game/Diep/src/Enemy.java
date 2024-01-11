import java.awt.Color;

public class Enemy extends Ship
{
    //Cooldown before next checking for new nearest
    private double targeting_cooldown = 0;
    //Whether or not to switch target
    private boolean lock_target = false;

    //Ship's AI
    private NPC_Personality personality;

    public Enemy(double x, double y,
                 String name,
                 Color color,
                 int team_id,
                 double collision_radius,
                 int i_am_bitmask,
                 int i_hit_bitmask,
                 NPC_Personality personality,
                 Board board)
    {
        super(x, y,
                name,
                collision_radius,
                color,
                team_id,
                i_am_bitmask,
                i_hit_bitmask,
                BoardTopDown.bullet_bitmask,//I am
                BoardTopDown.everything_bitmask,//I hit
                board);
        this.personality = personality;
    }

    /* This method is intended to be called once per frame and will
     * update this sprite. */
    public void update(double elapsed_seconds)
    {   //Check if it's time to get new targets
        targeting_cooldown -= elapsed_seconds;
        if (targeting_cooldown < 0 && !lock_target)
        {   //Update nearest ship, shot, and brick variables
            this.updateTarget();
            targeting_cooldown = 0.1;
        }
        //AI
        personality.update(elapsed_seconds, this);
        //Apply friction, move, and stay in bounds.
        super.update(elapsed_seconds);
    }

    /**Override parent class's function where this enemy gains xp
     * in order to check if it is time to level up. */
    void giveXP(int amount)
    {
        super.giveXP(amount);
        //Level up if possible
        personality.levelUp(this);
        personality.shipLevelUp(this);
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

    void informDied()
    {
        this.personality.died(this);
    }
}