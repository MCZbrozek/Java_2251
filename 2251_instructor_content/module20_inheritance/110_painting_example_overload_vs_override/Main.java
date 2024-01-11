public class Main
{
	public static void main(String[] args)
	{
		OilPainting vermeer = new OilPainting(100.0, "Vermeer");
		
		System.out.println(vermeer);
		
		vermeer.getFamous();

		System.out.println(vermeer);

		vermeer.getFamous(5.1);

		System.out.println(vermeer);
	}
}