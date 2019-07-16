import java.io.File;

public class Main
{
	public static void main(String[] args)
	{
		String filename = args[0];
		File file = new File(filename);
		System.out.println("files: <byte>" + file.length());
		System.out.println("files: <KByte>" + file.length() / 1000);
		System.out.println("files: <MByte>" + file.length() / 1000 / 1000);
	}
}

