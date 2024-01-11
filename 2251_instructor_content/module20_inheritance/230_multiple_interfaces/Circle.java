public class Circle implements Polygon
{
	private double radius;
	private int sides;

	public Circle(double radius)
	{
		this.radius = radius;
		this.sides = 1;
	}

	@Override
	public double calcArea()
	{
		return Math.PI * this.radius * this.radius;
	}
	
	@Override
	public int getSides()
	{
		return this.sides;
	}
	
	@Override
	public double calcPerimeter()
	{
		return 2*Math.PI*this.radius;
	}
	
}