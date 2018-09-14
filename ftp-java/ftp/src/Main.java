import it.sauronsoftware.ftp4j.*;

public class Main
{
	public Main()
	{
		System.out.println("connect ...");
		System.out.println("login name?: ");
		keyinput ki = new keyinput();
		String ftpServer = ki.initalize();
		try
		{
			FTPClient client = new FTPClient();
			client.connect(ftpServer);
			System.out.println("success.");
		}
		catch(Exception error)
		{
			System.out.println("error!");
		}

	}
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		new Main();
	}
}

