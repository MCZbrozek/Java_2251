/*
A partial solution to this exercise follows:
https://leetcode.com/problems/valid-parentheses/
*/
public class SolutionIntermediate
{

	public static boolean isValid(String s)
	{
		int open_parend_count = 0;
		int open_curly_count = 0;
		int open_square_count = 0;
		//Loop over each character of the String
		for(int i=0; i<s.length(); i++)
		{
			//Add 1 for open parend and subtract 1 for closing parends
			switch (s.charAt(i))
			{
				case '(':
					open_parend_count++;
					break;
				case ')':
					open_parend_count--;
					break;
				case '[':
					open_square_count++;
					break;
				case ']':
					open_square_count--;
					break;
				case '{':
					open_curly_count++;
					break;
				case '}':
					open_curly_count--;
					break;
			}
			//If the count is ever negative then we have a problem
			if(open_parend_count < 0)
				return false;
			if(open_square_count < 0)
				return false;
			if(open_curly_count < 0)
				return false;
		}
		//The string is valid if the count of open parends matches
		//the count of closing parends.
		return open_parend_count==0 && open_square_count==0 && open_curly_count==0;
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