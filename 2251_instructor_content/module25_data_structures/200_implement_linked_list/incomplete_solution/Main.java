
public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub


		boolean b = validMove(-1,2); //should be false
		System.out.println("out of bounds low: "+b);
		
		boolean b = validMove(1,22222); //should be false
		System.out.println("out of bounds high: "+b);
		
		board[0][0] = CellState.X;
		board[0][1] = CellState.X;
		board[0][2] = CellState.X;
		GameState g = gameStatus();
		System.out.println("should be a win: "+g);
		
		
		
	}

}
