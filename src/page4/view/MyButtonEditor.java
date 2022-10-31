package page4.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit; 
import java.io.File;
import java.io.IOException; 
import java.nio.file.Files;
import java.util.logging.Level;

import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.TableCellEditor;

import page4.controller.*;
import page4.model.*; 

public class MyButtonEditor extends AbstractCellEditor implements TableCellEditor {
	private static final long serialVersionUID = 1L;
	
	private ImageIcon iLoad = new ImageIcon(FileRoute.IMAGE_LOADING);
	private ImageIcon iEmpty = new ImageIcon("");

	private int row;
	private int column;
	private String num; 
	private File fileSrc;

	private Page4_ScriptGenerationPanel Page4;

	private ImageIcon image;
	private JLabel jload;
	private JFrame preview;
	
	private JTable table;
	private JButton button;

	private JPanel panel;
	private JPanel jpLoad;

	public MyButtonEditor(ImageIcon image, Page4_ScriptGenerationPanel thisPage4_ScriptGenerationPanel) {
		this.Page4 = thisPage4_ScriptGenerationPanel;
		this.image = image;

		initButton();

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(this.button, BorderLayout.CENTER);
	}

	public void setLoadingText(String src) {
		jload.setText(src);
	}

	public void setLoadingText(String fileName, Double progress) {
		if (fileSrc.toString().equals(fileName)) {
			if (progress > 99) {
				jload.setText("即將完成");
			} else {
				jload.setText(String.format("%.0f%%", progress));
			}
		}
	}

	private void initButton() {
		button = new JButton(" ", image);
		button.setBackground(ColorManager.COLOR_WHITE);

		button.addActionListener(e -> { 
			String filepath = String.format("%s/%s.csv", FileRoute.PATH_SCRIPT, table.getValueAt(row, 0)); 
			if (table.getValueAt(row, column).equals("刪除")) {
				// 事件通知器
				fireEditingStopped();
				
				int result = JOptionPane.showConfirmDialog(null, "是否要刪除 " + table.getValueAt(row, 0) + ".csv",
						"刪除檔案", JOptionPane.YES_NO_OPTION);

				// 刪除劇本要做的事情
				doSomethingBeforeDelete();
				// 刪除檔案
				deleteFile(result, filepath);
			} 
			else if (table.getValueAt(row, column).equals("預覽")) {
				// 預覽劇本要做的事情
				jload = new JLabel("載入中...", iLoad, SwingConstants.CENTER);
				jload.setHorizontalTextPosition(SwingConstants.CENTER);
				jload.setOpaque(true);
				jload.setFont(new Font(Main.FONT_NAME, Font.PLAIN, 22));
				jload.setBackground(ColorManager.COLOR_WHITE);

				jpLoad = new JPanel(new GridLayout(1, 1));
				jpLoad.add(jload);
				jpLoad.setOpaque(false);

				// 取得螢幕的高度
				int h = Toolkit.getDefaultToolkit().getScreenSize().height - Main.SUB_FRAME_SIZE.height;
				// 取得螢幕的寬度
				int w = Toolkit.getDefaultToolkit().getScreenSize().width - Main.SUB_FRAME_SIZE.width;
				preview = new JFrame("劇本預覽");
				preview.setLocation((w/2), (h/2));
				preview.setSize(Main.SUB_FRAME_SIZE);
				preview.setLayout(new BorderLayout());
				preview.add(jpLoad, BorderLayout.CENTER);
				preview.setVisible(true);

				try {
					Thread loading = new Thread(new DelayThread(filepath));
					loading.start();
					Thread.sleep(1);
				} catch (InterruptedException e1) {
					Main.log(Level.WARNING, e1.getMessage());
					Thread.currentThread().interrupt();
				}

				fireEditingStopped();// 事件通知器

			} else if (table.getValueAt(row, column).equals("選擇")) {
				try {
					// 選擇劇本要做的事情
					SimulationScript Script = SimulationScript.FileReader(filepath, true);
					String[] temp = Script.getscript_paramater();
					
					Page4.set_path(filepath);
					Page4.setCurSelectRow(row);
					Page4.set_label_value(table.getValueAt(row, 0).toString(), table.getValueAt(row, 1).toString(), temp);
					Page4.setStatus("已選擇劇本", 1);
				} 
				catch (Exception e1) {
					Page4.setStatus("無法選擇劇本", 2);
				}
				
				fireEditingStopped();
			} 
		});
	}

	public void doSomethingBeforeDelete() {
		int CurSelectRow = Page4.getCurSelectRow();
		if (row < CurSelectRow) {
			Page4.setCurSelectRow(CurSelectRow - 1);
		} else if (row == CurSelectRow) {
			Page4.setCurSelectRow(-1);
			Page4.reset_label_value();
		}
	}
	
	public void deleteFile(int isDeletable, String path) {
		if (isDeletable == 0) { //使用者選擇"是"的按鈕
			File file = new File(path);
			
			try {
				Files.delete(file.toPath());
				Page4.deleteScript(path);
				((TableModel) table.getModel()).deleteRow(row);
				Page4.setStatus("已刪除檔案", 1);
			} 
			catch (IOException e1) {
				Main.log(Level.WARNING, e1.getMessage());
				Page4.setStatus("無法刪除檔案", 2);
			}
		}
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.num = String.valueOf(value);
		this.table = table;
		this.row = row;
		this.column = column;
		this.button.setText(value == null ? "" : String.valueOf(value));
		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return num;
	}

	/**
	 * 預覽畫面執行序實作
	 * 
	 * @author Chasel
	 *
	 */
	class DelayThread implements Runnable {

		private String path;
		
		public DelayThread(String path) {
			this.path = path;
		}
		
		@Override
		public void run() {
			try {
				Page5_loadScriptPanel Page5 = new Page5_loadScriptPanel(path,
						(String) (table.getValueAt(row, 0) + ".csv"));

				jload.setText("即將完成");

				JPanel jp = (JPanel) preview.getContentPane();
				jp.removeAll();
				jp.add(Page5.getPanel(), BorderLayout.CENTER);
				jp.repaint();

				preview.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				preview.setVisible(true);
			} catch (Exception e) {
				jload.setIcon(iEmpty);
				jload.setText("無法預覽劇本");
				Main.log(Level.WARNING, e.getMessage());
			}
		}
	}

}