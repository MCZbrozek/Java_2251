public class TextAccumulator
{
	private String text = "";
	
	/* TODO: Create a private integer variable
	named count */
	

	public void append(String sentence)
	{
		text = text + sentence; //append
		//TODO: increment count by one.
		
	}
	
	//getters
	public String getText(){ return text; }
	//TODO: Change the following method to return count.
	public int getCount(){ return 0; }
}