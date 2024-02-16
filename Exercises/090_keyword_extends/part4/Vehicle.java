
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
class Vehicle {
	protected String brand = "Ford"; // Vehicle attribute

	public void honk() // Vehicle method
	{
		if (this instanceof Car) {
			System.out.println("Beep, Beep!");
		} else if (this instanceof Bike) {
			System.out.println("Ring, Ring");
		} else {
			System.out.println("oink, oink");
		}
	}
}