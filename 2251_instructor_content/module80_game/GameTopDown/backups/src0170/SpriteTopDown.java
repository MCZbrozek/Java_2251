import java.awt.*;

/**
 * Created by nealh_000 on 5/22/2017.
 * This class implements some functionality common to all sprites in
 * this top down game.
 */
public abstract class SpriteTopDown extends SpriteCircular
{
    protected Color color = Color.WHITE;
    private int health=0;
    private int max_health=1;

    public SpriteTopDown(double x, double y,
                          int health,
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
        this.health = health;
        this.max_health = health;
    }

    /** Override parent class's draw. */
    public void draw(Board b, Graphics g, int offset_x, int offset_y)
    {
        //Determine whether or not to draw the sprite
        if(this.isVisible())
        {
            Graphics2D g2d = (Graphics2D) g;
            //Update the on_screen status of this sprite.
			int draw_x = (int)(this.x + offset_x);
			int draw_y = (int)(this.y + offset_y);
			this.setIsOnScreen(draw_x, draw_y);
            if(this.getIsOnScreen())
            {   //Draw sprite as a circle.
                g2d.setColor(color);
                g2d.fillOval(draw_x,draw_y,
                        (int)this.getCollisionRadius()*2,
                        (int)this.getCollisionRadius()*2);
                //Draw orientation circle
                double heading_adjust = 40;
                double dx = Math.cos(this.getAngle())*heading_adjust;
                double dy = Math.sin(this.getAngle())*heading_adjust;
                double heading_radius = this.getCollisionRadius() / 3;
                g2d.fillOval((int)(this.getXCenter() + dx - heading_radius + offset_x),
                        (int)(this.getYCenter() + dy - heading_radius + offset_y),
                        (int)heading_radius*2, //width
                        (int)heading_radius*2); //height
                //Draw health if health is less than 100%
                if(this.getPercentHealth() < 1.0)
                {
                    g2d.setColor(Color.RED);
                    g2d.fillRect((int) this.getX() + offset_x, (int) (this.getY() + offset_y + this.getCollisionRadius() * 1.8), (int) (this.getCollisionRadius() * 2), 5); //height
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect((int) this.getX() + offset_x, (int) (this.getY() + offset_y + this.getCollisionRadius() * 1.8), (int) (this.getCollisionRadius() * 2 * this.getPercentHealth()), 5); //height
                }
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