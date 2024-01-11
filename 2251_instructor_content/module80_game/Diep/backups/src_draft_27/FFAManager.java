import java.awt.*;
import java.util.ArrayList;

/** This object gradually decreases the arena size in the FFA. */
class FFAManager extends SpritePhysics
{
    private BoardTopDown board_reference;
    private double countdown = 40;
    private boolean shrink_map = true;

    FFAManager(BoardTopDown b, boolean shrink_map)
    {
        super(0,0, 0,"", 0, 0);
        board_reference = b;
        this.shrink_map = shrink_map;
    }

    @Override
    void update(double elapsed_seconds)
    {
        //Make 800 pixels the minimum arena size.
        //Only decrease the countdown if the arena is larger.
        if(Constants.boundary_down>800)
        {
            countdown -= elapsed_seconds;
        }
        if(countdown < 0 && this.shrink_map)
        {   //Decrease the size of the game by 50 pixels in both directions
            Constants.boundary_down -= 50;
            Constants.boundary_right -= 50;
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
            //Reset countdown to 30 seconds
            countdown = 30;
            //Spawn a flock of non-respawning suicide bricks
            Constants.loadExperienceBricks(board_reference,
                    (int)(Math.random()*Constants.suicide_brick_count),
                    1, 20, 5, Color.PINK,
                    Constants.SHAPE_TRIANGLE, true,
                    1, false, Constants.default_friction);
            //Spawn a powerup
            PowerUp p = new PowerUp(
                    Constants.getRandomX(),
                    Constants.getRandomY(),
                    1,
                    0,
                    "images/powerup.png",
                    BoardTopDown.xp_brick_bitmask,
                    BoardTopDown.everything_bitmask,
                    board_reference);
            board_reference.addSprite(p);
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
