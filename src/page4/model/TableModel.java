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
 

//Table的Model
public class TableModel extends AbstractTableModel { 
	
	private static final long serialVersionUID = 1L;
	
	// 宣告儲存資料的二維陣列
	private ArrayList<String[]> tableModelData = new ArrayList<String[]>(); 
	private String[] colName; 
	private Class[] types; 
	 
	// 為配合TableCellEditor物件,
	// 必須宣告儲存檔案資料之型別的Class陣列
	public TableModel(ArrayList<String[]> data, String[] colName, Class[] types) {
		this.tableModelData = data; 
		this.colName = colName;
		this.types = types;
	}

	// 取得欄位的個數
	public int getColumnCount() {
		return colName.length;
	}

	// 取得資料的列數
	public int getRowCount() {
		return tableModelData.size();
	}

	// 取得指定欄、列位置的儲存格資料
	public Object getValueAt(int row, int col) {
		return tableModelData.get(row)[col];
	}

	// 取得欄位名稱, 可不定義
	@Override
	public String getColumnName(int column) {
		return colName[column];
	}

	@Override
	public Class getColumnClass(int col) {
		return types[col];
	}
	// 取得檔案某欄位資料之類別的Class物件,
	// 此方法將供TableCellEditor物件運作使用

	// 若允許編輯表格資料必須實作此方法
	@Override
	public boolean isCellEditable(int row, int col) {
		boolean b = true;
		if (colName[col].equals("刪除") || colName[col].equals("預覽") || colName[col].equals("選擇"))
			b = true;
		else
			b = false;
		return b;
	}

	// 刪除劇本
	public void deleteRow(int row) {
		tableModelData.remove(row);
		fireTableRowsInserted(0, tableModelData.size() - 1);
	}

	// 新增劇本
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
				String[] s = { name[0], new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()), "刪除", "預覽", "選擇" };
				tableModelData.add(s);
			} catch (IOException e) {
				Main.log(Level.WARNING, e.getMessage());
			}
		}
		 
		fireTableRowsInserted(tableModelData.size() - 1, tableModelData.size() - 1);
	}
}