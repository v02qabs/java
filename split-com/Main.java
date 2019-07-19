import java.io.*;
public class Main
{
	public Main(){}
	public void init()
	{
		System.out.println("split the comma.");
	}
	public static void main(String[] args)
	{
		Main m = new Main();
		m.init();
		m.splitcom();
	}
	public void splitcom()
	{
		System.out.println("split start." );
		java.io.File file = new java.io.File("./Main.java");
		String line;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null)
			{

				System.out.println("line : " + line);
			}
			br.close();

		}
		catch(Exception error)
		{
			System.out.println("file reading error.");
		}
	}

}

