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

	public static void main(String[] args)
	{
			try
			{
				if(args[0].equals("add"))
				{
				System.out.println("第一引数：" + args[0]);
				}
				if(args[0].equals("push"))	
				{
						System.out.println("サーバへファイルをアップロードします。");
						
						new Jftpg().zipping();
			}
				
					if(args[1].equals("."))
					{
						System.out.println("第二引数: " + args[1]);
						System.out.println("ディレクトリをプロジェクトに追加します。");
												File ftop = new File(".");
						new Jftpg().upload(ftop);
						
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
      else if( file.isDirectory() )
        upload( file );
      else if( file.isFile() )
        execute( file );
    }
}

			

				  public void execute( File file ) {
    // ここにやりたい処理を書く
    //System.out.println( file.getPath() );
    FileWrite(file.getPath());
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
  		System.out.println(message);
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
			
			fileUpping(hostname,user,password, "tmp.zip" ,rootdir);
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
			//client.setType(it.sauronsoftware.ftp4j.FTPClient.TYPE_BINARY);
			File upfile = new File(uploadfiles);
			client.changeDirectory(rootdir);
			client.upload(upfile);
			System.out.println("転送成功");
			client.disconnect(true);
	
		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}
 
	}

}

