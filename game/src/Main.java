import java.util.Timer;
import java.util.TimerTask;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JFrame
{
	private Image image;
	private Graphics g;
	private int x,y;
	public static void main(String[] args)
	{
			System.out.println("Hello");
			new Main();
	}

	public Main()
	{
		setBounds(0,0,900,900);
		setTitle("game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("mycar.png"));
		while(true)
		{
			x++;
		Timer timer = new Timer();
		timer.schedule(new MyTimer(),1l,10l);
		setVisible(true);
		}
		}
	public void paint(Graphics g)
	{
		g.drawImage(image, x,10, this);
	}
	private Image getscreen(){
		Image screen = createImage(300, 300);
		x++;
		return screen;
	}

class MyTimer extends TimerTask{
	@Override
	public void run()
	{
		repaint();
	}
}
}
