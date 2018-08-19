import java.io.*;
import org.mozilla.universalchardet.*;

public class fenc
{
	byte fl;
	public fenc(String fname)
	{
		fenct(fname);
		flen(fname);
		System.out.println(fname);
	}
	public void fenct(String fname)
	{
		try
		{
			byte[] b = new byte[100000000];
			FileInputStream fs = new FileInputStream(fname);
			UniversalDetector d = new UniversalDetector(null);
			int r;
			while((r = fs.read(b))> 0 && !d.isDone())
			{
				d.handleData(b, 0, r);
			}
			d.dataEnd();
			String enc = d.getDetectedCharset();
			System.out.println("文字コードは、" + enc + "です。");
			d.reset();

		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}

	}
	public byte flen(String fname)
	{
		File f = new File(fname);
		try
		{
			System.out.println("ファイルの長さは " + f.length() + "byte です。");
			return fl;
		}
		catch(Exception error)
		{
			System.out.println("error.");
		}
		return fl;
	}
	public static void main(String[] args)
	{
		System.out.println("引数：" + args[0]);
		String fname = args[0];
		new fenc(fname);
	}
}

