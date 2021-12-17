import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.*;

public class Main
{
	public static void main(String[] args)
		{
			Display display = new Display();
				Shell shell = new Shell(display);
				Browser browser = new Browser(shell, SWT.NONE);
				browser.setUrl("http://sn39s.work");
				browser.setBounds(0,0,500,500);
				shell.pack();
				shell.open();
				
				while(!shell.isDisposed())
				{
					if(!display.readAndDispatch())
						display.sleep();
				}
				display.dispose();
			}
			
}
			
			
