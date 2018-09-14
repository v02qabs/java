import it.sauronsoftware.ftp4j.FTPClient;

class Main
{
    public Main()
    {
	try
	    {
		FTPClient client = new FTPClient();
		client.connect("ftp.chobi.net");
		System.out.println("OK.");
	    }
	catch(Exception error)
	    {
		System.out.println("Ng.");
	    }
    }
    public static void main(String[] args)
    {
	new Main();
    }
}
