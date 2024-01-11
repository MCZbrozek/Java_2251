
public class Main
{

	public static void main(String[] args)
	{
		Tree root = new Tree();

		System.out.println("Depth should be 0 == "+
					Integer.toString(root.GetDepth()));

		root.Add(50);

		System.out.println("Depth should be 0 == "+
					Integer.toString(root.GetDepth()));

		System.out.println("Contains 50 should be true == "+
					Boolean.toString(root.Contains(50)));

		root.Add(6);

		System.out.println("Depth should be 1 == "+
				Integer.toString(root.GetDepth()));

		root.Remove(50);

		System.out.println("Depth should be 0 == "+
					Integer.toString(root.GetDepth()));

		System.out.println("Contains 50 should be false == "+
					Boolean.toString(root.Contains(50)));

		root.Add(50);
		root.Add(25);
		root.Add(3);
		root.Add(1);
		root.Add(2);
		root.Add(5);
		root.Add(4);
		root.Add(0);
		root.Add(75);
		root.Add(99);

		System.out.println("Depth should be 3 == "+
					Integer.toString(root.GetDepth()));

		System.out.println("Contains 90 should be false == "+
					Boolean.toString(root.Contains(90)));

		System.out.println("Contains 4 should be true == "+
					Boolean.toString(root.Contains(4)));

		System.out.println("Contains 99 should be true == "+
					Boolean.toString(root.Contains(99)));

		System.out.println("Print the tree. Should be\n"+
					"0, 1, 2, 3, 4, 5, 6, 25, 50, 75, 99, ");

		root.Print();

		root.Remove(3);

		System.out.println("Depth should be between 3 and 5.\n"+
					"Can you make the depth of your code 3? That is ideal.\n"+
					"Depth == "+Integer.toString(root.GetDepth()));

		System.out.println("Contains 3 should be false == "+
					Boolean.toString(root.Contains(3)));

		root.Remove(75);

		System.out.println("Contains 75 should be false == "+
					Boolean.toString(root.Contains(75)));

		System.out.println("Print the tree. Should be\n"+
					"0, 1, 2, 4, 5, 6, 25, 50, 99, ");

		root.Print();

		System.out.println("");
	}

}
