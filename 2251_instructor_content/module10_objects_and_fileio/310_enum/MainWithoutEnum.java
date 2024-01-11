public class MainWithoutEnum
{
	private static final int LOW = 1;
	private static final int MEDIUM = 2;
	private static final int HIGH = 3;

	public static void main(String[] args)
	{
		int myVar = 87654;
		System.out.println(myVar);
		
		if(myVar == MEDIUM)
		{
			System.out.println("Changing myVar");
			myVar = HIGH;
		}
		System.out.println(myVar);
	}
}
