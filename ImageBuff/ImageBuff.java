import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.image.*;


public class  ImageBuff
{
	public static void main(String[] args)
	{
		System.out.println("Hello ImageBuff.");

		new Init();
	}
}

class Init extends JFrame
{
	private BufferedImage image;
	private JScrollPane pane;
	private JTextArea area;
	private JTextField field;
	
	public Init()
	{
		init();
	}
	public void init()
	{
		try
		{
			image = ImageIO.read(Init.class.getResource("icon.jpg"));
		}
		catch(Exception error)
		{
		}		
			
		setBounds(0,0,560,560);
		setLayout(new FlowLayout());
		field = new JTextField(20);
		area = new JTextArea(10,10);
		area.setLineWrap(true);
		pane = new JScrollPane(area);
		add(pane);
		add(field);
		setVisible(true);
		repaint();
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(image, 0,200,this);
	}
}

