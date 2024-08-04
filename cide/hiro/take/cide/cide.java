package hiro.take.cide;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class cide extends Frame{

	Process process;
	Panel panel;
	private TextField field1;
	private TextField field2;
	private Label label1;
	private Label label2;
	private Button comp_button;
	private Button debug_button;
	private TextArea textArea;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		cide ci = new cide();
		ci.setBounds(0,0,500,500);
		ci.setTitle("CIDE");
		ci.setVisible(true);
		ci.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//System.out.println("終了。");
				System.exit(0);
			}
		});
	}
	public cide()
	{
		//gcc();
		workspace();
	}
	public void printInputStream(InputStream is) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
			try
			{
				for (;;)
				{
		 			String line = br.readLine();
		 			if (line == null) break;
		 			textArea.setText("");
		 			textArea.append(line + "¥n");

			 	}
			} finally
			{
				br.close();
			}

	}
	public void workspace()
	{
		panel = new Panel();
		panel.setLayout(null);
		label1 = new Label("ソースコードの場所");
		label1.setBounds(0,0,200,30);
		label2 = new Label("実行ファイルの作成先");
		label2.setBounds(0, 31, 200, 30);
		field1 = new TextField();
		field1.setBounds(210, 0, 200, 30);
		field2 = new TextField();
		field2.setBounds(210, 31, 200, 30);

		comp_button = new Button("ビルド");
		comp_button.setBounds(0,31*3 ,100,30);
		comp_button.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO 自動生成されたメソッド・スタブ
						gcc();
					}

				});
		debug_button = new Button("実行");
		debug_button.setBounds(110,31*4,100,30);
		debug_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				debug();
			}
		});

		textArea = new TextArea();
		textArea.setText("コンソール");
		textArea.setBounds(0,31*5,200,200);


		panel.add(label1);
		panel.add(field1);

		panel.add(label2);
		panel.add(field2);
		panel.add(comp_button);
		panel.add(debug_button);
		panel.add(textArea);

		add(panel);
	}

	public void debug()
	{
		System.out.println("deug...");
		try
		{

			Runtime r = Runtime.getRuntime();
			//String work = "e:/msys2/home/takes/hello/hello.c";
			String hello = field1.getText().toString();
			String outhello = field2.getText().toString();
			process = r.exec("./" + outhello);
			//process = r.exec(outhello);

			process.waitFor();
			InputStream is = process.getInputStream();
			 //標準出力
			 printInputStream(is);

			 InputStream es = process.getErrorStream();
			  //標準エラー
			  printInputStream(es);


		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//Processの使い方は後述

	}

	public void gcc()
	{
		try
		{

			Runtime r = Runtime.getRuntime();
			//String work = "e:/msys2/home/takes/hello/hello.c";
			String hello = field1.getText().toString();
			String outhello = field2.getText().toString();
			process = r.exec("gcc " + hello + " -o " + outhello);
			//process = r.exec(outhello);

			process.waitFor();
			InputStream is = process.getInputStream();
			 //標準出力
			 printInputStream(is);

			 InputStream es = process.getErrorStream();
			  //標準エラー
			  printInputStream(es);


		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//Processの使い方は後述
	}

}
