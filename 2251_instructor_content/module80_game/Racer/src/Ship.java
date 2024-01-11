import java.awt.Color;
import java.util.ArrayList;

/* An example class that the player controls.
Responds to keyboard input.
*/
public class Ship extends SpriteTopDown
{
	private KeyManager key_setter;
	private Board board;
	private String name;
	private double destinationX;
	private double destinationY;

	private double turn_rate = Math.PI/2; //radians per sec
	private double speed = 150; //pixels per sec

	//max range for sensors
	private double maxRange = 300;

	public Ship(String name,
			double x, double y, 
			double collision_radius, 
			KeyManager key_setter,
			int i_am_bitmask,
			int i_hit_bitmask,
			Board board,
			double destinationX,
			double destinationY)
	{
		super(x, y, 
			100, //health
			0, //angle
			collision_radius, 
			i_am_bitmask,
			i_hit_bitmask);
			this.name = name;
		this.key_setter = key_setter;
		this.board = board;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
	}
	
	public String getName(){return name;}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	public void update(double elapsed_seconds)
	{
		int mode = 0;
		if(mode==0)
			this.ballisticMotion(elapsed_seconds);
		else if(mode==1)
			autoMotion01(elapsed_seconds);
		else if(mode==2)
			autoMotion02(elapsed_seconds);
	}
	
	private void autoMotion01(double elapsed_seconds)
	{
		//draft 1:
		//if goal is to the left, turn left
		//else turn right
		//move ahead
		double angleDiff = getAngleDiff(destinationX,destinationY);
		//A positive result indicates that this should rotate clockwise, a negative result indicates that this should rotate counter clockwise
		if(angleDiff > 0)
			this.turnClockwise(turn_rate, elapsed_seconds);
		else
			this.turnCounterClockwise(turn_rate, elapsed_seconds);

		if(Math.abs(angleDiff) < Math.PI/8){
			speed++;
		}else{
			speed--;
			if(speed<0)
				speed=0;
		}
		
		move(elapsed_seconds);
	}

	private void autoMotion02(double elapsed_seconds)
	{
		//TODO LEFT OFF HERE
		//draft 2:
		//if goal left
			//if wall left, move straight ahead
			//if wall left and ahead, turn right
		//if goal right
			//if wall right, move straight
			//if wall right and ahead, turn left
		//if goal ahead
			//if wall left and ahead, turn right
			//if wall right and ahead, turn left
			//if wall ahead, turn left (arbitrarily decided)
			//else move straight ahead
		double angleDiff = getAngleDiff(destinationX,destinationY);
		SensorReport left = sense(-90);
		SensorReport ahead = sense(0);
		SensorReport right = sense(90);
		double angleMargin = Math.PI/8;
		System.out.println();
		System.out.println("left null "+(left==null));
		System.out.println("ahead null "+(ahead==null));
		System.out.println("right null "+(right==null));
		System.out.println(angleDiff);
		//if goal left
		if(angleDiff < -angleMargin){
			System.out.println("Goal left");
			//if wall left
			if(left!=null && left.getType().equals("wall")){
				System.out.println("wall left");
				//move straight ahead
				move(elapsed_seconds);
			}
			//if wall left and ahead, turn right
			else if(left!=null && left.getType().equals("wall") && ahead!=null && ahead.getType().equals("wall")){
				System.out.println("wall left & ahead");
				this.turnClockwise(turn_rate, elapsed_seconds);
			}
			else{
				System.out.println("no wall");
				//turn left
				this.turnCounterClockwise(turn_rate, elapsed_seconds);
			}
		//if goal right
		}else if(angleDiff > angleMargin){
			System.out.println("Goal right");
			//if wall right
			if(right!=null && right.getType().equals("wall")){
				System.out.println("wall right");
				//move straight ahead
				move(elapsed_seconds);
			}
			//if wall right and ahead, turn left
			else if(right!=null && right.getType().equals("wall") && ahead!=null && ahead.getType().equals("wall")){
				System.out.println("wall right & ahead");
				this.turnCounterClockwise(turn_rate, elapsed_seconds);
			}
			else{
				System.out.println("no wall");
				//turn right
				this.turnClockwise(turn_rate, elapsed_seconds);
			}
		}
		//if goal ahead
		else{
			System.out.println("Goal ahead");
			//if wall left and ahead, turn right
			if(left!=null && left.getType().equals("wall") && ahead!=null && ahead.getType().equals("wall")){
				System.out.println("wall left & ahead");
				this.turnClockwise(turn_rate, elapsed_seconds);
			}
			//if wall right and ahead, turn left
			else if(right!=null && right.getType().equals("wall") && ahead!=null && ahead.getType().equals("wall")){
				System.out.println("wall right & ahead");
				this.turnCounterClockwise(turn_rate, elapsed_seconds);
			}
			//if wall ahead, turn left (arbitrarily decided)
			else if(ahead!=null && ahead.getType().equals("wall")){
				System.out.println("wall ahead");
				this.turnCounterClockwise(turn_rate, elapsed_seconds);
			}
		}
		//move straight ahead
		move(elapsed_seconds);
		
		//draft 3: The ability to backtrack will require a certain amount of memory, some sort of breadcrumb trail indicating where we have and have not been before.
		//Alternatively, player could spawn a bunch of circle hit boxes (either nearby or just all over the map), push the hitboxes out of walls, then figure out nearest neighbor connections between the hitboxes, then chart a path through the network of hitboxes to the finish line and use this as the trail to get to the finish.

		/*
	double destinationX
	double destinationY

		double turn_rate = ?
		double speed = ?
		double new_dx = speed * Math.cos(getAngle());
		double new_dy = speed * Math.sin(getAngle());
		setVelocity(new_dx, new_dy);

	//returns difference between current angle/heading and the angle/heading that points toward the given x,y coordinates
	//A negative result indicates that this should rotate clockwise, a positive result indicates that this should rotate counter clockwise
	public double getAngleDiff(
		double x_destination,
		double y_destination)

		move(elapsed_seconds)

		this.turnClockwise(turn_rate, elapsed_seconds);

		this.turnCounterClockwise(turn_rate, elapsed_seconds);
*/
	}
	
	@Override
	public void move(double elapsed_seconds)
	{
		double new_dx = speed * Math.cos(getAngle());
		double new_dy = speed * Math.sin(getAngle());
		setVelocity(new_dx, new_dy);
		super.move(elapsed_seconds);
	}

	//Pre: angleAdjust is in degrees
	protected SensorReport sense(double angleAdjust)
	{
		//Convert to radians
		angleAdjust = angleAdjust*Math.PI/180;
		double sensorWidth = 3;
		//Get everything hit by the sensor
		ArrayList<SpritePhysics> hit_by = this.board.getLineCollisions(
			this.getXCenter(), this.getYCenter(),
			this.getAngle()+angleAdjust, maxRange, sensorWidth, true); //boolean at end turns on testing
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
		board.addSpriteIntangible(
			new DisplayLine(
				this.getXCenter(),
				this.getYCenter(),
				0.005, //Time beam remains on screen in seconds
				this.getXCenter()+Math.cos(this.getAngle()+angleAdjust)*minDistance,
				this.getYCenter()+Math.sin(this.getAngle()+angleAdjust)*minDistance,
				(int)sensorWidth,
				Color.YELLOW));
		if(minDistance == maxRange){
			return null;
		}else{
			if(nearest instanceof Finish)
				return new SensorReport(nearest.getCenter(), minDistance, "goal");
			else if(nearest instanceof Wall || nearest instanceof RoundWall)
				return new SensorReport(nearest.getCenter(), minDistance, "wall");
			else{
				System.out.println("Error in Ship.sense. Sensed unknown object");
				System.exit(1);
				return null;
			}
		} //if(minDistance == maxRange){ //If anything was sensed or not
	} //protected SensorReport sense
	
	private void ballisticMotion(double elapsed_seconds)
	{
		//TODO LEFT OFF HERE TESTING
		//sense(-90);
		sense(0);
		//sense(90);
		
		//Turning
		if(key_setter.ascii_input[37]){//left key
			this.turnCounterClockwise(2*Math.PI,//turn rate in radians per second
					elapsed_seconds);
		}else if(key_setter.ascii_input[39]){//right key
			this.turnClockwise(2*Math.PI,//turn rate in radians per second
					elapsed_seconds);
		}
		//Forward and backward motion
		if(key_setter.ascii_input[38])//up key
		{
			double acceleration = 300;
			this.ballisticAccelerate(acceleration,elapsed_seconds);
		}
		else if(key_setter.ascii_input[40])//down key
		{	//Braking
			//double friction = 0.99; //Percent reduction in velocity per second
			//this.ballisticDecelerate(friction, elapsed_seconds);
			
			double friction = 0.005; //Percent reduction in velocity per millisecond
			this.ballisticDecelerateMillis(friction, elapsed_seconds);
		}
		super.move(elapsed_seconds);
	}
	
	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{

	}
}