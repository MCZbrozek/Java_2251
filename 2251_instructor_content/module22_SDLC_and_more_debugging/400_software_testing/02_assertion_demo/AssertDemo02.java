/* Source: infoworld.com/article/3543239/how-to-use-assertions-in-java.html

Compile with
> javac AssertDemo02.java

Run with assertions enabled no error
> java -ea AssertDemo02 java.png

Run with assertions enabled with error
> java -ea AssertDemo02

*/
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

class PNG
{
	/**
	 *  Create a PNG instance, read specified PNG file, and decode
	 *  it into suitable structures.
	 *
	 *  @param filespec path and name of PNG file to read
	 *
	 *  @throws NullPointerException when <code>filespec</code> is
	 *			 <code>null</code>
	 */
	PNG(String filespec) throws IOException
	{
		// Enforce preconditions in non-private constructors and
		// methods.
		if (filespec == null)
			throw new NullPointerException("filespec is null");
		try (FileInputStream fis = new FileInputStream(filespec))
		{
			readHeader(fis);
		}
	}

	private void readHeader(InputStream is) throws IOException
	{
		// Confirm that precondition is satisfied in private
		// helper methods.
		assert is != null : "null passed to input stream";
	}
}



public class AssertDemo02
{
	public static void main(String[] args) throws IOException
	{
		PNG png = new PNG((args.length == 0) ? null : args[0]);
	}
}