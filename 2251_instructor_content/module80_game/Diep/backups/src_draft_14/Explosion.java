import java.awt.*;

public class Explosion extends SpritePhysics
{
	private double timeout; //seconds before vanishing
	private double timeout_reset;
	private int image_index = 0; //index of the first image in the sequence.
	private int image_max = 0;
	private String image_base_name;
	
    public Explosion(double x, double y,
    		double angle,
    		double timeout,
    		String image_file)
    {
        super(x, y, angle,
        		image_file,
        		0, //I am Collision bitmask
        		0); //I hit Collision bitmask
        this.timeout = timeout;
        //String image_id, double angle
        setImage(image_file, angle);
        this.moveToCenter(x, y);
        this.setVisible(true);
    }

    //This constructor is used
	//for the firey sequence of explosions.
    public Explosion(double x, double y,
    		double angle,
    		double timeout,
    		String image_file,
    		int image_max)
    {
        super(x, y, angle,
        		image_file+"0",
        		0, //I am Collision bitmask
        		0); //I hit Collision bitmask
        //Keep the base image name so we can append the number
        //to it to get the next image in the sequence.
        this.image_base_name = image_file;
        //We will need to reset the timer for each image in the sequence.
        this.timeout = timeout;
        this.timeout_reset = timeout;
        //This is the largest image number in the sequence.
        this.image_max = image_max;
        //String image_id, double angle
        setImage(image_file+"0", angle);
        this.moveToCenter(x, y);
        this.setVisible(true);
    }

    public void update(double elapsed_seconds)
    {
    	timeout -= elapsed_seconds;
    	if (timeout < 0) 
    	{	//Check for a sequence of images. This is used
    		//for the firey sequence of explosions.
    		if(this.image_index < this.image_max)
    		{	//Advance to the next image.
    			this.image_index++;
    	        //String image_id, double angle
    	        setImage(this.image_base_name+Integer.toString(this.image_index),
    	        		this.getAngle());
    	        //Reset the timer.
    	        this.timeout = this.timeout_reset;
    		}
    		else
    		{
    			this.setRemoveMeTrue();
    		}
    	}
        this.move(elapsed_seconds);
    } //public void update(Craft craft, ArrayList<Sprite> sprite_list, int index)

	@Override
	Rectangle getBoundingRectangle(){ return null; }

	@Override
	public void handleCollision(SpritePhysics spritePhysics){}

	@Override
	public boolean checkCollided(SpritePhysics spritePhysics){return false;}

	@Override
	public void shoveOut(SpritePhysics spritePhysics){}
}