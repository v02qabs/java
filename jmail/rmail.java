import java.io.*;
import org.apache.commons.net.*;
import org.apache.commons.net.imap.*;

class rmail
{
	public static void main(String[] args)
	{
		System.out.println("Recive mail");
		new rmail().init();
	}
	private void init()
	{
		System.out.println("Connect to server");
		try{
			IMAPClient client = new IMAPClient();
			client.connect("mail.chobi.net");

			client.login("takesue090@chobi.net", "take01");
			System.out.println("success.");
			if(client.select("inbox"))
			{
					System.out.println("false");
			}
			if(client.examine("inbox"))
			{
				System.out.println("false");
			}

			if(client.list("","*")){
				System.out.println("true");
			}

			System.out.println("list get");
		}
		catch(Exception error)
		{
			System.out.println("error");
		}
	}
}



