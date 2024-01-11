import java.awt.*;
import java.util.ArrayList;

public class Ship extends SpriteTopDown
{
    //Added as a debugging tool for the NPC AI's
    private String strategy = "";

    private double bullet_spread = Math.PI/16;
    private double recoil = Constants.recoil;

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

    //Twin shot variables
    private double twin_shot_firing_cooldown = 0;
    private boolean barrel_ready = true;

    private int bullet_is_bitmask=0;
    private int bullet_hits_bitmask=0;

    private double regen_cooldown = 0;
    private double regen_cooldown_reset = 2.0;

    //Time until cloaking for the cloak ship
    private double cloak_cooldown=0;

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
    //Variable for reverting shape after getting the mimic powerup
    private int original_shape;

    //Variables for the triple shot power up
    //How many triple shots are left to fire
    int triple_shot_count = 0;
    private double triple_shot_cooldown = 0;

    private double[] cooldowns = new double[Constants.NUM_POWERUPS];
    private boolean[] powerup_on = new boolean[Constants.NUM_POWERUPS];

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
        this.original_shape = shape;
    }

    void setAttributes()
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

        this.recoil = Constants.recoil;

        if(this.shape == Constants.SHAPE_MACHINE_GUN)
        {
            //Refire way faster, penetration down, kickback from bullets lower
            this.refire_rate = Constants.refire_rate_levels[refire_rate_level]/3.0;
            this.bullet_health = Math.max(1, Constants.bullet_health_levels[penetration_level]/3);
            this.recoil = 5.0;
            this.bullet_spread = Math.PI/4;
            //Slightly larger ship
            this.original_radius = 30;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
        }
        else if(this.shape == Constants.SHAPE_FLANKER ||
                this.shape == Constants.SHAPE_CROSS ||
                this.shape == Constants.SHAPE_All_DIRECTIONS)
        {   //Zero recoil
            this.recoil = 0;
            //Slightly larger ship
            this.original_radius = 30;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
        }
        else if(this.shape == Constants.SHAPE_SNIPER)
        {   //long lived, fast, penetrating, high damage, slow refire, accuracy
            this.bullet_damage = this.bullet_damage*2;
            this.bullet_speed = this.bullet_speed*1.6;
            this.refire_rate = this.refire_rate+1.0;
            this.bullet_longevity = this.bullet_longevity*3;
            this.bullet_health = this.bullet_health*2;
            this.bullet_spread = 0;
            //Make the camera look ahead so this ship can see its target
            if(this instanceof Player)
            {
                board_reference.centerOnSprite(this, Constants.SNIPER_LOOK_AHEAD);
            }
            //Slightly larger ship
            this.original_radius = 30;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
        }
        else if(this.shape == Constants.SHAPE_RAMMER ||
                this.shape == Constants.SHAPE_CLOAK ||
                this.shape == Constants.SHAPE_EXPLODER)
        {   //speed up, can't shoot
            this.speed = this.speed*1.3;
            this.original_speed = speed;
            this.original_shape = this.shape;
            if (this.shape == Constants.SHAPE_EXPLODER)
            {   //Bullets explode instantly
                this.bullet_longevity = 0;
            }
        }
        else if(this.shape == Constants.SHAPE_TWIN)
        {
            //Slightly larger ship
            this.original_radius = 30;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
        }
    }

    void setAttributes(double speed, int bullet_damage, double bullet_speed,
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

    private void createBullet(double angle, double x, double y)
    {
        //Standard bullet radius. This is modified below.
        int bullet_radius = 6;
        //Change some bullet attributes if the BIG_BULLETS powerup is active
        int modifier = 1;
        if(this.powerup_on[Constants.POW_BIG_BULLETS]){ modifier = 8; }
        Shot temp_shot = new Shot(x,y,
                bullet_is_bitmask,
                bullet_hits_bitmask,
                this,
                angle+(Math.random()-0.5)*bullet_spread,
                bullet_longevity*modifier/2,
                bullet_damage,
                bullet_speed,
                bullet_health*modifier,
                bullet_radius*modifier,
                this.powerup_on[Constants.POW_EXPLODING_BULLETS] ||
                        Constants.bullet_explodes_default ||
                        this.shape==Constants.SHAPE_EXPLODER,
                board_reference);
        temp_shot.shove_out_impulse = temp_shot.shove_out_impulse*modifier;
        if(this.shape==Constants.SHAPE_EXPLODER)
        {
            temp_shot.setExplosionParameters(
                    Constants.explosion_distance*bullet_speed/300,
                    bullet_damage+body_damage,
                    (this.bullet_longevity_level+1)*Constants.explosion_impulse/5);
        }
        board_reference.addSprite(temp_shot);
    }

    private void shoot(double angle, double x, double y)
    {   //Rammer and cloaker ships do not shoot
        if(this.shape == Constants.SHAPE_RAMMER ||
                this.shape == Constants.SHAPE_CLOAK)
        {
            return;
        }
        this.createBullet(angle+(Math.random()-0.5)*bullet_spread, x,y);
        if(this.powerup_on[Constants.POW_SPREAD_SHOT])
        {
            this.createBullet(angle+Math.PI/6.0+(Math.random()-0.5)*bullet_spread, x,y);
            this.createBullet(angle-Math.PI/6.0+(Math.random()-0.5)*bullet_spread, x,y);
        }
        //Shove shooter back from recoil
        this.changeVelocity(
                -Math.cos(this.getAngle())*this.recoil,
                -Math.sin(this.getAngle())*this.recoil);
    }

    /** Intermediary function to see if this is a special ship shape
     * with a special firing mechanism. */
    private void shootIntermediary()
    {
        if(this.shape == Constants.SHAPE_FLANKER)
        {   //Fire forwards and backwards if this is a flanker
            this.shoot(this.getAngle(), this.getXCenter(), this.getYCenter());
            this.shoot(this.getAngle()+Math.PI, this.getXCenter(), this.getYCenter());
        }
        else if(this.shape == Constants.SHAPE_CROSS)
        {   //Fire forwards and backwards and to each side
            this.shoot(this.getAngle(), this.getXCenter(), this.getYCenter());
            this.shoot(this.getAngle()+Math.PI, this.getXCenter(), this.getYCenter());
            this.shoot(this.getAngle()+Math.PI/2, this.getXCenter(), this.getYCenter());
            this.shoot(this.getAngle()-Math.PI/2, this.getXCenter(), this.getYCenter());
        }
        else if(this.shape == Constants.SHAPE_All_DIRECTIONS)
        {   //Fire all around
            for (int i = 0; i < Constants.direction_count; i++)
            {
                this.shoot(this.getAngle()+i*2*Math.PI/Constants.direction_count, this.getXCenter(), this.getYCenter());
            }
        }
        else if(this.shape == Constants.SHAPE_TWIN)
        {   //Fire two forward, but off-center shots if this is a twin shot
            if(this.barrel_ready)
            {
                this.shoot(this.getAngle(),
                        this.getXCenter()+Math.cos(this.getAngle()+Math.PI/2)*10,
                        this.getYCenter()+Math.sin(this.getAngle()+Math.PI/2)*10);
            }
            else
            {
                this.shoot(this.getAngle(),
                        this.getXCenter()+Math.cos(this.getAngle()-Math.PI/2)*10,
                        this.getYCenter()+Math.sin(this.getAngle()-Math.PI/2)*10);
            }
            //Switch barrels for next shot
            this.barrel_ready = !this.barrel_ready;
        }
        else
        {   //Fire normally
            this.shoot(this.getAngle(), this.getXCenter(), this.getYCenter());
        }
    }

    //Perform default attack if it has cooled down
    void attack()
    {
        if(firing_cooldown<0 || twin_shot_firing_cooldown<0)
        {
            if(firing_cooldown<0)
            {
                this.firing_cooldown = this.refire_rate;
            }
            else
            {   //Second refire rate as plus or minus 0.1 noise in the refire.
                this.twin_shot_firing_cooldown = this.refire_rate+(Math.random()-0.5)/10.0;
            }
            this.shootIntermediary();
            //Check for the triple shot powerup
            if(this.powerup_on[Constants.POW_TRIPLE_SHOT])
            {
                triple_shot_count = 2;
                triple_shot_cooldown = 0.1;
            }
        }
        //Check for the firing of triple shots
        else if(this.powerup_on[Constants.POW_TRIPLE_SHOT] && triple_shot_cooldown<0 && triple_shot_count > 0)
        {
            triple_shot_count--;
            triple_shot_cooldown = 0.1;
            this.shootIntermediary();
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
        if(this.shape == Constants.SHAPE_TWIN){twin_shot_firing_cooldown -= elapsed_seconds;}
        //Health regen
        regen_cooldown -= elapsed_seconds;
        if (regen_cooldown < 0)
        {
            regen_cooldown += regen_cooldown_reset;
            this.gainHealth();
        }
        //Cooldown triple shot
        triple_shot_cooldown -= elapsed_seconds;
        //Cooldown time until cloaking. Only cooldown and cloak when not moving.
        //Ship can actually creep along slowly.
        if(Math.abs(this.getdx())<Constants.cloak_speed_limit && Math.abs(this.getdy())<Constants.cloak_speed_limit)
        {
            cloak_cooldown -= elapsed_seconds;
        }
        else
        {   //three seconds of motionlessness to cloak
            cloak_cooldown = 3.0;
        }
        //cooldown all the powerups
        for (int i = 0; i < cooldowns.length; i++)
        {
            cooldowns[i] -= elapsed_seconds;
        }
        //Reset the speed if the powerup is off
        if (this.powerup_on[Constants.POW_SPEED_UP] && cooldowns[Constants.POW_SPEED_UP]<0)
        {
            this.powerup_on[Constants.POW_SPEED_UP] = false;
            this.speed = original_speed;
        }
        //Reset the friction if the powerup is off
        if (this.powerup_on[Constants.POW_NO_FRICTION] && cooldowns[Constants.POW_NO_FRICTION]<0)
        {
            this.powerup_on[Constants.POW_NO_FRICTION] = false;
            this.friction = original_friction;
        }
        //Reset the shove out if the powerup is off
        if (this.powerup_on[Constants.POW_SHOVE_OUT] && cooldowns[Constants.POW_SHOVE_OUT]<0)
        {
            this.powerup_on[Constants.POW_SHOVE_OUT] = false;
            this.shove_out_impulse = original_shove_out;
        }
        //Reset the speed, regen, and radius if the goliath powerup is off
        if (this.powerup_on[Constants.POW_GOLIATH] && cooldowns[Constants.POW_GOLIATH]<0)
        {
            this.powerup_on[Constants.POW_GOLIATH] = false;
            this.speed = original_speed;
            double temp_x = this.getXCenter();
            double temp_y = this.getYCenter();
            this.setCollisionRadius(original_radius);
            this.moveToCenter(temp_x, temp_y);
            this.regen_cooldown_reset = original_regen;
        }
        //Reset the look of the ship if mimic is off
        if (this.powerup_on[Constants.POW_MIMIC] && cooldowns[Constants.POW_MIMIC]<0)
        {
            this.powerup_on[Constants.POW_MIMIC] = false;
            this.setCollisionRadius(original_radius);
            this.color = original_color;
            this.shape = this.original_shape;
            this.no_health_draw = false;
            this.no_direction_draw = false;
            this.setAttributes();
        }
        //Reset the blackhole status
        if (this.powerup_on[Constants.POW_BLACKHOLE] && cooldowns[Constants.POW_BLACKHOLE]<0)
        {
            this.powerup_on[Constants.POW_BLACKHOLE] = false;
            this.setBodyDamage(original_body_damage);
        }
        //If blackhole is on, pull in nearby sprites
        if (this.powerup_on[Constants.POW_BLACKHOLE])
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
        //Deactivate any other powerups that have expired
        for (int i = 0; i < cooldowns.length; i++)
        {
            this.powerup_on[i] = cooldowns[i]>0;
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
    {   //For now, 10 second duration on all powerups
        cooldowns[powerup_id] = 10;
        this.powerup_on[powerup_id] = true;
        //Check for teleport away
        if(powerup_id == Constants.POW_TELEPORT)
        {
            //Warp this ship off the edge of the map in a random direction.
            double rand_angle = Math.random()*Math.PI*2;
            this.setX(Constants.boundary_right/2+Math.cos(rand_angle)*Constants.boundary_right/1.5);
            this.setY(Constants.boundary_down/2+Math.sin(rand_angle)*Constants.boundary_down/1.5);
        }
        //Check for brickstravaganza
        else if(powerup_id == Constants.POW_BRICKSTRAVAGANZA)
        {   //Spawn an extra 100 bricks around the player.
            Constants.loadExperienceBricks(
                    this.getXCenter(), this.getYCenter(),
                    board_reference,
                    100, 3, 10, 1,
                    new Color(153,51,255), Constants.SHAPE_TRIANGLE,
                    false, 0, false);
        }
        //Check for speed up
        else if(powerup_id == Constants.POW_SPEED_UP)
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
            //Make the blackhole have a shorter duration than other powerups.
            cooldowns[powerup_id] = 7;
            this.setHealthMax();
            this.setBodyDamage(Math.max(5,body_damage+2));
        }
    }

    /* Override parent class's takeDamage purely so we can check
    * the invicibility powerup. */
    public void takeDamage(int damage, Sprite damage_dealer)
    {
        if(!this.powerup_on[Constants.POW_INVINCIBILITY])
        {
            super.takeDamage(damage, damage_dealer);
        }
    }

    boolean cloaked(){ return this.shape==Constants.SHAPE_CLOAK&&cloak_cooldown<0; }

    /** Override parent class's draw. */
    @Override
    public void draw(Board b, Graphics g, int offset_x, int offset_y)
    {
        //Don't draw the cloaked ship
        if(this.cloaked()){return;}
        super.draw(b, g, offset_x, offset_y);
        //Determine whether or not to draw the sprite's name.
        //Don't draw the name if this ship is trying to mimic
        //an experience brick.
        if(this.isVisible() && !this.powerup_on[Constants.POW_MIMIC])
        {
            int draw_x = (int) (this.getX() + (double) offset_x);
            int draw_y = (int) (this.getY() + (double) offset_y);
            if (this.getIsOnScreen())
            {   //Draw this enemy's name
                g.setColor(Color.WHITE);
                Font font = new Font("Serif", Font.BOLD, 18);
                g.setFont(font);
                g.drawString(name, draw_x-30, draw_y+(int)this.getCollisionRadius());
                //Draw strategy
                g.drawString(strategy, draw_x-30, -20+draw_y+(int)this.getCollisionRadius());
                //Draw cloak cooldown
                if(this.shape==Constants.SHAPE_CLOAK)
                {
                    g.drawString(Long.toString(Math.round(100*cloak_cooldown)), draw_x-30, -40+draw_y+(int)this.getCollisionRadius());
                }
                //Display names of any active powerups
                int count = 0;
                for (int i = 0; i < this.powerup_on.length; i++)
                {
                    if(this.powerup_on[i])
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