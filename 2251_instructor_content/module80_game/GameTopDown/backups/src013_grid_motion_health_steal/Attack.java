/**
 * Created by nealh_000 on 7/16/2017.
 *
 * attacks have the following attributes
 position relative to shooter center
 radius
 bitmasks
 expiration timeout
 damage
 knockback boolean
 speed
 cooldown
 Currently shots always shoot in direction shooter is facing.
 */
public class Attack
{
    //How far the attack is offset from attackers center
    //in the direction attacker is facing.
    private double offset;
    //Size of the attack
    private double radius;
    //bitmasks
    private int i_am_bitmask = 0;
    private int i_hit_bitmask = 0;
    //How long the attack lasts
    private double duration;
    //Damage dealt by the attack
    private int damage;
    //Whether or not the attack knocks back those it hits.
    private boolean knockback;
    //Whether or not the attack steals health from those it hits.
    private boolean health_steal;
    //How fast the attack moves away from attacker in the
    //direction the attacker is facing.
    private double speed;
    //How long until the attack can be used again.
    private double cooldown = 0;
    private double cooldown_duration;
    //Reference to the board for adding shots
    private Board board_reference;

    public Attack(double offset,
            double radius,
            double duration,
            int damage,
            boolean knockback,
            double speed,
            double cooldown_duration,
            Board board_reference)
    {
        this.offset = offset;
        this.radius = radius;
        this.duration = duration;
        this.damage = damage;
        this.knockback = knockback;
        this.speed = speed;
        this.cooldown_duration = cooldown_duration;
        this.board_reference = board_reference;
    }

    public void setHealthSteal(boolean health_steal){this.health_steal = health_steal;}

    public void cast(SpriteTopDown caster)
    {
        if(cooldown<=0)
        {
            Shot temp_shot = new Shot(
                    caster.getXCenter() + Math.cos(caster.getAngle())*offset,
                    caster.getYCenter() + Math.sin(caster.getAngle())*offset,
                    this.radius,
                    i_am_bitmask,
                    i_hit_bitmask,
                    caster,
                    duration,
                    damage,
                    knockback,
                    speed);
            temp_shot.setHealthSteal(health_steal);
            board_reference.addSprite(temp_shot);
            cooldown = this.cooldown_duration;
        }
    }

    public void update(double elapsed_seconds)
    {
        if(cooldown > 0){ cooldown -= elapsed_seconds; }
    }

    public void setBitmasks(int i_am, int i_hit)
    {
        this.i_am_bitmask = i_am;
        this.i_hit_bitmask = i_hit;
    }

    public Attack getCopy()
    {
        return new Attack(offset,
            radius,
            duration,
            damage,
            knockback,
            speed,
            cooldown_duration,
            board_reference);
    }
}