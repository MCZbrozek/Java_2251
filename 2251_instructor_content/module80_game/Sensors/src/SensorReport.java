public class SensorReport
{
	private double x;
	private double y;
	private double distance;
	private String type;

	//Constructor 1
	public SensorReport(double x, double y, double distance, String type)
	{
		this.x = x;
		this.y = y;
		this.distance = distance;
		this.type = type;
	}
	//Constructor 2
	public SensorReport(double[] coords, double distance, String type)
	{
		this(coords[0], coords[1], distance, type);
	}
	
	//Getters
	public double getX(){return x;}
	public double getY(){return y;}
	public double getDistance(){return distance;}
	public String getType(){return type;}

	public double[] getCoordinates(){
		double[] d = {x,y};
		return d;
	}

	@Override
	public String toString()
	{
		return String.format(type+" located at %.1f,%.1f distance %.1f pixels away\n",x,y,distance);
	}
}