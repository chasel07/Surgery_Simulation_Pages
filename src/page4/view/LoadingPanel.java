package page4.view;

import java.awt.Font;
import java.awt.GridLayout; 

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import page4.model.*;
import page4.controller.*;

public class LoadingPanel extends JPanel {
	 
	private static final long serialVersionUID = 1L; 
	private JLabel jload; 
	
	public LoadingPanel() {
		ImageIcon iLoad = new ImageIcon(FileRoute.IMAGE_LOADING);  
		
		jload = new JLabel("0%", iLoad, SwingConstants.CENTER);
		jload.setHorizontalTextPosition(SwingConstants.CENTER); 
		jload.setOpaque(true);
		jload.setFont(new Font(Main.FONT_NAME, Font.PLAIN, 22));
		jload.setBackground(ColorManager.COLOR_WHITE);
		
		this.setLayout(new GridLayout(1,1));
		this.add(jload); 
		this.setOpaque(false);  
	}
	
	public void setLoadingText(String src) { jload.setText(src); }
	
	public void setLoadingText(String fileName, Double progress) {  
		if(progress > 99) { 
			jload.setText("即將完成"); 
		}else { 
			jload.setText(String.format("%.0f%%", progress)); 
		}  
	}
}
