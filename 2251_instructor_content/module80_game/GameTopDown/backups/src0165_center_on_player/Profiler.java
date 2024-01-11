//THIS FILE WAS COPIED OVER FROM GAMEMINIMAL ON 4/1/2023

/* Measure game time spent drawing, computing,
 * and yielding in order to assess system 
 * performance. */
public class Profiler
{
	private static final boolean PRINT_PERIODICALLY = false;
	private long start_time;
	private long end_time;
	private boolean started = false;
	//Get the time of the first stopwatch start
	//so that the percent time can be calculated.
	private long first_start_time;
	private long[] buckets;
	private String[] labels;
	//Count the number of times each label is called.
	//This can be used to determine the number of 
	//function calls and estimate time it takes to run
	//the function once.
	//TODO this is not used at present, but the 
	//hiccups show basically average and worst case behavior
	//so maybe this is redundant.
	private int[] call_count;
	//Percent time devoted to drawing, computing, yielding.
	//Store the most recent calculated times.
	private double[] percentages;
	//Keep running averages of percent time spent in each bucket.
	private double[] running_average;
	//How long to wait before updating percentages
	private long reset_time;
	/* I started noticing hiccups in the game play as 
	 * if some frames took very long to compute or draw 
	 * and this was a way for me to narrow in on what 
	 * exactly was causing the problem.
	 * The data showed that more often drawing took too long
	 * rather than computing, but computing time had some 
	 * scarily large spikes.
	Track worst 100 times to determine where the 
	hiccups are coming from. By hiccups I mean that
	sometimes the game seems to skip or lag because,
	I assume, of particular frames taking a really long
	time. This will help isolate the problem. */
	private long[][] hiccups;
	//Ignore the first few timings because the
	//start of the game is wonky, stuff is being lodaded into
	//RAM and this is just generally not representative of the
	//game as a whole.
	private int skip_timings_count = 1500;
	
	public Profiler(String[] labels)
	{
		this.labels = labels;
		buckets = new long[labels.length];
		hiccups = new long[labels.length][100];
		percentages = new double[labels.length];
		running_average = new double[labels.length];
		call_count = new int[labels.length];
		//Initialize default value of zero for all of these.
		for(int i=0; i<labels.length; i++)
		{
			buckets[i] = 0;
			percentages[i] = 0;
			running_average[i] = 0;
			call_count[i] = 0;
			for(int j=0; j<100; j++)
			{
				hiccups[i][j] = 0;
			}
		}
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
		long elapsed_time = end_time - start_time;
		buckets[index] += elapsed_time;
		call_count[index]++;
		//Record data on abnormally long elapsed time
		skip_timings_count--;
		if(skip_timings_count < 0)
		{
			hiccups[index][0] = elapsed_time;
			long temp;
			//One pass of bubble sort will sort hiccups in
			//ascending order.
			for(int i=0; i<hiccups[index].length-1; i++)
			{
				if(hiccups[index][i] > hiccups[index][i+1])
				{
					temp = hiccups[index][i];
					hiccups[index][i] = hiccups[index][i+1];
					hiccups[index][i+1] = temp;
				}
			}
		}
		//Check if it is time to update the percentages and reset.
		if(end_time - first_start_time > reset_time)
		{
			started = false;
			printStats(PRINT_PERIODICALLY);
			reset();
		}
	}

	private void reset()
	{
		for(int i=0; i<buckets.length; i++)
		{ buckets[i] = 0; }
	}
	
	//Update the percentages and print the statistics
	//if print_stuff is true.
	private void printStats(boolean print_stuff)
	{
		long total_elapsed = System.nanoTime() - first_start_time;
		if(print_stuff){System.out.println("\nPercent Time Spent By Task");}
		for(int i=0; i<buckets.length; i++)
		{	//Save all the percentages
			percentages[i] = (double)(buckets[i]/1000000) / (double)(total_elapsed/1000000);
			//Update the running averages only after skipping
			//the first few updates.
			if(this.skip_timings_count < 0)
			{
				if(running_average[i] == 0){ running_average[i] = percentages[i]; }
				else{ running_average[i] = (running_average[i]+percentages[i])/2.0; }
			}
			//Print out the percentages
			if(print_stuff){System.out.println(labels[i]+": "+Utils.formatDecimal(percentages[i]));}
		}
		if(print_stuff)
		{
			System.out.println("Actual Time Spent By Task");
			for(int i=0; i<buckets.length; i++)
			{
				System.out.println(labels[i]+": "+Long.toString(buckets[i]/1000000)+" millis");
			}
		}
	}
	
	public String getProfileText(int index)
	{
		return labels[index]+Utils.formatDecimal(percentages[index])+"%";
	}
	
	public void printRunningAverages()
	{
		System.out.println("\nAverage Elapsed Time for this game:");
		for(int i=0; i<buckets.length; i++)
		{
			System.out.println(labels[i]+": "+Utils.formatDecimal(running_average[i]));
		}
	}
	
	public void printHiccupCSV()
	{
		System.out.println("\nHiccups:");
		for(int i=0; i<buckets.length; i++)
		{
			System.out.print(labels[i]+",");
			for(int j=0; j<hiccups[i].length; j++)
			{
				System.out.print(hiccups[i][j]+",");
			}
			System.out.println("");
		}
	}
}