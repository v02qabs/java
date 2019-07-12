import java.applet.*;
import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.applet.*;
import com.sun.j3d.utils.universe.*;

public class Ja11 extends JApplet
{
	private SimpleUniverse simpleU;

	public void init()
	{
		setLayout(new BorderLayout());
		Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

		add(canvas, BorderLayout.CENETER);
		simpleU = new SimpleUniverse(canvas);	
		simpleU.getViewingPlatform().setNominalViewingTransform();
	}

	public void destroy()
	{
		simpleU.cleanup();
	}
	public static void main(String[] args)
	{
		MainFrame frame = new MainFrame(new Ja11(), 512,512);
	}
}

