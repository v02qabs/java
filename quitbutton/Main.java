import javax.swing.*;
import java.awt.event.*;
public class Main implements ActionListener
{
	public static void main(String[] args)
	{
			new Main().start();
	}
	public Main()
	{
	}
	public void start()
	{
		System.out.println("Starting...");
		JFrame f = new JFrame();
		f.setTitle("Quit Button");
		JButton quit_button = new JButton("Quit");
		quit_button.setBounds(100,100,100,40);
		f.setBounds(300,100,200,200);
		f.setLayout(null);
		f.add(quit_button);
		f.show();
		quit_button.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e)
	{
		System.exit(0);
	}
	
}
