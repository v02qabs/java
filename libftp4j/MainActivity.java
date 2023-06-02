//listNames

import java.io.*;
import javax.swing.*;
import java.util.*;
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
					//ServerName type:
					System.out.println("接続先サーバー名：");
					Scanner server = new Scanner(System.in);
					String scanner_server = server.nextLine();
					client.connect(scanner_server);
					//login user info
					System.out.println("ユーザー名");
					Scanner user = new Scanner(System.in);
					String user_scanner = user.nextLine();
					System.out.println("パスワード：");
					Scanner pass = new Scanner(System.in);
					String pass_scanner = pass.nextLine();
					client.login(user_scanner, pass_scanner);
					
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



