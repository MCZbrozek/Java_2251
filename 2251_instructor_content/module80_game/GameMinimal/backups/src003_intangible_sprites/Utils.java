
/* This class is home to static utility functions.
 */
public class Utils 
{
	/** Returns the distance between the coordinates. */
	public static double distance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x1-x2, 2) + 
				Math.pow(y1-y2, 2));
	}
	
	/** Pre: angle is in radians.
	 * Modify the given angle to be in the range -pi to pi. */
	public static double normalizeAngle(double angle)
	{
		while(angle < -Math.PI)
		{
			angle += 2*Math.PI;
		}
		angle = angle % (2*Math.PI);
		while(angle > Math.PI)
		{
			angle -= 2*Math.PI;
		}
		return angle;
	}

	/** Pre: angle is in degrees.
	 * Modify the given angle to be in the range -180 to 180. */
	public static double normalizeAngleDegrees(double angle)
	{
		while(angle < -180)
		{
			angle += 360;
		}
		angle = angle % 360;
		while(angle > 180)
		{
			angle -= 360;
		}
		return angle;
	}

	/** Pre: angle1 and angle2 are in radians.
	 * Returns the difference between the two angles in radians.
	 * In other words, if you add the return value of 
	 * this method to angle1, you will get angle2. */
	public static double getAngleDifference(double angle1, double angle2)
	{	//Make both angles in the range -pi to pi
		angle1 = Utils.normalizeAngle(angle1);
		angle2 = Utils.normalizeAngle(angle2);
		//Modify the angle difference if it is greater than 180
		//https://jibransyed.wordpress.com/2013/09/05/game-maker-gradually-rotating-an-object-towards-a-target/
		if(Math.abs(angle1-angle2) > Math.PI)
		{
			if(angle1 > angle2)
			{
				return -1 * ((2*Math.PI - angle1) + angle2);
			}
			else
			{
				return (2*Math.PI - angle2) + angle1;
			}
		}
		else
		{
			return angle1-angle2;
		}
	}

	/** Pre: angle1 and angle2 are in degrees.
	 * Returns the difference between the two angles in degrees.
	 * In other words, if you add the return value of
	 * this method to angle1, you will get angle2. */
	public static double getAngleDifferenceDegrees(double angle1, double angle2)
	{	//Make both angles in the range -180 to 180
		angle1 = Utils.normalizeAngleDegrees(angle1);
		angle2 = Utils.normalizeAngleDegrees(angle2);
		//Modify the angle difference if it is greater than 180
		//https://jibransyed.wordpress.com/2013/09/05/game-maker-gradually-rotating-an-object-towards-a-target/
		if(Math.abs(angle1-angle2) > 180)
		{
			if(angle1 > angle2)
			{
				return -1 * ((360 - angle1) + angle2);
			}
			else
			{
				return (360 - angle2) + angle1;
			}
		}
		else
		{
			return angle1-angle2;
		}
	}

	/** Format a double to two decimal places.
	* http://stackoverflow.com/questions/2379221/java-currency-number-format */
	public static String formatDecimal(double number)
	{
		return String.format("%10.2f", number);
	}

}