import it.sauronsoftware.ftp4j.*;

public class Main
{
    public static void main(String[] args)
    {
	System.out.println("start.");
	Main m = new Main();
	m.connect();
    }
    public void connect()
    {
	    try
	    {
		    FTPClient client = new FTPClient();
	    }
	    catch(Exception error)
	    {
		    System.out.println("error.");
	    }
    }

}
