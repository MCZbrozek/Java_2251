public class Circle extends Shape
{
	protected double radius;
	
	public Circle()
	{
		this(1.0, "red");
	}
	
	public Circle(double radius)
	{
		this(radius, "red");
	}
	
	public Circle(double radius, String color)
	{
		this.radius = radius;
		this.color = color;
	}
	
	public double getRadius()
	{
		return radius;
	}
	public void setRadius(double r)
	{
		radius = r;
	}

	@Override
	public double getArea()
	{
		return Math.PI * radius * radius;
	}
	
	@Override
	public double getPerimeter()
	{
		return 2 * Math.PI * radius;
	}

}