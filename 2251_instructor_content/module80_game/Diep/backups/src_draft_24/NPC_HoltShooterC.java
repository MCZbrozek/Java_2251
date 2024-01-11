class NPC_HoltShooterC extends NPC_Personality
{
    NPC_HoltShooterC(Board b)
    {
        super(b);
        this.debugging=true;
        String[] temp_upgrade_sequence = {
                "Bullet Damage",
                "Penetration",
                "Refire Rate","Refire Rate","Refire Rate",
                "Bullet Damage","Bullet Damage","Bullet Damage",
                "Penetration", "Penetration",
                "Bullet Speed", "Bullet Longevity",
                "Refire Rate","Refire Rate","Refire Rate",
                "Refire Rate",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Damage","Bullet Damage","Bullet Damage",
                "Health", "Health",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Speed", "Bullet Longevity",
                "Bullet Speed", "Bullet Longevity",
                "Health Regen",
                "Health Regen", "Health Regen",
                "Health Regen", "Health Regen",
                "Health Regen"};
        this.upgrade_sequence = temp_upgrade_sequence;

        int[] temp_ship_upgrade_sequence = new int[2];
        temp_ship_upgrade_sequence[0] = Constants.SHAPE_MACHINE_GUN;
        //Randomly choose between carrier and ricochet shot.
        if(Math.random()<0.5)
        {
            temp_ship_upgrade_sequence[1] = Constants.SHAPE_CARRIER;
        }
        else
        {
            temp_ship_upgrade_sequence[1] = Constants.SHAPE_RICOCHET_SHOT;
        }
        this.ship_upgrade_sequence = temp_ship_upgrade_sequence;
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        //If nearest ship is invincible and at all near, run away
        if(s.nearest_ship!=null && s.nearest_ship.getPowerups()[Constants.POW_INVINCIBILITY] && s.distanceTo(s.nearest_ship)<600)
        {
            this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "invincible ship. Run away!");
        }
        //When low level, just shoot the nearest xp bricks until you level up.
        //This will speed up initial level
        else if(s.getLevel()<2 && s.nearest_xp!=null)
        {
            if(s.distanceTo(s.nearest_xp)<150)
            {
                this.fleeShoot(elapsed_seconds, s.nearest_xp, s, "maintain distance");
            }
            else
            {
                this.shootAttack(elapsed_seconds, s.nearest_xp, s, "get xp");
            }
        }
        //When high level, focus on killing other ships
        else if(s.getLevel()>=31 && s.nearest_ship!=null)
        {
            if(s.distanceTo(s.nearest_ship)>550)
            {
                this.shootAttack(elapsed_seconds, s.nearest_ship, s, "Maxed out. Attack!");
            }
            else
            {
                this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "maintain distance");
            }
        }
        else
        {
            //Get nearest big xp brick
            ExperienceBrick memory_brick = this.getNearestBigXp(s, 2);
            //Seek nearest powerup if it is anywhere close
            if (s.nearest_powerup != null && s.distanceTo(s.nearest_powerup) < 600)
            {
                this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "Powerup!");
            }
            //Shoot nearest ship if it gets too close
            else if (s.nearest_ship != null && s.distanceTo(s.nearest_ship) < 250)
            {
                this.shootMove(elapsed_seconds, s.nearest_ship, memory_brick, s, "shoot ship. get xp");
            }
            //Opportunistically attack very weak ships nearby
            else if (s.nearest_ship != null && s.nearest_ship.getHealth() / s.getBulletDamage() < 5)
            {
                this.shootAttack(elapsed_seconds, s.nearest_ship, s, "Blood in the water");
            }
            //Shoot nearest brick while moving toward biggest xp brick
            else
            {
                //Shoot nearest big brick if it's close but maintain distance
                if (s.distanceTo(memory_brick) < 150)
                {
                    this.fleeShoot(elapsed_seconds, memory_brick, s, "maintain distance");
                }
                else if (s.distanceTo(memory_brick) < 450)
                {
                    this.shootAttack(elapsed_seconds, memory_brick, s, "get big xp");
                }
                else if (s.shape == Constants.SHAPE_MACHINE_GUN)
                {
                    //Gently spin and shoot
                    s.turnAmount(Math.PI, elapsed_seconds);
                    //Move toward big exp brick
                    s.moveToward(elapsed_seconds, memory_brick.getXCenter(), memory_brick.getYCenter());
                }
                else
                {   //Shoot nearest brick. Move toward nearest big xp brick
                    this.shootMove(elapsed_seconds, s.nearest_xp, memory_brick, s, "shoot xp. get xp");
                }
            }
        }
        //Shoot as often as possible.
        s.attack();
    }
}