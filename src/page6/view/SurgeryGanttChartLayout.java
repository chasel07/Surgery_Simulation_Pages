package page6.view;

import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel; 

import page6.model.Calendar;
import page6.model.CalendarManager;
 

public class SurgeryGanttChartLayout {  
	
	private CalendarManager CM;
	
	//�ΨӤ���panel �� �̯S��
	private JPanel mainPanel; 
	public JPanel getPanel() { 
		return mainPanel; 
	} 
	
	/**
	 * ��N�ɶ��̯S�� �ƪ��t�m
	 * @throws IOException 
	 */
	public SurgeryGanttChartLayout(JFrame jframe, File src) throws IOException { 
		/*
		 * Data
		 */
		CM = new CalendarManager(src); 
		TreeMap<Integer, ArrayList<String[]>> data = 
				CM.GroupByDay(Calendar.SUR_START.column(), Calendar.SUR_END.column());
		String[] Days = ToDaysList(data.keySet());
		/*
		 * View 
		 */ 
		ArrayList<JPanel> GanttChartPerDay = createORGanttChart(data);  
		JComboBox<String> JCB_SelectDay = ComboBoxGenerator.Create_switchPage(jframe, Days, GanttChartPerDay); 
		JLabel Title_SelectDay = LabelGenerator.Create_Title("�ثe��ܪ��u�@�ѡG");
		  
		int fill = GridBagConstraints.NONE;
		int alignLeft = GridBagConstraints.WEST;
		int alignRight = GridBagConstraints.EAST;
		
		mainPanel = LayoutController.createPanel(); 
		
		Object[][] gbcData = {
			{0, 0, 1, 1, 1, 0, alignRight, fill, Title_SelectDay},
			{1, 0, 1, 1, 1, 0, alignLeft,  fill, JCB_SelectDay} 
		};
		LayoutController.add(gbcData[0]);
		LayoutController.add(gbcData[1]);
		LayoutController.add(0, 1, 2, 1, 1, 1, GanttChartPerDay.get(0));
	}
	
	/**
	 * ��Ѽƶi����ܤW���׹�
	 * 
	 * @param Src
	 * @return
	 */
	private String[] ToDaysList(Set<Integer> src) { 
		ArrayList<String> DaysList = new ArrayList<String>();
		
		for(Integer i: src) { 
			DaysList.add(String.format("�� %d ��\u3000\u3000\u3000\u3000\u3000", i+1));
		} 
		
		return DaysList.toArray(new String[DaysList.size()]);
	}	
		
	/**
	 * ��N�ɶ��̯S�� ���s
	 * 
	 * @param data
	 * @return
	 * @throws IOException 
	 */
	private ArrayList<JPanel> createORGanttChart(TreeMap<Integer, ArrayList<String[]>> data) throws IOException {    
		ArrayList<JPanel> charts = new ArrayList<>();  
		charts.add(new JPanel()); 
		for(Entry<Integer, ArrayList<String[]>> day: data.entrySet()) 
		{ 
			TreeMap<String, ArrayList<String[]>> result;
			result = CM.GroupByColumn(day.getValue(), Calendar.ROOM.column());
			ORGanttChart orgc = new ORGanttChart(day.getKey(), result); 
			charts.add(orgc.GetPanel());
		}   
		charts.set(0, LayoutController.createPanel(charts.get(1)));
		return charts;
	}
	
}
