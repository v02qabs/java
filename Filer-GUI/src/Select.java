import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class Select
{
	private JTextArea label;
	private String HOME = "/home/user";
	private JList list;

	public Select(JTextArea label)
	{
		this.label = label;

	}
	public String Append_Path(String obj)
	{
	String path =	this.label.getText().toString();
	System.out.println("obj = " + path + "/" + obj);
	this.label.append("/" + obj);
	return this.label.getText().toString();
	}
	public void Selection(String path, JList list)
	{
	this.list = list;
		try
		{
			File f = new File(path);
			if(f.isDirectory())
			{
				System.out.println("isdir.");

				File f0 = new File(this.label.getText().toString());
			String data1[] = f0.list();
			DefaultListModel model = new DefaultListModel();
			for(String d1 : data1)
			{
				model.addElement(d1);	
			}
			this.list.setModel(model);

		}

		}
		catch(java.lang.NullPointerException error)
		{
		}
	}

}

