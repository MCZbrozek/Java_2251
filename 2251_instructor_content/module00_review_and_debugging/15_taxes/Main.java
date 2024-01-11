/*  
Calculates income tax, adjusted income, and total income.
5/31/2021
Lab02
CSCI 1152 R01
*/
import java.text.NumberFormat;
import java.util.Scanner;

public class Main 
{

	public static void main (String[] args)
	{
		Scanner sc = new Scanner(System.in);
		String response = "continue";
		while(response.equals("continue"))
		{
			calculateTaxes(sc);
			System.out.println("Do you have more taxes to calculate? (\"continue\" or quit)");
			sc.nextLine();//flush the buffer
			response = sc.nextLine();
		}
		sc.close();
		System.out.println("Goodbye");
	}
	
	private static void calculateTaxes(Scanner input)
	{
		System.out.print("Number of Exemptions: ");
		int exemptions = input.nextInt();

		System.out.print("Gross Salary: ");
		double gross_salary = input.nextDouble();

		System.out.print("Interest Income: ");
		double interest_income = input.nextDouble();

		System.out.print("Capital Gains: ");
		double capital_gains = input.nextDouble();

		System.out.print("Charitable Contributions: ");
		double charitable_contributions = input.nextDouble();

		double total_income = gross_salary + interest_income + capital_gains;
		double adjusted_income = total_income - (exemptions*1500.00) - charitable_contributions;
		double final_adjusted_income = adjusted_income;

		double total_tax = 0;
		double temp_tax;

		//might be cleaner to use a switch here
		if (adjusted_income > 50000) 
		{
			temp_tax = adjusted_income - 50000;
			adjusted_income = 50000; 
			total_tax += .28 * temp_tax;
		}
		if (adjusted_income > 32000)
		{
			temp_tax = adjusted_income - 32000;
			adjusted_income = 32000; 
			total_tax += .23 * temp_tax;
		}
		if (adjusted_income > 10000)
		{
			temp_tax = adjusted_income - 10000;
			adjusted_income = 10000; 
			total_tax += .15 * temp_tax;
		}

		//This is some silly code to get rid 
		//of that space.
		//Got it from: https://stackoverflow.com/questions/58337706/how-do-i-put-a-dollar-sign-when-using-printf-in-java
		NumberFormat f = NumberFormat.getCurrencyInstance();

		System.out.printf("\n%-16s %10s \n","Total Income:", f.format(total_income));
		System.out.printf("%-16s %10s \n","Adjusted Income:", f.format(final_adjusted_income));
		System.out.printf("%-16s %10s \n","Total Tax:", f.format(total_tax));
	}
	
}
