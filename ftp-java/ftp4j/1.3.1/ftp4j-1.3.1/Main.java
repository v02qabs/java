import it.sauronsoftware.ftp4j.*;

public class Main
{
    public static void main(String[] args)
    {
	System.out.println("Hello.");
	Main m = new Main();
	m.connection();
    }
    public BufferedReader br;
    public void connection()
    {
	br = new BufferedReader(new InputStreamReader(System.in));
	String conftps = br.readLine();
	try
	{
		FTPClient cl = new FTPClient();
		cl.connect(conftps);
	}
	catch(Exception error)
	{
		System.out.println("faild.");
	}

}
