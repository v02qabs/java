import java.io.*;
import javax.swing.*;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		new Main().Init();
	}
	public void Init()
	{
		JFrame f = new JFrame();
		f.setBounds(0,0,500,500);
		f.setVisible(true);
	}
}

