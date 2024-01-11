/*
A partial solution to this exercise follows:
https://leetcode.com/problems/valid-parentheses/
*/
public class SolutionBasic
{

	public static boolean isValid(String s)
	{
		int open_parend_count = 0;
		//Loop over each character of the String
		for(int i=0; i<s.length(); i++)
		{
			//Add 1 for open parend and subtract 1 for closing parends
			if( s.charAt(i) == '(' )
			{
				open_parend_count++;
			}
			else if( s.charAt(i) == ')' )
			{
				open_parend_count--;
			}
			//If the count is ever negative then we have a problem
			if(open_parend_count < 0)
				return false;
		}
		//The string is valid if the count of open parends matches
		//the count of closing parends.
		return open_parend_count==0;
	}
	
	public static void main(String[] args)
	{
		String test = "()";
		System.out.println("Should be true: "+isValid(test));
		
		test = "()[]{}";
		System.out.println("Should be true: "+isValid(test));
		
		test = "(]";
		System.out.println("Should be false: "+isValid(test));
		
		test = "([)]";
		System.out.println("Should be false: "+isValid(test));
		
		test = "{[]}";
		System.out.println("Should be true: "+isValid(test));

		test = ")(";
		System.out.println("Should be false: "+isValid(test));

		test = "((()))()(())";
		System.out.println("Should be true: "+isValid(test));

		test = "(()))(";
		System.out.println("Should be false: "+isValid(test));
	}
}