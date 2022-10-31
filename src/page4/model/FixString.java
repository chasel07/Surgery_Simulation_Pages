package page4.model;

public class FixString {
	
	private FixString() {
		// Do Nothing. 
	}
	
	public static String[] filter(String[] line, char sign) {
		StringBuilder s; 
		for(int i = 0; i < line.length; i++) {
			s = new StringBuilder();
			for(int j = 0; j < line[i].length(); j++)
				if(line[i].charAt(j) != sign) s.append(line[i].charAt(j)); 
			line[i] = s.toString();
		}  
		return line;
	}
	
	public static String filter(String str, char sign) {
		StringBuilder s = new StringBuilder();
		for(int j = 0; j < str.length(); j++)
			if(str.charAt(j) != sign) s.append(str.charAt(j)); 
		str = s.toString();
		return str;
	}
}
