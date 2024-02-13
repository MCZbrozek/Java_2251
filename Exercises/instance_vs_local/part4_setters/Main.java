class Main
{
	public static void main(String[] args)
	{
		Position coordinate = new Position();
		
		coordinate.setX(3);
		coordinate.setY(4);
		
		System.out.println("The coordinate " + 
			coordinate.toString() + " is distance " + 
			coordinate.distanceFromZero() + 
			" away from the origin.");
		
	}
}