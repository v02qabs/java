import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class main
{
	public main()
	{
		new winpanel();
	}
	public static void main(String[] args)
	{
		System.out.println("Hello.\n");
		new winpanel();

	}
}


class winpanel extends JFrame implements ActionListener
{
	public winpanel()
	{
		setBounds(0,0,500,500);
		setTitle("jpad ver 0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		past();
		setVisible(true);
	}
	private JTextArea edit;
	private JButton save;
	private void  past()
	{
		edit = new JTextArea();
		save = new JButton("save");
		save.addActionListener(this);		
		JPanel all_panel = new JPanel();	
		JPanel control_panel = new JPanel();
		GridLayout control = new GridLayout(1,2);	
		GridLayout layout = new GridLayout(2,1);
		control_panel.setLayout(control);
		all_panel.add(control_panel);
		control_panel.add(save);
		all_panel.setLayout(layout);
		
		all_panel.add(control_panel);
		all_panel.add(edit);
		Container  contents = getContentPane();
		contents.add(all_panel);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("typed subject.");
		if(e.getSource() == save)
			save();
		else
			error();

	}

	private void save()
	{
	try{	System.out.println("save");
		String sub = edit.getText().toString();
		System.out.println("g : " + sub);
		File save_file = new File("./log");
		BufferedWriter writer = new BufferedWriter(new FileWriter(save_file));
		writer.write(sub);
		writer.close();
	}
	catch(Exception error)
	{error();}
	}
	private void error()
	{
		System.out.println("error");
	}
}

