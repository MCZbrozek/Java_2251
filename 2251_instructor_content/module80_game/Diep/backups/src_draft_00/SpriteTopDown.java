import java.awt.*;

/**Created by nealh_000 on 5/22/2017.
 * This class implements some functionality common to all sprites in
 * this top down game. */
public abstract class SpriteTopDown extends SpriteCircular
{
    Color color = Color.WHITE;

    private int health = 0;
    private int max_health = 1;

    boolean no_health_draw = false;
    boolean no_direction_draw = false;

    private double friction = -0.95;

    private int xp=0;//Experience Points
    private int cumulative_xp=0;
    //Experience given for killing this sprite:
    private int xp_reward;

    //Damage dealt by collisions with this body
    private int body_damage = 1;

    //Delay between damage taking from the same sprite in seconds
    private double redamage_cooldown = 0;
    private double redamage_cooldown_reset = 0.1;
    private SpriteTopDown recent_damage_source = null;

    public SpriteTopDown(double x, double y, int health, double angle, double radius, int xp_reward, int i_am_bitmask, int i_hit_bitmask)
    {
        super(x, y, angle, radius, "", i_am_bitmask, i_hit_bitmask);
        this.health = health;
        this.max_health = health;
        this.xp_reward = xp_reward;
    }

    public void update(double elapsed_seconds)
    {
        //Apply friction
        this.changeVelocity(this.getdx() * friction * elapsed_seconds, this.getdy() * friction * elapsed_seconds);
        //Move
        this.move(elapsed_seconds);
        //Keep this sprite in bounds
        double impulse = 400;
        if (this.getX() < 0)
        {
            this.changeVelocity(impulse * elapsed_seconds, 0);
        }
        if (this.getX() + this.getCollisionRadius() * 2 > BoardTopDown.boundary_right)
        {
            this.changeVelocity(-impulse * elapsed_seconds, 0);
        }
        if (this.getY() < 0)
        {
            this.changeVelocity(0, impulse * elapsed_seconds);
        }
        if (this.getY() + this.getCollisionRadius() * 2 > BoardTopDown.boundary_down)
        {
            this.changeVelocity(0, -impulse * elapsed_seconds);
        }
        //Cooldown the damage immunity from most recent source of damage to this sprite.
        redamage_cooldown -= elapsed_seconds;
    }

    /**
     * Override parent class's draw.
     */
    public void draw(Board b, Graphics g, int offset_x, int offset_y)
    {
        //Determine whether or not to draw the sprite
        if (this.isVisible())
        {
            int draw_x = (int) (this.getX() + (double) offset_x);
            int draw_y = (int) (this.getY() + (double) offset_y);
            Graphics2D g2d = (Graphics2D) g;
            //Update the on_screen status of this sprite.
            this.setIsOnScreen(draw_x, draw_y);
            if (this.getIsOnScreen())
            {   //Draw sprite as a circle.
                g2d.setColor(color);
                g2d.fillOval(draw_x, draw_y, (int) this.getCollisionRadius() * 2, (int) this.getCollisionRadius() * 2);
                if (!no_direction_draw)
                {
                    //Draw orientation circle
                    double heading_adjust = 40;
                    double dx = Math.cos(this.getAngle()) * heading_adjust;
                    double dy = Math.sin(this.getAngle()) * heading_adjust;
                    double heading_radius = this.getCollisionRadius() / 3;
                    g2d.fillOval((int) (draw_x + this.getCollisionRadius() + dx - heading_radius), (int) (draw_y + this.getCollisionRadius() + dy - heading_radius), (int) heading_radius * 2, //width
                            (int) heading_radius * 2); //height
                }
                if (!no_health_draw && this.getPercentHealth() < 1.0)
                {
                    //Draw health
                    g2d.setColor(Color.RED);
                    g2d.fillRect(draw_x, (int) (draw_y + this.getCollisionRadius() * 1.8), (int) (this.getCollisionRadius() * 2), 5); //height
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(draw_x, (int) (draw_y + this.getCollisionRadius() * 1.8), (int) (this.getCollisionRadius() * 2 * this.getPercentHealth()), 5); //height
                }
            }
        }
    }

    void gainHealth()
    {
        this.health = Math.min(max_health,this.health+1);
    }

    void setMaxHealth(int health)
    {
        int difference = health - this.max_health;
        this.max_health = health;
        this.health += difference;
    }

    int getHealth()
    {
        return health;
    }

    private double getPercentHealth()
    {
        return ((double) health) / ((double) max_health);
    }

    public void takeDamage(int damage, Sprite damage_dealer)
    {
        health = Math.max(health - damage, 0);
        //If self is dead...
        if(health<=0)
        {   //If self is dead and damage_dealer is a Ship, give experience to other
            if(damage_dealer instanceof Ship)
            {
                ((Ship)damage_dealer).giveXP(this.getXpReward());
            }
            //If self is dead and damage_dealer is a Shot, give experience to the shooter
            else if(damage_dealer instanceof Shot)
            {
                ((Shot)damage_dealer).giveXpToShooter(this.getXpReward());
            }
        }
    }

    /* Handle a collision between this sprite and other.
     * This prevents enemies from phasing through each other or the
     * player by shoving each other out of the same space.*/
    public void handleCollision(SpritePhysics other)
    {   //If other is this sprite's own bullet, just return
        if(other instanceof Shot && this == ((Shot)other).getShooter()){return;}
        //Push other out
        double impulse = 10;
        double angle = this.angleToSprite(other);
        other.changeVelocity(Math.cos(angle)*impulse,
                Math.sin(angle)*impulse);
        //If this is an experience brick or a bullet, then push self out
        //because the other won't do it.
        /*if(this instanceof ExperienceBrick || this instanceof Shot)
        {   //Push self out
            this.changeVelocity(-Math.cos(angle) * impulse, -Math.sin(angle) * impulse);
        }*/ //TODO test not having this.

        //Check for recent damage source. If other is not the most recent
        //damage source or the redamage cooldown is less than zero, then
        //deal damage to other
        if(other != recent_damage_source || redamage_cooldown<0)
        {   //Damage the other unless self and other are both experience
            //bricks which should push each other out, but not hurt each
            //other
            if(!(this instanceof ExperienceBrick && other instanceof ExperienceBrick))
            {
                other.takeDamage(body_damage, this);
            }
            //If this is an experience brick, then damage self
            /*if(this instanceof ExperienceBrick)
            {   //Damage the self
                this.takeDamage(((SpriteTopDown)other).getBodyDamage(), other);
            }*/ //TODO test not having this.
            //Reset recent damage source
            redamage_cooldown = redamage_cooldown_reset;
            recent_damage_source = (SpriteTopDown)other;
        }
    }

    public boolean getRemoveMe()
    {
        return super.getRemoveMe() || this.getHealth() <= 0;
    }

    private int getBodyDamage()
    {
        return this.body_damage;
    }
    void setBodyDamage(int amount)
    {
        this.body_damage = amount;
    }
    int getXpReward(){ return xp_reward; }
    void giveXP(int amount){
        this.xp += amount;
        if(amount>0)
        {
            this.cumulative_xp += amount;
        }
    }
    int getXP(){ return this.xp; }
    int getCumulativeXP(){ return this.cumulative_xp; }
}