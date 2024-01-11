
public class MissileSeeking extends Sprite
{	//reference to Board so sprites can be added to it 
	//by explosions and fired guns.
	private Board board_reference;
	//Seek this sprite
	private Sprite target;
	private double turn_rate = 3.0;
	private double speed = 250.0;
	//missile animation
	private int image_number = 0;
	private final int image_number_max = 9;
	private final double image_change_reset = 0.02;
	private double image_change_countdown = image_change_reset;

	//x and y are the coordinates the missile will initially
	//center on.
	public MissileSeeking(
			double x, double y,
			double angle,
			Board b,
			Sprite target)
	{
		super(x, y,
				angle,
				20.0, //collision radius
				"alien_missile0",
				Board.seeker_missile_bitmask);
		this.target = target;
		this.board_reference = b;
		//load the image
		this.setImage(this.image_file, //String image_id
				this.getAngle()); //double angle
		//Re-center the missile on its given x and y coordinates
		moveToCenter(this.getX(), this.getY());
	} //public Missile(double x, double y, double angle)

	public void update(double elapsed_seconds)
	{
		this.turnToward(target.getXCenter(), target.getYCenter(),
			turn_rate, elapsed_seconds);
		this.dx = Math.cos(this.getAngle())*speed;
		this.dy = Math.sin(this.getAngle())*speed;
		this.move(elapsed_seconds);
		//Animate the missile
		this.image_change_countdown -= elapsed_seconds;
		if(this.image_change_countdown < 0)
		{
			image_number = (image_number+1)%image_number_max;
			image_change_countdown = image_change_reset;
			this.image_file = "alien_missile"+Integer.toString(this.image_number);
			this.setImage(this.image_file, this.getAngle());
		}
	} //public void update(Craft craft, ArrayList<Sprite> sprite_list, int index)

	//Damage other and then remove this missile from the game.
	public void handleCollision(Sprite other)
	{	//Remove this missile
		this.destroyMe();
		//Add an explosion
		this.board_reference.addIntangible(
				new Explosion(this.getXCenter(),
							  this.getYCenter(),
							  0, //angle
							  0.1, //timeout
							  "images/red_hot_cross.png")
				);
	} //public void handleCollision(Sprite other)
}
