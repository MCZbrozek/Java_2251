import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.Random;

/** This class is home to static utility functions. */
public class Utils 
{
	public static Random random_generator = new Random();

	public static void flushRandomGenerator()
	{
		for(int i=0; i<50; i++)
		{
			Utils.random_generator.nextInt();
		}
	}

	/** Return a random int between min inclusive
	 * and max exclusive. */
	public static int randInt(int min, int max)
	{
		if(min == max){ return min; }
		return Utils.random_generator.nextInt(max-min)+min;
	}

	/** Return a random double between min and max. */
	public static double randDouble(double min, double max)
	{
		return Utils.random_generator.nextDouble()*(max-min)+min;
	}

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

	/** Given an angle, return the two digits of this angle as
	 * a percentage of the circle. For example: pi/2 = 25
	 * pi = 50 and 3pi/2 = 75
	 * This function is currently used exclusively by
	 * ImageManager.getTransformedImage but it saves us tons
	 * of dropped frames.
	 * Unit tests:
	 * 	System.out.print("Should be 00: ");
	 System.out.println(Utils.getPercentAngle(0));
	 System.out.print("Should be 25: ");
	 System.out.println(Utils.getPercentAngle(Math.PI/2));
	 System.out.print("Should be 50: ");
	 System.out.println(Utils.getPercentAngle(Math.PI));
	 System.out.print("Should be 75: ");
	 System.out.println(Utils.getPercentAngle(3*Math.PI/2));
	 System.exit(0); */
	public static String getPercentAngle(double angle)
	{
		while(angle < 0)
		{
			angle += 2*Math.PI;
		}
		angle = (angle % (2*Math.PI)) / (2*Math.PI);
		String temp = Long.toString(Math.round(100*angle));
		if(temp.length()<2)
		{
			return "0"+temp;
		}
		else
		{
			return temp;
		}
	}

	/** http://www.superflashbros.net/as3sfxr/ */
	public static void playSound(String FileName, float volume_reduction)
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(FileName).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			FloatControl gainControl =
					(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			//Reduce volume by volume_reduction decibels.
			gainControl.setValue(-volume_reduction);
			clip.start();
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
}