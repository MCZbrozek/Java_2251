public class Node<T> //A link in the chain that is a singly-linked list
{
	private T data;
	private Node next; //Self referential class

	public Node(T data)
	{
		this.data = data;
		this.next = null;
	}
	
	public T getData() { return data; }
	public Node<T> getNext() { return next; }
	public void setNext(Node<T> n) { next = n; }
	public boolean hasNext() { return next != null; }
}