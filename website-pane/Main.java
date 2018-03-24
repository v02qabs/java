import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.net.*;

public class Main extends JFrame
{
	public JTextField utf;
	public JEditorPane webp;
	public JScrollPane scpan;
	public JPanel panel;
	public JButton button1;

	public static void main(String[] args)
	{
		System.out.println("Hello");
		new Main();
	}

	public Main()
	{
		super("Main");
		utf = new JTextField();
		webp = new JEditorPane();
		scpan = new JScrollPane(webp);
		panel = new JPanel();
		button1 = new JButton("load...");

		panel.add(button1);
		add(utf, BorderLayout.NORTH);
		add(webp, BorderLayout.CENTER);
		add(panel, BorderLayout.SOUTH);

		button1.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						try
						{
							URL url = new URL(utf.getText());
							webp.setPage(url);
						}
						catch(Exception error)
						{
							System.out.println("error");
						}

					}
				});

		addWindowListener(new CloseWindowListener());
		setSize(600,600);
		setVisible(true);

		utf.requestFocus();
	}
}

class CloseWindowListener extends WindowAdapter
{
	public void windowCloseing(WindowEvent e)
	{
		System.exit(0);
	}
}




