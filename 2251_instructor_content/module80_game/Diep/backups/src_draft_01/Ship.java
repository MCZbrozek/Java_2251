public class Ship extends SpriteTopDown
{
    Board board_reference;

    double speed; //speed of ship
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

    String name;

    public Ship(double x, double y,
                String name,
                double collision_radius,
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
                i_am_bitmask,
                i_hit_bitmask);
        this.board_reference = board;
        this.name = name;
        this.bullet_is_bitmask = bullet_is_bitmask;
        this.bullet_hits_bitmask = bullet_hits_bitmask;
        this.setAttributes();
    }

    private void setAttributes()
    {
        speed = Constants.speed_levels[speed_level];
        bullet_damage = Constants.bullet_damage_levels[bullet_damage_level];
        bullet_speed = Constants.bullet_speed_levels[bullet_speed_level];
        refire_rate = Constants.refire_rate_levels[refire_rate_level];
        bullet_longevity = Constants.bullet_longevity_levels[bullet_longevity_level];
        this.setMaxHealth(Constants.health_levels[health_level]);
        regen_cooldown_reset = Constants.regen_levels[regen_level];
        this.setBodyDamage(Constants.collision_damage_levels[collision_damage_level]);
        bullet_health = Constants.bullet_health_levels[penetration_level];
    }

    //Perform default attack if it has cooled down
    void attack()
    {
        if(firing_cooldown<=0)
        {
            Shot temp_shot = new Shot(
                    bullet_is_bitmask,
                    bullet_hits_bitmask,
                    this,
                    bullet_longevity,
                    bullet_damage,
                    bullet_speed,
                    bullet_health);
            board_reference.addSprite(temp_shot);
            firing_cooldown = this.refire_rate;
        }

        double recoil = 1.0;
        this.changeVelocity(
                -Math.cos(this.getAngle())*recoil,
                -Math.sin(this.getAngle())*recoil);
    }

    public void update(double elapsed_seconds)
    {
        super.update(elapsed_seconds);
        //Update Level
        while(level<Constants.xp_per_level.length && this.getXP()>=Constants.xp_per_level[level])
        {
            //Reduce xp by current level amount and increase level
            this.giveXP(-Constants.xp_per_level[level]);
            this.level++;
        }
        //Reduce firing cooldown
        firing_cooldown -= elapsed_seconds;
        //Health regen
        regen_cooldown -= elapsed_seconds;
        if(regen_cooldown<0)
        {
            regen_cooldown += regen_cooldown_reset;
            this.gainHealth();
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
                speed_level++; break;
            case "Bullet Damage":
                bullet_damage_level++; break;
            case "Bullet Speed":
                bullet_speed_level++; break;
            case "Bullet Longevity":
                bullet_longevity_level++; break;
            case "Refire Rate":
                refire_rate_level++; break;
            case "Health":
                health_level++; break;
            case "Health Regen":
                regen_level++; break;
            case "Collision Damage":
                collision_damage_level++; break;
            case "Penetration":
                penetration_level++; break;
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
}