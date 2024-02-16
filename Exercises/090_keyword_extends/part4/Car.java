
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
class Car extends Vehicle {
	private String modelName = "Mustang"; // Car attribute
	private int tireWear = 0;

	public void drive() {
		tireWear++;
	}

	@Override
	public String toString() {
		return this.brand + " " + this.modelName + " with tire wear = " + tireWear;
	}
}