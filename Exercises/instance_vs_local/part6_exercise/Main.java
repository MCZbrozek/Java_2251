/*
Name: Michael Zbrozek
Date: 2/13/2024
Purpose: Rewrite the code in Circle.java and Main.java so that the Circle has a radius instance variable and 5.7 only needs to be used once in the entire program to set the value of the radius.
Sources:BrightSpace instruction video 	

Files: 
Main.Java
Circle.java

*/
class Main {
	public static void main(String[] args) {
		Circle disk = new Circle(5.7);

		System.out.println("The " +
				disk.toString() + " has a circumference of " +
				disk.getCircumference() +
				" and an area of " + disk.getArea());

	}
}