import java.awt.*;

//Used for Billiards and soccer games. Tries to shove bricks into team's basket
class NPC_Soccer extends NPC_Personality
{
    //Where we want to put the brick
    BrickBasket team_basket = null;
    //Useful for focusing one brick at a time
    private ExperienceBrick current_brick = null;

    NPC_Soccer(Board b)
    {
        super(b);
        this.debugging = true;
        //TODO Change this
        String[] temp_upgrade_sequence = {"Health Regen", "Health Regen",
                "Health Regen", "Health Regen",
                "Health Regen", "Health Regen",
                "Health Regen", "Health", "Speed", "Speed",
                "Health", "Collision Damage", "Collision Damage",
                "Collision Damage", "Collision Damage", "Speed",
                "Speed", "Speed", "Speed", "Health", "Collision Damage",
                "Collision Damage", "Health", "Health", "Health",
                "Health", "Collision Damage","Speed","Speed",
                "Bullet Damage","Bullet Damage","Bullet Damage"};
        this.upgrade_sequence = temp_upgrade_sequence;
        //TODO Change this
        this.ship_upgrade_sequence = new int[1];
        this.ship_upgrade_sequence[0] = Constants.SHAPE_RAMMER;
    }

    //Returns true if ship is on the wrong side of the brick to
    //shove the brick into the goal.
    //If the angle from the ship to the goal is similar to the angle from the
    //ship to the brick, then the ship is facing the right direction.
    //But what difference between the angles is close enough?
    //You must decide. Note: close_enough is in radians
    private boolean wrongSideOfBrick(Ship s, double close_enough, boolean testing)
    {
        //Avoid errors
        if(current_brick==null || team_basket==null){return false;}
        double angle_to_goal = Utils.normalizeAngle(s.angleToSprite(team_basket));
        double angle_to_brick = Utils.normalizeAngle(s.angleToSprite(current_brick));
        if(testing)
        {
            System.out.println();
            System.out.print("Angle to goal: ");
            System.out.println((int) (angle_to_goal * 180 / Math.PI));
            System.out.print("Angle to brick: ");
            System.out.println((int) (angle_to_brick * 180 / Math.PI));
            System.out.print("Difference in angles: ");
            System.out.println((int) (Utils.getAngleDifference(angle_to_goal, angle_to_brick) * 180 / Math.PI));
            System.out.print("Close enough ");
            System.out.print((int) (close_enough * 180 / Math.PI));
            System.out.print(": ");
            System.out.println(Math.abs(Utils.getAngleDifference(angle_to_goal, angle_to_brick)) > close_enough);
        }
        return Math.abs(Utils.getAngleDifference(angle_to_goal, angle_to_brick)) > close_enough;
    }

    //Move around current_brick until the ship is at
    //the desired angle. Maintain distance distance from current_brick.
    private void orbitToAngle(Ship s, double elapsed_seconds,
                                 double desired_angle, double distance)
    {
        //Get the difference between ship's angle and the desired angle
        double angle = s.angleToSprite(current_brick);
        double difference = Utils.getAngleDifference(angle, desired_angle);
        //Calculate a new angle that will take the ship closer to the desired angle
        angle = current_brick.angleToSprite(s);
        if (difference > 0)
        {
            angle = angle - Math.PI/8;
        }
        else
        {
            angle = angle + Math.PI/8;
        }
        double x = current_brick.getXCenter() + Math.cos(angle)*distance;
        double y = current_brick.getYCenter() + Math.sin(angle)*distance;
        //Temporary brick as goal for debugging.
        ExperienceBrick temp = new ExperienceBrick(x,y,0,0,0,Color.WHITE, 0, false, 0, 0, 0, true, null);
        this.ramAttack(elapsed_seconds, temp, s, "orbit into position");
    }

    @Override
    void update(double elapsed_seconds, Ship s)
    {
        //Avoid errors
        if(s.nearest_xp == null){return;}
        //Reset dead bricks
        if(current_brick!=null && current_brick.getRemoveMe()){current_brick = null;}
        //Pick a new brick if none is selected already.
        if(current_brick==null){current_brick = s.nearest_xp;}


        //TODO Your code goes here.
        /*
        Use the following functions to write your AI:

        In this file:
        boolean wrongSideOfBrick(Ship s, double close_enough, boolean testing)

        In this file:
        void orbitToAngle(Ship s, double elapsed_seconds,
                                 double desired_angle, double distance)

        Demo of the distance to function's use. It returns a double.
        s.distanceTo(team_basket)
        s.distanceTo(current_brick)

        You can still access the nearest experience brick...
        s.nearest_xp
        ...or the nearest ship.
        s.nearest_ship

        You can ask for the angle from the ship to a given sprite such as
        a ship or experience brick.
        s.angleTo(sprite)

        Shoot and fly towards target
        void shootAttack(double elapsed_seconds, SpritePhysics target, Ship s, String debug_info)

        Use shots for propulsion as you try to ram target
        void ramAttack(double elapsed_seconds, SpritePhysics target, Ship s, String debug_info)

        Flee from target
        void fleeShoot(double elapsed_seconds, SpritePhysics target, Ship s, String debug_info)
        */


        //TODO
        /* Delete all the following when you share code with students. This
           will be the basis for your team's code.*/

        //scale distance with distance to goal
        double close_enough = Math.PI/8;
        double distance = 110;
        if(s.distanceTo(team_basket)<800)
        {
            distance = 80;
        }
        else if(s.distanceTo(team_basket)<500)
        {
            distance = 40;
        }
        //Seek position on far side of brick from basket.
        if(this.wrongSideOfBrick(s, close_enough, false))
        {
            double desired_angle = s.angleToSprite(team_basket);
            this.orbitToAngle(s, elapsed_seconds, desired_angle, distance);
        }
        else
        {
            //This switching should help keep our position adjusted.
            //Get the current distance to the brick so that the orbit
            // doesn't move us away from the brick
            distance = s.distanceTo(current_brick);
            //For a half second orbit to angle
            double desired_angle = s.angleToSprite(team_basket);
            this.orbitToAngle(s, elapsed_seconds/2, desired_angle, distance);
            //For the other half second thrust toward the brick
            this.shootAttack(elapsed_seconds/2, current_brick, s, "kick");
        }
    }
}
