package page4.controller;

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

import page4.model.*;
import page4.view.*;
 
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

	public static final String SYSTEM_NAME = "手術室資源配置決策輔助系統";
	public static final String FILE_TYPE = "CSV";
	public static final String COMMENT = "分布資料 (." + FILE_TYPE + ")";

	public static final JFrame JF_SYSTEM = new JFrame(SYSTEM_NAME);
	private static final LoadingPanel lOADING_PANEL = new LoadingPanel(); 
	public static final JPanel JP_SHOW = new JPanel(new BorderLayout());
	public static final MenuBar JP_MENU = new MenuBar();
	private static JPanel GBC_PANEL;
	private static Logger LOGGER;
	
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
		switchPage(new Page4_ScriptGenerationPanel(data));
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
	 
	public static void setLoadingText(String src) { 
		lOADING_PANEL.setLoadingText(src); 
	}
	 
	public static void setLoadingText(String fileName, double Progress) {
		lOADING_PANEL.setLoadingText(fileName, Progress);
	} 
}
