import java.io.*;
import java.net.*;

public class Client 
{
	final static String host = "localhost";
	final static int port = 8080;

	public static void main(String[] args)
	{
		System.out.println("Hello");
		File file = new File("readme.md");
		byte[] buffer = new byte[512];
		try
		{
			Socket sock = new Socket(host,port);
			InputStream is = new FileInputStream(file);
			OutputStream os = sock.getOutputStream();

			int filelen;
			while((filelen = is.read(buffer))>0)
			{
				os.write(buffer, 0, filelen);
			}
			
			os.flush();
			os.close();
			is.close();
			sock.close();
		}
		catch(Exception error)
		{
			System.out.println("error");
		}
	}
}


