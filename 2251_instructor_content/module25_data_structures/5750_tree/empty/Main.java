
public class Main
{
	public static void main(String[] args)
	{
		Tree root = new Tree();

		System.out.println("Depth should be 0 == "+Integer.toString(root.getDepth()));

		root.add(50);

		System.out.println("Depth should be 1 == "+Integer.toString(root.getDepth()));

		System.out.println("contains 50 should be true == "+Boolean.toString(root.contains(50)));

		root.add(6);

		System.out.println("Depth should be 2 == "+Integer.toString(root.getDepth()));

		root.remove(50);

		System.out.println("Depth should be 1 == "+Integer.toString(root.getDepth()));

		System.out.println("contains 50 should be false == "+Boolean.toString(root.contains(50)));

		root.add(50);
		root.add(25);
		root.add(3);
		root.add(1);
		root.add(2);
		root.add(5);
		root.add(4);
		root.add(0);
		root.add(75);
		root.add(99);

		System.out.println("Depth should be 3 == "+Integer.toString(root.getDepth()));

		System.out.println("contains 90 should be false == "+Boolean.toString(root.contains(90)));

		System.out.println("contains 4 should be true == "+Boolean.toString(root.contains(4)));

		System.out.println("contains 99 should be true == "+Boolean.toString(root.contains(99)));

		System.out.println("print the tree. Should be\n0, 1, 2, 3, 4, 5, 6, 25, 50, 75, 99, ");

		root.print();

		root.remove(3);

		System.out.println("Depth should be between 3 and 5.\nCan you make the depth of your code 3? That is ideal.\nDepth == "+Integer.toString(root.getDepth()));

		System.out.println("contains 3 should be false == "+Boolean.toString(root.contains(3)));

		root.remove(75);

		System.out.println("contains 75 should be false == "+Boolean.toString(root.contains(75)));

		System.out.println("print the tree. Should be\n0, 1, 2, 4, 5, 6, 25, 50, 99, ");

		root.print();

		System.out.println("");
	}

}
