import it.sauronsoftware.ftp4j.FTPClient;

class test
{
	public test()
	{
		try
		{
			FTPClient client = new FTPClient();
			System.out.println("initok.");
			client.connect("ftp.chobi.net");
			System.out.println("conneciton OK.");
		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}
	}
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		new test();
	}
}

