import java.io.*;
import java.util.*;
import java.lang.Runtime;
public class MainActivity
{
	public MainActivity()
	{}
}

class Jftpg
{
	

	public Jftpg()
	{}
	private static void read_local_dir(String dir)
	{
		new Jftpg().upload(new File(dir));
	}
	public String home = System.getPropery
	public static void main(String[] args)
	{
			try
			{
				if(args[0].equals("add"))
				{
				System.out.println("第一引数：" + args[0]);
				System.out.println("ファイルリストを登録します。");
				System.out.println("登録されたディレクトリ&ファイル：" + args[1]);
				new File("./.log.txt").delete();
						
				read_local_dir(args[1]);
				}
				if(args[0].equals("push"))	
				{
						System.out.println("サーバへファイルをアップロードします。");
						
						//new Jftpg().zipping();
				
				}
					
		}
		catch(Exception error)
		{
				System.out.println("");
		}
						
		}
		private void upload(File dir)
		{
		
    File[] files = dir.listFiles();
    if( files == null )
      return;
    for( File file : files ) {
      if( !file.exists() )
        continue;
      
      else if( file.isFile() )
	
        FileWrite(file.getAbsolutePath());
    }
}

			

 public void execute( File file ) {
    // ここにやりたい処理を書く
    //System.out.println( file.getPath() );
   		FileWrite(file.getName());
	
  }
  private String getTime()
  {
  		System.out.print("ファイルアップロードを実行した時間：");
  		Date nowtime = new Date();
  		System.out.println(nowtime);
  		return nowtime.toString();
	}
  
  private void FileWrite(String message)
  {
  		System.out.println("c : " + message);
  		try
  		{
						File write_log = new File("./.log.txt");
						FileWriter fwriter = new FileWriter(write_log, true);
						fwriter.write(message + "\n");
						fwriter.close();
						
				}
				catch(Exception error)
				{
						System.out.println("書き込み失敗");
				}
		}
	private void zipping()
	{
		try
		{
			System.out.println("zipping...");
			Runtime r = Runtime.getRuntime();
			r.exec("zip -r tmp.zip ./");
			String hostname,user,password, rootdir;
			System.out.print("hostname: ");
			BufferedReader br_hostname= new BufferedReader(new InputStreamReader(System.in));
			hostname = br_hostname.readLine();
			System.out.print("username: ");
			BufferedReader br_user = new BufferedReader(new InputStreamReader(System.in));
			user = br_user.readLine();
			System.out.print("password: ");
			BufferedReader br_password = new BufferedReader(new InputStreamReader(System.in));
			password = br_password.readLine();
			System.out.print("server dir: ");
			BufferedReader br_rootdir = new BufferedReader(new InputStreamReader(System.in));
			rootdir = br_rootdir.readLine();
			BufferedReader br_upload_files = new BufferedReader(new FileReader(new File("./.log.txt")));
			String line;
			while((line = br_upload_files.readLine()) != null)
			{
				System.out.println("o: " + line);
				File upload_file = new File(line);
				fileUpping(hostname,user,password, line ,rootdir);
			}
		}
		catch(Exception error)
		{
			System.out.println("error making zip files");
		}
	}
		
	private void fileUpping(String hostname, String user, String password, String uploadfiles, String rootdir)
	{
		try
		{
			it.sauronsoftware.ftp4j.FTPClient client = new it.sauronsoftware.ftp4j.FTPClient();
			client.connect(hostname);
			client.login(user, password);
			client.setType(it.sauronsoftware.ftp4j.FTPClient.TYPE_BINARY);
			File upfile = new File(uploadfiles);
			client.changeDirectory(rootdir);
			client.upload(upfile);
			System.out.println("転送成功");
			client.disconnect(true);
	
		}
		catch(Exception error)
		{
			System.out.println("error: " + error.toString());
		}
 
	}

}

