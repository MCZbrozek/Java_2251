/*
Name: Michael Zbrozek
Date: 2/13/2024
Purpose: Rewrite the code in Circle.java and Main.java so that the Circle has a radius instance variable and 5.7 only needs to be used once in the entire program to set the value of the radius.
Sources: BrightSpace instruction video

Files: 
Main.Java
Circle.java

*/

import java.lang.Math;

class Circle {
	private double radius;

	public Circle(double radius) {
		this.radius = radius;
	}

	public double getCircumference() {
		return 2 * Math.PI * radius;
	}

	public double getArea() {
		return Math.PI * radius * radius;
	}

	@Override
	public String toString() {
		return "circle with radius " + radius;
	}
}