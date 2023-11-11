import java.io.*;
import it.sauronsoftware.ftp4j.*;
import java.net.*;
import java.util.*;


class Jftpg
{
	public static void main(String[] args)
	{
		System.out.println("Hello Jftp");
		try
		{
			String option_command = args[0];
			String file_path_name = args[1];
			System.out.println("オプション：" + option_command + "\n" + "ファイル名もしくは、ファイルディレクトリ：" + file_path_name);
			if(option_command.equals("add"))
			{
				System.out.println("add files or add paths");
				File file_path = new File(file_path_name);
				if(file_path.isFile())
				{
					System.out.println("this is file.");
					new Jftpg().log(file_path);
				}
				else if(file_path.isDirectory())
				{
					System.out.println("this is dir.");
					File[] lists = file_path.listFiles();
					if(lists == null)
					{
						return;
						}
					for(File file : lists)
						{
							if(!file.exists())
							continue;
							else if (file.isFile())
							{
									new Jftpg().log(file);
							}
						}	
				}
			}
			else if(option_command.equals("push"))
			{
				System.out.println("push files.");
				new Jftpg().read_logs();
			}
			else if(option_command.equals("init"))
			{
				System.out.println("init log files...");
				new Jftpg().inits();
			}


		}catch(Exception error)
		{
			System.out.println("java Jftpg add <file_path or file_name> \n" + "java Jftpg push");
		}
		
	
	}
	private void inits()
	{
		System.out.println("delete files..");
		new File(home + log).delete();
	}

	private void read_logs()
	{
		System.out.println("log file read now...");
		login();
		System.out.println("login ok.");
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(home + log));
			String line;
			while((line=br.readLine()) != null)
			{
				System.out.println(line);
				uploads(line);
			}
			
		}

		catch(Exception error)
		{
			System.out.println("read error" + error.toString());
		}

	}
	FTPClient client;
	private void login()
	{
	try
	{	client = new FTPClient();
	       client.connect("pro.chobi.net");
       		client.login("takesue090","Takesue!9!A");
       		Scanner scan_dir = new Scanner(System.in);
       		String dir = scan_dir.next();
		client.changeDirectory(dir);
	}catch(Exception error)
	{
		System.out.println("login error" + error.toString());
	}
	}



 	private void now_dir()
 	{
 	}
 		
	private void uploads(String line)
	{
	try{
		now_dir();

		client.upload(new File(line));
		
	}catch(Exception error)
	{
		System.out.println("upload error" + error.toString());
	}
	}




	String home = System.getProperty("user.home");
	String log = "/log.txt";
	File log_file = new File(home + log);
	private void log(File file_name)
	{
		System.out.println(file_name);
		try
		{
		
			BufferedWriter bw = new BufferedWriter(new FileWriter(log_file, true));
			bw.write(file_name + "\n");
			bw.close();
		}
		catch(Exception error)
		{
			System.out.println("Writer error : " + error.toString());
		}
	}
	

}

