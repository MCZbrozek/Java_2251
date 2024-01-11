public class IntegerArithmetic implements Arithmetic
{
	
	protected int recent_results = 0;

	public int getRecentResult()
	{
		return recent_results;
	}
	
	@Override
	public int add(int a, int b)
	{
		recent_results = a+b;
		return recent_results;
	}
	
	@Override
	public int subtract(int a, int b)
	{
		recent_results = a-b;
		return recent_results;
	}
}