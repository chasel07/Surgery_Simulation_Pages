package page1.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import page1.model.*;
import page1.view.*;
    

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

	private static JFrame JF_SUB_WINDOW;

	public static final JPanel JP_SHOW = new JPanel(new BorderLayout());
	public static final MenuBar JP_MENU = new MenuBar();

	public static Page1_initialPanel PAGE_1;

	public static boolean IS_HOME_PAGE = true;
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
 
  
	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
		initialFrame();
		showFrame(); 
		setLogger();
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
		double h = JF_SCALE * Toolkit.getDefaultToolkit().getScreenSize().height;// ���o�ù�������
		double w = JF_SCALE * Toolkit.getDefaultToolkit().getScreenSize().width;// ���o�ù����e��
		JF_SIZE = new Dimension((int) w, (int) h); // �t�m�����j�p

		JF_SYSTEM.setIconImage(ImageIO.read(new File(FileRoute.IMAGE_LOGO)));
		JF_SYSTEM.setBackground(Color.black);
		JF_SYSTEM.setSize(JF_SIZE);
		JF_SYSTEM.setLayout(new BorderLayout());
		LayoutController.setMainFrame(JF_SYSTEM);

		GBC_PANEL = (JPanel) JF_SYSTEM.getContentPane();
		GBC_PANEL.setBackground(Color.white);
		GBC_PANEL.add(JP_MENU, BorderLayout.NORTH);
		GBC_PANEL.add(JP_SHOW, BorderLayout.CENTER);

		PAGE_1 = new Page1_initialPanel();
		JP_SHOW.add(PAGE_1, BorderLayout.CENTER);
	}

	/**
	 * �^����
	 */
	public static void backHome() {
		switchPage(PAGE_1);
	}

	/**
	 * �e������
	 * 
	 * @param src
	 */
	public static void switchPage(JPanel src) {
		IS_HOME_PAGE = (src == PAGE_1);
		switchPage(JP_SHOW, src);
	}

	public static void switchPage(JPanel Panel, Component replacer) {
		Panel.removeAll();
		Panel.add(replacer, BorderLayout.CENTER);
		Panel.repaint();
		JF_SYSTEM.setVisible(true);
	}
	
	/**
	 * �������A�O��
	 * 
	 * @param id 
	 */
	public static void switchLights(int id) { 
		ArrayList<JLabel> jl = Main.JP_MENU.getjl_route();
		for (JLabel i : jl) {
			i.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		}
		
		if(id != -1) {
			jl.get(id).setForeground(ColorManager.COLOR_MENUBAR_TEXT_LIGHT);
		} 
	}
	

	public static void showFrame() {
		double h = JF_SIZE.height * (1 - JF_SCALE) / 2;
		double w = JF_SIZE.width * (1 - JF_SCALE) / 2;
		JF_SYSTEM.setLocation((int) w, (int) h);
		JF_SYSTEM.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JF_SYSTEM.setVisible(true);
	}

	public static void setLoadingText(String fileName, double Progress) {
		PAGE_1.setLoadingText(fileName, Progress);
	}

	public static void clearSelected() {
		PAGE_1.clearSelected();
	}

	public static void SetSubwindow(JFrame Src) {
		JF_SUB_WINDOW = Src;
		JF_SUB_WINDOW.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// �]�w�D�e�����y�{����A�C(�����䪺���@�C)
				ArrayList<JLabel> jl = Main.JP_MENU.getjl_route();
				for (JLabel i : jl) {
					i.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
				}
				jl.get(4).setForeground(ColorManager.COLOR_MENUBAR_TEXT_LIGHT);
			}
		});
		JF_SUB_WINDOW.setVisible(true);
	}

	public static JFrame GetSubwindow() {
		return JF_SUB_WINDOW;
	}

}
