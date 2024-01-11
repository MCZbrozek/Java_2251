import java.awt.*;

class ExperienceBrick extends SpriteTopDown
{
    private boolean suicidal = false;
    private boolean regenerate = true;

    ExperienceBrick(double x, double y,
                    double collision_radius,
                    int health,
                    int xp_reward,
                    Color color,
                    int shape,
                    boolean suicidal,
                    int i_am_bitmask,
                    int i_hit_bitmask,
                    int body_damage,
                    boolean regenerate,
                    Board b)
    {
        super(x, y,
                health,
                0,
                collision_radius,
                xp_reward,
                shape,
                i_am_bitmask,
                i_hit_bitmask,
                body_damage,
                b);
        this.color = color;
        this.no_direction_draw = true;
        this.moveToCenter(x,y);
        this.suicidal = suicidal;
        this.regenerate = regenerate;
        this.speed = Constants.suicide_brick_speed;
    }

    /* This method is intended to be called once per frame and will
     * update this sprite. */
    public void update(double elapsed_seconds)
    {
        //Apply friction, move, and stay in bounds.
        super.update(elapsed_seconds);
        //If suicidal, find nearest ship and try to crash into it.
        if(suicidal)
        {
            if(nearest_ship == null || nearest_ship.getHealth()<=0){this.updateTarget();}
            if(nearest_ship != null){this.moveToward(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());}
        }
    }

    public void takeDamage(int damage, Sprite damage_dealer)
    {
        super.takeDamage(damage, damage_dealer);
        //If this experience brick is dead, relocate it and reset its health
        if(this.getHealth()<=0 && regenerate)
        {
            this.setHealthMax();
            this.setX(Constants.getRandomX());
            this.setY(Constants.getRandomY());
            this.nearest_ship = null; //Reset the nearest ship
        }
    }
}