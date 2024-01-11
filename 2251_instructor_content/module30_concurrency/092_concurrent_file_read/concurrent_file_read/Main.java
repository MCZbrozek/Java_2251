
public class Main
{
	public static void main(String[] args) 
	{
		//Create and start threads
		TheCounter thread0 = new TheCounter("GreatExpectations.txt");
		TheCounter thread1 = new TheCounter("MobyDick.txt");
		TheCounter thread2 = new TheCounter("Frankenstein.txt");

		long start_time = System.nanoTime();
		thread0.start();
		thread1.start();
		thread2.start();

		//Wait for all threads to complete their tasks
		try {
			thread0.join();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted exception");
			e.printStackTrace();
			System.exit(1);
		}

		long duration = System.nanoTime() - start_time;
		System.out.printf("Computation took %.4f ms.%n", duration / 1000000.0);
		
		//Print out results
		System.out.println("Great Expectations has "+thread0.getCount()+" 'the's");
		System.out.println("Moby Dick has "+thread1.getCount()+" 'the's");
		System.out.println("Frankenstein has "+thread2.getCount()+" 'the's");
	}	
	
}