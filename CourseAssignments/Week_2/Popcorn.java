/*
Name: Michael Zbrozek
Date: 1/20/2024
Purpose: Bounce from array index to value
Sources:
	None
Files: Popcorn.java
*/

/*
Array Popcorn

Fill in code for the method named
"countPops" at the bottom of this
document.
countPops takes an integer array as
input. The method will repeatedly
read a value from the array, go to
the indicated position, read the
value at that position, then go there,
and so on until a limit of 100 is
reached or the index is out of bounds.

The first value should be read from
the array at index 0.

The method must return an integer
count of how many times it read a
value from the array.

Here's an example.

INPUT: {1,3,0,5}
OUTPUT: 3
The method reads 1 from index 0
The method reads 3 from index 1
The method reads 5 from index 3
The method identifies that index 5
is out of bounds and returns 3 to
indicate that 3 values were read
from the array.

Here's another example:

INPUT: {4,-1,0,5,2,8,-2}
OUTPUT: 100
The method reads 4 from index 0
The method reads 2 from index 4
The method reads 0 from index 2
The method reads 4 from index 0
...
The method repeats up to a limit
of 100 times and returns 100.

Here's another example:

INPUT: {3,-1,4,2,5,-2}
OUTPUT: 5
The method reads 3 from index 0
The method reads 2 from index 3
The method reads 4 from index 2
The method reads 5 from index 4
The method reads -2 from index 5
The method identifies that index -2
is out of bounds and returns 5 to
indicate that 3 values were read
from the array.

Upload your Java file as your
answer to this question.
*/
public class Popcorn {
	// Do not alter this method. It is purely for testing purposes.
	public static void main(String[] args) {
		System.out.println();
		int score = 0;

		// Test case 1: out of bounds above
		int[] example1 = { 1, 3, 0, 5 };
		score += tester(example1, 3);

		// Test case 2: loop to limit
		int[] example2 = { 4, -1, 0, 5, 2, 8, -2 };
		score += tester(example2, 100);

		// Test case 3: out of bounds below
		int[] example3 = { 3, -1, 4, 2, 5, -2 };
		score += tester(example3, 5);

		// Test case 4: out of bounds above barely
		int[] example4 = { 3, -1, 4, 2, 5 };
		score += tester(example4, 4);

		// Test case 5: empty array
		int[] example5 = {};
		score += tester(example5, 0);

		System.out.println("WOWZA! -- Your output is " + score + " out of 5. -- SO COOL!");
		System.out.println("WARNING: A perfect score here DOES NOT guarantee full credit on this question.");
	}

	/*
	 * Do not alter this method. It is purely
	 * for testing purposes. It takes an array
	 * to test and an expected count as input
	 * and returns 1 if the countPops
	 * implementation gives the expected
	 * output and returns 0 otherwise.
	 */
	private static int tester(int[] example, int expectedCount) {
		int x = countPops(example);
		System.out.print("Count is: " + x);
		if (x == expectedCount) {
			System.out.println(". Correct!");
			return 1;
		} else {
			System.out.println(". INCORRECT");
			return 0;
		}
	}

	// method takes in an array of numbers and uses the value at each index to
	// determine the next index to move to.
	// Returns number of times that it "popped" between value and index before
	// Either an index out of bounds error or a 100 pops. Whichever comes first.
	public static int countPops(int[] arr) {
		int count = 0;
		int pop = 0;
		int prevIndex = 0;
		System.out.println("\n--- Next ---");
		for (int i : arr) {
			System.out.print(i + " ");
		}
		if (arr.length > 0) {
			while (count < 100) {
				prevIndex = pop;
				pop = arr[pop];
				count++;
				System.out.printf("\nThe method reads %d from index %d", pop, prevIndex);
				if (pop > arr.length - 1 || pop < 0) {
					System.out.printf(
							"\nThe method identifies that index %d is out of bounds; %d values were read from the array.\n",
							prevIndex, count);
					break;
				}
			}
			if (count == 100) {
				System.out.println("\nThe method repeats up to a limit of 100 times and returns 100.");
			}
		} else {
			System.out.println("[ . ? . ? . ? ]");
			System.out.println("That array was empty, don't do that again!");
		}
		return count;
	}
}