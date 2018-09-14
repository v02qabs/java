import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class item extends Frame
{
	public static void main(String[] args)
	{
		System.out.println("Hello");
		new item();
	}
	public item()
	{
		initalize();
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public void initalize()
	{
		setBounds(0,0,500,500);
		setVisible(true);
		
	}

		
}
