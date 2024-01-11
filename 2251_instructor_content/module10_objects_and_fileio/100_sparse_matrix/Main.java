public class Main
{
	public static void main(String[] args) //starting point
	{
		int[][] matrix = {{4,0,0,0},
						  {0,0,0,0},
						  {0,1,0,0}};
		
		int rows = 3;
		int columns = 4;
		SparseMatrix sparse = new SparseMatrix(rows, columns, 0);
		System.out.println(sparse);
		
		int[][] m = sparse.getMatrix();
		System.out.println("Before:");
		print2dArray(m);
		
		sparse.addValue(4, 0, 0);
		sparse.addValue(1, 2, 1);
		
		m = sparse.getMatrix();
		System.out.println("After:");
		print2dArray(m);
		
		//System.out.println(matrix[1][1]);
		//System.out.println();
		//print2dArray(matrix);
		
		
		SparseMatrix sparse_new = new SparseMatrix(matrix);
		System.out.println("\nTesting 2nd constructor:");
		m = sparse_new.getMatrix();
		print2dArray(m);
		
	} //public static void main(String[] args)
	
	
	public static void print2dArray(int[][] m)
	{
		for(int row=0; row<m.length; row++)
		{
			for(int column=0; column<m[row].length; column++)
			{
				System.out.printf("%4d", m[row][column]);
			}
			System.out.print("\n");
			//System.out.println();
		} //for(int row=0; row<matrix.length; row++)
	}
	
}