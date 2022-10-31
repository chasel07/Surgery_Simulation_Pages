package page3.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import page3.controller.Main;
import page3.model.ColorManager;
import page3.model.Distribution;
import page3.model.JSSS;
 

public class Page3_Distribution_selection extends JPanel {

	private static final long serialVersionUID = 1L;

	private String[] distribution_select_mode = { "系統建議", "自行選擇" };
	private String[] distribution_select_colName = { "科別", "手術時間分佈" };

	private String TimeUnit = "分鐘";

	private final String[] DType = { "(--尚未選擇--)", "Exponential", "Erlang", "Triangular", "Uniform", "Normal" };

	private final int Exponential = 1;
	private final int Erlang = 2;
	private final int Triangular = 3;
	private final int Uniform = 4;

	private final int fill_N = GridBagConstraints.NONE;
	private final int fill_B = GridBagConstraints.BOTH;
	private final int fill_H = GridBagConstraints.HORIZONTAL;

	private final int alignL = GridBagConstraints.WEST;
	private final int alignC = GridBagConstraints.CENTER;
	private final int alignR = GridBagConstraints.EAST;

	// ---------------------------------------------------------------------------------------
	private int selectMode = 0;
	private int[] ischosenN = { 0, 0 };

	private JCheckBox[] chooseAll = new JCheckBox[ischosenN.length];

	private ArrayList<ArrayList<JCheckBox>> jcheckbs = new ArrayList<>();
	private ArrayList<ArrayList<JComboBox<String>>> jcb_SelectDistribution = new ArrayList<>();
	// ---------------------------------------------------------------------------------------
	private final JFrame jframe = Main.JF_SYSTEM;
	private Distribution data;
	// 科別名稱
	private String[] SubjectName;
	private ArrayList<String[]> ss, sd;
	// "名稱","最大值","最小值","眾數","平均值","標準差","次數","分布"

	private JComboBox<String> jcb_select;
	private JPanel[] originalDPanel;
	private JPanel[] simulationDPanel;

	// 宣告網格約束變數
	private GridBagConstraints gbc = new GridBagConstraints();
	private JPanel gbc_panel;

	public Page3_Distribution_selection(Distribution data) {
		this.data = data;
		// 載入手術統計資料、手術分布(包含標題)
		this.ss = data.getSurgeryStatictis();
		this.sd = data.getSurgeryDistribution();
		this.SubjectName = data.getSubjectName();
		this.setLayout(new BorderLayout());
		this.add(Choose_Distribution(), BorderLayout.EAST);
		this.add(plotDistribution(), BorderLayout.CENTER);
	}

	// 左手邊兩個面板
	public JPanel plotDistribution() {
		// 原始資料的分布圖
		originalDPanel = new JPanel[ss.size() - 1];
		// 模擬資料的分布圖
		simulationDPanel = new JPanel[ss.size() - 1];
		// 建立各科資料分布圖
		for (int row = 1; row < ss.size(); row++) {

			String[] tempSS = ss.get(row);
			double max = Double.parseDouble(tempSS[Main.COL_MAX]);
			double avg = Double.parseDouble(tempSS[Main.COL_AVG]);
			double std = Double.parseDouble(tempSS[Main.COL_STD]);

			String[] tempSD = sd.get(row);
			int group = tempSD.length - 1;
			int[] count = new int[group];
			for (int col = 1; col < count.length; col++) {
				count[col - 1] = (int) Double.parseDouble(tempSD[col]);
			}

			String DomainName = String.format("間距量： %.2f分鐘", max / group);
			ChartPanel cp = getDistributionChart("", DomainName, count, 0);
			String mode;
			if (tempSS[Main.COL_MODE].equals("0")) {
				mode = "(眾數不存在)";
			} else {
				mode = tempSS[Main.COL_MODE] + "分鐘";
			}

			originalDPanel[row - 1] = new JPanel(new GridBagLayout());
			this.gbc_panel = originalDPanel[row - 1];

			
			Object[] gbcData1 = { 0, 0, 2, 1, 1, 1.2, this.alignC, this.fill_B, cp }; 
			setGBC(gbcData1);
			
			gbc.insets = new Insets(20, 0, 0, 0); 
			Object[][] gbcData2 = { 
				{0, 3, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("最長手術時間：")},
				{1, 3, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(tempSS[Main.COL_MAX], TimeUnit)} 
			};  
			for(Object[] dataRow: gbcData2) setGBC(dataRow);  
				  
			gbc.insets = new Insets(0, 0, 0, 0);
			Object[][] gbcData3 = { 
				{0,  4, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("最短手術時間：")},
				{1,  4, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(tempSS[Main.COL_MIN], TimeUnit)},
				{0,  6, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("平均手術時間：")},
				{1,  6, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(String.format("%.2f%s", avg, TimeUnit))},
				{0,  8, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("手術時間標準差：")},
				{1,  8, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(String.format("%.2f", std))},
				{0, 10, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("手術時間眾數：")},
				{1, 10, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(mode)},
				{0, 12, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("手術執行次數：")},
				{1, 12, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(tempSS[Main.COL_COUNT], "次")} 
			};
			for(Object[] dataRow: gbcData3) setGBC(dataRow);  
			  
			// 對指定科別模擬各種不同的分布
			createSimulDistribution(row);

			// 對指定科別推薦最佳分布
			int recommend = recommend_distribution(row);

			String temp_DType = DType[recommend + 1];
			DType[recommend + 1] = DType[recommend + 1].concat(" (建議)");

			for (int i = 0; i < distribution_select_mode.length; i++) {
				addItemstoComboBoxN(i, row - 1, DType, recommend + 1);
			}
			// 對指定科別繪製各種不同的分布圖
			createSimulationBarChart(row);

			DType[recommend + 1] = temp_DType;
		}
		// 原始資料分布圖
		JPanel ChartDisplay1 = new JPanel(new BorderLayout());
		ChartDisplay1.add(originalDPanel[0]);

		// 模擬資料分布圖
		JPanel ChartDisplay2 = new JPanel(new BorderLayout());
		ChartDisplay2.add(simulationDPanel[0]);
		ChartDisplay2.setBorder(this.getTitleBorder("模擬資料分布"));
		ChartDisplay2.setBackground(ColorManager.COLOR_WHITE);

		// 加入科別名稱
		jcb_select = new JComboBox<String>(Main.renameSubject(SubjectName));
		jcb_select.setFont(Main.FONT_TEXT);
		jcb_select.addItemListener(e -> {
			int index = jcb_select.getSelectedIndex();
			ChartDisplay1.removeAll();
			ChartDisplay1.add(originalDPanel[index]);
			ChartDisplay1.repaint();

			ChartDisplay2.removeAll();
			ChartDisplay2.add(simulationDPanel[index]);
			ChartDisplay2.repaint();

			jframe.setVisible(true);
		});

		JPanel ChartDisplay = new JPanel(new GridBagLayout());
		gbc_panel = ChartDisplay;
		gbc_panel.setBorder(this.getTitleBorder("原始資料分布")); 
		Object[][] gbcDataTitle = { 
			{0, 0, 1, 1, 0, 0, alignR, fill_N, getLabel("科別：")},
			{1, 0, 3, 1, 1, 0, alignL, fill_H, jcb_select},
			{0, 1, 4, 1, 1, 1, alignC, fill_B, ChartDisplay1}, 
		};  
		for(Object[] dataRow: gbcDataTitle) setGBC(dataRow);  
		spacer(0, 0.2);

		gbc_panel = new JPanel(new GridBagLayout()); 
		Object[][] gbcData = { 
			{0, 0, 1, 1,   1, 1, this.alignC, this.fill_B, ChartDisplay},
			{1, 0, 1, 1, 3.5, 1, this.alignC, this.fill_B, ChartDisplay2} 
		}; 
		for(Object[] dataRow: gbcData) setGBC(dataRow); 
		  
		return gbc_panel;
	}

	private JPanel Choose_Distribution() {
		JTabbedPane TabbedPanel = new JTabbedPane();
		TabbedPanel.setFont(Main.FONT_TEXT_BOLD);
		TabbedPanel.addChangeListener(e -> {
			if (e.getSource() == TabbedPanel) {
				selectMode = TabbedPanel.getSelectedIndex();
			}
		});

		for (int i = 0; i < distribution_select_mode.length; i++) {
			TabbedPanel.addTab(distribution_select_mode[i], createSelectPanel(i));
		}
		// Choose_Distribution_Panel
		gbc_panel = new JPanel(new GridBagLayout());
		gbc_panel.setBorder(getTitleBorder("目前選擇的分布"));
		gbc_panel.setPreferredSize(new Dimension(300, gbc_panel.getHeight()));
		
		Object[] gbcData = { 0, 0, 1, 1, 1, 1, this.alignC, this.fill_B, TabbedPanel };
		setGBC(gbcData);
		
		gbc.insets = new Insets(0, 0, 0, 0);
		return gbc_panel;
	}

	private JCheckBox[] createCheckBox(int select_mode) {
		// 被選擇的數量
		ischosenN[select_mode] = 0;
		jcheckbs.add(new ArrayList<>());

		JCheckBox[] chooseSome = new JCheckBox[SubjectName.length];

		ActionListener al_chooseSome = e -> {
			ischosenN[select_mode] = 0;
			for (int i = 0; i < chooseSome.length; i++) {
				if (chooseSome[i].isSelected()) {
					ischosenN[select_mode]++;
				}
			}
			chooseAll[select_mode].setSelected(ischosenN[select_mode] == chooseSome.length);
		};

		for (int i = 0; i < chooseSome.length; i++) {
			chooseSome[i] = new JCheckBox();
			chooseSome[i].setFont(Main.FONT_TEXT);
			chooseSome[i].addActionListener(al_chooseSome);
			jcheckbs.get(select_mode).add(chooseSome[i]);
		}

		chooseAll[select_mode] = new JCheckBox();
		chooseAll[select_mode].setBackground(ColorManager.COLOR_WHITE);
		chooseAll[select_mode].addActionListener(e -> {
			for (JCheckBox i : chooseSome) {
				i.setSelected(chooseAll[select_mode].isSelected());
			}
			ischosenN[select_mode] = chooseAll[select_mode].isSelected() ? SubjectName.length : 0;
		});

		return chooseSome;
	}

	private JButton createScriptButton(int select_mode) {
		JButton button = new JButton("建立劇本");
		button.setFont(Main.FONT_TEXT);
		button.addActionListener(e -> { 
			if (ischosenN[select_mode] == 0) {
				JOptionPane.showMessageDialog(jframe, "請至少選擇一個科別！", "建立劇本要求", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				Distribution.saveAsFile(data, data.getSrcFileName());
				Main.log(Level.INFO, "分布已儲存至 " + data.getSrcFileName());
			} catch (IOException E) {
				String msg = String.format("無法存取 %s ，因為檔案正由另一個程序使用。", data.getSrcFileName());
				JOptionPane.showMessageDialog(jframe, msg, "存取錯誤", JOptionPane.ERROR_MESSAGE);
				Main.log(Level.WARNING, E.getMessage());
			}

			ArrayList<String[]> info = data.getSurgeryStatictis();
			ArrayList<String[]> info_choose = new ArrayList<>();
			info_choose.add(info.get(0));
			boolean isComplete = true;

			ArrayList<JCheckBox> jcheckbs_temp = jcheckbs.get(select_mode);

			for (int i = 0; i < jcheckbs_temp.size(); i++) {
				if (!jcheckbs_temp.get(i).isSelected())
					continue;

				JComboBox<String> jcb = jcb_SelectDistribution.get(select_mode).get(i);
				String item = (String) jcb.getSelectedItem();

				if (item.equals(DType[0])) {
					isComplete = false;
					jcb.setBorder(new LineBorder(Color.red));
				}

				info_choose.add(info.get(i + 1));
			}

			if (!isComplete) {
				JOptionPane.showMessageDialog(jframe, "尚有已選擇的科別還未選擇其分布類型！", "建立劇本要求", JOptionPane.ERROR_MESSAGE);
				return;
			}

			data.setSurgeryStatictis(info_choose);
			Main.switchLights(3);
			JOptionPane.showMessageDialog(this.jframe, "劇本產生頁面");
			
			
			Main.switchLights(2);
			Main.switchPage(this);
		});

		return button;
	}

	/**
	 * 包含勾選方塊、項目名稱、組合方塊、建立分布等按鈕
	 * 
	 * @param select_mode = 0 為 "系統建議" <br>
	 *                    select_mode = 1 為 "自行選擇"
	 * 
	 * @return 回傳每一個建立完成的分布選擇面板
	 */
	private JPanel createSelectPanel(int select_mode) {
		// 選核方塊
		JCheckBox[] chooseSome = createCheckBox(select_mode);
		// 科別標籤建立
		JLabel[] jl_subjectN = new JLabel[SubjectName.length];
		for (int i = 0; i < jl_subjectN.length; i++) {
			jl_subjectN[i] = getLabel_B(SubjectName[i]);
		}
		// 建立科別分布下拉式選單
		jcb_SelectDistribution.add(createComboBoxes(SubjectName.length, select_mode > 0));

		// 手術時間分布選擇面板
		gbc_panel = new JPanel(new GridBagLayout());
		gbc.insets = new Insets(5, 0, 0, 0); 
		Object[][] gbcDataTitle = {
			// 標題列
			{0, 0, 1, 1, 1, 0, this.alignL, this.fill_N, chooseAll[select_mode]},
			{1, 0, 1, 1, 1, 0, this.alignC, this.fill_B, getLabel_B(distribution_select_colName[0])},
			{2, 0, 1, 1, 1, 0, this.alignC, this.fill_N, getLabel_B(distribution_select_colName[1])},
			// 分隔線
			{0, 1, 3, 1, 1, 0, this.alignC, this.fill_H, new JSeparator()}
		}; 
		for(Object[] dataRow: gbcDataTitle) setGBC(dataRow); 
		for (int i = 0; i < SubjectName.length; i++) {
			chooseSome[i].setBackground(ColorManager.COLOR_WHITE);
			Object[][] gbcDataContent = {
				{0, i + 2, 1, 1, 1, 1, this.alignC, this.fill_B, chooseSome[i]},
				{1, i + 2, 1, 1, 1, 1, this.alignC, this.fill_B, jl_subjectN[i]}, 
				{2, i + 2, 1, 1, 1, 1, this.alignL, this.fill_H, jcb_SelectDistribution.get(select_mode).get(i)}	 
			}; 
			for(Object[] dataRow: gbcDataContent) setGBC(dataRow);
		} 
		JScrollPane SystemSelected = new JScrollPane(gbc_panel);
		SystemSelected.setBorder(new EmptyBorder(0, 0, 0, 0));

		gbc_panel = new JPanel(new GridBagLayout());
		Object[][] gbcDataBottom = {
			{0, 2, 3, 1, 1, 1, this.alignC, this.fill_B, SystemSelected},
			{2, 3, 1, 1, 0, 0, this.alignR, this.fill_N, createScriptButton(select_mode)}
		};   
		for(Object[] dataRow: gbcDataBottom) setGBC(dataRow);
		  
		return gbc_panel;
	}

	private TitledBorder getTitleBorder(String title) {
		TitledBorder i = new TitledBorder(title);
		i.setTitleFont(Main.FONT_TITLE_BOLD);
		i.setTitleColor(ColorManager.COLOR_TITLE);
		i.setBorder(Main.LINEBORDER);
		return i;
	}

	/**
	 * 建立n個組合方塊
	 * 
	 * @param n 指定建立數量
	 * @return 回傳一套組合方塊
	 */
	private ArrayList<JComboBox<String>> createComboBoxes(int n, boolean enable) {

		ArrayList<JComboBox<String>> a = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			JComboBox<String> temp = new JComboBox<String>();
			temp.setFont(Main.FONT_TEXT);
			temp.setEnabled(enable);
			a.add(temp);
		}
		return a;
	}

	/**
	 * @param a     某套組合方塊
	 * @param n     指定陣列中的第幾個
	 * @param items 欲加入組合方塊的項目
	 */
	private void addItemstoComboBoxN(int m, int n, String[] items, int selectedIndex) {
		JComboBox<String> jcb = jcb_SelectDistribution.get(m).get(n);
		for (int i = 0; i < items.length; i++) {
			jcb.addItem(items[i]);
		}
		jcb.setSelectedItem(m == 0 ? items[selectedIndex] : ss.get(n + 1)[Main.COL_DTYPE]);
		jcb.addItemListener(e -> {
			int SelectedIndex = jcb.getSelectedIndex();
			// 手術時間統計資料
			ss.get(n + 1)[Main.COL_DTYPE] = DType[SelectedIndex];
			jcb.setBorder(SelectedIndex > 0 ? new EmptyBorder(0, 0, 0, 0) : new LineBorder(Color.red));
		});
	}

	// 快速設定Label
	private JLabel getLabel(String... text) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < text.length; i++) {
			str.append(text[i]);
		}
		JLabel jl = new JLabel(str.toString());
		jl.setFont(Main.FONT_TEXT);
		return jl;
	}

	private JLabel getLabel_B(String text) {
		JLabel jl = new JLabel(text);
		jl.setFont(Main.FONT_TEXT_BOLD);
		return jl;
	}

	ArrayList<ArrayList<Double>> SimulDistribution = new ArrayList<>();
	ArrayList<ChartPanel> SimulationChart = new ArrayList<>();

	/**
	 * 對指定科別模擬各種分布表
	 * 
	 * @param Subject 科別選擇
	 */
	private void createSimulDistribution(int Subject) {

		String[] SubjectInformation = ss.get(Subject);
		SimulDistribution.clear();

		int n = Integer.valueOf(SubjectInformation[Main.COL_COUNT]);
		double mean = Double.valueOf(SubjectInformation[Main.COL_AVG]);
		double std = Double.valueOf(SubjectInformation[Main.COL_STD]);
		double max = Double.valueOf(SubjectInformation[Main.COL_MAX]);
		double min = Double.valueOf(SubjectInformation[Main.COL_MIN]);
		double mode = Double.valueOf(SubjectInformation[Main.COL_MODE]);
		double outlier = Double.valueOf(SubjectInformation[Main.COL_OUTLIERS]);

		for (int d = 1; d < DType.length; d++) { 
			if (d == Triangular && ((max < mode) || (mode < min) || (min > max))) { 
				continue;
		    }
			
			ArrayList<Double> values = new ArrayList<>();
			JSSS createD = new JSSS(); 
			
			for (int i = 0; i < n; i++) {
				double v;
				do {
					switch (d) {
						case Exponential:
							v = createD.EX(mean);
							break;
						case Erlang:
							v = createD.ER(mean, 2);
							break;
						case Triangular:
							v = createD.TR(min, mode, max);
							break;
						case Uniform:
							v = createD.UN(min, max);
							break;
						default:
							v = createD.RN(mean, std);
							break;
					} 
				}
				while(v < 0 || v > outlier);
				values.add(v);
			}
			values.sort(null);
			SimulDistribution.add(values);
		}
	}

	private int recommend_distribution(int Subject) {
		String[] dataRow = sd.get(Subject);
		int[] A = new int[dataRow.length];
		for (int j = 1; j < dataRow.length; j++) {
			A[j] = Integer.valueOf(dataRow[j]);
		}

		int group = dataRow.length - 1;
		// 間距量
		String[] values = data.getSurgeryStatictis().get(Subject);
		double space_max = Double.parseDouble(values[Main.COL_MAX]) / group;

		int best_distribution = -1;
		double best_value = -1;

		for (int i = 0; i < SimulDistribution.size(); i++) {
			double AB = 0;
			double A_vectorLen = 0;
			double B_vectorLen = 0;

			int[] B = Distribution.getDistribution(SimulDistribution.get(i), group, space_max);

			for (int j = 0; j < B.length; j++) {
				AB += A[j + 1] * B[j];
				A_vectorLen += A[j + 1] * A[j + 1];
				B_vectorLen += B[j] * B[j];
			}

			double curValue = AB / Math.sqrt(A_vectorLen * B_vectorLen);
			if (best_value < curValue) {
				best_distribution = i;
				best_value = curValue;
			}
		}
		return best_distribution;
	}

	private void createSimulationBarChart(int Subject) {
		// 組數
		int group = sd.get(Subject).length - 1;
		// 間距量
		String[] values = data.getSurgeryStatictis().get(Subject);
		double space_max = Double.parseDouble(values[Main.COL_MAX]) / group;
		// 分布圖表
		JPanel jp = new JPanel(new GridLayout(3, 2, 5, 5));
		for (int d = 0; d < SimulDistribution.size(); d++) {
			String DomainName = String.format("間距量： %.2f分鐘", space_max);
			int[] count = Distribution.getDistribution(SimulDistribution.get(d), group, space_max);
			jp.add(getDistributionChart(DType[d + 1], DomainName, count, Subject));
		}
		simulationDPanel[Subject - 1] = jp;
	}

	// 繪製分布圖表的函式
	private ChartPanel getDistributionChart(String title, String DomainName, int[] count, int row) {
		ChartFactory.setChartTheme(Main.getChartTheme());

		XYSeries Domain = new XYSeries(DomainName);
		for (int i = 0; i < count.length; i++) {
			Domain.add(i + 1, count[i]);
		}

		XYSeriesCollection intervalXYDataSet = new XYSeriesCollection();
		intervalXYDataSet.addSeries(Domain);

		JFreeChart chart = ChartFactory.createXYBarChart(title, "", false, "次數", intervalXYDataSet,
				PlotOrientation.VERTICAL, true, true, false);
		ChartPanel cp = new ChartPanel(chart);
		setXYBarChart_Blue(chart, count);

		cp.addChartMouseListener(new ChartMouseListener() {

			@Override
			public void chartMouseClicked(ChartMouseEvent arg0) {
				if (row > 0 && selectMode != 0) {
					JComboBox<String> jcb = jcb_SelectDistribution.get(selectMode).get(row - 1);
					jcb.setSelectedItem(title);

					JCheckBox jckb = jcheckbs.get(selectMode).get(row - 1);
					if (!jckb.isSelected()) {
						jckb.setSelected(true);
						ischosenN[selectMode]++;
					}

					if (ischosenN[selectMode] == jcheckbs.get(selectMode).size()) {
						chooseAll[selectMode].setSelected(true);
					}
				}
			}

			@Override
			public void chartMouseMoved(ChartMouseEvent arg0) {
				// Do nothing
			}
		});
		return cp;
	}

	public static JFreeChart setXYBarChart_Blue(JFreeChart chart, int[] count) {
		chart.setTextAntiAlias(false);
		chart.setBackgroundPaint(Color.white);
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// 取得繪製圖表區域的物件
		XYPlot plot = chart.getXYPlot();
		// 設置橫向虛線可見
		plot.setRangeGridlinesVisible(true);
		// 設置虛線色彩
		plot.setRangeGridlinePaint(Color.gray);
		// 設置色彩
		plot.setBackgroundPaint(new Color(255, 255, 255));
		plot.setForegroundAlpha(1.0f);
		plot.setBackgroundAlpha(0.5f);

		// 設置數據軸浮點數顯示精度
		DecimalFormat df = new DecimalFormat("#0");
		// 設置數據軸標籤格式
		NumberAxis vn = (NumberAxis) plot.getRangeAxis();
		vn.setNumberFormatOverride(df);
		int row = 0;
		for (int i : count)
			if (row < i)
				row = i;
		if (row < 4)
			vn.setTickUnit(new NumberTickUnit(1));

		NumberAxis hn = (NumberAxis) plot.getDomainAxis();
		int col = count.length + 1;
		if (col < 8)
			hn.setTickUnit(new NumberTickUnit(1));

		ValueAxis domainAxis = plot.getDomainAxis();
		// 設置X軸標題字體
		domainAxis.setLabelFont(Main.FONT_TEXT);
		// 設置X軸的數值字體
		domainAxis.setTickLabelFont(Main.FONT_TEXT);
		domainAxis.setAutoRange(false);

		Range r = new Range(0, col);
		domainAxis.setDefaultAutoRange(r);
		domainAxis.setRange(r);
		domainAxis.setLowerMargin(0.1);
		domainAxis.setUpperMargin(0.1);
		plot.setDomainAxis(domainAxis);

		ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setLabelFont(Main.FONT_TEXT);
		rangeAxis.setTickLabelFont(Main.FONT_TEXT);
		// 設置最高的一個 Item 於圖片頂端的距離
		rangeAxis.setUpperMargin(0.15);
		// 設置最低的一個 Item 於圖片底端的距離
		rangeAxis.setLowerMargin(0.15);
		plot.setRangeAxis(rangeAxis);

		// 解決中文亂碼的問題
		TextTitle textTitle = chart.getTitle();
		textTitle.setFont(Main.FONT_TITLE);
		domainAxis.setTickLabelFont(Main.FONT_TEXT);
		domainAxis.setLabelFont(Main.FONT_TEXT);
		vn.setTickLabelFont(Main.FONT_TEXT);
		vn.setLabelFont(Main.FONT_TEXT);

		XYItemRenderer renderer = new XYBarRenderer();
		renderer.setSeriesPaint(0, new Color(46, 143, 250));
		plot.setRenderer(renderer);
		return chart;
	}

	private void setGBC(Object[] setting) {
		gbc.gridx = Integer.valueOf(setting[0].toString());
		gbc.gridy = Integer.valueOf(setting[1].toString());
		gbc.gridwidth = Integer.valueOf(setting[2].toString());
		gbc.gridheight = Integer.valueOf(setting[3].toString());
		gbc.weightx = Double.valueOf(setting[4].toString());
		gbc.weighty = Double.valueOf(setting[5].toString());
		gbc.anchor = Integer.valueOf(setting[6].toString());
		gbc.fill = Integer.valueOf(setting[7].toString());
		gbc_panel.add((Component) setting[8], gbc);
		gbc_panel.setBackground(ColorManager.COLOR_WHITE);
	}

	private void spacer(double wx, double wy) {
		gbc.gridx = 0;
		gbc.gridy = gbc.gridy + 1;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.WEST;
		gbc_panel.add(new JLabel("  "), gbc);
		gbc_panel.setBackground(ColorManager.COLOR_WHITE);
	}
}
