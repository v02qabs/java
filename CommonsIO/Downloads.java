import org.apache.commons.io.FileUtils;
import java.io.File;
import java.net.URL;

class DatabaseDownloader {

    public static void downloadDatabase(String urlString, String localFilePath) throws Exception {
        URL url = new URL(urlString);
        File file = new File(localFilePath);
        FileUtils.copyURLToFile(url, file);
    }
    public static void main(String[] args){
    	System.out.println("Hello Downloader");
    	try{
		new DatabaseDownloader().downloadDatabase("https://hirotakeakesi.work/data.db", "./data.db");
	}
	catch(Exception error)
	{
		System.err.println(error.getMessage());
	}

	
    }

}
