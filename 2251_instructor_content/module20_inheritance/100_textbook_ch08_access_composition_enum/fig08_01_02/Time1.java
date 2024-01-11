// Fig. 8.1: Time1.java
// Time1 class declaration maintains the time in 24-hour format.

public class Time1
{
	private int hour; // 0 - 23
	private int minute; // 0 - 59
	private int second; // 0 - 59

	// set a new time value using universal time; throw an
	// exception if the hour, minute or second is invalid
	public void setTime(int hour, int minute, int second)
	{
		// validate hour, minute and second
		if (hour < 0 || hour >= 24 || minute < 0 || minute >= 60 || second < 0 || second >= 60)
		{
			throw new IllegalArgumentException(
				"hour, minute and/or second was out of range");
		}

		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	// convert to String in universal-time format (HH:MM:SS)
	public String toUniversalString()
	{
		//String.format is used in a similar manner to printf
		//System.out.printf("%02d:%02d:%02d", hour, minute, second);
		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	// convert to String in standard-time format (H:MM:SS AM or PM)
	public String toString()
	{
		//Get the hour in a 1 through 12 format
		String to_return = "";
		if(hour == 0 || hour == 12)
			to_return += 12;
		else
			to_return += hour % 12;
		//Get formatted minutes and seconds
		to_return += String.format(":%02d:%02d ",minute, second);
		//Get the AM or PM
		if(hour < 12)
			to_return += "AM";
		else
			to_return += "PM";
		//Return results
		return to_return;
		
		//This is the book's way of doing it:
		/*
		return String.format("%d:%02d:%02d %s",
			((hour == 0 || hour == 12) ? 12 : hour % 12),
			minute, second, (hour < 12 ? "AM" : "PM"));
		*/
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