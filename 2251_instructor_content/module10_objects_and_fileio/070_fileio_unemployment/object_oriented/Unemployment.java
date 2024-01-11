public class Unemployment
{
	//11 year's worth of data is in the file
	private UnemploymentData[] data = new UnemploymentData[11];
	
	private int index = 0;
	
	//Constructor
	public Unemployment()
	{
		for(int i=0; i<11; i++)
		{
			data[i] = new UnemploymentData();
		}
	}
	
	public void addYear(int year)
	{
		data[index].setYear(year);
	}
	
	public void addData(double d)
	{
		data[index].addData(d);
	}
	
	public void incrementYear()
	{
		index++;
	}
	
	@Override
	public String toString()
	{
		String temp = String.format("%s %12s\n", "Year", "Median");
		for(int i=0; i<data.length; i++)
		{
			temp += data[i].toString();
		}
		return temp;
	}
}