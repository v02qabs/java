//import java.io.*;
//


public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Hello.");
		new Main();
	}
	public Main()
	{
		System.out.println("type keyboard.");
		keyinput ki = new keyinput();
		String i = ki.initalize();
		System.out.println("i: " + i);
	}
}

