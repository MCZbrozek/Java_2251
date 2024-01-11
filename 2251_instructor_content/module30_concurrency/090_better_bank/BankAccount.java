public class BankAccount
{
	private int every_last_penny;
	
	//Constructor
	public BankAccount(int amount)
	{
		every_last_penny = amount;
	}
	
	public int getAmount(){ return every_last_penny; }
	
	public synchronized void deposit(int amount)
	{
		every_last_penny = every_last_penny + amount;
	}
	
	public synchronized void withdraw(int amount)
	{
		every_last_penny = every_last_penny - amount;
	}
}