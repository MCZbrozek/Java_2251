/*
Name: Michael Zbrozek
Date: 2/13/2024
Purpose: modify dog class, create dogs array, iterate over array based on user prompts
Sources:GeeksforGeeks 	

Files: 
Main.Java
Dog.java

*/

/*
This code takes inspiration from here:
https://www.tutorialspoint.com/java/java_object_classes.htm
*/
public class Dog {
	// Instance variables
	private String breed;
	private String name;
	private int age;
	private String color;
	private double hungry = 0.9; // varies from 0 to 1. with 1 being very hungry
	private double sleepy = 0.1; // varies from 0 to 1. with 1 being very sleepy

	// Default Constructor
	public Dog() {
		breed = "unknown";
		age = 1;
		color = "generic";
	}

	// Constructor
	public Dog(String breed_choice, String name_choice, int age_choice, String color_choice) {
		breed = breed_choice;
		name = name_choice;
		age = age_choice;
		color = color_choice;
	}

	@Override
	public String toString() {
		return "Breed: " + breed + "\nName: " + name + "\nAge: " + age + "\nColor: " + color;
	}

	public void bark(String on_the_street) {
		if (on_the_street.equals("mailman"))
			System.out.println("Woof woof");
	}

	public void eat() {
		if (hungry >= 0.5) {
			System.out.println("snarf gobble");
			hungry = 0.0;
		} else {
			System.out.println("refuses to eat");
		}
	}

	// Getter
	public double getHungry() {
		return 0.5; // Placeholder: I know this is wrong, but I'm stuck.
	}

	public String getBreed() {
		return breed;
	}

	public void setBreed(String breed) {
		this.breed = breed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(String age) {
		int ageInt = Integer.parseInt(age);
		this.age = ageInt;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setHungry(double hungry) {
		this.hungry = hungry;
	}

	public double getSleepy() {
		return sleepy;
	}

	public void setSleepy(double sleepy) {
		this.sleepy = sleepy;
	}

}
