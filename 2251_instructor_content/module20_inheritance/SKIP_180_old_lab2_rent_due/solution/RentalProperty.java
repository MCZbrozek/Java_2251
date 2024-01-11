public class RentalProperty implements Payment, Comparable<RentalProperty>
{
	//initialize the instance variables to 0 and "" 
	protected int sequenceNumber=0;
	protected String rentalType="";
	protected String rentalID="";
	protected int bedrooms=0;
	protected float rent=0.0f;
	
	//constructor for the rental property
	public RentalProperty (int sequenceNumber, String rentalType,String rentalID, int bedrooms, float rent)
	{
		this.sequenceNumber=sequenceNumber;
		this.rentalType=rentalType;
		this.rentalID=rentalID;
		this.bedrooms=bedrooms;
		this.rent=rent;
	}
	
	//methods from implementing the Payment interface getRent and updateRent
	public double getRent() {
		return this.rent;
	}
	
	//method to return string format for fileWriter in driver class
	public String updateRent() 
	{
		String update = String.format("     %d   %s   %s  %d%10.2f%n", sequenceNumber, rentalType, rentalID, bedrooms, rent);
		return update;
	}
	
	//toString method to output to the screen in proper format
	public String toString()
	{
		String output = String.format("   %s               $%,9.2f", rentalID, rent);
		return output;
	}
	
	//method to implement comparable to allow sorting based on rentalID
	@Override
	public int compareTo(RentalProperty rental)
	{
		return this.rentalID.compareTo(rental.rentalID);
	}
}
