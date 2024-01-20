/*

I CANNOT GET THIS TO WORK.


Sources:
https://stackoverflow.com/questions/9368764/calculate-size-of-object-in-java
https://stackoverflow.com/questions/52353/in-java-what-is-the-best-way-to-determine-the-size-of-an-object

Information on manifest files and jar files: https://introcs.cs.princeton.edu/java/85application/jar/jar.html

I ran this command to make it into a jar file named TapeMeasure.jar:
jar cf TapeMeasure.jar ObjectSizeFetcher.java
jar cmf MANIFEST.MF TapeMeasure.jar ObjectSizeFetcher.java 
jar cmf MANIFEST.MF TapeMeasure ObjectSizeFetcher.java Main.java SparseElement.java SparseMatrix.java
jar cmf MANIFEST.MF TapeMeasure *.class

Invoke with:
java -javaagent:TapeMeasure.jar Main
*/
import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher
{
	private static Instrumentation instrumentation;

	public static void premain(String args, Instrumentation inst)
	{
		instrumentation = inst;
	}

	public static long getObjectSize(Object o)
	{
		return instrumentation.getObjectSize(o);
	}
}