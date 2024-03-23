//Linked List Exercise
public class Main
{
	public static void main(String[] args)
	{
		LinkedList<String> todos = new LinkedList<String>();
		todos.append("Eat breakfast");
		todos.append("Feed the cat");
		todos.append("Get dressed");
		todos.append("Brush teeth");
		todos.append("Drink coffee");
		todos.append("Shower");
		System.out.println(todos.getLength());
		
		System.out.println(todos.getValueAt(0));
		System.out.println(todos.getValueAt(4));
		//System.out.println(todos.getValueAt(6));
		
		todos.insertAfter("Feed the dog", 1);

		System.out.println();
		
		//Print out the linked list
		int length = todos.getLength();
		for(int i=0; i<length; i++)
		{
			System.out.println(todos.getValueAt(i));
		}
		
		String item = todos.removeAt(5);
		System.out.println("\nRemoved '"+item+"' from the list\n");

		//Print out the linked list
		length = todos.getLength();
		for(int i=0; i<length; i++)
		{
			System.out.println(todos.getValueAt(i));
		}
	}
}
