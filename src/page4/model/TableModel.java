package page4.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.swing.table.AbstractTableModel;

import page4.controller.Main;
 

//Table��Model
public class TableModel extends AbstractTableModel { 
	
	private static final long serialVersionUID = 1L;
	
	// �ŧi�x�s��ƪ��G���}�C
	private ArrayList<String[]> tableModelData = new ArrayList<String[]>(); 
	private String[] colName; 
	private Class[] types; 
	 
	// ���t�XTableCellEditor����,
	// �����ŧi�x�s�ɮ׸�Ƥ����O��Class�}�C
	public TableModel(ArrayList<String[]> data, String[] colName, Class[] types) {
		this.tableModelData = data; 
		this.colName = colName;
		this.types = types;
	}

	// ���o��쪺�Ӽ�
	public int getColumnCount() {
		return colName.length;
	}

	// ���o��ƪ��C��
	public int getRowCount() {
		return tableModelData.size();
	}

	// ���o���w��B�C��m���x�s����
	public Object getValueAt(int row, int col) {
		return tableModelData.get(row)[col];
	}

	// ���o���W��, �i���w�q
	@Override
	public String getColumnName(int column) {
		return colName[column];
	}

	@Override
	public Class getColumnClass(int col) {
		return types[col];
	}
	// ���o�ɮ׬Y����Ƥ����O��Class����,
	// ����k�N��TableCellEditor����B�@�ϥ�

	// �Y���\�s�����ƥ�����@����k
	@Override
	public boolean isCellEditable(int row, int col) {
		boolean b = true;
		if (colName[col].equals("�R��") || colName[col].equals("�w��") || colName[col].equals("���"))
			b = true;
		else
			b = false;
		return b;
	}

	// �R���@��
	public void deleteRow(int row) {
		tableModelData.remove(row);
		fireTableRowsInserted(0, tableModelData.size() - 1);
	}

	// �s�W�@��
	public void addRow(TreeMap<String, File> script, File scriptFolder) throws IOException { 
		script.clear();
		for (File file : scriptFolder.listFiles()) {
			if (!file.isDirectory()) {
				script.put(file.toString(), file);
			}
		}
		
		tableModelData = new ArrayList<String[]>();
		BasicFileAttributes attrs;
		Calendar c = Calendar.getInstance(); 
		for (Entry<String, File> i : script.entrySet()) {
			String[] name = i.getValue().getName().split(".csv");

			Path directoryPath = Paths.get(i.getValue().getPath());
			try {
				attrs = Files.readAttributes(directoryPath, BasicFileAttributes.class);
				c.setTimeInMillis(attrs.creationTime().toMillis());
				String[] s = { name[0], new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()), "�R��", "�w��", "���" };
				tableModelData.add(s);
			} catch (IOException e) {
				Main.log(Level.WARNING, e.getMessage());
			}
		}
		 
		fireTableRowsInserted(tableModelData.size() - 1, tableModelData.size() - 1);
	}
}