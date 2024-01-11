import java.awt.*;

/**
 * Created by nealh_000 on 12/29/2017.
 */
class LevelUpInterface
{
    //Constants for positioning the interface
    private final int x = 40;
    private final int y = 100;
    private final int width = 15;
    private final int height = 10;
    private final int buffer = 10;
    private final int width_buffer = 2;

    private PlusSign[] plus_signs = new PlusSign[Constants.attribute_names.length];
    //The ship that this interface is displaying for
    private Ship focused;
    private Board b;

    private boolean display = false;

    LevelUpInterface(Ship s, Board b)
    {
        this.focused = s;
        this.b = b;
        //Populate plus signs
        for (int i = 0; i < plus_signs.length; i++)
        {
            String name = Constants.attribute_names[i];
            int level = focused.getAttributeLevel(name);
            plus_signs[i] = new PlusSign(
                    name.length()*10+x+level*(width+width_buffer),
                    y+i*(height+buffer)-2,
                    focused, i);
        }
    }

    void toggleDisplay(){display = !display;}

    //Change which ship this interface is set up for
    void setFocusedShip(Ship s)
    {
        this.focused = s;
        for(PlusSign ps:plus_signs)
        {
            ps.setFocusedShip(s);
        }
    }

    void draw(Graphics g)
    {
        int levelup_points = focused.availableLevelUpPoints();
        if(levelup_points>0 || display)
        {
            g.drawString("Available Points: "+levelup_points,x,y-buffer);
            for (int i = 0; i <Constants.attribute_names.length; i++)
            {
                String name = Constants.attribute_names[i];
                g.setColor(Constants.attribute_colors[i]);
                int level = focused.getAttributeLevel(name);
                for (int j=0; j<level; j++)
                {
                    g.fillRect(x+j*(width+width_buffer),y+i*(height+buffer), width, height);
                }
                g.drawString(name,x+level*(width+width_buffer),y+i*(height+buffer)+12);
                //If this attribute is not max level and levelup points
                //are available and the focused ship is a player,
                //then draw a plus sign to be clicked on
                if(!plus_signs[i].maxedOut() && levelup_points>0 && focused instanceof Player)
                {
                    plus_signs[i].draw(b,g);
                }
            }
        }
    }

    //If one of the plus signs contains the coordinates then level it up
    void levelUp(double x, double y)
    {
        for (PlusSign p:plus_signs)
        {
            if(!p.maxedOut() && p.containsClick(x,y))
            {
                p.levelUp();
                return;
            }
        }
    }





    private class PlusSign extends Sprite
    {
        private Ship focused;
        private int attribute_index;

        PlusSign(double x, double y, Ship focused, int attribute_index)
        {
            super(x, y, 0, "images/plus.png", 0, 0);
            this.setImage(this.getImageString(), 0);
            this.focused = focused;
            this.attribute_index = attribute_index;
        }

        boolean maxedOut()
        {
            return Constants.attribute_level_limit <= focused.getAttributeLevel(Constants.attribute_names[attribute_index]);
        }

        void levelUp()
        {
            focused.levelUpAttribute(Constants.attribute_names[attribute_index]);
        }

        boolean containsClick(double x, double y)
        {
            return x < this.getX() + this.getWidth() && y < this.getY() + this.getHeight() && x > this.getX() && y > this.getY();
        }

        //Change which ship this interface is set up for
        void setFocusedShip(Ship s)
        {
            this.focused = s;
        }

        @Override
        void update(double v){}

        @Override
        Rectangle getBoundingRectangle()
        {
            return null;
        }
    }
}