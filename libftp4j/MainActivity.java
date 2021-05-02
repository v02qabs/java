//listNames

import java.io.*;
import javax.swing.*;
import it.sauronsoftware.ftp4j.*;

class ftpFilelist
{
	public ftpFilelist()
	{init();}
	
	public static void main(String[] args)
	{
		System.out.println("Hello.!");
		new ftpFilelist();
	}
	
	private void init()
	{
			try
			{
					System.out.println("connecting sever...");
					FTPClient client = new FTPClient();
					client.connect("www43.onamae.ne.jp");
					client.login("sn39s@sn39s.work", "Takesue090.");
					
					String flist_server[] = client.listNames();
					for(String flist : flist_server)
					{
						System.out.println("s: " + flist);
					}
					//client.disconnect(true);					
		}catch(Exception error)
		{
				System.out.println(error.toString());
		}
	}
		
}



