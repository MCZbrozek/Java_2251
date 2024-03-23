
public class LinkedListElement
{
	
	private Object value = null;
	private LinkedListElement next = null;
	private LinkedListElement previous = null;
	
	/* Add sprites or words from sentences to the list.
	 * Remove duplicate words.
	 * Sort the words.
	 */

	/* Constructor
	 * Post: 
	 */
	public LinkedListElement()
	{
		
	}

	public LinkedListElement(Object v)
	{
		this.value = v;
	}

	/* Pre: 
	 * Post: 
	 */
	public Object getValue()
	{
		return value;
	}

	/* Pre: 
	 * Post: 
	 */
	public void setNext(LinkedListElement next)
	{
		this.next = next;
		next.previous = this;
	}
	
	/* Pre: 
	 * Post: 
	 */
	public void setPrevious(LinkedListElement previous)
	{
		this.previous = previous;
		previous.next = this;
	}
	
	/* Pre: 
	 * Post: 
	 */
	public LinkedListElement getNext()
	{
		return next;
	}
	
	/* Pre: 
	 * Post: 
	 */
	public LinkedListElement getPrevious()
	{
		return previous;
	}
	
	/* Pre: 
	 * Post: 
	 */
	public LinkedListElement getFirst()
	{
		if(previous == null) {
			return this;
		}
		else{
			return previous.getFirst();
		}
	}
	
	/* Pre: 
	 * Post: 
	 */
	public void getLast()
	{
		if(next == null) {
			return this;
		}
		else{
			return next.getLast();
		}

	}
	
	/* Pre: The list has at least one element.
	 * Post: Removes this linked list element from the
	 * list and returns the removed element. 
	 */
	public LinkedListElement remove()
	{
		previous.setNext(next);
		return this;
	}
	
	/* Post: Adds a linked list element with value
	 * equal to the given value to the front of this
	 * linked list.
	 */
	public void addFirst(Object value)
	{
		if(previous == null) {
			LinkedListElement temp = LinkedListElement(value);
			this.setPrevious(temp);
		}
		else{
			previous.addFirst(value);
		}
	}
	
	/* Post: Adds a linked list element with value
	 * equal to the given value to the end of this
	 * linked list.
	 */
	public void addLast(Object value)
	{
		
	}
	
	/* Post: Returns true if this linked list
	 * contains the given value.
	 */
	public boolean contains(Object value)
	{
		LinkedListElement current = this.getFirst();
		
		if(current.getValue() == value)
			return true;
		
		while(current.hasNext())
		{
			current = current.getNext();
			if(current.getValue() == value)
				return true;
		}
		
		return false;
	}

	/* Pre: This linked list contains the given value.
	 * Post: Returns the LinkedList element whose value
	 * matches the given value.
	 */
	public LinkedListElement getByValue(Object value)
	{
		return null;
	}

}