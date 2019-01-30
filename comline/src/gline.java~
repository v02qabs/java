import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;

class gline 
{
	private JTextArea tja1;
	private String compile;

	private ArrayList<String> array = new ArrayList<String>();

	public gline(JTextArea ja1, String jline)
	{
		this.tja1 = ja1;
		this.compile = jline;
		make_array();
	}
	private void printInput(InputStream is) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try
		{
			for(;;)
			{
				String line = br.readLine();
				if(line == null)
					break;

				System.out.println("line : " + line);
			}
		}
		catch(Exception error)
		{
			System.out.println("error.\n");
		}

	}
	public void make_array()
	{
		String[] a_list = compile.split("\n");
		//System.out.println(compile);
		for(int i=0; i<a_list.length; i++)
		{
			array.add(a_list[i]);
		}
		//last list visible;
		String array_com = array.get(a_list.length-1);
		try
		{
			Runtime r = Runtime.getRuntime();
			Process p = r.exec(array_com);
			InputStream es = p.getErrorStream();
			printInput(es);
			InputStream is = p.getInputStream();
			printInput(is);
		}
		catch(Exception error)
		{
			System.out.println("error.");
		}
	}
}
