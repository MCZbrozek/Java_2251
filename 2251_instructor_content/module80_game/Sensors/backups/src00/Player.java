import java.awt.Color;
import java.util.ArrayList;

public class Player extends SpriteTopDown
{
	private Board board_reference;

	private int collisionCount = 0;

	//This is used to hide complexity from the programmer.
	private double elapsedSeconds;

	//Remember the most recently sensed sprite center
	private double[] sensedCoordinates = null;

	public Player(Board board)
	{
		super(100, 100, //x, y
			100, //health (not used)
			0, //initial heading
			20, //radius
			"",//image file
			2,//I am
			1);//I hit
		this.color = Color.BLUE;
		this.board_reference = board;
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		this.elapsedSeconds = elapsed_seconds;
	}

	//turn_rate is in degrees per second
	//Turns toward the given x, y coordinates
	public void turnToward(double x, double y, double turn_rate)
	{
		//Convert to radians
		turn_rate = turn_rate*Math.PI/180;
		//Turn
		this.turnToward(x,y,turn_rate, elapsedSeconds);
	}

	//coords is a length 2 double array representing x,y coordinates
	//turn_rate is in degrees per second
	//Turns toward the given x, y coordinates
	public void turnToward(double[] coords, double turn_rate)
	{
		//Convert to radians
		turn_rate = turn_rate*Math.PI/180;
		try{
			//Turn
			this.turnToward(coords[0],coords[1],turn_rate, elapsedSeconds);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("\nException in Player.java in turnToward(double[] coords, double turn_rate). First input is supposed to be length 2 double array. Instead length is "+coords.length);
			e.printStackTrace();
			System.exit(1);
		}
	}

	//turn_rate is in degrees per second
	public void turnLeft(double turn_rate)
	{
		//Convert to radians
		turn_rate = turn_rate*Math.PI/180;
		//Turn
		this.turnCounterClockwise(turn_rate, elapsedSeconds);
	}

	//turn_rate is in degrees per second
	public void turnCounterClockwise(double turn_rate)
	{
		//Convert to radians
		turn_rate = turn_rate*Math.PI/180;
		//Hide the elapsed seconds complexity
		this.turnCounterClockwise(turn_rate, elapsedSeconds);
	}

	//turn_rate is in degrees per second
	public void turnRight(double turn_rate)
	{
		//Convert to radians
		turn_rate = turn_rate*Math.PI/180;
		//Turn
		this.turnClockwise(turn_rate, elapsedSeconds);
	}

	//turn_rate is in degrees per second
	public void turnClockwise(double turn_rate)
	{
		//Convert to radians
		turn_rate = turn_rate*Math.PI/180;
		//Hide the elapsed seconds complexity
		this.turnClockwise(turn_rate, elapsedSeconds);
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other){ collisionCount++; }
	
	public int getCollisionCount(){ return collisionCount; }
	
	//Pre: speed is in pixels per second
	public void moveAtSpeed(double speed)
	{
		this.setVelocity(
			Math.cos(this.getAngle())*speed,
			Math.sin(this.getAngle())*speed);
		this.move(elapsedSeconds);
	}
	
	//Pre: angleAdjust is in degrees
	protected double sense(double angleAdjust)
	{
		//Convert to radians
		angleAdjust = angleAdjust*Math.PI/180;
		double maxRange = 1000;
		double sensorWidth = 3;
		//Get everything hit by the sensor
		ArrayList<SpritePhysics> hit_by = this.board_reference.getLineCollisions(
			this.getXCenter(), this.getYCenter(),
			this.getAngle()+angleAdjust, maxRange, sensorWidth);
		//Get the nearest object hit by the sensor, ommitting self
		double minDistance = maxRange;
		double temp;
		SpritePhysics nearest = null;
		for (SpritePhysics sp : hit_by)
		{
			temp = this.distanceTo(sp);
			if(sp!=this && temp < minDistance)
			{
				nearest = sp;
				minDistance = temp;
			}
		}
		//Draw the laser
		board_reference.addSpriteIntangible(
			new DisplayLine(
				this.getXCenter(),
				this.getYCenter(),
				0.005, //Time beam remains on screen in seconds
				this.getXCenter()+Math.cos(this.getAngle()+angleAdjust)*minDistance,
				this.getYCenter()+Math.sin(this.getAngle()+angleAdjust)*minDistance,
				(int)sensorWidth,
				Color.RED));
		if(minDistance == maxRange){
			sensedCoordinates = null;
			return -1;
		}else{
			sensedCoordinates = nearest.getCenter();
			return minDistance;
		}
	}

	public boolean sensedObject(){
		return sensedCoordinates!=null;
	}

	public double[] getSensedLocation()
	{
		return sensedCoordinates;
	}

}