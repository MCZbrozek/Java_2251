public class Cylinder extends Circle
{
	private double height;
	
	public Cylinder(double radius, double height)
	{
		this(radius, height, "red");
	}

	public Cylinder(double radius, double height, String color)
	{
		super(radius, color);
		this.height = height;
	}
	
	@Override
	public double getArea()
	{
		//Area = 2 * circle area + height * perimeter
		return 2*super.getArea() + height * getPerimeter();
	}
}





