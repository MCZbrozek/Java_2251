
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
// Imports inspired by ThreadedAutoComplete Assignment
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;

// Other suggested imports for improved interface readability
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class MatrixGUI extends JFrame {
    // Variable to hold the fileName entered into the JTextField
    private JTextField enterFileName;

    // A private JTextArea instance to display the matix
    private JTextArea matrixTextArea1;
    private JTextArea matrixTextArea2;
    private JTextArea resultTextArea;

    // Instance of Client
    private Client client;

    // Matrix objects
    int[][] matrix1;
    int[][] matrix2;
    int[][] resultMatrix = null;

    // Rows and cols
    int rows;
    int cols;

    // set up the GUI
    public MatrixGUI(Client client) {
        super("Networked, Threaded, Matrix Magic!");

        // Custom color Source GeeksforGeeks, made more specific using ChatGPT
        Color transparentBlue = new Color(0, 0, 255, 100);
        Color customColor = new Color(47, 79, 79);

        // Set our fonts
        // For the enterFileName
        Font fontSerif = new Font("Serif", Font.PLAIN, 22);
        // For the matrixTextArea
        Font fontMono = new Font("Monospaced", Font.PLAIN, 14);

        // Set our border
        LineBorder grayBorder = new LineBorder(customColor);

        setLayout(null);

        enterFileName = new JTextField("matrix2.txt");
        enterFileName.setFont(fontSerif);
        enterFileName.setBorder(grayBorder);
        // enterFileName.setBackground(transparentBlue);
        // enterFileName.setForeground(Color.white);
        enterFileName.setBounds(10, 10, 800, 60);
        add(enterFileName);

        // JTextArea to display matrix
        matrixTextArea1 = new JTextArea();
        matrixTextArea2 = new JTextArea();
        resultTextArea = new JTextArea();

        // Font styling for the JTextAreas
        matrixTextArea1.setFont(fontMono);
        matrixTextArea2.setFont(fontMono);
        resultTextArea.setFont(fontMono);

        // add displays to this JFrame
        add(matrixTextArea1);
        add(matrixTextArea2);
        add(resultTextArea);

        // Define the dimensions of matrixTextAreas
        matrixTextArea1.setBounds(10, 80, 800, 200);
        matrixTextArea2.setBounds(10, 300, 800, 200);
        resultTextArea.setBounds(10, 520, 800, 200);

        setSize(850, 750);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -- Event Listener for file names, when user presses 'enter'.
        enterFileName.addActionListener(e -> {
            String fileName = enterFileName.getText().trim(); // saves our file name when user presses enter
            try {
                // System.out.println(enterFileName.getText()); <-- Test

                // Call the readMatrixFromFile method, which receives the filename from the
                // user.
                MatrixFileProcessor.readMatrixFromFile(fileName);

                rows = MatrixFileProcessor.getRows();

                cols = MatrixFileProcessor.getCols();

                // The previous function sets the matrix objects, we just fetch them here so we
                // can print them to the JTextAreas
                matrix1 = MatrixFileProcessor.getFirstHalf();
                // Pass the matrix and the assigned text area to the display matrix methods
                displayMatrix(matrix1, matrixTextArea1);
                matrix2 = MatrixFileProcessor.getSecondHalf();
                displayMatrix(matrix2, matrixTextArea2);
                // We were able to call the sendObject method from the GUI because we passed an
                // instance of the client to the GUI constructor.
                client.sendData(rows);
                client.sendData(cols);
                client.sendObject(matrix1);
                client.sendObject(matrix2);

                while (resultMatrix == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException InterruptedException) {
                        System.out.println("InterruptedException line");
                    }
                    // call the Getter method from client to make the result available for printing
                    // for the result matrix.
                    resultMatrix = client.getResultMatrix();
                }
                // Display the result matrix!
                displayMatrix(resultMatrix, resultTextArea);

                // Otherwise I would need to
                // Thread would be responsible for grabbing the data from client and putting it
                // in the text area.

            } catch (IOException ex) {
                ex.printStackTrace();
                // Handle file reading error
            }
        });
    }

    // This was made for when I thought I should run the
    // readMatrixFromFile(fileName) from Main.java
    public String getFilePath() {
        String filePath = enterFileName.getText().trim();
        return filePath;
    }

    // Method takes in the matrix we need and the JTextArea that it was assigned to
    // and loops around so that it can print the array to the GUI
    private void displayMatrix(int[][] matrix, JTextArea matrixTextArea) {
        // Clear previous content
        matrixTextArea.setText("");

        // Print the matrix in JTextArea
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrixTextArea.append(matrix[i][j] + "\t");
            }
            matrixTextArea.append("\n");
        }
    }

}
