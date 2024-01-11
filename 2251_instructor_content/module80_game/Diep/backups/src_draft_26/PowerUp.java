class PowerUp extends SpriteTopDown
{
    //For now, randomize the powerups
    //TODO -2 added on next line to temporarily remove mimic and
    // teleport powerups that no one likes.
    private int powerup_id = (int)(Math.random()*(Constants.NUM_POWERUPS-2));

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

    //Constructor that sets powerup type
    PowerUp(double x, double y,
            int health,
            int xp_reward,
            String image_file,
            int i_am_bitmask,
            int i_hit_bitmask,
            Board b,
            int powerup_id)
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
        this.powerup_id = powerup_id;
    }

    public void takeDamage(int damage, Sprite damage_dealer)
    {
        //Give the powerup to any ship that runs into this
        if(damage_dealer instanceof Ship)
        {
            ((Ship)damage_dealer).activatePowerup(powerup_id);
            //Remove powerup from the game
            this.setRemoveMeTrue();
            /* //Powerups used to respawn on their own, but now ffa manager
            //occassionally respawns them.
            //Reset and re-randomize the powerup
            powerup_id = (int)(Math.random()*Constants.NUM_POWERUPS);
            this.setHealthMax();
            this.setX(Constants.getRandomX());
            this.setY(Constants.getRandomY());
            */
        }
    }
}