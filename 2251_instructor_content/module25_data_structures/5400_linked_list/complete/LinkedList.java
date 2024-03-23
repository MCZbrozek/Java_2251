//Manager of Nodes aka links
public class LinkedList<T>
{
	private Node<T> start; //also called head

	//Constructor
	public LinkedList(){ start = null; }
	
	public void append(T data)
	{
		if(start == null)
			start = new Node<T>(data);
		else{
			Node<T> last = this.getLast();
			last.setNext(new Node<T>(data));
		}
	}
	
	private Node<T> getLast()
	{
		Node<T> last = start;
		while(last.hasNext())
		{
			last = last.getNext();
		}
		return last;
	}
	
	public int getLength()
	{
		if(start == null)
			return 0;
		
		Node<T> current = start;
		int count = 1;
		while(current.hasNext()){
			current = current.getNext();
			count++;
		}
		return count;
	}

	public Node<T> getNodeAt(int index)
	{
		Node<T> current = start;
		for(int i=0; i<index; i++)
		{
			current = current.getNext();
		}
		return current;
	}
	
	public T getValueAt(int index)
	{
		return this.getNodeAt(index).getData();
	}

	public void insertAfter(T data, int index)
	{
		Node<T> at_index = this.getNodeAt(index);
		//Create the new link to be inserted
		Node<T> new_node = new Node<T>(data);
		//Attach the new_node between existing nodes.
		//Order is important here.
		new_node.setNext( at_index.getNext() );
		at_index.setNext(new_node);
	}
	
	//Exercise:
	public T removeAt(int index)
	{
		//Should fix incase index is zero!
		
		//Get the node BEFORE the node with the given index.
		Node<T> before = this.getNodeAt(index-1);
		//Get the node after
		Node<T> after = before.getNext().getNext();
		//Get the data of the node to be removed
		T data = before.getNext().getData();
		//Attach before to after, bypassing the node inbetween
		before.setNext(after);
		return data;
	}
}
