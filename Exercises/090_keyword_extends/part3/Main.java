class Main
{
	public static void main(String[] args)
	{
		// Create a myCar object
		Car myCar = new Car();

		// Call the honk() method (from the Vehicle class) on the myCar object
		myCar.honk();

		myCar.drive();
		myCar.drive();
		myCar.drive();
		
		// Display the value of the brand attribute (from the Vehicle class) and the value of the modelName from the Car class
		System.out.println(myCar);
		
		Jetski watercraft = new Jetski();
		
		watercraft.goOnWater();
		
		watercraft.honk();
		
		//These are a no no:
		//myCar.goOnWater();
		//watercraft.drive();
	}
}