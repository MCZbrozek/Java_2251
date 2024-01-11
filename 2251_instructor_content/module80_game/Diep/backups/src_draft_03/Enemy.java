import java.awt.*;
import java.util.ArrayList;

public class Enemy extends Ship
{
    public static final int LEVELUP_RANDOM = 0;
    public static final int LEVELUP_SHOOTER = 1;
    public static final int LEVELUP_RAM = 2;
    public static final int LEVELUP_SHOOTER_B = 3;
    public static final int LEVELUP_RAM_B = 4;
    private int levelup_strategy;

    private final String[] levelup_sequence_shooter = {"Bullet Damage",
            "Bullet Damage", "Bullet Longevity", "Penetration",
            "Refire Rate", "Refire Rate", "Penetration", "Bullet Speed",
            "Bullet Speed", "Penetration", "Health", "Health",
            "Bullet Damage","Bullet Damage","Bullet Damage","Bullet Damage",
            "Refire Rate", "Refire Rate", "Refire Rate", "Refire Rate"};
    //An alternative levelup sequence for a shooter
    private final String[] levelup_sequence_shooter_b = {"Bullet Damage",
            "Bullet Damage", "Bullet Speed", "Refire Rate",
            "Bullet Speed", "Refire Rate", "Refire Rate",
            "Bullet Longevity", "Penetration", "Refire Rate",
            "Speed", "Speed", "Penetration","Penetration",
            "Bullet Damage","Bullet Damage","Bullet Damage","Bullet Damage",
            "Refire Rate","Refire Rate"};
    private final String[] levelup_sequence_ram = {"Collision Damage",
            "Collision Damage", "Health Regen", "Health Regen", "Health Regen",
            "Health Regen", "Health Regen", "Health", "Speed", "Speed",
            "Health", "Collision Damage", "Collision Damage",
            "Collision Damage", "Collision Damage", "Speed",
            "Speed", "Speed", "Speed", "Health"};
    private final String[] levelup_sequence_ram_b = {"Health Regen", "Health Regen",
            "Health Regen", "Health Regen", "Health Regen", "Health",
            "Collision Damage", "Collision Damage", "Speed", "Speed",
            "Collision Damage", "Collision Damage",
            "Collision Damage", "Collision Damage", "Health", "Speed",
            "Speed", "Speed", "Speed", "Health"};

    //Keep track of the nearest Ship, nearest experience brick,
    // and nearest shot to this Ship
    private Ship nearest_ship = null;
    private Shot nearest_shot = null;
    //Fabricate initial nearest brick to avoid null pointer error
    private ExperienceBrick nearest_xp = new ExperienceBrick(-100,-100,
            15, 5, 2, Color.GREEN,
            0, 0);
    //Cooldown before next checking for new nearest
    private double targeting_cooldown = 0;
    //Refresh nearest sprites every targeting_cooldown_reset seconds
    private final double targeting_cooldown_reset = 0.1;

    public Enemy(double x, double y,
                 String name,
                 Color color,
                 double collision_radius,
                 int i_am_bitmask,
                 int i_hit_bitmask,
                 int levelup_strategy,
                 Board board)
    {
        super(x, y,
                name,
                collision_radius,
                i_am_bitmask,
                i_hit_bitmask,
                BoardTopDown.bullet_bitmask,//I am
                BoardTopDown.everything_bitmask,//I hit
                board);
        this.color = color;
        this.levelup_strategy = levelup_strategy;
    }

    private void updateTarget(double elapsed_seconds)
    {
        //Check if it's time to get new targets
        targeting_cooldown -= elapsed_seconds;
        if (targeting_cooldown < 0)
        {
            //Get all the sprites
            ArrayList<SpritePhysics> nearby_sprites = board_reference.getSpriteList();
            //Find the nearest sprites in the list
            double closest_distance_ship = Double.MAX_VALUE;
            double closest_distance_xp = Double.MAX_VALUE;
            double closest_distance_shot = Double.MAX_VALUE;
            SpritePhysics temp_sprite;
            for(int i=0; i<nearby_sprites.size(); i++)
            {
                temp_sprite = nearby_sprites.get(i);
                if(temp_sprite instanceof ExperienceBrick)
                {
                    //If current brick is a high experience brick and is within 350 pixels
                    //then don't change it
                    if(nearest_xp.getXpReward()==10 && this.distanceTo(nearest_xp)<350) {}
                    //If current brick is a medium experience brick and is within 250 pixels
                    //then don't change it
                    else if(nearest_xp.getXpReward()==2 && this.distanceTo(nearest_xp)<250){}
                    //If current brick is within 150 pixels and is damaged
                    //then don't change it
                    else if(nearest_xp.getPercentHealth()<1.0 && this.distanceTo(nearest_xp)<150){}
                    //else check to see if there is a closer brick
                    else if(this.distanceTo(temp_sprite) < closest_distance_xp)
                    {
                        closest_distance_xp = this.distanceTo(temp_sprite);
                        nearest_xp = (ExperienceBrick) temp_sprite;
                    }
                }
                else if(temp_sprite instanceof Shot && ((Shot)temp_sprite).getShooter() != this)
                {
                    if (this.distanceTo(temp_sprite) < closest_distance_shot)
                    {
                        closest_distance_shot = this.distanceTo(temp_sprite);
                        nearest_shot = (Shot)temp_sprite;
                    }
                }
                else if(temp_sprite instanceof Ship && temp_sprite != this)
                {
                    if (this.distanceTo(temp_sprite) < closest_distance_ship)
                    {
                        closest_distance_ship = this.distanceTo(temp_sprite);
                        nearest_ship = (Ship)temp_sprite;
                    }
                }
            }
            targeting_cooldown = targeting_cooldown_reset;
        }
    }

    private void moveToward(double elapsed_seconds, double x, double y)
    {
        if(y < this.getYCenter())
        {	//up
            this.changeVelocity(0, -speed*elapsed_seconds);
        }else
        {   //down
            this.changeVelocity(0, speed*elapsed_seconds);
        }
        if(x < this.getXCenter())
        {	//left
            this.changeVelocity(-speed*elapsed_seconds, 0);
        }else
        {   //right
            this.changeVelocity(speed*elapsed_seconds, 0);
        }
    }

    private void moveAwayFrom(double elapsed_seconds, double x, double y)
    {
        if(y < this.getYCenter())
        {	//down
            this.changeVelocity(0, speed*elapsed_seconds);
        }else
        {   //up
            this.changeVelocity(0, -speed*elapsed_seconds);
        }
        if(x < this.getXCenter())
        {	//right
            this.changeVelocity(speed*elapsed_seconds, 0);
        }else
        {   //left
            this.changeVelocity(-speed*elapsed_seconds, 0);
        }
    }

    /* This method is intended to be called once per frame and will
     * update this sprite. */
    public void update(double elapsed_seconds)
    {   //Choose a target
        this.updateTarget(elapsed_seconds);
        //AI
        if(this.levelup_strategy == LEVELUP_RAM || this.levelup_strategy == LEVELUP_RAM_B)
        {
            this.rammerBehavior(elapsed_seconds);
        }
        else
        {
            this.shooterBehavior(elapsed_seconds);
        }
        this.attack();
        //Apply friction, move, and stay in bounds.
        super.update(elapsed_seconds);
        //Level up if possible
        this.levelUp();
    }

    /** AI for shooters and random bots. */
    private void shooterBehavior(double elapsed_seconds)
    {
        /*If nearest ship is close either flee it or attack it.
        * If at level 20 or above, be more aggressive toward ships
        * Otherwise attack the nearest experience brick. */
        if(this.distanceTo(nearest_ship)<500 || this.getLevel()>19)
        {
            //Orient gun toward the ship
            this.setAngle(this.angleToSprite(nearest_ship));
            if(this.getLevel()>19 && this.getHealth()>40)
            {   //Maintain distance
                if(this.distanceTo(nearest_ship)<300){this.moveAwayFrom(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());}
                else{this.moveToward(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());}
            }
            else if((this.getHealth() >= 25 && nearest_ship.getHealth()<10) || this.getHealth() > nearest_ship.getHealth()*5)
            {   //Attack!
                this.moveToward(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
            }
            else
            {   //Run away from the nearest ship
                this.moveAwayFrom(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
            }
        }
        else
        {   //Otherwise attack the nearest experience brick.
            //Orient toward target
            this.setAngle(this.angleToSprite(nearest_xp));
            //Maintain safe distance from target
            double dist = this.distanceTo(nearest_xp);
            if(dist<120)
            {
                this.moveAwayFrom(elapsed_seconds, nearest_xp.getXCenter(), nearest_xp.getYCenter());
            }
            else
            {
                this.moveToward(elapsed_seconds, nearest_xp.getXCenter(), nearest_xp.getYCenter());
            }
        }
    }

    /** AI for ram bots. */
    private void rammerBehavior(double elapsed_seconds)
    {
        //If health is low, or level is low, avoid everything and wait for regen
        if(this.getHealth()<25 || this.getLevel()<3)
        {
            SpriteTopDown nearest = nearest_ship;
            if(this.distanceTo(nearest_xp) < this.distanceTo(nearest))
            {
                nearest = nearest_xp;
            }
            if(this.distanceTo(nearest_shot) < this.distanceTo(nearest))
            {
                nearest = nearest_shot;
            }
            //Move away from nearest object
            this.moveAwayFrom(elapsed_seconds, nearest.getXCenter(), nearest.getYCenter());
            //Orient gun toward the nearest xp brick so that the rammer
            //can continue to gain experience.
            this.setAngle(this.angleToSprite(nearest_xp));
        }
        /*If nearest ship is close either flee it or attack it.
        * If at level 20 or above, be more aggressive toward ships
        * Otherwise attack the nearest experience brick. */
        else if(this.distanceTo(nearest_ship)<450 || this.getLevel()>19)
        {
            if((this.getHealth() >= 25 && nearest_ship.getHealth()<10) || this.getHealth() > nearest_ship.getHealth()*5 || (this.getLevel()>19 && this.getHealth() > nearest_ship.getHealth()))
            {   //Attack!
                //Orient gun away from the ship for extra recoil thrust!
                this.setAngle(nearest_ship.angleToSprite(this));
                this.moveToward(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
            }
            else
            {   //Run away from the nearest shot
                //Orient gun toward the ship
                this.setAngle(this.angleToSprite(nearest_ship));
                this.moveAwayFrom(elapsed_seconds, nearest_ship.getXCenter(), nearest_ship.getYCenter());
            }
        }
        else
        {   //Otherwise attack the nearest experience brick.
            //Orient gun away from the xp for extra recoil thrust!
            this.setAngle(nearest_xp.angleToSprite(this));
            this.moveToward(elapsed_seconds, nearest_xp.getXCenter(), nearest_xp.getYCenter());
        }
    }

    private void levelUp()
    {
        if(availableLevelUpPoints()>0)
        {
            String name;
            if(levelup_strategy == LEVELUP_RANDOM)
            {
                name = Constants.getRandomAttribute();
            }
            else if(levelup_strategy == LEVELUP_SHOOTER)
            {   //-1 because the array is indexed starting at zero,
                //but the first level up happens at level 1.
                int temp = (getLevel()-1)%levelup_sequence_shooter.length;
                name = levelup_sequence_shooter[temp];
            }
            else if(levelup_strategy == LEVELUP_SHOOTER_B)
            {
                //-1 because the array is indexed starting at zero,
                //but the first level up happens at level 1.
                int temp = (getLevel()-1)%levelup_sequence_shooter_b.length;
                name = levelup_sequence_shooter_b[temp];
            }
            else if(levelup_strategy == LEVELUP_RAM_B)
            {
                //-1 because the array is indexed starting at zero,
                //but the first level up happens at level 1.
                int temp = (getLevel()-1)%levelup_sequence_ram_b.length;
                name = levelup_sequence_ram_b[temp];
            }
            else //if(levelup_strategy == LEVELUP_RAM)
            {   //-1 because the array is indexed starting at zero,
                //but the first level up happens at level 1.
                int temp = (getLevel()-1)%levelup_sequence_ram.length;
                name = levelup_sequence_ram[temp];
            }
            levelUpAttribute(name);
            //System.out.println("Enemy leveled up "+name);
        }
    }

    /** Alert this enemy that it was damaged by another ship so it can respond. */
    public void takeDamage(int damage, Sprite damage_dealer)
    {
        super.takeDamage(damage, damage_dealer);
        //Remember the responsible Ship
        if(damage_dealer instanceof Ship)
        {
            nearest_ship = (Ship)damage_dealer;
        }
        else if(damage_dealer instanceof Shot)
        {
            nearest_ship = ((Shot)damage_dealer).getShooter();
        }
    }

    /** Override parent class's draw. */
    @Override
    public void draw(Board b, Graphics g, int offset_x, int offset_y)
    {
        super.draw(b, g, offset_x, offset_y);
        //Determine whether or not to draw the sprite's name
        if (this.isVisible())
        {
            int draw_x = (int) (this.getX() + (double) offset_x);
            int draw_y = (int) (this.getY() + (double) offset_y);
            if (this.getIsOnScreen())
            {   //Draw this enemy's name
                g.setColor(Color.WHITE);
                Font font = new Font("Serif", Font.BOLD, 18);
                g.setFont(font);
                g.drawString(name, draw_x-30, draw_y+(int)this.getCollisionRadius());
            }
        }
    }

}