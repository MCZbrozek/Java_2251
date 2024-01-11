class NPC_HoltShooterD extends NPC_Personality
{
    NPC_HoltShooterD(Board b)
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

        this.ship_upgrade_sequence = new int[3];
        this.ship_upgrade_sequence[0] = Constants.SHAPE_TWIN;
        this.ship_upgrade_sequence[1] = Constants.SHAPE_SNIPER;
        //Randomly choose between carrier and ricochet shot.
        if(Math.random()<0.33)
        {
            this.ship_upgrade_sequence[2] = Constants.SHAPE_SEEKER;
        }
        else if(Math.random()<0.66)
        {
            this.ship_upgrade_sequence[2] = Constants.SHAPE_CANNON;
        }
        else
        {
            this.ship_upgrade_sequence[2] = Constants.SHAPE_LASER;
        }
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
        //When high level, focus on killing other ships, but don't move toward them
        else if(s.getLevel()>=31 && s.nearest_ship!=null)
        {
                this.shootAttack(0, s.nearest_ship, s, "Maxed out. Attack!");
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
            else if (s.nearest_ship != null && s.distanceTo(s.nearest_ship) < 400)
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
                if (s.distanceTo(memory_brick) < 200)
                {
                    this.fleeShoot(elapsed_seconds, memory_brick, s, "maintain distance");
                }
                else if (s.distanceTo(memory_brick) < 500)
                {
                    this.shootAttack(elapsed_seconds, memory_brick, s, "get big xp");
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