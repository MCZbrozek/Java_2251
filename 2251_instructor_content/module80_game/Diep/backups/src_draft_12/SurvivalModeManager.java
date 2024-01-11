import java.awt.*;
import java.util.ArrayList;

class SurvivalModeManager extends SpritePhysics
{
    private Board board_reference;
    private Player player_reference;
    private ArrayList<SpriteTopDown> wave = new ArrayList<>();
    private int level = 0;

    SurvivalModeManager(Board b, Player p)
    {
        super(0,0, 0,"", 0, 0);
        board_reference = b;
        player_reference = p;
        fillWave();
    }

    @Override
    void update(double elapsed_seconds)
    {
        for (int i = wave.size()-1; i>=0; i--)
        {
            if(wave.get(i).getHealth()<=0)
            {
                wave.remove(i);
            }
        }
        if(wave.size()==0)
        {
            level++;
            fillWave();
        }
    }

    @Override
    Rectangle getBoundingRectangle()
    {
        return null;
    }

    private void fillWave()
    {
        for(int i=0; i<level; i++)
        {
            Enemy e = new Enemy(
                    Constants.getRandomX(),
                    Constants.getRandomY(),
                    "Killer",
                    Color.orange,
                    20,
                    BoardTopDown.ship_bitmask,//I am
                    BoardTopDown.everything_bitmask,//I hit
                    Enemy.TERMINATOR_SHOOTER,
                    board_reference);
            e.lockTarget(player_reference);
            e.setAttributes(250, 1, 350,
            1, 8, 3, 2.0,
                    1, 1);
            board_reference.addSprite(e);
            wave.add(e);
        }
        Constants.loadExperienceBricks(this.board_reference,
                level,
                1, 10, 5, Color.PINK,
                Constants.SHAPE_TRIANGLE, true, 1, false);
    }

    @Override
    void handleCollision(SpritePhysics spritePhysics)
    {

    }

    @Override
    boolean checkCollided(SpritePhysics spritePhysics)
    {
        return false;
    }

    @Override
    void shoveOut(SpritePhysics spritePhysics)
    {

    }
}
