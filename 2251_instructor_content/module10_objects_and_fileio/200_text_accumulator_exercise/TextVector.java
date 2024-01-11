import java.util.ArrayList;

public class TextVector
{
	private ArrayList<String> text = new ArrayList<String>();

	public void append(String sentence)
	{
		text.add(sentence);
	}
	
	//getters
	public String getText()
	{
		String temp = "";
		for(String s : text) //for each loop
		{
			temp = temp + s;
		}
		return temp;
	}
	public int getCount()
	{
		return text.size();
	}
}