/*
Name: Michael Zbrozek
Date: 2/13/2024
Purpose: a method that takes as input an array of integers and a target integer, and returns the indexes of the two numbers in the array that adds up to the target. Or return -1,-1 if no such pair exists.

Sources: 	

Files: 
Main.Java
*/
public class Main {
    public static void main(String[] args) {
        int[] array = { 2, 5, 6, 15, 14, 7, 9 };
        int target = 19;
        int[] outPut = new int[2];

        outPut = findCombo(array, target, outPut);

        if ((outPut[0] == -1) && (outPut[1] == -1)) {
            System.out.println("There were no combinations of numbers that sum to the target, sorry. ");
        }
        for (int r : outPut) {
            System.out.print("\"" + r + "\" ");
        }

    }

    // method to find a combo of integers in an array that add to target
    public static int[] findCombo(int[] array, int target, int[] outPut) {
        int num2 = 0;
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            int num1 = array[i];
            for (int j = 1; j < array.length; j++) {
                outPut[0] = i;
                outPut[1] = j;
                num2 = array[j];
                sum = num1 + num2;
                System.out.println(i + "= " + num1 + " + " + num2 + " = " + sum);
                if (sum == target) {
                    System.out.println("The index of the two numbers that add to the target are - ");
                    return outPut;
                } else {
                    outPut[0] = -1;
                    outPut[1] = -1;
                }
            }
        }
        return outPut;
    }
}
