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
    //nearest shot, nearest powerup
    Ship nearest_ship = null;
    Shot nearest_shot = null;
    ExperienceBrick nearest_xp = null;
    PowerUp nearest_powerup = null;

    //Usually out of 100, making this over 100 means it takes less than 1 second to lose all momentum.
    double friction = -1.1;
    //Force with which objects shove out from each other when they overlap
    double shove_out_impulse = 8.0;

    //Determine the shape to draw this sprite.
    //Shape options are in constants.
    int shape = 0;

    //Not all sprites move, but those that do need a speed
    double speed;

    //Used by mimic and cloak ships to put on a burst of speed temporarily.
    private double stamina = 0;

    //Health and max health of this sprite.
    int health = 0;
    int max_health = 1;

    //Whether or not to draw the health bar and direction indicator
    //for this sprite. For example, Shots have neither a health bar
    //nor a direction indicator.
    boolean no_health_draw = false;
    boolean no_direction_draw = false;

    private int xp=0;//Experience Points
    //Experience given for killing this sprite:
    private int xp_reward;

    //Damage dealt by collisions with this body
    int body_damage = 1;

    //Delay between damage taking from the same sprite in seconds
    private double redamage_cooldown = 0;
    private SpriteTopDown recent_damage_source = null;

    public SpriteTopDown(double x, double y, int health, double angle,
                         double radius, int xp_reward,
                         int shape,
                         int i_am_bitmask, int i_hit_bitmask,
                         Board board_reference)
    {
        super(x, y, angle, radius,
                "",
                i_am_bitmask, i_hit_bitmask);
        this.health = health;
        this.max_health = health;
        this.xp_reward = xp_reward;
        this.shape = shape;
        this.board_reference = board_reference;
    }

    public SpriteTopDown(double x, double y, int health, double angle,
                         double radius, int xp_reward,
                         int shape,
                         int i_am_bitmask, int i_hit_bitmask,
                         int body_damage,
                         Board board_reference)
    {
        super(x, y, angle, radius,
                "",
                i_am_bitmask, i_hit_bitmask);
        this.health = health;
        this.max_health = health;
        this.xp_reward = xp_reward;
        this.shape = shape;
        this.body_damage = body_damage;
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
        this.changeVelocity(this.getdx() * friction * elapsed_seconds, this.getdy() * friction * elapsed_seconds);
        //Move
        //Cloak and mimic ships gain a burst of speed when they are not stationary
        //and they have positive stamina.
        if(stamina > 0 && //stamina up
                Math.abs(this.getdx())+Math.abs(this.getdy())>10) //not stationary
        {    //Used by mimic and cloak ships to put on a burst of speed temporarily.
            this.move(elapsed_seconds*3);
            stamina -= elapsed_seconds;
        }
        else
        {   //All other ships move normally.
            this.move(elapsed_seconds);
            //Cloak and mimic ships recover stamina while stationary.
            //Up to 1.5 seconds of burst speed.
            if((this.shape==Constants.SHAPE_MIMIC || this.shape==Constants.SHAPE_CLOAK) &&
                    Math.abs(this.getdx())+Math.abs(this.getdy())<10 &&
                    stamina<1.5)
            {
                stamina += elapsed_seconds;
            }
        }
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

    /** Used for drawing the "guns" on the ships. */
    private void drawRect(Graphics2D g2d, int draw_x, int draw_y,
                          double radius, double angle, int side_length,
                          int skew, int length)
    {
        double adj = Math.cos(angle);
        double opp = Math.sin(angle);
        int[] x_points2 = {
                (int) (draw_x + radius + 30 * adj + side_length * Math.cos(angle + Math.PI / 2)),
                (int) (draw_x + radius + 30 * adj + side_length * Math.cos(angle - Math.PI / 2)),
                (int) (draw_x + radius + (30+length) * adj + (side_length+skew) * Math.cos(angle - Math.PI / 2)),
                (int) (draw_x + radius + (30+length) * adj + (side_length+skew) * Math.cos(angle + Math.PI / 2))};
        int[] y_points2 = {
                (int) (draw_y + radius + 30 * opp + side_length * Math.sin(angle + Math.PI / 2)),
                (int) (draw_y + radius + 30 * opp + side_length * Math.sin(angle - Math.PI / 2)),
                (int) (draw_y + radius + (30+length) * opp + (side_length+skew) * Math.sin(angle - Math.PI / 2)),
                (int) (draw_y + radius + (30+length) * opp + (side_length+skew) * Math.sin(angle + Math.PI / 2))};
        g2d.fillPolygon(x_points2, y_points2, 4);
    }

    /**
     * Override parent class's draw.
     */
    public void draw(Board board, Graphics g, int offset_x, int offset_y)
    {
        //Determine whether or not to draw the sprite
        if (this.isVisible())
        {
            int draw_x = (int)(this.getX() + offset_x);
            int draw_y = (int)(this.getY() + offset_y);
            Graphics2D g2d = (Graphics2D) g;
            //Update the on_screen status of this sprite.
            this.setIsOnScreen(draw_x, draw_y);
            if (this.getIsOnScreen())
            {   //Draw sprite
                g2d.setColor(color);
                int radius = (int)this.getCollisionRadius();
                if(shape==Constants.SHAPE_CIRCLE ||
                        shape == Constants.SHAPE_MACHINE_GUN ||
                        shape == Constants.SHAPE_FLANKER ||
                        shape == Constants.SHAPE_SNIPER ||
                        shape == Constants.SHAPE_TWIN ||
                        shape == Constants.SHAPE_CROSS ||
                        shape == Constants.SHAPE_All_DIRECTIONS ||
                        shape == Constants.SHAPE_EXPLODER ||
                        shape == Constants.SHAPE_GRAVITY ||
                        shape == Constants.SHAPE_CANNON ||
                        shape == Constants.SHAPE_SEEKER ||
                        shape == Constants.SHAPE_LASER ||
                        shape == Constants.SHAPE_RICOCHET ||
                        shape == Constants.SHAPE_RICOCHET_SHOT)
                {
                    if(shape == Constants.SHAPE_EXPLODER)
                    {   //Draw 2 rings around ship.
                        g2d.setColor(Color.RED);
                        g2d.fillOval(draw_x-6, draw_y-6, radius*2+12, radius*2+12);
                        g2d.setColor(Color.ORANGE);
                        g2d.fillOval(draw_x-3, draw_y-3, radius*2+6, radius*2+6);
                        g2d.setColor(color);
                    }
                    g2d.fillOval(draw_x, draw_y, radius*2, radius*2);
                }
                else if(shape==Constants.SHAPE_RECT || shape==Constants.SHAPE_MIMIC)
                {
                    g2d.fillRect(draw_x, draw_y, radius*2, radius*2);
                }
                else if(shape==Constants.SHAPE_TRIANGLE)
                {
                    int[] x_points = {draw_x+radius, draw_x, draw_x+radius*2};
                    int[] y_points = {draw_y, draw_y+radius*2, draw_y+radius*2};
                    g2d.fillPolygon(x_points,y_points,3);
                }
                else if(shape==Constants.SHAPE_IMAGE)
                {
                    g2d.drawImage(this.getImage(), draw_x, draw_y, board);
                }
                else if(shape == Constants.SHAPE_CARRIER)
                {   //Hexagon
                    int spikes = 6;
                    int[] x_points = new int[spikes];
                    int[] y_points = new int[spikes];
                    for (int i = 0; i < spikes; i++)
                    {
                        x_points[i] = (int)(draw_x+radius+Math.cos(i*2*Math.PI/spikes)*radius);
                        y_points[i] = (int)(draw_y+radius+Math.sin(i*2*Math.PI/spikes)*radius);
                    }
                    g2d.fillPolygon(x_points,y_points,spikes);
                }
                else //if(shape == Constants.SHAPE_RAMMER || shape == Constants.SHAPE_CLOAK)
                {
                    int spikes = 32;
                    int[] x_points = new int[spikes];
                    int[] y_points = new int[spikes];
                    boolean alternate = true;
                    double spike_height;
                    for (int i = 0; i < spikes; i++)
                    {
                        if(alternate){spike_height = 20;}
                        else{spike_height = 0;}
                        x_points[i] = (int)(draw_x+radius+Math.cos(i*2*Math.PI/spikes)*(radius+spike_height));
                        y_points[i] = (int)(draw_y+radius+Math.sin(i*2*Math.PI/spikes)*(radius+spike_height));
                        alternate = !alternate;
                    }
                    g2d.fillPolygon(x_points,y_points,spikes);
                }
                //Don't draw the direction indicator if no_direction_draw is set.
                if(!no_direction_draw)
                {
                    //Draw orientation circle
                    if(shape == Constants.SHAPE_MACHINE_GUN)
                    {   //Draw new forward gun
                        g2d.setColor(Color.gray);
                        this.drawRect(g2d, draw_x, draw_y, radius, this.getAngle(), 15, 5, 30);
                    }
                    else if(shape == Constants.SHAPE_FLANKER || shape == Constants.SHAPE_CROSS)
                    {   //Draw forward gun
                        g2d.setColor(Color.gray);
                        this.drawRect(g2d, draw_x, draw_y, radius, this.getAngle(), 15, 0, 30);
                        //Draw rear gun
                        this.drawRect(g2d, draw_x, draw_y, radius, this.getAngle()+Math.PI, 15, 0, 30);
                        //If this is a cross ship, draw two more guns on each side
                        if(shape == Constants.SHAPE_CROSS)
                        {
                            this.drawRect(g2d, draw_x, draw_y, radius, this.getAngle()+Math.PI/2, 15, 0, 30);
                            this.drawRect(g2d, draw_x, draw_y, radius, this.getAngle()-Math.PI/2, 15, 0, 30);
                        }
                    }
                    else if(shape == Constants.SHAPE_All_DIRECTIONS)
                    {   //Draw guns
                        g2d.setColor(Color.gray);
                        for (int i = 0; i < Constants.direction_count; i++)
                        {
                            this.drawRect(g2d, draw_x, draw_y, radius,
                                    this.getAngle()+i*2*Math.PI/Constants.direction_count,
                                    15, 0, 30);
                        }
                    }
                    else if(shape == Constants.SHAPE_SNIPER || shape == Constants.SHAPE_SEEKER)
                    {   //Draw long-barreled forward gun
                        g2d.setColor(Color.gray);
                        this.drawRect(g2d, draw_x, draw_y, radius, this.getAngle(),
                                15, 0, 50);
                    }
                    else if(shape == Constants.SHAPE_CANNON)
                    {   //Draw bigger long-barreled forward gun
                        g2d.setColor(Color.gray);
                        this.drawRect(g2d, draw_x, draw_y, radius, this.getAngle(),
                                22, 0, 80);
                    }
                    else if(shape == Constants.SHAPE_LASER)
                    {   //Draw long narrow forward gun
                        g2d.setColor(Color.gray);
                        this.drawRect(g2d, draw_x, draw_y, radius, this.getAngle(),
                                10, 0, 80);
                    }
                    else if(shape == Constants.SHAPE_TWIN)
                    {   //Draw two forward guns side by side
                        g2d.setColor(Color.gray);
                        double adj = Math.cos(this.getAngle());
                        double opp = Math.sin(this.getAngle());
                        int a = 50; //near side of barrel
                        int b = 30; //far side of barrel
                        int c = 5; //inner side of barrel
                        int d = 32; //outer side of barrel
                        //Draw one gun right of center
                        int[] x_points = {
                                (int) (draw_x + radius +a*adj + d*Math.cos(this.getAngle() + Math.PI / 2)),
                                (int) (draw_x + radius +a*adj - c*Math.cos(this.getAngle() - Math.PI / 2)),
                                (int) (draw_x + radius +b*adj - c*Math.cos(this.getAngle() - Math.PI / 2)),
                                (int) (draw_x + radius +b*adj + d*Math.cos(this.getAngle() + Math.PI / 2))};
                        int[] y_points = {
                                (int) (draw_y + radius +a*opp + d*Math.sin(this.getAngle() + Math.PI / 2)),
                                (int) (draw_y + radius +a*opp - c*Math.sin(this.getAngle() - Math.PI / 2)),
                                (int) (draw_y + radius +b*opp - c*Math.sin(this.getAngle() - Math.PI / 2)),
                                (int) (draw_y + radius +b*opp + d*Math.sin(this.getAngle() + Math.PI / 2))};
                        g2d.fillPolygon(x_points, y_points, 4);
                        //Draw another gun left of center
                        int[] x_points2 = {
                                (int) (draw_x + radius +a*adj - c*Math.cos(this.getAngle() + Math.PI / 2)),
                                (int) (draw_x + radius +a*adj + d*Math.cos(this.getAngle() - Math.PI / 2)),
                                (int) (draw_x + radius +b*adj + d*Math.cos(this.getAngle() - Math.PI / 2)),
                                (int) (draw_x + radius +b*adj - c*Math.cos(this.getAngle() + Math.PI / 2))};
                        int[] y_points2 = {
                                (int) (draw_y + radius +a*opp - c*Math.sin(this.getAngle() + Math.PI / 2)),
                                (int) (draw_y + radius +a*opp + d*Math.sin(this.getAngle() - Math.PI / 2)),
                                (int) (draw_y + radius +b*opp + d*Math.sin(this.getAngle() - Math.PI / 2)),
                                (int) (draw_y + radius +b*opp - c*Math.sin(this.getAngle() + Math.PI / 2))};
                        g2d.fillPolygon(x_points2, y_points2, 4);
                    }
                    else
                    {
                        double heading_adjust = 40;
                        double dx = Math.cos(this.getAngle()) * heading_adjust;
                        double dy = Math.sin(this.getAngle()) * heading_adjust;
                        double heading_radius = this.getCollisionRadius() / 3;
                        g2d.fillOval((int) (draw_x + this.getCollisionRadius() + dx - heading_radius),
                                (int) (draw_y + this.getCollisionRadius() + dy - heading_radius),
                                (int) heading_radius * 2, //width
                                (int) heading_radius * 2); //height
                    }
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
        double closest_distance_powerup = Double.MAX_VALUE;
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
            //The line of code below: nearest_xp.getHealth()<=0 ||
            //is super important to prevent ships from getting stuck being obsessed with a
            //dead brick.
            if (temp_sprite instanceof ExperienceBrick &&
                    (       nearest_xp == null ||
                            nearest_xp.getHealth()<=0 ||
                            (          !(nearest_xp.getXpReward() == 10 && this.distanceTo(nearest_xp) < 350)
                                    && !(nearest_xp.getXpReward() == 2 && this.distanceTo(nearest_xp) < 250)
                                    && !(nearest_xp.getPercentHealth() < 1.0 && this.distanceTo(nearest_xp) < 150)
                                    && this.distanceTo(temp_sprite) < closest_distance_xp
                            )
                    ))
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
                    && this.distanceTo(temp_sprite) < closest_distance_ship
                    && !((Ship) temp_sprite).cloaked())
            {
                closest_distance_ship = this.distanceTo(temp_sprite);
                nearest_ship = (Ship) temp_sprite;
            }
            else if(temp_sprite instanceof PowerUp &&
                    (nearest_powerup==null || this.distanceTo(nearest_powerup) < closest_distance_powerup))
            {
                closest_distance_powerup = this.distanceTo(temp_sprite);
                nearest_powerup = (PowerUp) temp_sprite;
            }
        }
        //Fix a bug where a captured powerup does not get unset
        if(nearest_powerup!=null && nearest_powerup.getRemoveMe())
        {
            nearest_powerup = null;
        }
        //Same bug as above is possible for any sprite so here we fix it
        //for the others
        if(nearest_ship!=null && nearest_ship.getRemoveMe())
        {
            nearest_ship = null;
        }
        if(nearest_shot!=null && nearest_shot.getRemoveMe())
        {
            nearest_shot = null;
        }
        if(nearest_xp!=null && nearest_xp.getRemoveMe())
        {
            nearest_xp = null;
        }
    }

    void gainHealth()
    {
        //Dead players can't gain health
        if(this.health > 0)
        {
            this.health = Math.min(max_health,this.health+1);
        }
    }

    void setMaxHealth(int health)
    {
        int difference = health - this.max_health;
        this.max_health = health;
        //Dead players can't gain health
        if(this.health > 0)
        {
            this.health += difference;
        }
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
        other.changeVelocity(Math.cos(angle)*shove_out_impulse,
                Math.sin(angle)*shove_out_impulse);
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
        if(this instanceof ExperienceBrick)
        {
            return super.getRemoveMe() || (this.getHealth() <= 0 && !((ExperienceBrick)this).regenerate);
        }
        else
        {
            return super.getRemoveMe() || this.getHealth() <= 0;
        }
    }

    void setBodyDamage(int amount)
    {
        this.body_damage = amount;
    }
    int getXpReward(){ return xp_reward; }
    void giveXP(int amount){ this.xp += amount; }
    int getXP(){ return this.xp; }

    /** The following is used when a ship dies. */
    void reduceXP(double percent_reduction)
    {
        this.xp = (int)(this.xp * (1-percent_reduction));
    }

    //returns this sprite's current stamina level
    double getStamina(){return stamina;}
}