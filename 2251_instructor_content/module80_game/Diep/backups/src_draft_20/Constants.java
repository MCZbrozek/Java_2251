import java.awt.*;
import java.util.Random;

class Constants
{
    private static Random random_generator = new Random();

    //Assorted game constants:

    //Number of experience bricks in the world
    static int low_exp_brick_count = 200;
    static int med_exp_brick_count = 20;
    static int high_exp_brick_count = 5;
    static int suicide_brick_count = 1;

    static final double suicide_brick_speed = 230;

    //Recoil from shooting
    static final double recoil = 20.0;
    //Delay between damage taking from the same sprite in seconds
    static final double redamage_cooldown_reset = 0.1;
    //Stay in bounds impulse
    static final double inbounds_impulse = 500;

    //Top and left boundaries of the game world default to zero.
    //These are the right and bottom boundaries
    static int boundary_right = 3000;
    static int boundary_down = 3000;

    static final int[] xp_per_level = {4, 10, 12, 16, 20, 24, 30, 36, 42, 50, 58, 66, 74, 82, 90, 100, 110, 120, 130, 130, 130, 140, 140, 140, 140, 150, 150, 150, 150, 150, 150, 150};

    /*attribute_level_limit is the smallest index that is valid in all the
    level arrays below. Making this integer too large will result in an Index
    Array Out Of Bounds Exception.*/
    static final int attribute_level_limit = 7;

    static final double[] speed_levels = {180, 210, 240, 270, 300, 330, 360, 390};
    static int[] bullet_damage_levels = {1, 2, 3, 4, 5, 6, 8, 10};
    static double[] bullet_speed_levels = {260, 300, 340, 380, 420, 460, 500, 540};
    static final double[] refire_rate_levels = {0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2};
    static final double[] bullet_longevity_levels = {3.5, 4, 4.5, 5, 5.5, 6, 6.5, 7};
    static final int[] health_levels = {50, 75, 100, 125, 150, 175, 200, 225};
    static final double[] regen_levels = {4, 3.5, 3, 2.5, 2, 1.5, 1, 0.5};
    static final int[] collision_damage_levels = {3, 4, 5, 6, 7, 9, 11, 13};
    static final int[] bullet_health_levels = {1, 2, 4, 6, 8, 10, 12, 14};

    static final String[] attribute_names = {"Speed", "Bullet Damage", "Bullet Speed", "Bullet Longevity", "Refire Rate", "Health", "Health Regen", "Collision Damage", "Penetration"};
    static final Color[] attribute_colors = {Color.cyan, Color.ORANGE, Color.YELLOW, Color.WHITE, Color.MAGENTA, Color.GREEN, Color.BLUE, Color.red, Color.PINK};

    //Whether or not bullets default to exploding and other
    //bullet explosion parameters.
    static boolean bullet_explodes_default = false;
    static double explosion_distance = 300;
    static double explosion_impulse = 500;
    static int explosion_damage = 1;

    //Sprite shape options
    static final int SHAPE_RECT = 0;
    static final int SHAPE_CIRCLE = 1;
    static final int SHAPE_TRIANGLE = 2;
    static final int SHAPE_IMAGE = 3;
    static final int SHAPE_RAMMER = 4;
    static final int SHAPE_FLANKER = 5;
    static final int SHAPE_TWIN = 6;
    static final int SHAPE_MACHINE_GUN = 7;
    static final int SHAPE_EXPLODER = 8;
    static final int SHAPE_MIMIC = 9;
    static final int SHAPE_CROSS = 10;
    static final int SHAPE_SNIPER = 11;
    static final int SHAPE_CARRIER = 12;
    static final int SHAPE_GRAVITY = 13;
    static final int SHAPE_CREEPER = 14;
    static final int SHAPE_All_DIRECTIONS = 15;
    static final int SHAPE_SEEKER = 16;
    static final int SHAPE_CANNON = 17;
    static final int SHAPE_CLOAK = 18;

    //How many directions the SHAPE_All_DIRECTIONS ship shoots in.
    static final int direction_count = 8;

    //Level at which ship upgrades become available.
    //Later on this should be different for different ships.
    static final int ship_upgrade_level_limit = 10;
    static final int ship_upgrade_step = 5;

    //How far ahead the sniper sees
    static final double SNIPER_LOOK_AHEAD = -300;

    //Powerups
    static final int POW_TRIPLE_SHOT = 0;
    static final int POW_INVINCIBILITY = 1;
    static final int POW_SPREAD_SHOT = 2;
    static final int POW_SPEED_UP = 3;
    static final int POW_NO_FRICTION = 4;
    static final int POW_SHOVE_OUT = 5;
    static final int POW_GOLIATH = 6;
    static final int POW_MIMIC = 7;
    static final int POW_EXPLODING_BULLETS = 8;
    static final int POW_BLACKHOLE = 9;
    static final int POW_BIG_BULLETS = 10;
    static final int POW_TELEPORT = 11;
    static final int POW_BRICKSTRAVAGANZA = 12;
    static final int NUM_POWERUPS = 13;

    static final String[] POWERUP_NAMES = {"Triple-Shot",
                                        "Invincibility",
                                        "Spread-Shot",
                                        "Speed-Up",
                                        "Friction-Off",
                                        "Shove",
                                        "Goliath",
                                        "Mimic",
                                        "Explosive",
                                        "Blackhole",
                                        "Big Bullets",
                                        "Teleport Away!",
                                        "Brickstravaganza!"};

    //Returns a random x coordinate
    static double getRandomX(){return random_generator.nextDouble()*boundary_right;}
    //Returns a random y coordinate
    static double getRandomY(){return random_generator.nextDouble()*boundary_down;}

    static String getRandomAttribute()
    {
        return attribute_names[random_generator.nextInt(attribute_names.length)];
    }

    static void loadExperienceBricks(Board b, int brick_count,
                                     int health, double radius,
                                     int reward, Color c, int shape,
                                     boolean suicidal,
                                     int body_damage,
                                     boolean regenerate)
    {
        for (int i = 0; i <brick_count; i++)
        {
            b.addSprite(
                    new ExperienceBrick(
                            getRandomX(),
                            getRandomY(),
                            radius,
                            health,
                            reward,
                            c,
                            shape,
                            suicidal,
                            BoardTopDown.xp_brick_bitmask,
                            BoardTopDown.everything_bitmask,
                            body_damage,
                            regenerate,
                            b)
            );
        }
    }


    /**Like the above function but centers bricks on given x, y coordinates
     * Used for the brick-stravaganza powerup.*/
    static void loadExperienceBricks(double x, double y, Board b, int brick_count,
                                     int health, double radius,
                                     int reward, Color c, int shape,
                                     boolean suicidal,
                                     int body_damage,
                                     boolean regenerate)
    {
        double angle;
        for (int i = 0; i <brick_count; i++)
        {
            angle = Math.random()*Math.PI*2;
            b.addSprite(
                    new ExperienceBrick(
                            x+Math.cos(angle)*Math.random()*500,
                            y+Math.sin(angle)*Math.random()*500,
                            radius,
                            health,
                            reward,
                            c,
                            shape,
                            suicidal,
                            BoardTopDown.xp_brick_bitmask,
                            BoardTopDown.everything_bitmask,
                            body_damage,
                            regenerate,
                            b)
            );
        }
    }


    //Initialize the ship upgrade list.
    static final ShipUpgradeStruct[] ship_upgrade_list =
            {
                    new ShipUpgradeStruct(ship_upgrade_level_limit, "Upgrade to rammer", SHAPE_RAMMER, SHAPE_CIRCLE),
                    new ShipUpgradeStruct(ship_upgrade_level_limit, "Upgrade to flanker", SHAPE_FLANKER, SHAPE_CIRCLE),
                    new ShipUpgradeStruct(ship_upgrade_level_limit, "Upgrade to twin-shot", SHAPE_TWIN, SHAPE_CIRCLE),
                    new ShipUpgradeStruct(ship_upgrade_level_limit, "Upgrade to machine gun", SHAPE_MACHINE_GUN, SHAPE_CIRCLE),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + ship_upgrade_step, "Upgrade to exploder", SHAPE_EXPLODER, SHAPE_RAMMER),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + ship_upgrade_step, "Upgrade to mimic", SHAPE_MIMIC, SHAPE_RAMMER),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + ship_upgrade_step, "Upgrade to cross", SHAPE_CROSS, SHAPE_FLANKER),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + ship_upgrade_step, "Upgrade to sniper", SHAPE_SNIPER, SHAPE_TWIN),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + 3 * ship_upgrade_step, "Upgrade to carrier", SHAPE_CARRIER, SHAPE_MACHINE_GUN),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + 2 * ship_upgrade_step, "Upgrade to gravity", SHAPE_GRAVITY, SHAPE_EXPLODER),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + 2 * ship_upgrade_step, "Upgrade to creeper", SHAPE_CREEPER, SHAPE_MIMIC),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + 2 * ship_upgrade_step, "Upgrade to all-direction", SHAPE_All_DIRECTIONS, SHAPE_CROSS),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + 2 * ship_upgrade_step, "Upgrade to seeker missile", SHAPE_SEEKER, SHAPE_SNIPER),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + 2 * ship_upgrade_step, "Upgrade to cannon", SHAPE_CANNON, SHAPE_SNIPER),
                    new ShipUpgradeStruct(ship_upgrade_level_limit + 3 * ship_upgrade_step, "Upgrade to cloak", SHAPE_CLOAK, SHAPE_CREEPER)
            };

    /** Gives the given ship the requested upgrade if possible. */
    static void levelUpShip(Ship s, int requested_upgrade)
    {
        for (ShipUpgradeStruct upgrade : ship_upgrade_list)
        {
            if(upgrade.ship_shape==requested_upgrade && upgrade.requirementsMet(s))
            {
                s.setShape(requested_upgrade);
                s.setAttributes();
                break;
            }
        }
    }
}