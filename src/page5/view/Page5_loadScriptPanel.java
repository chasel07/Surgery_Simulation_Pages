package page5.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap; 

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder; 
import javax.swing.table.DefaultTableModel;

import page5.controller.*;
import page5.model.*;

import javax.swing.JPanel;

public class Page5_loadScriptPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * Data
	 * 
	 */  
	private static final TreeMap<String, String> ScheduleType = new TreeMap<String, String>();
	static { 
		ScheduleType.put("������A��","FIFO");
		ScheduleType.put("�u�M����", "SVF");
		ScheduleType.put("���M����", "BVF");
	} 
	
	private String field_name;
	private Boolean Previous;
	private String[] next_page_script_parameter;
	private String srcFile = "";

	private ArrayList<String[]> calender;
	private String[] calender_title;

	private final int fill_B = GridBagConstraints.BOTH;
	private final int fill_H = GridBagConstraints.HORIZONTAL;
	private final int fill_N = GridBagConstraints.NONE;

	private final int alignL = GridBagConstraints.WEST;
	private final int alignC = GridBagConstraints.CENTER;
	private final int alignR = GridBagConstraints.EAST;

	/**
	 * View
	 */
	private JComboBox<String> input_ScheduleMode;
	private JTextField input_RoomAmount;
	private JFormattedTextField input_RoomMaximum;
	private JFormattedTextField input_OnlyEmgRooms;

	private JPanel gbc_panel, thisPanel;
	private GridBagConstraints gbc = new GridBagConstraints();

	private final JFrame jfSystem = Main.JF_SYSTEM;
	//����ƹw����
	private JPanel preview;

	private JTextField selectCol = null;
	private JTable Display;
	private JLabel status;
	
	public JPanel getPanel() {
		return preview;
	}
	
	/**
	 * ���y�{�T��i�J�I�ϥΤ��غc�l
	 * @param temp
	 * @param Previous
	 * @throws IOException
	 * @throws ParseException
	 */
	public Page5_loadScriptPanel(File temp, Boolean Previous) throws IOException, ParseException {
		this.Previous = Previous;
		this.srcFile  = temp.toString();
		 
		SimulationParametersModel model = new SimulationParametersModel();
 
		SimulationScript Script = SimulationScript.FileReader(srcFile);
		this.calender = Script.getCalender();
		this.calender_title = Script.getCalendar_title();
		
		// -------Ū���ɮצW�١BŪ���ɮ׫إߤ��-------
		String[] src1 = temp.getName().split("\\\\");
		String[] src2 = src1[src1.length - 1].split("\\.");
		Calendar c = Calendar.getInstance();
		BasicFileAttributes attrs = Files.readAttributes(temp.toPath(), BasicFileAttributes.class);
		c.setTimeInMillis(attrs.creationTime().toMillis());
		this.set_label_value(src2[0], new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()), Script.getscript_paramater());

		// View
		JPanel centerPanel = PreviewPanel();
		JPanel rightPanel  = controlPanel(Script, model); 
		centerPanel.setBackground(ColorManager.COLOR_WHITE);
		rightPanel.setBackground(ColorManager.COLOR_WHITE);

		this.setLayout(new GridBagLayout());
		gbc_panel = this;
		setGBC(15, 1, 0, 0, 1, 1, alignL, fill_B, centerPanel);
		setGBC(1, 1, 1, 0, 1, 1, alignR, fill_B, rightPanel);
		Main.switchPage(this); // ��������
	}

	/**
	 * �Ω���page4���͹w���e����
	 * 
	 * @param path
	 * @param name
	 * @throws IOException
	 * @throws ParseException
	 */
	public Page5_loadScriptPanel(String path, String name) throws IOException, ParseException {
		field_name = name;
		
		SimulationScript Script = SimulationScript.FileReader(path);
		calender = Script.getCalender();
		calender_title = Script.getCalendar_title();
		preview = PreviewPanel();
	}

	/**
	 * �׹� "��������ݩ�" ����ܤ覡
	 * 
	 * @param name
	 * @param date
	 * @param src
	 */
	public void set_label_value(String name, String date, String[] src) { 
		//�����ɶ�����
		double time = Double.valueOf(src[0]);
		int m = (int) time;
		int d = m / 1440;
		m = m - d * 1440;
		int h = m / 60;
		m = m - h * 60;
		StringBuilder str = new StringBuilder();
		str.append(d).append("��");
		if(h != 0) str.append(h).append("�p��");
		if(m != 0) str.append(h).append("����"); 
			  
		String[] script_parameter = { name, date, str.toString(), src[1], src[2], src[3], src[4], src[5] };
		next_page_script_parameter = script_parameter; 
	}

	/**
	 * �����ѼƳ]�w������
	 * 
	 * @return
	 * @throws ParseException
	 */
	private JPanel controlPanel(SimulationScript Script, SimulationParametersModel model) throws ParseException {
		JPanel TopPanel = new JPanel();
		TopPanel.setLayout(new BorderLayout());
		TopPanel.setBorder(getTitleBorder("�����ѼƳ]�w"));
		TopPanel.setPreferredSize(new Dimension(300, TopPanel.getHeight()));
		TopPanel.setBackground(ColorManager.COLOR_WHITE);

		gbc_panel = new JPanel();
		gbc_panel.setBackground(ColorManager.COLOR_WHITE);
		gbc_panel.setLayout(new GridBagLayout());

		JLabel ScheduleMode = new JLabel("��e�Ƶ{�Ҧ�");
		ScheduleMode.setFont(Main.FONT_TEXT_BOLD);
		input_ScheduleMode = new JComboBox<String>();
		input_ScheduleMode.setFont(Main.FONT_TEXT);
		for (String key : ScheduleType.keySet()) {
			input_ScheduleMode.addItem(key);
		}

		JLabel RoomAmount = new JLabel("��N���`����");
		RoomAmount.setFont(Main.FONT_TEXT_BOLD);
		input_RoomAmount = new JTextField();
		input_RoomAmount.setText(String.valueOf(Script.getRoomName().length));
		input_RoomAmount.setFont(Main.FONT_TEXT);
		input_RoomAmount.setEditable(false);

		JLabel RoomMaximum = new JLabel("�̦h�i�ζ���");
		RoomMaximum.setFont(Main.FONT_TEXT_BOLD);
		// �̤j�i�ζ���
		input_RoomMaximum = new JFormattedTextField();
		input_RoomMaximum.setText("");
		input_RoomMaximum.setFont(Main.FONT_TEXT);
		input_RoomMaximum.addCaretListener( e -> {
			String text = input_RoomMaximum.getText().toString();
			if (!text.isEmpty() && text.matches("[+]?\\d*")) {
				model.setRoomMaxN(Integer.valueOf(text));
			} else {
				model.setRoomMaxN(-1);
			} 
		});

		JLabel OnlyEmgRooms = new JLabel("��M�M�ζ���");
		OnlyEmgRooms.setFont(Main.FONT_TEXT_BOLD);
		// ��M�M�ζ���
		input_OnlyEmgRooms = new JFormattedTextField();
		input_OnlyEmgRooms.setText("");
		input_OnlyEmgRooms.setFont(Main.FONT_TEXT);
		input_OnlyEmgRooms.addCaretListener(e -> {
			String text = input_OnlyEmgRooms.getText().toString();
			if (!text.isEmpty() && text.matches("[+]?\\d*")) {
				model.setEORoomN(Integer.valueOf(text));
			} else {
				model.setEORoomN(-2);
			} 
		});

		// ������ܳ]�w
		setGBC(1, 1, 0, 1, 1, 1, alignR, fill_N, ScheduleMode);
		setGBC(1, 1, 1, 1, 1, 1, alignL, fill_H, input_ScheduleMode);
		setGBC(1, 1, 0, 2, 1, 1, alignR, fill_N, RoomAmount);
		setGBC(1, 1, 1, 2, 1, 1, alignL, fill_H, input_RoomAmount);
		setGBC(1, 1, 0, 3, 1, 1, alignR, fill_N, RoomMaximum);
		setGBC(1, 1, 1, 3, 1, 1, alignL, fill_H, input_RoomMaximum);
		setGBC(1, 1, 0, 4, 1, 1, alignR, fill_N, OnlyEmgRooms);
		setGBC(1, 1, 1, 4, 1, 1, alignL, fill_H, input_OnlyEmgRooms);
		setGBC(1, 1, 0, 6, 2, 1, alignC, fill_H, Parameters_window());
		spacer(2);
		setGBC(0, 0, 1, 8, 1, 1, alignR, fill_H, getCreateBtn(Script, model));

		TopPanel.add(gbc_panel, BorderLayout.CENTER);
		if (Previous) {
			JButton CreateBtn = new JButton("�W�@�B");
			CreateBtn.setFont(Main.FONT_TEXT);
			setGBC(0, 0, 0, 8, 1, 1, alignL, fill_H, CreateBtn);
			CreateBtn.addActionListener( e -> { 
				
				ArrayList<JLabel> jl = Main.JP_MENU.getjl_route();
				for (JLabel i : jl) {
					i.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
				}
				jl.get(3).setForeground(ColorManager.COLOR_MENUBAR_TEXT_LIGHT);

				Main.switchPage(thisPanel);
				 
			});
		}
		return TopPanel;
	}

	/**
	 * ����ݩ���ܪ�����
	 * @return
	 */
	public JPanel Parameters_window() {
		String[] parameters_name = { 
				"�@���W��:", 
				"�Ыؤ��:", 
				"�����ɶ�����:", 
				"�C�饭����F�f�w�H��:", 
				"�O�_���ۭq�@��:", 
				"��e�ɶ�:",
				"�M��ɶ�:",
				"�üƺؤl:"
		};
		
		if (next_page_script_parameter[4].equals("0")) 
		{
			next_page_script_parameter[4] = "�_";
		} 
		else 
		{
			next_page_script_parameter[2] = String.format("�ۭq %s", next_page_script_parameter[2]);
			next_page_script_parameter[3] = String.format("�ۭq %s�H", next_page_script_parameter[3]);
			next_page_script_parameter[4] = "�O";
			next_page_script_parameter[5] = String.format("�ۭq %s����", next_page_script_parameter[5]);
			next_page_script_parameter[6] = String.format("�ۭq %s����", next_page_script_parameter[6]);
			next_page_script_parameter[7] = String.format("�ۭq %s", next_page_script_parameter[7]);
		}
		
		/* View */
		JLabel title = new JLabel("�@���Ѽ�");
		title.setFont(Main.FONT_TITLE);
		
		JLabel[] label_name = new JLabel[8];
		JLabel[] label_value= new JLabel[8];

		TitledBorder line = new TitledBorder(getTitleBorder("�@���Ѽ�"));
		
		JPanel Panel = new JPanel();
		Panel.setBackground(ColorManager.COLOR_WHITE);
		Panel.setBorder(line);
		Panel.setBackground(ColorManager.COLOR_PREVIEWPANEL_BACKGOUND);
		Panel.setLayout(new GridBagLayout());

		Insets ins = gbc.insets;
		gbc.insets = new Insets(10, 10, 10, 10);
		for (int i = 0; i < next_page_script_parameter.length; i++) {
			label_name[i] = new JLabel(parameters_name[i]);
			label_name[i].setFont(Main.FONT_TEXT_BOLD);
			label_value[i] = new JLabel(next_page_script_parameter[i]);
			label_value[i].setFont(Main.FONT_TEXT);
			
			setGBC(0, i + 1, 1, 1, 1, 1, alignR, fill_N);
			Panel.add(label_name[i], gbc);
			setGBC(2, i + 1, 1, 1, 1, 1, alignL, fill_N);
			Panel.add(label_value[i], gbc);
		}
		gbc.insets = ins;
		
		return Panel;
	}

	private JButton getCreateBtn(SimulationScript Script, SimulationParametersModel model) {
		JButton CreateBtn = new JButton("�i�����");
		CreateBtn.setFont(Main.FONT_TEXT);

		CreateBtn.addActionListener( e -> {
			boolean isExecutable = getSimulationParameters(Script, model);

			if (isExecutable) {
				processOtherSchedule(Script, model);  
			}
		});
		return CreateBtn;
	}
		
	private void processOtherSchedule(SimulationScript Script, SimulationParametersModel model) { 
		Main.switchLights(5);
		JOptionPane.showMessageDialog(jfSystem, "��ܼ������G");
		Main.switchLights(4);
	}
	
	
	/**
	 * @return ��ƹw�����
	 */
	private JPanel PreviewPanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(ColorManager.COLOR_WHITE);

		
		String[] file = this.srcFile.split("\\\\");
		if (!file[file.length - 1].equals(""))
			tablePanel.setBorder(getTitleBorder("�ɦW�G" + file[file.length - 1]));
		else
			tablePanel.setBorder(getTitleBorder("�ɦW�G" + field_name));


		status = new JLabel("�N��", SwingConstants.RIGHT);
		JPanel jpStatus = new JPanel(new BorderLayout());
		jpStatus.setBackground(ColorManager.COLOR_WHITE);
		jpStatus.add(status, BorderLayout.WEST);

		Display = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		Display.getTableHeader().setReorderingAllowed(false);
		Display.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		Display.setRowHeight(Main.TEXT_SIZE + 20);
		Display.setSelectionForeground(ColorManager.COLOR_TABLE_FOREGOUND);
		Display.setSelectionBackground(ColorManager.COLOR_TABLE_BACKGOUND);
		Display.setDragEnabled(false);
		Display.setFont(Main.FONT_TEXT);
		Display.getTableHeader().setFont(Main.FONT_TEXT_BOLD);

		DefaultTableModel deal = (DefaultTableModel) Display.getModel();

		String[] ColumnName = calender_title;
		deal.setColumnCount(0);
		deal.addColumn("");
		for (String i : ColumnName)
			deal.addColumn(i);

		deal.setRowCount(calender.size());

		for (int i = 0; i < calender.size(); i++) {
			deal.setValueAt(String.valueOf(i + 1), i, 0);

			String[] row = calender.get(i);
			for (int j = 0; j < row.length; j++) {
				deal.setValueAt(row[j], i, j + 1);
			}
		}
		Display.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selRow = Display.rowAtPoint(e.getPoint());
				int selCol = Display.columnAtPoint(e.getPoint());
				String Col = Display.getColumnName(selCol);

				if (selRow == -1) {
					Display.clearSelection();
					status.setText("�N��");
				} else {
					changeStatusBar(selRow, selCol, Col);
				} 
			}

			public void mousePressed(MouseEvent e) {
				//Do nothing
			}

			public void mouseReleased(MouseEvent e) {
				//Do nothing
			}

			public void mouseEntered(MouseEvent e) {
				//Do nothing
			}

			public void mouseExited(MouseEvent e) {
				//Do nothing
			}
		});

		TableColumnAdjuster tca = new TableColumnAdjuster(Display);
		tca.adjustColumns();
		tablePanel.add(new JLabel("            "), BorderLayout.NORTH);
		tablePanel.add(new JScrollPane(Display), BorderLayout.CENTER);
		tablePanel.add(jpStatus, BorderLayout.SOUTH);
		return tablePanel;
	}
	
	private void changeStatusBar(int selRow, int selCol, String Col) { 
		if (selectCol != null) {
			if (selCol > 0 && selCol < Display.getColumnCount()) {
				selectCol.setText(Col);
				selectCol = null;
				Display.clearSelection();
				status.setText("�w����ġ]" + Col + "�^��");
			} else {
				status.setText("�����L��!!�Э��s���!!");
			}
		} else {
			if (selCol == 0) {
				Display.setRowSelectionInterval(selRow, selRow);
				status.setText("�w��� �ġ]" + (selRow + 1) + "�^�C");
			} else {
				status.setText("�w��� �ġ]" + (selRow + 1) + "�^�C �ġ]" + Col + "�^��");
			}
		} 
	}

	private TitledBorder getTitleBorder(String title) {
		TitledBorder i = new TitledBorder(title);
		i.setTitleFont(Main.FONT_TITLE_BOLD);
		i.setTitleColor(ColorManager.COLOR_TITLE);
		i.setBorder(Main.LINEBORDER);
		return i;
	}

	private void setGBC(double wx, double wy, int x, int y, int w, int h, int align, int fill, Component A) {
		gbc.fill = fill;
		gbc.anchor = align;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc_panel.add(A, gbc);
	}

	private void setGBC(int x, int y, int w, int h, double wx, double wy, int align, int fill) {
		gbc.fill = fill;
		gbc.anchor = align;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
	}

	private void spacer(int wy) {
		gbc.gridx = 0;
		gbc.gridy = gbc.gridy + 1;
		gbc.weightx = 1;
		gbc.weighty = wy;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 10, 0, 10);
		gbc_panel.add(new JLabel(""), gbc);
	}

	/**
	 * ���o�����Ѽƪ��t�m (�P�ɶi��Ѽ�����)
	 * 
	 * @return true��ܰѼƪ��t�m�L�~�A�Ϥ�false
	 */
	private boolean getSimulationParameters(SimulationScript Script, SimulationParametersModel model) {
		boolean isExecutable = true;

		String Qmode = ScheduleType.get(input_ScheduleMode.getSelectedItem().toString());
		model.setQmode(Qmode);

		int RoomTotal = Script.getRoomName().length;
		model.setRoomTotal(RoomTotal);

		if (model.getRoomMaxN() <= 0) {
			JOptionPane.showMessageDialog(jfSystem, "��N�г̦h�i�ζ��ƥ�����J����ơI", "�����n�D", JOptionPane.ERROR_MESSAGE);
			isExecutable = false;
		} else if (model.getRoomMaxN() > RoomTotal) {
			JOptionPane.showMessageDialog(jfSystem, "�̦h�i�ζ��Ƥ��i�j���N���`����", "�����n�D", JOptionPane.ERROR_MESSAGE);
			isExecutable = false;
		} else if (model.getEORoomN() < 0) {
			JOptionPane.showMessageDialog(jfSystem, "��M�M�ݶ��ƥ�����J�D�t��ơI", "�����n�D", JOptionPane.ERROR_MESSAGE);
			isExecutable = false;
		} else if (model.getEORoomN() >= model.getRoomMaxN()) {
			JOptionPane.showMessageDialog(jfSystem, "��M�M�ݶ��ƥ����p��̦h�i�ζ���", "�����n�D", JOptionPane.ERROR_MESSAGE);
			isExecutable = false;
		}

		return isExecutable;
	}
}
