import javax.swing.event.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SetParts extends JPanel
{
	private JTextArea visible_position; //position.
	private JList file_list; //filelist.

	public SetParts()
	{
		include();
	}
	public void include()
	{
		setLayout(null);
		visible_position = new JTextArea();
		
		visible_position.setBounds(0,0,300,30);
		visible_position.setText("/home/user");

		//list
		file_list = new JList();
		file_list.setBounds(0,30,400,400);
		file_list.setModel(new ListItem());
		file_list.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				String obj = file_list.getSelectedValue().toString();
				System.out.println("/home/user/" + obj);
		new Select(visible_position).Append_Path(obj);
		new Select(visible_position).Selection(visible_position.getText().toString(), file_list);
			}
		});
		JScrollPane scroll = new JScrollPane();
		scroll.getViewport().setView(file_list);
		scroll.setBounds(0,40,400,400);
		//add container.
		add(visible_position);
		add(scroll);
	}
}

