import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
	GridLayout layout;
	private void init()
	{
		setBounds(0,0,800,800);
		all_panel = new JPanel();
		list = new JList();
		all_panel.add(list);
		layout = new GridLayout(1,0);
		all_panel.setLayout(layout);
		Container content = getContentPane();
		content.add(all_panel);
		setVisible(true);
	}

	
}

