class Main
{
	public static void main(String[] args)
	{
		Position coordinate = new Position(3,4);
		
		System.out.println("The coordinate " + 
			coordinate.toString() + " is distance " + 
			coordinate.distanceFromZero() + 
			" away from the origin.");
		
	}
}