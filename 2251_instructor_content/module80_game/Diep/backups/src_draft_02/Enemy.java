import java.awt.*;
import java.util.ArrayList;

public class Enemy extends Ship
{
    //Current target of this Enemy
    private SpriteTopDown target = null;
    //Cooldown before next checking for a new target to shoot
    private double targeting_cooldown = 0;
    //Check for a new target every targeting_cooldown_reset seconds
    private final double targeting_cooldown_reset = 2.5;

    //Ship that most recently shot this Enemy
    private Ship shot_by = null;
    //Cooldown and cooldown reset for running away in seconds
    private double flee_cooldown = 0;
    private final double flee_cooldown_reset = 5.0;

    public Enemy(double x, double y,
                 String name,
                 Color color,
                 double collision_radius,
                 int i_am_bitmask,
                 int i_hit_bitmask,
                 Board board)
    {
        super(x, y,
                name,
                collision_radius,
                i_am_bitmask,
                i_hit_bitmask,
                BoardTopDown.bullet_bitmask,//I am
                BoardTopDown.everything_bitmask,//I hit
                board);
        this.color = color;
    }

    private void updateTarget(double elapsed_seconds)
    {
        //Remove dead targets
        if(target!= null && target.getHealth()<=0)
        {
            target = null;
        }
        //Check if it's time to get a new target
        targeting_cooldown -= elapsed_seconds;
        if (target == null || targeting_cooldown < 0)
        {
            //Get all the sprites
            ArrayList<SpritePhysics> nearby_sprites = board_reference.getSpriteList();
            //Find the nearest sprite in the list
            double smallest_distance = Double.MAX_VALUE;
            int index = 0;
            double temp_distance;
            SpritePhysics temp_sprite;
            for(int i=0; i<nearby_sprites.size(); i++)
            {
                temp_sprite = nearby_sprites.get(i);
                //Only seek bricks and Ships that are not self.
                if(temp_sprite instanceof ExperienceBrick || (temp_sprite instanceof Ship && temp_sprite!=this))
                {
                    temp_distance = this.distanceTo(temp_sprite);
                    if (temp_distance < smallest_distance)
                    {
                        smallest_distance = temp_distance;
                        index = i;
                    }
                }
            }
            target = (SpriteTopDown)nearby_sprites.get(index);
            targeting_cooldown = targeting_cooldown_reset;
        }
    }

    private void moveToward(double elapsed_seconds, double x, double y)
    {
        if(y < this.getYCenter())
        {	//up
            this.changeVelocity(0, -speed*elapsed_seconds);
        }else
        {   //down
            this.changeVelocity(0, speed*elapsed_seconds);
        }
        if(x < this.getXCenter())
        {	//left
            this.changeVelocity(-speed*elapsed_seconds, 0);
        }else
        {   //right
            this.changeVelocity(speed*elapsed_seconds, 0);
        }
    }

    private void moveAwayFrom(double elapsed_seconds, double x, double y)
    {
        if(y < this.getYCenter())
        {	//down
            this.changeVelocity(0, speed*elapsed_seconds);
        }else
        {   //up
            this.changeVelocity(0, -speed*elapsed_seconds);
        }
        if(x < this.getXCenter())
        {	//right
            this.changeVelocity(speed*elapsed_seconds, 0);
        }else
        {   //left
            this.changeVelocity(-speed*elapsed_seconds, 0);
        }
    }

    /* This method is intended to be called once per frame and will
     * update this sprite. */
    public void update(double elapsed_seconds)
    {
        if(flee_cooldown>0)
        {   //Orient gun toward the ship that shot me
            this.setAngle(this.angleToSprite(shot_by));
            //Run away
            this.moveAwayFrom(elapsed_seconds, shot_by.getXCenter(), shot_by.getYCenter());
            flee_cooldown -= elapsed_seconds;
        }
        else
        {   //Choose a target
            this.updateTarget(elapsed_seconds);
            //Orient toward target
            this.setAngle(this.angleToSprite(target));
            //Impulse toward target
            this.moveToward(elapsed_seconds, target.getXCenter(), target.getYCenter());
        }
        this.attack();
        //Apply friction, move, and stay in bounds.
        super.update(elapsed_seconds);
        //Level up if possible
        this.levelUp();
    }

    private void levelUp()
    {
        if(availableLevelUpPoints()>0)
        {
            String name = Constants.getRandomAttribute();
            levelUpAttribute(name);
            //System.out.println("Enemy leveled up "+name);
        }
    }

    /** Alert this enemy that it was damaged by another ship so it can respond. */
    public void takeDamage(int damage, Sprite damage_dealer)
    {
        super.takeDamage(damage, damage_dealer);
        //Remember the responsible Ship
        if(damage_dealer instanceof Ship)
        {
            shot_by = (Ship)damage_dealer;
            //Engage flight behavior
            flee_cooldown = flee_cooldown_reset;
        }
        else if(damage_dealer instanceof Shot)
        {
            shot_by = ((Shot)damage_dealer).getShooter();
            //Engage flight behavior
            flee_cooldown = flee_cooldown_reset;
        }
    }

    /** Override parent class's draw. */
    @Override
    public void draw(Board b, Graphics g, int offset_x, int offset_y)
    {
        super.draw(b, g, offset_x, offset_y);
        //Determine whether or not to draw the sprite's name
        if (this.isVisible())
        {
            int draw_x = (int) (this.getX() + (double) offset_x);
            int draw_y = (int) (this.getY() + (double) offset_y);
            if (this.getIsOnScreen())
            {   //Draw this enemy's name
                g.setColor(Color.WHITE);
                Font font = new Font("Serif", Font.BOLD, 18);
                g.setFont(font);
                g.drawString(name, draw_x-30, draw_y+(int)this.getCollisionRadius());
            }
        }
    }

}