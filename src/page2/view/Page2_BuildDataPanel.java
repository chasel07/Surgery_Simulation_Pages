package page2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import page2.model.*;
import page2.controller.*;

public class Page2_BuildDataPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final int fill_B = GridBagConstraints.BOTH;
	private final int fill_H = GridBagConstraints.HORIZONTAL; 
	private final int fill_N = GridBagConstraints.NONE;

	private final int alignL = GridBagConstraints.WEST;
	private final int alignC = GridBagConstraints.CENTER;
	private final int alignR = GridBagConstraints.EAST;

	private String srcFile = "";
	private Distribution data;

	private final JFrame jfSystem = Main.JF_SYSTEM;
	private final JPanel jpBuild = this;

	private JPanel gbc_panel;
	private GridBagConstraints gbc = new GridBagConstraints();

	/* 確認資料欄位的JTextField */
	private ArrayList<JTextField> setCol = new ArrayList<>();
	/* 手術術式統計資料 */
	private ArrayList<String[]> SD_data;

	/* 記錄目前使用者點選了哪一個 JTextField */
	private JTextField selectCol = null;
	/* 資料預覽畫面 */
	private JTable Display;
	/* 表格下方狀態列的狀態訊息 */
	private JLabel status;

	private Boolean finish = false;

	public Boolean isfinish() {
		return finish;
	}

	public Page2_BuildDataPanel(File srcFile) throws IOException {
		
		if(srcFile != null) {
			this.srcFile = srcFile.toString(); 
			// 必須先使用 distribution 做檔案的初步分析
			Main.showLoading();
			data = new Distribution(this.srcFile);
			// 統計手術術式標準差
			new Page2_Disease(this.srcFile, FileRoute.PATH_SURGERY_TEMP);
			// 取得手術術式標準差資料
			CSVReader Read_SD_Data = new CSVReader(FileRoute.PATH_SURGERY_TEMP);
			SD_data = Read_SD_Data.readAll();
			Read_SD_Data.close();
		} 
		  
		JPanel centerPanel = PreviewPanel();
		JPanel rightPanel = ColumnCheckPanel();
		gbc_panel = this;
		gbc_panel.setLayout(new GridBagLayout());
		setGBC(0, 1, 1, 0, 1, 1, alignR, fill_B, rightPanel);
		setGBC(1, 1, 0, 0, 1, 1, alignL, fill_B, centerPanel);
		finish = true; 
	}

	public void Clear_pageface(JPanel jpDisplay) {
		jpDisplay.removeAll();
	}

	private JPanel ColumnCheckPanel() {
		JPanel TopPanel = new JPanel();
		TopPanel.setLayout(new BorderLayout());
		TopPanel.setBorder(getTitleBorder("欄位確認"));
		TopPanel.setPreferredSize(new Dimension(300, TopPanel.getHeight()));
		TopPanel.setBackground(ColorManager.COLOR_WHITE);

		gbc_panel = new JPanel();
		gbc_panel.setLayout(new GridBagLayout());
		gbc_panel.setBackground(ColorManager.COLOR_WHITE);

		setGBC(1, 1, 0, 1, 1, 1, alignR, fill_N, jlTitle(Main.DAY));
		setGBC(1, 1, 1, 1, 1, 1, alignL, fill_H, inputField(data != null ? data.Day : ""));

		setGBC(1, 1, 0, 2, 1, 1, alignR, fill_N, jlTitle(Main.ROOM));
		setGBC(1, 1, 1, 2, 1, 1, alignL, fill_H, inputField(data != null ? data.Room: ""));

		setGBC(1, 1, 0, 3, 1, 1, alignR, fill_N, jlTitle(Main.PRIORITY));
		setGBC(1, 1, 1, 3, 1, 1, alignL, fill_H, inputField(data != null ? data.priority: ""));

		setGBC(1, 1, 0, 4, 1, 1, alignR, fill_N, jlTitle(Main.SUBJECT));
		setGBC(1, 1, 1, 4, 1, 1, alignL, fill_H, inputField(data != null ? data.subject: ""));

		setGBC(1, 1, 0, 5, 1, 1, alignR, fill_N, jlTitle(Main.SUR_START));
		setGBC(1, 1, 1, 5, 1, 1, alignL, fill_H, inputField(data != null ? data.surStart: ""));

		setGBC(1, 1, 0, 6, 1, 1, alignR, fill_N, jlTitle(Main.SUR_END));
		setGBC(1, 1, 1, 6, 1, 1, alignL, fill_H, inputField(data != null ? data.surEnd: ""));

		setGBC(1, 1, 0, 7, 1, 1, alignR, fill_N, jlTitle(Main.ANESTHESIA));
		setGBC(1, 1, 1, 7, 1, 1, alignL, fill_H, inputField(data != null ? data.anesthesia: ""));

		setGBC(1, 1, 0, 8, 1, 1, alignR, fill_N, jlTitle(Main.ANE_START));
		setGBC(1, 1, 1, 8, 1, 1, alignL, fill_H, inputField(data != null ? data.aneStart: ""));

		setGBC(1, 1, 0, 9, 1, 1, alignR, fill_N, jlTitle(Main.ANE_END));
		setGBC(1, 1, 1, 9, 1, 1, alignL, fill_H, inputField(data != null ? data.aneEnd: ""));

		spacer(1, 4.5);
		setGBC(0, 0, 1, 15, 1, 1, alignR, fill_N, getCreateBtn()); // 更改處
		TopPanel.add(gbc_panel, BorderLayout.CENTER);
		return TopPanel;
	}

	/**
	 * 進入下個畫面檢視統計結果
	 * 
	 * @return 按鈕
	 */
	private JButton getCreateBtn() {
		JButton createBtn = new JButton("檢視統計結果");
		createBtn.setFont(Main.FONT_TEXT);
		createBtn.addActionListener(e -> {
			// 重置狀態列內容值
			selectCol = null;
			Display.clearSelection();
			status.setText("就緒");
	
			// "確認欄位"輸入框是否只有大寫英文
			String upper = "[A-Z]+";
			// 驗證輸入是否正確用
			Boolean allOK = true;
			// 取得設定的欄位
			int[] colN = new int[setCol.size()];
			// 掃描輸入框
			for (int i = 0; i < setCol.size(); i++) {
				String sc = setCol.get(i).getText();
				if (sc.matches(upper)) {
					colN[i] = parseInt_C(sc) - 1;
				} else {
					setCol.get(i).setText("？");
					setCol.get(i).setForeground(Color.RED);
					allOK = false;
				}
			}
			// 驗證通過就顯示結果
			if (allOK) {
				showStatisticalResults(colN);
			} else {
				JOptionPane.showMessageDialog(jfSystem, "欄位尚未選擇完成！", "欄位確認", JOptionPane.ERROR_MESSAGE);
			} 
		});
		return createBtn;
	}

	/**
	 * 顯示統計結果
	 * 
	 * @param columnSet 資料選取的欄位
	 */ 
	private void showStatisticalResults(int[] columnSet) {
		try {
			data.setColumn(columnSet);
			data.create();
			if (data.getUnrecognizable() > 0) {
				// TODO 該不該被拔除
				String Msg = String.format("有 %d 個日期無法辨識!", data.getUnrecognizable());
				JOptionPane.showMessageDialog(jfSystem, Msg, "欄位確認", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e1) { 
			JOptionPane.showMessageDialog(jfSystem, "欄位選擇有誤！", "欄位確認", JOptionPane.ERROR_MESSAGE);
			
			Main.log(Level.WARNING, e1.getMessage());
			return;
		}

		// 科別統計資料(後台 匯流向下一個頁面)
		ArrayList<String[]> info = data.getSurgeryStatictis();
		// 科別統計資料(僅供使用者檢視)
		ArrayList<String[]> info_show = new ArrayList<>();
		// 紀錄所有科別的JPanel
		ArrayList<JPanel> table_set = new ArrayList<JPanel>();
		
		// 用來當頁籤頁面上層
		JPanel SD_Main_Panel=new JPanel();
		
		SD_Main_Panel.setLayout(new BorderLayout());
		
		JTabbedPane tabbedPane = new JTabbedPane();// 頁籤
		
		tabbedPane.setFont(Main.FONT_TITLE_BOLD);// 設定頁籤格式
		
		Collections.sort(SD_data, (o1, o2) -> {
			int diff = o1[0].compareTo(o2[0]);
			if (diff > 0) 
			{
				return 1;
			} 
			else if (diff < 0) 
			{
				return -1;
			}
			return 0; 
		});
		//table_data_set紀錄根據科別分割的資料
		ArrayList<ArrayList<String[]>> table_data_set = segmentation_data(SD_data);
		String[] Section = table_data_set.get(table_data_set.size() - 1).get(0);
		//選擇哪一科別表格顯示的JComboBox
		JComboBox<String> jComboBox = new JComboBox<String>(Section);
		jComboBox.setFont(Main.FONT_TEXT);
		jComboBox.addItemListener( e -> {
			SD_Main_Panel.removeAll();
			SD_Main_Panel.add(jComboBox, BorderLayout.NORTH);
			SD_Main_Panel.add(table_set.get(jComboBox.getSelectedIndex()),BorderLayout.CENTER);
			SD_Main_Panel.repaint();
			tabbedPane.addTab("術式時間統計表(未摘除離群值)", SD_Main_Panel);
			tabbedPane.setSelectedIndex(1); 
		});

		// 處理標題
		info_show.add(Main.S_COL_NAME);
		// 處理內容
		for (int i = 1; i < info.size(); i++) {
			// 主要對 cpt 這個字串陣列進行處理
			String[] cps = info.get(i); // 不採用完全複製 因為分布那行被拿掉
			String[] cpt = { Main.renameSubject(cps[Main.COL_NAME]), // 對科別名稱進行修正
					cps[Main.COL_MAX], cps[Main.COL_MIN], String.format("%.2f", Double.parseDouble(cps[Main.COL_AVG])),
					String.format("%.2f", Double.parseDouble(cps[Main.COL_STD])), cps[Main.COL_MODE],
					cps[Main.COL_COUNT] };
			if (cps[Main.COL_MODE].equals("0"))
				cpt[5] = "(眾數不存在)";
			info_show.add(cpt);
		}

		JTable table = makeTable(null, info_show);
		for (int i = 0; i < table_data_set.size() - 1; i++) {
			table_set.add(Create_Table(table_data_set.get(i)));
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		TableColumnModel cModel = table.getColumnModel();
		int n = cModel.getColumnCount();
		for (int col = 1; col < n; col++) {
			cModel.getColumn(col).setPreferredWidth(200);
		}

		JPanel jpStatus = new JPanel(new BorderLayout());
		jpStatus.setBackground(ColorManager.COLOR_STATUS_BACKGOUND);
		jpStatus.add(status, BorderLayout.WEST);

		JPanel tablePanel = new JPanel(new BorderLayout());
		JScrollPane jsp = new JScrollPane(table);
		jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
		tablePanel.add(jsp, BorderLayout.CENTER);
		tablePanel.add(jpStatus, BorderLayout.SOUTH);
		table_set.add(tablePanel);

		gbc_panel = new JPanel(new GridBagLayout());
		tabbedPane.addTab("手術時間統計表", tablePanel);
		SD_Main_Panel.add(jComboBox, BorderLayout.NORTH);
		SD_Main_Panel.add(table_set.get(0), BorderLayout.CENTER);
//		tabbedPane.addTab("術式時間統計表(未摘除離群值)", SD_Main_Panel)
		gbc_panel.setBackground(Color.white);
		tabbedPane.setBackground(Color.white);
		setGBC(1, 1, 0, 1, 2, 1, alignC, fill_B, tabbedPane);
		setGBC(0, 0, 0, 2, 1, 1, alignR, fill_N, getReturnBtn());
		setGBC(0, 0, 1, 2, 1, 1, alignR, fill_N, getSaveBtn()); 
		
		Main.switchLights(1);
		Main.switchPage(gbc_panel);
	}

	private ArrayList<ArrayList<String[]>> segmentation_data(ArrayList<String[]> data) {
		String[] temp, title = data.get(data.size() - 1).clone();
		ArrayList<ArrayList<String[]>> set = new ArrayList<ArrayList<String[]>>();
		ArrayList<String[]> temp_set = new ArrayList<String[]>();

		List<String> cheak_select_state = new ArrayList<String>();
		String section = "";
		temp_set.add(title);
	
		for (int i = 0; i < data.size() - 1; i++) {
			temp = data.get(i);
			if (section.equals("")) {
				section = temp[0];
				cheak_select_state.add(section);
			}

			if (temp[0].equals(section)) {
				String[] newstrset = temp.clone();
				temp_set.add(newstrset);
			} else {
				set.add(temp_set);
				temp_set = new ArrayList<String[]>();
				temp_set.add(title);
				String[] newstrset = temp.clone();
				temp_set.add(newstrset);
				section = data.get(i)[0];
				cheak_select_state.add(section);
			}

			if ((i + 1) == data.size() - 1) {
				set.add(temp_set);
				temp_set = new ArrayList<String[]>();
				String[] newstrset = new String[cheak_select_state.size()];
				for (int j = 0; j < newstrset.length; j++) {
					newstrset[j] = cheak_select_state.get(j);
				}
				temp_set.add(newstrset);
				set.add(temp_set);
			}
		}
		return set;
	}

	private JTable customTableProperties(int AutoResizeMode) {
		JTable table = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				JLabel c = (JLabel) super.prepareRenderer(renderer, row, col);

				if (row == 0 && col > 0) {
					c.setHorizontalAlignment(SwingConstants.CENTER);
				} else if (row > 0 && col > 2) {
					c.setHorizontalAlignment(SwingConstants.RIGHT);
				} else {
					c.setHorizontalAlignment(SwingConstants.LEFT);
				}
				return c;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		 
		table.getTableHeader().setReorderingAllowed(false);
		table.setDragEnabled(true);
		table.setAutoResizeMode(AutoResizeMode);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowHeight(Main.TEXT_SIZE + 10);

		table.setGridColor(ColorManager.COLOR_TABLE_GRID);
		table.setSelectionForeground(ColorManager.COLOR_TABLE_FOREGOUND);
		table.setSelectionBackground(ColorManager.COLOR_TABLE_BACKGOUND);

		table.setFont(Main.FONT_TEXT);
		table.getTableHeader().setFont(Main.FONT_TEXT_BOLD);
		
		table.setBackground(Color.white);
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selRow = table.rowAtPoint(e.getPoint());
				int selCol = table.columnAtPoint(e.getPoint());
				String Col = table.getColumnName(selCol);

				if (selRow == -1) {
					table.clearSelection();
					status.setText("就緒");
				} else {
					changeStatus(table, selRow, selCol, Col);
				} 
			}

			public void mousePressed(MouseEvent e) {
				//Do nothing.
			}

			public void mouseReleased(MouseEvent e) {
				//Do nothing.
			}

			public void mouseEntered(MouseEvent e) {
				//Do nothing.
			}

			public void mouseExited(MouseEvent e) {
				//Do nothing.
			}
		});
		 
		return table; 
	}
	
	/**
	 * 建立顯示統計結果的表格 
	 */ 
	private JPanel Create_Table(ArrayList<String[]> data) {  
		JTable table = customTableProperties(JTable.AUTO_RESIZE_ALL_COLUMNS);
		DefaultTableModel deal = (DefaultTableModel) table.getModel();
		
		// 重設標題列
		String[] ColumnName = data.get(0); 
		String[] ColumnCode = getColCode(ColumnName.length); 
		deal.setColumnCount(0);
		for (String i : ColumnCode) {
			deal.addColumn(i);
		} 
		
		// 重設第一列資料
		int rowN = data.size();  
		deal.setRowCount((rowN < 30) ? 50 : rowN); 
		deal.setValueAt("1", 0, 0); 
		for (int i = 0; i < ColumnName.length; i++) {
			deal.setValueAt(ColumnName[i], 0, i + 1);
		}
		
		// 重設其他列資料
		for (int i = 1; i < data.size(); i++) {
			// 顯示第幾列
			deal.setValueAt(String.valueOf(i + 1), i, 0);
			// 設置資料
			String[] row = data.get(i);
			for (int j = 0; j < row.length; j++) {
				deal.setValueAt(row[j], i, (j + 1));
			}
		}
 
		TableColumnModel cModel = table.getColumnModel();
		int n = cModel.getColumnCount();
		for (int col = 1; col < n; col++) {
			cModel.getColumn(col).setPreferredWidth(200);
		}
		JPanel jpStatus = new JPanel(new BorderLayout());
		jpStatus.setBackground(ColorManager.COLOR_STATUS_BACKGOUND);
		jpStatus.add(status, BorderLayout.WEST);

		JPanel tablePanel = new JPanel(new BorderLayout());
		JScrollPane jsp = new JScrollPane(table);
		jsp.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		tablePanel.setBackground(Color.white);
		tablePanel.add(jsp, BorderLayout.CENTER);
		tablePanel.add(jpStatus, BorderLayout.SOUTH);
		return tablePanel;
	}
	
	private void changeStatus(JTable table, int selRow, int selCol, String Col) {
		if (selectCol != null) 
		{
			if (selCol > 0 && selCol < table.getColumnCount()) 
			{
				selectCol.setText(Col);
				selectCol = null;
				table.clearSelection();
				status.setText("已選取第" + Col + "行");
			} 
			else {
				status.setText("此欄位無效!!請重新選擇!!");
			}
		} 
		else {
			if (selCol == 0) {
				table.setRowSelectionInterval(selRow, selRow);
				status.setText("已選取 第" + (selRow + 1) + "列");
			} else {
				status.setText("已選取 第" + (selRow + 1) + "列 第" + Col + "行");
			}
		} 
	}
	 
	/**
	 * 
	 * 
	 * @param path
	 * @throws IOException
	 */
	private void Sava_SD_Data(String path) throws IOException {
		CSVWriter writer = new CSVWriter(path);
		writer.WriteAll(SD_data);
		writer.close();
	}

	private JButton getReturnBtn() {
		JButton saveBtn = new JButton("返回上個頁面");
		saveBtn.setFont(Main.FONT_TEXT);
		saveBtn.addActionListener(e -> { 
			ArrayList<JLabel> jl = Main.JP_MENU.getjl_route();
			for (JLabel i : jl) {
				i.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
			}
			jl.get(0).setForeground(ColorManager.COLOR_MENUBAR_TEXT_LIGHT); 
			Main.switchPage(jpBuild); 
		});
		return saveBtn;
	}

	private JButton getSaveBtn() {
		JButton saveBtn = new JButton("設定資料分布");
		saveBtn.setFont(Main.FONT_TEXT);
		saveBtn.addActionListener(e -> {
			if (data.getSurgeryStatictis().size() <= 1) {
				JOptionPane.showMessageDialog(jfSystem, "您目前所產生的資料還不足以進行模擬", "資料錯誤", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// 顯示於自訂對話盒的訊息
			String strMsg = "是否要儲存文件?\n\n如果按一下[不要儲存]，此統計資料\n將無法重新使用。"; 
			String strTitle = "統計資料";

			ImageIcon icon_save = new ImageIcon(FileRoute.IMAGE_DISK);
			// 宣告自訂對話盒的按鈕項目
			String[] options = new String[] { "儲存", "不要儲存", "取消" }; 
			// 宣告自訂對話盒
			JOptionPane pane = new JOptionPane(strMsg);
			// 設定自訂對話盒的選項
			pane.setOptions(options);
			pane.setIcon(icon_save); 
			// 設定自訂對話盒的訊息類型
			pane.setMessageType(JOptionPane.PLAIN_MESSAGE);
			
			// 建立自訂對話盒的對話盒物件
			JDialog dialog = pane.createDialog(jfSystem, strTitle);
			// 顯示對話盒
			dialog.setVisible(true);  
			//按下取消 直接返回
			if (pane.getValue().equals(options[2])) { 
				return; 
			}   
			if (pane.getValue().equals(options[0])) { 
				SaveFile(getSaveFilePath());
			}  
			else { 
				try {
					data.setSrcFileName(FileRoute.PATH_TEMP);
					 
					Distribution.saveAsFile(data, data.getSrcFileName()); 
					Main.log(Level.INFO, "統計資料僅存放在記憶體或暫存檔"); 
					
					Main.switchLights(2); 
					JOptionPane.showMessageDialog(jfSystem, "分布選擇頁面");
					Main.switchLights(1);
					Main.switchPage(this);
				} 
				catch (IOException e1) { 
					String msg = "此檔案可能正由另一個程序使用：" + "\n" + data.getSrcFileName();
					JOptionPane.showMessageDialog(jfSystem, msg, "存檔失敗", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		return saveBtn;
	}

	/**
	 * 表格標題列的英文字母標示轉換成對應數值 A=1 B=2 ... AA=27
	 * 
	 * @param src
	 * @return
	 */
	private int parseInt_C(String src) {
		int n = 0;
		
		for (int i = src.length(); i > 0; i--) {
			int m = src.charAt(i-1) -'A' + 1;
			n += m * Math.pow(26, (double)src.length() - i);
		}
		
		return n;
	}

	/**
	 * 產生表格標題列的英文字母
	 * 
	 * @param size 欄位數量
	 * @return
	 */
	private String[] getColCode(int size) {
		String[] ColCode = new String[size + 1];
		ColCode[0] = "";
		for (int i = 0; i < size; i++) {
			int temp = i;

			StringBuilder code = new StringBuilder();
			code.append((char) ('A' + temp % 26));
			temp /= 26;

			while (temp != 0) {
				int a = temp % 26 - 1;
				code.append((char) ('A' + a));
				temp /= 26;
			}

			ColCode[i + 1] = code.reverse().toString();
		}
		return ColCode;
	}

	/**
	 * 原始資料預覽畫面
	 * 
	 * @return 表格畫面
	 */
	private JPanel PreviewPanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		 
		String[] file = this.srcFile.split("\\\\"); 
		tablePanel.setBorder(getTitleBorder("檔名：" + file[file.length - 1]));// 設定標題外框

		status = new JLabel("就緒", SwingConstants.RIGHT);
		status.setFont(Main.FONT_TEXT);
		status.setForeground(ColorManager.COLOR_BLACK);

		JPanel jpStatus = new JPanel(new BorderLayout());
		jpStatus.add(status, BorderLayout.WEST);
		jpStatus.setBackground(ColorManager.COLOR_STATUS_BACKGOUND);
		
		ArrayList<String[]> raw;
		String[] ColumnName = {"","","","","","","","","","","","","","","","","",""}; 
		if(data == null) { 
			raw = new ArrayList<String[]>();
			for(int i=0; i<30; i++) {
				String[] str = {"","","","","","","","","","","","","","","","","",""};
				raw.add(str);
			}  
		}else { 
			ColumnName = data.getColumnNameLine(); 
			raw = data.getRaw(); 
		} 
		Display = makeTable(ColumnName, raw);

		JScrollPane jsp_table = new JScrollPane(Display);
		jsp_table.setBorder(new EmptyBorder(0, 0, 0, 0));
		jsp_table.setBackground(ColorManager.COLOR_WHITE);

		tablePanel.add(jsp_table, BorderLayout.CENTER);
		tablePanel.add(jpStatus, BorderLayout.SOUTH);
		tablePanel.setBackground(ColorManager.COLOR_WHITE);
		return tablePanel;
	}

	/**
	 * 顯示原始資料的表格
	 * 
	 * @param ColumnName 欄位名稱
	 * @param data       資料紀錄
	 * @return 表格
	 */
	private JTable makeTable(String[] ColumnName, ArrayList<String[]> data) { 
		JTable table = customTableProperties(JTable.AUTO_RESIZE_OFF); 
		DefaultTableModel deal = (DefaultTableModel) table.getModel();
		
		// 判斷沒有標題列 沒有則為1
		int CNN = 0; 
		if (ColumnName == null) {
			ColumnName = data.get(0);
			CNN = 1;
		}
 
		// 重設標題列
		String[] ColumnCode = getColCode(ColumnName.length); 
		deal.setColumnCount(0);
		for (String i : ColumnCode) { 
			deal.addColumn(i);
		}

		// 重設第一列資料
		int rowN = data.size() + 1 - CNN; 
		deal.setRowCount((rowN < 30) ? 50 : rowN);
		deal.setValueAt("1", 0, 0); 
		for (int i = 0; i < ColumnName.length; i++) {
			deal.setValueAt(ColumnName[i], 0, i + 1);
		}
		
		// 重設其他列資料
		for (int i = CNN; i < data.size(); i++) {
			// 顯示第幾列
			deal.setValueAt(String.valueOf((i - CNN) + 2), (i - CNN + 1), 0);
			// 設置資料
			String[] row = data.get(i);
			for (int j = 0; j < row.length; j++) {
				deal.setValueAt(row[j], (i - CNN + 1), j + 1);
			}
		}
 
		return table;
	}

	/**
	 * 產生框線
	 * 
	 * @param title
	 * @return
	 */
	private TitledBorder getTitleBorder(String title) {
		TitledBorder i = new TitledBorder(title);
		i.setTitleFont(Main.FONT_TITLE_BOLD);
		i.setTitleColor(ColorManager.COLOR_TITLE);
		i.setBorder(Main.LINEBORDER);
		return i;
	}

	/**
	 * 產生標籤
	 * 
	 * @param target 欲顯示的內容
	 * @return
	 */
	private JLabel jlTitle(String target) {
		JLabel A = new JLabel(target);
		A.setFont(Main.FONT_TEXT);
		return A;
	}

	/**
	 * 產製一列 "確認欄位" 的輸入欄
	 * 
	 * @param target
	 * @return
	 */
	private JPanel inputField(String target) {
		JLabel A = new JLabel("第");
		A.setFont(Main.FONT_TEXT);
		A.setForeground(ColorManager.COLOR_MENUBAR_BTNHOME_TEXT);
		A.setBackground(ColorManager.COLOR_WHITE);
		A.setOpaque(true);

		JLabel B = new JLabel("行");
		B.setFont(Main.FONT_TEXT);
		B.setForeground(ColorManager.COLOR_MENUBAR_BTNHOME_TEXT);
		B.setBackground(ColorManager.COLOR_WHITE);
		B.setOpaque(true);
 
		JTextField jtf_col = new JTextField(5); 
		jtf_col.setHorizontalAlignment(SwingConstants.CENTER);
		jtf_col.setBorder(new TextBorderUtils(Color.black, 1, true));
		
		HashMap<String, Integer> ColumnName = (data != null) ? data.getColumnName(): new HashMap<String, Integer>();
		if (ColumnName.containsKey(target)) {
			String str = Display.getColumnName(ColumnName.get(target) + 1);
			jtf_col.setText(str);
		} else {
			jtf_col.setText("？");
			jtf_col.setForeground(Color.RED);
		}
		
		jtf_col.addCaretListener(e -> { 
			jtf_col.setForeground(Color.black);
			selectCol = jtf_col;
			Display.setRowSelectionInterval(0, 0);
			status.setText("狀態：請選擇指定欄位"); 
		});
		
		setCol.add(jtf_col);
		jtf_col.setFont(Main.FONT_TEXT);
		jtf_col.setForeground(ColorManager.COLOR_MENUBAR_BTNHOME_TEXT);
		jtf_col.setBackground(ColorManager.COLOR_WHITE);

		JPanel Panel = new JPanel(new BorderLayout(10, 5));
		Panel.add(A, BorderLayout.WEST);
		Panel.add(jtf_col, BorderLayout.CENTER);
		Panel.add(B, BorderLayout.EAST);
		Panel.setBackground(ColorManager.COLOR_WHITE);

		return Panel;
	}

	/**
	 * 儲存檔案的對話框
	 * 
	 * @return 檔案儲存路徑
	 */
	private String getSaveFilePath() {
		myFilter filter = new myFilter("CSV", "純逗點分隔資料 (.csv)");
		JFileChooser select = new JFileChooser(FileRoute.PATH_DISTRIBUTION);

		select.setDialogTitle("儲存檔案");
		select.addChoosableFileFilter(filter);
		select.removeChoosableFileFilter(select.getAcceptAllFileFilter());
		select.setFileFilter(filter);

		int result = select.showSaveDialog(jfSystem);
		if (result == JFileChooser.APPROVE_OPTION) {
			String filePath = select.getSelectedFile().toString();
			String temp = filePath.substring(filePath.length() - 4, filePath.length());
			if (!temp.equals(".csv")) {
				filePath += ".csv";
			}
			return filePath;
		} else {
			return null;
		}
	}

	private void SaveFile(String path) {
		if (path != null) {
			data.setSrcFileName(path);
			String SD_Path = path.replace("distribution", "Standard_Deviation");
			
			try {
				Distribution.saveAsFile(data, path);
				Sava_SD_Data(SD_Path);  
				Main.log(Level.INFO, "統計資料已儲存至 " + data.getSrcFileName());  
				Main.switchLights(2); 
				
				JOptionPane.showMessageDialog(jfSystem, "分布選擇頁面");
				Main.switchLights(-1);  
			} 
			catch (IOException e1) {
				String msg = String.format("程序無法存取 %s%n因為此檔案正由另一個程序使用", path);
				JOptionPane.showMessageDialog(jfSystem, msg, "存檔失敗", JOptionPane.ERROR_MESSAGE);
			}
		} 
		else { 
			Main.log(Level.INFO, "取消儲存 - 不進行任何動作");  
		}
	}
	
	/**
	 * 檔案類型過濾(給儲存檔案的對話框用)
	 * 
	 * @author Chasel
	 */
	class myFilter extends javax.swing.filechooser.FileFilter {
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

	/**
	 * 設置版面用
	 * 
	 * @param wx
	 * @param wy
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param align
	 * @param fill
	 * @param A
	 */
	private void setGBC(double wx, double wy, int x, int y, int w, int h, int align, int fill, Component A) {
		gbc.fill = fill;
		gbc.anchor = align;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.insets = new Insets(10, 5, 0, 5);
		gbc_panel.add(A, gbc);
		gbc_panel.setBackground(ColorManager.COLOR_WHITE);
	}

	/**
	 * 在版面塞入空白 用以推擠元件
	 * 
	 * @param wx
	 * @param wy
	 */
	private void spacer(double wx, double wy) {
		gbc.gridx = 0;
		gbc.gridy = gbc.gridy + 1;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 10, 0, 10);
		gbc_panel.add(new JLabel(""), gbc);
		gbc_panel.setBackground(ColorManager.COLOR_WHITE);
	}
}
