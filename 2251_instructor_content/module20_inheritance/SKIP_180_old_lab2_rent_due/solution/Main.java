import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.util.Arrays;

public class Main
{
	//method will return the total number of lines in the text file
	public static int numProperty(Path rentalDB)
	{
		int count = 0;//int to count total number of properties in DB
		try { 
			Scanner scanner = new Scanner(rentalDB);
			while (scanner.hasNextLine())
			{
				count += 1;
				scanner.nextLine();
			}
			scanner.close();
		}
		catch (IOException e) {
			System.out.println("Caught an ioException");
		}
		return count;
	}
	
	public static void main(String[] args)
	{
		Path rentalDB = Paths.get("rentalDB.txt");
		int count = numProperty(rentalDB);//get total number of lines from the DB.txt file
		
		//create an array of the superclass to hold the various elements
		RentalProperty[] rentals = new RentalProperty[count];
		int sequenceNumber;
		String rentalType;
		String rentalID;
		int bedrooms;
		float rent;
		
		//read the .txt file and fill the array
		try {
			Scanner reader = new Scanner(rentalDB);
			for(int i=0; i<count; i++)//use for loop because I know the count, works with array better
			{
				sequenceNumber = reader.nextInt();
				rentalType = reader.next();
				rentalID = reader.next();
				bedrooms = reader.nextInt();
				rent = reader.nextFloat();
				if (rentalType.equals("A")) {
					rentals[i] = new ApartmentRental(sequenceNumber, rentalType, rentalID, bedrooms, rent);
				}
				else {
					rentals[i] = new SingleFamilyRental(sequenceNumber, rentalType, rentalID, bedrooms, rent);
				}
			}
			reader.close();
		}
		catch (IOException e) {
			System.out.println("Caught an IOException");
		}
		
		try
		{
			FileWriter fileWriter = new FileWriter("rentalDB.txt");
			for (int i=0; i<rentals.length; i++) {
				rentals[i].getRent();
				fileWriter.write(rentals[i].updateRent());
			}
			fileWriter.close();
		}
		catch(IOException e) {
			System.out.println("Caught an IOException");
		}
		
		//sort the arrays using comparable interface 
		Arrays.sort(rentals);
		
		System.out.printf("%nSingle-Family rental Summary:%nHouse ID No.             Rental Due%n");
		System.out.printf("================         ==========%n");
		for (int i=0; i<rentals.length; i++) {
			if (rentals[i].rentalType.equals("S")) {
				System.out.println(rentals[i].toString());
			}
		}
		System.out.printf("%nApartment rental Summary:%nApartment ID No.         Rental Due%n");
		System.out.printf("================         ==========%n");
		for (int i=0; i<rentals.length; i++) {
			if (rentals[i].rentalType.equals("A")) {
				System.out.println(rentals[i].toString());
			}
		}
	}
}