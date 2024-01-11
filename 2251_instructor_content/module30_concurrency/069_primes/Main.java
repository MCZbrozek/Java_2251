/*

*/
public class Main
{
	public static void main(String args[])
	{
		PrimeFinder T1 = new PrimeFinder("Thread T1");
		PrimeFinder T2 = new PrimeFinder("Thread T2");
		
		//Start the threads
		T1.start();
		T2.start();

		T1.setPriority(1);
		T2.setPriority(10);

		try{
			T1.join();
			T2.join();
		}catch(InterruptedException e){
			System.out.println("Interrupted");
		}
		System.out.println("Finished in Main");
	}
	
}
