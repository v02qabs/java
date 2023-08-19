import java.io.*;


class test
{
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		try
		{
			String str = br.readLine();
			if(str.equals("x"))
				System.exit(0);
		}
		catch(Exception error)
		{
			System.out.println("error");
		}
	}
}
