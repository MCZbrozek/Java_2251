
public class ExampleGridMover extends SpriteCircular
{
	private KeyManager key_setter;
	private Board board;

	//Time to wait between actions in seconds
	private static final double WAITTIME = 0.25;
	private double waitCountdown = 0.0;

	//Use this array to maintain alignment without
	//fear of rounding errors.
	private static final double[] ANGLE_OPTIONS = {0, Math.PI/2, Math.PI, 3*Math.PI/2};
	private int angle_index = 0; //Current angle

	public ExampleGridMover(double x, double y, 
			double collision_radius, 
			String image_file,
			KeyManager key_setter,
			int i_am_bitmask,
			int i_hit_bitmask,
			Board board)
	{
		super(x, y, 
			0, //initial angle
			collision_radius, 
			image_file, 
			i_am_bitmask,
			i_hit_bitmask);
		this.key_setter = key_setter;
		this.board = board;
		//String image_id, double angle, int custom_width, int custom_height, boolean use_custom
		this.setImage(image_file, this.getAngle());
	}
	
	/* This method is intended to be called
	once per frame and will update this sprite. */
	public void update(double elapsed_seconds)
	{
		waitCountdown -= elapsed_seconds;
		
		if(waitCountdown > 0){ return; }
		
		/*
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace();
			System.exit(1);
		}*/
		
		//this.moveOnGrid();
		this.moveForwardOnGrid();
	}
	
	private void rotateCounterClock()
	{
		angle_index--;
		if(angle_index<0){
			angle_index=ANGLE_OPTIONS.length-1;
		}
		this.setAngle(ANGLE_OPTIONS[angle_index]);
	}
	private void rotateClock()
	{
		angle_index = (angle_index+1)%ANGLE_OPTIONS.length;
		this.setAngle(ANGLE_OPTIONS[angle_index]);
	}
	
	private void moveForwardOnGrid()
	{
		//Rotation
		if(key_setter.ascii_input[37]){//left key
			//Rotate counter clockwise
			this.rotateCounterClock();
			waitCountdown = WAITTIME;
		}else if(key_setter.ascii_input[39]){//right key
			//Rotate clockwise
			this.rotateClock();
			waitCountdown = WAITTIME;
		}
		//Vertical motion
		if(key_setter.ascii_input[38]){//up key
			//Move forwards
			/* Here I use height as the amount
			to move. I'm assuming height and
			width are the same. */
			this.moveFixed(this.getHeight());
			waitCountdown = WAITTIME;
		}else if(key_setter.ascii_input[40]){//down key
			//Move backwards
			this.moveFixed(-this.getHeight());
			waitCountdown = WAITTIME;
		}
	}
	
	private void moveOnGrid()
	{
		//Horizontal motion
		if(key_setter.ascii_input[37]){//left key
			this.setX(this.getX() - this.getWidth());
			waitCountdown = WAITTIME;
		}else if(key_setter.ascii_input[39]){//right key
			this.setX(this.getX() + this.getWidth());
			waitCountdown = WAITTIME;
		}
		//Vertical motion
		if(key_setter.ascii_input[38]){//up key
			this.setY(this.getY() - this.getHeight());
			waitCountdown = WAITTIME;
		}else if(key_setter.ascii_input[40]){//down key
			this.setY(this.getY() + this.getHeight());
			waitCountdown = WAITTIME;
		}
	}

	/* Handle a collision between this sprite and other. */
	public void handleCollision(SpritePhysics other)
	{

	}
}