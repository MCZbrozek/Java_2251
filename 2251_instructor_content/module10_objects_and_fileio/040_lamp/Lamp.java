public class Lamp //Class name starts with an upper case letter and MUST match the file name.
{
	// stores the value for light
	// true if light is on
	// false if light is off
	private boolean is_on; //attributes and variables all lowercase and separate words with underscores

	// method to turn on the light
	public void turnOn()
	{ //method names start lowercase and all other words start uppercase.
		is_on = true;
		System.out.println("Light on? " + is_on);
	}

	// method to turnoff the light
	public void turnOff()
	{
		is_on = false;
		System.out.println("Light on? " + is_on);
	}
	
	public boolean lampIsOn()
	{
		return is_on;
	}
}
