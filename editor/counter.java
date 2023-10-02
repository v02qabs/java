import java.io.*;

class counter
{
	public counter()
	{
		count();
	}
	public static void main(String[] args)
	{
		new counter();
	}
	private void count()
	{
		write("~/Download");
		write("~/Downloads/firefox.tar.gz");
		read();
	}
	private String ans = null;
	private BufferedWriter bw;
	public void write(String...  written)
	{
		try
		{
			File file = new File("num.txt");
			bw =  new BufferedWriter(new FileWriter(file));
			for(String i : written)
			{
				ans = i;
				bw.write("/home/takesue090/" + ans);
				bw.close();

			}
		}
		catch(Exception error)
		{
			System.out.println("writting error");
		}

	}
	public String line;
	public String read()
	{

		try
		{
			File file = new File("num.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			if(line == null)
			{
				System.out.println("this file is null.");
			}
			else if(line.equals("/home/takesue090/")){
				System.out.println(line);
			}
			else
			{
				System.out.println(line);
			}
			return br.readLine();

		}
		catch(Exception error)
		{
			System.out.println("error");
		}
		return line;

	}
}

