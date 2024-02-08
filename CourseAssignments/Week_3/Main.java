
/*
Name: Michael Zbrozek
Date: 1/28/2024
Purpose: Read in the data from the file and save each row of data into a new HurricaneRowData object, which are further organized into an ArrayList. Find the row containing the maximum ACE value and return the corresponding year.
Sources:
	GeeksForGeeks 
    https://www.geeksforgeeks.org/classes-objects-java/

Files: 
Main.Java
HurricaneRowData.java
ace.csv
*/

//Needed for input from file
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

//Needed for output to file
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) {

        ArrayList<HurricaneRowData> data = new ArrayList<HurricaneRowData>();
        try {
            File file = new File("ace.csv");
            Scanner scanner = new Scanner(file);

            // Skip first line of file
            String skipLine = scanner.nextLine();
            // System.out.println(skipLine);

            /*
             * Main must read in the data from the file and save each row of data into a new
             * HurricaneRowData object, which are further organized into an ArrayList.
             */
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");

                // New hurricaneRowData
                int year = Integer.parseInt(values[0]);
                int aceVal = Integer.parseInt(values[1]);
                int num1 = Integer.parseInt(values[2]);
                int num2 = Integer.parseInt(values[3]);
                int num3 = Integer.parseInt(values[4]);
                HurricaneRowData hurRowData = new HurricaneRowData(year, aceVal, num1, num2, num3);
                data.add(hurRowData);
            }
        } catch (Exception e) {
            System.out.println("ERROR!");
            System.out.println(e);
            e.printStackTrace();
            System.exit(1);
        }
        /*
         * // Display out the year and maximum ACE value BOTH on the command prompt and
         * also output the information to a text file.
         */
        // Call the yearOfMaxACE method on the arraylist
        String year = yearOfMaxACE(data);
        System.out.println(year);

        // write to file
        try {
            FileWriter fileWriter = new FileWriter("yearOfMaxACE.txt");
            fileWriter.write(year.toString());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    // Private static method that takes the ArrayList of data as input and returns
    // the year in which the ACE index (Hint: the year is 2005)
    private static String yearOfMaxACE(ArrayList<HurricaneRowData> data) {
        int maximumACE = data.get(0).getAceVal();
        // int yearOfMaxACE = data.get(0).getYear();
        String yearToString = data.get(0).toString();
        for (HurricaneRowData hurricaneRowData : data) {
            if (maximumACE < hurricaneRowData.getAceVal()) {
                maximumACE = hurricaneRowData.getAceVal();
                yearToString = hurricaneRowData.toString();
            }
        }
        return yearToString;
    }

}
