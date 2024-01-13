// Name: Mike Zbrozek
// Class: 2251
// Assignment: CosineFromScratch
// Purpose: Modify as instructed
// FileName: "CosineFromScratch.java"

import java.util.Scanner;
import java.lang.Math;

public class CosineFromScratch {
	/*
	 * The following code demonstrates how to
	 * estimate the cosine of a number.
	 * 
	 * Your assignment is to migrate the calculation
	 * of the cosine into a new static method named
	 * cosine. The method should take one double
	 * input and return one double output.
	 * 
	 * Make sure to call the method from main and
	 * set the variable estimate equal to the
	 * returned value.
	 */

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("\nGive me a number: ");
		double x = input.nextDouble(); // Trying to find cosine of this number

		double correct_cos = Math.cos(x);

		double estimate = cosine(x);

		System.out.printf("%-22s %f\n", "The correct cosine is:", correct_cos);
		System.out.printf("%-22s %f\n", "Our estimate is:", estimate);
	}

	public static int factorial(int n) {
		int to_return = 1;
		for (int i = 1; i <= n; i++) {
			to_return = to_return * i;
		}
		return to_return;
	}

	public static double cosine(double x) {
		// Source for the following:
		// https://austinhenley.com/blog/cosine.html
		double num_terms = 10;
		int div = (int) (x / Math.PI);
		x = x - (div * Math.PI);
		int sign = -1;
		double estimate = 1.0;
		double inter = 1.0;
		double num = x * x;
		for (int i = 1; i <= num_terms; i++) {
			double comp = 2.0 * i;
			double den = comp * (comp - 1.0);
			inter *= num / den;
			estimate += inter * sign;
			sign *= -1; // flip the sign
		}
		// If div is odd, flip the estimate
		if (div % 2 != 0) {
			estimate = estimate * -1;
		}
		return estimate;
	}
}
