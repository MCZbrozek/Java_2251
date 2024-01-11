/* For reference:
https://staff.fnwi.uva.nl/a.j.p.heck/Courses/JAVAcourse/ch3/s1.html
*/
public class Main
{
	public static void main(String[] args)
	{
		Character punctuation = new Character("apostrophe", "'");
		System.out.println("Here is punctuation:");
		punctuation.draw();
		
		System.out.println();
		
		Character c = new Character("letter N", "N");
		System.out.println("Here is c:");
		c.draw();
		c.italicize();
		System.out.println("Here is c italicized:");
		c.draw();
		
		
	}
}