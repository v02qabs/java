import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Main
{
	public static void main(String[] args)
		{
			Display display = new Display();
				Shell shell = new Shell(display);
				Browser browser = new Browser(shell, SWT.NONE);
				browser.setUrl("https://google.co.jp");
				browser.setBounds(0,0,500,500);
				FormData browsData = new FormData();
				browsData.top = new FormAttachment(1000,0);
				browser.setLayoutData(browsData);
				shell.open();
				
				while(!shell.isDisposed())
				{
					if(!display.readAndDispatch())
						display.sleep();
				}
				display.dispose();
			}
			
}
			
			
