package hiro;

import java.net.*;
import java.io.*;

class Main
{
	public Main()
	{
		try
		{
			init();
		}
		catch(Exception error)
		{
			System.out.println("ERROR");
		}

	}
	public static void main(String[] args){
		System.out.println("Hello");
		new Main();
	}
	public void init() throws Exception
	{
		URL u = new URL("https://sg982zc.chobi.net");
		InputStreamReader is = new InputStreamReader(u.openStream());

		for(int c; (c = is.read()) != -1;)
		{
			System.out.printf("%c", c);
		}
		is.close();
	}
}

