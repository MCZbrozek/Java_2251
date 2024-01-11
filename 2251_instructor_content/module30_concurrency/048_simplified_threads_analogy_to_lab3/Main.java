public class Main
{
	public static void main(String args[])
	{
		//Create threads and pass in data involved in the calculation
		ThreadDemo T1 = new ThreadDemo(7, 3);
		ThreadDemo T2 = new ThreadDemo(1, 5);

		T1.start();
		T2.start();
		
		//Wait on threads to finish.
		/*
		try {
			while(T1.isAlive() || T2.isAlive())
			{
				Thread.sleep(100);
			}
		} catch (InterruptedException ex) {
			System.out.println("Interrupted");
		}*/
		
		//Alternative way to wait on threads to finish.
		//Both ways to wait come from here:
		//www.tutorialspoint.com/javaexamples/thread_check.htm
		try{
			//join is the counterpart to fork in C!
			T1.join();
			T2.join();
		}catch(InterruptedException e){
			System.out.println("Interrupted");
		}
		
		//Get results back. Process them if needed.
		System.out.println(T1.getResult());
		System.out.println(T2.getResult());
	}
}