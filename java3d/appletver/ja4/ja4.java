import java.io.*;
import java.applet.*;
import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.vecmath.*;

public class ja4 extends Applet
{
	public ja4()
	{
	}
	public SimpleUniverse simpleU;
	public BoundingSphere bounds = new BoundingSphere(new Point3d(), 10.0);
	
	public void init()
	{
		setLayout(new BorderLayout());
		Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		add(canvas);
		simpleU = new SimpleUniverse(canvas);
		simpleU.getViewingPlatform().setNominalViewingTransform();
	
		simpleU.addBranchGraph(createSceneGraph());
	}
	private BranchGroup createSceneGraph()
	{
		BranchGroup group = new BranchGroup();
		Background back = new Background(new Color3f(0.0f, 0.0f, 0.0f));
		back.setApplicationBounds(bounds);
		group.addChild(back);
		Point3f[] point = new Point3f[3];
		Color3f[] color = new Color3f[3];
		point[0] = new Point3f(0.3f, 0.0f, 0.0f);
		point[1] = new Point3f(0.0f,0.7f, 0.0f);
		point[2] = new Point3f(-0.3f,0.0f,0.0f); 
		color[0] = new Color3f(1.0f, 0.0f, 0.0f);
		color[1] = new Color3f(1.0f, 0.0f, 0.0f);
		color[2] = new Color3f(1.0f, 0.0f, 0.0f);

		TriangleArray triangle = new TriangleArray(point.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		triangle.setCoordinates(0,point);
		triangle.setColors(0,color);
		Shape3D line3D = new Shape3D(triangle);
		group.addChild(line3D);
		group.compile();
		return group;
	}
	public void destroy()
	{
		simpleU.cleanup();
	}
	public static void main(String[] args)
	{
		MainFrame mf = new MainFrame(new ja4(), 500,500);
	}
}

