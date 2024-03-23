public class Tree // A sorted binary tree of integers
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
		this.parent = null;
		this.leftChild = null;
		this.rightChild = null;
		this.hasValue = false;
	}

	private void clearSelf()
	{
		this.parent = null;
		this.leftChild = null;
		this.rightChild = null;
		this.hasValue = false;
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
		//System.out.println("Adding value: "+value); //TESTING
		//If the current node has no value, make the current value equal value and set has_value to true.
		if(!hasValue)
		{
			//System.out.println("Node has no value. Terminal"); //TESTING
			this.value = value;
			this.hasValue = true;
		}
		//Otherwise, if value is less than the current node's value, add the value to the left child.
		else if(value < this.value)
		{
			//System.out.println(value+" is less than "+this.value); //TESTING

			//If the child does not exist (child == null), create it.
			if(leftChild == null)
			{
				//System.out.println("Left child null so creating."); //TESTING
				leftChild = new Tree();
				leftChild.parent = this;
			}
			leftChild.add(value);
		}
		//If the value is greater than or equal to the current node's value, add the value to the right child.
		else
		{
			//System.out.println(value+" >= "+this.value); //TESTING
			//If the child does not exist (child == null), create it.
			if(rightChild == null)
			{
				//System.out.println("Right child null so creating."); //TESTING
				rightChild = new Tree();
				rightChild.parent = this;
			}
			rightChild.add(value);
		}
	}

	/* Pre: The tree contains value.
	 * Post: Removes the node containing value from
	 * the tree while preserving the rest of the
	 * tree. Returns the root of the tree, which
	 * may be different from before.
	 */
	public Tree remove(int value)
	{
		//Does this have the value, remove this tree element
		if(hasValue && this.value == value)
		{
			//The process described here only
			//works when this Tree is the left
			//child of its parent. You must
			//do things differently if it's
			//the right child:
			// Parent sets its left child to be
			// this's left child.
			// Add all of the values from the
			// right child branch to the root
			// of this tree.
			
			Tree toReturn = null;
			if(parent != null)
			{
				if(this == parent.leftChild)
				{
					//System.out.println("Parent has value: "+parent.value); //TESTING
					// Parent sets its left child to be
					// this's left child.
					parent.leftChild = this.leftChild;
					if(this.leftChild!=null)
					{
						this.leftChild.parent = parent;
					}
					// Add all of the values from the
					// right child branch to the root
					// of this tree.
					parent.addTree(this.rightChild);
					toReturn = parent.getRoot();
					//System.out.println("Root has value: "+toReturn.value); //TESTING
				}
				else //this is the rightChild of parent
				{
					//System.out.println("Parent has value: "+parent.value); //TESTING
					// Parent sets its right child to be
					// this's right child.
					parent.rightChild = this.rightChild;
					if(this.rightChild!=null)
					{
						this.rightChild.parent = parent;
					}
					// Add all of the values from the
					// leftt child branch to the root
					// of this tree.
					parent.addTree(this.leftChild);
					toReturn = parent.getRoot();
					//System.out.println("Root has value: "+toReturn.value); //TESTING
				}
			}
			//In the following conditions, the parent reference is null
			else if(leftChild != null)
			{
				leftChild.parent = null;
				leftChild.addTree(rightChild);
				//Return the new parent
				toReturn = leftChild;
			}
			else if(rightChild != null)
			{
				rightChild.parent = null;
				rightChild.addTree(leftChild);
				//Return the new parent
				toReturn = rightChild;
			}
			clearSelf();
			return toReturn;
		} //if(hasValue && this.value == value)

		//Otherwise, if value is less than the current node's value, check the tree to the left.
		else if(value < this.value && leftChild != null)
		{
			return leftChild.remove(value);
		}
		//If the value is greater than or equal to the current node's value, check the tree to the right.
		else if(rightChild != null)
		{
			return rightChild.remove(value);
		}
		return getRoot();
	}
	
	
	/* Pre: ???
	* Post: Adds all the values from the 
	* given Tree to the root of this Tree. */
	private void addTree(Tree t)
	{
		if(t==null)
		{
			return;
		}
		//Get the root of the tree
		Tree root = getRoot();
		//Iterate through the Tree t
		//Adding each value to root as we go
		if(t.hasValue)
		{
			root.add(t.value);
		}
		//Add in the left branches
		this.addTree(t.leftChild);
		//Add in the right branches
		this.addTree(t.rightChild);
	}
	
	
	private Tree getRoot()
	{
		// Base case
		if(this.parent == null)
		{
			return this;
		}
		else // Recursive case
		{
			return this.parent.getRoot();
		}
	}
	

	/* Post: Returns true if a sub-tree of the current tree contains value.
	 */
	public boolean contains(int value)
	{
		//If the current node has no value, return false.
		if(!hasValue)
		{
			return false;
		}
		//Does this have the value
		else if(this.value == value)
		{
			return true;
		}
		//Otherwise, if value is less than the current node's value, check the tree to the left.
		else if(value < this.value)
		{
			if(leftChild == null)
				return false;
			return leftChild.contains(value);
		}
		//If the value is greater than or equal to the current node's value, check the tree to the right.
		else
		{
			if(rightChild == null)
				return false;
			return rightChild.contains(value);
		}
	}

	/* Post: Returns the values in the tree separated by commas in order.
	 */
	public String toString()
	{
		//Get the left subtree String
		//Get this Tree's value
		//Get the right subtree String

		//Get the left subtree String
		String toReturn = "";
		if(leftChild != null)
		{
			toReturn += leftChild.toString();
		}
		//Get this Tree's value
		toReturn += value+", ";
		//Get the right subtree String
		if(rightChild != null)
		{
			toReturn += rightChild.toString();
		}
		return toReturn;
	}


	//Try to display the tree in a tree-like manner
	public void printAsTree()
	{
		String[] strArr = toStringAsTree();
		for(String s : strArr)
			System.out.println(s);
	}
	// My attempt to get the tree in
	// a hierarchical representation.
	public String[] toStringAsTree()
	{
		// Get the depth of the tree
		int depth = getDepth();
		// Create a String array of length depth
		String[] toReturn = new String[depth];
		//Replace nulls with empty strings
		for(int i=0; i<depth; i++)
			toReturn[i] = "";
		//Enter this tree's value with some tabs
		toReturn[0] = strRepeat("\t",depth/2+1) + value;
		//Get the left subtree String[]
		if(leftChild != null)
		{
			String[] leftString = leftChild.toStringAsTree();
			for(int i=0; i<leftString.length; i++)
			{
				toReturn[i+1] += leftString[i];
			}
		}
		//Get the right subtree String[]
		if(rightChild != null)
		{
			String[] rightString = rightChild.toStringAsTree();
			for(int i=0; i<rightString.length; i++)
			{
				toReturn[i+1] += rightString[i];
			}
		}
		return toReturn;
	}
	private String strRepeat(String s, int count)
	{
		String toReturn = "";
		for(int i=0;i<count;i++)
			toReturn += s;
		return toReturn;
	}


	/* Post: Returns the depth of the largest branch of the tree.
	 */
	public int getDepth()
	{
		if(!hasValue)
		{
			return 0;
		}
		else // otherwise depth is at least 1
		{
			int leftDepth = 0;
			int rightDepth = 0;
			if(leftChild != null)
				leftDepth = leftChild.getDepth();
			if(rightChild != null)
				rightDepth = rightChild.getDepth();
			if(leftDepth < rightDepth)
			{
				return rightDepth + 1;
			}
			else
			{
				return leftDepth + 1;
			}
		}
	}

}
