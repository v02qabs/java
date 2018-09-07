import java.io.*;

class FileP
{
	public FileP()
	{
	}
	public void mFileP()
	{
		System.out.println("getPath!");
	}
	public void gSplit()
	{
		System.out.println("Hello.");
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String input = br.readLine();
			System.out.println(input);
			Split(input);
		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}
	}
	public void Split(String in)
	{
		System.out.println("Split");
		String ins[] = in.split(" ");
		System.out.println("ins " + ins[1]);
		String fpath = ins[1];
		File f1 = new File(fpath);
		String f1s = f1.getAbsolutePath();
		getPath(f1s);
	}
	public String getPro(String fpath)
	{
		java.lang.System.setProperty("usr.dir", fpath);
		String s = java.lang.System.getProperty("usr.dir");
		return s;
	}
	public void getPath(String fdir)
	{
		//java.lang.System.setProperty("usr.dir", fpath);
		System.out.println("dir: " + fdir);
		try
		{
			System.out.println("dir = " + fdir);
			File f = new File(fdir);
			f.getAbsolutePath();
			String flist1[] = f.list();
			for(String str : flist1)
			{
				System.out.println(str);
			}
			gSplit();

		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}

	}
	public String fDir(String fpath)
	{
		String fp = java.lang.System.getProperty("usr.dir");
		System.out.println(fp);
		return fp;
	}

}

