import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/* This class displays a line on the screeen. */
public class DisplayLine extends SpriteRectangular
{
    //Where the beam ends
    private double end_x;
    private double end_y;
    //How long to draw the line in seconds before removing it from the screen
    private double draw_timeout;
    private Color color;
    private int beam_width;

    public DisplayLine(double x, double y,
                       double draw_countdown,
                       double end_x,
                       double end_y,
                       int width,
                       Color c)
    {
        super(x, y,
                0,
                "", 0, 0);
        this.draw_timeout = draw_countdown;
        this.end_x = end_x;
        this.end_y = end_y;
        this.beam_width = width;
        this.color = c;
    }

    /* Override sprite's draw method. */
    public void draw(Board b, Graphics g,
                     int offset_x, int offset_y)
    {
        /*if(true)
        {	//Draw collision rectangle
            g.setColor(Color.BLUE);
            g.fillRect((int)this.getX()+offset_x,
                    (int)this.getY()+offset_y,
                    this.getWidth(), this.getHeight());
        }*/
        Graphics2D g2d = (Graphics2D)g;
        g2d.setStroke(new BasicStroke(this.beam_width));
        g2d.setColor(this.color); //animation beam
        g2d.drawLine(
                (int)this.getXCenter()+offset_x,
                (int)this.getYCenter()+offset_y,
                (int)this.end_x+offset_x,
                (int)this.end_y+offset_y);
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