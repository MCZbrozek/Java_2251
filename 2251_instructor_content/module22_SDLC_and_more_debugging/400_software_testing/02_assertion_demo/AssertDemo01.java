/* Source: infoworld.com/article/3543239/how-to-use-assertions-in-java.html

Compile with
> javac AssertDemo01.java
Run with assertions enabled
> java -ea AssertDemo01

*/
public class AssertDemo01
{
	public static void main(String[] args)
	{
		int x = -1;
		
		assert x >= 0;
		//assert x >= 0 : "x < 0"; //More informative
		
		System.out.println("made it here");
	}
}