class ThreadDemo extends Thread
{
	private int a;
	private int b;
	private int result;
	
	public ThreadDemo(int a, int b)
	{
		this.a = a;
		this.b = b;
	}
	
	//This is the only method that needs implemented to extend Thread
	@Override
	public void run()
	{
		result = a+b;
	}
	
	public int getResult(){ return result; }
}