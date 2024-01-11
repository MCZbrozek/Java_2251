/* Source:
https://staff.fnwi.uva.nl/a.j.p.heck/Courses/JAVAcourse/ch3/s1.html
*/
public class Character
{
	protected String font = "Times New Roman";
	protected int fontsize = 12;
	protected String name = "";
	protected String value = "";
	private boolean italicized = false;
	
	//Constructor
	public Character(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	//Constructor
	public Character(String font, int fontsize, String name, String value)
	{
		this.font = font;
		this.fontsize = fontsize;
		this.name = name;
		this.value = value;
	}
	
	public void italicize()
	{
		italicized = !italicized;
	}

	public void draw()
	{
		String output = "<style fontsize="+fontsize+";font=\""+font+"\">";
		if(italicized)
			System.out.println(output+"<i>"+value+"</i></style>");
		else
			System.out.println(output+value+"</style>");
	}
	
	public void rename(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
}