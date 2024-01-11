/*
This class demonstrates how to create
a basic player that moves around 
collecting dots, but there are many
more strategies that could be attempted.

Consider
	-changing speed or turnRate
	-using multiple sensors angled in different directions
	-moving first to the closest dots
	-maintaining an ArrayList of sensed dots and seeking them out in a more efficient order
	-investigating Sprite.java's capabilities in \GameMinimal\src\ for example the methods such as angleToDestination, distanceTo, or turnToward

*/
public class PlayerExample extends Player
{
	//pixels per second
	private double speed = 50;
	
	//degrees per second
	private double turnRate = 30;

	//Constructor
	public PlayerExample(Board board)
	{
		//Mandatory. Do not change.
		super(board);
	}
	
	/* This method is called once per
	 * frame to update this sprite. */
	 @Override
	public void update(double elapsed_seconds)
	{
		//Mandatory. Do not change.
		super.update(elapsed_seconds);
		
		strategy1();
	}

	//Exectues a strategy to efficiently
	//collect all the dots
	private void strategy1()
	{
		/* Get the sensor report for
		a sensing straight ahead.
		Will return null if nothing
		is sensed. */
		SensorReport report = sense(0);
		
		//Choose direction to turn
		if(report!=null){
			if(report.getType().equals("good"))
			{
				//Something good is dead ahead.
				//Don't turn, Get it!
				//Move at speed pixels per second
				this.moveAtSpeed(speed);
			}
			else
			{
				//Otherwise turn away
				this.turnAway(report.getCoordinates(), turnRate);
				//You can also turn away using x,y coordinates
				//turnAway(double x, double y, double angleInDegreesPerSec);
			}
		}else{
			//turn left at a rate of
			//turnRate degrees per second
			turnLeft(turnRate);
			//You can also turn left with this:
			//turnCounterClockwise(turnRate);
			//You can turn right with either of the following:
			//turnRight(turnRate);
			//turnClockwise(turnRate);
		}
		//You can even get the coordinates
		//of the most recently sensed dot.
		//These could be remembered for 
		//later use.
		if(report != null)
		{
			System.out.println(report);
			
			//You can also turn toward a given x,y coordinate using
			//turnToward(double[] coordinates, double angleInDegreesPerSec);
			//or
			//turnToward(double x, double y, double angleInDegreesPerSec);
		}
	}

}