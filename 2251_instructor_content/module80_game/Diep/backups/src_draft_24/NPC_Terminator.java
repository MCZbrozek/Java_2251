class NPC_Terminator extends NPC_Personality
{
    //Just move toward target and shoot. That's all. Does not level up.
    NPC_Terminator(Board b)
    {
        super(b);
        this.upgrade_sequence = new String[0];
        this.ship_upgrade_sequence = new int[0];
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        if(s.nearest_ship!=null)
        {
            this.shootAttack(elapsed_seconds, s.nearest_ship, s, "");
        }
        //Shoot as often as possible.
        s.attack();
    }
}