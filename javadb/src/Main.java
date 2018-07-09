import java.io.*;
import java.lang.*;
import java.sql.*;

public class Main
{
	public Main()
	{
		System.out.println("welcome");
		initalize();
	}
	public static void main(String[] args)
	{
		System.out.println("Start");
		new Main();
	}
	public void initalize()
	{
		Connection con;
		try
		{
			con = DriverManager.getConnection("jdbc:derby:test1;create=true");
			DriverManager.getConnection("jdbc:derby:test1;shutdown=true");
			con.close();
			System.out.println("OK.");
		}
		catch(Exception error)
		{
			System.out.println("NG.");
		}
	}
}

