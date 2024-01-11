import java.util.ArrayList;

public class NPC_BONNER extends NPC_Personality
{
    Board board;
    public NPC_BONNER(Board b){
        super(b);

        board=b;

        int[] temp_ship_upgrade_sequence = {Constants.SHAPE_FLANKER,
                Constants.SHAPE_CROSS, Constants.SHAPE_All_DIRECTIONS};
        this.ship_upgrade_sequence=temp_ship_upgrade_sequence;

        String[] temp_upgrade_sequence = {"Bullet Damage",
                "Bullet Damage", "Bullet Damage", "Bullet Damage",
                "Refire Rate", "Refire Rate", "Refire Rate", "Refire Rate",
                "Refire Rate", "Refire Rate", "Refire Rate", "Health Regen",
                "Health Regen","Bullet Damage", "Bullet Damage", "Bullet Damage", "Bullet Speed",
                "Bullet Speed", "Bullet Speed", "Bullet Speed", "Bullet Speed", "Bullet Speed",
                "Bullet Speed", "Bullet Longevity", "Bullet Longevity", "Bullet Longevity",
                "Bullet Longevity", "Bullet Longevity", "Bullet Longevity",
                "Bullet Longevity", "Penetration", "Penetration"};
        this.upgrade_sequence = temp_upgrade_sequence;
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        if(s.getLevel()>=31){
            //Get all the ships
            ArrayList<SpritePhysics> reference=board.getSpriteList();
            ArrayList<Ship> ships=new ArrayList<>();
            for(int i=0;i<reference.size();i++){
                if(reference.get(i) instanceof Ship){
                    ships.add((Ship)reference.get(i));
                }
            }

            if(ships.size()>0){
                Ship weakship=ships.get(0);
                for(int i=0;i<ships.size();i++){
                    if(ships.get(i).getHealth()<weakship.getHealth()){
                        weakship=ships.get(i);
                    }
                }

                shootAttack(elapsed_seconds,weakship,s,"I'm comin for them tities");
            }
        }
        if(s.nearest_ship!=null && s.distanceTo(s.nearest_ship)<200)
        {
            //If it is weak or my level is high, kill it!
            if(s.nearest_ship.getHealth() < 20 || s.getLevel()>20)
            {
                this.shootAttack(elapsed_seconds, s.nearest_ship, s, "I smell blood in the water!");
            }
            else
            {   //Otherwise, run away!
                this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "Tactical retreat!");
            }
        }
        else if(s.nearest_shot!=null && s.distanceTo(s.nearest_shot)<100 && s.getLevel()>25)
        {
            this.fleeShoot(elapsed_seconds, s.nearest_ship, s, "I can dodge bullets.");
        }
        else if(s.nearest_powerup!=null && s.distanceTo(s.nearest_powerup)<450)
        {   //Ram the powerup
            this.ramAttack(elapsed_seconds, s.nearest_powerup, s, "I wants it!");
        }
        else if(s.nearest_xp!=null)
        {
            //Find all the close xp bricks in the game
            ArrayList<SpritePhysics> reference=board.getSpriteList();
            ArrayList<ExperienceBrick> bricks=new ArrayList<>();
            for(int i=0;i<reference.size();i++){
                if(reference.get(i) instanceof ExperienceBrick && s.distanceTo(reference.get(i))<500){
                    bricks.add((ExperienceBrick)reference.get(i));
                }
            }

            if(bricks.size()>0)
            {
                //Find the biggest and closest xp brick
                int largest_xp=1;
                ExperienceBrick bigbrick=bricks.get(0);
                for(int i=1;i<bricks.size();i++)
                {
                    if(bricks.get(i).getXpReward()>largest_xp)
                    {
                        largest_xp=bricks.get(i).getXpReward();
                        bigbrick=bricks.get(i);
                    } else if(bricks.get(i).getXpReward() == largest_xp)
                    {
                        if(s.distanceTo(bricks.get(i))<s.distanceTo(bigbrick))
                        {
                            bigbrick=bricks.get(i);
                        }
                    }
                }

                //Attack the nearest and biggest experience brick.
                if(s.distanceTo(bigbrick)<120)
                {
                    this.fleeShoot(elapsed_seconds, bigbrick, s, "Caution!");
                } else
                {
                    this.shootAttack(elapsed_seconds, bigbrick, s, "Delicious XP!");
                }
            }
        }

        s.attack();
    }

    void died(Ship s)
    {
        System.out.println(s.getName()+" died and has "+s.getLives()+" lives remaining.");

        int[] temp_ship_upgrade_sequence = {Constants.SHAPE_MACHINE_GUN};
        this.ship_upgrade_sequence=temp_ship_upgrade_sequence;

        String[] temp_upgrade_sequence = {"Bullet Damage",
                "Bullet Damage", "Bullet Damage", "Bullet Damage",
                "Refire Rate", "Refire Rate", "Refire Rate", "Refire Rate",
                "Refire Rate", "Refire Rate", "Refire Rate", "Health Regen",
                "Health Regen","Bullet Speed", "Bullet Speed",
                "Bullet Damage", "Bullet Damage", "Bullet Damage",
                "Bullet Speed", "Bullet Speed", "Bullet Speed", "Bullet Speed",
                "Bullet Speed", "Bullet Longevity", "Bullet Longevity", "Bullet Longevity",
                "Bullet Longevity", "Bullet Longevity", "Bullet Longevity",
                "Bullet Longevity", "Penetration", "Penetration"};
        this.upgrade_sequence = temp_upgrade_sequence;

        for(int i=0;i<32;i++)
        {
            this.levelUp(s);
            this.shipLevelUp(s);
        }
    }
}
