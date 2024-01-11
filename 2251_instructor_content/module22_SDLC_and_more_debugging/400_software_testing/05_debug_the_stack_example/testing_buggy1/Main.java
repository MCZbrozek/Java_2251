
public class Main
{
	public static final boolean DEBUG = true;

	public static void main(String[] args)
	{
		//Exercise the stack.
		
		//First we will test the constructor.
		BuggyStack1 theStack = null;
		try {
			theStack = new BuggyStack1(5); // make new stack of size 5
		}
		catch(Exception e) {
			System.out.println("Caught exception when creating size 5 stack");
			System.out.println(e);
		}
		
		//Check that the stack is empty.
		assert theStack.isEmpty() : "A new stack should be empty";
	
		theStack.push(20);
		theStack.push(40);
		theStack.push(60);
		theStack.push(80);
		
		//check that the stack is not yet full
		assert !theStack.isFull() : "A size 5 stack should not yet be full";

		theStack.push(100);
		
		//check that the stack is now full
		assert theStack.isFull() : "A size 5 stack should now be full";

		int counter = 0;

		while (!theStack.isEmpty()) {
			int value = theStack.pop();
		    System.out.print(value);
		    System.out.print(" ");

			if(DEBUG)
			{
				if(counter > 4)
				{
					System.out.println("ERROR: stack should be empty. Terminating while loop.");
					break;
				}
				counter++;
			}
		}
		System.out.println("");

		//check that the stack is now full
		assert theStack.isEmpty() : "The stack should now be empty";

	}

}
