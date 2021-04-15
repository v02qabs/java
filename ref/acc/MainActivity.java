import java.io.*;

public class MainActivity
{
	public MainActivity()
	{
		System.out.println("Welcome to my code of encoding to html.");
		Sshow("input trans file...");
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String fname = br.readLine();
			Sshow("Inputed file name is " + fname);
			File f = new File(fname);
			int data;
			char txt;
			FileReader fr = new FileReader(f);
			while((data = fr.read()) != -1)
			{
					
					txt = (char)data;
					String text = String.valueOf(txt);
					System.out.print("text: " + text);
			}
			
			
		}
		catch(Exception error)
		{
			Sshow(error.toString());
		}
		
	}
	public static void main(String[] args)
	{
		new MainActivity();
	}
	private void Sshow(String mes)
	{
		System.out.println(mes);
	}
	
}

