import java.io.*;

class AllFiles
{
	public static void main(String[] args)
	{
		new AllFiles();
	}
	public AllFiles()

	{
		System.out.println("All List File");
		init();
	}
	private void init()
	{
		try{
			File file = new File("/");
			File[] fsfile = file.listFiles();
		}
		catch(Exception error)
		{
			System.out.println("error");
		}
	}
}

