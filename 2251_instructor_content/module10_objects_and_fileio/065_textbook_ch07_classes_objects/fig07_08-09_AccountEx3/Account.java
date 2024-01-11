/* Fig. 7.8: Account.java
Account class with a double instance variable
balance and a constructor and deposit method
that perform validation. */

public class Account
{
	private String name; // instance variable
	private double balance; // instance variable
	
	/* TODO: Add a new private integer instance
	variable named depositCount and set it equal
	to zero. */
	

	// Account constructor that receives two parameters
	public Account(String name, double balance) {
		this.name = name; // assign name to instance variable name

		/* TODO: Input validation:
		Set this.balance equal to the given balance
		only if the given balance is greater than
		0.0 */

	}

	// method that deposits (adds) only a valid amount to the balance
	public void deposit(double depositAmount) {
		/* TODO: Input validation:
		Increase balance by depositAmount only
		if depositAmount is greater than 0.0.
		Also increment depositCount, but only if
		balance was modified. */

	}

	// method returns the account balance
	public double getBalance() {
		return balance;
	}

	// method that sets the name
	public void setName(String name) {
		this.name = name;
	}

	// method that returns the name
	public String getName() {
		return name;
	}
	
	public int getDepositCount() {
		//TODO: return depositCount instead of zero
		return 0;
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
