package hiro.take.MyApp;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame
{
	public Main()
	{
		initalize();
	}
	public static void main(String[] args)
	{	
		new Main();
	}
	public JFrame frame = new JFrame();
	public void initalize()
	{
		System.out.println("Hello.");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds( 0, 0, 500, 500);
		setTitle("Hello");	
		Parts(this);
		InitSetVisible();
	}
	public void InitSetVisible()
	{
		repaint();
		setVisible(true);
	}

	int count = 0;
	public JPanel panel = new JPanel();
	public JButton button;
	public JLabel l1 = new JLabel();
	public void Parts(JFrame frame)
	{
		//c = getContentPane();
		setLayout(null);
		
		button = new JButton("OK");
		button.setBounds(0,0,100,30);
		button.addActionListener(new ActionListener()	
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MRun mr = new MRun();
				mr.show("HelloTakesue");
			};
		});
		l1.setBounds(0,300,100,30);
		l1.setText("KeyCode");
		Container con1 = getContentPane();
		con1.add(button);
		con1.add(l1);
		addKeyListener(new KeyAdapter()
			{
				@Override
				public void keyTyped(KeyEvent e)
				{
					l1.setText("click");
				}

			});
	}
	public void Ini3()
	{
			repaint();
			setLayout(null);
			JButton bt[] = new JButton[3];
			for(int i=0; i<bt.length; i++)
			{
				bt[i] = new JButton("Hello2");
				bt[i].addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						button.setText("Hello0");
					}
				});
				bt[i].setBounds(0, i*30, 100, 30);
				add(bt[i]);
			}
	}

	public void Ini4()
	{
		repaint();
		count = count + 1;
		System.out.println(count);
		JButton b[] = new JButton[count];
		for(int i=0; i<b.length; i++)
		{
			b[i] = new JButton("TEST");
			b[i].setBounds(0,30+(i*30), 100,30);
			add(b[i]);
		}
	
	}

}
