import java.lang.Math;

class Position
{
	//instance variables:
	private int x;
	private int y;
	
	//setters
	public void setX(int x){ this.x = x; }
	public void setY(int y){ this.y = y; }

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