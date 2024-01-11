/*
Source:
www3.ntu.edu.sg/home/ehchua/programming/java/J5e_multithreading.html
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** Illustrate Unresponsive UI problem caused by 
"busy" Event-Dispatching Thread */
public class Ex01UnresponsiveUI extends JFrame
{
	private boolean stop = false;  // start or stop the counter
	private JTextField tfCount;
	private int count = 1;

	/** Constructor to setup the GUI components */
	public Ex01UnresponsiveUI()
	{
		Container cp = this.getContentPane();
		cp.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		cp.add(new JLabel("Counter"));
		tfCount = new JTextField(count + "", 10);
		tfCount.setEditable(false);
		cp.add(tfCount);

		JButton btnStart = new JButton("Start Counting");
		cp.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				stop = false;
				for (int i = 0; i < 100000; ++i) {
					if (stop) break;  // check if STOP button has been pushed
					tfCount.setText(count + "");
					++count;
				}
			}
		});
		JButton btnStop = new JButton("Stop Counting");
		cp.add(btnStop);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				stop = true;  // set the stop flag
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Counter");
		setSize(300, 120);
		setVisible(true);
	}

	/** The entry main method */
	public static void main(String[] args)
	{
		// Run GUI codes in Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Ex01UnresponsiveUI();  // Let the constructor do the job
			}
		});
	}
}