//Created with support from:
//https://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/
import java.util.concurrent.locks.ReentrantLock;

public class Tableware
{
	//https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReentrantLock.html
	private ReentrantLock knife = new ReentrantLock();
	private ReentrantLock fork = new ReentrantLock();
	
	private int blocked_count = 0;
	
	//Returns true if knife was successfully acquired
	public boolean pickupKnife()
	{
		//tryLock both attempts to acquire the lock and reports
		//back True if it was successful or False if not.
		boolean acquired = knife.tryLock();
		if(acquired)
			System.out.println(Thread.currentThread().getName()+" picked up knife.");
		else
			System.out.println(Thread.currentThread().getName()+" wants knife but can't have it.");
		return acquired;
	}
	
	public boolean iHaveKnife()
	{
		return knife.isHeldByCurrentThread();
	}
	
	public void putDownKnife()
	{
		System.out.println(Thread.currentThread().getName()+" puts down knife.");
		knife.unlock();
	}

	//Returns true if fork was successfully acquired
	public boolean pickupFork()
	{
		boolean acquired = fork.tryLock();
		if(acquired)
			System.out.println(Thread.currentThread().getName()+" picked up fork.");
		else
			System.out.println(Thread.currentThread().getName()+" wants fork but can't have it.");
		return acquired;
	}
	
	public boolean iHaveFork()
	{
		return fork.isHeldByCurrentThread();
	}
	
	public void putDownFork()
	{
		System.out.println(Thread.currentThread().getName()+" puts down fork.");
		fork.unlock();
	}
	
	private String whatsMissing()
	{
		if(!iHaveFork() && !iHaveKnife())
			return "knife and fork";
		if(!iHaveKnife())
			return "knife";
		if(!iHaveFork())
			return "fork";
		return "";
	}
	
	public boolean eat()
	{
		if(iHaveKnife() && iHaveFork())
		{
			System.out.println("Yum :) says "+Thread.currentThread().getName());
			return true;
		}
		else
		{
			System.out.println(Thread.currentThread().getName()+" can't eat :( doesn't have "+whatsMissing());
			blocked_count++;
			return false;
		}
	}
	
	public int getFailureCount()
	{
		return blocked_count;
	}
}