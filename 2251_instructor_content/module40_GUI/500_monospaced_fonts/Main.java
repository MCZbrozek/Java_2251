import javax.swing.JFrame;

public class Main
{
	public static void main(String[] args)
	{
		EmployeeDatabase database = new EmployeeDatabase();
		database.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		database.setSize(550, 350);
		database.setVisible(true);
	}
}