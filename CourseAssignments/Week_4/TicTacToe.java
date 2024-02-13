
/*
Name: Michael Zbrozek
Date: 2/4/2024
Purpose: Print board and run tests for TicTacToe game
Sources:
ChatGPT - See prompts in comments
	

Files: 
Main.Java
TicTacToe.java

*/

// import java.lang.reflect.Method;
import java.util.Scanner; // Import the Scanner class

public class TicTacToe {

    // enum cellState
    public enum CellState {
        X,
        O,
        EMPTY
    }

    // Instance variables
    private int col;
    private int row;
    private boolean playAgain = true;
    private boolean nextTurn = true;
    private CellState[][] board;
    private CellState player;

    // Constructor Declaration
    public TicTacToe() {
        board = new CellState[3][3];
        initializeBoard(board);
    }

    // method to set board cellStates to empty
    /*
     * SOURCE: ChatGPT
     * PROMPT: "Print tictactoe board in java, show me a
     * method for adding player moves using an enum to
     * track cell state"
     */
    public void initializeBoard(CellState[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = CellState.EMPTY;
            }
        }
    }

    // Method to read in an int from the user called in the gameLoop Method
    public int getUserInt(String prompt) {
        System.out.print(prompt + ": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    } // end getUserInt

    // Method to read in an String from the user called in the gameLoop Method
    public String getUserString(String prompt) {
        System.out.print(prompt + ": ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    } // end getUserString

    // Method to control game play, consists of 2 while loops, one to start a new
    // game, the inner loop continues the current game until an end condition is
    // reached.
    public void gameLoop() {
        System.out.println("WELCOME to TIC TAC TOE!");
        while (playAgain) {
            TicTacToe game1 = new TicTacToe();
            // game1.initializeBoard(board);
            game1.printBoard();
            game1.player = CellState.O;
            System.out.println("Player " + getCellText(game1.player) + " It's now your turn ");
            while (game1.nextTurn) {
                // run scanner methods getUserInt() to get cell data
                String promptRow = ("Please enter a row (1, 2, or 3)");
                row = getUserInt(promptRow) - 1; // convert player index
                String promptCol = ("Please enter a column (1, 2, or 3)");
                col = getUserInt(promptCol) - 1;
                game1.validMove(col, row, game1.player);
                game1.printBoard();
                game1.gameStatus();
                System.out.println();
            }
            String promptPlayAgain = ("Do you want to play again? (y/n)");
            String userSelection = getUserString(promptPlayAgain);
            if (userSelection.equals("y")) {
                playAgain = true;
            } else {
                System.out.println("Thank you for playing, have a great day!");
                playAgain = false;
            }
        }
    }

    // method to print the board in it's current state
    /*
     * SOURCE:chatgpt
     * PROMPT: See above
     */
    public void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(getCellText(board[i][j]) + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    public String getCellText(CellState cellState) {
        switch (cellState) {
            case X:
                return "X";
            case O:
                return "O";
            case EMPTY:
            default:
                return " ";
        }
    }

    // Check to see if player's move is valid
    public boolean validMove(int col, int row, CellState player) {
        if ((row >= 0 && row < 3)
                && (col >= 0 && col < 3)
                && (board[row][col] == CellState.EMPTY)) {
            board[row][col] = player;
            return true;
        } else {
            System.out.println("Invalid Move, enter a valid row or column (1-3) or find an empty cell.");
            return false;
        }

    }

    public void gameStatus() {
        // check for win by calling the win method
        if (win()) {
            System.out.println("Player " + player + " Wins!!");
            nextTurn = false;
            return;
        }

        if (draw()) {
            System.out.println("No winner this time, play again?");
            nextTurn = false;
            return;
        }
        // System.out.println("Value of player on line 159: " + player);
        // Switch to the next player
        if (player == CellState.O) {
            player = CellState.X;
        } else {
            player = CellState.O;
        }

        System.out.println("Player " + getCellText(player) + ", it's your turn.");
    }// end gameState method

    // Check to see if a player has won horizontally, diagonally or vertically
    /*
     * Modified from ChatGPT
     * prompt
     * "Create a method called gameStatus to keep track of player turns and logic to see if a player has won the game."
     */
    public boolean win() {
        if (checkRows() || checkCols() || checkDiag()) {
            return true;
        } else {
            return false;
        }
    } // end win method

    // checks each row, makes sure first cell is not empty and that each cell state
    // matches through the row.
    public boolean checkRows() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != CellState.EMPTY
                    && board[i][0] == board[i][1]
                    && board[i][1] == board[i][2]) {
                return true;
            }
        }
        return false;
    } // end checkRows method

    // checks each col, makes sure first cell is not empty and that each cell state
    // matches through the col.
    public boolean checkCols() {
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != CellState.EMPTY
                    && board[0][i] == board[1][i]
                    && board[1][i] == board[2][i]) {
                return true;
            }
        }
        return false;
    } // end checkCols method

    // checks each possible diagonal from [0][0] through [2][2] & [0][2] through
    // [2][0], makes sure first cell is not empty and that each cell state
    // matches through the row.
    private boolean checkDiag() {
        if (board[0][0] != CellState.EMPTY
                && board[0][0] == board[1][1]
                && board[1][1] == board[2][2]) {
            return true;
        }

        if (board[0][2] != CellState.EMPTY
                && board[0][2] == board[1][1]
                && board[1][1] == board[2][0]) {
            return true;
        }
        return false;
    } // checkDiag

    // checks to see if all of the spaces are filled with a CellState, if a win
    // condition was not called, this is run in gameStatus if board is not full,
    // play continues
    private boolean draw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == CellState.EMPTY) {
                    return false; // Board is not full, the game can continue
                }
            }
        }
        return true; // Board is full, it's a draw
    } // end draw method

    public void runTests() {
        iobLow(row, col);
        iobHigh(row, col);
        inBounds(row, col);
        iNotEmpty(row, col);
        winCondition(row, col);
        drawCondition(row, col);
        winDiag(row, col);
        continueCondition(row, col);
    } // end runTests method

    // validMove returns false when one (or both) of its inputs is out of bounds on
    // the low end
    public void iobLow(int row, int col) {
        System.out.println("returns false when one (or both) of its inputs is out of bounds: too LOW");
        TicTacToe game1 = new TicTacToe();
        row = -1;
        col = 0;
        game1.printBoard();
        boolean eval = game1.validMove(col, row, CellState.O);
        System.out.println(eval);
        if (eval) {
            System.out.println("TRVE: Valid Move");
        } else {
            System.out.println("False! Invalid move" + " col " + col + " row " + row);
        }
        System.out.println();
    }

    // validMove returns false when one (or both) of its inputs is out of bounds on
    // the high end
    public void iobHigh(int row, int col) {
        System.out.println("returns false when one (or both) of its inputs is out of bounds: too high");
        TicTacToe game1 = new TicTacToe();
        row = 1;
        col = 4;

        boolean eval = game1.validMove(col, row, CellState.O);
        game1.printBoard();
        System.out.println(eval);
        if (eval) {
            System.out.println("TRVE: Valid Move");
        } else {
            System.out.println("False! Invalid move" + " col " + col + " row " + row);
        }
        System.out.println();
    }

    // validMove returns false when its inputs index a cell in the board that is not
    // empty
    public void iNotEmpty(int row, int col) {
        System.out.println("returns false when its inputs index a cell  that is not empty");
        TicTacToe game1 = new TicTacToe();
        game1.validMove(0, 0, CellState.X);
        game1.validMove(1, 0, CellState.X);
        boolean eval = game1.validMove(0, 0, CellState.O);
        game1.printBoard();
        System.out.println(eval);
        if (!eval) {
            System.out.println("This space is occupied");
        } else {
            System.out.println("Valid move, cellState EMPTY");
        }
        System.out.println();
    }

    // validMove returns true when its inputs are in bounds and refer to an empty
    // board cell
    public void inBounds(int row, int col) {
        TicTacToe game1 = new TicTacToe();
        row = 1;
        col = 2;
        game1.printBoard();
        boolean eval = game1.validMove(col, row, CellState.O);
        System.out.println(eval);
        if (eval) {
            System.out.println("TRVE: Valid Move");
        } else {
            System.out.println("False! Invalid move");
        }
        System.out.println();
    }

    // gameStatus returns WIN when a player has three in a row
    public void winCondition(int row, int col) {
        System.out.println("gameStatus returns WIN when a player has three in a row");
        TicTacToe game1 = new TicTacToe();
        game1.validMove(1, 0, CellState.O);
        game1.validMove(1, 1, CellState.O);
        game1.validMove(1, 2, CellState.O);
        game1.printBoard();
        game1.gameStatus();
        System.out.println();
    }

    // gameStatus returns WIN when a player has three in a row
    public void winDiag(int row, int col) {
        System.out.println("gameStatus returns WIN when a player has three in a row DIAGONALLY");
        TicTacToe game1 = new TicTacToe();
        game1.validMove(0, 0, CellState.X);
        game1.validMove(1, 1, CellState.X);
        game1.validMove(2, 2, CellState.X);
        game1.printBoard();
        game1.gameStatus();
        System.out.println();
    }

    // gameStatus returns DRAW when the board is full and there are no
    // three-in-a-rows
    public void drawCondition(int row, int col) {
        System.out.println("gameStatus returns DRAW when there are NO 3 in a row conditions");
        TicTacToe game1 = new TicTacToe();
        game1.validMove(0, 0, CellState.O);
        game1.validMove(1, 1, CellState.X);
        game1.validMove(2, 2, CellState.X);
        game1.validMove(1, 0, CellState.X);
        game1.validMove(1, 2, CellState.O);
        game1.validMove(0, 2, CellState.X);
        game1.validMove(2, 0, CellState.O);
        game1.validMove(0, 1, CellState.X);
        game1.validMove(2, 1, CellState.O);
        game1.printBoard();
        game1.gameStatus();
        System.out.println();
    }

    // gameStatus returns CONTINUE when at least one cell is empty and there are no
    // three-in-a-rows
    public void continueCondition(int row, int col) {
        System.out.println("gameStatus returns DRAW when there are NO 3 in a row conditions");
        TicTacToe game1 = new TicTacToe();
        game1.validMove(0, 0, CellState.O);
        game1.validMove(1, 1, CellState.X);
        game1.validMove(2, 2, CellState.X);
        game1.validMove(1, 0, CellState.X);
        game1.validMove(1, 2, CellState.O);
        game1.validMove(0, 2, CellState.X);
        game1.validMove(2, 0, CellState.O);

        game1.printBoard();
        game1.gameStatus();
        System.out.println();
    }
}
