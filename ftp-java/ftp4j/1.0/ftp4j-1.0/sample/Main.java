import it.sauronsoftware.ftp4j.FTPClient;

public class Main
{
    public Main()
    {
	    FTPClient client = new FTPClient();
	    try
	    {
		    client.connect("ftp.chobi.net");
		    System.out.println("success.");
	    }
	    catch(Exception error)
	    {
		    System.out.println("error");
	    }

    }
    public static void main(String[] args)
    {
	System.out.println("start...!");
	new Main();
    }
}

