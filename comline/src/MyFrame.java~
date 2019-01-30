import javax.swing.*;
import java.awt.event.*;
import java.io.*;

class MyFrame extends Thread
{
	private JTextArea ja = new JTextArea();
	private JFrame f = new JFrame();
	public void message(String mes)
	{
		System.out.println(mes);
	}

	public void Venter(String str)
	{
	}
	public void printInput(InputStream is) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try
		{
			for(;;)
			{
				String line = br.readLine();
				if(line == null)
					break;

				ja.append(line + "\n");			
			}
		}
		finally
		{
			br.close();
		}
	}
	public void runscript(int aLine)
	{
		int ret = aLine + 1 - aLine;
		String rs = String.valueOf(ret);	
		
		getLine(ja, ja.getText().toString());
	}
	public void getLine(JTextArea ja, String gett)
	{
		new gline(ja, gett);
	}
	public void init()
	{
		System.out.println("this app finish.\n");
		f.setBounds(0,0,500,500);
		ja.setBounds(0,0,470,470);
		ja.append("");
		f.add(ja);
		ja.addKeyListener(new KeyAdapter()
		{
			@Override
				public void keyPressed(KeyEvent e)
				{
					int key = e.getKeyCode();
					if(key == KeyEvent.VK_ENTER)
					{
						//message("Enter.");
						getLine(ja, ja.getText().toString());
					}

				}
		});
		show();
	}
	public void run()
	{
		init();
	}
	public void show()
	{
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	

}

