public class filelength
{
	public filelength(){}
	public static void main(String[] args)
	{
		String filename = args[0];
		System.out.println("file[byte]: " + filename.length());
		System.out.println("file[Kbyte]: " + filename.length() / 1000);
		System.out.println("file[Mbyte]: " + filename.length() /1000/ 1000);
	}
}

