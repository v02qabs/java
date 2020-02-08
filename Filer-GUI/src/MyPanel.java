import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MyPanel extends JFrame
{
	public MyPanel(){}
	public void setting()
	{
		System.out.println("This is MyPanel.");
		setBounds(0,0,500,500);
		myparts();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Hello Filer GUI...");
		setVisible(true);
	}
	private void myparts()
	{
		Container content = getContentPane();
		content.add(new SetParts());
	}
}

