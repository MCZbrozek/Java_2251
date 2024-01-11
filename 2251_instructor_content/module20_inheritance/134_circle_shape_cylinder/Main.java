//Source: https://www3.ntu.edu.sg/home/ehchua/programming/java/j3f_oopexercises.html

public class Main
{
	public static void main(String args[])
	{
		Circle c = new Circle();
		System.out.println("Area = "+c.getArea());

		Cylinder cyl = new Cylinder(2.0, 3.0);
		System.out.println("Surface Area = "+cyl.getArea());

	}
}