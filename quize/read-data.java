import java.io.*;
import java.nio.file.*;

class readData
{
	public readData()
	{
		System.out.println("Hello read data class");
		readData();
	}
	private void readData()
	{
		Path data_path = Paths.get("./korian.txt");
		try
		{
			String korian_data = Files.readString(data_path);
			System.out.println(korian_data);
			new StringData(korian_data);
			

		}
		catch(Exception error)
		{
			System.out.println(error.toString());
		}
	}
}

