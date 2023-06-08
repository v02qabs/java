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

}
	private JTextArea journal;
	private JButton save_button;

		private void panel()
		{
		
			journal = new JTextArea();
			journal.addKeyListener(this);
			journal.setBounds(0,0,450,450);
			JPanel panel = new JPanel();
			panel.setLayout(null);

			save_button = new JButton("保存");
			save_button.setBounds(10,460,100,30);
			

			panel.add(journal);
			panel.add(save_button);
			
			Container content = getContentPane();
			content.add(panel);
		}

		

@Override
		public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == Skey && e.isAltDown())
		{
			System.out.println("save");
		}

	}

		private int Skey = KeyEvent.VK_S;
		
			
	 @Override
public void keyTyped(KeyEvent e){
}
			
@Override
		public void keyReleased(KeyEvent e){}
}


