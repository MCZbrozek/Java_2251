
public class MainTopDown
{
	public static void main(String[] args)
	{
		System.out.println("Run from the classes folder with this:");
		System.out.println("> java -cp ..\\..\\GameMinimal\\classes; MainTopDown");
		System.out.println("or include a number to use as the random seed, or the hardmode flag with obstacles to be avoided, or a time limit in seconds, for instance:");
		System.out.println("> java -cp ..\\..\\GameMinimal\\classes; MainTopDown -seed 4728 -hardmode -timelimit 60");

		//Set random seed or not
		int i = indexOf(args, "-seed");
		long seed = -1;
		if(i!=-1)
			seed = Long.parseLong(args[i+1]);
		
		//Set hardmode or not
		i = indexOf(args, "-hardmode");
		boolean hardmode = i!=-1;

		//Set time limit or not
		i = indexOf(args, "-timelimit");
		int timeLimit = -1;
		if(i!=-1)
			timeLimit = Integer.parseInt(args[i+1]);

		BoardTopDown b = new BoardTopDown(seed, hardmode, timeLimit);
		
		Main m = new Main(b, true);
		Main.SHOW_COLLISION = false;
		m.runGameLoop();
	}
	
	
	private static int indexOf(String[] args, String target){
		for(int i=0; i<args.length; i++){
			if(args[i].equals(target))
				return i;
		}
		return -1;
	}
}
