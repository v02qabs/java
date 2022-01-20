import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class fchooser extends JFrame
{
	public static void main(String[] args)
	{
		System.out.println("Hello");
		String path = args[0];
		new fchooser(path);
	}
	public fchooser(String path)
	{
		init(path);
	}
	JPanel all_panel;
	JList list;
	DefaultListModel model;
	GridLayout layout;
	private void init(String path)
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0,800,800);
		all_panel = new JPanel();
		list = new JList();
		model = new DefaultListModel();
		model.addElement("one");
		list.setModel(model);
		all_panel.add(list);
		layout = new GridLayout(1,0);
		all_panel.setLayout(layout);
		Container content = getContentPane();
		content.add(all_panel);
		Files(path);
		setVisible(true);
	}
	File ldf;
	File now;
	private void Files(String path)
	{
		//System.out.println("ok.");
		now = new File(path);
		File[] list = now.listFiles();
		if(list == null)
			return ;

		for(File f : list)
		{
			if(f.isDirectory())
			{
				System.out.println("Dir" + f.getName());
				//Files(f.getAbsolutePath());
				//Files(f.getPath());
				JList list2 = new JList();
				model.addElement("dir : " + f.getName().toString());
				list2.setModel(model);
			}
			else
			{
				System.out.println("File " + f.getName());
				//model = new DefaultListModel();
				model.addElement("file : " + f.getName().toString());
				JList list1 = new JList();
				//list.addElement(f.getName());
				list1.setModel(model);
			}
		}
	}
}

