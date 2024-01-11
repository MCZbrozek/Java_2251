import com.sun.corba.se.impl.orbutil.closure.Constant;

class PowerUp extends SpriteTopDown
{
    //For now, randomize the powerups
    private int powerup_id = (int)(Math.random()*Constants.NUM_POWERUPS);

    PowerUp(double x, double y,
                    int health,
                    int xp_reward,
                    String image_file,
                    int i_am_bitmask,
                    int i_hit_bitmask,
                    Board b)
    {
        super(x, y,
                health,
                0,
                0,
                xp_reward,
                Constants.SHAPE_IMAGE,
                i_am_bitmask,
                i_hit_bitmask,
                b);
        this.no_direction_draw = true;
        this.moveToCenter(x,y);
        this.speed = Constants.suicide_brick_speed;
        this.setImage(image_file,0);
    }

    public void takeDamage(int damage, Sprite damage_dealer)
    {
        super.takeDamage(damage, damage_dealer);
        //If this experience brick is dead, relocate it and reset its health
        if(this.getHealth()<=0)
        {
            //Give the powerup to the ship that killed this
            if(damage_dealer instanceof Ship)
            {
                ((Ship)damage_dealer).activatePowerup(powerup_id);
            }
            else if(damage_dealer instanceof Shot)
            {
                ((Shot)damage_dealer).getShooter().activatePowerup(powerup_id);
            }
            //Reset and re-randomize the powerup
            powerup_id = (int)(Math.random()*Constants.NUM_POWERUPS);
            this.setHealthMax();
            this.setX(Constants.getRandomX());
            this.setY(Constants.getRandomY());
        }
    }
}