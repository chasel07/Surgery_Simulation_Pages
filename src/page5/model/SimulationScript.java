package page5.model;

import java.io.IOException; 
import java.util.ArrayList; 
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
	
	/**
	 * 亂數種子
	 * @param src
	 */
	public void setcurrent_rnd(long src) {
		current_rnd = src;
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
		//Do nothing.
	}
	public void setCalendar(ArrayList<String[]> src) {
		calendar = src;
	}

	public ArrayList<String[]> getCalender() {
		return calendar;
	}
}
