public class Tree // A sorted tree of integers
{
	private Tree parent; //root
	private Tree leftChild; //left branch
	private Tree rightChild; //right branch
	private boolean hasValue;
	private int value;

	/* Constructor
	 * Post: creates an empty tree.
	 */
	public Tree()
	{
		//TODO
	}

	/* Post: If the current node has no value, make the current value 
	 * equal value and set has_value to true.
	 * Otherwise, if value is less than the current node's value, 
	 * add the value to the left child.
	 * If the value is greater than the current node's value, add the 
	 * value to the right child.
	 * If the child does not exist (child == null), create it.
	 */
	public void add(int value)
	{
		//TODO
	}

	/* Pre: The tree contains value.
	 * Post: Removes the node containing value from the tree 
	 * while preserving the rest of the tree.
	 */
	public void remove(int value)
	{
		//TODO
	}

	/* Post: Returns true if a sub-tree of the current tree contains value.
	 */
	public boolean contains(int value)
	{
		//TODO
		return true;
	}

	/* Post: Prints the values in the tree separated by commas in order.
	 */
	public void print()
	{
		//TODO
		System.out.println("\n");
	}

	/* Post: Returns the depth of the largest branch of the tree.
	 */
	public int getDepth()
	{
		//TODO
		return 0;
	}

}
