import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


class Main{
	public Main()
	{
	}
	public static void main(String[] args)
	{
		new thpad();
	}
}

class thpad extends JFrame implements KeyListener
			{
				public thpad()
{
	setBounds(0,0,500,500);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	panel();

	setVisible(true);
	typekey();

}
	private JTextArea journal;
		private void panel()
		{
		
			journal = new JTextArea();
			journal.setBounds(0,0,450,450);
			JPanel panel = new JPanel();
			panel.setLayout(null);
			panel.add(journal);
			
			Container content = getContentPane();
			content.add(panel);
		}

		private void typekey()
		{
			journal.addKeyListener(this);
		}
				public void keysave(KeyEvent e){

		if(e.getKeyCode() == Skey && e.isAltDown())
		{
			System.out.println("save");
		try
		{
					Runtime run = Runtime.getRuntime();
					Sting dir = "/";
					run.exec("java fchooser " + dir);
			}
			catch(Exception error)
			{
					System.out.println("error");
			}
			}
			}
			
		private int Skey = KeyEvent.VK_S;
		
	 public void keyTyped(KeyEvent e){}
		public void keyPressed(KeyEvent e){

			keysave(this.e);
			}
			
			
		public void keyReleased(KeyEvent e){}
}


