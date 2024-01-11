import java.awt.*;

class BrickBasket extends SpriteTopDown
{
    private int points = 0;
    private Ship[] team; //What ships are on this basket's team.

    BrickBasket(double x, double y,
                double collision_radius,
                Color color,
                int i_am_bitmask,
                int i_hit_bitmask,
                Board b,
                Ship[] team)
    {
        super(x, y,
                1,
                0,
                collision_radius,
                0,
                Constants.SHAPE_RECT,
                i_am_bitmask,
                i_hit_bitmask,
                0,
                b);
        this.color = color;
        this.no_direction_draw = true;
        this.team = team;
    }

    /* This method is intended to be called once per frame and will
     * update this sprite. */
    public void update(double elapsed_seconds){}

    public void handleCollision(SpritePhysics other)
    {
        if(other instanceof ExperienceBrick)
        {
            //Reset brick
            ((ExperienceBrick) other).health = 0;
            //Gain points
            int reward = ((ExperienceBrick) other).getXpReward();
            this.points += reward;
            for (Ship ship : this.team)
            {
                ship.giveXP(reward);
            }
        }
    }

    /* Override parent class's takeDamage to check invicibility. */
    //public void takeDamage(int damage, Sprite damage_dealer){}
}