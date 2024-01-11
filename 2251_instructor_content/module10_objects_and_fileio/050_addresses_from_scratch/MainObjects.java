public class MainObjects
{
	public static void main(String[] args)
	{
		Address[] addresses = {
			new Address(1,"Candelaria","ABQ","NM",87106),
			new Address(2,"Griegos","ABQ","NM",87107),
			new Address(3,"Lead","ABQ","NM",87108),
			new Address(5,"Coal","ABQ","NM",87109),
			new Address(4,"Silver","ABQ","NM",87110)
		};
		
		int index = 2;
		System.out.println("So and so lives at\n"+addresses[index]);
		System.out.println();
		
		//Swap last two elements
		Address temp = addresses[3];
		addresses[3] = addresses[4];
		addresses[4] = temp;
		
		//Print all
		for(int i=0; i<addresses.length; i++)
		{
			System.out.println(addresses[i]+"\n");
		}
	}
}