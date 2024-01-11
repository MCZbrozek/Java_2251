import java.awt.*;
import java.util.Random;

/**
 * Created by nealh_000 on 12/29/2017.
 */
class Constants
{
    private static Random random_generator = new Random();

    static final int[] xp_per_level = {2, 5, 8, 11, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 45};

    /*attribute_level_limit is the smallest index that is valid in all the
    level arrays below. Making this integer too large will result in an Index
    Array Out Of Bounds Exception.*/
    static final int attribute_level_limit = 4;

    static final double[] speed_levels = {100, 150, 200, 250, 300};
    static final int[] bullet_damage_levels = {1, 2, 3, 4, 5};
    static final double[] bullet_speed_levels = {300, 340, 380, 420, 460};
    static final double[] refire_rate_levels = {0.8, 0.7, 0.6, 0.5, 0.3};
    static final double[] bullet_longevity_levels = {1.5, 2, 2.5, 3, 3.5};
    static final int[] health_levels = {50, 75, 100, 125, 150};
    static final double[] regen_levels = {8, 5, 3, 1, 0.5};
    static final int[] collision_damage_levels = {1, 2, 3, 5, 7};
    static final int[] bullet_health_levels = {1, 2, 4, 6, 8};

    static final String[] attribute_names = {"Speed", "Bullet Damage", "Bullet Speed", "Bullet Longevity", "Refire Rate", "Health", "Health Regen", "Collision Damage", "Penetration"};
    static final Color[] attribute_colors = {Color.cyan, Color.ORANGE, Color.YELLOW, Color.WHITE, Color.MAGENTA, Color.GREEN, Color.BLUE, Color.red, Color.PINK};

    static String getRandomAttribute()
    {
        return attribute_names[random_generator.nextInt(attribute_names.length)];
    }

}
