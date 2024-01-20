public class Main
{
	public static void main(String[] args)
	{
		System.out.println();
		int[][] matrix = {{0, 4, 0, 0, 0},
						  {0, 0, 0, 0, 0},
						  {0, 0, 0, 3, 0},
						  {0, 0, 0, 0, 0},
						  {0, 1, 0, 0, 0}};

		SparseMatrix sparse = new SparseMatrix(matrix, 0);
		
		long size = ObjectSizeFetcher.getObjectSize(sparse);
		System.out.println(size);
	}
	
	
	
	public static void printArray(int[][] m)
	{
		for(int r=0; r<m.length; r++)
		{
			for(int c=0; c<m[0].length; c++)
			{
				System.out.printf("%4d", m[r][c]);
			}
			System.out.println();
		}

	}
}