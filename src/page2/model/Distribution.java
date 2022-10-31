package page2.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import page2.controller.Main;
 

//實作序列化 輸出檔案無法直觀供人觀察 
//僅有資料分析的功能

public class Distribution implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// 預設欄位名稱
	public String Day = "手術日";
	public String Room = "室別";
	public String priority = "分類";
	public String subject = "科別";
	public String surStart = "劃刀時間";
	public String surEnd = "縫合切口";
	public String anesthesia = "麻醉";
	public String aneStart = "麻醉開始";
	public String aneEnd = "麻醉完成";
	public String ArriveTime = "到達時間";
	public String EntryTime = "入室時間";
	public String doctor = "主治醫師";
	public String number = "病歷號";

	// 原始資料
	private ArrayList<String[]> Raw = new ArrayList<>();

	// 取得原始資料
	public ArrayList<String[]> getRaw() {
		return Raw;
	}

	// 設置原始資料
	public void setRaw(ArrayList<String[]> src) {
		Raw = src;
	}

	// 原始資料欄位名稱查詢
	private HashMap<String, Integer> ColumnName;

	// 取得原始資料欄位名稱
	public HashMap<String, Integer> getColumnName() {
		return ColumnName;
	}

	// 設置原始資料欄位名稱
	public void setColumnName(String Key, Integer value) {
		ColumnName.put(Key, value);
	}

	// 原始資料欄位一維陣列
	private String[] ColumnNameLine;

	// 取得原始資料欄位一維陣列
	public String[] getColumnNameLine() {
		return ColumnNameLine;
	}

	// 每日人數統計
	private double DailyAveg; // 每日平均人數
	private double DailyMax; // 某日最高人數
	private double DailyMin; // 某日最低人數

	private double DailyRAveg; // 某日常規氘平均人數

	public double getDailyRAveg() {
		return DailyRAveg;
	}

	// 對應的函式
	// initDaily()
	// saveDaily(TreeMap<String, Integer> S)
	// getDailyAveg()
	// getDailyMax()
	// getDailyMin()

	// 每小時抵達時間統計
	private double[] ArrivalTimeAveg = new double[24];
	// 對應的函式
	// initDaily()
	// getArrivalTimeAveg()

	// 每小時常規刀發生機率
	private double[] RArrivalAveg = new double[24];

	// 取得每小時到達人數平均
	public void setRArrivalAveg(double[] src) {
		RArrivalAveg = src;
	}

	// 取得每小時到達人數平均
	public double[] getRArrivalAveg() {
		return RArrivalAveg;
	}

	// 每小時急刀一級發生機率
	private double[] EArrivalAveg = new double[24];

	public void setEArrivalAveg(double[] src) {
		EArrivalAveg = src;
	}

	public double[] getEArrivalAveg() {
		return EArrivalAveg;
	}

	// 每小時急刀二級發生機率
	private double[] OArrivalAveg = new double[24];

	public void setOArrivalAveg(double[] src) {
		OArrivalAveg = src;
	}

	public double[] getOArrivalAveg() {
		return OArrivalAveg;
	}

	// 每日急刀每日平均人數
	private double EOArrivalDailyAvg;

	public void setEOArrivalDailyAvg(double src) {
		EOArrivalDailyAvg = src;
	}

	// 取得每日急刀每日平均人數
	public double getEOArrivalDailyAvg() {
		return EOArrivalDailyAvg;
	}
	// 對應的函式
	// initDaily()
	// getArrivalTimeAveg()

	// 刀別 手術優先權統計
	private String[] priority_type;
	//private String[] priority_specify "E", "O"
	private double EArrival; // 急刀一級發生機率
	private double OArrival; // 急刀二級發生機率
	// 對應的函式
	// setPriority_type(String[] priority_type)
	// getPriority_type()
	// setEArrival(double EArrival)
	// setOArrival(double OArrival)
	// getEArrival()
	// getOArrival()
	// initEOArrival()

	// 麻醉時間分布統計
	private ArrayList<String[]> ADistribution;
	private ArrayList<String[]> AStatistics;

	public void setAnesthesiaDistribution(ArrayList<String[]> src) {
		ADistribution = src;
	}

	public ArrayList<String[]> getAnesthesiaDistribution() {
		return ADistribution;
	}

	public void setAnesthesiaStatictis(ArrayList<String[]> src) {
		AStatistics = src;
	}

	public ArrayList<String[]> getAnesthesiaStatictis() {
		return AStatistics;
	}

	// 到達人數分布
	private ArrayList<Integer> PeopleNumber = new ArrayList<Integer>();

	public void setPeopleNumber(ArrayList<Integer> src) {
		PeopleNumber = src;
	}

	public ArrayList<Integer> getPeopleNumber() {
		return PeopleNumber;
	}

	// 手術時間分布統計
	private ArrayList<String[]> SDistribution;
	private ArrayList<String[]> SStatistics;

	public void setSurgeryDistribution(ArrayList<String[]> src) {
		SDistribution = src;
	}

	public ArrayList<String[]> getSurgeryDistribution() {
		return SDistribution;
	}

	public void setSurgeryStatictis(ArrayList<String[]> src) {
		SStatistics = src;
	}

	public ArrayList<String[]> getSurgeryStatictis() {
		return SStatistics;
	}

	public String[] getSubjectName() {
		int SubjectCount = SStatistics.size();
		String[] SubjectName = new String[SubjectCount - 1];
		for (int row = 1; row < SubjectCount; row++) {
			SubjectName[row - 1] = SStatistics.get(row)[0];
		}
		return SubjectName;
	}

	// 取得每小時到達人數平均
	public void setArrivalTimeAveg(double[] ArrivalTimeAveg) {
		this.ArrivalTimeAveg = ArrivalTimeAveg;
	}

	public double[] getArrivalTimeAveg() {
		return ArrivalTimeAveg;
	}
	// ---------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------

	// 主要功能函式
	// 來源檔案名稱
	private String FileName_src;

	// 設定來源檔案名稱
	public void setSrcFileName(String src) {
		FileName_src = src;
		File f = new File(FileName_src);
		totalBytes = f.length();
	}

	// 取得來源檔案名稱
	public String getSrcFileName() {
		return FileName_src;
	}

	public Distribution() {
	}

	public Distribution(String src) throws IOException {
		setSrcFileName(src);
		DataLoding();
	}

	private double totalBytes = 0;
	private double gainBytes = 0;

	private void addProgress(String[] src) {
		for (String i : src) {
			gainBytes += i.getBytes().length + 1;
			double Progress = gainBytes / totalBytes * 100;
			Main.setLoadingText(FileName_src, Progress);
		}
	}

	private void DataLoding() throws IOException {
		CSVReader reader = new CSVReader(FileName_src); 
		Main.log(Level.INFO, "已載入" + FileName_src);

		String[] Line = reader.readNext();
		addProgress(Line);
		// 過濾換行字元並記錄標題列
		ColumnNameLine = FixString.filter(Line, '\n');
		// 用來快速尋找欄位名稱
		ColumnName = new HashMap<>();
		for (int i = 0; i < Line.length; i++) {
			ColumnName.put(Line[i], i);
		}

		if (ColumnName.containsKey(Day)) {
			Main.log(Level.INFO, "欄位名稱已匹配");
		}

		// 載入原始資料
		Raw = new ArrayList<>();
		while ((Line = reader.readNext()) != null) {
			Raw.add(Line);
			addProgress(Line);
		}

		Main.log(Level.INFO, "資料讀取完成");
		
		reader.close();
	}

	/**
	 * 設定選取的欄位
	 * 
	 * **請下中斷點查看數值為何 不應該再使用system.out.printf()來查看值**
	 * 
	 * @param col 欄位對應值集合
	 */
	public void setColumn(int[] col) {
		Day = ColumnNameLine[col[0]];
		Room = ColumnNameLine[col[1]];
		priority = ColumnNameLine[col[2]];
		subject = ColumnNameLine[col[3]];
		surStart = ColumnNameLine[col[4]];
		surEnd = ColumnNameLine[col[5]];
		anesthesia = ColumnNameLine[col[6]];
		aneStart = ColumnNameLine[col[7]];
		aneEnd = ColumnNameLine[col[8]];
	}

	public void create() throws IOException {
		initSurgeryRoom();

		initDaily();

		initEOArrival();

		count_Anesthesia();
		preprocessing(anesthesiaGroups);
		ADistribution = printDistribution(anesthesiaGroups);
		AStatistics = printStatistics(anesthesiaGroups);

		count_Surgery();
		preprocessing(surgeryData);
		SDistribution = printDistribution(surgeryData);
		SStatistics = printStatistics(surgeryData);
	}

	public static Distribution loadingFile(String FileName) throws IOException {
		CSVReader csv = new CSVReader(FileName);

		Distribution temp = new Distribution();
		temp.setSrcFileName(FileName);
		
		csv.readNext();
		String[] line = csv.readNext();
		temp.setDailyAveg(Double.parseDouble(line[0]));// 每日平均人數
		temp.setDailyMax(Double.parseDouble(line[1])); // 每日最高人數
		temp.setDailyMin(Double.parseDouble(line[2])); // 每日最低人數
		temp.setEArrival(Double.parseDouble(line[3])); // 急刀一級機率
		temp.setOArrival(Double.parseDouble(line[4])); // 急刀二級機率
		temp.setEOArrivalDailyAvg(Double.parseDouble(line[5]));// 急刀抵達機率
		
		// 常規刀抵達機率
		csv.readNext(); 
		line = csv.readNext();
		double[] r = new double[line.length];
		for (int i = 0; i < r.length; i++)
			r[i] = Double.valueOf(line[i]);
		temp.setRArrivalAveg(r);
		
		// 急刀一級抵達機率
		csv.readNext(); 
		line = csv.readNext();
		r = new double[line.length];
		for (int i = 0; i < r.length; i++)
			r[i] = Double.valueOf(line[i]);
		temp.setEArrivalAveg(r);

		// 急刀二級抵達機率
		csv.readNext(); 
		line = csv.readNext();
		r = new double[line.length];
		for (int i = 0; i < r.length; i++)
			r[i] = Double.valueOf(line[i]);
		temp.setOArrivalAveg(r);
		
		// 手術房名稱
		csv.readNext(); 
		line = csv.readNext();
		temp.setRoomName(line);
		
		// 平均抵達時間
		csv.readNext();
		line = csv.readNext();
		double[] ATA = new double[line.length];
		for (int i = 0; i < ATA.length; i++)
			ATA[i] = Double.valueOf(line[i]);
		temp.setArrivalTimeAveg(ATA);
		
		// 手術時間分布
		csv.readNext();
		line = csv.readNext();
		int T = Integer.parseInt(line[0]);
		ArrayList<String[]> SD = new ArrayList<String[]>();
		while ((T--) != 0)
			SD.add(csv.readNext());
		temp.setSurgeryDistribution(SD);

		// 手術時間統計
		csv.readNext();
		line = csv.readNext();
		T = Integer.parseInt(line[0]);
		ArrayList<String[]> SS = new ArrayList<String[]>();
		while ((T--) != 0)
			SS.add(csv.readNext());
		temp.setSurgeryStatictis(SS);

		// 麻醉時間分布
		csv.readNext();
		line = csv.readNext();
		T = Integer.parseInt(line[0]);
		ArrayList<String[]> AD = new ArrayList<String[]>();
		while ((T--) != 0)
			AD.add(csv.readNext());
		temp.setAnesthesiaDistribution(AD);

		// 麻醉時間統計
		csv.readNext();
		line = csv.readNext();
		T = Integer.parseInt(line[0]);
		ArrayList<String[]> AS = new ArrayList<String[]>();
		while ((T--) != 0)
			AS.add(csv.readNext());
		temp.setAnesthesiaStatictis(AS);

		csv.close();
		return temp;
	}

	public static void saveAsFile(Distribution data, String FileName) throws IOException {
		CSVWriter csv = new CSVWriter(FileName);

		String[] title = new String[1];

		String[] temp = { "每日平均人數", "每日最高人數", "每日最低人數", "急刀一級發生機率", "急刀二級發生機率", "急刀抵達機率" };
		csv.WriteNext(temp);
		temp[0] = String.valueOf(data.getDailyAveg());
		temp[1] = String.valueOf(data.getDailyMax());
		temp[2] = String.valueOf(data.getDailyMin());
		temp[3] = String.valueOf(data.getEArrival());
		temp[4] = String.valueOf(data.getOArrival());
		temp[5] = String.valueOf(data.getEOArrivalDailyAvg());
		csv.WriteNext(temp);

		title[0] = "常規刀抵達機率";
		csv.WriteNext(title);
		double[] r = data.getRArrivalAveg();
		temp = new String[r.length];
		for (int i = 0; i < r.length; i++)
			temp[i] = String.valueOf(r[i]);
		csv.WriteNext(temp);

		title[0] = "急刀一級抵達機率";
		csv.WriteNext(title);
		r = data.getEArrivalAveg();
		temp = new String[r.length];
		for (int i = 0; i < r.length; i++)
			temp[i] = String.valueOf(r[i]);
		csv.WriteNext(temp);

		title[0] = "急刀二級抵達機率";
		csv.WriteNext(title);
		r = data.getOArrivalAveg();
		temp = new String[r.length];
		for (int i = 0; i < r.length; i++)
			temp[i] = String.valueOf(r[i]);
		csv.WriteNext(temp);
		// ----------------------------------------------------------------
		// ----------------------------------------------------------------
		title[0] = "手術房名稱";
		csv.WriteNext(title);
		csv.WriteNext(data.getRoomName());

		title[0] = "平均抵達時間";
		csv.WriteNext(title);
		double[] ATA = data.getArrivalTimeAveg();
		temp = new String[ATA.length];
		for (int i = 0; i < ATA.length; i++)
			temp[i] = String.valueOf(ATA[i]);
		csv.WriteNext(temp);

		title[0] = "手術時間分布";
		csv.WriteNext(title);
		ArrayList<String[]> SD = data.getSurgeryDistribution();
		title[0] = String.valueOf(SD.size());
		csv.WriteNext(title);
		for (String[] i : SD)
			csv.WriteNext(i);

		title[0] = "手術時間統計";
		csv.WriteNext(title);
		ArrayList<String[]> SS = data.getSurgeryStatictis();
		title[0] = String.valueOf(SS.size());
		csv.WriteNext(title);
		for (String[] i : SS)
			csv.WriteNext(i);

		title[0] = "麻醉時間分布";
		csv.WriteNext(title);
		ArrayList<String[]> AD = data.getAnesthesiaDistribution();
		title[0] = String.valueOf(AD.size());
		csv.WriteNext(title);
		for (String[] i : AD)
			csv.WriteNext(i);

		title[0] = "麻醉時間統計";
		csv.WriteNext(title);
		ArrayList<String[]> AS = data.getAnesthesiaStatictis();
		title[0] = String.valueOf(AS.size());
		csv.WriteNext(title);
		for (String[] i : AS)
			csv.WriteNext(i);

		csv.close();
	}

	// ---------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------
	// 手術房名稱
	private String[] RoomName;

	public void setRoomName(String[] RoomName) {
		this.RoomName = RoomName;
	}

	public String[] getRoomName() {
		return RoomName;
	}

	private void initSurgeryRoom() throws IOException {
		TreeSet<String> temp = new TreeSet<String>();
		if (ColumnName.containsKey(Room)) {
			int col_room = ColumnName.get(Room);
			for (String[] i : Raw)
				temp.add(i[col_room]);
			// 去除空字串
			temp.remove("");
			// 使用一維陣列存放
			RoomName = (String[]) temp.toArray(new String[temp.size()]);
		} else {
			Main.log(Level.INFO, "無法建立手術室名稱!!");
		}
	}

	/**
	 * 計算各個手術的麻醉時間並依照麻醉類型分組
	 */
	private TreeMap<String, ArrayList<Double>> anesthesiaGroups;

	public void count_Anesthesia() {
		anesthesiaGroups = new TreeMap<>();

		int StartA = this.ColumnName.get(aneStart);
		int EndA = this.ColumnName.get(aneEnd);
		int NameA = this.ColumnName.get(anesthesia);

		for (String[] row : Raw) {
			String name = row[NameA];
			double time = Time.LengthMin(row[StartA], row[EndA]);

			if (name.equals("") || time <= 0) continue;
			
			anesthesiaGroups.computeIfAbsent(name, k -> new ArrayList<Double>());
			anesthesiaGroups.get(name).add(time);
		}
	}

	/**
	 * 手術時間統計
	 */
	private TreeMap<String, ArrayList<Double>> surgeryData;

	public void count_Surgery() {
		surgeryData = new TreeMap<>();

		int StartS = this.ColumnName.get(surStart);
		int EndS = this.ColumnName.get(surEnd);
		int NameS = this.ColumnName.get(subject);

		for (String[] row : Raw) {
			String name = row[NameS];
			double time = Time.LengthMin(row[StartS], row[EndS]);
			
			if (name.equals("") || time <= 0) continue;

			surgeryData.computeIfAbsent(name, k-> new ArrayList<Double>());
			surgeryData.get(name).add(time);
		}
	}

	/**
	 * 紀錄離群值
	 */
	private TreeMap<String, Double> Outliers = new TreeMap<>();

	/**
	 * 資料前處理(排序 + 刪除離群值)
	 * 
	 * @param src 資料來源
	 */
	private void preprocessing(TreeMap<String, ArrayList<Double>> src) {
		// 重新紀錄離群值
		Outliers.clear();

		for (Entry<String, ArrayList<Double>> entry : src.entrySet()) {
			ArrayList<Double> values = entry.getValue();
			// 資料由小到大排序
			values.sort(null);
			
			Main.log(Level.INFO, entry.getKey() + " 之手術時間已排序%n");
			// 求出離群值界線
			int Q1 = (int) (0.25 * values.size());
			int Q3 = (int) (0.75 * values.size());
			double upLimit = 1.5 * (values.get(Q3) - values.get(Q1)) + values.get(Q3);
			Outliers.put(entry.getKey(), upLimit);

			// 刪除離群值
			for (int j = values.size() - 1; j >= 0; j--) {
				if (values.get(j) <= upLimit)
					break;
				values.remove(j);
			}
		}
	}

	private ArrayList<String[]> printDistribution(TreeMap<String, ArrayList<Double>> src) {
		ArrayList<String[]> distribution = new ArrayList<>();

		String[] dataRow = { "區間編號" };
		distribution.add(dataRow);

		int max_group = 0;
		for (Entry<String, ArrayList<Double>> entry : src.entrySet()) {
			int[] row = getDistribution(entry.getValue());
			if (max_group < row.length)
				max_group = row.length;

			dataRow = new String[row.length + 1];
			dataRow[0] = entry.getKey();
			for (int i = 0; i < row.length; i++) {
				dataRow[i + 1] = String.valueOf(row[i]);
			}

			distribution.add(dataRow);
		}

		dataRow = new String[max_group + 1];
		dataRow[0] = "區間編號";
		for (int i = 1; i < dataRow.length; i++) {
			dataRow[i] = String.valueOf(i);
		}

		distribution.set(0, dataRow);
		return distribution;
	}

	public static int[] getDistribution(ArrayList<Double> value) {
		int n = value.size();
		int group = (int) Math.round(1 + 3.322 * Math.log10(n));

		int[] num = new int[group];
		Arrays.fill(num, 0);

		double max = value.get(n - 1);
		double space = max / group;

		for (Double i : value) {
			if (i == 0)
				continue; // 手術時間為0則不計

			int index = (int) (i / space);
			if (index >= group)
				index = group - 1;
			num[index]++;
		}
		return num;
	}

	public static int[] getDistribution(ArrayList<Double> values, int group, double space) {
		int[] count = new int[group];

		for (Double k : values) {
			int index = (int) (k / space);
			if (index >= group)
				index = group - 1;
			count[index]++;
		}

		return count;
	}

	/**
	 * 
	 * @param src 每一列為類型，每一行為統計目標
	 * @return
	 */
	private ArrayList<String[]> printStatistics(TreeMap<String, ArrayList<Double>> src) {

		ArrayList<String[]> statictis = new ArrayList<>(); // 統計表
		String[] colName = { "名稱", "最大值", "最小值", "眾數", "平均值", "標準差", "次數", "分布", "離群值界線" };
		statictis.add(colName);

		for (Entry<String, ArrayList<Double>> entry : src.entrySet()) {
			ArrayList<Double> value = entry.getValue();
			
			int len = value.size();
			double min = 0;
			double max = 0;
			double mode = 0;
			double avg = 0;
			double dif = 0;

			if (len != 0) {
				min = value.get(0);
				max = value.get(len - 1);
				mode = searchMode(value);
				avg = calculateAvg(value);
				dif = calculateStd(value, avg);
			}

			String[] row = new String[colName.length];
			row[0] = entry.getKey();
			row[1] = String.valueOf(max);
			row[2] = String.valueOf(min);
			row[3] = String.valueOf(mode);
			row[4] = String.valueOf(avg);
			row[5] = String.valueOf(dif);
			row[6] = String.valueOf(len);
			row[7] = "";
			row[8] = String.valueOf(Outliers.get(entry.getKey()));
			
			statictis.add(row);
		}
		return statictis;
	}

	private double calculateAvg(ArrayList<Double> numbers) {
		double avg = 0;
		for (Double num : numbers)
			avg += num;
		avg /= numbers.size();
		return avg;
	}

	private double calculateStd(ArrayList<Double> numbers, double avg) {
		double std = 0;
		for (Double num : numbers) {
			std += (num - avg) * (num - avg);
		}
		std = Math.sqrt(std / numbers.size());

		return std;
	}

	/**
	 * 眾數（mode）指一組數據中出現次數最多的數據值。
	 * 例如{8，7，7，8，6，5，5，8，8，8}中，出現最多的是8，因此眾數是8，眾數可能是一個數（數據值），
	 * 但也可能是多個數（數據值）。若數據的數據值出現次數相同且無其他數據值時，則不存在眾數。
	 * 例如{5，2，8，2，5，8}中，2、5、8出現次數相同且沒有其他數，因此此數據不存在眾數。
	 * 
	 * 在統計學上，眾數和平均數、中位數類似，都是母體或隨機變數有關集中趨勢的重要資訊。 在高斯分布（常態分布）中，眾數為峰值所在的位置，和平均數、中位數相同。
	 * 但若分布是高度偏斜分布，眾數可能會和平均數、中位數有很大的差異。
	 * 
	 * 分布中的眾數不一定只有一個，若機率質量函數或機率密度函數在x1, x2……等多個點都有最大值，就會有多個眾數，
	 * 最極端的情形是離散型均勻分布，所有的點機率都相同，所有的點都是眾數。若機率密度函數有數個局部最大值，
	 * 一般會將這幾個極值都稱為眾數，此連續機率分布會稱為多峰分布（和單峰性相反）。
	 * 
	 * @param numbers 其內容必須皆為正整數並且由小到大排序。
	 * 
	 * @return 回傳找到的眾數，如果有多個眾數。
	 */
	private double searchMode(ArrayList<Double> numbers) {
		if (numbers.size() == 0) {
			Main.log(Level.INFO, "Distribution.searchMode - 該陣列沒有任何數值!");
			return 0;
		}

		ArrayList<Double> mode = new ArrayList<>();
		double curMode = numbers.get(0);
		if (curMode <= 0) {
			Main.log(Level.INFO, "Distribution.searchMode - 發現該陣列有非正整數的值!\n");
			return 0;
		}

		int maxAmount = 0;
		int curAmount = 0;

		for (Double num : numbers) {
			if (curMode != num) {
				if (maxAmount < curAmount) {
					mode.clear();
					mode.add(curMode);
					maxAmount = curAmount;
				} else if (maxAmount == curAmount) {
					mode.add(curMode);
				}

				curAmount = 1;
				curMode = num;
			} else {
				curAmount++;
			}
		}

		if (mode.size() <= 0) {
			return 0; 
		}
			
		if (maxAmount * mode.size() < numbers.size()) {
			return mode.get((mode.size() - 1) / 2);
		}
		
		return 0;
	}

	// -----------------------------------------------------------------------------------------------
	// ---------DailyAvegN----------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	private TreeMap<String, Integer> S;
	//private String MaxDay  某日最高人數日期
	//private String MinDay  某日最低人數日期

	public void setDailyAveg(double DailyAveg) {
		this.DailyAveg = DailyAveg;
	}

	public double getDailyAveg() {
		return DailyAveg;
	}

	public void setDailyMax(double DailyMax) {
		this.DailyMax = DailyMax;
	}

	public double getDailyMax() {
		return DailyMax;
	}

	public void setDailyMin(double DailyMin) {
		this.DailyMin = DailyMin;
	}

	public double getDailyMin() {
		return DailyMin;
	}

	private int unrecognizable;

	public int getUnrecognizable() {
		return unrecognizable;
	}

	private void initDaily() {
		S = new TreeMap<String, Integer>();
		DailyAveg = 0.0;
		DailyMax = 0.0;
		DailyMin = Double.MAX_VALUE;

		unrecognizable = 0; 
		
		int DayColumn = getColumnValueBy(Day); 
		int priorityCol = getColumnValueBy(priority); 
		int ArrivalTimeCol = getColumnValueBy(ArriveTime); 
		
		int DailyAmount = 1;
		
		String curDate = ""; // 紀錄這一格日期
		String oldcurDate = ""; // 紀錄上一格日期
		String PriorityData = "";
		
		// 統計資料抵達時間
		double[] RArrivalStatistic = new double[24];
		// 統計急刀一級到達平均
		double[] EArrivalStatistic = new double[24];
		// 統計急刀二級到達平均
		double[] OArrivalStatistic = new double[24];
  
		for (String[] Line : Raw) {
			int time = 0;
			// 將時間轉換成分鐘
			time = CountTime(Line[ArrivalTimeCol]);

			// 如果目前這列的日期不為空值 則代表來到下一天
			if (!Line[DayColumn].equals(""))
				curDate = Line[DayColumn];
			
			PriorityData = Line[priorityCol];
			// 因第一個資料沒有上一筆 所以將它設置為此筆
			if (oldcurDate.equals(""))
				oldcurDate = Line[DayColumn];

			try {
				// 如果time不為1440且不為假日則加入統計
				if (time < 1440 && !Time.isWeekend(curDate)) {
					if (PriorityData.equals("E"))
						EArrivalStatistic[time / 60] += 1;
					else if (PriorityData.equals("O"))
						OArrivalStatistic[time / 60] += 1;
					else if (PriorityData.equals("R"))
						RArrivalStatistic[time / 60] += 1;
				}

				// 如果新資料為假日 舊資料為平日則記錄舊資料
				if (Time.isWeekend(curDate)) {
					if (!Time.isWeekend(oldcurDate)) {
						save(oldcurDate, DailyAmount);
						curDate = Line[DayColumn];
						DailyAmount = 1;
					}
				}
				// 如果格子為空或是和上一次讀取的資料一樣則數量+1
				else if ((Line[DayColumn].equals("") || Line[DayColumn].equals(oldcurDate))) {
					DailyAmount++;
				}
				// 如舊資料非平日且不符合以上條件則紀錄舊資料
				else if (!Time.isWeekend(oldcurDate)) {
					save(oldcurDate, DailyAmount);
					curDate = Line[DayColumn];
					DailyAmount = 1;
				}
				// 如果不符合以上所有情況則刷新資料
				else {
					curDate = Line[DayColumn];
					DailyAmount = 1;
				}
				
				// 如果新資料不為空格(舊資料同一天)則將舊資料刷新成新資料
				if (!curDate.equals("")) {
					oldcurDate = curDate;
				}

			} catch (ParseException e) {
				unrecognizable++;
			}
		}

		// 如果最後一天不為假日則記錄資料
		try {
			if (!Time.isWeekend(curDate))
				save(curDate, DailyAmount);
		} catch (ParseException e) {
			unrecognizable++;
		}

		// 計算每日病患個數平均值
		DailyRAveg = 0;
		EOArrivalDailyAvg = 0.;
		// 計算出每小時平均與急刀到達占比
		for (int i = 0; i < RArrivalStatistic.length; i++) {
			EArrivalAveg[i] = (EArrivalStatistic[i] / S.size());

			OArrivalAveg[i] = (OArrivalStatistic[i] / S.size());

			RArrivalAveg[i] = (RArrivalStatistic[i] / S.size());

			EOArrivalDailyAvg += (EArrivalStatistic[i] + OArrivalStatistic[i]);
			DailyAveg += EArrivalAveg[i] + OArrivalAveg[i] + RArrivalAveg[i];
			DailyRAveg += RArrivalAveg[i];
		}
		EOArrivalDailyAvg /= S.size();
	}

	private int getColumnValueBy(String colName) { 
		if (ColumnName.containsKey(colName)) {
			return ColumnName.get(colName);
		}
		return -1;
	}
	
	
	// 將資料的時間轉換成分鐘
	private int CountTime(String str) {
		int time = 0;
		// 如果字串內容不為空輸出數值 否則輸出1440
		if (!str.equals("")) {
			time = ((int) str.charAt(0) - 48) * 10 * 60; // 將時間轉成分鐘
			time = time + ((int) str.charAt(1) - 48) * 60;
			time = time + ((int) str.charAt(3) - 48) * 10;
			time = time + ((int) str.charAt(4) - 48);

			return time;
		} else
			return 1440;
	}

	private void save(String curDate, Integer dailyAmount) {

		if (dailyAmount != 0) {
			PeopleNumber.add(dailyAmount);

			if (DailyMax < dailyAmount) {
				//MaxDay  curDate
				DailyMax = dailyAmount;
			}

			if (DailyMin > dailyAmount) {
				//MinDay  curDate
				DailyMin = dailyAmount;
			}

			S.put(curDate, dailyAmount);
		}
	}

	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	public void setPriority_type(String[] priority_type) {
		this.priority_type = priority_type;
	}

	public String[] getPriority_type() {
		return priority_type;
	}

	public void setEArrival(double EArrival) {
		this.EArrival = EArrival;
	}

	public double getEArrival() {
		return EArrival;
	}

	public void setOArrival(double OArrival) {
		this.OArrival = OArrival;
	}

	public double getOArrival() {
		return OArrival;
	}

	public void initEOArrival() {
		if (ColumnName.containsKey(priority)) {
			int col_pri = ColumnName.get(priority);
			int E = 0;
			int O = 0;
			int Total = 0;

			for (String[] i : Raw) {
				if (i[col_pri].equals("E"))
					E++;
				else if (i[col_pri].equals("O"))
					O++;
				if (!i[col_pri].isEmpty())
					Total++;
			}

			if (Total == 0) {
				EArrival = OArrival = 0;
			} else {
				EArrival = (double) E / Total;
				OArrival = (double) O / Total;
			}
		} else {
			Main.log(Level.INFO, "Distribution-initEOArrival 找不到欄位而無法建立刀別機率!!");
		}
	}
}
