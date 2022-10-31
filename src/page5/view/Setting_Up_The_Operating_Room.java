package page5.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit; 

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import page5.model.ColorManager; 
import page5.controller.Main;

public class Setting_Up_The_Operating_Room extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GridBagConstraints gbc = new GridBagConstraints();
	private JPanel gbc_panel; 
	private final int fill_N = GridBagConstraints.NONE;

	private final int alignL = GridBagConstraints.WEST; 
	private final int alignR = GridBagConstraints.EAST;
	
	public Setting_Up_The_Operating_Room(String[] OR,int num) {
		super("設定指定手術房間");  
		JLabel  rule1=new JLabel("規則一:"), rule2=new JLabel("規則二:")
				,R1O1=new JLabel("手術房間1="),R1O2=new JLabel("手術房間2=")
				,R2O1=new JLabel("手術房間1="),R2O2=new JLabel("手術房間2=")
				,R2O3=new JLabel("手術房間3="),R2O4=new JLabel("手術房間4=");
		JButton button = new JButton("設定完成");
		
		button.addActionListener(e -> { setVisible(false); });
		
		rule1.setFont(Main.FONT_TITLE_BOLD);
		rule2.setFont(Main.FONT_TITLE_BOLD);
		button.setFont(Main.FONT_TITLE_BOLD);
		R1O1.setFont(Main.FONT_TEXT);
		R1O2.setFont(Main.FONT_TEXT);
		R2O1.setFont(Main.FONT_TEXT);
		R2O2.setFont(Main.FONT_TEXT);
		R2O3.setFont(Main.FONT_TEXT);
		R2O4.setFont(Main.FONT_TEXT);
		
		setBackground(ColorManager.COLOR_WHITE);
		setLayout(new BorderLayout());
		getContentPane().setBackground(ColorManager.COLOR_WHITE);
		JComboBox<String> rule1_OR1_seletion =new JComboBox<String>();
		JComboBox<String> rule1_OR2_seletion =new JComboBox<String>();
		JComboBox<String> rule2_OR1_seletion =new JComboBox<String>();
		JComboBox<String> rule2_OR2_seletion =new JComboBox<String>();
		JComboBox<String> rule2_OR3_seletion =new JComboBox<String>();
		JComboBox<String> rule2_OR4_seletion =new JComboBox<String>();
		for (int i = 0; i < num; i++) {
			rule1_OR1_seletion.addItem(OR[i]);
			rule1_OR1_seletion.setFont(Main.FONT_TEXT);
			rule1_OR2_seletion.addItem(OR[i]);
			rule1_OR2_seletion.setFont(Main.FONT_TEXT);
			rule2_OR1_seletion.addItem(OR[i]);
			rule2_OR1_seletion.setFont(Main.FONT_TEXT);
			rule2_OR2_seletion.addItem(OR[i]);
			rule2_OR2_seletion.setFont(Main.FONT_TEXT);
			rule2_OR3_seletion.addItem(OR[i]);
			rule2_OR3_seletion.setFont(Main.FONT_TEXT);
			rule2_OR4_seletion.addItem(OR[i]);
			rule2_OR4_seletion.setFont(Main.FONT_TEXT);
		}
		gbc_panel=new JPanel();
		gbc_panel.setLayout(new GridBagLayout());
		
		Object[][] gbcData = {
			{1, 1, 0, 0, 1, 1, alignL, fill_N, rule1},
			{1, 1, 0, 1, 1, 1, alignR, fill_N, R1O1},
			{1, 1, 1, 1, 1, 1, alignL, fill_N, rule1_OR1_seletion},
			{1, 1, 2, 1, 1, 1, alignR, fill_N, R1O2},
			{1, 1, 3, 1, 1, 1, alignL, fill_N, rule1_OR2_seletion},
			{1, 1, 0, 2, 1, 1, alignL, fill_N, rule2},
			{1, 1, 0, 3, 1, 1, alignR, fill_N, R2O1},
			{1, 1, 1, 3, 1, 1, alignL, fill_N, rule2_OR1_seletion},
			{1, 1, 2, 3, 1, 1, alignR, fill_N,  R2O2},
			{1, 1, 3, 3, 1, 1, alignL, fill_N, rule2_OR2_seletion},
			{1, 1, 4, 3, 1, 1, alignR, fill_N, R2O3},
			{1, 1, 5, 3, 1, 1, alignL, fill_N, rule2_OR3_seletion},
			{1, 1, 6, 3, 1, 1, alignR, fill_N,  R2O4},
			{1, 1, 7, 3, 1, 1, alignL, fill_N, rule2_OR4_seletion},
			{1, 1, 7, 4, 1, 1, alignL, fill_N, button}
		}; 
		for(Object[] DataRow: gbcData) { setGBC(DataRow); }
		 
		add(gbc_panel);
		pack();
		int h = Toolkit.getDefaultToolkit().getScreenSize().height- getHeight();//取得螢幕的高度
		int w = Toolkit.getDefaultToolkit().getScreenSize().width - getWidth(); //取得螢幕的寬度 
		setLocation(w/2, h/2);		
		setVisible(true); 
	}
	
	//  wx  wy  x  y  w  h  align  fill  A
	private void setGBC(Object[] args) {
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.weightx = Double.valueOf(args[0].toString());
		gbc.weighty = Double.valueOf(args[1].toString());
		gbc.gridx = Integer.valueOf(args[2].toString());
		gbc.gridy = Integer.valueOf(args[3].toString());
		gbc.gridwidth = Integer.valueOf(args[4].toString());
		gbc.gridheight = Integer.valueOf(args[5].toString());
		gbc.anchor = Integer.valueOf(args[6].toString());
		gbc.fill = Integer.valueOf(args[7].toString());  
		gbc_panel.add((Component) args[8], gbc);
	}
}
