import java.util.Random;

public class Main
{
	public static void main(String[] args)
	{
		
		if(testMaxSize(5))
			System.out.println("FAILED test case: testMaxSize(5)\n");
		else
			System.out.println("PASSED test case: testMaxSize(5)\n");


		if(testMaxSize(1000))
			System.out.println("FAILED test case: testMaxSize(1000)\n");
		else
			System.out.println("PASSED test case: testMaxSize(1000)\n");


		if(testMaxSize())
			System.out.println("FAILED test case: testMaxSize()\n");
		else
			System.out.println("PASSED test case: testMaxSize()\n");
		
		
		if(testIsEmpty(1000))
			System.out.println("FAILED test case: testIsEmpty(1000)");
		else
			System.out.println("PASSED test case: testIsEmpty(1000)");
		
		
	}
	
	/* Test that a size x stack filled with x items is actually full.
	*/
	public static boolean testMaxSize(int x)
	{
		BuggyStack2 test_stack = new BuggyStack2(x);
		
		for(int i=0; i<x; i++)
			test_stack.push(i);
		
		return test_stack.isFull() != true;
	}


	/* Test that a randomly sized stack filled with items is actually full.
	*/
	public static boolean testMaxSize()
	{
		Random rand = new Random();
		int r = rand.nextInt()%10000;
		System.out.println("About to run testMaxSize on int "+r);
		try {
			return testMaxSize(r);
		} catch(Exception e) {
			System.out.println("Exception caught when running test case: testMaxSize("+r+")");
			System.out.println(e);
		}
		return true;
	}


	/* Test that a stack that gets cleared is empty.
	*/
	public static boolean testIsEmpty(int x)
	{
		try {
			BuggyStack2 test_stack = new BuggyStack2(x);
			
			for(int i=0; i<x; i++)
				test_stack.push(i);
			
			for(int i=0; i<x; i++)
				test_stack.pop();
		
			return test_stack.isEmpty() != true;
		}
		catch (Exception e) {
			System.out.println("Exception caught when running test case: testIsEmpty("+x+")");
			//System.out.println(e);
			e.printStackTrace();
		}
		return true;
	}
	
	
}
