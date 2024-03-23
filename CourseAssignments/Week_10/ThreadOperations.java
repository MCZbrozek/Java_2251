/*
Name: Michael Zbrozek
Date: 3/21/2024
Purpose: Read in matrixes, split them into 2 matrixes and further subdivide them into quadrants that will be added together by 4 threads and output as a separate matrix print to console.
Sources:
ChatGPT - See prompts in comments
CSCI1152 - Paintball assignment for 2d array scanner and print
GeeksforGeeks - See links in comments


Files: 
Main.Java
ThreadOperations.java
*/

// ThreadOperation – extends Thread and performs submatrix addition
public class ThreadOperations extends Thread {
    private int[][] matrixA;
    private int[][] matrixB;
    private int quadrant;

    // Each ThreadOperation will take as input (through the
    // constructor) two matrices and a quadrant indicator. The indicator could be a
    // String, an int, an enum or a set of indexes. It’s up to you.
    public ThreadOperations(int[][] matrixA, int[][] matrixB, int quadrant) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }

    public void run() {

    }
}
