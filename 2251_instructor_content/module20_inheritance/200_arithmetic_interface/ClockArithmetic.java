public class ClockArithmetic extends IntegerArithmetic
{
	@Override
	public int add(int a, int b)
	{
		recent_results = super.add(a,b);
		recent_results = fixValue(recent_results);
		return recent_results;
	}
	
	@Override
	public int subtract(int a, int b)
	{
		recent_results = super.subtract(a,b);
		recent_results = fixValue(recent_results);
		return recent_results;
	}
	
	private int fixValue(int x)
	{
		//Fix negative numbers
		while(x<=0)
		{
			x = x+12;
		}
		//Fix positive numbers greater than 12
		while(12 < x)
		{
			x = x-12;
		}
		return x;
	}
}


