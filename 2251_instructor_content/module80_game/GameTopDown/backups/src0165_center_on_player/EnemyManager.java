import java.awt.*;
import java.util.HashMap;

/**
 * Created by nealh_000 on 12/22/2017.
 */
public class EnemyManager
{
    private HashMap<String,Enemy> enemies = new HashMap();

    public EnemyManager(Board board, AttackManager am, Player player1)
    {
        //Create a short range punching enemy.
        Enemy e = new Enemy("fighter",
                350,350,
                10.0,
                100,
                100,//pixels per second
                Color.YELLOW,
                40,
                true,
                am.getAttack("punch"),
                BoardTopDown.enemy_bitmask,//i am
                BoardTopDown.player_bitmask+BoardTopDown.enemy_bitmask,//I hit
                player1,
                board);
        enemies.put("fighter", e);

        //Create a long range shooter enemy
        e = new Enemy("archer",
                350,350,
                15.0,
                50,
                80,//pixels per second
                Color.GREEN,
                300,
                true,
                am.getAttack("shoot"),
                BoardTopDown.enemy_bitmask,//i am
                BoardTopDown.player_bitmask+BoardTopDown.enemy_bitmask,
                player1,
                board);
        enemies.put("archer", e);

        //Create a big bruiser enemy
        e = new Enemy("ogre",
                350,350,
                30.0,
                400,
                40,//pixels per second
                Color.RED,
                50,
                false,//do not avoid clumping. this makes the bruiser shoves others out of the way.
                am.getAttack("clobber"),
                BoardTopDown.enemy_bitmask,//i am
                BoardTopDown.player_bitmask+BoardTopDown.enemy_bitmask,
                player1,
                board);
        enemies.put("ogre", e);
    }

    public Enemy getEnemy(String key)
    {
        if(!enemies.containsKey(key))
        {
            System.out.println("ERROR in EnemyManager.getEnemy. No key '"+key+"'. Exiting.");
            System.exit(1);
        }
        return enemies.get(key).getCopy();
    }
}