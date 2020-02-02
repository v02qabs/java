import java.awt.dnd.*;
import java.awt.*;
import java.awt.event.*;

public class DND extends Frame
{
	public DND(){}
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		new DND().init();
	}
	private void init()
	{
		setTitle("HelloDND");
		setBounds(0,0,500,500);
		setVisible(true);
		addWindowListener(new WindowListener()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
			public void windowClosed(WindowEvent e){}
			public void windowActivated(WindowEvent e){}
			public void windowDeiconified(WindowEvent e){}
			public void windowIconified(WindowEvent e){}
			public void windowOpened(WindowEvent e){}
			public void windowDeactivated(WindowEvent e){}
		});
		DragSource ds = new DragSource();
		ds.addDragSourceListener(new DragSourceListener()
		{
			public void dragDropEnd(DragSourceDropEvent dsde){
				if(dsde.getDropSuccess())
				{
					System.out.println("OK");
				}
			}
			public void dragEnter(DragSourceDragEvent dsde){
			}
			public void dragOver(DragSourceDragEvent dsde){
			}
			public void dragExit(DragSourceEvent dsde)
			{
			}
			public void dropActionChanged(DragSourceDragEvent dsde){
			}
		});
	}
} 
