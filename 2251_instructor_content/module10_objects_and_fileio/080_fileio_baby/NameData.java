
public class NameData
{
	private int rank;
	private int year;
	private String name;
	private int frequency;

	//constructor
	public NameData(int rank, int year, String name, int frequency)
	{
		this.rank = rank;
		this.year = year;
		this.name = name;
		this.frequency = frequency;
	}
	
	public int getYear(){ return year; }
	public String getName(){ return name; }
	public int getRank(){ return rank; }
	
	@Override
	public String toString()
	{
		return String.format("%10d %10d %10s %10d\n",
					rank, year, name, frequency);
	}
}
