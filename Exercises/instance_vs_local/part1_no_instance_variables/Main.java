class Main
{
	public static void main(String[] args)
	{
		Position coordinate = new Position();
		
		System.out.println("The coordinate " + 
			coordinate.toString(3,4) + " is distance " + 
			coordinate.distanceFromZero(3,4) + 
			" away from the origin.");
		
	}
}