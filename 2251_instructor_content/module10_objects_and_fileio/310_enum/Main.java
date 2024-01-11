/* Compare this class to MainWithoutEnum.java
Then discuss MainWithCellstate.java and its
connection with the TicTacToe lab. */
public class Main
{
	enum Level
	{
		LOW,
		MEDIUM,
		HIGH,
		EXTREME
	}

	public static void main(String[] args)
	{
		Level myVar = Level.MEDIUM;
		System.out.println(myVar);
		
		if(myVar == Level.MEDIUM)
		{
			System.out.println("Changing myVar");
			myVar = Level.HIGH;
		}
		System.out.println(myVar);
	}
}
