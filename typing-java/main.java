import  java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class main extends JFrame implements KeyListener
{
	JLabel label;
	int loc;
	String word_string = "hello,string";
	char word[];
	public static  void main(String[] args)
		{
				System.out.println("hello.");
				new main().init();
		}
		@Override
		public void keyTyped(KeyEvent e)
		{}
		@Override
		public  void keyReleased(KeyEvent e)
		{}
		@Override
		public void keyPressed(KeyEvent e)
		{
			label.setText(String.valueOf(e.getKeyChar()));

			String[] a_array = word_string.split("");

			try
			{
			String str1 = String.valueOf(e.getKeyChar());
			System.out.println("sa "  + a_array[loc]);
			System.out.println("str " +  str1);
			loc++;
			String str2 = a_array[loc-1];
			System.out.println(str2);
			if(!str1.equals(str2))
			System.out.println("NG");

			System.out.println("OK");

			}
			catch(Exception error)
			{
				System.out.println("error");
			}

		}
		private void setWindow()
		{
			System.out.println("setwindow");
			setBounds(0,0,500,500);
			setTitle("Typinggame");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(null);
			addKeyListener(this);
		}

		private void init()
		{
			System.out.println("init");
			setWindow();
			label = new JLabel("red");
			label.setBounds(0,0,100,30);
			label.setText("Hello");
			Container container = getContentPane();
			container.add(label);
			setVisible(true);
		}
		private JLabel visible_ptag()
		{
			JLabel label = new JLabel();
			label.setBounds(0,0,100,30);
			return label;
		}	
		private void text_code(KeyEvent e)
		{
			System.out.println("text code()");
			String str1[] = word_string.split("i");
			System.out.println("OK");
			
			
			
			
			
			
			
		}		
}
