public class TestThread
{
	public static void main(String args[])
	{
		ThreadDemo T1 = new ThreadDemo();
		T1.start();
		
		ThreadDemo T2 = new ThreadDemo();
		T2.start();
	}
}