//ImageVisible.java

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.Random;

public class ImageVisible extends Applet implements Runnable
{
	Image img1;
	int x,y,vx,vy;
	public void init()
	{	
			img1 = getImage(getClass().getResource("mogura.gif"));
			//enableEvents(AWTEvent.MOUSE_EVENT_MASK);
			setBounds(0,0,200,200);
			System.out.println("a");
			//new Rand(new Random(), x).start();
			new Thread      (this).start();
	} 
	public void paint(Graphics g)
	{                                           
		g.drawImage(img1,x,0,this);
		
	}
	private boolean flag = false;
	public void processMouseEvent(MouseEvent e)
	{
		//System.out.println("click");
		    int clickX = e.getXOnScreen();
    int clickY = e.getYOnScreen();

				System.out.println(clickX);
	}
	
		Random r;
/*
	
	public void run()
{
	for(int i=0; i<100; i++)
	{
	r = new Random();
		x = r.nextInt(100);
		sleep();
		System.out.println(x);
		repaint();
		
	}
}
*/


	public void run()
	{
		while(true)
		{	
				x = x +1;
				System.out.println("x " + x);
				sleep();
				repaint();
				if(x == 200)
					break;
	}
}
				
				
	private void sleep()
	{
			try
			{
					new Thread().sleep(80);
			}
			catch(Exception error)
			{}
	}
	
	
}

