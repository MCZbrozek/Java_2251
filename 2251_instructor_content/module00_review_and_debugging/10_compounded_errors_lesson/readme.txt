
A frustrated student sent me a message with the associated code. I now use it as an example of the importance of
	-frequent testing
	-immediately fixing any errors that are identified
	-the fact that an increase in the number of errors is not necessarily an indication of regression away from a solution
	-the fact that a reduction in the count of errors is not necessarily an indication of progress toward a solution
	


HERE IS THE MESSAGE I SENT TO THE STUDENT:

Local variables (variables declared inside methods) are neither public nor private (they're local). So delete "private" off the front of these:
private int[][] matrixA;
private int[][] matrixB;
private int[][] matrixC;

This should be in ThreadOperation, not in ThreadOperationTest, because this is the method that must be overridden in order to extend thread:
@Override
public void run()
{
}

You're missing import statements for File and Scanner, and you will also need a try catch. This question may provide some guidance:
https://stackoverflow.com/questions/13185727/reading-a-txt-file-using-scanner-class-in-java

After that, use nextInt to read in the first two integers from the file.

Do your best to eliminate errors first thing before moving onward. It can be tough when multiple errors stack up as they have here.

This will get you started.