public class Rectangle implements Polygon
{
	private double width;
	private double length;
	private int sides;

	public Rectangle(double width, double length)
	{
		this.width = width;
		this.length = length;
		this.sides = 4;
	}

	@Override
	public double calcArea()
	{
		return this.width*this.length;
	}
	
	@Override
	public int getSides()
	{
		return this.sides;
	}
	
	@Override
	public double calcPerimeter()
	{
		return 2*this.width + 2*this.length;
	}
	
}