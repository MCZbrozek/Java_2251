//Story source: https://www.dltk-teach.com/rhymes/littlered/story.htm
public class Main
{
	public static void main(String args[])
	{
		System.out.println("\n");

		String[][] story = {
			{"Once", "upon", "a", "time,", "there", "was", "a", "little", "girl"},
			{"who", "lived", "in", "a", "village", "near", "the", "forest."},
			{"Whenever", "she", "went", "out,", "the", "little", "girl", "wore", "a", "red", "riding", "cloak,"},
			{"so", "everyone", "in", "the", "village", "called", "her", "Little", "Red", "Riding", "Hood."}
			};
		
		StoryTeller T1 = new StoryTeller(story[0]);
		StoryTeller T2 = new StoryTeller(story[1]);
		StoryTeller T3 = new StoryTeller(story[2]);
		StoryTeller T4 = new StoryTeller(story[3]);

		//Start the threads
		T1.start();
		T2.start();
		T3.start();
		T4.start();

		//Wait on threads to finish.
		try{
			T1.join();
			T2.join();
			T3.join();
			T4.join();
		}catch(InterruptedException e){
			System.out.println("Interrupted");
		}
		
		System.out.println("\nFINISHED\n");
	}
	
}
