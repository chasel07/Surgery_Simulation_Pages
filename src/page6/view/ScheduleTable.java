package page6.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import page6.controller.Main;
import page6.model.CSVReader;
import page6.model.ColorManager;
import page6.model.Patient;
 

public class ScheduleTable extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String file_input;

	private HashMap<String, ArrayList<Patient>> patient_room;

	private ArrayList<String> ColorTable = Main.ColorTable;
	private HashMap<String, Color> SubjectColor = Main.SubjectColor;

	// 紀錄各標題所對應的index
	private HashMap<String, Integer> name_num;
	
	private ArrayList<String> room_data = new ArrayList<>(); // 紀錄有哪些室別
	private ArrayList<String> ApplicationID = new ArrayList<String>(); // 申請序號
	private ArrayList<String> PatientID = new ArrayList<String>(); // 病患ID
	private ArrayList<String> PatientName = new ArrayList<String>(); // 病患姓名
	private ArrayList<String> Identity = new ArrayList<String>(); // 病患身分
	private ArrayList<String> Section = new ArrayList<String>(); // 科別
	private ArrayList<String> Doctor = new ArrayList<String>(); // 主治醫師
	private ArrayList<String> Room = new ArrayList<String>(); // 室別
	private ArrayList<String> Type = new ArrayList<String>(); // 類型
	private ArrayList<String> SurgeryName = new ArrayList<String>(); // 手術名稱
	private ArrayList<String> AnesthesiaName = new ArrayList<String>(); // 麻醉名稱
	private ArrayList<Integer> ArrivalTime = new ArrayList<Integer>(); // 到達時間
	private ArrayList<Integer> EntryTime = new ArrayList<Integer>(); // 入室時間
	private ArrayList<Integer> AnesthesiaStart = new ArrayList<Integer>(); // 麻醉開始
	private ArrayList<Integer> AnesthesiaFinish = new ArrayList<Integer>(); // 麻醉完成
	private ArrayList<Integer> ScratchTime = new ArrayList<Integer>(); // 劃刀時間
	private ArrayList<Integer> SutureTime = new ArrayList<Integer>(); // 縫合切口
	private ArrayList<Integer> SurgeryTime = new ArrayList<Integer>(); // 手術時長
	private ArrayList<Integer> AnesthesiaEnd = new ArrayList<Integer>(); // 麻醉結束
	private ArrayList<Integer> TransferTime = new ArrayList<Integer>(); // 轉送時間
	private ArrayList<Integer> CleanTime = new ArrayList<Integer>(); // 清潔時間

	private int readData(CSVReader reader) {
		int num = 0;
		String[] nextLine;

		while (((nextLine = reader.readNext()) != null) && (nextLine.length > 5)) {
			if (!nextLine[name_num.get("事件代碼")].equals("3.0")) {
				continue;
			}

			ApplicationID.add(getStringBy(nextLine, "申請序號")); 
			PatientID.add(getStringBy(nextLine, "病歷號")); 
			Identity.add(getStringBy(nextLine, "病患身分")); 
			PatientName.add(getStringBy(nextLine, "病患姓名")); 
			Doctor.add(getStringBy(nextLine, "主治醫師")); 
			Type.add(getStringBy(nextLine, "手術級別")); 
			SurgeryName.add(getStringBy(nextLine, "手術名稱")); 
			AnesthesiaName.add(getStringBy(nextLine, "麻醉名稱"));

			ArrivalTime.add(getDoubleBy(nextLine, "到達時間")); 
			EntryTime.add(getDoubleBy(nextLine, "病患入室時間")); 
			AnesthesiaStart.add(getDoubleBy(nextLine, "麻醉開始時間")); 
			AnesthesiaFinish.add(getDoubleBy(nextLine, "麻醉完成時間"));
			ScratchTime.add(getDoubleBy(nextLine, "劃刀時間"));
			SutureTime.add(getDoubleBy(nextLine, "縫合切口"));
			SurgeryTime.add(getDoubleBy(nextLine, "手術時長"));
			AnesthesiaEnd.add(getDoubleBy(nextLine, "麻醉結束時間"));
			TransferTime.add(getDoubleBy(nextLine, "轉送時間"));
			CleanTime.add(getDoubleBy(nextLine, "清潔時間"));

			processSubjectColumn(nextLine, Section, name_num);
			
			if (processRoomColumn(nextLine, Room, name_num)) 
			{ 
				break;
			}
				 
			num++;
		}

		return num;
	}
	 
	private String getStringBy(String[] line, String key) { 
		if (name_num.get(key) == null) {
			return "";
		}  
		else { 
			return line[name_num.get(key)]; 
		}  
	}
	
	private int getDoubleBy(String[] line, String key) { 
		if (name_num.get(key) == null) {
			return -1;
		}  
		else { 
			return (int) Double.parseDouble(line[name_num.get(key)]);
		}  
	}
		
	

	private void dataLoading() throws IOException {

		CSVReader reader = new CSVReader(file_input);
 
		name_num = new HashMap<>(); 
		// 讀取標題那列
		String[] nextLine = reader.readNext();
		for (int i = 0; i < nextLine.length; i++) {
			// 將各標題與其所對應的index放入
			name_num.put(nextLine[i], i);
		}

		int num = readData(reader);

		reader.close();

		// 紀錄病患與手術房
		patient_room = new HashMap<>();
		for (int i = 0; i < num; i++) {
			Object[] args = { ApplicationID.get(i), PatientID.get(i), PatientName.get(i), Identity.get(i),
					Section.get(i), Doctor.get(i), Room.get(i), Type.get(i), SurgeryName.get(i), AnesthesiaName.get(i),
					ArrivalTime.get(i), EntryTime.get(i), AnesthesiaStart.get(i), AnesthesiaFinish.get(i),
					ScratchTime.get(i), SutureTime.get(i), SurgeryTime.get(i), AnesthesiaEnd.get(i),
					TransferTime.get(i), CleanTime.get(i) };

			Patient patient = new Patient(args);

			if (patient_room.get(patient.getRoom()) == null) {
				ArrayList<Patient> temp = new ArrayList<>();
				temp.add(patient);
				patient_room.put(patient.getRoom(), temp);
			} else {
				ArrayList<Patient> temp = patient_room.get(patient.getRoom());
				temp.add(patient);
				patient_room.put(patient.getRoom(), temp);
			}
		}

	}

	private void processSubjectColumn(String[] nextLine, ArrayList<String> Section, HashMap<String, Integer> name_num) {
		if (name_num.get("所屬科別") == null)
			Section.add("");
		else {
			String Subject = nextLine[name_num.get("所屬科別")];
			Section.add(Subject);
			SubjectColor.computeIfAbsent(Subject, k -> Color.decode(ColorTable.get(SubjectColor.size())));
		}
	}

	private boolean processRoomColumn(String[] nextLine, ArrayList<String> Room, HashMap<String, Integer> name_num) {
		if (name_num.get("手術房別") == null) {
			Main.log(Level.WARNING, "無手術室別 無法進行排程!!");
			return true; // break off
		}

		String room = nextLine[name_num.get("手術房別")];
		if (!room_data.contains(room)) {
			room_data.add(room);
		}
		Room.add(room);
		return false;
	}

	public JTable customTableProperties() {
		DefaultTableModel model = new DefaultTableModel();

		JTable table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(80);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(Main.FONT_TEXT_BOLD);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int Row = table.rowAtPoint(e.getPoint());
				int Col = table.columnAtPoint(e.getPoint());

				Object name = table.getValueAt(Row, Col);
				if (name == null)
					return;
				if (e.getClickCount() == 2) {
					mouseDoubleClicked(name, Col);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Do nothing

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// Do nothing
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// Do nothing
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Do nothing
			}
		});

		return table;
	}

	public void setTableData(JTable table) {
		DefaultTableModel deal = (DefaultTableModel) table.getModel();

		int index = 0;
		for (int j = 0; j < patient_room.size(); j++) {
			if (index < patient_room.get(room_data.get(j)).size()) {
				index = patient_room.get(room_data.get(j)).size();
			}
		}

		Vector<Integer> id = new Vector<>();
		for (int n = 1; n <= index; n++) {
			id.add(n);
		}
		deal.addColumn("", id);

		room_data.sort(null);
		for (int i = 0; i < room_data.size(); i++) {
			Vector<Object> v = new Vector<Object>();
			for (int j = 0; j < patient_room.get(room_data.get(i)).size(); j++) {
				Patient p = patient_room.get(room_data.get(i)).get(j);
				String pid = "病患ID: " + p.getPatientID();
				String timeStart;
				String timeFinish;

				timeStart = "入室時間: " + String.format("%02d:%02d:%02d", p.getEntryTime() / 1440,
						p.getEntryTime() % 1440 / 60, p.getEntryTime() % 60);
				timeFinish = "離開時間: " + String.format("%02d:%02d:%02d", p.getTransferTime() / 1440,
						p.getTransferTime() % 1440 / 60, p.getTransferTime() % 60);

				String string = "<html><body>" + pid + "<br>" + timeStart + "<br>" + timeFinish + "<body></html>";
				v.add(string);
			}
			deal.addColumn(room_data.get(i), v);
		}
	}

	public ScheduleTable(String srcFile) throws IOException {
		super(new BorderLayout());

		file_input = srcFile;
		dataLoading();

		Vector<String> tittle = new Vector<String>();
		tittle.addAll(patient_room.keySet());

		JTable table = customTableProperties();
		setTableData(table);

		// 設置儲存格偏好寬度
		for (int column = 0; column < table.getColumnCount(); column++) {
			TableColumn tableColumn = table.getColumnModel().getColumn(column);
			int preferredWidth = tableColumn.getMinWidth();
			int maxWidth = tableColumn.getMaxWidth();

			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
				Component c = table.prepareRenderer(cellRenderer, row, column);
				int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
				preferredWidth = Math.max(preferredWidth, width);

				// We've exceeded the maximum width, no need to check other rows

				if (preferredWidth >= maxWidth) {
					preferredWidth = maxWidth;
					break;
				}
			}
			tableColumn.setPreferredWidth(preferredWidth);
		}

		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0, n = tcm.getColumnCount(); i < n; i++) {
			TableColumn tc = tcm.getColumn(i);
			tc.setCellRenderer(new CellRenderer_Subject());
		}

		// 添加表頭
		add(table.getTableHeader(), BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);
	}

	private void mouseDoubleClicked(Object name, int Col) {
		String[] click = name.toString().split(" ");
		click = click[1].split("<br>");
		for (int i = 0; i < patient_room.get(room_data.get(Col - 1)).size(); i++) {
			Patient p = patient_room.get(room_data.get(Col - 1)).get(i);
			if (click[0].equals(p.getPatientID())) {
				new Detail(p);
				break;
			}
		}
	}

	private class CellRenderer_Subject extends DefaultTableCellRenderer {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setValue(value);
			setFont(table.getFont());

			if (value == null) {
				setBackground(Color.white);
				table.setSelectionBackground(Color.white);
				return this;
			}

			if (column <= 0 || column > patient_room.size()) {
				return this;
			}

			ArrayList<Patient> roomRecord = patient_room.get(room_data.get(column - 1));
			if (row < 0 || row >= roomRecord.size()) {
				return this;
			}

			Patient p = roomRecord.get(row);
			Color curColor = SubjectColor.get(p.getSection());

			setBackground(curColor);

			if (isSelected) {
				table.setSelectionBackground(curColor);
				table.setSelectionForeground(ColorManager.COLOR_TABLE_SELECT_FOREGROUND);
				setForeground(ColorManager.COLOR_TABLE_SELECT_FOREGROUND);
			} else {
				table.setSelectionForeground(ColorManager.COLOR_WHITE);
				setForeground(ColorManager.COLOR_WHITE);
			}

			return this;
		}
	}

}
