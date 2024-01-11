/* If you want neatly aligned output, you need to use monospaced fonts.
*/
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.Font;

public class EmployeeDatabase extends JFrame
{
	private final JTextArea textArea;
	private final JButton toggleFontButton;
	private boolean fontMonospaced = false;
	
	public EmployeeDatabase()
	{
		BoxLayout boxlayout = new BoxLayout(
				getContentPane(),
				BoxLayout.PAGE_AXIS);
		getContentPane().setLayout(boxlayout);

		textArea = new JTextArea("TextArea");
		textArea.setEditable(false);
		String str = 
			String.format(" %-8s%-8s%8s\n","Year","Name","Number") + 
			String.format(" %-8d%-8s%8d\n",1111,"Jayne",46) +
			String.format(" %-8d%-8s%8d\n",8347,"Mal",1) +
			String.format(" %-8d%-8s%8d\n",3491,"Inara",9283) +
			String.format(" %-8d%-8s%8d\n",982,"River",82);
		textArea.setText(str);
		add(textArea);

		toggleFontButton = new JButton("Plain Button");
		add(toggleFontButton); // add toggleFontButton to JFrame

		toggleFontButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event)
				{
					toggleFont();
				}
			}
		);
		
		//Toggle the font initially
		//because neither of the fonts
		//I used is the default font.
		toggleFont();
	}
	
	private void toggleFont()
	{
		/* IMPORTANT
"Courier New" is what's called a monospaced font. 
This means that every character has the same width.
The default font is NOT monospaced and that makes it
virtually impossible to format your output in a neatly
organized table.
See the code below such as
String.format("%-25s", temp);
*/
		if(fontMonospaced)
		{
			textArea.setFont(new Font("Courier New", Font.BOLD, 32));
		}
		else
		{
			textArea.setFont(new Font("Serif Plain", Font.PLAIN, 32));
		}
		fontMonospaced = !fontMonospaced;
	}
}

