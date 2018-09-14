import it.sauronsoftware.ftp4j.*;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		Main m = new Main();
		m.con();
	}
	public void con()
	{
		try
		{
			FTPClient fclient = new FTPClient();
			fclient.connect("ftp.chobi.net");
		}
		catch(Exception error)
		{
			System.out.println("error.");
		}
	}
}

