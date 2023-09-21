import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

class start extends Frame
{
	public static void main(String[] args)
	{
		System.out.println("Hello game.");
		new MainMenu();
	}
}
class MainMenu extends Frame
{
	public MainMenu()
	{
		init();
	}
	private void init()
	{
		System.out.println("数字並び替えクイズ");
		Thread t = new starting_game();
		t.start();
	}

}

class starting_game extends Thread
{	
	public void run()
	{
		init();
	}
	private String game_num[] = {"0","1","2","3","4","5", "6","7","8","9"};

	private void init()
	{
		Scanner scan = new Scanner(System.in);
		String string_input = scan.next();
		if(string_input.equals("0"))
		{
			System.out.print("\033[H\033[2J");
			mondai();	
		}

	}
	private void mondai()
	{
		System.out.println("次の文字を並び替えてください");
		System.out.println("10秒後にコンソールがリフレッシュされます。");
		System.out.println("game_num : " + game_num[1] + " " + game_num[3]);
		try
		{
			Thread.sleep(1000L);
			Scanner anser_scanner = new Scanner(System.in);
			String anser = anser_scanner.next();
			if(anser.equals(game_num[1] + game_num[3]))
					{

				System.out.println("ok.");
			}
		}		
		catch(Exception error)
		{
			System.out.println("error");
		}

	}

}

