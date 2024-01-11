import java.awt.*;

class LevelUpInterface
{
    //Constants for positioning the interface
    private final int x = 40;
    private final int y = 100;
    private final int width = 15;
    private final int height = 10;
    private final int buffer = 10;
    private final int width_buffer = 2;
    private final int extra_text_buffer = 12;

    //List of ship upgrades
    private ShipUpgrade[] ship_upgrades;

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
        ship_upgrades = new ShipUpgrade[Constants.ship_upgrade_list.length];
        for (int i = 0; i < ship_upgrades.length; i++)
        {
            PlusSign temp = new PlusSign(
                    x, y+(height+buffer)*(Constants.attribute_names.length+i),
                    focused, 0);
            ship_upgrades[i] = new ShipUpgrade(Constants.ship_upgrade_list[i], temp);
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
                g.drawString(name,x+level*(width+width_buffer),y+i*(height+buffer)+extra_text_buffer);
                //If this attribute is not max level and levelup points
                //are available and the focused ship is a player,
                //then draw a plus sign to be clicked on
                if(!plus_signs[i].maxedOut() && levelup_points>0 && focused instanceof Player)
                {
                    plus_signs[i].draw(b,g);
                }
            }
            //Check to see if ship is high enough level for each ship upgrade
            for (ShipUpgrade ship_upgrade : ship_upgrades)
            {
                if(ship_upgrade.canUpgrade(focused))
                {
                    ship_upgrade.draw(b, g);
                }
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
        //Check if any ship upgrades contain the click
        for (ShipUpgrade ship_upgrade : ship_upgrades)
        {
            if(ship_upgrade.doUpgrade(x,y,focused))
            {
                focused.setShape(ship_upgrade.getShape());
                focused.setAttributes();
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


    private class ShipUpgrade
    {
        private ShipUpgradeStruct upgrade_struct;
        private PlusSign button;
        ShipUpgrade(ShipUpgradeStruct upgrade_struct, PlusSign button)
        {
            this.upgrade_struct = upgrade_struct;
            this.button = button;
        }
        public void draw(Board b, Graphics g)
        {
            button.draw(b,g);
            g.drawString(upgrade_struct.text,
                    (int)(button.getX()+button.getWidth()),
                    (int)(button.getY()+extra_text_buffer));

        }
        int getShape(){ return upgrade_struct.ship_shape; }
        boolean canUpgrade(Ship s){ return this.upgrade_struct.requirementsMet(s); }
        boolean doUpgrade(double x, double y, Ship s)
        {
            return this.containsClick(x,y) && this.upgrade_struct.requirementsMet(s);
        }
        private boolean containsClick(double x, double y){return button.containsClick(x,y);}
    }
}