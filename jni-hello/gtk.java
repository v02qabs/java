public class gtk
{
	static
	{
		System.loadLibrary("hello");
	}
	public native void show();
	public static void main(String[] args)
	{
		gtk g = new gtk();
		g.show();
	}
}

