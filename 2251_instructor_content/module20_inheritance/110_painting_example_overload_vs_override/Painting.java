public class Painting
{
	private double value;
	private String artist;
	
	public Painting(double value, String artist)
	{
		this.value = value;
		this.artist = artist;
	}
	
	public void multiplyValue(double x)
	{
		this.value = this.value*x;
	}
	
	@Override
	public String toString()
	{
		return "$"+Double.toString(value)+" by "+artist;
	}
}