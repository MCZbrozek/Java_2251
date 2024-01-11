public class RuntimeError
{
	public static void main(String[] args)
	{
		System.out.println("Watch me divide by zero.");
		
		//Uncomment the following for a runtime error.
		int x = 1234/0;
		
		System.out.println("Did it work?");
	}
}