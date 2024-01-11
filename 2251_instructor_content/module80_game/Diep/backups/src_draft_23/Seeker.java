import java.awt.*;
import java.util.ArrayList;

//Setting the speed of Seeker to zero creates a more accurate (same speed)
//version of the seeker. Speed must be set to zero so the parent class's
//update function doesn't cause the bullet to move.
public class Seeker extends Shot
{
    private BoardTopDown board;
    SpritePhysics target = null;
    private double backup_speed;
    //Beam width gets wider until target is acquired.
    private int beam_width = 0;

    Seeker(double x, double y,
         int i_am_bitmask,
         int i_hit_bitmask,
         Ship shooter,
         double angle,
         double timeout,
         int damage,
         double speed,
         int health,
         int radius,
         boolean does_explode,
         BoardTopDown board)
    {
        super(x,y,
                i_am_bitmask,
                i_hit_bitmask,
                shooter,
                angle,
                timeout,
                damage,
                speed,
                health,
                radius,
                does_explode,
                false,
                board);
        this.board = board;
        this.speed = speed;
        this.backup_speed = speed;
        this.color = Color.MAGENTA;
        this.shape = Constants.SHAPE_TRIANGLE;
    }

    /* This method is intended to be called once per frame and will
     * update this sprite. */
    public void update(double elapsed_seconds)
    {
        this.countdown(elapsed_seconds);
        //Find a target by beaming a line straight ahead
        if(this.target==null)
        {   //Beam width gets wider until target is acquired.
            this.beam_width++;
            //Look ahead for a target
            ArrayList<SpritePhysics> sprites_ahead =
                    this.board.getLineCollisions(
                            this.getXCenter(),
                            this.getYCenter(),
                            this.getAngle(),
                            1000,
                            beam_width);
            for (SpritePhysics sp : sprites_ahead)
            {
                //If the sprite is not me and is not another bullet.
                //If the sprite is to my left, I must be headed left in order
                //to engage it. If the sprite is to my right, I must be headed
                //right in order to engage it
                if(sp != this && !(sp instanceof Shot) && !(sp instanceof Seeker) &&
                        (   (this.getdx()<0 && sp.getXCenter()<this.getXCenter()) ||
                            (this.getdx()>=0 && sp.getXCenter()>this.getXCenter())
                        )
                  )
                {   //Get the nearest target
                    if(this.target == null || this.distanceTo(this.target)>this.distanceTo(sp))
                    {
                        this.target = sp;
                    }
                }
            }
            this.move(elapsed_seconds);
        }
        else
        {
            //Setting the speed of Seeker to zero creates a more accurate (same speed)
            //version of the seeker. Speed must be set to zero so the parent class's
            //update function doesn't cause the bullet to move.
            if(this.speed == 0)
            {
                this.move(this.angleToSprite(target), this.backup_speed*elapsed_seconds);
                //Draw line to target
                board.addSpriteIntangible(new DisplayLine(this.getXCenter(), this.getYCenter(),
                        0.01, target.getXCenter(), target.getYCenter(),
                        1, Color.MAGENTA));
            }
            else
            {
                double angle_to = this.angleToSprite(target);
                this.changeVelocity(Math.cos(angle_to)*this.speed*elapsed_seconds,
                        Math.sin(angle_to)*this.speed*elapsed_seconds);
                this.move(elapsed_seconds);
                //Draw line to target
                board.addSpriteIntangible(new DisplayLine(this.getXCenter(), this.getYCenter(),
                        0.12, target.getXCenter(), target.getYCenter(),
                        1, Color.MAGENTA));
            }
        }
    }
}