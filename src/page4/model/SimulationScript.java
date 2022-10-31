package page4.model;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import page4.controller.Main;

/**
 * 單純用來產生模擬劇本(腳本)，內容只有抵達事件，也不進行過程模擬。
 * 
 * @author Chasel
 */
public class SimulationScript {
	 
	//亂數種子
	private long current_rnd = 200;
	
	// Calendar 欄位屬性名稱 
	private String[] Attribution = { "時間點", "抵達順序", "事件代碼", "手術級別", "所屬科別", "麻醉名稱", "到達時間", "麻醉開始時間", "麻醉完成時間", "麻醉時長",
			"劃刀時間", "縫合切口", "手術時長", "轉送時長", "清潔時長", "手術房別", "開始等候時間", "等候時長", "病患入室時間", "麻醉結束時間", "轉送時間", "清潔時間" };
	private String[] StringAttribution = { "申請序號", "病患姓名", "病患身分", "手術名稱", "病歷號", "主治醫師" };
	private static final String NODATA = "noData";
	
	private int Time_Code = 1; // "到達時間點"
	private int ID_Code = 2; // "病人代號"
	private int Event_Code = 3; // "事件"
	private int Priority_Code = 4; // "刀別"
	private int Subject_Code = 5; // "所屬科別"
	private int Anesthesia_Code = 6; // "麻醉方式"
	private int ArrivalTime_Code = 7; // "抵達時間"
	private int AnesthesiaBeginTime_Code = 8; // "麻醉開始時間點"
	private int AanesthesiaFinishTime_Code = 9;// "麻醉結束時間點"
	private int AnesthesiaTime_Code = 10; // "麻醉時間長度",
	private int SurgeryBeginTime_Code = 11; // "手術開始時間點"
	private int SurgeryEndTime_Code = 12; // "手術開始時間點"
	private int SurgeryTime_Code = 13; // "手術開始長度"
	private int TransferTimelong_Code = 14; // "轉送時間長度"
	private int CleanTimelong_Code = 15; // "清潔時間長度"
	private int RoomNumber_Code = 16; // "手術房號"
	private int WaitBeginTime_Code = 17; // 開始等待時間點
	private int WaitTime_Code = 18; // 等待時間長度
	private int InRoomTime_Code = 19; // 入室時間
	private int AanesthesiaEnd_Code = 20; // 麻醉結束
	private int TransferTime_Code = 21; // "轉送時間"
	private int CleanTime_Code = 22; // "清潔時間" 
	private int ApplyID_Code = 23; // 申請序號
	private int PatientName_Code = 24; // 病患姓名
	private int Identity_Code = 25; // 身分
	private int SurgeryName_Code = 26; // 手術名稱
	private int patientID_Code = 27; // 病歷號
	private int Doctor_Code = 28;
	
	/* 劇本存放的地方 */
	private ArrayList<String[]> calendar = new ArrayList<>();
	
	// 病歷號
	private int CurNumber = 0; 
	// 科別機率表(輪盤法)
	private double[] Prob_subject;
	// 麻醉機率表(輪盤法)
	private double[] Prob_anesthesia;
	
	// 字串屬性陣列
	private ArrayList<String[]> StringAttributionArr;
	// 下一位急刀病患抵達時間
	private double nextEOArrivalTime = Double.MAX_VALUE;
	// 確認下一位病患是否為急刀
	private boolean EOCheck;
	// 計算平均多久會來一位急刀病患
	private double AvgofEOEventArrivalTime;
	// 病人抵達的時間表
	private ArrayList<Double> ArrivalTime = new ArrayList<Double>();
	// 常規刀病人抵達的計數器
	private int RArrivalCount = 0;
	
	// 劇本產生器
	private JSSS createScript;
	// 事件產生器
	private JSSS ArrivalTimesss;
	// 急刀事件產生器
	private JSSS EO_Eventsss;
	// 手術刀別產生器
	private JSSS RS_priority; 
	// 科別隨機產生器(英文寫錯)
	private JSSS RS_Surgery;
	// 各科別手術時間隨機產生器
	private JSSS[] RS_SurgeryTime;
	// 麻醉隨機產生器
	private JSSS RS_Anesthesia;
	// 各類型麻醉時間產生器
	private JSSS[] RS_AnesthesiaTime;

	/**
	 * 亂數種子
	 * @param src
	 */
	public void setcurrent_rnd(long src) {
		current_rnd = src;
	}
	
	public JSSS[] getRS_SurgeryTime() {
		return RS_SurgeryTime;
	}
	
	public JSSS[] getRS_AnesthesiaTime() {
		return RS_AnesthesiaTime;
	}
	
	// 模擬時間長度
	private double SimulationTime = 0.0;
	public void setSimulationTime(double src) {
		SimulationTime = src;
	}
	public double getSimulationTime() {
		return SimulationTime;
	}
	
	// 劇本設置的一天的平均人數  平日
	private double DailysetNPeople = 0.0; 
	public void setDailysetNPeople(double src) {
		DailysetNPeople = src;
	}
	public double getDailysetNPeople() {
		return DailysetNPeople;
	}
	
	// 一天的平均人數  平日
	protected double DailyNPeople = 0.0; 
	public void setDailyNPeople(double src) {
		DailyNPeople = src;
	}
	public double getDailyNPeople() {
		return DailyNPeople;
	}

	// 一天急刀的平均人數
	protected double EOArrivalDailyAvg;
	public void setEOArrivalDailyAvg(double src) {
		EOArrivalDailyAvg = src;
	}
	public double getEOArrivalDailyAvg() {
		return EOArrivalDailyAvg;
	}

	// 統計資料中一天的最大值
	protected double DailyMaxNPeople = 0.0;
	public void setDailyMaxNPeople(double src) {
		DailyMaxNPeople = src;
	}
	public double getDailyMaxNPeople() {
		return DailyMaxNPeople;
	}

	// 統計資料中一天的最小值
	protected double DailyMinNPeople = 0.0;
	public void setDailyMinNPeople(double src) {
		DailyMinNPeople = src;
	}
	public double getDailyMinNPeople() {
		return DailyMinNPeople;
	}

	// 每小時平均有幾個常規刀病患抵達
	protected double[] RArrivalAveg;
	public void setAvegTimeofArrival(double[] src) {
		RArrivalAveg = src;
	}
	public double[] getAvegTimeofArrival() {
		return RArrivalAveg;
	}
	
	// 各科別手術時間分布 (包含欄位名稱)
	protected ArrayList<String[]> dtb_subject;
	public void setSubjectDistribution(ArrayList<String[]> src) {
		dtb_subject = src;
	}
	public ArrayList<String[]> getSubjectDistribution() {
		return dtb_subject;
	}
	
	// 各種麻醉方法之分布 (包含欄位名稱)
	protected ArrayList<String[]> dtb_anesthesia;
	public void setAnesthesiaDistribution(ArrayList<String[]> src) {
		dtb_anesthesia = src;
	}
	public ArrayList<String[]> getAnesthesiaDistribution() {
		return dtb_anesthesia;
	}

	// 抵達分布
	private int dtb_Arrival = 1;
	public void setDtb_Arrival(int src) {
		dtb_Arrival = src;
	}
	public int getDtb_Arrival() {
		return dtb_Arrival;
	}

	// 榮總急刀一級病患抵達的機率
	protected double ESurgery = 0.0;
	public void setESurgery(double src) {
		ESurgery = src;
	}
	public double getESurgery() {
		return ESurgery;
	}

	// 榮總急刀二級病患抵達的機率
	protected double OSurgery = 0.0;
	public void setOSurgery(double src) {
		OSurgery = src;
	}
	public double getOSurgery() {
		return OSurgery;
	}
 
	// 常規刀病患人數
	private double DailyRAveg = 0;
	public void setDailyRAveg(double src) {
		DailyRAveg = src;
	}
	public double getDailyRAveg() {
		return DailyRAveg;
	}
	
	// 手術室轉送時間
	private double TransferTimelong = 5.0;
	public void setTransferTime(double TransferTime) {
		this.TransferTimelong = TransferTime;
	}
	public double getTransferTime() {
		return TransferTimelong;
	}

	// 手術室清潔時間
	private double CleanTimelong = 5.0;
	public void setCleanTime(double CleanTime) {
		this.CleanTimelong = CleanTime;
	}
	public double getCleanTime() {
		return CleanTimelong;
	}

	// 手術室名稱
	private String[] RoomName;
	private void setRoomName(String[] src) {
		RoomName = src;
	}
	public String[] getRoomName() {
		return RoomName;
	}

	// 科別名稱
	private String[] SubjectName;
	public void setSubjectName(String[] src) {
		SubjectName = src;
	}
	public String[] getSubjectName() {
		return SubjectName;
	}

	// 麻醉名稱
	private String[] AnesthesiaName;
	public void setAnesthesiaName(String[] src) {
		AnesthesiaName = src;
	}
	public String[] getAnesthesiaName() {
		return AnesthesiaName;
	}

	// 取得屬性代碼
	public int getTime_Code() {
		return Time_Code;
	}

	public int getID_Code() {
		return ID_Code;
	}

	public int getEvent_Code() {
		return Event_Code;
	}

	public int getPriority_Code() {
		return Priority_Code;
	}

	public int getSubject_Code() {
		return Subject_Code;
	}

	public int getAnesthesia_Code() {
		return Anesthesia_Code;
	}

	public int getArrivalTime_Code() {
		return ArrivalTime_Code;
	}

	public int getAnesthesiaBeginTime_Code() {
		return AnesthesiaBeginTime_Code;
	}

	public int getAanesthesiaFinishTime_Code() {
		return AanesthesiaFinishTime_Code;
	}

	public int getAnesthesiaTime_Code() {
		return AnesthesiaTime_Code;
	}

	public int getSurgeryBeginTime_Code() {
		return SurgeryBeginTime_Code;
	}

	public int getSurgeryEndTime_Code() {
		return SurgeryEndTime_Code;
	}

	public int getSurgeryTime_Code() {
		return SurgeryTime_Code;
	}

	public int getTransferTimelong_Code() {
		return TransferTimelong_Code;
	}

	public int getCleanTimelong_Code() {
		return CleanTimelong_Code;
	}

	public int getRoomNumber_Code() {
		return RoomNumber_Code;
	}

	public int getWaitBeginTime_Code() {
		return WaitBeginTime_Code;
	}

	public int getWaitTime_Code() {
		return WaitTime_Code;
	}

	public int getApplyID_Code() {
		return ApplyID_Code;
	}

	public int getPatientName_Code() {
		return PatientName_Code;
	}

	public int getIdentity_Code() {
		return Identity_Code;
	}

	public int getSurgeryName_Code() {
		return SurgeryName_Code;
	}

	public int getInRoomTime_Code() {
		return InRoomTime_Code;
	}

	public int getAanesthesiaEnd() {
		return AanesthesiaEnd_Code;
	}

	public int getTransferTime_Code() {
		return TransferTime_Code;
	}

	public int getCleanTime_Code() {
		return CleanTime_Code;
	}

	public int getpatientID_Code() {
		return patientID_Code;
	}

	public int getDoctor_Code_Code() {
		return Doctor_Code;
	}
/*----------------------------------------------上方為資料-----下方為函式處理-----------------------------------------------------*/
	/**
	 * 
	 * @param srcFile
	 * @return
	 * @throws IOException
	 */

	public static SimulationScript FileReader(String srcFile) throws IOException {
		return FileReader(srcFile, false);
	}
	
	/**
	 * 讀取資料
	 * 
	 * @param srcFile 資料來源
	 * @param onlyAttributes 是否只讀取資料屬性
	 * @return 模擬劇本
	 * @throws IOException
	 */ 
	public static SimulationScript FileReader(String srcFile, boolean onlyAttributes) throws IOException {
		CSVReader CSV = new CSVReader(srcFile);

		SimulationScript temp = new SimulationScript();

		//跳過Key 每日平均抵達病患 模擬時長(分) 亂數種子 是否為自訂劇本 轉送時長 清潔時長
		CSV.readNext();
		//Value
		String[] Line = CSV.readNext();
		//是否為自訂劇本
		boolean Customize = Line[3].equals("1");
		// 轉換成 View 所顯示的位置順序 (模擬時長 每日平均人數 是否為自訂劇本 轉送時長 清潔時長 亂數種子)
		String[] p = { Line[1], Line[0], Line[3], Line[4], Line[5], Line[2] };
		
		temp.setscript_paramater(p);
		temp.setDailyNPeople(Double.valueOf(Line[0]));
		temp.setSimulationTime(Double.valueOf(Line[1]));
		
		// 手術房名稱
		CSV.readNext(); 
		Line = CSV.readNext(); 
		ArrayList<String> tmp = new ArrayList<String>();
		for (String i : Line) {
			if (!i.equals(""))
				tmp.add(i);
		}
		temp.setRoomName(tmp.toArray(new String[tmp.size()]));

		// 科別名稱
		CSV.readNext(); 
		Line = CSV.readNext(); 
		temp.setSubjectName(Line);
		
		// 麻醉名稱
		CSV.readNext(); 
		Line = CSV.readNext(); 
		temp.setAnesthesiaName(Line);

		if (!onlyAttributes) {
			Line = CSV.readNext(); 
			temp.setCalendar_title(Line);

			ArrayList<String[]> dtb = new ArrayList<>();
			while ((Line = CSV.readNext()) != null) {
				if (Customize) {
					Line[2] = "1";
					Line[3] = "0";
					Line[7] = Line[8] = Line[10] = Line[11] = "-1";
					for (int i = 15; i < 22; i++)
						Line[i] = "-1";
				}
				dtb.add(Line);
			}
			temp.setCalendar(dtb);
		}
			
		CSV.close();
		return temp;
	}

	private String[] script_paramater;

	public void setscript_paramater(String[] src) {
		script_paramater = src;
	}

	public String[] getscript_paramater() {
		return script_paramater;
	}

	private String[] calendar_title;

	public void setCalendar_title(String[] src) {
		calendar_title = src;
	}

	public String[] getCalendar_title() {
		return calendar_title;
	}

	public void saveAsFile(String FileName) throws IOException {
		CSVWriter CSV = new CSVWriter(FileName);

		String[] temp = new String[6];
		temp[0] = "每日平均抵達病患"; // "每日平均抵達病患"
		temp[1] = "模擬時長(分)"; // 模擬時長
		temp[2] = "亂數種子"; // 亂數種子
		temp[3] = "是否為自訂劇本";
		temp[4] = "轉送時長";
		temp[5] = "清潔時長";

		CSV.WriteNext(temp);

		temp = new String[6];
		temp[0] = String.valueOf(DailysetNPeople); // 平日
		temp[1] = String.valueOf(SimulationTime); // 模擬時間長度
		temp[2] = String.valueOf(current_rnd); // 亂數種子
		temp[3] = "0"; // 是否為自訂劇本(是為1 否為0)
		temp[4] = String.valueOf(TransferTimelong); // 轉送時長
		temp[5] = String.valueOf(CleanTimelong); // 清潔時長

		CSV.WriteNext(temp);

		temp = new String[1];
		temp[0] = "手術房名稱";
		CSV.WriteNext(temp);
		CSV.WriteNext(RoomName); // 手術室名稱

		temp = new String[1];
		temp[0] = "所有科別名稱"; // 科別名稱
		CSV.WriteNext(temp); // 手術室名稱
		temp = new String[dtb_subject.size() - 1];
		// 第一個為標籤名稱 因此從1開始才有資料
		for (int i = 1; i < dtb_subject.size(); i++)
			temp[i - 1] = dtb_subject.get(i)[0];
		CSV.WriteNext(temp); // 手術室名稱

		temp = new String[1];
		temp[0] = "所有麻醉名稱"; // 科別名稱
		CSV.WriteNext(temp); // 手術室名稱
		temp = new String[dtb_anesthesia.size() - 1];
		// 第一個為標籤名稱 因此從1開始才有資料
		for (int i = 1; i < dtb_anesthesia.size(); i++)
			temp[i - 1] = dtb_anesthesia.get(i)[0];
		CSV.WriteNext(temp); // 手術室名稱

		// 標題
		temp = new String[Attribution.length + StringAttribution.length];
		for (int i = 0; i < temp.length; i++) {
			if (i < Attribution.length)
				temp[i] = Attribution[i];
			else
				temp[i] = StringAttribution[i - Attribution.length];
		}
		CSV.WriteNext(temp);
		// 內容
		for (String[] i : calendar)
			CSV.WriteNext(i);

		CSV.close();
	}

	// Construct
	public SimulationScript() {
		
	}

	public SimulationScript(Distribution src) {
		// 病患編號
		CurNumber = 0;
		// 每日人數
		DailyNPeople = src.getDailyAveg();
		// 每日設置人數
		DailysetNPeople = 0;
		// 每小時平均有幾個人抵達
		RArrivalAveg = src.getRArrivalAveg();
		// 平均每天會有幾個急刀抵達
		EOArrivalDailyAvg = src.getEOArrivalDailyAvg();
		// 每日人數最大值
		DailyMaxNPeople = src.getDailyMax();
		// 每日人數最小值
		DailyMinNPeople = src.getDailyMin();
		// 每日常規平均人數
		DailyRAveg = src.getDailyRAveg();

		// 榮總急刀一級發生機率
		ESurgery = src.getEArrival();
		// 榮總急刀二級病患發生機率
		OSurgery = src.getOArrival();
		// 手術室名稱
		RoomName = src.getRoomName();

		// 各科手術時間統計表 (包含欄位名稱)
		dtb_subject = src.getSurgeryStatictis();
		// 設定科別的產生機率表
		Prob_subject = SetProbability(dtb_subject);

		// 各種麻醉時間統計表 (包含欄位名稱)
		dtb_anesthesia = src.getAnesthesiaStatictis();
		// 設定麻醉的產生機率表
		Prob_anesthesia = SetProbability(dtb_anesthesia);

		// 初始化模擬器 並得到所有事件到達時間
		createScript = new JSSS();
		// 亂數種子補上
		createScript.SETSEE((int) current_rnd);

		// 初始化
		// 下一位急刀病患抵達時間
		nextEOArrivalTime = 0.;
		// 確認下一位病患是否為急刀
		EOCheck = false;
		// 急刀事件產生器
		EO_Eventsss = new JSSS();
		// 設置亂數種子
		EO_Eventsss.SETSEE((int) current_rnd);
		// 常規刀人數
		RArrivalCount = 0;

		// 用於儲存字串屬性
		StringAttributionArr = new ArrayList<String[]>();

		// 隨機選擇手術
		RS_Surgery = new JSSS();
		// 設置亂數種子
		RS_Surgery.SETSEE((int) current_rnd);
		// 隨機手術時間
		RS_SurgeryTime = new JSSS[Prob_subject.length];
		for (int i = 0; i < RS_SurgeryTime.length; i++) {
			RS_SurgeryTime[i] = new JSSS();
			// 設置亂數種子
			RS_SurgeryTime[i].SETSEE((int) current_rnd);
		}

		// 隨機選擇麻醉
		RS_Anesthesia = new JSSS();
		// 隨機麻醉時間
		RS_AnesthesiaTime = new JSSS[Prob_anesthesia.length];
		for (int i = 0; i < RS_AnesthesiaTime.length; i++) {
			RS_AnesthesiaTime[i] = new JSSS();
			// 設置亂數種子
			RS_AnesthesiaTime[i].SETSEE((int) current_rnd);
		}
		// 隨機選擇刀別
		RS_priority = new JSSS();
		// 設置亂數種子
		RS_priority.SETSEE((int) current_rnd);

		// 設定屬性欄數
		createScript.INIQUE(0, Attribution.length, 0); 
	}

	private void primeSSSrnd() {
		// 亂數種子補上
		createScript.SETSEE((int) current_rnd);
		// 設置亂數種子
		EO_Eventsss.SETSEE((int) current_rnd);
		// 設置亂數種子
		RS_Surgery.SETSEE((int) current_rnd);
		for (int i = 0; i < RS_SurgeryTime.length; i++) {
			// 設置亂數種子
			RS_SurgeryTime[i].SETSEE((int) current_rnd);
		}
		for (int i = 0; i < RS_AnesthesiaTime.length; i++) {
			// 設置亂數種子
			RS_AnesthesiaTime[i].SETSEE((int) current_rnd);
		}
		// 設置亂數種子
		RS_priority.SETSEE((int) current_rnd);
	}

	private void getAllREventArrivalTime() {
		// 模擬時間
		double time = 0.;
		// 紀錄模擬天數
		int day = 0;
		// 紀錄模擬的小時區間
		int hour = 0;

		double pt = 0.;

		double count = 0.;

		for (int i = 0; i < 7; i++) {
			if (i < 7) {
				count += RArrivalAveg[i];
				RArrivalAveg[i] = 0;
			}
		}

		RArrivalAveg[7] += count;

		// 設置的平均人數與資料的平均人數比值
		double DailyNPeopleRatio = DailysetNPeople / DailyNPeople;

		ArrivalTimesss = new JSSS();
		ArrivalTimesss.SETSEE((int) current_rnd);
		while (time < SimulationTime) {
			// 計算各數據
			day = (int) time / 1440;
			hour = (int) ((time % 1440) / 60);
			double getarrTime;

			// 取得抵達時間 每小時平均需乘上平均人數比值
			if (RArrivalAveg[hour] != 0) {
				double s = (RArrivalAveg[hour] * DailyNPeopleRatio);
				s += 0.5;
				s = (int) s;
				getarrTime = ArrivalTimesss.EX(60 / s);
			} else
				getarrTime = 99;

			pt += getarrTime;
			if (pt < 60) // 如果事件時間總和沒有超過這一小時的時間 則記錄
			{
				ArrivalTime.add(time + pt);
			} else // 將各數據重置
			{
				pt = 0.;
				time = day * 1440f + (hour + 1f) * 60;
			}
		}
	}

	public void run() {
		// 更新亂數種子
		primeSSSrnd();
		// 取得所有常規刀病患抵達時間
		getAllREventArrivalTime();
		// 抵達如果大於0才代表有急刀病患
		if (EOArrivalDailyAvg > 0) {
			// 設置的平均人數與資料的平均人數比值
			double DailyNPeopleRatio = DailysetNPeople / DailyNPeople;

			// AvgofEventArrivalTime初始化 公式為模擬時間/((模擬時間/一天時間) * 一天之中平均有幾位急刀病患 * 每日平均人數比值)
			AvgofEOEventArrivalTime = SimulationTime
					/ ((SimulationTime / 1440) * EOArrivalDailyAvg * DailyNPeopleRatio);
		} else
			AvgofEOEventArrivalTime = 0;

		// 產生第一個抵達時間
		// 取得第一個事件時間
		double firstEvent = ArrivalTime.get(0);
		// 如每日到達人數為0 平均抵達時間也會為0 因此如果為0則代表不會有急刀抵達則不產生
		if (EOArrivalDailyAvg > 0)
			nextEOArrivalTime = EO_Eventsss.EX(AvgofEOEventArrivalTime);
		// 如第一位病患為急刀病患則使用急刀病患的抵達時間 且製造下一位病患的抵達時間
		if (nextEOArrivalTime < firstEvent && EOArrivalDailyAvg > 0 && nextEOArrivalTime < SimulationTime) {
			// 創建"第一位病患抵達時間"新事件 (起始事件代碼為 1)
			createScript.CREATE(nextEOArrivalTime, CurNumber++);
			// EX函式製作的為抵達時間間隔 因此要將時間加上製作時間點
			double time = EO_Eventsss.EX(AvgofEOEventArrivalTime);
			nextEOArrivalTime += time;
			EOCheck = true; // 為急刀則設置true
		} else {
			createScript.CREATE(firstEvent, CurNumber++); // 創建"第一位病患抵達時間"新事件 (起始事件代碼為 1)
			// 因第一個事件為常規刀所以將常規刀計數器++
			RArrivalCount++;
		}

		// 設置模擬時間為 SimulationTime 分鐘
		createScript.SIMEND(SimulationTime);

		// 跳過第一個事件
		createScript.NEXTEV();
		ARRIVL();
		createScript.DISPOS();

		int ecode;
		do {
			// 取得事件佇列清單中的事件代碼
			if ((ecode = createScript.NEXTEV()) > 0) {
				addCalender();
				ARRIVL();
			}
			createScript.DISPOS();
		} while (ecode != 0);
	}

	private void ARRIVL() {
		createScript.SETA(Time_Code, (int) createScript.T()); // "抵達時間點"(劇本)
		createScript.SETA(ID_Code, createScript.IDE() + 1f); // "病人代號"從1開始算所以+1
		createScript.SETA(Event_Code, createScript.EVC()); // "事件"
		// 刀別 如果此刀為常規刀則設置為0 否則決定急刀一級或二級
		if (EOCheck == false)
			createScript.SETA(Priority_Code, 0);
		else
			createScript.SETA(Priority_Code, getEOPriority());
		createScript.SETA(Subject_Code, getType(Prob_subject, RS_Surgery));// 科別
		createScript.SETA(Anesthesia_Code, getType(Prob_anesthesia, RS_Anesthesia)); // "麻醉方式"
		createScript.SETA(ArrivalTime_Code, (int) createScript.T()); // "抵達時間點"
		createScript.SETA(AnesthesiaBeginTime_Code, -1); // "麻醉開始時間點"
		createScript.SETA(AanesthesiaFinishTime_Code, -1); // "麻醉完成時間點"
		createScript.SETA(AnesthesiaTime_Code, (int) getAnesthesiaTime()); // "麻醉時間長度"
		createScript.SETA(SurgeryBeginTime_Code, -1); // "手術開始時間點"
		createScript.SETA(SurgeryEndTime_Code, -1); // "手術結束時間點"
		createScript.SETA(SurgeryTime_Code, (int) getSurgeryTime()); // "手術時間長度"
		createScript.SETA(TransferTimelong_Code, (int) TransferTimelong); // "轉送時間長度"
		createScript.SETA(CleanTimelong_Code, (int) CleanTimelong); // "清潔時間長度"
		createScript.SETA(RoomNumber_Code, -1); // "手術室別"
		createScript.SETA(WaitBeginTime_Code, -1); // "開始等候時間點"
		createScript.SETA(WaitTime_Code, -1); // "等候的時間長度"
		createScript.SETA(InRoomTime_Code, -1); // 入室時間
		createScript.SETA(AanesthesiaEnd_Code, -1); // 麻醉結束
		createScript.SETA(TransferTime_Code, -1); // "轉送時間"
		createScript.SETA(CleanTime_Code, -1); // "清潔時間"

		// 以下為字串屬性 因屬性code由1開始計算 因此從要-1
		String[] StrAttribution = new String[StringAttribution.length];
		StrAttribution[ApplyID_Code - Attribution.length - 1] = NODATA; // 申請序號
		StrAttribution[PatientName_Code - Attribution.length - 1] = NODATA;// 病患姓名
		StrAttribution[Identity_Code - Attribution.length - 1] = NODATA; // 身分
		StrAttribution[SurgeryName_Code - Attribution.length - 1] = NODATA;// 手術名稱
		// 病歷號
		StrAttribution[patientID_Code - Attribution.length - 1] = String.valueOf(createScript.A(ID_Code));
		// 主治醫師
		StrAttribution[Doctor_Code - Attribution.length - 1] = NODATA;

		StringAttributionArr.add(StrAttribution); // 將屬性加入

		// 如果最後一位抵達時則不增加下一位病患
		if ((RArrivalCount + 1) != ArrivalTime.size()) {
			// 如果這一刀為急刀則不增加常規刀數量
			if (EOCheck == false)
				RArrivalCount++;
			// 將檢查設置為false
			EOCheck = false;
			double nextArriveTime = ArrivalTime.get(RArrivalCount);
			// 如第一位病患為急刀病患則使用急刀病患的抵達時間 且製造下一位病患的抵達時間
			if (nextEOArrivalTime < nextArriveTime && EOArrivalDailyAvg > 0 && nextEOArrivalTime < SimulationTime) {
				double time = EO_Eventsss.EX(AvgofEOEventArrivalTime);
				// 創建"第一位病患抵達時間"新事件 (起始事件代碼為 1)
				createScript.CREATE(nextEOArrivalTime - (int) createScript.T(), CurNumber++);
				// EX函式製作的為抵達時間間隔 因此要將時間加上製作時間點
				nextEOArrivalTime += time;
				// 為急刀則設置true
				EOCheck = true;
			} else
				createScript.CREATE(nextArriveTime - (int) createScript.T(), CurNumber++);
		} else if (nextEOArrivalTime < SimulationTime && EOArrivalDailyAvg > 0) {
			double time = EO_Eventsss.EX(AvgofEOEventArrivalTime);
			// 創建"第一位病患抵達時間"新事件 (起始事件代碼為 1)
			createScript.CREATE(nextEOArrivalTime - (int) createScript.T(), CurNumber++);
			// EX函式製作的為抵達時間間隔 因此要將時間加上製作時間點
			nextEOArrivalTime += time;
			// 為急刀則設置true
			EOCheck = true;
		}
	}

	// 決定急刀一級或是二級
	private int getEOPriority() {
		double x = RS_priority.RA(); // 隨機產生一個浮點數
		double A = ESurgery + OSurgery; // 2個的機率
		double E = ESurgery / A; // 急刀一級

		if (x < E)
			return 2; // 急刀一級回傳 2
		else
			return 1; // 急刀二級回傳 1
	}

	// 決定該病患麻醉時間
	private double getAnesthesiaTime() {
		int Anesthesia = (int) createScript.A(Anesthesia_Code) + 1;
		// 麻醉資料分布
		String[] AType = dtb_anesthesia.get(Anesthesia);

		String distribute = AType[FindTitle(dtb_anesthesia, "分布")];
		double mean = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "平均值")]);
		double std = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "標準差")]);
		double max = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "最大值")]);
		double min = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "最小值")]);
		double model = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "眾數")]);

		double AnesthesiaTime;
		JSSS generator = RS_AnesthesiaTime[(int) createScript.A(Anesthesia_Code)];
		do {
			switch (distribute) {
			case "Exponential":
				AnesthesiaTime = generator.EX(mean);
				break;

			case "Erlang":
				AnesthesiaTime = generator.ER(mean / 2, 2);
				break;

			case "Poisson":
				AnesthesiaTime = generator.NP(mean);
				break;

			case "Triangular":
				AnesthesiaTime = generator.TR(min, model, max);
				break;

			case "Uniform":
				AnesthesiaTime = generator.UN(min, max);
				break;

			default: // 常態分佈
				AnesthesiaTime = generator.RN(mean, std);
			}
		} while (AnesthesiaTime < 0);

		return AnesthesiaTime;
	}

	// 決定科別之手術時間
	private double getSurgeryTime() {
		int subject = (int) createScript.A(Subject_Code) + 1;
		// 取得該科別的參數資料
		String[] SData = dtb_subject.get(subject);

		String distribute = SData[FindTitle(dtb_subject, "分布")];
		double mean = Double.parseDouble(SData[FindTitle(dtb_subject, "平均值")]);
		double std = Double.parseDouble(SData[FindTitle(dtb_subject, "標準差")]);
		double max = Double.parseDouble(SData[FindTitle(dtb_subject, "最大值")]);
		double min = Double.parseDouble(SData[FindTitle(dtb_subject, "最小值")]);
		double model = Double.parseDouble(SData[FindTitle(dtb_subject, "眾數")]);

		double SurgeryTime;
		JSSS generator = RS_SurgeryTime[(int) createScript.A(Subject_Code)];
		do {
			switch (distribute) {
			case "Exponential":
				SurgeryTime = generator.EX(mean);
				break;

			case "Erlang":
				SurgeryTime = generator.ER(mean / 2, 2);
				break;

			case "Poisson":
				SurgeryTime = generator.NP(mean);
				break;

			case "Triangular":
				SurgeryTime = generator.TR(min, model, max);
				break;

			case "Uniform":
				SurgeryTime = generator.UN(min, max);
				break;

			default: // 常態分佈
				SurgeryTime = generator.RN(mean, std);
			}
		} while (SurgeryTime < 0);
		return SurgeryTime;
	}

	// 決定科別或麻醉方式
	private int getType(double[] Table, JSSS generator) {
		double x = generator.RA();
		int Type = 0;
		while (Type < Table.length) {
			if (x >= Table[Type])
				Type++;
			else
				break;
		}
		return Type;
	}
 
	public void setCalendar(ArrayList<String[]> src) {
		calendar = src;
	}

	public ArrayList<String[]> getCalender() {
		return calendar;
	}

	public void addCalender() {
		String[] eventInfo = new String[Attribution.length + StringAttribution.length];
		for (int i = 0; i < Attribution.length; i++) {

			// 如果要填寫的欄位為科別 則將科別名稱填入
			if ((i + 1) == Subject_Code)
				eventInfo[i] = dtb_subject.get((int) createScript.A(Subject_Code) + 1)[0];
			// 如果要填寫的欄位為麻醉 則將麻醉名稱填入
			else if ((i + 1) == Anesthesia_Code)
				eventInfo[i] = dtb_anesthesia.get((int) createScript.A(Anesthesia_Code) + 1)[0];
			else
				eventInfo[i] = String.valueOf(createScript.A(i + 1));
		}
		for (int i = 0; i < StringAttribution.length; i++) {
			// 因病患代號由1開始計算 因此-1
			eventInfo[i + Attribution.length] = StringAttributionArr.get((int) createScript.A(ID_Code) - 1)[i];
		}
		calendar.add(eventInfo);
	}

	private int FindTitle(ArrayList<String[]> Data, String Name) {
		int NumberofColumn = -1;

		if (Data != null) {
			String[] Title = Data.get(0);
			for (int i = 0; i < Title.length; i++) {
				if (Title[i].equals(Name))
					NumberofColumn = i;
			}

			if (NumberofColumn == -1) {
				Main.log(Level.INFO, Name + " 欄位找不到!");
			}
		}

		return NumberofColumn;
	}

	private double[] SetProbability(ArrayList<String[]> Data) {
		int NumberofColumn = FindTitle(Data, "次數");

		if (Data == null || Data.size() <= 1 || NumberofColumn == -1) {
			Main.log(Level.INFO, this.toString().concat("-SetProbability 沒有可用的資料"));
			return new double[0];
		} else {
			// 資料筆數
			int n = Data.size() - 1;
			// 次數總計
			int Total = 0;
			// Data.get(0) 是標題列所以由 1 開始計
			for (int i = 1; i < Data.size(); i++) {
				Total += Integer.parseInt(Data.get(i)[NumberofColumn]);
			}

			// 機率累積表
			double[] Table = new double[n];
			// 全部總發生次數為零 代表大家應該都是一樣的發生機率
			if (Total == 0) {
				Arrays.fill(Table, 1f / n);
			} else {
				Table[0] = Double.parseDouble(Data.get(1)[NumberofColumn]) / Total;
				for (int i = 1; i < Table.length; i++) {
					Table[i] = Double.parseDouble(Data.get(i + 1)[NumberofColumn]) / Total;
					Table[i] += Table[i - 1];
				}
			}
			return Table;
		}
	}
}
