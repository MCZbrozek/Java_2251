
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

import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Import from user defined file path - as used in Human Resource exercises
import java.nio.file.Path;
import java.nio.file.Paths;

public class MatrixFileProcessor {
    private static int[][] firstHalf;
    private static int[][] secondHalf;
    public static int rows;
    public static int cols;

    public static void setFirstMatrix(int[][] firstMatrix) {
        firstHalf = firstMatrix;
    }

    public static void setSecondMatrix(int[][] secondMatrix) {
        secondHalf = secondMatrix;
    }

    public static int[][] getFirstHalf() {
        return firstHalf;
    }

    public static int[][] getSecondHalf() {
        return secondHalf;
    }

    public static int getRows() {
        return rows;
    }

    public static int getCols() {
        return cols;
    }

    // Initialize the scanner and check for a valid path
    public static void readMatrixFromFile(String fileNameString) throws IOException {
        Path filePath = Paths.get(fileNameString);

        // instantiate scanner
        Scanner fileReader = null;

        try {
            fileReader = new Scanner(filePath);
        } catch (Exception e) {
            System.out
                    .println("Fatal Error!! - JK, your file isn't coming in quite right. Check the path and try again! "
                            + e.getMessage());
            System.exit(1);
        }

        try {
            // Give the console some idea of what is going on.
            System.out.println("Reading in matrix from file: " + filePath + "\n");

            // Read in first line from file, and set vars to create array
            // Initialize 2d array with row and column length
            rows = fileReader.nextInt();
            cols = fileReader.nextInt();
            System.out.println("row =" + rows);
            System.out.println("col =" + cols);

            // Call the populate array function and use it to set the matrix objects
            int[][] firstHalf = populateTargetArray(fileReader, rows, cols);
            setFirstMatrix(firstHalf);

            int[][] secondHalf = populateTargetArray(fileReader, rows, cols);
            setSecondMatrix(secondHalf);

            // Print each half to the console - Testing
            System.out.println("The first half - ");
            // print2dArray(firstHalf);

            System.out.println("The second half - ");
            // print2dArray(secondHalf);

        } catch (java.util.InputMismatchException e) {
            System.out.println("Input does not match\n");
            e.printStackTrace();
            System.out.println("\n" + e + "\n");
        }
        fileReader.close();
    }

    // populateTargetArray takes in the scanner and and the instantiated array and
    // read next int to populate the array
    public static int[][] populateTargetArray(Scanner fileReader, int rows, int cols) {
        int[][] tempMatrix = new int[rows][cols];
        // There is difference between row(i) and rows(int read from file).
        // Similarly col(j) is a counter, cols is an int read from file
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                tempMatrix[row][col] = fileReader.nextInt();
            }
        }
        return tempMatrix;
    }

    // print2dArray takes a two-dimensional array as input and prints it out with
    // the rows and columns lined up. You must use System.out.printf.
    public static void print2dArray(int[][] array) {
        // code to print 2dArray
        // source - method I wrote for paintball.java in 1152
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                System.out.printf("%-2d", array[row][col]);
            }
            System.out.print("\n");
        }
    }
}
