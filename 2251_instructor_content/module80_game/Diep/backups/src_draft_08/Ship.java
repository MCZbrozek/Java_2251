import java.awt.*;
import java.util.ArrayList;

public class Ship extends SpriteTopDown
{
    private int bullet_damage;
    private double bullet_speed; //Pixels per second
    private double bullet_longevity; //Seconds
    private double refire_rate; //Seconds
    private int bullet_health;

    private int level = 0;
    private int speed_level = 0;
    private int bullet_damage_level = 0;
    private int bullet_speed_level = 0;
    private int bullet_longevity_level = 0;
    private int refire_rate_level = 0;
    private int health_level = 0;
    private int regen_level = 0;
    private int collision_damage_level = 0;
    private int penetration_level = 0;

    private double firing_cooldown = 0;

    private int bullet_is_bitmask=0;
    private int bullet_hits_bitmask=0;

    private double regen_cooldown = 0;
    private double regen_cooldown_reset = 2.0;

    private String name;

    //Variable for the speed up powerup
    private double original_speed;
    //variable for the friction down powerup
    private double original_friction;
    //variable for the shove out powerup
    private double original_shove_out;
    //variables for the goliath powerup
    private double original_regen;
    private double original_radius;
    //variable for the mimic powerup
    private Color original_color;
    //variable for the blackhole powerup
    private int original_body_damage;

    //Variables for the triple shot power up
    //How many triple shots are left to fire
    int triple_shot_count = 0;
    private double triple_shot_cooldown = 0;

    private double[] cooldowns = new double[Constants.NUM_POWERUPS];

    public Ship(double x, double y,
                String name,
                double collision_radius,
                Color color,
                int i_am_bitmask,
                int i_hit_bitmask,
                int bullet_is_bitmask,
                int bullet_hits_bitmask,
                Board board)
    {
        super(x, y,
                1,//This doesn't matter because it gets reset for ships
                0, //initial heading
                collision_radius,
                0,
                Constants.SHAPE_CIRCLE,
                i_am_bitmask,
                i_hit_bitmask,
                board);
        this.name = name;
        this.bullet_is_bitmask = bullet_is_bitmask;
        this.bullet_hits_bitmask = bullet_hits_bitmask;
        this.setAttributes();
        this.original_friction = friction;
        this.original_shove_out = shove_out_impulse;
        this.original_radius = collision_radius;
        this.color = color;
        this.original_color = color;
    }

    private void setAttributes()
    {
        this.speed = Constants.speed_levels[speed_level];
        this.bullet_damage = Constants.bullet_damage_levels[bullet_damage_level];
        this.bullet_speed = Constants.bullet_speed_levels[bullet_speed_level];
        this.refire_rate = Constants.refire_rate_levels[refire_rate_level];
        this.bullet_longevity = Constants.bullet_longevity_levels[bullet_longevity_level];
        this.setMaxHealth(Constants.health_levels[health_level]);
        this.regen_cooldown_reset = Constants.regen_levels[regen_level];
        this.setBodyDamage(Constants.collision_damage_levels[collision_damage_level]);
        this.bullet_health = Constants.bullet_health_levels[penetration_level];
        this.original_speed = speed;
        this.original_regen = regen_cooldown_reset;
        this.original_body_damage = body_damage;
    }

    public void setAttributes(double speed, int bullet_damage, double bullet_speed,
                              double refire_rate, double bullet_longevity,
                              int health, double regen_cooldown_reset, int body_damage,
                              int bullet_health)
    {
        this.speed = speed;
        this.bullet_damage = bullet_damage;
        this.bullet_speed = bullet_speed;
        this.refire_rate = refire_rate;
        this.bullet_longevity = bullet_longevity;
        this.setMaxHealth(health);
        this.regen_cooldown_reset = regen_cooldown_reset;
        this.setBodyDamage(body_damage);
        this.bullet_health = bullet_health;
        this.original_speed = speed;
        this.original_regen = regen_cooldown_reset;
        this.original_body_damage = body_damage;
    }

    private void shoot()
    {
        Shot temp_shot = new Shot(
                bullet_is_bitmask,
                bullet_hits_bitmask,
                this,
                this.getAngle(),
                bullet_longevity,
                bullet_damage,
                bullet_speed,
                bullet_health,
                cooldowns[Constants.POW_EXPLODING_BULLETS]>0 || Constants.bullet_explodes_default,
                board_reference);
        board_reference.addSprite(temp_shot);
        if(cooldowns[Constants.POW_SPREAD_SHOT]>0)
        {
            temp_shot = new Shot(
                    bullet_is_bitmask,
                    bullet_hits_bitmask,
                    this,
                    this.getAngle()+Math.PI/6.0,
                    bullet_longevity,
                    bullet_damage,
                    bullet_speed,
                    bullet_health,
                    cooldowns[Constants.POW_EXPLODING_BULLETS]>0 || Constants.bullet_explodes_default,
                    board_reference);
            board_reference.addSprite(temp_shot);
            temp_shot = new Shot(
                    bullet_is_bitmask,
                    bullet_hits_bitmask,
                    this,
                    this.getAngle()-Math.PI/6.0,
                    bullet_longevity,
                    bullet_damage,
                    bullet_speed,
                    bullet_health,
                    cooldowns[Constants.POW_EXPLODING_BULLETS]>0,
                    board_reference);
            board_reference.addSprite(temp_shot);
        }
        //Shove shooter back from recoil
        this.changeVelocity(
                -Math.cos(this.getAngle())*Constants.recoil,
                -Math.sin(this.getAngle())*Constants.recoil);
    }

    //Perform default attack if it has cooled down
    void attack()
    {
        if(firing_cooldown<=0)
        {
            firing_cooldown = this.refire_rate;
            this.shoot();
            //Check for the triple shot powerup
            if(cooldowns[Constants.POW_TRIPLE_SHOT] > 0)
            {
                triple_shot_count = 2;
                triple_shot_cooldown = 0.1;
            }
        }
        //Check for the firing of triple shots
        else if(triple_shot_cooldown<0 && triple_shot_count > 0)
        {
            triple_shot_count--;
            triple_shot_cooldown = 0.1;
            this.shoot();
        }
    }

    public void update(double elapsed_seconds)
    {
        super.update(elapsed_seconds);
        //Update Level
        while (level < Constants.xp_per_level.length && this.getXP() >= Constants.xp_per_level[level])
        {
            //Reduce xp by current level amount and increase level
            this.giveXP(-Constants.xp_per_level[level]);
            this.level++;
        }
        //Reduce firing cooldown
        firing_cooldown -= elapsed_seconds;
        //Health regen
        regen_cooldown -= elapsed_seconds;
        if (regen_cooldown < 0)
        {
            regen_cooldown += regen_cooldown_reset;
            this.gainHealth();
        }
        triple_shot_cooldown -= elapsed_seconds;
        //cooldown all the powerups
        for (int i = 0; i < cooldowns.length; i++)
        {
            cooldowns[i] -= elapsed_seconds;
        }
        //Reset the speed if the powerup is off
        if (original_speed != speed && cooldowns[Constants.POW_SPEED_UP] < 0)
        {
            this.speed = original_speed;
        }
        //Reset the friction if the powerup is off
        if (original_friction != friction && cooldowns[Constants.POW_NO_FRICTION] < 0)
        {
            this.friction = original_friction;
        }
        //Reset the shove out if the powerup is off
        if (original_shove_out != shove_out_impulse && cooldowns[Constants.POW_SHOVE_OUT] < 0)
        {
            this.shove_out_impulse = original_shove_out;
        }
        //Reset the speed, regen, and radius if the goliath powerup is off
        if (original_radius != this.getCollisionRadius() && cooldowns[Constants.POW_GOLIATH] < 0)
        {
            this.speed = original_speed;
            this.setCollisionRadius(original_radius);
            this.regen_cooldown_reset = original_regen;
        }
        //Reset the look of the ship if mimic is off
        if (this.no_direction_draw && cooldowns[Constants.POW_MIMIC]<0)
        {
            this.setCollisionRadius(20);
            this.color = original_color;
            this.shape = Constants.SHAPE_CIRCLE;
            this.no_health_draw = false;
            this.no_direction_draw = false;
        }
        //Reset the blackhole status
        if (this.original_body_damage != body_damage && cooldowns[Constants.POW_BLACKHOLE]<0)
        {
            this.setBodyDamage(original_body_damage);
        }
        //If blackhole is on, pull in nearby sprites
        if (cooldowns[Constants.POW_BLACKHOLE]>0)
        {   //Pull in other sprites with force based on distance
            double gravity_range = 800;
            ArrayList<SpritePhysics> in_range = board_reference.getAllWithinDistance(this.getXCenter(), this.getYCenter(), gravity_range);
            //Pull sprites in
            double impulse = -1000; //negative makes it a pulling force
            double distance, angle;
            for (SpritePhysics s:in_range)
            {
                distance = this.distanceTo(s);
                if(distance>0)
                {
                    angle = this.angleToSprite(s);
                    s.changeVelocity(Math.cos(angle) * impulse / distance,
                            Math.sin(angle) * impulse / distance);
                }
            }

        }
    }

    int getLevel(){return level;}

    double getPercentExperience()
    {
        //If beyond the top level, just return 0
        if(level >= Constants.xp_per_level.length-1){return 0;}
        //Return percentage of current level
        return (double)this.getXP() / (double)Constants.xp_per_level[level];
    }

    int availableLevelUpPoints()
    {
        return level - (speed_level+bullet_damage_level+bullet_speed_level+refire_rate_level+bullet_longevity_level+health_level+regen_level+collision_damage_level+penetration_level);
    }

    void levelUpAttribute(String name)
    {
        switch (name)
        {
            case "Speed":
                if(speed_level<Constants.speed_levels.length-1){speed_level++;}
                break;
            case "Bullet Damage":
                if(bullet_damage_level<Constants.bullet_damage_levels.length-1){bullet_damage_level++;}
                break;
            case "Bullet Speed":
                if(bullet_speed_level<Constants.bullet_speed_levels.length-1){bullet_speed_level++;}
                break;
            case "Bullet Longevity":
                if(bullet_longevity_level<Constants.bullet_longevity_levels.length-1){bullet_longevity_level++;}
                break;
            case "Refire Rate":
                if(refire_rate_level<Constants.refire_rate_levels.length-1){refire_rate_level++;}
                break;
            case "Health":
                if(health_level<Constants.health_levels.length-1){health_level++;}
                break;
            case "Health Regen":
                if(regen_level<Constants.regen_levels.length-1){regen_level++;}
                break;
            case "Collision Damage":
                if(collision_damage_level<Constants.collision_damage_levels.length-1){collision_damage_level++;}
                break;
            case "Penetration":
                if(penetration_level<Constants.bullet_health_levels.length-1){penetration_level++;}
                break;
            default:
                System.out.println("Error in Player.levelUpAttribute('"+name+"')");
                System.exit(0);
        }
        this.setAttributes();
    }

    int getAttributeLevel(String name)
    {
        switch (name)
        {
            case "Speed":
                return speed_level;
            case "Bullet Damage":
                return bullet_damage_level;
            case "Bullet Speed":
                return bullet_speed_level;
            case "Bullet Longevity":
                return bullet_longevity_level;
            case "Refire Rate":
                return refire_rate_level;
            case "Health":
                return health_level;
            case "Health Regen":
                return regen_level;
            case "Collision Damage":
                return collision_damage_level;
            case "Penetration":
                return penetration_level;
            default:
                System.out.println("Error in Player.getAttributeLevel('"+name+"')");
                System.exit(0);
        }
        return 0;
    }

    String getName(){return name;}

    //Reward for killing a ship is the ship's level times 4
    int getXpReward()
    {
        return this.level*4;
    }

    void activatePowerup(int powerup_id)
    {
        //For now, 10 second duration on all powerups
        cooldowns[powerup_id] = 10;
        //Check for speed up
        if(powerup_id == Constants.POW_SPEED_UP)
        {
            this.speed = this.speed*2;
        }
        //Check for friction off
        else if(powerup_id == Constants.POW_NO_FRICTION)
        {
            this.friction = 0;
        }
        //Check for shove out
        else if(powerup_id == Constants.POW_SHOVE_OUT)
        {
            this.shove_out_impulse = 1000;
        }
        //Check for Goliath
        else if(powerup_id == Constants.POW_GOLIATH)
        {
            this.speed = this.speed/2;
            double temp_x = this.getXCenter();
            double temp_y = this.getYCenter();
            this.setCollisionRadius(200);
            this.moveToCenter(temp_x, temp_y);
            this.regen_cooldown_reset = Math.min(0.5, this.regen_cooldown_reset/2.0);
        }
        //Check for mimic
        else if(powerup_id == Constants.POW_MIMIC)
        {
            this.setCollisionRadius(25);
            this.color = Color.CYAN;
            this.shape = Constants.SHAPE_RECT;
            this.no_health_draw = true;
            this.no_direction_draw = true;
        }
        //check for blackhole
        else if(powerup_id == Constants.POW_BLACKHOLE)
        {
            this.setHealthMax();
            this.setBodyDamage(Math.max(5,body_damage+2));
        }
    }

    /* Override parent class's takeDamage purely so we can check
    * the invicibility powerup. */
    public void takeDamage(int damage, Sprite damage_dealer)
    {
        if(cooldowns[Constants.POW_INVINCIBILITY] < 0)
        {
            super.takeDamage(damage, damage_dealer);
        }
    }

    /** Override parent class's draw. */
    @Override
    public void draw(Board b, Graphics g, int offset_x, int offset_y)
    {
        super.draw(b, g, offset_x, offset_y);
        //Determine whether or not to draw the sprite's name.
        //Don't draw the name if this ship is trying to mimic
        //an experience brick.
        if(this.isVisible() && cooldowns[Constants.POW_MIMIC]<0)
        {
            int draw_x = (int) (this.getX() + (double) offset_x);
            int draw_y = (int) (this.getY() + (double) offset_y);
            if (this.getIsOnScreen())
            {   //Draw this enemy's name
                g.setColor(Color.WHITE);
                Font font = new Font("Serif", Font.BOLD, 18);
                g.setFont(font);
                g.drawString(name, draw_x-30, draw_y+(int)this.getCollisionRadius());
                //Display names of any active powerups
                int count = 0;
                for (int i = 0; i < cooldowns.length; i++)
                {
                    if(cooldowns[i]>0)
                    {
                        g.drawString(Constants.POWERUP_NAMES[i], draw_x-30, draw_y+(int)(1.8*this.getCollisionRadius())+count*20);
                        count++;
                    }
                }
                //Add some sparkles if any powerup is active
                if(count>0)
                {
                    this.board_reference.addSpriteIntangible(new Dazzle(this.getXCenter(), this.getYCenter()));
                }
            }
        }
    }

}