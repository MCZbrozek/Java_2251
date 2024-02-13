import java.lang.Math;

class Position
{
	//instance variables:
	private int x;
	private int y;
	
	//constructor
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public double distanceFromZero()
	{
		return Math.sqrt(x*x + y*y);
	}
	
	@Override
	public String toString()
	{
		return x + ", " + y;
	}
}