import java.io.*;

public class flenJ
{
	public flenJ()
	{
	}
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		flenJ fl = new flenJ();
		fl.flen();
	}
	public void flen()
	{
		File f = new File("/storage/sdcard0/homepage/index.html");
		try
		{
			System.out.println("ファイルの長さ；" + f.length());
		}
		catch(Exception error)
		{
			System.out.println("error.");
		}
	}
}

