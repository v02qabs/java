import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;


public class ImageVisible extends  Frame implements Runnable
{
	public  ImageVisible()
	{
		image_visible();
	}
	private Thread th; private int imgnum = 0;
	public static void main(String[] args)
	{
		System.out.println("Hello");
		new ImageVisible();
		
	}
	private void image_visible()
	{
		setTitle("HelloImage");
		setSize(500,500);
		image_load();
		th = new Thread(this);
		th.start();
		setVisible(true);

	}
	Image image;
	private void image_load()
	{
		 
	}
	public void paint(Graphics g)
	{
		g.drawImage(image, 100,100,this);
	}
	public void run()
	{
	while(true)
	{
		imgnum = 0;
		image = Toolkit.getDefaultToolkit().getImage("icon" + imgnum + ".gif");
		System.out.println("NUM: " + imgnum);
		//repaint();
		imgnum = 1;
		
		try
		{

			Thread.sleep(5);

		}
		catch(Exception error)
		{}

			if(imgnum == 1)
			break;
	}
	}


}

