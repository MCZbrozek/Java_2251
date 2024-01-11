public class Main
{
	public static void main(String[] args)
	{
		Polygon r = new Rectangle(7.3, 9);
		System.out.println("The rectangle's area is "+r.calcArea());
		System.out.println("The rectangle's perimeter is "+r.calcPerimeter());
		System.out.println("Rectangles have "+r.getSides()+" sides.");

		Polygon c = new Circle(2);
		System.out.println("The circle's area is "+c.calcArea());
		System.out.println("The circle's perimeter is "+c.calcPerimeter());
		System.out.println("Circles have "+c.getSides()+" side (or an infinite number of sides, depending on how you want to look at it).");

		System.out.println();

		Polygon[] poly_list = {r, c};
		for( Polygon p : poly_list )
		{
			System.out.println( p.calcArea() );
		}
	}
}