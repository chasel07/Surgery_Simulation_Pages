package page5.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LayoutController {
	private static JFrame jfSystem;
	private static JPanel gbc_panel;
	private static GridBagConstraints gbc = new GridBagConstraints();
	
	private LayoutController() {
		//Do nothing. 
	}
	 
	public static void setMainFrame(JFrame jf) { jfSystem = jf; }  
	
	public static JPanel createPanel() { 
		gbc_panel = new JPanel(new GridBagLayout());
		gbc_panel.setBackground(Color.white); 
		gbc.insets = new Insets(0,0,0,0);  
		return gbc_panel;
	}  
	
	public static JPanel createPanel(LayoutManager Layout) { 
		gbc_panel = new JPanel(Layout);
		gbc_panel.setBackground(Color.white); 
		gbc.insets = new Insets(0,0,0,0);  
		return gbc_panel;
	} 
	 
	public static JPanel createPanel(Component A) { 
		gbc_panel = new JPanel(new BorderLayout());
		gbc_panel.add(A, BorderLayout.CENTER);
		gbc_panel.setBackground(Color.white);  
		return gbc_panel;
	}  
	
	public static void switchPage(JPanel jp, Component replacer) { 
		jp.removeAll(); 
		jp.add(replacer, BorderLayout.CENTER);
		jp.repaint();   
		jfSystem.setVisible(true);
	}  
	
	public static void switchPage(JFrame JF, JPanel JP, Component Replacer) { 
		JP.removeAll(); 
		JP.add(Replacer, BorderLayout.CENTER);
		JP.repaint();   
		JF.setVisible(true);
	} 
	
	
	public static JPanel getPanel() {  
		return gbc_panel;
	}  
	
	public static void setTopMargin(int top) {
		gbc.insets = new Insets(top,0,0,0); 
	}
	 
	public static void add(Component A) 
	{  
		gbc_panel.add(A);
	} 
	
	public static void add(int x, int y, Component A) 
	{ 
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.fill   = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1; 
		gbc.gridwidth  = 1;
		gbc.gridheight = 1; 
		gbc_panel.add(A, gbc);
	} 
	
	public static void add(int x, int y, int align, Component A) {
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = align;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = 1;
		gbc.gridheight = 1; 
		gbc_panel.add(A, gbc);
	}
 
	public static void add(int x,int y,int w,int h, Component A) 
	{ 
		gbc.fill   = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1; 
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth  = w;
		gbc.gridheight = h; 
		gbc_panel.add(A, gbc);
	} 
	 
	public static void add(int x,int y,int w,int h,double wx,double wy, Component A) 
	{ 
		gbc.fill   = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth  = w;
		gbc.gridheight = h; 
		gbc_panel.add(A, gbc);
	} 
	 
	public static void add(Object[] args) { 
		gbc.gridx = Integer.valueOf(args[0].toString());
		gbc.gridy = Integer.valueOf(args[1].toString());
		gbc.gridwidth = Integer.valueOf(args[2].toString());
		gbc.gridheight = Integer.valueOf(args[3].toString());
		gbc.weightx = Double.valueOf(args[4].toString());
		gbc.weighty = Double.valueOf(args[5].toString());
		gbc.anchor = Integer.valueOf(args[6].toString());
		gbc.fill = Integer.valueOf(args[7].toString());
		
		gbc_panel.add((java.awt.Component) args[8], gbc);
	} 
}
