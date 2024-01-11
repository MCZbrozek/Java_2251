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

    //Any ship can become a machine gun ship at level 20.
    static final int machine_gun_ship_level_limit = 5;
    private PlusSign machine_gun_plus_sign;
    //Any ship can become a flanker ship at level 20.
    private final int flanker_ship_level_limit = 20;
    private PlusSign flanker_plus_sign;
    //Any ship can become a sniper ship at level 20.
    private final int sniper_ship_level_limit = 20;
    private PlusSign sniper_plus_sign;

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
        machine_gun_plus_sign = new PlusSign(
                x, y+(height+buffer)*Constants.attribute_names.length,
                focused, 0);
        flanker_plus_sign = new PlusSign(
                x, y+(height+buffer)*(Constants.attribute_names.length+1),
                focused, 0);
        sniper_plus_sign = new PlusSign(
                x, y+(height+buffer)*(Constants.attribute_names.length+2),
                focused, 0);
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
            int extra_text_buffer = 12;
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
                g.drawString(name,x+level*(width+width_buffer),y+i*(height+buffer)+extra_text_buffer);
                //If this attribute is not max level and levelup points
                //are available and the focused ship is a player,
                //then draw a plus sign to be clicked on
                if(!plus_signs[i].maxedOut() && levelup_points>0 && focused instanceof Player)
                {
                    plus_signs[i].draw(b,g);
                }
            }
            //Check to see if ship is high enough level to be a machine gunner
            if(focused.getLevel() >= machine_gun_ship_level_limit && focused.shape == Constants.SHAPE_CIRCLE)
            {
                machine_gun_plus_sign.draw(b,g);
                g.drawString("Upgrade to machine gun ship",
                        x+machine_gun_plus_sign.getWidth(),
                        y+(height+buffer)*Constants.attribute_names.length+extra_text_buffer);
            }
            //Check to see if ship is high enough level to be a flanker
            if(focused.getLevel() >= flanker_ship_level_limit && focused.shape == Constants.SHAPE_CIRCLE)
            {
                flanker_plus_sign.draw(b,g);
                g.drawString("Upgrade to flanker ship",
                        x+flanker_plus_sign.getWidth(),
                        y+(height+buffer)*(Constants.attribute_names.length+1)+extra_text_buffer);
            }
            //Check to see if ship is high enough level to be a sniper
            if(focused.getLevel() >= sniper_ship_level_limit && focused.shape == Constants.SHAPE_CIRCLE)
            {
                sniper_plus_sign.draw(b,g);
                g.drawString("Upgrade to sniper ship",
                        x+sniper_plus_sign.getWidth(),
                        y+(height+buffer)*(Constants.attribute_names.length+2)+extra_text_buffer);
            }
        }
    }

    //If one of the plus signs contains the coordinates then level it up
    void levelUp(double x, double y)
    {
        if(focused.availableLevelUpPoints()>0)
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
        //Machine gun ship upgrade
        if(focused.getLevel() >= machine_gun_ship_level_limit && focused.shape == Constants.SHAPE_CIRCLE && machine_gun_plus_sign.containsClick(x, y))
        {
            focused.shape = Constants.SHAPE_MACHINE_GUN;
            focused.setAttributes();
        }
        //Flanker ship upgrade
        else if(focused.getLevel() >= flanker_ship_level_limit && focused.shape == Constants.SHAPE_CIRCLE && flanker_plus_sign.containsClick(x, y))
        {
            focused.shape = Constants.SHAPE_FLANKER;
            focused.setAttributes();
        }
        //Sniper ship upgrade
        else if(focused.getLevel() >= sniper_ship_level_limit && focused.shape == Constants.SHAPE_CIRCLE && sniper_plus_sign.containsClick(x, y))
        {
            focused.shape = Constants.SHAPE_SNIPER;
            focused.setAttributes();
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