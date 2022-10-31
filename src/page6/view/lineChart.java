package page6.view;

import org.jfree.chart.ChartPanel;

import java.awt.BasicStroke;
import java.awt.Color; 
import java.awt.Rectangle;
import java.awt.RenderingHints; 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D; 
import java.text.DecimalFormat;
import java.util.ArrayList;
 

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis; 
import org.jfree.chart.panel.CrosshairOverlay; 
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge; 
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import page6.controller.Main;

public class lineChart{
	 						
	private int[][] color_RGB ={/*深紅*/{249,65,68,  60}, 
								/*深黃*/{249,199,79, 60},
								/*深綠*/{67,170,139, 60},  
								/*深藍*/{39,125,161, 60},
								/*深橘*/{228,139,41, 60},
								/*淺綠*/{6,214,160,  60}, 
								/*淺紅*/{239,71,111, 60}, 
								/*淺藍*/{17,138,178, 60}};
	
	private ChartPanel chartPanel;   
	public  ChartPanel getChartPanel() {return chartPanel;}
	
	private int n = 6;
	private int x = 0;
	
	private boolean isFloat;
	public lineChart(String title, String Xaxis, String Yaxis, ArrayList<String[]> data, boolean isFloat) {
		ChartFactory.setChartTheme(Main.getChartTheme()); 
		this.isFloat = isFloat;
		
		n = data.get(0).length - 1; 
		
		if(n > 0) {
			XYDataset xyDataSet = DataTransfer(data); 
			
			JFreeChart lineChart = ChartFactory.createXYLineChart(title, Xaxis, Yaxis, xyDataSet,
											         PlotOrientation.VERTICAL,
											         true,true,false);
	       
			chartPanel = new ChartPanel(setChartTheme(lineChart));

			for(int i=0; i<n; i++)addCrosshair(i); 
		} 
	}

	public JFreeChart setChartTheme(JFreeChart chart) {  
	    chart.setTextAntiAlias(false);  
        chart.setBackgroundPaint(Color.white); 
        chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				  					  RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);  
        //取得繪製圖表區域的物件
        XYPlot plot = chart.getXYPlot(); 
        //設置橫向虛線可見 
        plot.setRangeGridlinesVisible(true); 
        //設置虛線色彩
        plot.setRangeGridlinePaint(Color.gray);   
        //設置色彩
        plot.setBackgroundPaint(new Color(255, 255, 255)); 
        plot.setForegroundAlpha(1.0f);  
        plot.setBackgroundAlpha(0.5f);  
        //設置數據軸浮點數顯示精度 
        NumberAxis vn = (NumberAxis) plot.getRangeAxis(); 
      
        
        DecimalFormat df;  
        if(isFloat)  df = new DecimalFormat("#0.00");
		else 		 df = new DecimalFormat("#0");  
        //設置數據軸標籤格式 
        vn.setNumberFormatOverride(df);  
    
        ValueAxis domainAxis = plot.getDomainAxis(); 
        
        //設置X軸標題字體
        domainAxis.setLabelFont(Main.FONT_TEXT); 
        //設置X軸的數值字體
        domainAxis.setTickLabelFont(Main.FONT_TEXT); 
        
        domainAxis.setAutoRange(false);
        domainAxis.setRange(0, x);
        
        domainAxis.setLowerMargin(0.1);  
        domainAxis.setUpperMargin(0.1);  
        
        plot.setDomainAxis(domainAxis);  
       
        ValueAxis rangeAxis = plot.getRangeAxis(); 
        rangeAxis.setLabelFont(Main.FONT_TEXT); 
        rangeAxis.setTickLabelFont(Main.FONT_TEXT); 
        //設置最高的一個 Item 於圖片頂端的距離 
        rangeAxis.setUpperMargin(0.15); 
        //設置最低的一個 Item 於圖片底端的距離
        rangeAxis.setLowerMargin(0.15); 
        plot.setRangeAxis(rangeAxis); 
   
        //解決中文亂碼的問題 
        TextTitle textTitle = chart.getTitle(); 
        textTitle.setFont(Main.FONT_TITLE); 
        domainAxis.setTickLabelFont(Main.FONT_TEXT); 
        domainAxis.setLabelFont(Main.FONT_TEXT); 
        vn.setTickLabelFont(Main.FONT_TEXT); 
        vn.setLabelFont(Main.FONT_TEXT); 
        
        BasicStroke bold = new BasicStroke(2.5F);
		XYItemRenderer face = chart.getXYPlot().getRenderer();
	
		if(n == 1) face.setSeriesVisibleInLegend(0, false);
		for(int i=0; i<n; i++) {
			face.setSeriesStroke(i, bold); 
			face.setSeriesOutlineStroke(i, bold); 
			face.setSeriesShape(i, new Rectangle(0,0,30,30), true);
			
			int[] iRGB = color_RGB[i % color_RGB.length];
			face.setSeriesPaint(i, new Color(iRGB[0],iRGB[1],iRGB[2])); 
		}  
		
	    return chart; 
	} 
	
	private void addCrosshair(int i) {    
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();

		Crosshair xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		xCrosshair.setLabelVisible(true);
		xCrosshair.setLabelFont(Main.FONT_TEXT);
		  
		Crosshair yCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		yCrosshair.setLabelVisible(true);
		yCrosshair.setLabelFont(Main.FONT_TEXT);
		
		if(i == 0)crosshairOverlay.addDomainCrosshair(xCrosshair);
		crosshairOverlay.addRangeCrosshair(yCrosshair); 
		
		chartPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// Do nothing
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// Do nothing
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// Do nothing
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// Do nothing
				xCrosshair.setVisible(true);
				yCrosshair.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Do nothing
				xCrosshair.setVisible(false);
				yCrosshair.setVisible(false);
			} 
		});
		
		chartPanel.addChartMouseListener(new ChartMouseListener() {
			
			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				//Do nothing
				
			}
			
			@Override
			public void chartMouseMoved(ChartMouseEvent event) { 
				Rectangle2D dataArea = chartPanel.getScreenDataArea();

				JFreeChart chart = event.getChart();

				XYPlot plot = (XYPlot) chart.getPlot();

				ValueAxis xAxis = plot.getDomainAxis();

				double xPoint = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
				xPoint = Math.round(xPoint);
				
				double y = DatasetUtils.findYValue(plot.getDataset(), i, xPoint);
				 
				xCrosshair.setValue(xPoint); 
				xCrosshair.setLabelVisible(false);
				
				yCrosshair.setValue(y);  
				yCrosshair.setLabelOutlineVisible(false);
				
				int[] iRGB = color_RGB[i % color_RGB.length]; 
				yCrosshair.setLabelBackgroundPaint(new Color(iRGB[0],iRGB[1],iRGB[2],iRGB[3]));
				yCrosshair.setPaint(new Color(iRGB[0],iRGB[1],iRGB[2]));
				yCrosshair.setStroke(new BasicStroke(1.5F));
				yCrosshair.setLabelPaint(Color.BLACK);
			
				if(i%2==1) yCrosshair.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
				else yCrosshair.setLabelAnchor(RectangleAnchor.TOP_LEFT);
			}

		});
		chartPanel.addOverlay(crosshairOverlay);  
	}
 
	private XYDataset DataTransfer(ArrayList<String[]> src) {
		XYSeriesCollection dataset = new XYSeriesCollection(); 
		String [] colName = src.get(0);
		 
		int row = src.size() - 1;  
		x = Integer.valueOf(src.get(row)[0]) + 1; 
    	  
		for(int c = 1; c < colName.length; c++) {
			XYSeries series = new XYSeries(colName[c]);
			
			for(int r = 1; r < src.size(); r++) { 
				String[] val = src.get(r);
				series.add(Double.valueOf(val[0]) , Double.valueOf(val[c])); 
        	}
			dataset.addSeries(series);
    	} 		
		 
		return dataset; 
	} 
} 

