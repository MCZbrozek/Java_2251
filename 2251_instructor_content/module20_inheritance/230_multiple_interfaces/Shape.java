public interface Shape {
	
	//Not allowed. Interfaces may only declare methods, not attributes.
	//protected int sides;
	
	//int sides = 0; //Allowed if instantiated.
	//Also note that the variable is (and must be) public and final!
	//public final String imAShape = "yes";
	
	public int getSides();
	public double calcPerimeter();
}