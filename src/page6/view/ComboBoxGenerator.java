package page6.view;
 
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import page6.controller.Main;

public class ComboBoxGenerator {
	 
	private ComboBoxGenerator() { 
		//Do nothing.
	}
	 
	/**
	 * 建立下拉式選單
	 * 
	 * @param jframe 來自哪一個框架畫面
	 * @param Items 選單列表 (key)
	 * @param Panel 對應的版面 (value), 其中 index 0 應為切換版面用。
	 * @return
	 */
	public static JComboBox<String> Create_switchPage(JFrame jframe, String[] Items, ArrayList<JPanel> Panel) {   
		JComboBox<String> ComboBox = new JComboBox<String>(Items);
		ComboBox.setFont(Main.FONT_TITLE_BOLD);   
		ComboBox.addItemListener( e -> { 
			int index = ComboBox.getSelectedIndex() + 1;
			LayoutController.switchPage(jframe, Panel.get(0), Panel.get(index));  
		});  
		
		return ComboBox;
	}  
}
