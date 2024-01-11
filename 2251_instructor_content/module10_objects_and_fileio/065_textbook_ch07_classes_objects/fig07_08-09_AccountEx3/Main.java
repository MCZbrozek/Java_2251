/*
This is a modification of the code in
Fig. 7.8 and 7.9 from the book.

Fill in the code after each TODO to add
input validation and usage tracking for
deposits. Once finished, you should get
the following output if you recompile
and run the code first inputting -3 and
then 2.

Jane Green balance: $50.00
John Blue balance: $0.00

Enter deposit amount for account1: -3

adding -3.00 to account1 balance

Jane Green balance: $50.00
John Blue balance: $0.00

Enter deposit amount for account2: 2

adding 2.00 to account2 balance

Jane Green balance: $50.00
John Blue balance: $2.00

account1 had 3 deposits.
*/

// Inputting and outputting floating-point numbers with Account objects.
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Account account1 = new Account("Jane Green", 50.00);
		Account account2 = new Account("John Blue", -7.53);

		// display initial balance of each object
		System.out.printf("%s balance: $%.2f%n",
			account1.getName(), account1.getBalance());
		System.out.printf("%s balance: $%.2f%n%n",
			account2.getName(), account2.getBalance());

		// create a Scanner to obtain input from the command window
		Scanner input = new Scanner(System.in);

		System.out.print("Enter deposit amount for account1: ");
		double depositAmount = input.nextDouble();

		System.out.printf("%nadding %.2f to account1 balance%n%n", depositAmount);
		account1.deposit(depositAmount);

		// display balances
		System.out.printf("%s balance: $%.2f%n",
			account1.getName(), account1.getBalance());
		System.out.printf("%s balance: $%.2f%n%n",
			account2.getName(), account2.getBalance());

		System.out.print("Enter deposit amount for account2: ");
		depositAmount = input.nextDouble();
		
		System.out.printf("%nadding %.2f to account2 balance%n%n", depositAmount);
		account2.deposit(depositAmount);

		// display balances
		System.out.printf("%s balance: $%.2f%n",
			account1.getName(), account1.getBalance());
		System.out.printf("%s balance: $%.2f%n%n",
			account2.getName(), account2.getBalance());
		
		
		account1.deposit(depositAmount);
		account1.deposit(depositAmount);
		account1.deposit(depositAmount);
		System.out.println("account1 had "+account1.getDepositCount()+" deposits.");
	}
}

/**************************************************************************
 * (C) Copyright 1992-2018 by Deitel & Associates, Inc. and					*
 * Pearson Education, Inc. All Rights Reserved.									*
 *																								*
 * DISCLAIMER: The authors and publisher of this book have used their	  *
 * best efforts in preparing the book. These efforts include the			 *
 * development, research, and testing of the theories and programs		  *
 * to determine their effectiveness. The authors and publisher make		 *
 * no warranty of any kind, expressed or implied, with regard to these	 *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or		 *
 * consequential damages in connection with, or arising out of, the		 *
 * furnishing, performance, or use of these programs.							*
 *************************************************************************/
