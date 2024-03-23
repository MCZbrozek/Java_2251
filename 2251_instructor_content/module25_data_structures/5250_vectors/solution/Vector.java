
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
	 * initial capacity. Unless initial_capacity 
	 * is less than one, in which case, capacity
	 * starts at one.
	 */
	public Vector(int initial_capacity)
	{
		//Prevent sizes less than one because
		//with a size of zero doubling won't
		//work because zero times two is
		//still zero!
		if(initial_capacity < 1)
		{
			initial_capacity = 1;
		}
		this.values = new Object[initial_capacity];
		this.current_size = 0;
	}

	/* Post: Adds the given object to the end of the 
	 * vector, possibly extending the vector in the 
	 * process.
	 */
	public void add(Object obj)
	{
		//If there's plenty of room, put
		//the object in.
		if(current_size < values.length)
		{
			values[current_size] = obj;
			current_size++;
		}
		//Otherwise add more room then
		//recursively add the value.
		else
		{
			this.expand();
			this.add(obj);
		}
	}
	
	/* Pre: values has been instantiated.
	 * Post: Doubles the size of the 
	 * values array.
	 */
	private void expand()
	{
		//Create new array
		Object[] temp = new Object[values.length*2];
		//Copy values from values to temp
		for(int i=0; i<this.current_size; i++)
		{
			temp[i] = this.values[i];
		}
		//Then set values equal to temp.
		this.values = temp;
	}
	
	/* Post: Removes and returns element
	 * equal to the parameter. Size
	 * decreases by 1.
	 * All other values need shifted left.
	 * This method makes an indexOf
	 * method VERY handy, so I added that
	 * in order to make this easier to
	 * write.
	 */
	public Object remove(Object obj)
	{
		int i = this.indexOf(obj);
		if(i==-1)
			return null;
		return this.remove(i);
	}
	
	public int indexOf(Object obj)
	{
		for(int i=0; i<current_size; i++)
		{
			if(obj.equals(values[i]))
			{
				return i;
			}
		}
		return -1;
	}

	/* Pre: 0 <= index && index < this.Size()
	 * Post: The element at index "index" is 
	 * removed and returned. Size decreases 
	 * by 1.
	 */
	public Object remove(int index)
	{
		Object to_return = this.values[index];
		//Shift all other values back
		for( ; index<current_size; index++)
		{
			values[index] = values[index+1];
		}
		current_size--;
		return to_return;
	}

	/* Pre: 0 <= index && index < this.Size()
	 * Post: Returns the value stored at the given index.
	 */
	public Object get(int index)
	{
		return values[index];
	}

	/* Pre: 0 <= index && index < this.Size()
	 * Post: Sets the value at the given index to be obj.
	 */
	public void set(int index, Object obj)
	{
		values[index] = obj;
	}

	/* Post: Returns true if the vector is empty.
	 */
	public boolean isEmpty()
	{
		return current_size == 0;
	}

	/* Post: Returns the current size of the vector.
	 */
	public int size()
	{
		return current_size;
	}
	
	/* Post: Returns the vector as a string of 
	 * comma-separated values.
	 */
	@Override
	public String toString()
	{
		String temp = "";
		for(int i=0; i<current_size)
			temp += values[i].toString()+"\n";
		return temp;
	}
	
	/* Post: Prints the vector.
	 */
	public void print()
	{
		System.out.println(this.toString());
	}

}
