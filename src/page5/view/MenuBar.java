package page5.view;

import java.awt.Dimension;
import java.awt.FlowLayout; 
import java.util.ArrayList;

import javax.swing.ImageIcon; 
import javax.swing.JFrame;
import javax.swing.JLabel; 
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import page5.controller.Main;
import page5.model.ColorManager;
import page5.model.FileRoute;

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
		JLabel jbHome = new JLabel("");
		
		ImageIcon ihome = new ImageIcon(FileRoute.IMAGE_HOME);  
		jbHome.setIcon(ihome);
		
		jbHome.setPreferredSize(Size); 
		jbHome.setFont(Main.FONT_TEXT);
		jbHome.setOpaque(true);
		jbHome.setBorder(new TextBorderUtils(ColorManager.COLOR_MENUBAR_BACKGOUND, 10, true));
		jbHome.setBackground(ColorManager.COLOR_MENUBAR_BTNHOME);
		jbHome.setForeground(ColorManager.COLOR_MENUBAR_BTNHOME_TEXT);
		jbHome.setHorizontalAlignment(SwingConstants.CENTER); 
		return jbHome;    
	}

	private JLabel Confirm_Field() { 
		JLabel jbLS = new JLabel("絋粄把计逆");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Create_A_New_Data_Distribution() { 
		JLabel jbLS = new JLabel("浪跌参璸挡狦");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Set_Data_Distribution() { 
		JLabel jbLS = new JLabel("砞﹚戈だガ");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Set_Script_Parameters_And_Create_Script() {
		JLabel jbLS = new JLabel("砞﹚粿セ把计&ミ粿セ");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Set_Simulation_Parameters() { 
		JLabel jbLS = new JLabel("砞﹚家览把计");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	}
	
	private JLabel Run_A_Simulation_And_Display_The_Results() { 
		JLabel jbLS = new JLabel("磅︽家览&陪ボ挡狦");  
		jbLS.setForeground(ColorManager.COLOR_MENUBAR_TEXT_DARK);
		jbLS.setFont(Main.FONT_TEXT);
		return jbLS;  
	} 
}
