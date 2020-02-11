import java.io.*;
import java.awt.*;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		new Main().Init();
	}
	public void Init()
	{
		Frame f = new Frame();
		f.setTitle("Hello.");
		f.setBounds(0,0,500,500);
		f.setVisible(true);
	}
}

