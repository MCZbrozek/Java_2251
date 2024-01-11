
public class Vector
{
	private int current_size;
	private Object[] values;

	/* Post: Constructs a vector with initial 
	 * capacity of 10.
	 */
	public Vector()
	{
		this.values = new Object[10];
		this.current_size = 0;
	}

	/* Post: Constructs a vector with the given 
	 * initial capacity.
	 */
	public Vector(int initial_capacity)
	{
		this.values = new Object[initial_capacity];
		this.current_size = 0;
	}

	/* Post: Adds the given object to the end of the 
	 * vector, possibly extending the vector in the 
	 * process.
	 */
	public void add(Object obj)
	{
		//TODO
	}
	
	/* Post: Removes and returns element equal to 
	 * the parameter. Size decreases by 1.
	 */
	public Object remove(Object obj)
	{
		//TODO
		return null;
	}

	/* Pre: 0 <= index && index < this.Size()
	 * Post: The element at index "index" is removed
	 * and returned. Size decreases by 1.
	 */
	public Object remove(int index)
	{
		//TODO
		return null;
	}

	/* Pre: 0 <= index && index < this.Size()
	 * Post: Returns the value stored at the given index.
	 */
	public Object get(int index)
	{
		//TODO
		return null;
	}

	/* Pre: 0 <= index && index < this.Size()
	 * Post: Sets the value at the given index to be obj.
	 */
	public void set(int index, Object obj)
	{
		//TODO
	}

	/* Post: Returns true if the vector is empty.
	 */
	public boolean isEmpty()
	{
		//TODO
		return true;
	}

	/* Post: Returns the current size of the vector.
	 */
	public int size()
	{
		//TODO
		return -1;
	}
	
	/* Post: Returns the vector as a string of 
	 * comma-separated values.
	 */
	public String toString()
	{
		//TODO
		return "";
	}
	
	/* Post: Prints the vector.
	 */
	public void print()
	{
		System.out.println(this.toString());
	}

}
