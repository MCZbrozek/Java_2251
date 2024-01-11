import java.awt.Color;

class ExperienceBrick extends SpriteTopDown
{
    ExperienceBrick(double x, double y,
                 double collision_radius,
                 int health,
                 Color color,
                 int i_am_bitmask,
                 int i_hit_bitmask)
    {
        super(x, y,
                health,
                0,
                collision_radius,
                1,
                i_am_bitmask,
                i_hit_bitmask);
        this.color = color;
        this.no_direction_draw = true;
        this.moveToCenter(x,y);
    }
}