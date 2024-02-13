class Main
{
	public static void main(String[] args)
	{
		Position coordinate = new Position();
		
		coordinate.distanceFromZero(3,4);
		
		System.out.println("The coordinate " + 
			coordinate.toString() + " is distance " + 
			coordinate.distanceFromZero(3,4) + 
			" away from the origin.");
		
	}
}