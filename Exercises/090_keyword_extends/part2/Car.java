
class Car extends Vehicle
{
	private String modelName = "Mustang";	// Car attribute
	
	@Override
	public String toString()
	{
		return this.brand + " " + this.modelName;
	}
}