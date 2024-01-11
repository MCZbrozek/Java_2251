public class MainWithCellstate
{
	enum CellState
	{
		X,
		O,
		EMPTY
	}

	public static void main(String[] args)
	{
		CellState[][] board = new CellState[3][3];
		System.out.println(board[0][0]);
		
		for(int i=0; i<3; i++)
		{
			for(int j=0; j<3; j++)
			{
				board[i][j] = CellState.EMPTY;
			}
		}
		System.out.println(board[0][0]);
		
		board[2][1] = CellState.X;
	}
}
