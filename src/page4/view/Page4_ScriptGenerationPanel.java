package page4.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets; 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.SecureRandom; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar; 
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder; 
import javax.swing.filechooser.FileFilter; 
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
 
import page4.controller.*;
import page4.model.*;

public class Page4_ScriptGenerationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private File script_folder = new File(FileRoute.PATH_SCRIPT);
	private ImageIcon iEmpty = new ImageIcon("");
	private ImageIcon iLoad = new ImageIcon(FileRoute.IMAGE_LOADING_24);
	private ImageIcon iFinish = new ImageIcon(FileRoute.IMAGE_FINISH);
	private ImageIcon iFailure = new ImageIcon(FileRoute.IMAGE_FAILURE);

	private final String Msg_FileNameEmpty = "檔名不可為空";
	private final String Msg_FileNameEmpty_tip = "請輸入一個合適的檔名!!";
	private final String Msg_FileNameErr = "檔名不可包含...";
	private final String Msg_FileNameErr_tip = "檔案名稱不可包含 \\/:*?\"<>| 等字元";
	private final String numberMsg = "請輸入非負整數值";
	private final String numberMsg_tip = "請輸入非負整數值";

	private final String[] str1 = { "劇本名稱", "模擬時間長度(天):", "每日病患平均人數:", "轉送時間(分鐘):", "清潔時間(分鐘):", "新增劇本數", "亂數種子:" };
	private final String[] str2 = { "劇本名稱", "模擬時間長度", "每日病患平均人數", "轉送時間", "清潔時間", "亂數種子" };
	private final String[] str3 = { "未選擇", "1", "2", "3", "4", "5" };
	private final String[] str4 = { "", "", "", "", "", "" };
	private final String[] str5 = { "自訂", "隨機" };

	private final String[] parameters_name = { "目前選擇的劇本", "劇本名稱:", "創建日期:", "模擬時間長度(天):", "每日平均到達病患人數:", "是否為自訂劇本:",
			"轉送時間:", "清潔時間:", "亂數種子:" },
			parameters_value = { "＊＊＊＊＊＊＊＊＊＊", "＊＊＊＊.＊＊.＊＊", "＊＊＊天", "＊＊.0", "＊＊.0", "＊＊.0", "＊＊.0", "＊＊＊＊" };

	private class Range {
		private int lower;
		private int upper;
		public Range(int lower, int upper) { 
			this.lower = lower;
			this.upper = upper; 
		}
		
		public int getUpperBound() {
			return upper; 
		}
		
		public int getLowerBound() {
			return lower; 
		}
	}
	
	private final Range[] constraintInput = { null, new Range(1, 30), new Range(10, 200), new Range(1, 60), new Range(1, 60), new Range(1, Integer.MAX_VALUE)};
	
	private final TextBorderUtils Border_red = new TextBorderUtils(Color.RED, 1, true);
	private final TextBorderUtils Border_black = new TextBorderUtils(Color.black, 1, true);

	private String path = null;// 為選取劇本的路徑
	private Distribution data;
	private String[] next_page_script_parameter;// 劇本參數

	private JTextField[] input;
	private JButton ADD_button;
	private JButton Next_step;

	private JTable jt_ScriptList;

	private int ThreadCount = 0;
	private int curSelectRow = -1;
	private int reject = 0;
	
	public void setCurSelectRow(int row) {
		curSelectRow = row;
	};

	public int getCurSelectRow() {
		return curSelectRow;
	};

	private JLabel jlStatus;

	/**
	 * 設定執行工作後的結果
	 * 
	 * @param msg  提示訊息
	 * @param type = 0 顯示正在執行的圖示</br>
	 *             type = 1 顯示執行成功的圖示</br>
	 *             type = 2 顯示執行失敗的圖示</br>
	 */
	public void setStatus(String msg, int type) {
		jlStatus.setText(msg);
		switch (type) {
		case 0:
			jlStatus.setIcon(iLoad);
			break;
		case 1:
			jlStatus.setIcon(iFinish);
			break;
		case 2:
			jlStatus.setIcon(iFailure);
			break;
		default:
			jlStatus.setIcon(iEmpty);
		}
	}

	private JLabel[] label_value = new JLabel[8];

	 

	private GridBagConstraints gbc = new GridBagConstraints();
	private final int fill_N = GridBagConstraints.NONE;
	private final int fill_B = GridBagConstraints.BOTH;
	private final int fill_H = GridBagConstraints.HORIZONTAL;

	private final int alignL = GridBagConstraints.WEST;
	private final int alignC = GridBagConstraints.CENTER;
	private final int alignR = GridBagConstraints.EAST;

	private Page4_ScriptGenerationPanel Page4 = this;
	private JFrame jframe;
	private JPanel gbc_panel;// this_panel:為page4的Panel
	private JPanel jp_center, jp_right, jp_preview;

	private TreeMap<String, File> script; // 可快速尋找劇本

	public void deleteScript(String target) {
		script.remove(target);
	}

	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------
	private String script_name;
	private int script_num;
	private double SimulationTime;
	private double DailysetNPeople;
	private double TransferTime = 5.0;
	private double CleanTime = 5.0;
	private boolean RandCheck = false;
	private long current_rnd;

	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------
	// 建構子1
	public Page4_ScriptGenerationPanel(Distribution data) {
		this.jframe = Main.JF_SYSTEM;
		this.data = data;

		script = new TreeMap<>();
		findAllFilesInFolder();

		jp_center = ScriptListPanel();
		jp_right = Script_parameter_settings();
		jp_preview = Parameters_window();
		setLayout(new GridBagLayout());
		gbc_panel = this;

		JSplitPane SplitPanel1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jp_center, jp_preview);
		SplitPanel1.setBackground(ColorManager.COLOR_WHITE);
		SplitPanel1.setResizeWeight(0.85);
		SplitPanel1.setOpaque(false);
		SplitPanel1.setEnabled(false);

		JSplitPane SplitPanel2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, jp_right, Next_step);
		SplitPanel2.setBackground(ColorManager.COLOR_WHITE);
		SplitPanel2.setResizeWeight(0.99);
		SplitPanel2.setOpaque(false);
		SplitPanel2.setEnabled(false);

		Object[][] gbcData = {{15, 1, 0, 0, 1, 1, alignR, fill_B, SplitPanel1},
							  { 1, 1, 1, 0, 1, 1, alignL, fill_B, SplitPanel2}}; 
		for(Object[] dataRow: gbcData) { 
			setGBC(dataRow, (Component) dataRow[8]); 
		} 
	}

	// 找出選取的file中所有檔案並用ArrayList紀錄起來
	public void findAllFilesInFolder() {
		script.clear();
		for (File file : script_folder.listFiles()) {
			if (!file.isDirectory()) {
				script.put(file.toString(), file);
			}
		}
	}

	public ArrayList<String[]> getData() {
		ArrayList<String[]> Data = new ArrayList<String[]>();
		BasicFileAttributes attrs;
		Calendar c = Calendar.getInstance();

		for (Entry<String, File> i : script.entrySet()) {
			String[] name = i.getValue().getName().split(".csv");

			Path directoryPath = Paths.get(i.getValue().getPath());
			try {
				attrs = Files.readAttributes(directoryPath, BasicFileAttributes.class);
				c.setTimeInMillis(attrs.creationTime().toMillis());
				String[] s = { name[0], new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()), "刪除", "預覽", "選擇" };
				Data.add(s);
			} catch (IOException e) {
				Main.log(Level.WARNING, e.getMessage());
			}
		}
		return Data;
	}

	/**
	 * 劇本列表Panel
	 */
	private JPanel ScriptListPanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(ColorManager.COLOR_WHITE);
		tablePanel.setBorder(getTitleBorder("劇本選擇"));

		Class[] types = new Class[] { String.class, String.class, String.class, String.class, String.class };

		String[] colName = { "劇本名稱", "創建日期", "刪除", "預覽", "選擇" };// 宣告儲存欄位名稱的字串

		ArrayList<String[]> Data = getData();

		jt_ScriptList = new JTable(new TableModel(Data, colName, types));
		jt_ScriptList.getTableHeader().setReorderingAllowed(false);
		jt_ScriptList.getTableHeader().setFont(Main.FONT_TEXT_BOLD);
		jt_ScriptList.getTableHeader().setBackground(ColorManager.COLOR_BLACK);
		jt_ScriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jt_ScriptList.setRowSelectionAllowed(true); // 設定不選擇整行
		jt_ScriptList.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jt_ScriptList.setBackground(ColorManager.COLOR_WHITE);
		jt_ScriptList.setFont(Main.FONT_TEXT);
		jt_ScriptList.setRowHeight(Main.TEXT_SIZE + 20);

		TableColumnModel tcm = jt_ScriptList.getColumnModel();
		for (int i = 0, n = tcm.getColumnCount(); i < n; i++) {
			TableColumn tc = tcm.getColumn(i);
			tc.setCellRenderer(new DefaultTableCellRenderer() {
				/**
				* 
				*/
				private static final long serialVersionUID = 1L;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {

					if (row == curSelectRow) {
						setForeground(ColorManager.COLOR_TABLEHAND_FOREGOUND);
						setBackground(ColorManager.COLOR_TABLEHAND_BACKGOUND);
						table.setSelectionBackground(ColorManager.COLOR_TABLEHAND_BACKGOUND);
						table.setSelectionForeground(ColorManager.COLOR_TABLEHAND_FOREGOUND);
					} else {
						setForeground(ColorManager.COLOR_BLACK);
						setBackground(ColorManager.COLOR_WHITE);
						table.setSelectionBackground(ColorManager.COLOR_WHITE);
						table.setSelectionForeground(ColorManager.COLOR_BLACK);
					}

					return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}
			});
		}

		jt_ScriptList.getColumnModel().getColumn(2)
				.setCellEditor(new MyButtonEditor(new ImageIcon(FileRoute.IMAGE_DELETE), Page4));

		jt_ScriptList.getColumnModel().getColumn(2)
				.setCellRenderer(new MyButtonRenderer(new ImageIcon(FileRoute.IMAGE_DELETE), Page4));

		jt_ScriptList.getColumnModel().getColumn(3)
				.setCellEditor(new MyButtonEditor(new ImageIcon(FileRoute.IMAGE_PREVIEW), Page4));

		jt_ScriptList.getColumnModel().getColumn(3)
				.setCellRenderer(new MyButtonRenderer(new ImageIcon(FileRoute.IMAGE_PREVIEW), Page4));

		jt_ScriptList.getColumnModel().getColumn(4)
				.setCellEditor(new MyButtonEditor(new ImageIcon(FileRoute.IMAGE_TAP), Page4));

		jt_ScriptList.getColumnModel().getColumn(4)
				.setCellRenderer(new MyButtonRenderer(new ImageIcon(FileRoute.IMAGE_TAP), Page4));
		//////////////////////////////////

		JScrollPane jScrollPane = new JScrollPane(jt_ScriptList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane.getViewport().setBackground(ColorManager.COLOR_WHITE);
		jScrollPane.setOpaque(false);

		tablePanel.add(jScrollPane, BorderLayout.CENTER);
		return tablePanel;
	}
  
	// ------------------------------------------------------------------
	// ------------------------------------------------------------------
	// ------------------------------------------------------------------
	// 參數視窗的Panel
	public JPanel Parameters_window() {
		// 新增處
		JLabel[] label_name = new JLabel[9];

		JPanel Panel = new JPanel();
		Panel.setBorder(Main.LINEBORDER);
		Panel.setBackground(ColorManager.COLOR_PREVIEWPANEL_BACKGOUND);
		Panel.setLayout(new GridBagLayout());

		for (int i = 0; i < parameters_name.length; i++) {
			label_name[i] = new JLabel(parameters_name[i]);
			label_name[i].setFont(Main.FONT_TEXT_BOLD);
		}
		for (int i = 0; i < parameters_value.length; i++) {
			label_value[i] = new JLabel(parameters_value[i]);
			label_value[i].setFont(Main.FONT_TEXT);
		}
 
		label_name[0].setFont(Main.FONT_TITLE);
		label_name[0].setForeground(ColorManager.COLOR_TITLE);

		Insets ins = gbc.insets;
		gbc.insets = new Insets(10, 10, 10, 10);
 
		Object[][] gbcData = {
			{0, 0, 1, 1, 1, 1, alignL, fill_N, label_name[0]},
			
			{0, 1, 1, 1, 1, 1, alignR, fill_N, label_name[1]},
			{1, 1, 1, 1, 1, 1, alignL, fill_N, label_value[0]},
			
			{3, 1, 1, 1, 1, 1, alignR, fill_N, label_name[2]},
			{4, 1, 1, 1, 1, 1, alignL, fill_N, label_value[1]},
			
			{0, 2, 1, 1, 1, 1, alignR, fill_N, label_name[3]},
			{1, 2, 1, 1, 1, 1, alignL, fill_N, label_value[2]},
			
			{3, 2, 1, 1, 1, 1, alignR, fill_N, label_name[4]}, 
			{4, 2, 1, 1, 1, 1, alignL, fill_N, label_value[3]},
			
			{6, 2, 1, 1, 1, 1, alignR, fill_N, label_name[5]},
			{7, 2, 1, 1, 1, 1, alignL, fill_N, label_value[4]},

			{0, 3, 1, 1, 1, 1, alignR, fill_N, label_name[6]},
			{1, 3, 1, 1, 1, 1, alignL, fill_N, label_value[5]},

			{3, 3, 1, 1, 1, 1, alignR, fill_N, label_name[7]},
			{4, 3, 1, 1, 1, 1, alignL, fill_N, label_value[6]},

			{6, 3, 1, 1, 1, 1, alignR, fill_N, label_name[8]},
			{7, 3, 1, 1, 1, 1, alignL, fill_N, label_value[7]}
		}; 
		 
		for(Object[] dataRow: gbcData) { 
			setGBC(dataRow); 
			Panel.add((Component) dataRow[8], gbc);
		} 
		
		gbc.insets = ins;
		return Panel;
	}

	class myFilter extends FileFilter {
		String extension, description;

		public myFilter(String e, String d) {
			extension = e.toLowerCase();
			description = d;
		}

		@Override
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			String e = null;
			String s = f.getName();
			int i = s.lastIndexOf(".");
			if (i > 0 && i < s.length() - 1) {
				e = s.substring(i + 1).toLowerCase();
				if (extension.equals(e))
					return true;
			}
			return false;
		}

		@Override
		public String getDescription() {
			return this.description;
		}
	}

	public void set_path(String src) {
		path = src;
	}

	public void reset_label_value() {
		next_page_script_parameter = parameters_value;
		for (int i = 0; i < parameters_value.length; i++) {
			label_value[i].setText(parameters_value[i]);
		}
	}

	public void set_label_value(String name, String date, String[] src) {
		int i = 0;
		next_page_script_parameter = new String[8];

		label_value[i].setText(name);
		next_page_script_parameter[i++] = name;
		
		label_value[i].setText(date);
		next_page_script_parameter[i++] = date;
		
		double time = Double.valueOf(src[0]);
		int m = (int) time, d;
		d = m / 1440;
		String str = String.valueOf(d) + "天";
		label_value[i].setText(str);
		next_page_script_parameter[i++] = str;
		
		label_value[i].setText(src[1]);
		next_page_script_parameter[i++] = src[1];
		
		if (src[2].equals("0")) {
			label_value[i].setText("否");
		} else {
			label_value[i].setText("是");
		}
		next_page_script_parameter[i++] = src[2];
		
		label_value[i].setText(src[3]);
		next_page_script_parameter[i++] = src[3];
		
		label_value[i].setText(src[4]);
		next_page_script_parameter[i++] = src[4];
		
		label_value[i].setText(src[5]);
		next_page_script_parameter[i++] = src[5];
		
		Main.log(Level.FINEST, String.format("Set %d label value done.", i));
	}

	private void createNewScript(int id) { 
		int result = 0;
		boolean cover = false; 
		
		String FileName = String.format("%s/%s-%d.csv", FileRoute.PATH_SCRIPT, script_name.toLowerCase(), id); 

		TreeSet<String> script_lower = new TreeSet<String>();
		for (String i : script.keySet()) {
			script_lower.add(i.toLowerCase());
		}

		if (script_lower.contains(FileName)) {
			String curSelectName = label_value[0].getText().toLowerCase();

			cover = curSelectName.equals(FileName.toLowerCase());
			  
			StringBuilder str = new StringBuilder("發現已存在 ");
			str.append(FileName).append(".csv 此檔案\n是否要直接覆蓋？");

			result = JOptionPane.showConfirmDialog(null, str.toString(), "發現重複檔案", JOptionPane.YES_NO_OPTION);
		}

		// 隨機種子設置
		if (RandCheck == true) {
			SecureRandom random = new SecureRandom();
			current_rnd = (int) (16383 * random.nextDouble());
		} else {
			current_rnd = Long.parseLong(input[5].getText());
		}

		if (result == 0) { // 如果沒有找到重複的檔案時
			jlStatus.setText("建立劇本中...");
			jlStatus.setIcon(iLoad);
			try {
				Thread loading = new Thread(new CreateScript(id, cover));
				loading.start();
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				Main.log(Level.WARNING, e1.getMessage());
				Thread.currentThread().interrupt();
			}
		} else {
			reject++;
			ThreadCount--;
			setStatus("", 4);
		} 
	}
	
	private void createAddButton() {
		ADD_button = new JButton("新增劇本");
		ADD_button.setFont(Main.FONT_TEXT);
		ADD_button.addActionListener(e -> {  
			if (!check_script_parameter()) return;
			  
			script_name = input[0].getText();
			SimulationTime = Double.parseDouble(input[1].getText());
			DailysetNPeople = Double.parseDouble(input[2].getText());
			TransferTime = Double.parseDouble(input[3].getText());
			CleanTime = Double.parseDouble(input[4].getText());
			ThreadCount = script_num;
			 
			reject = 0;
			for (int j = 1; j <= script_num; j++) { 
				createNewScript(j);
			}

			for (int i = 0; i < jt_ScriptList.getRowCount(); i++) {
				if (jt_ScriptList.getValueAt(i, 0).toString().equals(label_value[0].getText().toString())) 
				{ 
					curSelectRow = i;
					break; 
				}
			}

			if (reject != script_num) {
				Thread loading = new Thread(new UpdateScript());
				loading.start();
			} 
			
			// 設定按鈕進表格內 
			jt_ScriptList.getColumnModel().getColumn(2)
					.setCellEditor(new MyButtonEditor(new ImageIcon(FileRoute.IMAGE_DELETE), Page4));

			jt_ScriptList.getColumnModel().getColumn(2)
					.setCellRenderer(new MyButtonRenderer(new ImageIcon(FileRoute.IMAGE_DELETE), Page4));

			jt_ScriptList.getColumnModel().getColumn(3)
					.setCellEditor(new MyButtonEditor(new ImageIcon(FileRoute.IMAGE_PREVIEW), Page4));

			jt_ScriptList.getColumnModel().getColumn(3)
					.setCellRenderer(new MyButtonRenderer(new ImageIcon(FileRoute.IMAGE_PREVIEW), Page4));

			jt_ScriptList.getColumnModel().getColumn(4)
					.setCellEditor(new MyButtonEditor(new ImageIcon(FileRoute.IMAGE_TAP), Page4));

			jt_ScriptList.getColumnModel().getColumn(4)
					.setCellRenderer(new MyButtonRenderer(new ImageIcon(FileRoute.IMAGE_TAP), Page4));

			findAllFilesInFolder(); 
		}); 
	}
	 
	private void createNextStepButton() { 
		Next_step = new JButton("下一步");
		Next_step.setFont(Main.FONT_TEXT);
		Next_step.setBackground(ColorManager.COLOR_WHITE);
		Next_step.addActionListener(e -> { 
			if (path == null) {
				setStatus("", 4);
				JOptionPane.showMessageDialog(jframe, "請在列表中選擇一個劇本！", "劇本尚未選擇", JOptionPane.ERROR_MESSAGE);
				return;
			}
				 
			setStatus("載入劇本中...", 0);
			try {
				Thread loading = new Thread(() -> { 
					Main.switchLights(4);
					jlStatus.setIcon(iFinish);
					jlStatus.setText("等待切換頁面..."); 
					JOptionPane.showMessageDialog(jframe, "劇本產生頁面"); 
					
					Main.switchLights(3);
					Main.switchPage(Page4); 
					jlStatus.setIcon(iEmpty);
					jlStatus.setText("");   
				});
				loading.start();
				Thread.sleep(1);
			} catch (InterruptedException e1) {
				Main.log(Level.WARNING, e1.getMessage());
				Thread.currentThread().interrupt();
			} 
		}); 
	}
	 
	public JPanel Script_parameter_settings() {

		JLabel[] lable = new JLabel[14];
		for (int i = 0; i < str1.length; i++) {
			lable[i] = new JLabel(str1[i]);
			lable[i].setFont(Main.FONT_TEXT_BOLD);
		}

		input = new JTextField[str4.length];
		input[0] = createTextField("script_name", str4[0]);
		for (int i = 1; i < str4.length; i++) {
			input[i] = createTextField("Number", str4[i]);
		}

		jlStatus = new JLabel("", SwingConstants.LEFT);
		jlStatus.setHorizontalTextPosition(SwingConstants.RIGHT);
		jlStatus.setOpaque(false);
		jlStatus.setFont(Main.FONT_TEXT);
		jlStatus.setBackground(ColorManager.COLOR_WHITE);

		createAddButton();
		createNextStepButton();
 
		JComboBox<String> combobox3 = createCombobox3();
		JComboBox<String> combobox2 = createCombobox2(combobox3);
	     
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(getTitleBorder("劇本參數設定"));
		panel.setBackground(ColorManager.COLOR_WHITE);
		panel.setPreferredSize(new Dimension(300, panel.getHeight()));
  
		gbc.insets = new Insets(0, 5, 0, 5); 
		Object[][] gbcData = {
			{0, 0, 1, 1, 1, 1, this.alignR, this.fill_N, lable[0]},
			{1, 0, 1, 1, 1, 1, this.alignL, this.fill_H, input[0]},

			{0, 1, 1, 1, 1, 1, this.alignR, this.fill_N, lable[1]},
			{1, 1, 1, 1, 1, 1, this.alignL, this.fill_H, input[1]},

			{0, 2, 1, 1, 1, 1, this.alignR, this.fill_N, lable[2]},
			{1, 2, 1, 1, 0, 0, this.alignL, this.fill_H, input[2]},

			{0, 3, 1, 1, 1, 1, this.alignR, this.fill_N, lable[3]},
			{1, 3, 1, 1, 1, 1, this.alignL, this.fill_H, input[3]},

			{0, 4, 1, 1, 1, 1, this.alignR, this.fill_N, lable[4]},
			{1, 4, 1, 1, 1, 1, this.alignL, this.fill_H, input[4]},

			{0, 5, 1, 1, 1, 1, this.alignR, this.fill_N, lable[5]},
			{1, 5, 1, 1, 1, 1, this.alignL, this.fill_H, combobox2},

			{0, 6, 1, 1, 1, 1, this.alignR, this.fill_N, lable[6]},
			{1, 6, 1, 1, 1, 1, this.alignL, this.fill_H, combobox3},

			{1, 7, 1, 1, 1, 1, this.alignL, this.fill_H, input[5]} 
		}; 
		for(Object[] dataRow: gbcData) {
			setGBC(dataRow);
			panel.add((Component) dataRow[8], gbc);
		}
		
		gbc.insets = new Insets(50, 0, 50, 0); 
		Object[] temp = {1, 14, 1, 100, 1, 1, this.alignC, this.fill_B};
		setGBC(temp); 
		panel.add(new JLabel("                     "), gbc);
 
		gbc.insets = new Insets(0, 5, 0, 5); 
		Object[][] gbcData2 = { 
			{1, 114, 1, 1, 0, 0, this.alignR, this.fill_H, ADD_button},
			{0, 114, 2, 1, 0, 0, this.alignR, this.fill_H, jlStatus}
		};
		for(Object[] dataRow: gbcData2) {
			setGBC(dataRow);
			panel.add((Component) dataRow[8], gbc);
		}
		 
		return panel;
	}
	
	private JComboBox<String> createCombobox2(JComboBox<String> combobox3) {
		//set Jcombox 's text in the center
		DefaultListCellRenderer dlcr = new DefaultListCellRenderer(); 
		dlcr.setHorizontalAlignment(SwingConstants.CENTER);
		
		JComboBox<String> combobox2 = new JComboBox<String>(str3);
		combobox2.setFont(Main.FONT_TEXT);
		combobox2.setRenderer(dlcr); 
		combobox2.addActionListener(ev -> {
			// 如果選擇沒有選到到未選擇
			if (combobox2.getSelectedIndex() != 0)
				script_num = Integer.valueOf(combobox2.getSelectedItem().toString());
			else
				script_num = 0;

			// 如果選擇數大於1則只能使用隨機
			if (script_num > 1) {
				combobox3.setSelectedIndex(1);
				combobox3.setEnabled(false);
				combobox3.setBackground(new Color(240, 240, 240));
			} else {
				combobox3.setEnabled(true);
			}
		}); 
		
		return combobox2;
	}
	
	private JComboBox<String> createCombobox3(){
		//set Jcombox 's text in the center
		DefaultListCellRenderer dlcr = new DefaultListCellRenderer(); 
		dlcr.setHorizontalAlignment(SwingConstants.CENTER);
		 
		JComboBox<String> combobox3 = new JComboBox<String>(str5); 
		combobox3.setFont(Main.FONT_TEXT);
		combobox3.setRenderer(dlcr);
		combobox3.setOpaque(true);
		combobox3.addActionListener(ev -> {
			if (combobox3.getSelectedItem().toString().equals("隨機")) {
				SecureRandom random = new SecureRandom();
				int rand = (int) (16383 * random.nextDouble());
				input[5].setText("");
				input[5].setEditable(false);
				input[5].setBorder(Border_black);

				jlStatus.setText("");
				jlStatus.setToolTipText("");
				jlStatus.setIcon(iEmpty);

				current_rnd = rand;
				RandCheck = true;
			} else {
				input[5].setText("");
				input[5].setEditable(true);
				RandCheck = false;
			}
		});
		
		return combobox3;
	}
	
	

	private JTextField createTextField(String Type, String Text) {
		JTextField textInput = new JTextField(Text);
		textInput.setFont(Main.FONT_TEXT);
		textInput.setHorizontalAlignment(SwingConstants.CENTER);
		textInput.setBorder(Border_black);
		textInput.addCaretListener(e -> {
			if (!textInput.isEditable()) {
				return;
			}

			if (Type.equals("script_name")) {
				createTextFieldScriptName(textInput);
			} 
			else {
				String text = textInput.getText();
				if (!text.matches("[+]?\\d+") || text.isEmpty()) {
					textInput.setBorder(Border_red);
					jlStatus.setText(numberMsg);
					jlStatus.setToolTipText(numberMsg_tip);
					jlStatus.setIcon(iFailure);
				} else {
					textInput.setBorder(Border_black);
					jlStatus.setText("");
					jlStatus.setToolTipText("");
					jlStatus.setIcon(iEmpty);
				}
			}
		});
		return textInput;
	}

	private void createTextFieldScriptName(JTextField textInput) { 
		script_name = textInput.getText();
		char[] c = { '\\', '/', ':', '*', '?', '"', '<', '>', '|' };

		if (script_name.isEmpty()) {
			textInput.setBorder(Border_red);
			jlStatus.setText(Msg_FileNameEmpty);
			jlStatus.setToolTipText(Msg_FileNameEmpty_tip);
			jlStatus.setIcon(iFailure);
		} 
		else for (int i = 0; i < c.length; i++) {
			if (script_name.indexOf(c[i]) >= 0) {
				textInput.setBorder(Border_red);
				jlStatus.setText(Msg_FileNameErr);
				jlStatus.setToolTipText(Msg_FileNameErr_tip);
				jlStatus.setIcon(iFailure);
				break;
			} else {
				jlStatus.setText("");
				jlStatus.setToolTipText("");
				jlStatus.setIcon(iEmpty);
				textInput.setBorder(Border_black);
			}
		} 
	}
	 
	public Boolean check_script_parameter() {
		boolean pass = true;

		script_name = input[0].getText();
		char[] c = { '\\', '/', ':', '*', '?', '"', '<', '>', '|' };

		if (script_name.isEmpty()) {
			input[0].setBorder(Border_red);
			jlStatus.setText(Msg_FileNameEmpty);
			jlStatus.setToolTipText(Msg_FileNameEmpty_tip);
			jlStatus.setIcon(iFailure);
			pass = false;
		} 
		else {
			for (int i = 0; i < c.length; i++) {
				if (script_name.indexOf(c[i]) >= 0) {
					input[0].setBorder(Border_red);
					jlStatus.setText(Msg_FileNameErr);
					jlStatus.setToolTipText(Msg_FileNameErr_tip);
					jlStatus.setIcon(iFailure);
					pass = false;
					break;
				} else {
					input[0].setBorder(Border_black);
					jlStatus.setText("");
					jlStatus.setToolTipText("");
					jlStatus.setIcon(iEmpty);
				}
			}
		}
		
		for (int i = 1; i < input.length; i++) { 
			if (!input[i].isEditable()) continue;
			
			String text = input[i].getText();
			if(text.equals("")) { 
				String msg = String.format("\"%s\"為必填欄位", str2[i]);
				JOptionPane.showMessageDialog(new JFrame(), msg, "錯誤訊息", JOptionPane.ERROR_MESSAGE);
			}
			
			boolean isOK = !text.isEmpty() && text.matches("[+]?\\d+") && isCompliantRange(text, constraintInput[i], str2[i]);
			
			input[i].setBorder(isOK ? Border_black : Border_red);  
			jlStatus.setText("");
			jlStatus.setToolTipText("");
			jlStatus.setIcon(iEmpty);
			
			if (!isOK) return isOK; 
		}
		  
		if (!pass) {
			JOptionPane.showMessageDialog(new JFrame(), "新增失敗，劇本參數可能有誤或不完整！", "錯誤訊息", JOptionPane.ERROR_MESSAGE);
		}
		 
		return pass;
	}
	
	
	/**
	 *  驗證數值是否符合範圍
	 * @param value
	 * @param range
	 * @param text
	 * @return
	 */
	private boolean isCompliantRange(Object value, Range range, String text) {  
		double number = Double.parseDouble(value.toString()); 
		double upperLimit = range.getUpperBound();
		double lowerLimit = range.getLowerBound();
		
		if(number >= lowerLimit && number <= upperLimit) { 
			return true; 
		}
		else { 
			String msg = String.format("在\"%s\"中只能輸入 %d ~ %d 的正整數", text, (int)lowerLimit, (int)upperLimit);
			JOptionPane.showMessageDialog(new JFrame(), msg, "錯誤訊息", JOptionPane.ERROR_MESSAGE);
			return false;
		} 
	}
	 
	// ------------------------------------------------------------------
	// ------------------------------------------------------------------
	// ------------------------------------------------------------------
	class ComboBoxRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value.toString().equals("3")) {
				c.setText("X");
				c.setBackground(Color.RED);
			} else {
				c.setText("Y");
				c.setBackground(Color.GREEN);
			}

			return this;
		}
	}

	private TitledBorder getTitleBorder(String title) {
		TitledBorder i = new TitledBorder(title);
		i.setTitleFont(Main.FONT_TITLE_BOLD);
		i.setTitleColor(ColorManager.COLOR_TITLE);
		i.setBorder(Main.LINEBORDER);
		return i;
	}

	// ------------------------------------------------------------------
	// ------------------------------------------------------------------
	// ------------------------------------------------------------------
	class CreateScript implements Runnable { 
		
		int index;
		boolean cover;

		public CreateScript(int index, boolean cover) {
			this.index = index;
			this.cover = cover;
		}

		@Override
		public void run() {
			try {
				SimulationScript ss = new SimulationScript(data);
				ss.setSimulationTime(SimulationTime * 1440);
				ss.setDailysetNPeople(DailysetNPeople);
				ss.setTransferTime(TransferTime);
				ss.setCleanTime(CleanTime);
				ss.setcurrent_rnd(current_rnd);
				ss.run();

				String Name = String.format("%s-%d", script_name.toLowerCase(), index);
				String Route = String.format("%s/%s.csv", FileRoute.PATH_SCRIPT, Name);  
				ss.saveAsFile(Route);
				ThreadCount--;

				if (cover) {
					SimulationScript Script = SimulationScript.FileReader(Route);

					String[] temp = Script.getscript_paramater();
					String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

					Page4.set_label_value(Name, date, temp);
				}
			} catch (Exception e1) {
				ThreadCount = -1;
				setStatus("存檔失敗!!", 2);
				Main.log(Level.WARNING, e1.getMessage());
			}
		}
	}

	class UpdateScript implements Runnable {
		@Override
		public void run() {
			try {
				/**
				 * 等待其他的線程完成
				 */
				while (ThreadCount > 0) {
					long t1 = System.currentTimeMillis();
					long t2 = System.currentTimeMillis();
					while (t2 - t1 < 1000) {
						t2 = System.currentTimeMillis();
					}
					Main.log(Level.INFO, "ThreadCount = " + ThreadCount);
				}
				TableModel Model = (TableModel) jt_ScriptList.getModel();
				Model.addRow(script, script_folder);
				if (ThreadCount == 0) {
					jlStatus.setText("已建立劇本");
					jlStatus.setIcon(iFinish);
				}
			} catch (IOException e1) {
				jlStatus.setText("無法更新劇本列表!!");
				jlStatus.setIcon(iFailure);
			}
		}
	}
 
	//  x  y  w  h  wx  wy  align  fill
	private void setGBC(Object[] args) {
		gbc.gridx = Integer.valueOf(args[0].toString());    
		gbc.gridy = Integer.valueOf(args[1].toString()); 
		gbc.gridwidth = Integer.valueOf(args[2].toString());
		gbc.gridheight = Integer.valueOf(args[3].toString());
		gbc.weightx = Double.valueOf(args[4].toString());
		gbc.weighty = Double.valueOf(args[5].toString());
		gbc.anchor = Integer.valueOf(args[6].toString());
		gbc.fill = Integer.valueOf(args[7].toString()); 
	}
	
	// wx  wy  x  y  w  h  align  fill
	private void setGBC(Object[] args, Component A) { 
		Object[] temp = {args[2], args[3], args[4], args[5], args[0], args[1], args[6], args[7]}; 
		setGBC(temp);  
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc_panel.add(A, gbc);
		gbc_panel.setBackground(ColorManager.COLOR_WHITE);
	}

}
