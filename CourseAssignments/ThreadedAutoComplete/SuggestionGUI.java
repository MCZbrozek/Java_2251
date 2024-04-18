
/*
Name: Michael Zbrozek
Date: 04/07/2024
Purpose: See instructions below
Sources:
Instructor notes and recorded lecture

Files: 
Main.Java
ThreadOperations.java
SuggestionGUI.java

*/
/* INSTRUCTIONS
Your assignment is to complete the four TODOs
in this document, starting with the top TODO
and working your way down in order.

Then you must make AT LEAST ONE additional
modification of your choice from the following
list:

OPTIONS:

	---STOP ON EXCLAMATION
	Currently the getTextToPeriod method in 
	ThreadedAutocomplete searches for periods 
	or question marks and gathers all the 
	text from the current Scanner position up 
	to that punctuation and returns it. Modify 
	the method to also stop at exclamation marks.
	You can test it with the word "tiresome" and
	if you see: "tiresome unlucky ghost story!"
	with blanks in the other textareas, then you
	did it correctly.
	Also I find my code in the top half of the getTextToPeriod method to be gross so if you 
	manage to make it more elegant, I'll throw in 
	a couple extra credit points.

	---QUIT TO EXIT
	Currently typing the word "quit" into the
	JTextField causes the threads to exit. Make
	it so that "quit" also closes the GUI.

	---BREAK BETWEEN WORDS
	The wrappedToFit method in 
	ThreadedAutocomplete splits the text every 60
	characters regardless of whether or not the
	split occurs in the middle of a word. Modify
	this method so that words are not split up.

	---MULTIWORD MATCH
	Currently only the last word entered in the 
	text field is used for matching. Modify 
	ThreadedAutocomplete to attempt to match on 
	the entire user input, then if no matches are
	found, attempt to match on the whole input
	minus the first word, then if no matches are
	found, try the whole input minus the first
	two words. And so on until a match is found
	or the user input is exhausted. If you do
	this option I'll throw in 3 bonus points
	because I think it's harder than some of
	the other options.

	---THREE BUTTON OUTPUT
	Add three buttons to the GUI (one for each
	JTextArea) and resize the GUI to display them.
	When the user clicks a button, the current
	JTextField contents, followed by the 
	autocompleted text in the corresponding 
	JTextArea should be written out to file.
	Further clicks continue to APPEND to the file
	rather than overwriting it so that the user
	can create a mashup story from the three
	novels. If you do this option I'll throw in
	5 bonus points because I think it's harder
	than some of the other options.

	---SOMETHING NEW
	Propose your own extension and complete it.
	Your extension must be of comparable 
	difficulty to the other options and must be
	described in detail in a comment at the top of
	Main.java that also tells me, your instructor,
	where to look in the code to see this
	modification.

Note clearly in a comment at the top of
Main.java which modifications you made.

Small amounts of extra credit are available for
additional modifications beyond the required one
(up to 5 points per).
*/
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
//Extra imports for improved interface readability
import java.awt.Font;
import java.awt.Color;

public class SuggestionGUI extends JFrame {
	private JTextField enterField;

	/*
	 * TODO: Declare three private JTextArea
	 * instance variables.
	 */
	private JTextArea textArea1;
	private JTextArea textArea2;
	private JTextArea textArea3;

	/*
	 * Test that the code still runs and compiles
	 * after this. You should notice no changes
	 * at this point.
	 */

	// set up GUI
	public SuggestionGUI() {
		super("Autocomplete from novels");

		setLayout(null);

		enterField = new JTextField("Enter text here");
		enterField.setFont(new Font("Serif", Font.PLAIN, 32));
		enterField.setBackground(Color.blue);
		enterField.setForeground(Color.yellow);
		enterField.setBounds(10, 10, 800, 60);
		add(enterField);

		/*
		 * TODO:Create 3 JTextArea objects. Each of the
		 * JTextArea will display suggested
		 * autocomplete text for one of the three
		 * novels.
		 */
		textArea1 = new JTextArea();
		textArea2 = new JTextArea();
		textArea3 = new JTextArea();

		/* add them to this JFrame. */
		add(textArea1);
		add(textArea2);
		add(textArea3);
		/*
		 * Use the following setBounds commands for spacing and sizing
		 * the JTextAreas on the GUI:
		 * For the first display:
		 * setBounds(10,80,800,200);
		 * For the second:
		 * setBounds(10,300,800,200);
		 * For the third:
		 * setBounds(10,520,800,200);
		 */
		textArea1.setBounds(10, 80, 800, 200);
		textArea2.setBounds(10, 300, 800, 200);
		textArea3.setBounds(10, 520, 800, 200);

		/*
		 * Run and test your code. You should notice
		 * three new text areas on the GUI.
		 */

		setSize(840, 840); // set size of window
		setVisible(true); // show window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/*
		 * TODO: Instantiate three new
		 * ThreadedAutocomplete objects. All three
		 * will take the enterField as their second
		 * input to the constructor. Each thread
		 * will take a different JTextArea as their
		 * third input and a different novel as
		 * their first input from among:
		 * "Frankenstein.txt"
		 * "MobyDick.txt"
		 * "GreatExpectations.txt"
		 */
		ThreadedAutocomplete thread1 = new ThreadedAutocomplete("Frankenstein.txt", enterField, textArea1);
		// only thread1 calls the setSuggestionGUI method, which will pass an instance
		// of the SuggestionGUI class to the ThreadedAutocomplete class so that we have
		// access to the closeGUI() method. MZ
		thread1.setSuggestionGUI(this);
		ThreadedAutocomplete thread2 = new ThreadedAutocomplete("MobyDick.txt", enterField, textArea2);
		ThreadedAutocomplete thread3 = new ThreadedAutocomplete("GreatExpectations.txt", enterField, textArea3);
		/*
		 * Run and test your code. You should notice
		 * no new features at this point.
		 */

		/*
		 * TODO: Write code to start all three threads running concurrently.
		 */
		thread1.start();
		thread2.start();
		thread3.start();

		try {
			thread1.join();
			thread2.join();
			thread3.join();
		} catch (InterruptedException e) {
			System.out.println("Interrupted exception");
			e.printStackTrace();
			System.exit(1);
		}
		/*
		 * Run and test your code. You should notice text from the three novels
		 * appearing in
		 * the three text areas (unless there is no match in the novel with the most
		 * recent word).
		 */
	}

	/* !!! MZ ADDED THE FOLLOWING !!! */
	/*
	 * Source ChatGPT - prompt -
	 * "how can I have my ThreadedAutocomplete class close the GUI if my closeGUI() method is in SuggestionsGUI.java"
	 */
	public void closeGUI() {
		SwingUtilities.invokeLater(() -> {
			dispose(); // Dispose of the JFrame
		});
	}
}
