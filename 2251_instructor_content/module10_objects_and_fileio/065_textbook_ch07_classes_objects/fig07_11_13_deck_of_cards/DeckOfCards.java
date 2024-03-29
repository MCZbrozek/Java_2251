// Fig. 7.12: DeckOfCards.java
// DeckOfCards class represents a deck of playing cards.
import java.security.SecureRandom;

public class DeckOfCards
{
	// random number generator
	private static final SecureRandom randomNumbers = new SecureRandom();
	
	// constant # of Cards
	private static final int NUMBER_OF_CARDS = 52;
	
	// Card references
	private Card[] deck = new Card[NUMBER_OF_CARDS];
	
	// index of next Card to be dealt (0-51)
	private int currentCard = 0;

	// constructor fills deck of Cards
	public DeckOfCards()
	{
		String[] faces = {"Ace", "Deuce", "Three", "Four", "Five", "Six","Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
		
		String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};

		// populate deck with Card objects
		for(int count=0; count<deck.length; count++)
		{
			deck[count] =
				new Card(faces[count % 13],
						 suits[count / 13]);
		}
		/* The potentially confusing for loop
		above is equivalent to the following,
		which might make more sense to 
		students who are uncomfortable with
		modular arithmetic and integer division.
		*/
		/*
		int index = 0;
		for(int f=0; f<faces.length; f++)
		{
			for(int s=0; s<suits.length; s++)
			{
				deck[index] = 
					new Card(faces[f],suits[s]);
				index++;
			}
		}
		*/
		
	}

	// shuffle deck of Cards with one-pass algorithm
	public void shuffle()
	{
		// next call to method dealCard should start 
		// at deck[0] again
		currentCard = 0;

		// for each Card, pick another random Card (0-51) 
		// and swap them
		for (int first = 0; first < deck.length; first++)
		{
			// select a random number between 0 and 51
			int second = randomNumbers.nextInt(NUMBER_OF_CARDS);

			// swap current Card with randomly selected Card
			Card temp = deck[first];
			deck[first] = deck[second];
			deck[second] = temp;
		}
	}

	// deal one Card
	public Card dealCard()
	{
		// determine whether Cards remain to be dealt
		if (currentCard < deck.length) {
			// return current Card in array
			return deck[currentCard++];
		}
		else {
			// return null to indicate that all Cards were dealt
			return null;
		}
	}
}


/**************************************************************************
 * (C) Copyright 1992-2018 by Deitel & Associates, Inc. and					*
 * Pearson Education, Inc. All Rights Reserved.									*
 *																								*
 * DISCLAIMER: The authors and publisher of this book have used their	  *
 * best efforts in preparing the book. These efforts include the			 *
 * development, research, and testing of the theories and programs		  *
 * to determine their effectiveness. The authors and publisher make		 *
 * no warranty of any kind, expressed or implied, with regard to these	 *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or		 *
 * consequential damages in connection with, or arising out of, the		 *
 * furnishing, performance, or use of these programs.							*
 *************************************************************************/
