import java.lang.IllegalArgumentException;

public class Address
{
	private int number;
	private String street;
	private String city;
	private String state;
	private int zip;
	
	public Address(int number, String street, String city, String state, int zip)
	{
		if(number <= 0)
		{
			throw new IllegalArgumentException("Street number cannot be negative or zero.");
		}
		
		this.number = number;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	@Override
	public String toString()
	{
		return number+" "+street+"\n"+city+", "+state+" "+zip;
	}
	
	//Getters and setters
	public int getNumber()
	{
		return number;
	}
	public void setNumber(int number)
	{
		this.number = number;
	}
}