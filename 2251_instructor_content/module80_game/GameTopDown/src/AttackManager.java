import java.util.HashMap;

public class AttackManager
{
    private HashMap<String,Attack> attacks = new HashMap<String,Attack>();

    public AttackManager(Board board)
    {
        //Punch attack used yellow enemies.
        //I'm experimenting with making this a very short range projectile
        //the way a punch would fly forward toward the receiver.
        Attack temp = new Attack("punch",
                10,//pixels
                5,//pixels
                0.01,//seconds
                50,
                false,
                1000,//pixels per second
                0.4,//seconds
                board);
        attacks.put("punch", temp);

        //Strong punch with knockback used by big red enemies.
        //I'm experimenting with making this a very short range projectile
        //the way a punch would fly forward toward the receiver.
        temp = new Attack("clobber",
                20,//pixels
                5,//pixels
                0.01,//seconds
                100,
                true,
                1000,//pixels per second
                1.0,//seconds
                board);
        attacks.put("clobber", temp);

        //Ranged attack used by player and green enemies
        temp = new Attack("shoot",
                40,//pixels
                5,//pixels
                2.5,//seconds
                34,
                false,
                400,//pixels per second
                0.2,//seconds
                board);
        attacks.put("shoot", temp);

        //Explosive attack centered on player with knockback
        temp = new Attack("bomb",
                0,//pixels
                300,//pixels
                0,//seconds
                20,
                true,
                0,//pixels per second
                3.0,//seconds
                board);
        attacks.put("bomb", temp);

        //Like the bomb, but steals health
        temp = new Attack("health steal bomb",
                0,//pixels
                200,//pixels
                0,//seconds
                10,
                true,
                0,//pixels per second
                3.0,//seconds
                board);
        temp.setHealthSteal(true);
        attacks.put("health steal bomb", temp);

        //Dash attack with knockback
        temp = new Attack("dash",
                0,//pixels
                60,//pixels
                0.25,//seconds
                10,
                true,
                2000,//pixels per second
                3.0,//seconds
                board,
                true,
                true);
        attacks.put("dash", temp);
    }

    public Attack getAttack(String key)
    {
        if(!attacks.containsKey(key))
        {
            System.out.println("ERROR in AttackManager.getAttack. No key '"+key+"'. Exiting.");
            System.exit(1);
        }
        return attacks.get(key).getCopy();
    }
}
