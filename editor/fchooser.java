import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class fchooser extends JFrame
{
	public static void main(String[] args)
	{
		System.out.println("Hello");
		new fchooser();
	}
	public fchooser()
	{
		init();
	}
	JPanel all_panel;
	JList list;
	private void init()
	{
		all_panel = new JPanel();
		list = new JList();
		all_panel.add(list);
		setLayout(all_panel, new GridLayout(1,1));
		setVisible(true);
	}

	
}

