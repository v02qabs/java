import java.io.*;
import java.net.*;

public class SockServer 
{
	final static int PORT=8080;

	public static void main(String[] args)
	{
		System.out.println("Hello");
		String outfile = "./item.txt";
		byte[] buffer = new byte[512];
		
		try
		{
			ServerSocket serverSock = new ServerSocket(PORT);
			Socket sock = serverSock.accept();

			InputStream inp = sock.getInputStream();
			OutputStream out = new FileOutputStream(outfile);

			int filelen;
			while((filelen = inp.read(buffer))>0)
			{
				out.write(buffer, 0, filelen);
			}

			out.flush();
			out.close();
			inp.close();
			sock.close();
			serverSock.close();
		}
		catch(Exception error)
		{
			System.out.println("error");
		}
	}
}

