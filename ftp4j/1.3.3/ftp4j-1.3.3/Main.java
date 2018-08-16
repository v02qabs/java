import it.sauronsoftware.ftp4j.FTPClient;

class Main
{
	public Main()
	{
		System.out.println("Hello.");
		try
		{
			FTPClient c = new FTPClient();
			c.connect("ftp.chobi.net");
		}
		catch(Exception error)
		{
			System.out.println("error");
		}
	}
	public static void main(String[] args)
	{
		new Main();
	}
}

