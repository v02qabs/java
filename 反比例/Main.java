import java.awt.*;
import  java.applet.*;
import java.awt.event.*;

public class Main extends Applet //implements Runnable
{
	Point  pt1,pt2;
	Point apt[] = new Point[500];
	int ipt = 0;
	boolean bool = true;
	
	
	public void init()
	{
		setBounds(0,0,600,600);
		//new Thread(this).start();
	}
	int x,yp = 200;
	int xp = 100; 
	
	
 public void paint(Graphics g)
 {
 	if(bool)
 		{
 				int width= pt2.x - pt1.x;
 				int height = pt2.y - pt1.y;
 				for(int i=0;i<ipt-1; i++)
 				{
 						g.drawLine(apt[i].x, apt[i].y, apt[i+1].x, apt[i+1].y);
 				}
 		}
 }
 

		

		
}

