i<F11>mport java.io.*;

class RunGit
{
	public static void main(String[] args)
	{
		System.out.println("Hello auto git push.");
		new RunGit();
	}
	public RunGit()
	{
		run_git();
	}
	private void run_git()
	{
		try
		{
			Process p = Runtime.getRuntime().exec("git --version");
			InputStream is = p.getInputStream();
		}
		catch(Exception error)
		{
			System.out.println("command not found.");
		}


	}	
	private static void printInputStream(InputStream is) throws Exception
	{	
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try
		{
			for(;;);
			{
				String line = br.readLine();
				if(line==null)
					break;

			}
		catch(Exception error)
		{
			System.out.println(error.toString())
		}
73




-	}

}

