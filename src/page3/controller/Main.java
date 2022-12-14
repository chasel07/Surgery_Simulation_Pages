package page3.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import org.jfree.chart.StandardChartTheme;
 
import page3.model.*;
import page3.view.*;
 
public class Main {
	public static double JF_SCALE = 0.9;
	public static Dimension JF_SIZE;
	
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
	public static final MenuBar JP_MENU = new MenuBar();
  
	private static JPanel GBC_PANEL;
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
 
	public static StandardChartTheme getChartTheme() {
		StandardChartTheme theme = new StandardChartTheme("chasel");
		theme.setLargeFont(Main.FONT_TEXT); // 可以改變軸向的字型
		theme.setRegularFont(Main.FONT_TEXT); // 可以改變圖例的字型
		theme.setExtraLargeFont(Main.FONT_TITLE);// 可以改變圖示的標題字型
		return theme;
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		
		setLogger();
		
		initialFrame();
		showFrame();  
	}

	private static void setLogger() {
		LOGGER = Logger.getLogger(Main.class.getName());
	}

	public static void log(Level lv, String msg) {
		LOGGER.log(lv, msg);
	}

	public static void initialFrame() throws IOException {
		if (JF_SCALE > 1 || JF_SCALE <= 0)
			JF_SCALE = 0.8;
		double h = JF_SCALE * Toolkit.getDefaultToolkit().getScreenSize().height;// 取得螢幕的高度
		double w = JF_SCALE * Toolkit.getDefaultToolkit().getScreenSize().width;// 取得螢幕的寬度
		JF_SIZE = new Dimension((int) w, (int) h); // 配置視窗大小

		JF_SYSTEM.setIconImage(ImageIO.read(new File(FileRoute.IMAGE_LOGO)));
		JF_SYSTEM.setBackground(Color.black);
		JF_SYSTEM.setSize(JF_SIZE);
		JF_SYSTEM.setLayout(new BorderLayout());
		LayoutController.setMainFrame(JF_SYSTEM);

		GBC_PANEL = (JPanel) JF_SYSTEM.getContentPane();
		GBC_PANEL.setBackground(Color.white);
		GBC_PANEL.add(JP_MENU, BorderLayout.NORTH);
		GBC_PANEL.add(JP_SHOW, BorderLayout.CENTER);
 
		
		File fileSrc = OpenFile(FILE_TYPE, COMMENT, FileRoute.PATH_DISTRIBUTION); 
		Distribution data = Distribution.loadingFile(fileSrc.toString()); 
		switchPage(new Page3_Distribution_selection(data));
	}
 
	/**
	 * 畫面切換
	 * 
	 * @param src
	 */
	public static void switchPage(JPanel src) { 
		switchPage(JP_SHOW, src);
	}

	public static void switchPage(JPanel Panel, Component replacer) {
		Panel.removeAll();
		Panel.add(replacer, BorderLayout.CENTER);
		Panel.repaint();
		JF_SYSTEM.setVisible(true);
	}
	
	/**
	 * 切換狀態燈號
	 * 
	 * @param id 
	 */
	public static void switchLights(int id) { 
		ArrayList<JLabel> jl = Main.JP_MENU.getjl_route();
		for (JLabel i : jl) {
			i.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		}
		jl.get(id).setForeground(ColorManager.COLOR_MENUBAR_TEXT_LIGHT);  
	}
	

	public static void showFrame() {
		double h = JF_SIZE.height * (1 - JF_SCALE) / 2;
		double w = JF_SIZE.width * (1 - JF_SCALE) / 2;
		JF_SYSTEM.setLocation((int) w, (int) h);
		JF_SYSTEM.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JF_SYSTEM.setVisible(true);
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
	
}
