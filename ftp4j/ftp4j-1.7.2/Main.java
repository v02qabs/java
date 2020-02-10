import it.sauronsoftware.ftp4j.FTPClient;
public class Main
{
	public Main(){}
	public static void main(String[] args)
	{
		new Main().init();
	}
	private FTPClient client;
	private void init()
	{
		client = new FTPClient();
		try
		{
			client.connect("ftp.chobi.net");
		}
		catch(Exception error)
		{
			System.out.println("connect error.");
		}
	}
}

