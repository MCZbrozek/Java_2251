public class OilPainting extends Painting
{
	private String medium = "oil";
	
	public OilPainting(double value, String artist)
	{
		super(value, artist);
	}
	
	@Override
	public String toString()
	{
		return super.toString()+" in "+medium;
	}
	
	//Overload
	public void getFamous()
	{
		//Double the value
		this.multiplyValue(2);
	}
	
	//Overload
	public void getFamous(double how_famous)
	{
		this.multiplyValue(how_famous);
	}

}