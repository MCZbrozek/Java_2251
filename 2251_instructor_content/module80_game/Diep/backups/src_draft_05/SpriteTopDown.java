import java.awt.*;
import java.util.ArrayList;

/**Created by nealh_000 on 5/22/2017.
 * This class implements some functionality common to all sprites in
 * this top down game. */
public abstract class SpriteTopDown extends SpriteCircular
{
    Color color = Color.WHITE;

    Board board_reference;

    //Some sprites will keep track of the nearest Ship, nearest experience brick,
    // and nearest shot
    Ship nearest_ship = null;
    Shot nearest_shot = null;
    ExperienceBrick nearest_xp = null;

    //Determine the shape to draw this sprite.
    //Shape options are in constants.
    private int shape = 0;

    //Not all sprites move, but those that do need a speed
    double speed;

    //Health and max health of this sprite.
    private int health = 0;
    private int max_health = 1;

    //Whether or not to draw the health bar and direction indicator
    //for this sprite. For example, Shots have neither a health bar
    //nor a direction indicator.
    boolean no_health_draw = false;
    boolean no_direction_draw = false;

    private int xp=0;//Experience Points
    private int cumulative_xp=0;
    //Experience given for killing this sprite:
    private int xp_reward;

    //Damage dealt by collisions with this body
    private int body_damage = 1;

    //Delay between damage taking from the same sprite in seconds
    private double redamage_cooldown = 0;
    private SpriteTopDown recent_damage_source = null;

    public SpriteTopDown(double x, double y, int health, double angle,
                         double radius, int xp_reward,
                         int shape,
                         int i_am_bitmask, int i_hit_bitmask,
                         Board board_reference)
    {
        super(x, y, angle, radius, "", i_am_bitmask, i_hit_bitmask);
        this.health = health;
        this.max_health = health;
        this.xp_reward = xp_reward;
        this.shape = shape;
        this.board_reference = board_reference;
    }

    void moveToward(double elapsed_seconds, double x, double y)
    {
        if(y < this.getYCenter())
        {	//up
            this.changeVelocity(0, -speed*elapsed_seconds);
        }else
        {   //down
            this.changeVelocity(0, speed*elapsed_seconds);
        }
        if(x < this.getXCenter())
        {	//left
            this.changeVelocity(-speed*elapsed_seconds, 0);
        }else
        {   //right
            this.changeVelocity(speed*elapsed_seconds, 0);
        }
    }

    void moveAwayFrom(double elapsed_seconds, double x, double y)
    {
        if(y < this.getYCenter())
        {	//down
            this.changeVelocity(0, speed*elapsed_seconds);
        }else
        {   //up
            this.changeVelocity(0, -speed*elapsed_seconds);
        }
        if(x < this.getXCenter())
        {	//right
            this.changeVelocity(speed*elapsed_seconds, 0);
        }else
        {   //left
            this.changeVelocity(-speed*elapsed_seconds, 0);
        }
    }

    public void update(double elapsed_seconds)
    {
        //Apply friction
        this.changeVelocity(this.getdx() * Constants.friction * elapsed_seconds, this.getdy() * Constants.friction * elapsed_seconds);
        //Move
        this.move(elapsed_seconds);
        //Keep this sprite in bounds
        if (this.getX() < 0)
        {
            this.changeVelocity(Constants.inbounds_impulse * elapsed_seconds, 0);
        }
        if (this.getX() + this.getCollisionRadius() * 2 > Constants.boundary_right)
        {
            this.changeVelocity(-Constants.inbounds_impulse * elapsed_seconds, 0);
        }
        if (this.getY() < 0)
        {
            this.changeVelocity(0, Constants.inbounds_impulse * elapsed_seconds);
        }
        if (this.getY() + this.getCollisionRadius() * 2 > Constants.boundary_down)
        {
            this.changeVelocity(0, -Constants.inbounds_impulse * elapsed_seconds);
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
            {   //Draw sprite
                g2d.setColor(color);
                int radius = (int)this.getCollisionRadius();
                if(shape==Constants.SHAPE_CIRCLE)
                {
                    g2d.fillOval(draw_x, draw_y, radius*2, radius*2);
                }
                else if(shape==Constants.SHAPE_RECT)
                {
                    g2d.fillRect(draw_x, draw_y, radius*2, radius*2);
                }
                else //if(shape==Constants.SHAPE_TRIANGLE)
                {
                    int[] x_points = {draw_x+radius, draw_x, draw_x+radius*2};
                    int[] y_points = {draw_y, draw_y+radius*2, draw_y+radius*2};
                    g2d.fillPolygon(x_points,y_points,3);
                }
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

    void updateTarget()
    {   //Get all the sprites
        ArrayList<SpritePhysics> nearby_sprites = board_reference.getSpriteList();
        //Find the nearest sprites in the list
        double closest_distance_ship = Double.MAX_VALUE;
        double closest_distance_xp = Double.MAX_VALUE;
        double closest_distance_shot = Double.MAX_VALUE;
        for (SpritePhysics temp_sprite : nearby_sprites)
        {
            //Avoid null pointer by checking if the current variable is null first
            //If current brick is a high experience brick and is within 350 pixels
            //then don't change it
            //If current brick is a medium experience brick and is within 250 pixels
            //then don't change it
            //If current brick is within 150 pixels and is damaged
            //then don't change it
            //else check to see if there is a closer brick
            if (temp_sprite instanceof ExperienceBrick &&
                    (nearest_xp == null ||
                            (!(nearest_xp.getXpReward() == 10 && this.distanceTo(nearest_xp) < 350)
                                    && !(nearest_xp.getXpReward() == 2 && this.distanceTo(nearest_xp) < 250)
                                    && !(nearest_xp.getPercentHealth() < 1.0 && this.distanceTo(nearest_xp) < 150)
                                    && this.distanceTo(temp_sprite) < closest_distance_xp)))
            {
                closest_distance_xp = this.distanceTo(temp_sprite);
                nearest_xp = (ExperienceBrick) temp_sprite;
            }
            else if(temp_sprite instanceof Shot && ((Shot)temp_sprite).getShooter()!=this
                    && this.distanceTo(temp_sprite) < closest_distance_shot)
            {
                closest_distance_shot = this.distanceTo(temp_sprite);
                nearest_shot = (Shot) temp_sprite;
            }
            else if(temp_sprite instanceof Ship && temp_sprite != this
                    && this.distanceTo(temp_sprite) < closest_distance_ship)
            {
                closest_distance_ship = this.distanceTo(temp_sprite);
                nearest_ship = (Ship) temp_sprite;
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
    void setHealthMax(){health = max_health;}

    double getPercentHealth()
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
        double angle = this.angleToSprite(other);
        other.changeVelocity(Math.cos(angle)*Constants.shove_out_impulse,
                Math.sin(angle)*Constants.shove_out_impulse);
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
            //Reset recent damage source
            redamage_cooldown = Constants.redamage_cooldown_reset;
            recent_damage_source = (SpriteTopDown)other;
        }
    }

    public boolean getRemoveMe()
    {
        return super.getRemoveMe() || this.getHealth() <= 0;
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