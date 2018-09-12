import java.io.*;

class keyinput
{
	public keyinput()
	{
	}
	String str;

	public String initalize()
	{
		System.out.println("Could you type any key");
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			str = br.readLine();
			
			return str;
		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}

			return str;
	}
}

