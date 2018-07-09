public class Main
{
	public void impShow()
	{
		libHello lh = new libHello();
		lh.show("Hello");
	}
	public Main()
	{
		impShow();
	}

	public static void main(String[] args)
	{
		new Main();
	}
}

