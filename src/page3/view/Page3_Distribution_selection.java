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

	private String[] distribution_select_mode = { "�t�Ϋ�ĳ", "�ۦ���" };
	private String[] distribution_select_colName = { "��O", "��N�ɶ����G" };

	private String TimeUnit = "����";

	private final String[] DType = { "(--�|�����--)", "Exponential", "Erlang", "Triangular", "Uniform", "Normal" };

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
	// ��O�W��
	private String[] SubjectName;
	private ArrayList<String[]> ss, sd;
	// "�W��","�̤j��","�̤p��","����","������","�зǮt","����","����"

	private JComboBox<String> jcb_select;
	private JPanel[] originalDPanel;
	private JPanel[] simulationDPanel;

	// �ŧi��������ܼ�
	private GridBagConstraints gbc = new GridBagConstraints();
	private JPanel gbc_panel;

	public Page3_Distribution_selection(Distribution data) {
		this.data = data;
		// ���J��N�έp��ơB��N����(�]�t���D)
		this.ss = data.getSurgeryStatictis();
		this.sd = data.getSurgeryDistribution();
		this.SubjectName = data.getSubjectName();
		this.setLayout(new BorderLayout());
		this.add(Choose_Distribution(), BorderLayout.EAST);
		this.add(plotDistribution(), BorderLayout.CENTER);
	}

	// �������ӭ��O
	public JPanel plotDistribution() {
		// ��l��ƪ�������
		originalDPanel = new JPanel[ss.size() - 1];
		// ������ƪ�������
		simulationDPanel = new JPanel[ss.size() - 1];
		// �إߦU���Ƥ�����
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

			String DomainName = String.format("���Z�q�G %.2f����", max / group);
			ChartPanel cp = getDistributionChart("", DomainName, count, 0);
			String mode;
			if (tempSS[Main.COL_MODE].equals("0")) {
				mode = "(���Ƥ��s�b)";
			} else {
				mode = tempSS[Main.COL_MODE] + "����";
			}

			originalDPanel[row - 1] = new JPanel(new GridBagLayout());
			this.gbc_panel = originalDPanel[row - 1];

			
			Object[] gbcData1 = { 0, 0, 2, 1, 1, 1.2, this.alignC, this.fill_B, cp }; 
			setGBC(gbcData1);
			
			gbc.insets = new Insets(20, 0, 0, 0); 
			Object[][] gbcData2 = { 
				{0, 3, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("�̪���N�ɶ��G")},
				{1, 3, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(tempSS[Main.COL_MAX], TimeUnit)} 
			};  
			for(Object[] dataRow: gbcData2) setGBC(dataRow);  
				  
			gbc.insets = new Insets(0, 0, 0, 0);
			Object[][] gbcData3 = { 
				{0,  4, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("�̵u��N�ɶ��G")},
				{1,  4, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(tempSS[Main.COL_MIN], TimeUnit)},
				{0,  6, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("������N�ɶ��G")},
				{1,  6, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(String.format("%.2f%s", avg, TimeUnit))},
				{0,  8, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("��N�ɶ��зǮt�G")},
				{1,  8, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(String.format("%.2f", std))},
				{0, 10, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("��N�ɶ����ơG")},
				{1, 10, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(mode)},
				{0, 12, 1, 1, 0.2, 0.2, this.alignR, this.fill_N, getLabel("��N���榸�ơG")},
				{1, 12, 1, 1, 0.2, 0.2, this.alignL, this.fill_N, getLabel(tempSS[Main.COL_COUNT], "��")} 
			};
			for(Object[] dataRow: gbcData3) setGBC(dataRow);  
			  
			// ����w��O�����U�ؤ��P������
			createSimulDistribution(row);

			// ����w��O���˳̨Τ���
			int recommend = recommend_distribution(row);

			String temp_DType = DType[recommend + 1];
			DType[recommend + 1] = DType[recommend + 1].concat(" (��ĳ)");

			for (int i = 0; i < distribution_select_mode.length; i++) {
				addItemstoComboBoxN(i, row - 1, DType, recommend + 1);
			}
			// ����w��Oø�s�U�ؤ��P��������
			createSimulationBarChart(row);

			DType[recommend + 1] = temp_DType;
		}
		// ��l��Ƥ�����
		JPanel ChartDisplay1 = new JPanel(new BorderLayout());
		ChartDisplay1.add(originalDPanel[0]);

		// ������Ƥ�����
		JPanel ChartDisplay2 = new JPanel(new BorderLayout());
		ChartDisplay2.add(simulationDPanel[0]);
		ChartDisplay2.setBorder(this.getTitleBorder("������Ƥ���"));
		ChartDisplay2.setBackground(ColorManager.COLOR_WHITE);

		// �[�J��O�W��
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
		gbc_panel.setBorder(this.getTitleBorder("��l��Ƥ���")); 
		Object[][] gbcDataTitle = { 
			{0, 0, 1, 1, 0, 0, alignR, fill_N, getLabel("��O�G")},
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
		gbc_panel.setBorder(getTitleBorder("�ثe��ܪ�����"));
		gbc_panel.setPreferredSize(new Dimension(300, gbc_panel.getHeight()));
		
		Object[] gbcData = { 0, 0, 1, 1, 1, 1, this.alignC, this.fill_B, TabbedPanel };
		setGBC(gbcData);
		
		gbc.insets = new Insets(0, 0, 0, 0);
		return gbc_panel;
	}

	private JCheckBox[] createCheckBox(int select_mode) {
		// �Q��ܪ��ƶq
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
		JButton button = new JButton("�إ߼@��");
		button.setFont(Main.FONT_TEXT);
		button.addActionListener(e -> { 
			if (ischosenN[select_mode] == 0) {
				JOptionPane.showMessageDialog(jframe, "�Цܤֿ�ܤ@�Ӭ�O�I", "�إ߼@���n�D", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				Distribution.saveAsFile(data, data.getSrcFileName());
				Main.log(Level.INFO, "�����w�x�s�� " + data.getSrcFileName());
			} catch (IOException E) {
				String msg = String.format("�L�k�s�� %s �A�]���ɮץ��ѥt�@�ӵ{�ǨϥΡC", data.getSrcFileName());
				JOptionPane.showMessageDialog(jframe, msg, "�s�����~", JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(jframe, "�|���w��ܪ���O�٥���ܨ���������I", "�إ߼@���n�D", JOptionPane.ERROR_MESSAGE);
				return;
			}

			data.setSurgeryStatictis(info_choose);
			Main.switchLights(3);
			JOptionPane.showMessageDialog(this.jframe, "�@�����ͭ���");
			
			
			Main.switchLights(2);
			Main.switchPage(this);
		});

		return button;
	}

	/**
	 * �]�t�Ŀ����B���ئW�١B�զX����B�إߤ��������s
	 * 
	 * @param select_mode = 0 �� "�t�Ϋ�ĳ" <br>
	 *                    select_mode = 1 �� "�ۦ���"
	 * 
	 * @return �^�ǨC�@�ӫإߧ�����������ܭ��O
	 */
	private JPanel createSelectPanel(int select_mode) {
		// ��֤��
		JCheckBox[] chooseSome = createCheckBox(select_mode);
		// ��O���ҫإ�
		JLabel[] jl_subjectN = new JLabel[SubjectName.length];
		for (int i = 0; i < jl_subjectN.length; i++) {
			jl_subjectN[i] = getLabel_B(SubjectName[i]);
		}
		// �إ߬�O�����U�Ԧ����
		jcb_SelectDistribution.add(createComboBoxes(SubjectName.length, select_mode > 0));

		// ��N�ɶ�������ܭ��O
		gbc_panel = new JPanel(new GridBagLayout());
		gbc.insets = new Insets(5, 0, 0, 0); 
		Object[][] gbcDataTitle = {
			// ���D�C
			{0, 0, 1, 1, 1, 0, this.alignL, this.fill_N, chooseAll[select_mode]},
			{1, 0, 1, 1, 1, 0, this.alignC, this.fill_B, getLabel_B(distribution_select_colName[0])},
			{2, 0, 1, 1, 1, 0, this.alignC, this.fill_N, getLabel_B(distribution_select_colName[1])},
			// ���j�u
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
	 * �إ�n�ӲզX���
	 * 
	 * @param n ���w�إ߼ƶq
	 * @return �^�Ǥ@�M�զX���
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
	 * @param a     �Y�M�զX���
	 * @param n     ���w�}�C�����ĴX��
	 * @param items ���[�J�զX���������
	 */
	private void addItemstoComboBoxN(int m, int n, String[] items, int selectedIndex) {
		JComboBox<String> jcb = jcb_SelectDistribution.get(m).get(n);
		for (int i = 0; i < items.length; i++) {
			jcb.addItem(items[i]);
		}
		jcb.setSelectedItem(m == 0 ? items[selectedIndex] : ss.get(n + 1)[Main.COL_DTYPE]);
		jcb.addItemListener(e -> {
			int SelectedIndex = jcb.getSelectedIndex();
			// ��N�ɶ��έp���
			ss.get(n + 1)[Main.COL_DTYPE] = DType[SelectedIndex];
			jcb.setBorder(SelectedIndex > 0 ? new EmptyBorder(0, 0, 0, 0) : new LineBorder(Color.red));
		});
	}

	// �ֳt�]�wLabel
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
	 * ����w��O�����U�ؤ�����
	 * 
	 * @param Subject ��O���
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
		// ���Z�q
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
		// �ռ�
		int group = sd.get(Subject).length - 1;
		// ���Z�q
		String[] values = data.getSurgeryStatictis().get(Subject);
		double space_max = Double.parseDouble(values[Main.COL_MAX]) / group;
		// �����Ϫ�
		JPanel jp = new JPanel(new GridLayout(3, 2, 5, 5));
		for (int d = 0; d < SimulDistribution.size(); d++) {
			String DomainName = String.format("���Z�q�G %.2f����", space_max);
			int[] count = Distribution.getDistribution(SimulDistribution.get(d), group, space_max);
			jp.add(getDistributionChart(DType[d + 1], DomainName, count, Subject));
		}
		simulationDPanel[Subject - 1] = jp;
	}

	// ø�s�����Ϫ��禡
	private ChartPanel getDistributionChart(String title, String DomainName, int[] count, int row) {
		ChartFactory.setChartTheme(Main.getChartTheme());

		XYSeries Domain = new XYSeries(DomainName);
		for (int i = 0; i < count.length; i++) {
			Domain.add(i + 1, count[i]);
		}

		XYSeriesCollection intervalXYDataSet = new XYSeriesCollection();
		intervalXYDataSet.addSeries(Domain);

		JFreeChart chart = ChartFactory.createXYBarChart(title, "", false, "����", intervalXYDataSet,
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
		// ���oø�s�Ϫ�ϰ쪺����
		XYPlot plot = chart.getXYPlot();
		// �]�m��V��u�i��
		plot.setRangeGridlinesVisible(true);
		// �]�m��u��m
		plot.setRangeGridlinePaint(Color.gray);
		// �]�m��m
		plot.setBackgroundPaint(new Color(255, 255, 255));
		plot.setForegroundAlpha(1.0f);
		plot.setBackgroundAlpha(0.5f);

		// �]�m�ƾڶb�B�I����ܺ��
		DecimalFormat df = new DecimalFormat("#0");
		// �]�m�ƾڶb���Ү榡
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
		// �]�mX�b���D�r��
		domainAxis.setLabelFont(Main.FONT_TEXT);
		// �]�mX�b���ƭȦr��
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
		// �]�m�̰����@�� Item ��Ϥ����ݪ��Z��
		rangeAxis.setUpperMargin(0.15);
		// �]�m�̧C���@�� Item ��Ϥ����ݪ��Z��
		rangeAxis.setLowerMargin(0.15);
		plot.setRangeAxis(rangeAxis);

		// �ѨM����ýX�����D
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
