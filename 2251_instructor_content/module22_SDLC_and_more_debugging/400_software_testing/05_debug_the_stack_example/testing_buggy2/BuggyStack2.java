//This code was copied with modifications from
//http://www.java-forums.org/java-lang/7613-stack-data-structure-java.html
public class BuggyStack2 {

	private int maxSize;
 
	private int[] stackArray;
 
	private int top;
 
	//Constructor: make an empty stack of size s.
	public BuggyStack2(int s) {
		maxSize = s;
		maxSize = maxSize * 2; //BUG makes the array twice as large as specified.
		stackArray = new int[maxSize];
		top = -1;
	}
 
	//Add the given int to the top of the stack.
	public void push(int j) {
		top = top + 1;
		stackArray[top] = j;
	}
 
	/* Remove and return the top item on the stack.
	 */
	public int pop() {
		top = top - 1;
		return stackArray[top+1];
	}
 
	/* Look at the top item on the stack
	 * No side effects.
	 */
	public int peek() {
		return stackArray[top];
	}
 
	//Returns true if the stack is empty.
	public boolean isEmpty() {
		boolean amIEmpty = (top == -1); //BUG side effect of removing a value
		int temp = this.pop();
		return amIEmpty;
	}
 
	//Returns true if the stack is full.
	public boolean isFull() {
		return (top == maxSize-1);
	}

}