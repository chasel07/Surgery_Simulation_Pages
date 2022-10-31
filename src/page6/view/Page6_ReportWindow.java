package page6.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Toolkit; 
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import page6.model.*;  
import page6.controller.*; 
 
public class Page6_ReportWindow extends JFrame {
	private static final long serialVersionUID = 1L;   
	/**
	 * View 
	 */
	private String strSimulResults = "模擬結果";
	private String labelRoom = "手術房間數"; 
	private String Result_Title = "各間手術房的使用情形";
	private JFrame window = this; 
	 
	private final double JF_SCALE = 0.7;
	private final int h = (int)(JF_SCALE * Toolkit.getDefaultToolkit().getScreenSize().height);// 取得螢幕的高度
	private final int w = (int)(JF_SCALE * Toolkit.getDefaultToolkit().getScreenSize().width); // 取得螢幕的寬度
	private Dimension windowSize = new Dimension(w, h);
	
	private JTabbedPane TabbedPanel;
 
	public Page6_ReportWindow(String title, simulation src) throws IOException {
		super(title);
		createReportWindow(src);
	}
	
	
	private void createReportWindow(simulation src) throws IOException { 
		setBackground(ColorManager.COLOR_WHITE);
		getContentPane().setBackground(ColorManager.COLOR_WHITE);
		setLayout(new BorderLayout(10,10));
		setIconImage(ImageIO.read(new File(FileRoute.IMAGE_LOGO)));
		
		src.run();
		
		try { 
			LocalDate now = LocalDate.now();
			File srcfile = new File(String.format("%s/calender_%s_%s.csv", FileRoute.PATH_CALENDAR, src.getMaxRoomN(), now.toString()));
			SurgeryGanttChartLayout sgcl = new SurgeryGanttChartLayout(this, srcfile);
			
			TabbedPanel = new JTabbedPane(); 
			TabbedPanel.setBackground(ColorManager.COLOR_BLACK);
			TabbedPanel.setFont(Main.FONT_TEXT_BOLD);
			TabbedPanel.addTab(strSimulResults, TextReportPanel(src));
			TabbedPanel.addTab("排程表(甘特圖)", sgcl.getPanel());
			TabbedPanel.addTab("排程表",Schedule(srcfile.toString()));
			TabbedPanel.addTab("評估與建議", ChartReportPanel(src));  
			TabbedPanel.setPreferredSize(windowSize);
			add(TabbedPanel, BorderLayout.CENTER); 
		} catch (IOException e) { 
			Main.log(Level.WARNING, e.getMessage());
		} 
		
		this.pack();
		int h = Toolkit.getDefaultToolkit().getScreenSize().height- getHeight();//取得螢幕的高度
		int w = Toolkit.getDefaultToolkit().getScreenSize().width - getWidth(); //取得螢幕的寬度 
		this.setLocation(w/2, h/2);
		this.setVisible(true);
	}
	 
	//模擬結果報告(文字)
	public JPanel TextReportPanel(simulation simul) throws IOException { 
		//顯示手術室文字資訊的面板
		JTextPane jtResult = new JTextPane();
		jtResult.setFont(Main.FONT_TEXT2);
		jtResult.setBackground(ColorManager.COLOR_WHITE);
		jtResult.setEditable(false); 
		jtResult.setCaretPosition(0);
		
		//預設顯示第一間手術室使用情形
		jtResult.setText(simul.getORUsageReport(0)); 
		
		//建立清單內容
        DefaultListModel<String> listModel = new DefaultListModel<>(); 
        listModel.addElement(Result_Title); 
        
        //加入文字檔中有使用到的手術房編號
        for(String OR: simul.getORList()) listModel.addElement(OR); 
		JList<String> jlRoom = new JList<>(listModel);   
		jlRoom.setSelectedIndex(1);
		jlRoom.setSelectionBackground(ColorManager.COLOR_TABLE_BACKGOUND);  
		jlRoom.setCellRenderer(new jlRenderer()); 
		jlRoom.addListSelectionListener(e -> {
			int n = jlRoom.getSelectedIndex();
			if(n > 0) {
				jtResult.setText(simul.getORUsageReport(n - 1)); 
				jtResult.setCaretPosition(0);  
			} 
		}); 
		
		Object[] left = {0, 0, 1, 1, 0.2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new JScrollPane(jlRoom)};
		Object[] right ={1, 0, 1, 1, 0.8, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new JScrollPane(jtResult)};
		 
		JPanel textReport = LayoutController.createPanel(); 
		LayoutController.add(left);
		LayoutController.add(right);
		
		return textReport;
	}
	 
	public class jlRenderer extends JLabel implements ListCellRenderer<String> {
		 
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) { 
			if(value.equals(Result_Title)) { 
				setFont(Main.FONT_TEXT);
				setBackground(ColorManager.COLOR_RESULT_TITLE_BACKGOUND); 
				setForeground(Color.white); 
				setHorizontalAlignment(SwingConstants.CENTER);
				setOpaque(true);
			} 
			else {
				setFont(Main.FONT_TEXT); 
				setHorizontalAlignment(SwingConstants.LEFT);
				if(isSelected) {
					setBackground(ColorManager.COLOR_TABLE_BACKGOUND);  
				}else {
					setBackground(ColorManager.COLOR_WHITE);  
				}  
				setForeground(Color.BLACK);   
			} 
			setText(value);  
			return this; 
		}
	}
	
	/**
	 * 評估與建議 折線圖
	 * @return 
	 */
	public JPanel ChartReportPanel(simulation simul) { 
		ArrayList<lineChart> charts = new ArrayList<>();
		charts.add(new lineChart("", labelRoom, "人數" , simul.getOperationFinish(), false));
		charts.add(new lineChart("", labelRoom, "利用率", simul.getOperationUtilization(), true));
		charts.add(new lineChart("", labelRoom, "等候時間(分鐘)", simul.getAverageWaiting(), false)); 
		charts.add(new lineChart("", labelRoom, "人數", simul.getMaxWaiting(), false)); 
		charts.add(new lineChart("", labelRoom, "時間(分鐘)", simul.getRoom_OverTime(), false));
		
		JPanel cPanel = new JPanel(new BorderLayout(5,5)); 
		cPanel.add(charts.get(0).getChartPanel(), BorderLayout.CENTER);
		
		JLabel sLabel = new JLabel("目前所選擇的圖表：");
		sLabel.setFont(Main.FONT_TITLE);
		
		JComboBox<String> chartsName  = new JComboBox<String>();
		chartsName.setFont(Main.FONT_TITLE_BOLD);
		chartsName.addItem(Main.CHART_TITLE_OF);
		chartsName.addItem(Main.CHART_TITLE_OU);
		chartsName.addItem(Main.CHART_TITLE_AW);
		chartsName.addItem(Main.CHART_TITLE_MW);
		chartsName.addItem(Main.CHART_TITLE_RO); 
		chartsName.addItemListener(e -> { 
			int index = chartsName.getSelectedIndex();
			cPanel.removeAll();
			cPanel.add(charts.get(index).getChartPanel());
			cPanel.repaint();
			cPanel.setBackground(ColorManager.COLOR_WHITE);
			window.setVisible(true); 
		});
		
		JPanel sPanel = new JPanel();
		sPanel.setBackground(ColorManager.COLOR_WHITE);
		sPanel.add(sLabel);
		sPanel.add(chartsName);
		
		JPanel panel = new JPanel(new BorderLayout());  
		panel.setBackground(ColorManager.COLOR_WHITE);
		panel.add(sPanel, BorderLayout.NORTH);
		panel.add(cPanel, BorderLayout.CENTER);
		return panel; 
	}
	
	public JScrollPane Schedule(String FileName) throws IOException {
		JScrollPane jsp = new JScrollPane(new ScheduleTable(FileName));    
		return jsp; 
	}
}
