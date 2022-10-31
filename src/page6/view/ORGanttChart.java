package page6.view;

import java.awt.Color; 
import java.awt.Font;
import java.awt.Paint;
import java.awt.RenderingHints; 
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList; 
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.Range;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.SimpleTimePeriod; 
  
import page6.controller.Main;
import page6.model.Calendar;
import page6.model.CalendarManager;
import page6.model.ColorManager;
import page6.model.FileRoute;

public class ORGanttChart extends JFrame {
	//
	private static final long serialVersionUID = 1L;
	
	//
	private File File_ColorList = new File(FileRoute.PATH_DATA + "/ColorCodeTable.csv"); 
	
	// 從 "1971/01/01 00:00:00.000" (yyyy/MM/dd HH:mm:ss.fff) 取得 long 的對應值
	private long Begin = 31507200000L;
	
	// 作為起始時間 並將時間範圍設為 1 天 
	private Range TimeDisplayRange = new Range(31507200000.0, 31593600000.0);

	// 手術房顯示範圍
	private Range RoomDisplayRange = new Range(-0.5, 9.5);
	
	//
	private double MouseWheel_Sensitive = 0.2;
	
	//
	private double RoomDisplayRangeSize = 10;
	
	// 處理單位名稱
	private String SymbolAxisTitle = "";
	
	// 時間軸標籤 
	private String DateAxisTitle = "";
	
	//
	private TaskSeriesCollection ds;
	
	//
	private String[] SymbolAxisLabel;
	
	//
	private ArrayList<String[]> subject;
	
	//
	private ChartPanel chartPanel;
	
	//
	private JPanel LegendPanel;
	
	//
	private ORGanttRenderer GanttRenderer;
	
	//
	private Font font_text = Main.FONT_TEXT;
	
	//
	private Font font_title = Main.FONT_TITLE;
	
	/**
	 * 
	 * @param Day
	 * @param Data
	 * @throws IOException
	 */
	public ORGanttChart(int Day, TreeMap<String, ArrayList<String[]>> Data) throws IOException {
		SetDataSource(Day, Data);
		CreateRenderer(Data);
		CreateChart();
		CreateLegendItems();
		EnabledScrolling();
	}
	  
	public JPanel GetPanel() { 
		LayoutController.createPanel();
		LayoutController.add(0,0,1,1,1,1,chartPanel);
		LayoutController.add(0,1,1,1,1,0,LegendPanel); 
		return LayoutController.getPanel();
	}
	
	/**
	 * 
	 * @param Day
	 * @param Data
	 */ 
	public void SetDataSource(int Day, TreeMap<String, ArrayList<String[]>> Data) {
		//建立一個想要顯示在甘特圖上的水平軸標籤陣列
		Set<String> Rooms = Data.keySet();
		SymbolAxisLabel = Rooms.toArray(new String[Rooms.size()]);
		
		//轉換 Data 成 JFreeChart 可以讀取的格式
		ds = new TaskSeriesCollection(); 
		for (Entry<String, ArrayList<String[]>> room : Data.entrySet()) {
			TaskSeries schedule = new TaskSeries(room.getKey());
 
			for (String[] surgeryRecord : room.getValue()) {  
				long Day_Millis = 86400000L;
				 
				long start = (long)(Double.parseDouble(surgeryRecord[Calendar.ENTER.column()]) * 60000.0);
				start = ((start/Day_Millis) == Day) ? start % Day_Millis : 0;
					 
				long end = (long)(Double.parseDouble(surgeryRecord[Calendar.EXIT.column()]) * 60000.0);
				end = ((end/Day_Millis) == Day) ? end % Day_Millis : Day_Millis;
				 
				SimpleTimePeriod stp = new SimpleTimePeriod(Begin + start, Begin + end);
				schedule.add(new Task(surgeryRecord[Calendar.ID.column()], stp));
			}
			ds.add(schedule);
		}  
	}
	
	public void SetDataSource(TaskSeriesCollection ds) { this.ds = ds; }

	private void SetPlotAppearance(XYPlot plot) {
		SymbolAxis xAxis = new SymbolAxis(SymbolAxisTitle, SymbolAxisLabel);
		xAxis.setGridBandsVisible(false);
	
		plot.setDomainAxis(xAxis);
		plot.setDomainAxisLocation(AxisLocation.TOP_OR_LEFT);
		
		plot.setRangePannable(true);
		plot.setRangeAxis(new DateAxis(DateAxisTitle));
		// 設置橫向虛線可見
		plot.setRangeGridlinesVisible(true);
		// 設置虛線色彩
		plot.setRangeGridlinePaint(Color.gray);
		// 設置色彩
		plot.setBackgroundPaint(Color.white);
		plot.setForegroundAlpha(1.0f);
		plot.setBackgroundAlpha(0.5f);
		
		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setRenderer(GanttRenderer);
		
		ValueAxis domainAxis = plot.getDomainAxis();
		domainAxis.setTickLabelFont(font_text);
		domainAxis.setLabelFont(font_text);
		domainAxis.setDefaultAutoRange(RoomDisplayRange);
		domainAxis.setRange(RoomDisplayRange);
		
		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(font_text);
		rangeAxis.setTickLabelFont(font_text);
		rangeAxis.setAutoRange(false);
		rangeAxis.setDefaultAutoRange(TimeDisplayRange);
		rangeAxis.setRange(TimeDisplayRange);
		rangeAxis.setInverted(true);
	}
	
	private void CreateChart() { 
		JFreeChart chart = ChartFactory.createXYBarChart(
				getTitle(), 
				SymbolAxisTitle, 
				false,
				DateAxisTitle, 
				new XYTaskDataset(ds),
				PlotOrientation.HORIZONTAL, 
				true, 
				false, 
				false
		);
		chart.setBackgroundPaint(Color.white);
		chart.setTextAntiAlias(false);
		chart.setBackgroundPaint(Color.white);
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		chart.getTitle().setFont(font_title); 
		
		SetPlotAppearance((XYPlot)chart.getPlot());

		chartPanel = new ChartPanel(chart);   
	}
  
	private void CreateRenderer(TreeMap<String, ArrayList<String[]>> data) {
		CalendarManager CM = new CalendarManager();
		subject = CM.SelectColumnFrom(data, Calendar.SUBJECT.column()); 
		GanttRenderer = new ORGanttRenderer();
	}
	
	public class ORGanttRenderer extends XYBarRenderer implements Serializable {  
		
		private static final long serialVersionUID = 1L;
    
		private ColorManager CM;
		 
		public ORGanttRenderer() { 
			try {
				CM = new ColorManager(File_ColorList);
			} 
			catch (IOException e) { 
				Main.log(Level.WARNING, e.getMessage());
			}  
			 
			setDefaultItemLabelGenerator((arg0, series, item) -> {
				return ds.getSeries(series).get(item).getDescription();
			});
			
			setItemLabelAnchorOffset(-10);
			setDefaultItemLabelFont(font_text);
			setDefaultItemLabelPaint(Color.BLACK);
			setDefaultLegendTextFont(font_text);
			setDefaultItemLabelsVisible(true);
			setDefaultSeriesVisibleInLegend(false);
			setUseYInterval(true);  
		}

		@Override
		public Paint getItemPaint(int row, int column) {
			String subjectName = subject.get(row)[column]; 
			return CM.getColor(subjectName)[0]; 
		}  
	}
	 
	private void EnabledScrolling() {   
		ValueAxis DomainAxis = chartPanel.getChart().getXYPlot().getDomainAxis();
		
		double LowerBound = -1;
		double UpperBound = SymbolAxisLabel.length;
		
		chartPanel.addMouseWheelListener(e -> { 
			double Direction = e.getWheelRotation() * MouseWheel_Sensitive; 
			double Min = DomainAxis.getRange().getLowerBound() + Direction;
			double Max = Min + RoomDisplayRangeSize;
			if(Min < LowerBound || Max > UpperBound) return;
			DomainAxis.setRange(Min, Max);  
		}); 
	} 
	
	private void CreateLegendItems() throws IOException { 
		LegendPanel = LayoutController.createPanel();
		
		CalendarManager CLDM = new CalendarManager();
		TreeSet<String> SubjectSet = CLDM.CreateSetFrom(subject);
		
		ColorManager CM = new ColorManager(File_ColorList);
		int i = 0;
		int j = 0;
		for(String s: SubjectSet) {
			JLabel label = LabelGenerator.Create_Legend(s, CM.getColor(s)[0]);
			LayoutController.add(i++, j, label);
			if(i == 10) { i=0; j++; }
		}  
	}
	 
}