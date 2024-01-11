public class SingleFamilyRental extends RentalProperty
{
	//constructor for SingleFamilyRental
	public SingleFamilyRental (int sequenceNumber, String rentalType,String rentalID, int bedrooms, float rent)
	{
		super(sequenceNumber, rentalType, rentalID, bedrooms, rent);
	}
	
	//implement the method from interface Payment
	@Override
	public double getRent()
	{
		rent = rent+rent*.04f;
		return rent;
	}

}