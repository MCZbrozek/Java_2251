import java.awt.*;
import java.util.ArrayList;

/* SHIP TYPE INFORMATION

MACHINE_GUN
(Control-f search this file for SHAPE_MACHINE_GUN for more information)
Upgrades from initial ship.
Rate of fire UP x3.
Bullet penetration DOWN /3.
Recoil reduced.
Bullet spread UP (accuracy reduced) 45 degree arc.

CARRIER
(Control-f search this file for SHAPE_CARRIER for more information)
Upgrades from machine gun.
Larger (collision radius of 50).
Refire UP x2.
Bullet penetration zero.
Recoil zero.
Fires seeking bullets.
Player-controlled carrier spawns bullets that seek the mouse cursor.

FLANKER
(Control-f search this file for SHAPE_FLANKER for more information)
Upgrades from initial ship.
Zero recoil.
Fires forward and backward.

CROSS FIRE
(Control-f search this file for SHAPE_CROSS for more information)
Upgrades from flanker.
Zero recoil.
Fires in 4 directions.

ALL DIRECTIONS FIRE
(Control-f search this file for SHAPE_ALL_DIRECTIONS for more information)
Upgrades from cross.
Zero recoil.
Fires in 8 directions.

TWIN SHOT
(Control-f search this file for SHAPE_TWIN for more information)
Upgrades from initial ship.
Fires two bullets straight ahead.
Recoil is automatically doubled.
Refire rate is random but averages out to be the same as initial ship.

SNIPER
(Control-f search this file for SHAPE_SNIPER for more information)
Upgrades from twin shot.
Bullet damage UP x2.
Bullet longevity UP x3.
Bullet penetration UP x2.
Bullet spread set to zero (accuracy 100%).
Bullet speed UP x1.6.
Refire rate DOWN +1 second between shots.
Player controlled sniper gains look-ahead camera.

SNIPER CANNON
(Control-f search this file for SHAPE_CANNON for more information)
Upgrades from sniper.
Bullet damage UP x2.
Bullet longevity UP x3.
Bullet penetration UP x2.
Bullet spread set to zero (accuracy 100%).
Bullet speed UP x1.6.
Bullet size UP x6.
Bullet shove UP x8 (bullets push objects they hit away).
Refire rate DOWN +1.2 seconds between shots.
Ship speed DOWN x(2/3).
Health regen DOWN /2 (twice as slow).
Player controlled sniper gains look-ahead camera.

SNIPER SEEKER
(Control-f search this file for SHAPE_SEEKER for more information)
Upgrades from sniper.
Bullet damage UP x2.
Bullet longevity UP x3.
Bullet penetration UP x2.
Bullet spread set to zero (accuracy 100%).
Bullet speed DOWN x0.7 (but seeker bullets accelerate differently anyway).
Refire rate DOWN +0.2 seconds between shots.
Fires seeking bullets.
Player controlled sniper gains look-ahead camera.

RAM
(Control-f search this file for SHAPE_RAMMER for more information)
Upgrades from initial ship.
Speed UP x1.3.
Health buffed 10%
Does not shoot.

MIMIC
(Control-f search this file for SHAPE_MIMIC for more information)
Upgrades from ram.
Does not shoot.
Health buffed 10%
Looks exactly like high xp brick. You can't even see its health bar.
Cloaks and decloaks from NPCs for intervals between 0 and 5 seconds at random.
Otherwise NPCs woud never be fooled.
Cloaked ships do not show up as a nearest_ship for NPCs.
Mimic has a temporary speed boost of x3 for up to 1.5 seconds. stamina for this
boost is recovered while stationary. Search for "stamina" in SpriteTopDown
for more details.

GRAVITY
(Control-f search this file for SHAPE_GRAVITY for more information)
Upgrades from rammer.
Does not shoot.
Health buffed 10%
Speed DOWN /2.
Body damage (aka collision damage dealt by this ship) UP +bullet_damage (in other words, upgrades to bullet damage now affect collision damage too)
The gravity ship changes size. When its size is positive it can collide with objects.
When its size is negative it cannot collide and cannot be touched.
The gravity ship attracts other sprites to it.
The range of the attraction is increased by bullet longevity upgrades and increased by one third of bullet health upgrades.
The strength of the attraction is increased by bullet speed upgrades.

CREEPER
(Control-f search this file for SHAPE_CREEPER for more information)
Upgrades from mimic.
Does not shoot.
Speed UP x1.3 (same speed as ram).
Health buffed 10%
When it stops moving a 3 second countdown begins. At zero, this ship becomes invisible.
Cloaked ships do not show up as a nearest_ship for NPCs.
The creeper can move slowly while cloaked.

EXPLODER
(Control-f search this file for SHAPE_EXPLODER for more information)
Upgrades from gravity.
Health buffed 10%
Speed UP x1.3 (same speed as ram).
Refire rate DOWN +1 second between shots.
Zero recoil.
Instead of bullets, creates explosion centered on ship.
Explosion radius = Constants.explosion_distance*bullet_speed/300
Explosion damage = bullet_damage + body_damage
Explosion impact = (bullet_longevity_level+1)*Constants.explosion_impulse/5

CLOAK
(Control-f search this file for SHAPE_CLOAK for more information)
Upgrades from creeper.
Does not shoot.
Body damage for this ship is the sum of collision and bullet damage.
When it stops moving a 3 second countdown begins. At zero, this ship becomes invisible.
Cloaked ships do not show up as a nearest_ship for NPCs.
Cloak has a temporary speed boost of x3 for up to 1.5 seconds. stamina for this
boost is recovered while stationary. Search for "stamina" in SpriteTopDown
for more details.

LASER
(Control-f search this file for SHAPE_LASER for more information)
Upgrades from sniper.
Zero recoil.
Bullet spread set to zero (accuracy 100%).
Bullet speed and longevity both increase the range of the laser by 200 x level.
Bullet penetration increases the width of the laser by 2 x level.
Refire rate DOWN +1 seconds between shots.
Player controlled sniper gains look-ahead camera.
*/

public class Ship extends SpriteTopDown
{
    //Added as a debugging tool for the NPC AI's
    String strategy = "";

    //Ships now get 3 lives
    private int lives = 3;

    private double bullet_spread = Math.PI/16;
    private double recoil = Constants.recoil;

    private int bullet_damage;
    private double bullet_speed; //Pixels per second
    private double bullet_longevity; //Seconds
    private double refire_rate; //Seconds
    private int bullet_health;

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

    //variable for the friction down powerup
    private double original_friction;
    //variable for the shove out powerup
    private double original_shove_out;
    protected double original_radius;
    //variable for the mimic powerup
    private Color original_color;
    //Variable for reverting shape after getting the mimic powerup
    protected int original_shape;

    //Variables for the triple shot power up
    //How many triple shots are left to fire
    int triple_shot_count = 0;
    private double triple_shot_cooldown = 0;

    private double[] cooldowns = new double[Constants.NUM_POWERUPS];
    private boolean[] powerup_on = new boolean[Constants.NUM_POWERUPS];

    //Gravity ship pulses. It changes its collision radius over time.
    private double pulse_timer = 0;

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
        this.original_friction = friction;
        this.original_shove_out = shove_out_impulse;
        this.original_radius = collision_radius;
        this.color = color;
        this.original_color = color;
        this.original_shape = shape;
        this.setAttributes();
    }

    void setShape(int shape)
    {
        this.shape = shape;
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
        this.bullet_spread = Math.PI/16;
        this.recoil = Constants.recoil;
        this.friction = this.original_friction;
        this.shove_out_impulse = this.original_shove_out;

        this.color = original_color;
        this.shape = this.original_shape;
        this.no_health_draw = false;
        this.no_direction_draw = false;

        double temp_x = this.getXCenter();
        double temp_y = this.getYCenter();
        this.setCollisionRadius(original_radius);
        this.moveToCenter(temp_x, temp_y);

        if(this.shape == Constants.SHAPE_MACHINE_GUN)
        {
            //Refire way faster, penetration down, kickback from bullets lower
            this.refire_rate = Constants.refire_rate_levels[refire_rate_level]/3.0;
            this.bullet_health = Math.max(1, Constants.bullet_health_levels[penetration_level]/3);
            this.recoil = 5.0;
            this.bullet_spread = Math.PI/4;
            //Slightly larger ship
            this.original_radius = 40;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
        }
        else if(this.shape == Constants.SHAPE_FLANKER ||
                this.shape == Constants.SHAPE_CROSS ||
                this.shape == Constants.SHAPE_All_DIRECTIONS)
        {   //Zero recoil
            this.recoil = 0;
            //Slightly larger ship
            this.original_radius = 40;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
        }
        else if(this.shape == Constants.SHAPE_SNIPER ||
                this.shape == Constants.SHAPE_CANNON ||
                this.shape == Constants.SHAPE_SEEKER ||
                this.shape == Constants.SHAPE_LASER)
        {   //long lived, fast, penetrating, high damage, slow refire, accuracy
            this.bullet_damage = this.bullet_damage*2;
            this.bullet_health = this.bullet_health*2;
            this.bullet_spread = 0;
            //Slow down the bullet if it is a seeker
            if(this.shape == Constants.SHAPE_SEEKER)
            {   //Seeker missile ship fires more frequently, but slower bullets
                this.bullet_speed = this.bullet_speed*0.7;
                this.refire_rate = this.refire_rate+0.2;
                this.bullet_longevity = this.bullet_longevity*3;
            }
            else if(this.shape == Constants.SHAPE_CANNON)
            {   //Nerfs to this very powerful ship
                this.refire_rate = this.refire_rate+1.2;
                this.speed = this.speed*2/3;
                this.regen_cooldown_reset = 2*this.regen_cooldown_reset;
            }
            else if(this.shape == Constants.SHAPE_LASER)
            {
                this.recoil = 0;
                this.refire_rate = this.refire_rate+1.0;
            }
            else
            {
                this.bullet_speed = this.bullet_speed*1.6;
                this.refire_rate = this.refire_rate+1.0;
                this.bullet_longevity = this.bullet_longevity*3;
            }
            //Make the camera look ahead so this ship can see its target
            if(this instanceof Player)
            {
                board_reference.centerOnSprite(this, Constants.SNIPER_LOOK_AHEAD);
            }
            //Slightly larger ship
            this.original_radius = 40;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
        }
        else if(this.shape == Constants.SHAPE_GRAVITY)
        {   //slower. bullet damage is added to collision damage
            this.speed = this.speed/2.0;
            this.setBodyDamage(Constants.collision_damage_levels[collision_damage_level]+this.bullet_damage);
            this.no_direction_draw = true;
        }
        else if(this.shape == Constants.SHAPE_RAMMER ||
                this.shape == Constants.SHAPE_CREEPER ||
                this.shape == Constants.SHAPE_EXPLODER ||
                this.shape == Constants.SHAPE_MIMIC ||
                this.shape == Constants.SHAPE_CLOAK)
        {   //speed up, can't shoot, health buffed 10%
            this.original_shape = this.shape;
            this.no_health_draw = false;
            this.no_direction_draw = true;
            this.setMaxHealth((int)(Constants.health_levels[health_level]*1.10));
            if(this.shape == Constants.SHAPE_EXPLODER)
            {   //Bullets explode instantly. Refire is much slower
                this.bullet_longevity = 0;
                this.refire_rate = this.refire_rate+1.0;
                this.speed = this.speed*1.3;
            }
            else if(this.shape == Constants.SHAPE_MIMIC)
            {
                this.original_radius = 50;
                this.setCollisionRadius(original_radius);
                this.original_shape = this.shape;
                this.color = Color.cyan;
                this.no_health_draw = true;
            }
            else if(this.shape == Constants.SHAPE_CLOAK)
            {   //Body damage can be increased by upgrading collision
                //or bullet damage
                this.setBodyDamage(Constants.collision_damage_levels[collision_damage_level]+this.bullet_damage);
            }
            else
            {
                this.speed = this.speed*1.3;
            }
        }
        else if(this.shape == Constants.SHAPE_TWIN)
        {
            //Slightly larger ship
            this.original_radius = 40;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
        }
        else if(this.shape == Constants.SHAPE_CARRIER)
        {   //larger ship
            this.original_radius = 60;
            this.setCollisionRadius(original_radius);
            this.original_shape = this.shape;
            //Faster refire. Zero penetration. Zero kick.
            this.refire_rate = Constants.refire_rate_levels[refire_rate_level]/2.0;
            this.bullet_health = 1;
            this.recoil = 0;
            this.no_direction_draw = true;
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
    }

    private void createBullet(double angle, double x, double y)
    {
        //Standard bullet radius. This is modified below.
        int bullet_radius = 15;
        //Change some bullet attributes if the BIG_BULLETS powerup is active
        //or the ship is the cannon ship
        int modifier = 1;
        if(this.powerup_on[Constants.POW_BIG_BULLETS] ||
                this.shape == Constants.SHAPE_CANNON)
        {
            modifier = 6;
        }
        Shot temp_shot;
        if(this.shape==Constants.SHAPE_SEEKER || this.shape==Constants.SHAPE_CARRIER)
        {
            temp_shot = new Seeker(x,y,
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
                    (BoardTopDown)board_reference);
            if(this.shape==Constants.SHAPE_CARRIER)
            {   //Carrier uses much more accurate seeker bullets
                temp_shot.speed = 0;
                //Player's seekers attack the mouse
                if(this instanceof Player)
                {
                    ((Seeker)temp_shot).target = ((Player)this).getMouseXY();
                }
            }
        }
        else if(this.shape == Constants.SHAPE_LASER)
        {
            //create laser beam
            double length = (bullet_speed_level+bullet_longevity_level+1)*200;
            double width = (bullet_health)*2;
            //Get everything hit by the laser
            ArrayList<SpritePhysics> shot_by = this.board_reference.getLineCollisions(
                    this.getXCenter(), this.getYCenter(),
                    this.getAngle(), length, width);
            //Damage everything hit by the laser
            for (SpritePhysics sp : shot_by)
            {   //Don't hit self or powerups.
                if(sp!=this && !(sp instanceof PowerUp))
                {
                    sp.takeDamage(bullet_damage, this);
                }
            }
            //Draw the laser
            board_reference.addSpriteIntangible(
                    new DisplayLine(this.getXCenter(), this.getYCenter(),
                            0.15,
                            this.getXCenter()+Math.cos(this.getAngle())*length,
                            this.getYCenter()+Math.sin(this.getAngle())*length,
                            (int)width,
                            Color.RED));
            return;
        }
        else
        {
            temp_shot = new Shot(x,y,
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
        }
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
    {   //Rammer, cloaker, and gravity ships do not shoot
        if(this.shape == Constants.SHAPE_RAMMER ||
                this.shape == Constants.SHAPE_CREEPER ||
                this.shape == Constants.SHAPE_GRAVITY ||
                this.shape == Constants.SHAPE_MIMIC ||
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
        if(this.shape != Constants.SHAPE_EXPLODER)
        {
            this.changeVelocity(
                    -Math.cos(this.getAngle())*this.recoil,
                    -Math.sin(this.getAngle())*this.recoil);
        }
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
        //Mimic has random cloaking from NPC's
        if(this.shape == Constants.SHAPE_MIMIC)
        {
            if(cloak_cooldown>0)
            {
                cloak_cooldown -= elapsed_seconds;
                //If cloak became negative, set it to a random negative value
                if(cloak_cooldown<=0){cloak_cooldown = -Math.random()*5;}
            }
            else
            {
                cloak_cooldown += elapsed_seconds;
                //If cloak became positive, set it to a random positive value
                if(cloak_cooldown>=0){cloak_cooldown = Math.random()*5;}
            }
        }
        else if(this.shape == Constants.SHAPE_CREEPER || this.shape == Constants.SHAPE_CLOAK)
        {
            //How fast the cloaked ship can creep along while remaining cloaked
            double cloak_speed_limit = 70.0;
            //Cooldown time until cloaking. Only cooldown and cloak when not moving.
            //Ship can actually creep along slowly.
            if(Math.abs(this.getdx())<cloak_speed_limit && Math.abs(this.getdy())<cloak_speed_limit)
            {
                cloak_cooldown -= elapsed_seconds;
            }
            else
            {   //three seconds of motionlessness to cloak
                cloak_cooldown = 3.0;
            }
        }
        //cooldown all the powerups
        for (int i = 0; i < cooldowns.length; i++)
        {
            cooldowns[i] -= elapsed_seconds;
            if(powerup_on[i] && cooldowns[i]<0)
            {
                powerup_on[i] = false;
                setAttributes(); //reset attributes
            }
        }
        //If blackhole is on, pull in nearby sprites
        if (this.powerup_on[Constants.POW_BLACKHOLE])
        {   //Pull in other sprites with force based on distance
            this.impulseTowardsMe(800,-1000); //negative makes it a pulling force
        }
        //Pulse the gravity ship and draw in nearby sprites
        if(this.shape == Constants.SHAPE_GRAVITY)
        {
            //Pulse the size of the sprite
            this.pulse_timer += elapsed_seconds;
            double x = this.getXCenter();
            double y = this.getYCenter();
            this.setCollisionRadius(this.original_radius+100*Math.cos(this.pulse_timer));
            this.moveToCenter(x,y);
            //pull in towards it
            this.impulseTowardsMe(
                    50*(((double)this.bullet_health)/3.0+this.bullet_longevity),//range based on bullet longevity and health (ie penetration)
                    -this.bullet_speed);//strength of gravity based on bullet speed
        }
    }

    private void impulseTowardsMe(double range, double impulse)
    {
        ArrayList<SpritePhysics> in_range = board_reference.getAllWithinDistance(this.getXCenter(), this.getYCenter(), range);
        //Pull sprites in
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

    int getLevel()
    {
        int xp_temp = this.getXP();
        int level = 0;
        while(xp_temp>0 && level<Constants.xp_per_level.length)
        {
            xp_temp -= Constants.xp_per_level[level];
            level++;
        }
        if(xp_temp==0){return level;}
        else{return level-1;}
    }

    double getPercentExperience()
    {
        int xp_temp = this.getXP();
        int level = 0;
        while(xp_temp>0)
        {
            if(level >= Constants.xp_per_level.length)
            {
                return 0;
            }
            else if(Constants.xp_per_level[level] > xp_temp)
            {
                return ((double)xp_temp)/((double)Constants.xp_per_level[level]);
            }
            xp_temp -= Constants.xp_per_level[level];
            level++;
        }
        return 0;
    }

    int availableLevelUpPoints()
    {
        return getLevel() - spentLevelUpPoints();
    }

    int spentLevelUpPoints()
    {
        return speed_level+bullet_damage_level+bullet_speed_level+refire_rate_level+bullet_longevity_level+health_level+regen_level+collision_damage_level+penetration_level;
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

    //Reward for killing a ship
    int getXpReward(){ return 40+this.getLevel()*4; }

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
                    100, 2, 16, 1,
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
            //Give invincibility while friction is off too
            cooldowns[Constants.POW_INVINCIBILITY] = 10;
            this.powerup_on[Constants.POW_INVINCIBILITY] = true;
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
            //Give invincibility while friction is off too
            cooldowns[Constants.POW_INVINCIBILITY] = 10;
            this.powerup_on[Constants.POW_INVINCIBILITY] = true;
        }
        //Check for mimic
        else if(powerup_id == Constants.POW_MIMIC)
        {
            this.setCollisionRadius(50);
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
            //Give invincibility while friction is off too
            cooldowns[Constants.POW_INVINCIBILITY] = 7;
            this.powerup_on[Constants.POW_INVINCIBILITY] = true;
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
        //If this ship is not dead, revive with reduced experience.
        if(this.getHealth()<=0 && this.lives>1)
        {
            //Decrease number of lives
            this.lives -= 1;
            //20% xp reduction
            this.reduceXP(0.20);
            //Reset ship back to basic ship
            this.speed_level = 0;
            this.bullet_damage_level = 0;
            this.bullet_speed_level = 0;
            this.bullet_longevity_level = 0;
            this.refire_rate_level = 0;
            this.health_level = 0;
            this.regen_level = 0;
            this.collision_damage_level = 0;
            this.penetration_level = 0;
            this.shape = Constants.SHAPE_CIRCLE;
            this.original_shape = this.shape;
            this.original_radius = 20;
            this.setCollisionRadius(20);
            this.setAttributes();
            //reset health to full
            this.health = this.max_health;
            //Warp this ship off the edge of the map in a random direction.
            double rand_angle = Math.random()*Math.PI*2;
            this.setX(Constants.boundary_right/2+Math.cos(rand_angle)*Constants.boundary_right/1.5);
            this.setY(Constants.boundary_down/2+Math.sin(rand_angle)*Constants.boundary_down/1.5);
            //Temporary invulnerability
            cooldowns[Constants.POW_INVINCIBILITY] = 10;
            this.powerup_on[Constants.POW_INVINCIBILITY] = true;
            //If this ship is an enemy, tell the personality that it died
            if(this instanceof Enemy)
            {
                ((Enemy)this).informDied();
            }
        }
    }

    boolean cloaked(){ return (this.shape==Constants.SHAPE_MIMIC || this.shape==Constants.SHAPE_CREEPER || this.shape==Constants.SHAPE_CLOAK) && cloak_cooldown<0; }

    /** Override parent class's draw. */
    @Override
    public void draw(Board b, Graphics g, int offset_x, int offset_y)
    {
        //Don't draw the cloaked ship
        if(this.cloaked() && this.shape!=Constants.SHAPE_MIMIC){return;}
        super.draw(b, g, offset_x, offset_y);
        //Determine whether or not to draw the sprite's name.
        //Don't draw the name if this ship is trying to mimic
        //an experience brick.
        if(this.isVisible() && !this.powerup_on[Constants.POW_MIMIC] && this.shape!=Constants.SHAPE_MIMIC)
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
                if(!strategy.equals(""))
                {
                    g.drawString(strategy, draw_x-30, -20+draw_y+(int)this.getCollisionRadius());
                }
                //Draw cloak cooldown
                if(this.shape==Constants.SHAPE_CREEPER || this.shape==Constants.SHAPE_CLOAK)
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

    //Returns list of powerup activation
    boolean[] getPowerups(){return powerup_on;}

    int getLives(){return this.lives;}
}