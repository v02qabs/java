package hiro.com.Downloads;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.net.URL;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        new App().use_Downloads_commons_IO();
    }
    private void use_Downloads_commons_IO(){
       try{
        downloadDatabase("https://hirotakeakesi.work", "/home/takesue090/logging.txt x +			");
        System.out.println("success");
        }
        catch(Exception error){
        	System.out.println(error.toString());
        	}
        	
    }

    public static void downloadDatabase(String urlString, String localFilePath) throws Exception {
        URL url = new URL(urlString);
        File file = new File(localFilePath);
        FileUtils.copyURLToFile(url, file);
    }

}
