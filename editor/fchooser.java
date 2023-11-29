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
		System.setProperty("user.dir","/home/takesue090");
		model.addElement(new File(path).getAbsolutePath() + "/..");
		
		for(File f : list)
		{
			if(f.isDirectory())
			{
				System.out.println("[e] : " + f.getAbsolutePath());
				model.addElement(f.getAbsolutePath());
				list1.setModel(model);
				
				
			}
			else{
				System.out.println("[f] : " + f.getAbsolutePath());
				model.addElement(f.getAbsolutePath());
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

	private int a=0;
	private String[] dirpp = new String[100];
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if(e.getValueIsAdjusting())
		{
			try
		{
			System.out.println("ok.");
			System.out.println("get selection : " + list1.getSelectedValue());
			File f = new File(list1.getSelectedValue().toString());
			if(f.isDirectory()){
				System.out.println("is dir.");
				model.clear();
				repaint();
				Files(f.getAbsolutePath());
			}else if(f.isFile())
			{
			}
		}
		catch(Exception error)
		{}
		}


	
	}
}

