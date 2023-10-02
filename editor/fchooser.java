import java.io.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;


public class fchooser extends JFrame implements ListSelectionListener
{	
	public counter c;
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
	JList list1;
	DefaultListModel model;
	GridLayout layout;
	String gpath;
	private void setPath(String path1)
	{
		this.gpath = path1;
	}
	private String getPath()
	{
		return this.gpath;
	}

	private void init(String path)
	{
		setPath(path);
		System.setProperty("user.dir", path);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0,0,800,800);
		all_panel = new JPanel();
		list1 = new JList();
		list1.addListSelectionListener(this);
		model = new DefaultListModel();
		model.addElement("..");
		list1.setModel(model);
		all_panel.add(list1);
		layout = new GridLayout(1,0);
		all_panel.setLayout(layout);
		Container content = getContentPane();
		content.add(all_panel);
		Files(path);
		setVisible(true);
	}
	File ldf;
	File now;
	private void Junp()
	{
		System.out.println("go to .");
	}

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
				System.out.println("Dir : " + f.getName());
				//Files(f.getAbsolutePath());
				//Files(f.getPath());
				JList list2 = new JList();
				list1.addListSelectionListener(this);
				model.addElement(f.getName().toString());
				list2.setModel(model);
			}
			else
			{
				System.out.println("File " + f.getName());
				//model = new DefaultListModel();
				model.addElement(f.getName().toString());
				JList list1 = new JList();
				//list.addElement(f.getName());
				list1.setModel(model);
			}
		}
	}
	private String Object;

	private void setString(String objs)
	{
		this.Object = objs;
	}
	private String getString()
	{
		return this.Object;
	}

	private boolean checkobj(String objs)
	{
		setString(objs);
		if(getString() != null)
		{
			System.out.println("written");
				
			return true;
		}
		else
		{
			System.out.println("no written");
		}
		return false;
	}

	private int a=0;
	private String[] dirpp = new String[100];
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if(e.getValueIsAdjusting())
		{
			return ;
		}
		//a = a+1;
		Object obj = list1.getSelectedValue();
		System.out.println("getpath : " + System.getProperty("user.dir") + obj.toString());
		File dir_check = new File(System.getProperty("user.dir") + obj.toString());
		if(dir_check.isFile())
		{
			System.out.println("this is file.");
			
		}
		else
		{
			System.out.println("this is dir");
			c = new counter();
			c.write(dir_check.getName());
		}

	}

}

