import java.io.*;

public class Main
{
    public Main()
    {
    }
    public static void main(String[] args)
    {
	System.out.println("File Name is : " + args[0]);
	Main m = new Main();
	String fname = args[0];
	m.getLength(fname);
    }
    public void getLength(String fname)
    {
 	System.out.println("File is : " + fname);
 	File f = new File(fname);
 	try
 	    {
 		System.out.println(f.length());
 	    }
 	catch(Exception error)
 	    {
 		System.out.println(error.toString());
 	    }
     }

}
