import java.util.*;
import java.text.*;

class tokyo
{
	public tokyo()
	{
		System.out.println("Output Time Locate: Tokyo.");
	}
	public void mytime()
	{
		Date d = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
		System.out.println("Today= " + sf.format(d));
		TimeZone t = TimeZone.getTimeZone("Asia/Tokyo");
		Calendar c = Calendar.getInstance(t);
		c.setTimeZone(t);
		System.out.println("Now Tokyo Time is = " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
	}

}
