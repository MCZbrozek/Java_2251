import java.lang.Math;

class Position
{
	public double distanceFromZero(int x, int y)
	{
		return Math.sqrt(x*x + y*y);
	}
	
	public String toString(int x, int y)
	{
		return x + ", " + y;
	}
}