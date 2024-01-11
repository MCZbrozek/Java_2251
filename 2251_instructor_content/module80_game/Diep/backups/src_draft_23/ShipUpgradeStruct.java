class ShipUpgradeStruct
{
    private int level_limit; //level at which ship can be acquired
    String text; //text to display next to level up button
    int ship_shape; //shape of the new ship
    private int precursor_shape; //required precursor shape
    ShipUpgradeStruct(int level_limit,
                      String text,
                      int ship_shape,
                      int precursor_shape)
    {
        this.level_limit = level_limit;
        this.text = text;
        this.ship_shape = ship_shape;
        this.precursor_shape = precursor_shape;
    }

    /** Returns true if the given ship meets the requirements for this upgrade. */
    boolean requirementsMet(Ship s)
    {
        return s.getLevel() >= this.level_limit &&
                s.shape == this.precursor_shape;
    }
}