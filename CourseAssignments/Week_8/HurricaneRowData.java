/*
Name: Michael Zbrozek
Date: 1/28/2024
Purpose: Read in the data from the file and save each row of data into a new HurricaneRowData object, which are further organized into an ArrayList. Find the row containing the maximum ACE value and return the corresponding year.
Sources:
	GeeksForGeeks 
    https://www.geeksforgeeks.org/classes-objects-java/

Files: 
Main.Java
HurricanRowData.java
ace.csv
*/

public class HurricaneRowData {

    // HurricaneRowData must have 5 private instance variables (one for each of the
    // columns of data in ace.csv).
    // The class must also have a constructor and a
    // toString method. You should add any getter or setter methods that you need.

    // Instance variables
    private int year;
    private int aceVal;
    private int num1;
    private int num2;
    private int num3;

    // Constructor declaration
    public HurricaneRowData(int year, int aceVal, int num1, int num2, int num3) {
        this.year = year;
        this.aceVal = aceVal;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
    }

    // Getters and setters, not gonna lie, I used VS Code's
    // "generate getters and setters"
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAceVal() {
        return aceVal;
    }

    public void setAceVal(int aceVal) {
        this.aceVal = aceVal;
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public int getNum3() {
        return num3;
    }

    public void setNum3(int num3) {
        this.num3 = num3;
    }

    // toString Method to print the result when max ACE is accessed by main
    @Override
    public String toString() {
        String temp = String.format("%d,  %d",
                this.aceVal, this.year);
        return temp;
    }

}

// what if empty
// 2. what if new value less than first
// 3 what does it look like printing out the whole thing?