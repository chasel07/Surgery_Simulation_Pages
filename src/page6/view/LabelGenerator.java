package page6.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import page6.controller.Main;

public class LabelGenerator {
	
	private LabelGenerator() {
		//Do nothing. 
	}
	
	public static JLabel Create(String Text) {  
		 JLabel label = new JLabel(Text); 
		 label.setFont(Main.FONT_TEXT);
		 return label; 
	}
	 
	public static JLabel Create_Title_Bold(String Text) {  
		 JLabel label = new JLabel(Text); 
		 label.setFont(Main.FONT_TITLE_BOLD); 
		 return label; 
	}
	
	public static JLabel Create_Title(String Text) {  
		 JLabel label = new JLabel(Text); 
		 label.setFont(Main.FONT_TITLE); 
		 return label; 
	}
	
	public static JLabel Create_Legend(String Text, Color Legend_Color) {  
		int size = 15;
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setColor(Legend_Color);
		g.fillRect(0, 0, size, size);
		g.dispose();
	    
	    JLabel label = new JLabel(Text);
		label.setFont(Main.FONT_TITLE);
		label.setIcon(new ImageIcon(img));
		label.setPreferredSize(new Dimension(100,25));
		 
		return label; 
	}
	
}
