
public class Missile extends Sprite 
{	//Damage dealt by this missile
	protected int damage;
	//reference to Board so sprites can be added to it 
	//by explosions and fired guns.
	protected Board board_reference;

	//x and y are the coordinates the missile will initially
	//center on.
	public Missile(String image_file, 
			double x, double y,
			double angle, 
			double speed, 
			int damage, 
			Board b,
			double flight_angle,
			int collision_bitmask)
	{
		super(x, y,
				angle,
				20.0, //collision radius
				image_file,
				collision_bitmask);
		this.damage = damage;
		board_reference = b;
		this.collision_circular = false;
		this.dx = Math.cos(flight_angle)*speed;
		this.dy = Math.sin(flight_angle)*speed;
		//load the image
		this.setImage(this.image_file, //String image_id
				angle+Math.PI); //All the bullets are backwards. +Math.PI fixes it.
		//Re-center the missile on its given x and y coordinates
		moveToCenter(this.getX(), this.getY());
	} //public Missile(double x, double y, double angle)

	public void update(double elapsed_seconds)
	{
		this.move(elapsed_seconds);
		if(this.x+this.width < 0 || this.x > Main.ScreenWidth)
		{
			this.destroyMe();
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
							  0.07, //timeout
							  "star2.png")
				);
	} //public void handleCollision(Sprite other)
}