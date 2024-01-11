import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/* This class displays a dot on the screeen. */
public class DisplayDot extends SpriteCircular
{
    private double radius;
    //How long to draw the dot in seconds before removing it from the screen
    private double draw_timeout;
    private Color color;

    public DisplayDot(double xcenter, double ycenter,
                       double draw_countdown,
                       double radius,
                      Color c)
    {
        super(xcenter, ycenter,
                0,
                radius,
                "", 0, 0);
        this.draw_timeout = draw_countdown;
        this.radius = radius;
        this.color = c;
    }

    /* Override sprite's draw method. */
    public void draw(Board b, Graphics g,
                     int offset_x, int offset_y)
    {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(this.color); //animation beam
        g2d.fillOval((int)(this.getX()-this.radius),
                (int)(this.getY()-this.radius),
                (int)(radius*2),
                (int)(radius*2));
    }

    public void update(double elapsed_seconds)
    {
        //Countdown to remove the beam from the screen
        draw_timeout -= elapsed_seconds;
        if(draw_timeout < 0)
        {
            this.setRemoveMeTrue();
        }
    } //public void update(Craft craft, ArrayList<Sprite> sprite_list, int index)

    public void handleCollision(SpritePhysics other){}
}