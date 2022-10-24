package page1.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener; 
import java.util.ArrayList;

import javax.swing.ImageIcon; 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import page1.controller.*;
import page1.model.*;


  
public class MenuBar extends JPanel{ 
	
	private static final long serialVersionUID = 1L;
	
	private final JFrame jfSystem = Main.JF_SYSTEM;
	private final Dimension Size = new Dimension(120, 30);
	
	private final ArrayList<JLabel> jl_route = new ArrayList<>();
	public ArrayList<JLabel> getjl_route() { return jl_route; }
	
	public MenuBar() { 
		super(); 
		FlowLayout FL = new FlowLayout(FlowLayout.LEFT);
		FL.setHgap(30);
		FL.setVgap(10);
		
		setLayout(FL);
		setBackground(ColorManager.COLOR_MENUBAR_BACKGOUND);
		setPreferredSize(new Dimension(jfSystem.getWidth(), 50));
		
		add(Home()); 

		jl_route.add(Confirm_Field());  
		jl_route.add(Create_A_New_Data_Distribution()); 
		jl_route.add(Set_Data_Distribution());
		jl_route.add(Set_Script_Parameters_And_Create_Script()); 
		jl_route.add(Set_Simulation_Parameters());  
		jl_route.add(Run_A_Simulation_And_Display_The_Results());
		
		for(JLabel i: jl_route) add(i);
	}
	   
	private JLabel Home(){
		JLabel jbHome = new JLabel("首頁");
		
		ImageIcon ihome = new ImageIcon(FileRoute.IMAGE_HOME);  
		jbHome.setIcon(ihome);
		
		jbHome.setPreferredSize(Size); 
		jbHome.setFont(Main.FONT_TEXT);
		jbHome.setOpaque(true);
		jbHome.setBorder(new TextBorderUtils(ColorManager.COLOR_MENUBAR_BACKGOUND, 10, true));
		jbHome.setBackground(ColorManager.COLOR_MENUBAR_BTNHOME);
		jbHome.setForeground(ColorManager.COLOR_MENUBAR_BTNHOME_TEXT);
		jbHome.setHorizontalAlignment(SwingConstants.CENTER);
		jbHome.addMouseListener(new MouseListener() {  
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(Main.IS_HOME_PAGE) return; 
				
				int isBack = 
					JOptionPane.showConfirmDialog(jfSystem, "確定要回到首頁嗎?", "回首頁", JOptionPane.OK_CANCEL_OPTION);
				
				if(isBack == 0) {   
					Main.clearSelected(); 
					
					for(JLabel i: jl_route) {
						i.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
					}  
					
					Main.backHome(); 
				} 
			}
			@Override
			public void mousePressed(MouseEvent e) {
				jbHome.setFont(Main.FONT_TITLE_BOLD);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				jbHome.setFont(Main.FONT_TITLE);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				jbHome.setFont(Main.FONT_TITLE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				jbHome.setFont(Main.FONT_TEXT);
			} 
		}); 
		return jbHome;   
		 
	}

	private JLabel Confirm_Field() { 
		JLabel jbLS = new JLabel("確認參數欄位");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Create_A_New_Data_Distribution() { 
		JLabel jbLS = new JLabel("檢視統計結果");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Set_Data_Distribution() { 
		JLabel jbLS = new JLabel("設定資料分布");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Set_Script_Parameters_And_Create_Script() {
		JLabel jbLS = new JLabel("設定劇本參數&建立劇本");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Set_Simulation_Parameters() { 
		JLabel jbLS = new JLabel("設定模擬參數");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Run_A_Simulation_And_Display_The_Results() { 
		JLabel jbLS = new JLabel("執行模擬&顯示結果");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	
	
}
