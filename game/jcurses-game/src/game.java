import java.io.*;

import jcurses.util.*;
import jcurses.widgets.*;

public class game
{
	public static void main(String[] args)
	{
		new game();
	}
	public game(){
		init();}
	private void init()
	{
		Window w = new Window(0,0,100,100,true,"HELLO");
		w.setVisible(true);
	}


}

