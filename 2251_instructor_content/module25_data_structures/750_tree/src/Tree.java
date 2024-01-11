public class Tree
{
	Tree parent;
	Tree left_child;
	Tree right_child;
	boolean has_value;
	int value;

	/* Constructor
	 * Post: creates an empty tree.
	 */
	Tree()
	{
		this.parent = null;
		this.left_child = null;
		this.right_child = null;
		this.has_value = false;
	}

	/* Post: If the current node has no value, make the current value 
	 * equal value and set has_value to true.
	 * Otherwise, if value is less than the current node's value, 
	 * add the value to the left child.
	 * If the value is greater than the current node's value, add the 
	 * value to the right child.
	 * If the child does not exist (child == null), create it. 
	 */
	public void Add(int value)
	{
		//TODO
	}

	/* Pre: The tree contains value.
	 * Post: Removes the node containing value from the tree 
	 * while preserving the rest of the tree.
	 */
	public void Remove(int value)
	{
		//TODO
	}

	/* Post: Returns true if a sub-tree of the current tree contains value.
	 */
	public boolean Contains(int value)
	{
		//TODO
		return false;
	}

	/* Post: Prints the values in the tree separated by commas in order.
	 */
	public void Print()
	{
		//TODO
		System.out.println("\n");
	}

	/* Post: Returns the depth of the largest branch of the tree.
	 */
	public int GetDepth()
	{
		//TODO
		return 0;
	}

}
