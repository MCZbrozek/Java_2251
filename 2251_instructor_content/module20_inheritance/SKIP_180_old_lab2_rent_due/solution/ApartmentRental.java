public class ApartmentRental extends RentalProperty
{
	//constructor for ApartmentRental
	public ApartmentRental (int sequenceNumber, String rentalType,String rentalID, int bedrooms, float rent)
	{
		super(sequenceNumber, rentalType, rentalID, bedrooms, rent);
	}
	
	//implement the method from interface Payment
	@Override
	public double getRent()
	{
		rent = rent*1.08f;
		return rent;
	}

}