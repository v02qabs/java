import it.sauronsoftware.ftp4j.*;

class item
{
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		FTPClient cl = new FTPClient();
		try
		{
			cl.connect("ftp.chobi.net");
		}catch(Exception error)
		{
			System.out.println("error");
		}
	}
}

