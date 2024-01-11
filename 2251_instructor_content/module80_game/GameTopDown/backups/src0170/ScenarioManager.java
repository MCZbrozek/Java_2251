import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by nealh_000 on 12/22/2017.
 */
public class ScenarioManager extends SpritePhysics
{
    private Stack action_stack = new Stack();
    private Board board;
    private EnemyManager enemies;
    //TODO LEFT OFF HERE - move the next two variables into a condition object which you need to create.
    //How long until next enemy cluster spawns
    private double spawn_cooldown = 0;
    private double spawn_reset = 10.0;

    //Keep track of all spawned enemies that are still alive.
    private ArrayList<Enemy> active_enemies = new ArrayList<Enemy>();

    public ScenarioManager(Board board, EnemyManager enemies)
    {
        super(0,0,0,"",0,0);
        this.board = board;
        this.enemies = enemies;
        //TODO LEFT OFF HERE
        /*Create an event that spawns units when there are no more
        * units around and create another event that pushes the first one on the stack.
        * You may need a list of actions for each event so that multiple actions can take place.
        * Then test out spawning 3 different sets of units in a row by
        * putting 3 different events on the stack. */
    }

    public void update(double elapsed_seconds)
    {
        //Pop current item on the stack. If condition is met then execute it.
        //else push it back on the stack.
        Enemy e;
        spawn_cooldown -= elapsed_seconds;
        if(spawn_cooldown < 0)
        {
            //Check for active enemies remaining. Remove any that are dead
            for(int i=active_enemies.size()-1; i>=0; i--)
            {
                e = active_enemies.get(i);
                if(e.getHealth()<=0)
                {
                    active_enemies.remove(i);
                }
            }
            //Do not continue if active enemies remain
            if(active_enemies.size()>0){return;}
            //Create a short range punchy enemy
            for(int i=0 ; i<3; i++)
            {
                e = enemies.getEnemy("fighter");
                board.addSprite(e);
                active_enemies.add(e);
            }
            //Create a long range shooter enemy
            for(int i=0 ; i<3; i++)
            {
                e = enemies.getEnemy("archer");
                board.addSprite(e);
                active_enemies.add(e);
            }
            //Create a big bruiser enemy
            e = enemies.getEnemy("ogre");
            board.addSprite(e);
            active_enemies.add(e);

            spawn_cooldown = spawn_reset;
        }
    }

    @Override
    Rectangle getBoundingRectangle(){return null;}

    @Override
    void handleCollision(SpritePhysics spritePhysics){}

    @Override
    boolean checkCollided(SpritePhysics spritePhysics){return false;}

    @Override
    void shoveOut(SpritePhysics spritePhysics){}

    private class GameEvent
    {
        private GameCondition condition;
        private GameAction action;
        GameEvent(GameCondition condition, GameAction action)
        {
            this.condition = condition;
            this.action = action;
        }

        public void update(double elapsed_seconds)
        {
            this.condition.update(elapsed_seconds);
        }

        public boolean conditionIsMet()
        {
            return condition.conditionIsMet();
        }

        public void act()
        {
            this.action.performAction();
        }
    }

    /** GameCondition: for now the condition is either a time limit or
     * when the list of enemies is empty. */
    private class GameCondition
    {
        private boolean no_more_enemies = false;
        private double time_out = Double.MAX_VALUE;
        GameCondition(boolean no_more_enemies)
        {
            this.no_more_enemies = no_more_enemies;
        }
        GameCondition(double time_out)
        {
            this.time_out = time_out;
        }

        public void update(double elapsed_seconds)
        {
            this.time_out -= elapsed_seconds;
        }

        public boolean conditionIsMet()
        {
            if(no_more_enemies)
            {
                return active_enemies.size() == 0;
            }
            else
            {
                return this.time_out < 0;
            }
        }
    }

    /** GameAction: for now the action is either spawning
     * units or pushing new GameEvents on the stack. */
    private class GameAction
    {
        private static final int SPAWN = 0;
        private static final int PUSH = 1;
        private int type;
        //Attributes used for spawning
        private String unit;
        private int number;
        //Attribute used for pushing on the stack
        private GameEvent to_push;

        /*Two different constructors. One for spawning
        * units and one for pushing GameEvents on the stack. */
        GameAction(String unit, int number)
        {
            this.type = SPAWN;
            this.unit = unit;
            this.number = number;
        }
        GameAction(GameEvent to_push)
        {
            this.type = PUSH;
            this.to_push = to_push;
        }

        /*For now either spawn more units or push a GameEvent on the
        * stack. */
        public void performAction()
        {
            if(this.type == SPAWN)
            {
                Enemy e;
                for(int i=0 ; i<number; i++)
                {
                    e = enemies.getEnemy(unit);
                    board.addSprite(e);
                    active_enemies.add(e);
                }
            }
            else //if(this.type == PUSH)
            {
                action_stack.push(to_push);
            }
        }
    }
}
