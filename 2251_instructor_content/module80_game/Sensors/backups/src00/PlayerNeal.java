
public class PlayerNeal extends Player
{
	private double speed = 50; //pixels per second
	private double turnRate = 30; //degrees per second
	private boolean maintainCounter = false;
	private boolean maintainClockwise = false;

	public PlayerNeal(Board board)
	{
		super(board);
	}
	
	/* This method is intended to be called once per frame and will 
	 * update this sprite. */
	 @Override
	public void update(double elapsed_seconds)
	{
		//Mandatory. Do not change.
		super.update(elapsed_seconds);
		
		//strategy00();
		//strategy01();
		//strategy02();
		//strategy03();
		//strategy04(elapsed_seconds); //cheating
		strategy05(elapsed_seconds); //ultra cheating
	}


	private void strategy00()
	{
		//Sense 45 degrees to the right (clockwise)
		double distClockwise = sense(45);
		//Sense straight ahead
		double distCenter = sense(0);
		//Sense 45 degrees to the left (counterclockwise)
		double distCounter = sense(-45);		
		//Choose directions

		this.turnRate = 30;
		if(distCenter!=-1){
			//Something is dead ahead, get it.
			//Speed up!
			speed++;
		}else{
			//slow down to a minimum of 50
			speed--;
			if(speed < 50)
				speed = 50;
			if(distCounter!=-1){
				turnCounterClockwise(30);
			}
			else{
				turnClockwise(turnRate);
			}
		}
		//Move at speed pixels per second
		this.moveAtSpeed(speed);
	}

	private void strategy01()
	{
		//Sense straight ahead
		double distCenter = sense(0);
		
		//Choose directions
		if(distCenter!=-1){
			//Something is dead ahead, get it.
			//Speed up!
			speed++;
		}else{
			//slow down to a minimum of 50
			speed--;
			if(speed < 50)
				speed = 50;
			//turn
			turnCounterClockwise(turnRate);
		}
		//Move at speed pixels per second
		this.moveAtSpeed(speed);
	}

	private void strategy02()
	{
		//Sense straight ahead
		double distCenter = sense(0);
		
		//Choose directions
		if(distCenter!=-1){
			//Something is dead ahead, get it.
			//Speed up!
			speed = speed + 5;
		}else{
			//stop
			speed = 0;
			//turn
			turnCounterClockwise(turnRate);
		}
		//Move at speed pixels per second
		this.moveAtSpeed(speed);
	}

	private void strategy03()
	{
		//Sense 45 degrees to the right (clockwise)
		double distClockwise = sense(45);
		//Sense straight ahead
		double distCenter = sense(0);
		//Sense 45 degrees to the left (counterclockwise)
		double distCounter = sense(-45);

		//Choose directions
		if(distCenter!=-1){
			//Something is dead ahead, get it.
			//Speed up!
			speed = speed + 5;
			turnRate = 30; //Reset turn rate;
			maintainCounter = false;
			maintainClockwise = false;
		}else if(maintainCounter){
			//Otherwise if something was noticed
			//counterclockwise, turn that way
			//until it's centered.
			speed = 0; //stop
			//turn 45 degrees per second
			//turnCounterClockwise(45);
			turnLeft(45); //Same as turnCounterClockwise(45);
		}else if(maintainClockwise){
			//Otherwise if something was noticed
			//clockwise, turn that way
			//until it's centered.
			speed = 0; //stop
			//turn 45 degrees per second
			//turnCounterClockwise(45);
			turnRight(45); //Same as turnClockwise(45);
		}else{
			//Otherwise turn clockwise and update the "maintain" variables
			maintainCounter = distCounter!=-1;
			maintainClockwise = distClockwise!=-1;
			speed = 0; //stop
			//turn 45 degrees per second
			//turnCounterClockwise(45);
			turnRight(45); //Same as turnClockwise(45);
		}
		
		//Move at speed pixels per second
		this.moveAtSpeed(speed);
	}

	private void strategy04(double elapsed_seconds)
	{	//Basically cheating
	
		double sensorAngles = 60;
	
		//Sense 60 degrees to the right (clockwise)
		double distClockwise = sense(sensorAngles);
		//Sense straight ahead
		double distCenter = sense(0);
		//Sense 60 degrees to the left (counterclockwise)
		double distCounter = sense(-sensorAngles);

		//Choose directions
		if(distCenter!=-1){
			//Jump right to it
			this.moveFixed((int)distCenter);
		}else if(distCounter!=-1){
			//turn right to it
			turnCounterClockwise(sensorAngles*Math.PI/180,1);
		}else if(distClockwise!=-1){
			//turn right to it
			turnClockwise(sensorAngles*Math.PI/180,1);
		}else{
			//Otherwise turn clockwise
			speed = 0; //stop
			turnRate = Math.PI/2; //turn faster
			//turn
			turnClockwise(turnRate,elapsed_seconds);
		}
	}
	
	private void strategy05(double elapsed_seconds)
	{	//Basically cheating
	
		double[] nearestDotPosition = null;
		double distToNearest = Double.MAX_VALUE;
		double dist;
		//Sweep the sensor through a circle
		for(int i=0; i<360; i++)
		{
			dist = sense(i);
			//Identify the nearest spot
			if(sensedObject() && dist < distToNearest)
			{
				nearestDotPosition = getSensedLocation();
				distToNearest = dist;
			}
		}
		
		//turn to face nearest
		if(nearestDotPosition != null){
			turnToward(nearestDotPosition,360);
		}else{
			//Turn ever so slightly so a different 360 is searched
			turnLeft(0.3);
		}
		
		//move right to it
		this.moveFixed((int)distToNearest);
	}
}