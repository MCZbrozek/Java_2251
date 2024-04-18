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

import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.security.SecureRandom;

public class ThreadedAutocomplete extends Thread {
	private String fileName;
	private JTextField enterField;
	private JTextArea output;
	private String last_word;
	private SuggestionGUI suggestionGUI; // MZ Reference to suggestionGUI class in order to call the closeGUI() method

	private static final SecureRandom randGenerator = new SecureRandom();

	// Limit the array size
	private static final int SIZE_LIMIT = 100;

	// MZ added - allows me to pass the instance of SuggestionGUI to the thread so I
	// can call the closeGUI() method on line 82
	public void setSuggestionGUI(SuggestionGUI suggestionGUI) {
		this.suggestionGUI = suggestionGUI; // Set the SuggestionGUI instance
	}

	// Constructor
	public ThreadedAutocomplete(String fileName, JTextField enterField, JTextArea output) {
		this.fileName = fileName;
		this.enterField = enterField;
		this.output = output;
		this.suggestionGUI = suggestionGUI;
	}

	@Override
	public void run() {
		// Connect to the file.
		last_word = getLastWord();
		String current_word = "";
		String autocomplete;
		// Search for matches.
		while (!current_word.equals("quit")) {
			// System.out.println("Thread "+getName()+" is still alive.");

			// Wait until the word changes to reupdate
			waitOnNewWord(current_word);
			current_word = last_word;

			// Populate an ArrayList with autocomplete
			// options
			ArrayList<String> options = getOptions();

			// System.out.println(options.size()+" matches found for '"+last_word+"'");
			// //TESTING

			// Select a particular option to display.
			if (options.size() > 0) {
				// Choose a random option from the list
				int randomInt = randGenerator.nextInt(options.size());
				autocomplete = options.get(randomInt);
				autocomplete = wrappedToFit(autocomplete);
				output.setText(autocomplete);
			} else {
				output.setText("");
			}
		}
		System.out.println("Thread " + getName() + " exiting.");
		/* !!! MZ ADDED THE FOLLOWING !!! */
		suggestionGUI.closeGUI();
	}

	// Populate and return an ArrayList with
	// autocompleted options from the given
	// file.
	private ArrayList<String> getOptions() {
		String line;
		// Reset with an all new Scanner
		Scanner input = getNewScanner();

		// Create an array list of some options for
		// completing from the user's last word.
		ArrayList<String> options = new ArrayList<String>();
		String autocomplete = null;
		int i;
		// Find matches in one of the novels
		while (input.hasNextLine()) {
			line = input.nextLine();

			// TESTING
			/*
			 * if(line.indexOf(last_word) != -1)
			 * {
			 * System.out.println(line);
			 * System.out.println(line.indexOf(" "+last_word+" "));
			 * System.out.println();
			 * }
			 */

			// Only match with blank space on
			// both sides of the word:
			i = line.indexOf(" " + last_word + " ");
			if (i != -1) {
				autocomplete = getTextToPeriod(input, line.substring(i));
				options.add(autocomplete);
				// Limit ArrayList size
				if (options.size() > SIZE_LIMIT)
					break;
			}
		}
		input.close();
		return options;
	}

	// Loop and wait until the user changes the
	// most recent word entered.
	private void waitOnNewWord(String current_word) {
		// Wait until the word changes to reupdate
		while (last_word.equals(current_word)) {
			// System.out.println(last_word+" --- "+current_word); //TESTING
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted.");
			}
			// Get the last word the user typed in
			last_word = getLastWord();
		}
	}

	// Retrieve a new Scanner for accessing the
	// given file.
	private Scanner getNewScanner() {
		Scanner input = null;
		File file = new File(fileName);
		try {
			/*
			 * The use of "UTF-8" is 100%
			 * essential, otherwise weird file
			 * formatting issues cause the Scanner
			 * to incorrectly detect end of file
			 * in GreatExpectations and MobyDick
			 * but not in Frankenstein.
			 * Source of solution:
			 * https://stackoverflow.com/questions/8330695/java-scanner-not-going-through-
			 * entire-file
			 */
			input = new Scanner(file, "UTF-8");
		} catch (FileNotFoundException e) {
			System.out.printf("%nError on file: %s (either empty or wrong file format)%n%n", file);
			e.printStackTrace();
			System.exit(1);
		}
		return input;
	}

	// Insert line breaks into the given String
	// after every 60 characters then return
	// the result.
	private String wrappedToFit(String toFit) {
		// Add a line break every 60 characters.
		String toReturn = "";
		while (toFit.length() > 60) {
			toReturn += toFit.substring(0, 60) + "\n";
			toFit = toFit.substring(60);
		}
		return toReturn + toFit;
	}

	// Get all the text between the current
	// Scanner location and the next period
	// or question mark.
	private String getTextToPeriod(Scanner input, String remainder) {
		int i = remainder.indexOf(".");
		int j = remainder.indexOf("?");
		/* !!! MIKE ADDED THE FOLLOWING !!! */
		int k = remainder.indexOf("!");
		int x = i;
		// If no period is found then set x
		// to the index of any question mark.
		if (x == -1) {
			x = j;
			// if j ("?") isn't found, then set x to "!" - Mike Z
			if (x == -1) {
				x = k;
			}
		}
		// If a period is found and a question
		// mark is too, use the earlier
		// punctuation
		else if (j != -1 && (j < x || k < x)) {
			x = j;
		}

		// If a sentence-ender was found, return
		// the String up to and including the
		// punctuation.
		if (x != -1) {
			return remainder.substring(0, x + 1);
		}
		if (input.hasNextLine()) {
			// Recursion
			remainder += " " + getTextToPeriod(input, input.nextLine());
		}
		return remainder;
	}

	// Get all the text from the input field
	// and return it as a String array.
	private String[] getTextFieldInput() {
		String text = enterField.getText();
		text = text.toLowerCase();
		String[] word_array = text.split(" ");
		return word_array;
	}

	// Get and return the last word input by the
	// user.
	private String getLastWord() {
		String[] user_input = getTextFieldInput();
		return user_input[user_input.length - 1];
	}
}