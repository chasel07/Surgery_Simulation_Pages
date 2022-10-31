package page5.model;

import java.io.BufferedWriter; 
import java.io.FileWriter;
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.logging.Logger;

import page5.controller.Main;

public class CSVWriter {  
	private BufferedWriter BW;
	private Logger logger;

	public CSVWriter(String FilePath) throws IOException {
		setLogger();

		// 確認是否輸入位址
		if (FilePath != null && !FilePath.isEmpty()) {
			BW = new BufferedWriter(new FileWriter(FilePath));
		} else {
			logger.warning("請輸入檔案位址");
		}
	}

	public void WriteNext(String[] Array) {  
		StringBuilder Out = new StringBuilder();

		for (int i = 0; i < Array.length; i++) { 
			if(Array[i] != null) {
				Array[i] = modifySlash(Array[i]); 
				Array[i] = wrapCommaAndNewline(Array[i]); 
				Out.append(Array[i]); 
			} 
			Out.append((i != (Array.length-1)) ? "," : "\n"); 
		}
  
		try {
			BW.write(Out.toString());
		} catch (IOException e) {
			logger.warning(e.getMessage());
		}
	}

	private String modifySlash(String src) {   
		StringBuilder result = new StringBuilder(); 
		while (!src.equals("")) {
			int index = src.indexOf('\"'); 
			if (index < 0) break; 
			result.append(src.substring(0, index)); 
			result.append("\"\"");
			src = src.substring(index+1); 
		} 
		
		return result.append(src).toString();
	}
	
	private String wrapCommaAndNewline(String src)
	{   
		if (src.contains(",") || src.contains("\n")) 
		{
			StringBuilder result = new StringBuilder(); 
			result.append("\"").append(src).append("\"");
			return result.toString();
		}
		
		return src; 
	}
	  
	public void close() throws IOException {
		BW.flush();
		BW.close();
	}

	public void WriteAll(ArrayList<String[]> out) {
		for (int i = 0; i < out.size(); i++) {
			WriteNext(out.get(i));
		}
	}

	private void setLogger() {
		logger = Logger.getLogger(Main.class.getName());
	}

}
