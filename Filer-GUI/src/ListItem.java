import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ListItem extends DefaultListModel
{
	public String HOME = System.getProperty("user.home"); //get home
	private JLabel label;
	private String CD;//Change dir:
	public ListItem(/*JLabel label*/)
	{
		System.out.println("ListItem...");
		try
		{

			addFirstItem(HOME);
		}
		catch(Exception error)
		{
			System.out.println("error.");
		}

	}
	private void addFirstItem(String HOME) throws Exception
	
	{
		System.out.println(HOME);
		File f = new File(HOME);
		String data[] = f.list();
		for(String data_c : data)
		{
			//System.out.println(data_c);
			addElement(data_c);
		}
	}
		
}


