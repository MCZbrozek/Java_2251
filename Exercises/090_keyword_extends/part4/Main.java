
/*
Name: Michael Zbrozek
Date: 2/16/2024
Purpose: Extends exercise
Sources:
Brightspace video
	

Files: 
Main.Java
Bike.java
Car.java
Jetski.java

*/
class Main {
	public static void main(String[] args) {

		// Create a myBike object
		Bike myBike = new Bike();

		// Call the yellAtDrivers method on the myBike object
		myBike.yellAtDrivers();
		myBike.ride();
		myBike.yellAtDrivers();
		myBike.ride();
		myBike.honk();
		myBike.yellAtDrivers();
		myBike.ride();
		myBike.yellAtDrivers();
		myBike.honk();
		myBike.yellAtDrivers();

		System.out.println(myBike.toString());

		// Create a myCar object
		Car myCar = new Car();

		// Call the honk() method (from the Vehicle class) on the myCar object
		myCar.honk();

		myCar.drive();
		myCar.drive();
		myCar.drive();

		// Display the value of the brand attribute (from the Vehicle class) and the
		// value of the modelName from the Car class
		System.out.println(myCar);

		Jetski watercraft = new Jetski();

		watercraft.goOnWater();

		watercraft.honk();

		// These are a no no:
		// myCar.goOnWater();
		// watercraft.drive();
	}
}