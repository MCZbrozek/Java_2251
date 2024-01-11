import java.awt.*;
import java.util.ArrayList;

/** This object gradually decreases the arena size in the FFA. */
class FFAManager extends SpritePhysics
{
    private BoardTopDown board_reference;
    private double countdown = 40;

    FFAManager(BoardTopDown b)
    {
        super(0,0, 0,"", 0, 0);
        board_reference = b;
    }

    @Override
    void update(double elapsed_seconds)
    {
        countdown -= elapsed_seconds;
        if(countdown < 0 && Constants.boundary_down>1000)
        {   //Decrease the size of the game by 100 pixels in both directions
            Constants.boundary_down -= 100;
            Constants.boundary_right -= 100;
            //Remove some bricks so the game doesn't get too crowded.
            int bricks_to_remove = 2;
            ArrayList<SpritePhysics> sprites = board_reference.getSpriteList();
            ExperienceBrick brick;
            for (SpritePhysics s:sprites)
            {   //Tell experience bricks not to respawn when they die.
                if(s instanceof ExperienceBrick)
                {
                    brick = (ExperienceBrick)s;
                    if(brick.regenerate)
                    {
                        brick.regenerate = false;
                        bricks_to_remove--;
                    }
                }
                //Stop when enough bricks have been removed
                if(bricks_to_remove<=0){break;}
            }
            //Reset countdown to 40 seconds
            countdown = 40;
        }
    }

    @Override
    Rectangle getBoundingRectangle()
    {
        return null;
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
