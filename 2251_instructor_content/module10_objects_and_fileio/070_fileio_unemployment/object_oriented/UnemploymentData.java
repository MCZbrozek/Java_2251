//https://www.w3schools.com/java/java_arraylist.asp
import java.util.ArrayList;

//https://www.javatpoint.com/how-to-sort-arraylist-in-java
import java.util.Collections;


public class UnemploymentData
{
	private int year;
	private ArrayList<Double> data;

	//Constructor
	public UnemploymentData()
	{
		data = new ArrayList<Double>();
	}
	
	public void setYear(int year)
	{
		this.year = year;
	}

	public int getYear()
	{
		return this.year;
	}
	
	public void addData(double d)
	{
		data.add(d);
	}
	
	public double getMedian()
	{
		//https://www.javatpoint.com/how-to-sort-arraylist-in-java
		Collections.sort(data);
		return data.get(data.size()/2);
	}
	
	@Override
	public String toString()
	{
		return String.format("%d %12.2f\n",
			this.getYear(),
			this.getMedian());
	}
}
