import java.util.Calendar;
import java.util.Collections;

public class Calenders {

	public static void main(String[] args) {
		Calendar cl = Calendar.getInstance();
		
		int thisYear = cl.get(Calendar.YEAR); //今年
		int thisMonth = cl.get(Calendar.MONTH); //今月(1月=0、2月=1であるため配列monthの添字に使用)
		outputCalendar(thisYear,thisMonth);
		
		//他の年、他の月の場合の確認用
		System.out.println("\n");
		int otherYear = 2024; //西暦を入力
		int otherMonth = 2; //月を入力
		outputCalendar(otherYear,otherMonth - 1);
		
	}
	
	public static void outputCalendar(int inputYear, int inputMonth) {
		Calendar cl = Calendar.getInstance();
		
		String[] month = {"January","February","March","April","May","June",
				"July","August","September","October","November","December"};
		String[] week = {"Su","Mo","Tu","We","Th","Fr","Sa"};
		
		int firstDay = 1; //月の初日
		//月の初日の曜日(日曜日=1、月曜日=2であるため-1をして配列weekの添字に使用)
		cl.set(inputYear,inputMonth,firstDay);
		int firstDayWeek = cl.get(Calendar.DAY_OF_WEEK) - 1; 
		int lastDay = cl.getActualMaximum(Calendar.DAY_OF_MONTH); //月の最終日

		System.out.print("     " + month[inputMonth] + " " +  inputYear); //年と月を出力
		System.out.println(); 

		//カレンダーの曜日を出力
		for (int i = 0; i < week.length; i++) {
			System.out.printf("%2s ",week[i]);
		}
		System.out.println();
		//初日が何曜日かに合わせて空白を出力
		System.out.print(String.join("", Collections.nCopies(firstDayWeek, "   ")));
		//日付を出力
		for (int i = firstDay; i <= lastDay; i++) {
			System.out.printf("%2d ",i);
			if ((firstDayWeek + i) % 7 == 0) {
				System.out.println();
			}			
		}
	}
}

