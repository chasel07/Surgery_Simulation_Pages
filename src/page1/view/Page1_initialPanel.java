package page1.view;
 
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.ImageIcon; 
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import page1.controller.*;
import page1.model.*;

import javax.imageio.ImageIO; 
 
public class Page1_initialPanel extends JPanel {  
	
	private String[] flowName = {"完整流程1","完整流程2","快速模擬"};
	
	private static final long serialVersionUID = 1L;
	
	private String FileType = Main.FILE_TYPE;
	private String comment  = Main.COMMENT; 
	
	private JFrame jfSystem = Main.JF_SYSTEM;  
	private JPanel jpLoad;
	
	private File fileSrc;   
	 
	private ArrayList<JComponent> jpSelect = new ArrayList<>();
	
	private boolean regulate = false; 
	//---------------------------------------------------------------
	private JLabel jload; 
	
	public void setLoadingText(String src) { jload.setText(src); }
	
	public void setLoadingText(String fileName, Double progress) { 
		if(fileSrc.toString().equals(fileName)) { 
			if(progress > 99) { 
				jload.setText("即將完成"); 
			}else { 
				jload.setText(String.format("%.0f%%", progress)); 
			} 
		} 
	}
		  
	public Page1_initialPanel() throws IOException {
		super(new GridBagLayout());  
	
		ImageIcon iLoad = new ImageIcon(FileRoute.IMAGE_LOADING);  
		
		jload = new JLabel("0%", iLoad, SwingConstants.CENTER);
		jload.setHorizontalTextPosition(SwingConstants.CENTER); 
		jload.setOpaque(true);
		jload.setFont(new Font(Main.FONT_NAME, Font.PLAIN, 22));
		jload.setBackground(ColorManager.COLOR_WHITE);
		
		jpLoad = new JPanel(new GridLayout(1,1));
		jpLoad.add(jload); 
		jpLoad.setOpaque(false); 
		 
	    setOpaque(false); 
	    
	    int fill_B = GridBagConstraints.BOTH;  
		int alignL = GridBagConstraints.WEST; 
	    Object[][] gbcData = {
	    		{"1",  "0.6", "0", "0", "2", "1", alignL, fill_B, createArea("")},
	    		{"0.23", "1", "0", "1", "1", "1", alignL, fill_B, createArea("")},
	    		{"1",    "1", "1", "1", "1", "1", alignL, fill_B, createArea(flowName[0])},
	    		{"0.23", "1", "0", "3", "1", "1", alignL, fill_B, createArea("")},
	    		{"1",    "1", "1", "3", "1", "1", alignL, fill_B, createArea(flowName[1])},
	    		{"0.23", "1", "0", "5", "1", "1", alignL, fill_B, createArea("")},
	    		{"1",    "1", "1", "5", "1", "1", alignL, fill_B, createArea(flowName[2])}
	    };
	    
	    GridBagConstraints gbc = new GridBagConstraints(); 
	    gbc.insets = new Insets(0,0,0,20); 
	    for(Object[] row: gbcData) {
	    	gbc.weightx = Double.valueOf(row[0].toString());
			gbc.weighty = Double.valueOf(row[1].toString());
	    	gbc.gridx = Integer.valueOf(row[2].toString());
			gbc.gridy = Integer.valueOf(row[3].toString());
			gbc.gridwidth = Integer.valueOf(row[4].toString());
			gbc.gridheight = Integer.valueOf(row[5].toString());
			gbc.anchor = Integer.valueOf(row[6].toString());
			gbc.fill = Integer.valueOf(row[7].toString());
			this.add((Component) row[8], gbc); 
	    } 
	} 
	 
	public void clearSelected() {
		for(JComponent i: jpSelect) {
			i.setBorder(new EmptyBorder(25,0,0,25)); 
		} 
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); 
		
		try {
			Dimension size = Main.JF_SYSTEM.getSize();
			BufferedImage bufferedImage = ImageIO.read(new File(FileRoute.IMAGE_WORKFLOW));
			Image image = bufferedImage.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
			if (image != null) {
				g.drawImage(image, 1, 1, this.getWidth(), this.getHeight(), this);
			} 
		} 
		catch (IOException e) {  
			Main.log(Level.WARNING, e.getMessage());
		}  
	}
	
	
	private JComponent createArea(String title) { 
		 TitledBorder titledBorder = getTitleBorder(title);
		 EmptyBorder emptyBorder = new EmptyBorder(25,0,0,25);
		 
		 JLabel p = new JLabel();  
		 p.setBorder(regulate ? titledBorder : emptyBorder);
	     p.setOpaque(false);
		 p.addMouseListener(new MouseListener() { 
			@Override
			public void mouseClicked(MouseEvent e) { 
				jload.setText("0%");
				
				if(title.equals(flowName[0])) {
					fullProcess1(); 
				}
				else if(title.equals(flowName[1])) { 
					fullProcess2(); 
				}
				else if(title.equals(flowName[2])) {
					quickSimulation();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) { 
				//Do nothing.
			}

			@Override
			public void mouseReleased(MouseEvent e) { 
				//Do nothing.
			}

			@Override
			public void mouseEntered(MouseEvent e) { 
				p.setBorder(title.equals("") ? emptyBorder : titledBorder);
			}

			@Override
			public void mouseExited(MouseEvent e) { 
				p.setBorder(emptyBorder);
			}
			  
		 }); 
		 
		 jpSelect.add(p); 
		 
		 return p;
	} 
	  
	/**
	 * 建立Thread例項，在Thread的構造方法中傳遞Runnable例項
	 * Runnable就代表在 Thread 上執行的任務 
	 */
	private void fullProcess1() { 
		fileSrc = OpenFile(FileType, comment, FileRoute.PATH_RAW);
		
		if(fileSrc != null) {
			//畫面切換
			Main.switchPage(jpLoad);  
			try {
				Thread loading = new Thread(new DelayThread(fileSrc, 0));
				loading.start();   
				Thread.sleep(1);
			} catch (InterruptedException e1) { 
				Main.log(Level.WARNING, e1.getMessage());
				Thread.currentThread().interrupt();
			}  
		}  
	}
	
	private void fullProcess2() {
		fileSrc = OpenFile(FileType, comment, FileRoute.PATH_DISTRIBUTION);  
		if(fileSrc != null) {   
			Main.switchPage(jpLoad);  
			try {
				Thread loading = new Thread(new DelayThread(fileSrc, 1));
				loading.start();   
				Thread.sleep(1);
			} catch (InterruptedException e1) { 
				Main.log(Level.WARNING, e1.getMessage());
				Thread.currentThread().interrupt();
			}  
		} 
	}
	
	private void quickSimulation() {
		fileSrc = OpenFile(FileType, comment, FileRoute.PATH_SCRIPT);  
		if(fileSrc != null) {   
			Main.switchPage(jpLoad);  
			try {
				Thread loading = new Thread(new DelayThread(fileSrc, 2));
				loading.start();  
				Thread.sleep(1);
			} catch (InterruptedException e1) { 
				Main.log(Level.WARNING, e1.getMessage());
				Thread.currentThread().interrupt();
			}  
		}   
	}
	
	
	//自定義執行緒執行任務類
	class DelayThread implements Runnable{
	    
	    private File fileSrc;  
	    private int jpSelect; 
		
	    public DelayThread(File src, int jpSelect) {
	    	this.fileSrc = src; 
	    	this.jpSelect = jpSelect;
	    }
	    
	    @Override
	    public void run() { 
	    	switch(jpSelect) {
	    		case 0: 
	    			Main.switchLights(0);
	    			JOptionPane.showMessageDialog(jfSystem, "資料檢視頁面"); 
	    			break; 
	    		case 1:
	    			Main.switchLights(2);
	    			JOptionPane.showMessageDialog(jfSystem, "分布選擇頁面"); 
	    			break; 
	    		case 2: 
	    			Main.switchLights(4);
	    			JOptionPane.showMessageDialog(jfSystem, "模擬參數設置頁面"); 
	    			break; 
		    	default:
		    		Main.switchLights(-1);
		    		JOptionPane.showMessageDialog(jfSystem, "頁面不存在");
	    	} 
	    	
	    	Main.switchLights(-1); 
	    	Main.backHome(); 
	    } 
	}
	 
	private File OpenFile(String FileType, String comment, String path) { 
	 	myFilter filter = new myFilter(FileType, comment);
		 
	 	JFileChooser select = new JFileChooser(path);  
	 	 
	 	select.setDialogTitle("Open the file");  
	 	select.addChoosableFileFilter(filter);
	 	select.removeChoosableFileFilter(select.getAcceptAllFileFilter());
	 	select.setFileFilter(filter); 
		 
		int result = select.showOpenDialog(jfSystem); 
		if(result == JFileChooser.APPROVE_OPTION) {
			return select.getSelectedFile();
		}else {
			return null; 
		}   
	}
	  
	private class myFilter extends javax.swing.filechooser.FileFilter{
		String extension , description;
		public myFilter(String e,String d) {
			extension = e.toLowerCase();
			description = d; 
		} 
		@Override
		public boolean accept(File f) {
			if(f.isDirectory()) return true;
			String e = null;
			String s = f.getName();
			int i = s.lastIndexOf(".");
			if(i > 0 && i < s.length() - 1) {
				e = s.substring(i + 1).toLowerCase();
				if(extension.equals(e))return true;
			} 
			return false;
		}

		@Override
		public String getDescription() {
			return this.description;
		}
	}
	   
	private TitledBorder getTitleBorder(String title) {
		TitledBorder i = new TitledBorder(title);
		i.setTitleFont(Main.FONT_TITLE_BOLD);
		i.setTitleColor(ColorManager.COLOR_TITLE);
		i.setBorder(Main.LINEBORDER);
		return i;
	}
}
