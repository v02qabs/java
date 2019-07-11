import java.applet.*;
import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.universe.*;

public class ja1 extends Applet
{
	private SimpleUniverse simpleU;

	public void init()
	{
		setLayout(new BorderLayout());
		Canvas3D canvas = new Canvas3D(simpleUniverse.getPreferedConfiguration());
		add(canvasi, BorderLayout.CENTER);
		simpleU = new SimpleUniverse(canvas);
		simpleU.getViewingPlatform().setNominalViewingTrajsform();
	}
	public void destroy()
	{
		simpleU.cleanup();
	}
	public static void main(String[] args)
	{
		MainFrame main = new MainFrame(new ja1(), 512,512);
	}
}

