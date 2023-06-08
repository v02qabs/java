import java.io.*;
import java.util.List;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class HelloView extends JFrame
{
	public static void main(String[] args)
	{
		System.out.println("文字列ビューへようこそ。");
		new HelloView().init();
	}
	private void init()
	{
		setBounds(0,0,500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("文字変換ビュー");

		past_parts();
		setVisible(true);
	}
	private void past_parts()
	{
		JTextArea area_main = new JTextArea();
		add(area_main);
	}

}

