public class ThreadedDeposit extends Thread
{
	private BankAccount account;

	//Constructor
	public ThreadedDeposit(BankAccount account)
	{
		this.account = account;
	}

	@Override
	public void run()
	{
		//Deposit 1 million nickels
		for(int i=0; i<1000000; i++)
			this.account.deposit(5);
	}
}