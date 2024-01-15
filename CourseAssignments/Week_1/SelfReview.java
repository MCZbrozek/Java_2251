/*
Home: Michael Zbrozek
Date: 1/14/2024
Purpose: To briefly review (or learn) Java content in order to be prepared for CSCI 2251.
Sources:
	EXAMPLE: I got help with compiling from my neighbor, Amelia Bedelia.
	EXAMPLE: I reviewed printf at https://www.baeldung.com/java-printstream-printf
	EXAMPLE: I asked chatGPT "What is wrong with this Java snippet:
for(int i=0; i<10 i++){
	System.out.println(i);
}"
Files: SelfReview.java
*/

import java.util.Scanner;

public class SelfReview {
    public static void main(String[] args) {

        // ----- STEP 4 ----
        // drSeuss Quote to be printed
        String[] drSeuss = { "\"Think left and think right", "and think low and think high.",
                "Oh, the thinks you can think up", "if only you try!\"" };
        System.out.println("Step 4");
        // For each line of the quote, format print with tab char and newline
        for (String string : drSeuss) {
            System.out.printf("\t%s%n", string);
        }
        ;

        // ----- STEP 5 ----
        // Use printf to display e, which is approximately 2.71828. Display 4 decimal
        // digits and make the output 15 spaces wide.
        double eulersNum = 2.718281828459045;
        String eulersString = String.format("%.4f", eulersNum);
        System.out.println("\nStep 5");
        System.out.printf("%15s %n", eulersString);

        // ----- STEP 6 ----
        // Create an integer variable named volume and set the value of volume to 4.
        int volume = 4;
        // Create a double variable named height and set its value to 9.46.
        double height = 9.46;
        // Create a String variable named mountain and set its value to "Chicoma".
        String mountain = "Chicoma";
        // Print out all three variables.
        System.out.println("\nStep 6");
        System.out.println("Volume = " + volume + "\nHeight = " + height + "\nMountain = " + mountain);

        // ----- STEP 7 ----
        // Import the Scanner. Then instantiate a Scanner variable and use it to ask the
        // user for the length, width, and wood type of the table they would like built.

        // Create scanner variable
        System.out.println("\nStep 7");
        Scanner input = new Scanner(System.in);
        System.out.print("How wide is your table?\n");
        int width = input.nextInt();
        System.out.print("How long is your table?\n");
        int length = input.nextInt();
        System.out.print("What type of wood?\n");
        String wood = input.next();

        // ----- STEP 8 ----
        // Is the table width > 48
        int area = width * length;
        System.out.println("\nStep 8");
        if (area > 48) {
            System.out.println("The area of your table exceeds the maximum of 48 units, we will charge you double!");
        } else {
            System.out.println("Area = " + area + " This is completely reasonable.");
        }

        // ----- STEP 9 ----
        System.out.println("\nStep 9");
        // Is length or width > 12
        if (length >= 12 || width >= 12) {
            System.out.println(
                    "Just so ya know, we have to use a special saw for a width or length that is greater than 12units!");
        }
        // Is wood Teak? Wow, big spender!!
        if (wood.toLowerCase().equals("teak")) {
            System.out.println(wood + "!" + " Wow, fancy");
        } else {
            System.out.println(wood + "!" + "Very Cool");
        }
        // ----- STEP 10 ----
        System.out.println("\nStep 10");
        int result = 0;
        for (int i = 1; i <= 100; i++) {
            int multOfSeven = i * 7;
            result = result + multOfSeven;
        }
        System.out.println(result);

        // ----- STEP 11 ----
        System.out.println("\nStep 11");
        int[] numbers = { 8, 3, 1, 2, -4, 9, 1, 6 };
        for (int i = 0; i < numbers.length; i++) {
            if (i == numbers.length - 1) {
                System.out.print(numbers[i] + ".");
            } else {
                System.out.print(numbers[i] + ",");
            }
        }
        // 300 1's, 2's, 3's
        int[] threeHundred = new int[899];
        for (int i = 0; i < threeHundred.length; i++) {
            if (i < 300 || i >= 600) {
                threeHundred[i] = 1;
            } else if (i >= 300 && i < 600) {
                threeHundred[i] = 2;
            }
        }
        // Check values
        System.out.println("\nthreeHundred[299]= " + threeHundred[299] + " threeHundred[300]= " + threeHundred[300]
                + " threeHundred[599]= " + threeHundred[599] + " threeHundred[600]= " + threeHundred[600]);

        // ----- STEP 12 ----
        System.out.println("\nStep 12");
        int count = countEven(numbers);
        int[] arrayOfEvens = getEvens(numbers, count);
        for (int i = 0; i < arrayOfEvens.length; i++) {
            if (i == arrayOfEvens.length - 1) {
                System.out.print(arrayOfEvens[i] + ".");
            } else {
                System.out.print(arrayOfEvens[i] + ",");
            }
        }
        // ----- STEP 13 ----
        System.out.println("\nStep 13");
        int[] nums1 = { 1, 2, 4, 5 };
        int[] nums2 = { 6, 7, 8 };
        double median = getMedianOfTwo(nums1, nums2);
        System.out.println("Nums1 Array =");
        for (int i : nums1) {
            System.out.print(i + " ");
        }
        System.out.println("\nNums2 Array =");
        for (int i : nums2) {
            System.out.print(i + " ");
        }
        System.out.println("\nMedian= " + median);
    }

    // method to count up the number of even numbers in an int array
    public static int countEven(int[] numbers) {
        int count = 0;
        for (int i : numbers) {
            if (i % 2 == 0) {
                count++;
            }
        }
        return count;
    }

    /*
     * takes an int array as input and returns an identical int array,
     * except that all the odds have been removed
     */
    public static int[] getEvens(int[] numbers, int count) {
        int[] newArray = new int[count];
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] % 2 == 0) {
                newArray[count - 1] = numbers[i];
                count--;
            }
        }
        return newArray;
    }

    /*
     * getMedianOfTwo takes two integer arrays as input. These two input arrays
     * are guaranteed to have ALREADY BEEN SORTED. The getMedianOfTwo method then
     * returns the median of the two arrays as if they were combined into one.
     */
    public static double getMedianOfTwo(int[] nums1, int[] nums2) {
        int x = nums1.length;
        int y = nums2.length;
        int[] newArray = new int[x + y];
        int nums2Loc = 0;
        double median = 0;
        int midValue = (int) (newArray.length / 2);

        for (int i = 0; i < x; i++) {
            newArray[i] = nums1[i];
        }
        for (int i = x; i < newArray.length; i++) {
            newArray[i] = nums2[nums2Loc];
            nums2Loc++;
        }
        if (newArray.length % 2 == 0) {
            median = (newArray[midValue] + newArray[midValue + 1]) / 2;
        } else {
            median = newArray[midValue];
        }
        return median;

    }
}
