import java.awt.*;

class ExperienceBrick extends SpriteTopDown
{

    ExperienceBrick(double x, double y,
                    double collision_radius,
                    int health,
                    int xp_reward,
                    Color color,
                    int i_am_bitmask,
                    int i_hit_bitmask)
    {
        super(x, y,
                health,
                0,
                collision_radius,
                xp_reward,
                i_am_bitmask,
                i_hit_bitmask);
        this.color = color;
        this.no_direction_draw = true;
        this.moveToCenter(x,y);
    }

    /**
     * Override parent class's draw so you can draw it as a square.
     */
    public void draw(Board b, Graphics g, int offset_x, int offset_y)
    {
        //Determine whether or not to draw the sprite
        if (this.isVisible())
        {
            int draw_x = (int) (this.getX() + (double) offset_x);
            int draw_y = (int) (this.getY() + (double) offset_y);
            Graphics2D g2d = (Graphics2D) g;
            //Update the on_screen status of this sprite.
            this.setIsOnScreen(draw_x, draw_y);
            if (this.getIsOnScreen())
            {   //Draw sprite as a circle.
                g2d.setColor(color);
                g2d.fillRect(draw_x, draw_y, (int) this.getCollisionRadius() * 2, (int) this.getCollisionRadius() * 2);
                if (this.getPercentHealth() < 1.0)
                {   //Draw health
                    g2d.setColor(Color.RED);
                    g2d.fillRect(draw_x, (int) (draw_y + this.getCollisionRadius() * 1.8), (int) (this.getCollisionRadius() * 2), 5); //height
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(draw_x, (int) (draw_y + this.getCollisionRadius() * 1.8), (int) (this.getCollisionRadius() * 2 * this.getPercentHealth()), 5); //height
                }
            }
        }
    }

    public void takeDamage(int damage, Sprite damage_dealer)
    {
        super.takeDamage(damage, damage_dealer);
        //If this experience brick is dead, relocate it and reset its health
        if(this.getHealth()<=0)
        {
            this.setHealthMax();
            this.setX(Constants.getRandomX());
            this.setY(Constants.getRandomY());
        }
    }
}