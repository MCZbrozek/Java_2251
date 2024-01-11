
/* This class is designed to move without
user input from the keyboard so students
can write the code that moves it instead. */
public class ExampleGridRobot extends SpriteCircular
{
	//Use this array to maintain alignment without
	//fear of rounding errors.
	private static final double[] ANGLE_OPTIONS = {0, Math.PI/2, Math.PI, 3*Math.PI/2};
	private int angle_index = 0; //Current angle

	public ExampleGridRobot(double x, double y, 
			double collision_radius, 
			String image_file)
	{
		super(x, y, 
			0, //initial angle
			collision_radius, 
			image_file);
		//String image_id, double angle, int custom_width, int custom_height, boolean use_custom
		this.setImage(image_file, this.getAngle());
	}
	
	/* This method is intended to be called
	once per frame and will update this sprite. */
	public void update(double elapsed_seconds)
	{
	}
	
	public void turnLeft()
	{
		angle_index--;
		if(angle_index<0){
			angle_index=ANGLE_OPTIONS.length-1;
		}
		this.setAngle(ANGLE_OPTIONS[angle_index]);
	}
	public void turnRight()
	{
		angle_index = (angle_index+1)%ANGLE_OPTIONS.length;
		this.setAngle(ANGLE_OPTIONS[angle_index]);
	}
	
	//Get a number of interpolated angles for smoother turning
	public double[] getLeftTurnInterp(int interpCount)
	{
		double currentAngle = this.getAngle();
		angle_index = angle_index-1;
		
		//TESTING
		//System.out.println("\n currentAngle = "+currentAngle);
		//System.out.println("index = "+angle_index);
		
		if(angle_index<0){
			//Deal with the weirdness that occurs
			//when turning left from angle 0 to
			//angle 3pi/2.
			currentAngle = 2*Math.PI;
			angle_index=ANGLE_OPTIONS.length-1;
		}
		double goalAngle = ANGLE_OPTIONS[angle_index];
		
		double increment = (goalAngle-currentAngle)/interpCount;
		
		double[] angles = new double[interpCount];
		for(int i=0; i<interpCount-1; i++)
			angles[i] = currentAngle+increment*i;
		//Set the goal angle explicitly to avoid rounding errors.
		angles[angles.length-1] = goalAngle;
		
		//TESTING
		//for(int i=0; i<angles.length; i++)
		//	System.out.printf("%5.1f", angles[i]);
		
		return angles;
	}
	public double[] getRightTurnInterp(int interpCount)
	{
		double currentAngle = this.getAngle();
		angle_index = (angle_index+1)%ANGLE_OPTIONS.length;
		
		//TESTING
		//System.out.println("\n currentAngle = "+currentAngle);
		//System.out.println("index = "+angle_index);
		
		double goalAngle = 0;
		double increment = 0;
		if(angle_index==0){
			//Deal with the weirdness that occurs
			//when turning right from angle 3pi/2
			//to angle 0.
			increment = (2*Math.PI-currentAngle)/interpCount;
		}
		else
		{
			goalAngle = ANGLE_OPTIONS[angle_index];
			increment = (goalAngle-currentAngle)/interpCount;
		}
		
		double[] angles = new double[interpCount];
		for(int i=0; i<interpCount-1; i++)
			angles[i] = currentAngle+increment*i;
		//Set the goal angle explicitly to avoid rounding errors.
		angles[angles.length-1] = goalAngle;
		
		//TESTING
		//for(int i=0; i<angles.length; i++)
		//	System.out.printf("%5.1f", angles[i]);
		
		return angles;
	}
		
	/* Returns a tuple (x,y) coordinate of the position that the player would be centered on if it moved ahead one. This is used in Main for detecting what sort of object is ahead of the player. */
	public double[] getPositionAhead()
	{
		double[] coordinates = this.getCenter();
		//Use height as the distance ahead.
		//Assumes height and width are pretty much the same.
		coordinates[0] = (int)(coordinates[0] + Math.cos(this.getAngle()) * this.getHeight());
		coordinates[1] = (int)(coordinates[1] + Math.sin(this.getAngle()) * this.getHeight());
		return coordinates;
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{

	}
}