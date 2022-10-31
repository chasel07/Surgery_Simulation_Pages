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
	private String strSimulResults = "�������G";
	private String labelRoom = "��N�ж���"; 
	private String Result_Title = "�U����N�Ъ��ϥα���";
	private JFrame window = this; 
	 
	private final double JF_SCALE = 0.7;
	private final int h = (int)(JF_SCALE * Toolkit.getDefaultToolkit().getScreenSize().height);// ���o�ù�������
	private final int w = (int)(JF_SCALE * Toolkit.getDefaultToolkit().getScreenSize().width); // ���o�ù����e��
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
			TabbedPanel.addTab("�Ƶ{��(�̯S��)", sgcl.getPanel());
			TabbedPanel.addTab("�Ƶ{��",Schedule(srcfile.toString()));
			TabbedPanel.addTab("�����P��ĳ", ChartReportPanel(src));  
			TabbedPanel.setPreferredSize(windowSize);
			add(TabbedPanel, BorderLayout.CENTER); 
		} catch (IOException e) { 
			Main.log(Level.WARNING, e.getMessage());
		} 
		
		this.pack();
		int h = Toolkit.getDefaultToolkit().getScreenSize().height- getHeight();//���o�ù�������
		int w = Toolkit.getDefaultToolkit().getScreenSize().width - getWidth(); //���o�ù����e�� 
		this.setLocation(w/2, h/2);
		this.setVisible(true);
	}
	 
	//�������G���i(��r)
	public JPanel TextReportPanel(simulation simul) throws IOException { 
		//��ܤ�N�Ǥ�r��T�����O
		JTextPane jtResult = new JTextPane();
		jtResult.setFont(Main.FONT_TEXT2);
		jtResult.setBackground(ColorManager.COLOR_WHITE);
		jtResult.setEditable(false); 
		jtResult.setCaretPosition(0);
		
		//�w�]��ܲĤ@����N�Ǩϥα���
		jtResult.setText(simul.getORUsageReport(0)); 
		
		//�إ߲M�椺�e
        DefaultListModel<String> listModel = new DefaultListModel<>(); 
        listModel.addElement(Result_Title); 
        
        //�[�J��r�ɤ����ϥΨ쪺��N�нs��
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
	 * �����P��ĳ ��u��
	 * @return 
	 */
	public JPanel ChartReportPanel(simulation simul) { 
		ArrayList<lineChart> charts = new ArrayList<>();
		charts.add(new lineChart("", labelRoom, "�H��" , simul.getOperationFinish(), false));
		charts.add(new lineChart("", labelRoom, "�Q�βv", simul.getOperationUtilization(), true));
		charts.add(new lineChart("", labelRoom, "���Ԯɶ�(����)", simul.getAverageWaiting(), false)); 
		charts.add(new lineChart("", labelRoom, "�H��", simul.getMaxWaiting(), false)); 
		charts.add(new lineChart("", labelRoom, "�ɶ�(����)", simul.getRoom_OverTime(), false));
		
		JPanel cPanel = new JPanel(new BorderLayout(5,5)); 
		cPanel.add(charts.get(0).getChartPanel(), BorderLayout.CENTER);
		
		JLabel sLabel = new JLabel("�ثe�ҿ�ܪ��Ϫ�G");
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
