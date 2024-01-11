import java.awt.*;

/**
 * Created by nealh_000 on 5/22/2017.
 * This class implements some functionality common to all sprites in
 * this top down game.
 */
public abstract class SpriteTopDown extends SpriteCircular
{
    protected Color color = Color.WHITE;
    //Health
    private int health=0;
    private int max_health=1;

    public SpriteTopDown(double x, double y,
                          double angle,
                          double radius,
                          String image_file,
                          int i_am_bitmask,
                          int i_hit_bitmask)
    {
        super(x, y,
                angle,
                radius,
                image_file,
                i_am_bitmask, i_hit_bitmask);
    }

    /**  */
    public void draw(Board b, Graphics g)
    {
        //Determine whether or not to draw the sprite
        if(this.isVisible())
        {
            Graphics2D g2d = (Graphics2D) g;
            //Update the on_screen status of this sprite.
            this.setIsOnScreen((int)this.getX(), (int)this.getY());
            if(this.getIsOnScreen())
            {   //Draw sprite as a circle.
                g2d.setColor(color);
                g2d.fillOval((int)(this.getX()),
                        (int)(this.getY()),
                        (int)this.getCollisionRadius()*2,
                        (int)this.getCollisionRadius()*2);
                //Draw orientation circle
                double heading_adjust = 40;
                double dx = Math.cos(this.getAngle())*heading_adjust;
                double dy = Math.sin(this.getAngle())*heading_adjust;
                double heading_radius = this.getCollisionRadius() / 3;
                g2d.fillOval((int)(this.getXCenter() + dx - heading_radius),
                        (int)(this.getYCenter() + dy - heading_radius),
                        (int)heading_radius*2, //width
                        (int)heading_radius*2); //height
                //Draw health
                g2d.setColor(Color.RED);
                g2d.fillRect((int)this.getX(), (int)(this.getY()+this.getCollisionRadius()*1.8),
                        (int)(this.getCollisionRadius()*2),
                        5); //height
                g2d.setColor(Color.GREEN);
                g2d.fillRect((int)this.getX(), (int)(this.getY()+this.getCollisionRadius()*1.8),
                        (int)(this.getCollisionRadius()*2*this.getPercentHealth()),
                        5); //height
            }
        }
    }

    public void setHealth(int health){ this.health = health; }
    public void setMaxHealth(int health){ this.max_health = health; }
    public int getHealth(){ return health; }
    private double getPercentHealth(){ return ((double)health)/((double)max_health); }
    public void takeDamage(int damage)
    {
        health = Math.min(health - damage, max_health);
    }
}