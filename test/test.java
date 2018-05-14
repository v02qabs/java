import java.sql.*;
import java.io.*;
import java.lang.*;

public class test extends Thread
{
	public test()
	{
		start();
	}

	public static void main(String[] args)
	{
		System.out.println("hello.");
		new test().run();
	}
	public void start()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			System.out.println("connection...");
		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}
	}

}

