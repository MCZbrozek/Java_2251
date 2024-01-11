public class Main
{
	public static void main(String[] args) //starting point
	{
		// create objects led and halogen
		Lamp led = new Lamp();
		//TODO: Create a new Lamp and put it in a
		//variable named halogen.


		// turn on the light by
		// calling method turnOn()
		led.turnOn();
		
		// turn off the light by
		// calling method turnOff()
		//TODO: Call the turnOff method that belongs 
		//to the halogen variable, not the led


		System.out.println("The LED lamp is on is a "+led.lampIsOn()+" statement.");

		//TODO: Uncomment the following. 
		//What is printed out? Answer in a comment.
		
		//System.out.println("The halogen lamp is on is a "+halogen.lampIsOn()+" statement.");
	}
}