/* Measure game time spent drawing, computing,
 * and yielding in order to assess system 
 * performance. */
public class Profiler
{
	private long start_time;
	private long end_time;
	private boolean started = false;
	//Get the time of the first stopwatch start
	//so that the percent time can be calculated.
	private long first_start_time;
	private long[] buckets;
	public static final int DRAW_INDEX = 0;
	public static final int COMPUTE_INDEX = 1;
	public static final int YIELD_INDEX = 2;
	private static final String[] labels = {"Drawing    ", "Computing", "Yielding    "};
	//Percent time devoted to drawing, computing, yielding.
	//Store the most recent calculated times.
	private double[] percentages = {0,0,0};
	//How long to wait before updating percentages
	private long reset_time;
	
	public Profiler()
	{
		buckets = new long[YIELD_INDEX+1];
		//Update roughly every 3 seconds.
		reset_time = 1000000000; //1 second
		reset_time = reset_time*3; //3 seconds
	}
	
	public void start()
	{
		start_time = System.nanoTime();
		if(!started)
		{
			started = true;
			first_start_time = start_time;
		}
	}
	
	public void end(int index)
	{
		end_time = System.nanoTime();
		buckets[index] += end_time - start_time;
		//Check if it is time to update the percentages and reset.
		if(end_time - first_start_time > reset_time)
		{
			started = false;
			printStats();
			reset();
		}
	}

	private void reset()
	{
		for(int i=0; i<buckets.length; i++)
		{ buckets[i] = 0; }
	}
	
	private void printStats()
	{
		long total_elapsed = System.nanoTime() - first_start_time;
		System.out.println("\nPercent Time Spent By Task");
		for(int i=0; i<buckets.length; i++)
		{
			//Save all the percentages
			percentages[i] = (double)(buckets[i]/1000000) / (double)(total_elapsed/1000000);
			//Print out the percentages
			System.out.println(labels[i]+": "+Utils.formatDecimal(percentages[i]));
		}
		System.out.println("Actual Time Spent By Task");
		for(int i=0; i<buckets.length; i++)
		{
			System.out.println(labels[i]+": "+Long.toString(buckets[i]/1000000)+" millis");
		}
	}
	
	public String getProfileText(int index)
	{
		return labels[index]+Utils.formatDecimal(percentages[index])+"%";
	}
}