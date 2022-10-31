package page4.model;

import java.io.*; 
import java.util.*; 
import java.util.logging.Logger;

import page4.controller.Main;
 
////////////////////////////////////////////////////////////////////////////////////
// public CSVReader(String FilePath) 將input的FilePath中的檔案位置輸入至BufferedReader中//
// public String[] readnext() 輸出String陣列 將每一列得到的結果存入回傳的陣列中
// public ArrayList <String[]> readAll() 將資料全部讀取並回傳ArrayList <String[]>
//  
// 
// 
// 
////////////////////////////////////////////////////////////////////////////////////
public class CSVReader { 
	
	private BufferedReader BR; 
	//存放一列的資料
	private ArrayList<String> line;  
	//整理出每一格字串用
	private StringBuilder cell; 
	// 確認字串是否在雙引號中 如果為true代表在引號中 false反之
	Boolean quoteCheck = false; 
	
	private Logger logger;

	public CSVReader(File Src) {
		this(Src.toString());
	}

	public CSVReader(String FilePath) {
		setLogger();

		// 確認是否輸入位址
		if (FilePath == null || FilePath.isEmpty()) {
			logger.warning("沒有輸入檔案位址");
			return;
		}

		int FESerch = FilePath.lastIndexOf("."); // 找尋最後一個.的位置得知副檔名位址
		String FileExtension = FilePath.substring(FESerch).toLowerCase();
		// 如果為CSV檔案繼續 不是則顯示提示
		if (!FileExtension.equals(".csv")) {
			logger.warning("此檔案非CSV檔案");
			return;
		}

		try {
			BR = new BufferedReader(new FileReader(FilePath));
		} catch (FileNotFoundException e) {
			logger.warning("此檔案非CSV檔案");
		}
	}

	public void close() throws IOException {
		BR.close();
	}

	public String[] readNext() { 
		String[] output = null; 
		
		try { 
			quoteCheck = false; 
			//存放一列的資料
			line = new ArrayList<String>();  
			//整理出每一格字串用
			cell = new StringBuilder();
			 
			boolean eof = false; 
			// 如果不是在 "" 內出現換行符號則停止迴圈 
			while (!eof) {	
				Character BRchar = getBR();
				
				if(BRchar == null) {
					eof = true; 
				}
				else if (BRchar == ',' && quoteCheck == false) {
					line.add(cell.toString()); 
					cell = new StringBuilder();
				} 
				else if (BRchar == '"') {
					eof = handleDoubleQuotes(); 
				} 
				else if (BRchar == '\n' && quoteCheck == false) {
					break;
				} 
				else if (BRchar == '\r' || BRchar == '\n') {
					continue;
				} 
				else {
					cell.append(BRchar);
				}    
			} 
			
			if(cell.length() != 0) line.add(cell.toString());  
			
			if(line.size() != 0) {
				output = line.toArray(new String[line.size()]);  
			} 
		} 
		catch (IOException e) 
		{
			logger.warning(e.getMessage()); 
		} 
		catch (NullPointerException e) 
		{  
			logger.warning(e.getMessage()); 
		}
		
		return output;
	}

	private boolean handleDoubleQuotes() throws IOException 
	{  	
		if (!quoteCheck) {
			quoteCheck = true;
			return false;
		}
		
		Character check = getBR(); 
		if(check == '"') {
			cell.append(check); 
		}
		else { 
			quoteCheck = false;
			
			line.add(cell.toString()); 
			cell = new StringBuilder(); 
			while (check != ',' && check != '\n') { 
				check = getBR();  
				if(check != null) return true;
			}
		}   
		
		return false;
	}
	 
	public ArrayList<String[]> readAll() {
		ArrayList<String[]> out = new ArrayList<String[]>();
		String[] S;
		while ((S = readNext()) != null) {
			out.add(S);
		}
		return out;
	}

	private Character getBR() throws IOException {
		int checkN = 0;
		checkN = BR.read();
		if (checkN == -1)
			return null;
		else
			return (char) checkN;
	}

	private void setLogger() {
		logger = Logger.getLogger(Main.class.getName());
	}

}