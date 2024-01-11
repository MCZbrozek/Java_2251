public class DateValue implements Comparable<DateValue>
{
	private int month, day, year;
	private double value;

	public DateValue(int month, int day, int year, double value)
	{
		this.month = month;
		this.day = day;
		this.year = year;
		this.value = value;
	}
	
	public String toString()
	{
		return month+"/"+day+"/"+year+" with value: "+value;
	}
	
	public int getYear(){ return year; }
	public int getMonth(){ return month; }
	public int getDay(){ return day; }
	
	@Override
	/*
	 * This is where we write the logic to sort.
	 */
	public int compareTo(DateValue other)
	{
		int comparison = this.year - other.getYear();
		if(comparison != 0)
			return comparison;
		else
		{
			comparison = this.month - other.getMonth();
			if(comparison != 0)
				return comparison;
			else
			{
				comparison = this.day - other.getDay();
				return comparison;
			}
		}
	}
}