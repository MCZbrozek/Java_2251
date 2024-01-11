/*
This code was copied with modifications from
www.java-forums.org/java-lang/7613-stack-data-structure-java.html
*/
public class Stack
{

	private int maxSize;
 
	private int[] stackArray;
 
	private int top;
 
	//Constructor: make an empty stack of size s.
	public Stack(int s) throws IllegalArgumentException
	{
		if(s <= 0)
			throw new IllegalArgumentException("Cannot instantiate a stack with zero or negative size '"+s+"'");
		maxSize = s;
		stackArray = new int[maxSize];
		top = -1;
	}
 
	//Add the given int to the top of the stack.
	public void push(int j)
	{
		try {
			top = top + 1;
			stackArray[top] = j;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Cannot push on the stack. Stack is full = "+this.isFull());
			System.out.println(e);
			e.printStackTrace();
			System.exit(1);
		}
	}
 
	/* Remove and return the top item on the stack.
	 */
	public int pop() throws Exception
	{
		if(top < 0)
			throw new Exception("Cannot pop from empty Stack");
		top = top - 1;
		return stackArray[top+1];
	}
 
	/* Look at the top item on the stack
	 * No side effects.
	 */
	public int peek()
	{
		int to_return = -1;
		try {
			to_return = stackArray[top];
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Cannot peek the stack. Stack is empty = "+this.isEmpty());
			System.out.println(e);
			e.printStackTrace();
			System.exit(1);
		}
		return to_return;
	}
 
	//Returns true if the stack is empty.
	public boolean isEmpty()
	{
		return (top == -1);
	}
 
	//Returns true if the stack is full.
	public boolean isFull()
	{
		return (top == maxSize-1);
	}

	//Remove all values from the stack
	public void clear()
	{
		top = -1;
		assert this.isEmpty() : "The stack should be empty after a call to clear.";
	}

	//Double the size of the stack
	public void doubleSize()
	{
		maxSize = maxSize*2;
		assert !this.isFull() : "The stack should not be full after a call to doubleSize.";
	}
}