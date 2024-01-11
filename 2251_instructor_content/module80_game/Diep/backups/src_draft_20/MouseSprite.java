import java.awt.*;

/**
 * Sprite that keeps track of mouse x and y so that seekers can target the mouse.
 */
public class MouseSprite extends SpritePhysics
{
    private KeyManager k;
    private Player p;

    public MouseSprite(Player p, KeyManager k)
    {
        super(0, 0, 0, "", 0, 0);
        this.p = p;
        this.k = k;
    }


    /* The following 4 methods are the only important methods in this
     * class. */
    @Override
    public double getX(){return p.getXCenter()+k.getMouseX()-Main.ScreenWidth/2;}
    @Override
    public double getY(){return p.getYCenter()+k.getMouseY()-Main.ScreenHeight/2;}
    @Override
    public double getXCenter(){return p.getXCenter()+k.getMouseX()-Main.ScreenWidth/2;}
    @Override
    public double getYCenter(){return p.getYCenter()+k.getMouseY()-Main.ScreenHeight/2;}


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

    @Override
    void update(double v)
    {

    }

    @Override
    Rectangle getBoundingRectangle()
    {
        return null;
    }
}
