import java.io.*;

public class Main
{
	public Main()
	{
	}
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		Main m = new Main();
		String g = m.getLine();
		m.setCount(g);
	}
	public void setCount(String g)
	{
		String sg[] = g.split("ん");
		int glength = g.length();
		System.out.println("len" + glength);
		System.out.println("s : " + sg[2-1]);
		int sh = Integer.valueOf(sg[2-1]);
		for(int i=0; i<=sh; i++)
		{
			System.out.println(sg[0] + "ん");
		}
	}
	public String getLine()
	{
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String line = br.readLine();
			return line;
		}
		catch(Exception error)
		{	
			System.out.println("Input error");
		}
		return null;
	}

}

