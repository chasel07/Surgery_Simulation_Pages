package page4.view;

import java.awt.BorderLayout; 
import java.awt.Component; 

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import page4.controller.Main;
import page4.model.ColorManager;
  

public class MyButtonRenderer implements TableCellRenderer {
	private JPanel panel; 
	private JButton button;  
	private ImageIcon image;
	
	private Page4_ScriptGenerationPanel Page4;
	
	public MyButtonRenderer(ImageIcon image, Page4_ScriptGenerationPanel Page4) {
		this.Page4 = Page4;
		this.image = image;
		 
		initButton();
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(button, BorderLayout.CENTER);
		panel.setOpaque(false);
	}

	private void initButton() {
		button = new JButton(" ",image);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setOpaque(true);
	}
 
	public Component getTableCellRendererComponent(JTable table, Object value, 
												boolean isSelected, boolean hasFocus, int row, int column) {
		 
		if(row == Page4.getCurSelectRow()) {
			button.setBackground(ColorManager.COLOR_TABLEHAND_BACKGOUND);   
			button.setForeground(ColorManager.COLOR_TABLEHAND_FOREGOUND);   
		}
		else {
			button.setBackground(ColorManager.COLOR_WHITE);   
			button.setForeground(ColorManager.COLOR_BLACK);  
		}
		
		button.setText(value == null ? "" : String.valueOf(value));
		button.setFont(Main.FONT_TEXT);
		return panel;
	}

}