package page3.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.border.LineBorder;

public class TextBorderUtils extends LineBorder { 
	
	private static final long serialVersionUID = 1L;
	  
	private Color color_Outside;	
	
	public TextBorderUtils(Color color, int thickness, boolean roundedCorners) { 
		super(color, thickness, roundedCorners);  
		color_Outside = null;
	}
	
	public TextBorderUtils(Color color1, Color color2, int thickness, boolean roundedCorners) { 
		super(color1, thickness, roundedCorners);  
		color_Outside = color2;
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, 
											   RenderingHints.VALUE_ANTIALIAS_ON);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHints(rh);
		
		if(color_Outside != null) {
			g2.setColor(color_Outside);
			
			for(int i=0; i<thickness; i++) {
				g2.drawRect(x-i, y-i, width-i-i, height-i-i);
			}
			  
			g2.setColor(this.lineColor); 
			if(!roundedCorners) {
			    g2.fillRect(x, y, width-1, height-1);
			}
			else {   
				g2.drawRoundRect(x, y, width-1, height-1, 20, 20);
			} 
		} 
		else { 
			
			g2.setColor(this.lineColor); 
			if(!roundedCorners) {
			    g2.fillRect(x, y, width-1, height-1);
			}
			else for(int i=0; i<thickness; i++) { 
				g2.drawRoundRect(x-i, y, width + i*2 -2, height-1, 20, 20);
			}  
			
		} 
	} 
}
