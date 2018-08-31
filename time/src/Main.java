import java.util.*;
import java.text.*;

public class Main
{
    public Main()
    {
    }
    public static void main(String[] args)
    {
	System.out.println("time...");
	Main m = new Main();
	m.settime();
    }
    public void settime()
    {
    	Date date = new Date();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
	System.out.print(sf.format(date));
	TimeZone t1 = TimeZone.getTimeZone("Asia/Tokyo");
	Calendar c = Calendar.getInstance(t1);
	c.setTimeZone(t1);
	System.out.println("タイムゾーン: " + t1.getID());
	//System.out.println(c.get(Calendar.HOUR_OF_DAY));
	System.out.println(c.get(Calendar.HOUR_OF_DAY) + "時" + c.get(Calendar.MINUTE) + "分" + c.get(Calendar.SECOND) + "秒");
    }
}

