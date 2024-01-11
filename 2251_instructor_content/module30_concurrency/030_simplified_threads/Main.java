public class Main
{
	private static class ThreadDemo extends Thread
	{
		public void run()
		{
			System.out.println("Running ");
		}
	}
	
	public static void main(String args[])
	{
		ThreadDemo T1 = new ThreadDemo();
		ThreadDemo T2 = new ThreadDemo();

		T1.start();
		T2.start();
	}
}