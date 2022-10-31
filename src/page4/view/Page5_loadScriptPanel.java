package page4.view;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder; 
import javax.swing.table.DefaultTableModel;

import page4.controller.*;
import page4.model.*;

import javax.swing.JPanel;

public class Page5_loadScriptPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * Data
	 * 
	 */  
	private String field_name;
	private String srcFile = "";

	private ArrayList<String[]> calender;
	private String[] calender_title;
	/**
	 * View
	 */
	//表格資料預覽用
	private JPanel preview;

	private JTextField selectCol = null;
	private JTable Display;
	private JLabel status;
	
	public JPanel getPanel() {
		return preview;
	}
	 
	/**
	 * 用於讓page4產生預覽畫面用
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
	 * @return 資料預覽表格
	 */
	private JPanel PreviewPanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(ColorManager.COLOR_WHITE);

		
		String[] file = this.srcFile.split("\\\\");
		if (!file[file.length - 1].equals(""))
			tablePanel.setBorder(getTitleBorder("檔名：" + file[file.length - 1]));
		else
			tablePanel.setBorder(getTitleBorder("檔名：" + field_name));


		status = new JLabel("就緒", SwingConstants.RIGHT);
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
					status.setText("就緒");
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
				status.setText("已選取第（" + Col + "）行");
			} else {
				status.setText("此欄位無效!!請重新選擇!!");
			}
		} else {
			if (selCol == 0) {
				Display.setRowSelectionInterval(selRow, selRow);
				status.setText("已選取 第（" + (selRow + 1) + "）列");
			} else {
				status.setText("已選取 第（" + (selRow + 1) + "）列 第（" + Col + "）行");
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
}
