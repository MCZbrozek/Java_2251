import java.awt.*;

class Dazzle extends SpriteTopDown
{
    private double timeout = 0.1;

    Dazzle(double x, double y)
    {
        super(  x, y,
                1,
                0,
                1,
                0,
                Constants.SHAPE_IMAGE,
                0,
                0,
                null);
        this.no_direction_draw = true;
        this.no_health_draw = true;
        this.moveToCenter(x+100*(Math.random()-0.5),y+100*(Math.random()-0.5));
        this.setImage("../images/star1.png", Math.PI*Math.random());
    }

    /* This method is intended to be called once per frame and will
     * update this sprite. */
    public void update(double elapsed_seconds)
    {
        timeout -= elapsed_seconds;
        if(timeout<0)
        {
            this.setRemoveMeTrue();
        }
    }
}