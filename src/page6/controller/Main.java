package page6.controller;

import java.awt.BorderLayout;
import java.awt.Color; 
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit; 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame; 
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException; 
import javax.swing.border.LineBorder;

import org.jfree.chart.StandardChartTheme;
 
import page6.model.SimulationScript;
import page6.model.ColorManager;
import page6.model.FileRoute; 
import page6.model.simulation; 
import page6.view.Page6_ReportWindow; 
 

public class Main {
	public static double JF_SCALE = 0.9;
	public static Dimension JF_SIZE;

	public static final ArrayList<String> ColorTable = new ArrayList<>();
	public static final HashMap<String, Color> SubjectColor = new HashMap<>();
	  

	public static Dimension SUB_FRAME_SIZE = new Dimension(
			(int) (0.6 * Toolkit.getDefaultToolkit().getScreenSize().width),
			(int) (0.6 * Toolkit.getDefaultToolkit().getScreenSize().height));

	public static final int TEXT_SIZE = 16;
	public static final int TITLE_SIZE = 20;
	public static final int HEADER_SIZE = 24;

	public static final String FONT_NAME = "微軟正黑體";

	public static final Font FONT_TEXT2 = new Font("細明體", Font.PLAIN, TEXT_SIZE);
	public static final Font FONT_TEXT = new Font(FONT_NAME, Font.PLAIN, TEXT_SIZE);
	public static final Font FONT_TEXT_BOLD = new Font(FONT_NAME, Font.BOLD, TEXT_SIZE);
	public static final Font FONT_TITLE = new Font(FONT_NAME, Font.PLAIN, TITLE_SIZE);
	public static final Font FONT_TITLE_BOLD = new Font(FONT_NAME, Font.BOLD, TITLE_SIZE);
	public static final Font FONT_HEADER = new Font(FONT_NAME, Font.PLAIN, HEADER_SIZE);
	public static final Font FONT_HEADER_BOLD = new Font(FONT_NAME, Font.BOLD, HEADER_SIZE);

	public static final LineBorder LINEBORDER = new LineBorder(ColorManager.COLOR_TABLEHAND_BACKGOUND, 3, true);

	public static final String CHART_TITLE_OF = "完成手術人數";
	public static final String CHART_TITLE_OU = "手術房平均利用率";
	public static final String CHART_TITLE_AW = "病患平均等候時間";
	public static final String CHART_TITLE_MW = "等候區最多人數";
	public static final String CHART_TITLE_RO = "平均超時時間";

	public static final String SYSTEM_NAME = "手術室資源配置決策輔助系統";

	public static final String DAY = "手術日期";
	public static final String ROOM = "手術房別";
	public static final String PRIORITY = "手術級別";
	public static final String SUBJECT = "所屬科別";
	public static final String SUR_START = "手術開始時間";
	public static final String SUR_END = "手術完成時間";
	public static final String ANESTHESIA = "麻醉名稱";
	public static final String ANE_START = "麻醉開始時間";
	public static final String ANE_END = "麻醉完成時間";
	public static final String ARRIVE_TIME = "病患到達時間";
	public static final String ENTRY_TIME = "病患入室時間";
	public static final String DOCTOR = "主治醫師";
	public static final String NUMBER = "病歷號";

	public static final String[] S_COL_NAME = { SUBJECT, "最長手術時間(分鐘)", "最短手術時間(分鐘)", "平均手術時間(分鐘)", "手術時間標準差(分鐘)",
			"手術時間眾數(分鐘)", "手術執行次數" };

	public static final int COL_NAME = 0;
	public static final int COL_MAX = 1;
	public static final int COL_MIN = 2;
	public static final int COL_MODE = 3;
	public static final int COL_AVG = 4;
	public static final int COL_STD = 5;
	public static final int COL_COUNT = 6;
	public static final int COL_DTYPE = 7;
	public static final int COL_OUTLIERS = 8;

	public static final String FILE_TYPE = "CSV";
	public static final String COMMENT = "分布資料 (." + FILE_TYPE + ")";

	private static HashMap<String, String> SEARCH_SUBJECT_NAME = new HashMap<>(); 
	static {
		SEARCH_SUBJECT_NAME.put("AES", "美容科");
		SEARCH_SUBJECT_NAME.put("CRS", "大腸直腸外科");
		SEARCH_SUBJECT_NAME.put("CS", "胸腔外科");
		SEARCH_SUBJECT_NAME.put("CVS", "心臟血管外科");
		SEARCH_SUBJECT_NAME.put("DENT", "口腔醫學科");
		SEARCH_SUBJECT_NAME.put("ENT", "耳鼻喉頭頸外科");
		SEARCH_SUBJECT_NAME.put("ESUR", "放射腫瘤科");
		SEARCH_SUBJECT_NAME.put("GI", "胃腸肝膽科");
		SEARCH_SUBJECT_NAME.put("GS", "一般外科");
		SEARCH_SUBJECT_NAME.put("GU", "泌尿外科");
		SEARCH_SUBJECT_NAME.put("GYN", "產科");
		SEARCH_SUBJECT_NAME.put("NS", "神經外科");
		SEARCH_SUBJECT_NAME.put("OPH", "眼科");
		SEARCH_SUBJECT_NAME.put("ORTH", "骨科");
		SEARCH_SUBJECT_NAME.put("PEDS", "兒童外科");
		SEARCH_SUBJECT_NAME.put("PS", "整形外科");
		SEARCH_SUBJECT_NAME.put("OBS", "婦科");
		SEARCH_SUBJECT_NAME.put("CV", "心臟內科");
		SEARCH_SUBJECT_NAME.put("CM", "胸腔內科");
		SEARCH_SUBJECT_NAME.put("PED", "兒科");
	}

	public static final JFrame JF_SYSTEM = new JFrame(SYSTEM_NAME); 
	public static final JPanel JP_SHOW = new JPanel(new BorderLayout());  
	
	private static Logger LOGGER;

	public static String renameSubject(String name) {
		StringBuilder s = new StringBuilder(name);
		if (SEARCH_SUBJECT_NAME.containsKey(name)) {
			s.append(" - ").append(SEARCH_SUBJECT_NAME.get(name));
		}
		return s.toString();
	}

	public static String[] renameSubject(String[] name) {
		String[] rename = new String[name.length];
		for (int i = 0; i < name.length; i++) {
			rename[i] = renameSubject(name[i]);
		}
		return rename;
	}

	public static void LoadSubjectColor() throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(FileRoute.PATH_COLOR_TABLE))) {
			String line;
			while ((line = in.readLine()) != null) {
				String[] t = line.split("\t");
				if (t.length >= 2) {
					StringBuilder s = new StringBuilder("0x");
					s.append(t[1]);
					ColorTable.add(s.toString());

					if (!t[0].equals("") && !SubjectColor.containsKey(t[0])) {
						SubjectColor.put(t[0], Color.decode(s.toString()));
					}
				}
			}
		}
	}

	public static StandardChartTheme getChartTheme() {
		StandardChartTheme theme = new StandardChartTheme("chasel");
		theme.setLargeFont(Main.FONT_TEXT); // 可以改變軸向的字型
		theme.setRegularFont(Main.FONT_TEXT); // 可以改變圖例的字型
		theme.setExtraLargeFont(Main.FONT_TITLE);// 可以改變圖示的標題字型
		return theme;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException, ParseException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		LoadSubjectColor();  
		setLogger();
		
		String srcFile = OpenFile(FILE_TYPE, COMMENT, FileRoute.PATH_SCRIPT).toString();   
		SimulationScript Script = SimulationScript.FileReader(srcFile); 
		
		String mode = "FIFO";
		int roomMaxAmount = 10;
		int roomForEandOsAmount = 0;
		String title = String.format("模擬結果_先到先服務_最多可用 %d 間_急刀專屬 %d 間", roomMaxAmount, roomForEandOsAmount);
		// 建立模擬器
		simulation simulation = new simulation(Script);  
		// 設置排程模式
		simulation.setQMode(mode);
		// 設置手術房最多可使用間數
		simulation.setMaxRoomN(roomMaxAmount);
		// 設置急刀專用間數
		simulation.setEORoomN(roomForEandOsAmount);
		// View
	    new Page6_ReportWindow(title, simulation); 
	}
 
	private static File OpenFile(String FileType, String comment, String path) { 
	 	myFilter filter = new myFilter(FileType, comment);
		 
	 	JFileChooser select = new JFileChooser(path);  
	 	 
	 	select.setDialogTitle("Open the file");  
	 	select.addChoosableFileFilter(filter);
	 	select.removeChoosableFileFilter(select.getAcceptAllFileFilter());
	 	select.setFileFilter(filter); 
		 
		int result = select.showOpenDialog(JF_SYSTEM); 
		if(result == JFileChooser.APPROVE_OPTION) {
			return select.getSelectedFile();
		}else {
			return null; 
		}   
	}
	  
	private static class myFilter extends javax.swing.filechooser.FileFilter{
		String extension , description;
		public myFilter(String e,String d) {
			extension = e.toLowerCase();
			description = d; 
		} 
		@Override
		public boolean accept(File f) {
			if(f.isDirectory()) return true;
			String e = null;
			String s = f.getName();
			int i = s.lastIndexOf(".");
			if(i > 0 && i < s.length() - 1) {
				e = s.substring(i + 1).toLowerCase();
				if(extension.equals(e))return true;
			} 
			return false;
		}

		@Override
		public String getDescription() {
			return this.description;
		}
	}
	
	private static void setLogger() {
		LOGGER = Logger.getLogger(Main.class.getName());
	}

	public static void log(Level lv, String msg) {
		LOGGER.log(lv, msg);
	}
   
}
