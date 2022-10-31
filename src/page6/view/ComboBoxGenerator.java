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
	 * �إߤU�Ԧ����
	 * 
	 * @param jframe �Ӧۭ��@�Ӯج[�e��
	 * @param Items ���C�� (key)
	 * @param Panel ���������� (value), �䤤 index 0 �������������ΡC
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
