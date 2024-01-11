public class MainArrays
{
	public static void main(String[] args)
	{
		int[] numbers = {1,2,3,5,4};
		String[] streets = {"Candelaria","Griegos", "Lead", "Coal", "Silver"};
		String[] cities = {"ABQ", "ABQ", "ABQ", "ABQ", "ABQ"};
		String[] states = {"NM", "NM", "NM", "NM", "NM"};
		int[] zips = {87106,87107,87108,87109,87110};
		
		int index = 2;
		System.out.println("So and so lives at\n"+addressToString(index,numbers,streets,cities,states,zips));
		System.out.println();
		
		//Swap last two elements
		int temp = numbers[3];
		numbers[3] = numbers[4];
		numbers[4] = temp;
		String tempStr = streets[3];
		streets[3] = streets[4];
		streets[4] = tempStr;
		tempStr = states[3];
		states[3] = states[4];
		states[4] = tempStr;
		tempStr = cities[3];
		cities[3] = cities[4];
		cities[4] = tempStr;
		temp = zips[3];
		zips[3] = zips[4];
		zips[4] = temp;
		
		//Print all
		for(int i=0; i<streets.length; i++)
		{
			System.out.println(addressToString(i,numbers,streets,cities,states,zips)+"\n");
		}
		
	}
	
	public static String addressToString(int i, int[] numbers, String[] streets, String[] cities, String[] states, int[] zips)
	{
		return numbers[i]+" "+streets[i]+"\n"+cities[i]+", "+states[i]+" "+zips[i];
	}
}