package page6.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import page6.controller.Main;

public class Time { 
		
	private Time() {}
	
	public static int parseMinute(String hhmm) {
		int mm = -1;
		if(!hhmm.isEmpty()) {
			String [] t = hhmm.split(":"); 
			mm = Integer.parseInt(t[0]) * 60 + Integer.parseInt(t[1]);
		}
		return mm;
	}
	
	static int[] parseIntArray(String src) { 
		String [] temp = src.split(":");
		if(temp.length == 2) {
			int[] result = new int[2];
			result[0] = Integer.parseInt(temp[0]);
			result[1] = Integer.parseInt(temp[1]); 
			return result;
		} 
		Main.log(Level.INFO, "Time.parseIntArray convert error! (src = " + src + ")"); 
		return new int[0];
	} 
	
	static int LengthMin(String start, String end) { //僅限24小時之內的範圍
		int result = -1; 
		int [] Ts = parseIntArray(start);
		int [] Te = parseIntArray(end);
		if(Ts.length==2 && Te.length==2) {
			result = (Te[0] - Ts[0]) * 60 + Te[1] - Ts[1]; 
			if(result < 0) result += 1440;
		}
		return result;
	}
	
	static String add(String A, int B) { 
		int [] Ta = parseIntArray(A);
		if(Ta.length==2) {
			int h = Ta[0] + (B / 60);
			int m = Ta[1] + (B % 60); 
			if(m >= 60) { 
				m %= 60; 
				h = (h + 1) % 24; 
			}
			return String.format("%02d:%02d", h, m);
		}
		Main.log(Level.INFO, "Time.add Error!");
		return "00:00"; 
	}
	
	static String add(String A, String B) { //A + B
		int [] Ta = parseIntArray(A); 
		int [] Tb = parseIntArray(B); 
		
		if(Ta.length==2 && Tb.length==2) { 
			int h = Ta[0] + Tb[0];
			int m = Ta[1] + Tb[1];
			if(m >= 60) { m %= 60; h++; }
			return String.format("%02d:%02d",h,m);
		}
		else {
			Main.log(Level.INFO, "Time.add Error!"); 
			return "00:00"; 
		} 
	}
	  
	/**
	 * 取得比較晚的時間點
	 * @param A
	 * @param B
	 * @return
	 */
	static String getLate(String A, String B) { 
		int [] Ta = parseIntArray(A);
		int [] Tb = parseIntArray(B); 
		if(Ta.length==2 && Tb.length==2 && ((Ta[0]<Tb[0]) || (Ta[0]==Tb[0] && Ta[1]<=Tb[1]))) { 
			return B; 
		}
		return A; 
	}
	
	static String getEarly(String A, String B) {   
		int [] Ta = parseIntArray(A);
		int [] Tb = parseIntArray(B);
		if(Ta.length==2 && Tb.length==2 && ((Ta[0] < Tb[0]) || (Ta[0] == Tb[0] && Ta[1] <= Tb[1]))) { 
			return A;  
		} 
		return B;
	}
	
	static int compare(String A, String B) {   
		if(getEarly(A,B).equals(A)) return 1;
		return 0;
	}
	
	static int searchArrayMin(String A, String [] B) {
		int pos = 0;
		int len = B.length;
		
		for( ; pos < len; pos++) 
			if(compare(A, B[pos]) == 0) break; 
		if (pos == len) {
			pos = 0;
			for(int i = 0; i < len; i++) {
				if(compare(B[pos], B[i]) == 0) pos = i; 
			}
		}
		
		return pos;
	}
	
	static String getWeek(String src, String format) throws ParseException {  
		SimpleDateFormat DateFormat = new SimpleDateFormat(format);    
    	return DateFormat.parse(src).toString().split(" ")[0];  
	} 
	 
	static boolean isWeekend(String src) throws ParseException {
		return isWeekend(src, "yyyy/MM/dd");
	} 
	
	static boolean isWeekend(String src, String format) throws ParseException {   
		SimpleDateFormat DateFormat = new SimpleDateFormat(format);  
		String week = DateFormat.parse(src).toString().split(" ")[0];
        return week.equals("Sun") || week.equals("Sat");
	} 
	 
}
