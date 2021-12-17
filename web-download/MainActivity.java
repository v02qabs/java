import java.io.*;

public class MainActivity
{
	MainActivity()
	{
	}
	public static void main(String[] args)
	{
		new MainActivity().down(args[0]);
		}
		private void down(String args)
		{
		new downloadTxt().download(args);
			}
}
			
