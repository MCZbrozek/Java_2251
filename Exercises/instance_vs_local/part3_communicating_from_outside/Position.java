import java.lang.Math;

class Position
{
	//instance variables:
	private int x;
	private int y;
	
	public double distanceFromZero(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		//I'm referring to local variables
		return Math.sqrt(x*x + y*y);
	}
	
	@Override
	public String toString()
	{
		//I'm referring to the instance variables
		return x + ", " + y;
	}
}