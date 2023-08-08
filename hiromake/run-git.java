import java.io.*;
import java.util.Date;

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
			Date today = new Date();
			String time = today.toString();
			System.out.println(time);
			Process p = Runtime.getRuntime().exec("git add . & git commit -m \"" + time + "\"" + "& git push");
			InputStream is = p.getInputStream();
			printInputStream(is);
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
			for(;;)
			{
				String line = br.readLine();
					System.out.println(line);
					if(line == null)
						System.exit(0);
			}
		}
		
		catch(Exception error)
		{
			System.out.println(error.toString());
		}
	}

}
