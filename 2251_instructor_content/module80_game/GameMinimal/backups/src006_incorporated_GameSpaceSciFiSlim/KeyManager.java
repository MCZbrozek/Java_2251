public class KeyManager 
{
	public boolean[] ascii_input;
	private double mouse_x=0;
	private double mouse_y=0;
	private boolean mouse_pressed = false;
	private boolean mouse_clicked = false;
	public boolean print_key_code = false;

	//Check what keys have been released with this array.
	public boolean[] ascii_release;

	public KeyManager() 
	{
		ascii_input = new boolean[256];
		ascii_release = new boolean[256];
    }
	
	public void keyPressed(int key)
	{
		if(print_key_code){ System.out.println(key); }
		ascii_input[key] = true;
	}
	
	public void keyReleased(int key)
	{
		ascii_input[key] = false;
		ascii_release[key] = true;
	}
	
	//Check that a key was released and set its released indicator back to false
	public boolean checkKeyReleased(int key)
	{
		boolean temp = ascii_release[key];
		ascii_release[key] = false;
		return temp;
	}

	public void mouseClicked(){ mouse_clicked=true; }

	public boolean checkMouseClicked()
	{
		boolean temp = mouse_clicked;
		mouse_clicked = false;
		return temp;
	}
	
	public void mousePressed()
	{
		this.mouse_pressed = true;
	}

	public void mouseReleased()
	{
		this.mouse_pressed = false;
	}
	
	public boolean getMousePressed()
	{
		return this.mouse_pressed;
	}
	
	public void mouseMoved(int x, int y)
	{
		this.mouse_x = (double)x;
		this.mouse_y = (double)y;
	}
	
	public double getMouseX()
	{
		return this.mouse_x;
	}
	
	public double getMouseY()
	{
		return this.mouse_y;
	}
}