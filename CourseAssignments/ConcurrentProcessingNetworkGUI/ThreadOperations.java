
/*
Name: Michael Zbrozek
Date: 4/18/2024
Assignment: Networking Part 2
Purpose: Read in matrixes at clientside GUI, split them into 2 matrixes and send to server further subdivide them into quadrants that will be added together by 4 threads and output as a separate matrix print to console.
Sources:
ChatGPT - See prompts in comments
CSCI1152 - Paintball assignment for 2d array scanner and print
GeeksforGeeks - See links in comments
Neal Hotshulte helped me to Debug and gave me some key tricks for making this gizmo run! 


Files: 
Client.java
ClientStart.java
Server.java
ServerStart.java
MatrixFileProcessor.java
MatrixGUI.java
ThreadOperations.java
*/

// ThreadOperation – extends Thread and performs submatrix addition
public class ThreadOperations extends Thread {
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] matrixResult;
    private int quadrant;

    // Each ThreadOperation will take as input (through the
    // constructor) two matrices, the result matrix and a quadrant indicator.
    public ThreadOperations(int[][] matrixA, int[][] matrixB, int[][] matrixResult, int quadrant) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.matrixResult = matrixResult;
        this.quadrant = quadrant;
    }

    // getMatrixDimensions will retrieve the rows and cols value of any matrix that
    // passed to it and output an int[] array of length 2 containing the rows @ [0]
    // and cols @ [1]
    public static int[] getMatrixDimensions(int[][] matrix) {
        int[] matrixDimensions = new int[2];

        int rows = matrix.length;
        int cols = matrix[0].length;

        matrixDimensions[0] = rows;
        matrixDimensions[1] = cols;

        return matrixDimensions;
    }

    /*
     * Determines the indexes needed to iterate over one of the four quadrants.
     * Takes as input the row count, column count, and quadrant 1-4.
     */
    public static int[] getQuadrantIndex(int[][] matrix, int quadrant) {
        // runs the getMatrix dimensions method to get rows and cols for a given matrix
        int[] matrixDimensions = getMatrixDimensions(matrix);

        // unpacks the array for use in this method
        int rows = matrixDimensions[0];
        int cols = matrixDimensions[1];

        // return 4 numbers in an array: row start, row end, column start,
        // column end.
        int rowStart = 0;
        int rowEnd = 0;
        int colStart = 0;
        int colEnd = 0;

        switch (quadrant) {
            case 1:
                // System.out.println("Entered case 1");
                rowStart = 0;
                rowEnd = (rows / 2) - 1;
                colStart = 0;
                colEnd = (cols / 2) - 1;
                break;
            case 2:
                // System.out.println("Entered case 2");
                rowStart = 0;
                rowEnd = (rows / 2) - 1;
                colStart = (cols / 2) - 1;
                colEnd = cols - 1;
                break;
            case 3:
                // System.out.println("Entered case 3");
                rowStart = (rows / 2);
                rowEnd = rows - 1;
                colStart = 0;
                colEnd = (cols / 2) - 1;
                break;
            case 4:
                // System.out.println("Entered case 4");
                rowStart = (rows / 2);
                rowEnd = rows - 1;
                colStart = (cols / 2);
                colEnd = cols - 1;
                break;

            default:
                System.out.println("Default case reached, check your inputs for 'getQuadrantIndex'");
                break;
        }
        int[] quadrantIndexesArray = new int[] { rowStart, rowEnd, colStart, colEnd };

        return quadrantIndexesArray;
    }

    public void run() {
        // Get quadrant indexes for each array.
        // System.out.println("Quad =" + quadrant);
        int[] quad_IndexesA = getQuadrantIndex(matrixA, quadrant);
        // int[] quad_IndexesB = getQuadrantIndex(matrixB, quadrant);

        // Unpack those quadIndex arrays, they are silly
        int rowStart = quad_IndexesA[0];
        int rowEnd = quad_IndexesA[1];
        int colStart = quad_IndexesA[2];
        int colEnd = quad_IndexesA[3];

        // Outer loop runs from rowStart to rowEnd
        for (int i = rowStart; i <= rowEnd; i++) {
            // Inner loop runs from colStart to colEnd
            for (int j = colStart; j <= colEnd; j++) {
                // Set the value of matrixResult at each row/col position to the sum of the
                // values in each location.
                matrixResult[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
    }
}
