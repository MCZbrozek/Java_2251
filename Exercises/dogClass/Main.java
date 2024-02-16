
/*
Name: Michael Zbrozek
Date: 2/13/2024
Purpose: modify dog class, create dogs array, iterate over array based on user prompts
Sources:GeeksforGeeks 	

Files: 
Main.Java
TicTacToe.java

*/
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Dog fido = new Dog();
		Dog ashaya = new Dog("pitbull", "ashaya", 9, "brindle");

		Scanner scanner = new Scanner(System.in);

		Dog[] dogs = new Dog[5]; // New array of type Dog, length 3
		dogs[0] = fido;
		dogs[1] = ashaya; // add existing dogs to array

		// Ask user to provide 3 additional dogs to array
		for (int i = 2; i < dogs.length; i++) {
			System.out.println("Give me a dog Name, Age, and Breed separated by spaces:");
			String userInput = scanner.nextLine();
			String[] userDog = userInput.split(" ");
			Dog newDog = new Dog();
			newDog.setName(userDog[0]);
			newDog.setAge(userDog[1]);
			newDog.setBreed(userDog[2]);
			dogs[i] = newDog;
		}

		for (Dog dog : dogs) {
			System.out.println();
			System.out.println(dog); // toString is called automatically
		}
		boolean quit = false;
		int breedCounter = 0;
		while (!quit) {
			System.out.println("Which breed would you like to count?: (or 'quit' to exit) ");
			String userBreed = scanner.nextLine().toLowerCase();
			if (userBreed.equals("quit")) {
				quit = true;
				System.out.println("Bye!");
			} else {
				for (Dog dog : dogs) {
					if (dog.getBreed().equalsIgnoreCase(userBreed)) {
						breedCounter++;
					}
				}
				System.out.println("There are " + breedCounter
						+ " matches to the breed " + userBreed);
			}

		}

		// fido.bark("roadrunner");
		// ashaya.bark("mailman");

		// double x = ashaya.getHungry();
		// System.out.println(x);
		// ashaya.eat();
		// System.out.println(ashaya.getHungry());
		// if (ashaya.getHungry() > 0.1) {
		// // System.out.println("Did you really eat?");
		// }
	}
}