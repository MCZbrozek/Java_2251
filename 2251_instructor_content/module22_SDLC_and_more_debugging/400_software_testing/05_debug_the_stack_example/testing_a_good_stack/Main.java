
public class Main
{
	public static final boolean DEBUG = true;

	public static void main(String[] args)
	{
		//Exercise the stack.
		
		//First we will test the constructor.
		Stack theStack = null;
		try {
			theStack = new Stack(5); // make new stack of size 5
		}
		catch(Exception e) {
			System.out.println("Caught exception when creating size 5 stack");
			System.out.println(e);
		}
		
		//Check that the stack is empty.
		assert theStack.isEmpty() : "A new stack should be empty";
		
		//For the following 3 constructors, does it cause an error?
		//Should it cause an error?
		
		//What if we create a stack of size 0?
		try {
			Stack theStack1 = new Stack(0);
		}
		catch(Exception e) {
			System.out.println("Caught exception when creating size 0 stack");
			System.out.println(e);
			System.out.println();
		}

		//What if we create a stack of size -1?
		try {
			Stack theStack2 = new Stack(-1);
		}
		catch(Exception e) {
			System.out.println("Caught exception when creating size -1 stack");
			System.out.println(e);
			System.out.println();
		}

		//What if we create a very large stack?
		try {
			Stack theStack3 = new Stack(99999999);
		}
		catch(Exception e) {
			System.out.println("Caught exception when creating size 99999999 stack");
			System.out.println(e);
			System.out.println();
		}

		//What happens if we pop here?
		try {
			theStack.pop();
		}catch(Exception e) {
			System.out.println(e+"\n");
		}
		
		theStack.push(20);
		theStack.push(40);
		theStack.push(60);
		theStack.push(80);
		
		//check that the stack is not yet full
		assert !theStack.isFull() : "A size 5 stack should not yet be full";

		theStack.push(100);
		
		//check that the stack is now full
		assert theStack.isFull() : "A size 5 stack should now be full";

		//Repeatedly pop until the stack is empty.
		int counter = 0;
		int value = -1;
		while (!theStack.isEmpty()) {
			try {
				value = theStack.pop();
			}catch(Exception e) {
				System.out.println(e);
			}
			
		    System.out.print(value);
		    System.out.print("  ");
			
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


		theStack.doubleSize();


		theStack.push(20);
		theStack.push(40);
		theStack.push(60);
		theStack.push(80);


		theStack.clear();
	}

}
