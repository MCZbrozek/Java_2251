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
    static int suicide_brick_count = 2;

    static final double suicide_brick_speed = 230;

    //Recoil from shooting
    static final double recoil = 40.0;
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
    static int[] bullet_damage_levels = {1, 2, 3, 4, 5, 6, 7, 8};
    static double[] bullet_speed_levels = {300, 340, 380, 420, 460, 500, 540, 580};
    static final double[] refire_rate_levels = {0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2};
    static final double[] bullet_longevity_levels = {1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5};
    static final int[] health_levels = {50, 75, 100, 125, 150, 175, 200, 225};
    static final double[] regen_levels = {6, 5, 4, 3, 2, 1, 0.5, 0.25};
    static final int[] collision_damage_levels = {1, 2, 3, 4, 5, 6, 7, 8};
    static final int[] bullet_health_levels = {1, 2, 4, 6, 8, 10, 12, 14};

    static final String[] attribute_names = {"Speed", "Bullet Damage", "Bullet Speed", "Bullet Longevity", "Refire Rate", "Health", "Health Regen", "Collision Damage", "Penetration"};
    static final Color[] attribute_colors = {Color.cyan, Color.ORANGE, Color.YELLOW, Color.WHITE, Color.MAGENTA, Color.GREEN, Color.BLUE, Color.red, Color.PINK};

    //Whether or not bullets default to exploding and other
    //bullet explosion parameters.
    static boolean bullet_explodes_default = false;
    static double explosion_distance = 300;
    static double explosion_impulse = 500;
    static int explosion_damage = 1;

    static final int SHAPE_RECT = 0;
    static final int SHAPE_CIRCLE = 1;
    static final int SHAPE_TRIANGLE = 2;
    static final int SHAPE_IMAGE = 3;

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
    static final int NUM_POWERUPS = 10;

    static final String[] POWERUP_NAMES = {"Triple-Shot",
                                        "Invincibility",
                                        "Spread-Shot",
                                        "Speed-Up",
                                        "Friction-Off",
                                        "Shove",
                                        "Goliath",
                                        "Mimic",
                                        "Explosive",
                                        "Blackhole"};

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
                            Constants.getRandomX(),
                            Constants.getRandomY(),
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
}