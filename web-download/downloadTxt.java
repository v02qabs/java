import java.io.*;
import java.nio.channels.*;
import java.net.*;

public class downloadTxt
{
		private URL url;
		public void download(String surl){
		try
		{
		url = new URL(surl);
		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}

    String path = url.getPath();
    String name = path.substring(path.lastIndexOf("/") + 1);
    long size = 0L;

    try (ReadableByteChannel bc = Channels.newChannel(url.openStream());
         FileChannel fc = new FileOutputStream(name).getChannel()) {

        //ファイルチャネルへデータを転送する
        size = fc.transferFrom(bc, 0, Long.MAX_VALUE);
        
    System.out.println(name + " - " + size + " bytes");
    
    }
	catch(Exception error)
			{
				System.out.println(error.toString());
			
			}
			}


}
