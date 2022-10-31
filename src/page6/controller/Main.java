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

	public static final String FONT_NAME = "�L�n������";

	public static final Font FONT_TEXT2 = new Font("�ө���", Font.PLAIN, TEXT_SIZE);
	public static final Font FONT_TEXT = new Font(FONT_NAME, Font.PLAIN, TEXT_SIZE);
	public static final Font FONT_TEXT_BOLD = new Font(FONT_NAME, Font.BOLD, TEXT_SIZE);
	public static final Font FONT_TITLE = new Font(FONT_NAME, Font.PLAIN, TITLE_SIZE);
	public static final Font FONT_TITLE_BOLD = new Font(FONT_NAME, Font.BOLD, TITLE_SIZE);
	public static final Font FONT_HEADER = new Font(FONT_NAME, Font.PLAIN, HEADER_SIZE);
	public static final Font FONT_HEADER_BOLD = new Font(FONT_NAME, Font.BOLD, HEADER_SIZE);

	public static final LineBorder LINEBORDER = new LineBorder(ColorManager.COLOR_TABLEHAND_BACKGOUND, 3, true);

	public static final String CHART_TITLE_OF = "������N�H��";
	public static final String CHART_TITLE_OU = "��N�Х����Q�βv";
	public static final String CHART_TITLE_AW = "�f�w�������Ԯɶ�";
	public static final String CHART_TITLE_MW = "���԰ϳ̦h�H��";
	public static final String CHART_TITLE_RO = "�����W�ɮɶ�";

	public static final String SYSTEM_NAME = "��N�Ǹ귽�t�m�M�����U�t��";

	public static final String DAY = "��N���";
	public static final String ROOM = "��N�ЧO";
	public static final String PRIORITY = "��N�ŧO";
	public static final String SUBJECT = "���ݬ�O";
	public static final String SUR_START = "��N�}�l�ɶ�";
	public static final String SUR_END = "��N�����ɶ�";
	public static final String ANESTHESIA = "�¾K�W��";
	public static final String ANE_START = "�¾K�}�l�ɶ�";
	public static final String ANE_END = "�¾K�����ɶ�";
	public static final String ARRIVE_TIME = "�f�w��F�ɶ�";
	public static final String ENTRY_TIME = "�f�w�J�Ǯɶ�";
	public static final String DOCTOR = "�D�v��v";
	public static final String NUMBER = "�f����";

	public static final String[] S_COL_NAME = { SUBJECT, "�̪���N�ɶ�(����)", "�̵u��N�ɶ�(����)", "������N�ɶ�(����)", "��N�ɶ��зǮt(����)",
			"��N�ɶ�����(����)", "��N���榸��" };

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
	public static final String COMMENT = "������� (." + FILE_TYPE + ")";

	private static HashMap<String, String> SEARCH_SUBJECT_NAME = new HashMap<>(); 
	static {
		SEARCH_SUBJECT_NAME.put("AES", "���e��");
		SEARCH_SUBJECT_NAME.put("CRS", "�j�z���z�~��");
		SEARCH_SUBJECT_NAME.put("CS", "�ݵĥ~��");
		SEARCH_SUBJECT_NAME.put("CVS", "��Ŧ��ޥ~��");
		SEARCH_SUBJECT_NAME.put("DENT", "�f����Ǭ�");
		SEARCH_SUBJECT_NAME.put("ENT", "�ջ���Y�V�~��");
		SEARCH_SUBJECT_NAME.put("ESUR", "��g�~�F��");
		SEARCH_SUBJECT_NAME.put("GI", "�G�z�x�x��");
		SEARCH_SUBJECT_NAME.put("GS", "�@��~��");
		SEARCH_SUBJECT_NAME.put("GU", "�c���~��");
		SEARCH_SUBJECT_NAME.put("GYN", "����");
		SEARCH_SUBJECT_NAME.put("NS", "���g�~��");
		SEARCH_SUBJECT_NAME.put("OPH", "����");
		SEARCH_SUBJECT_NAME.put("ORTH", "����");
		SEARCH_SUBJECT_NAME.put("PEDS", "�ൣ�~��");
		SEARCH_SUBJECT_NAME.put("PS", "��Υ~��");
		SEARCH_SUBJECT_NAME.put("OBS", "����");
		SEARCH_SUBJECT_NAME.put("CV", "��Ŧ����");
		SEARCH_SUBJECT_NAME.put("CM", "�ݵĤ���");
		SEARCH_SUBJECT_NAME.put("PED", "���");
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
		theme.setLargeFont(Main.FONT_TEXT); // �i�H���ܶb�V���r��
		theme.setRegularFont(Main.FONT_TEXT); // �i�H���ܹϨҪ��r��
		theme.setExtraLargeFont(Main.FONT_TITLE);// �i�H���ܹϥܪ����D�r��
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
		String title = String.format("�������G_������A��_�̦h�i�� %d ��_��M�M�� %d ��", roomMaxAmount, roomForEandOsAmount);
		// �إ߼�����
		simulation simulation = new simulation(Script);  
		// �]�m�Ƶ{�Ҧ�
		simulation.setQMode(mode);
		// �]�m��N�г̦h�i�ϥζ���
		simulation.setMaxRoomN(roomMaxAmount);
		// �]�m��M�M�ζ���
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
