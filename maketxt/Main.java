import java.io.*;

public class Main
{
	public Main(String s0)
	{
		initalize(s0);
	}
	public static void main(String[] args)
	{
		String s0 = args[0];
		System.out.println(s0);

		new Main(s0);
	}
	public void initalize(String s0)
	{
		System.out.println(s0);
		File fwrite = new File("./m.txt");
		try
		{
			FileWriter fw = new FileWriter(fwrite);
			fw.write(s0);
			fw.close();
		}
		catch(IOException error)
		{
			System.out.println("IOException");
		}
		catch(NullPointerException error)
		{	
			System.out.println("null move...");
		}
	}

		
}

