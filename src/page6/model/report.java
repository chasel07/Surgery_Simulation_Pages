package page6.model;
 
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;

import page6.controller.Main;

public class report {  

	private static final String OR_COUNT = "手術房間數";
			
	protected static final String[] colNameOF = { OR_COUNT, "急刀一級", "急刀二級", "常規刀", "總共" };
	
	
	protected static final TreeMap<String, String> ScheduleType = new TreeMap<String, String>(); 
	static {
		ScheduleType.put("BLS", "區塊排程");
		ScheduleType.put("FIFO","先到先服務");
		ScheduleType.put("SVF", "短刀先排");
		ScheduleType.put("BVF", "長刀先排");
	} 
	
	private String rounding = "%.3f";
	
	public final String FN_calender = FileRoute.PATH_CALENDAR;
	public final String FN_report = FileRoute.PATH_DATA + "/report.txt";
	
	// 檔案編號
	protected int FN_number = 0;
	protected ArrayList<String[]> calender;
	
	protected String[] AttributionName = null;
	protected String[] StrAttributionName = null;
 
	// 手術室使用情形統計報告
	protected ArrayList<ArrayList<String>> result;

	/**
	 * 取得某間手術室的使用情形
	 * @param roomId 手術室編號
	 * @return
	 */
	public String getORUsageReport(int roomId) {
		if(roomId < 0 || roomId >= result.size()) 
		{
			return "沒有任何資料";
		}
		
		StringBuilder text = new StringBuilder(); 
		for(String row: result.get(roomId)) { 
			text.append(row);
		}
		
		return text.toString();
	}
	
	
	/**
	 * 取得目前有記錄的手術室清單
	 * @param roomId 手術室編號
	 * @return
	 */
	public ArrayList<String> getORList() {
		ArrayList<String> ORs = new ArrayList<String>();
		
		//加入文字檔中有使用到的手術房編號
		for(ArrayList<String> info: result) {  
			ORs.add(info.get(0)); 
        }  
		
		return ORs;
	}

	// 模擬時間長度
	protected double SimulationTime = 0.0;

	// 設置模擬時間長度
	public void setSimulationTime(double src) {
		SimulationTime = src;
	}

	// 取得模擬時間長度
	public double getSimulationTime() {
		return SimulationTime;
	}

	// 排程模式
	protected String QMode = "FIFO";

	// 設置排程模式
	public void setQMode(String src) {
		QMode = src;
	}

	// 取得排程模式
	public String getQMode() {
		return QMode;
	}

	// 模擬時間單位
	protected String TimeUnit = "分鐘";

	// 設置模擬時間單位
	public void setTimeUnit(String src) {
		TimeUnit = src;
	}

	// 取得模擬時間單位
	public String getTimeUnit() {
		return TimeUnit;
	}

	// 一天的總人數 榮總一天平均有 83.6673058485139 人
	protected double MaxofPeople = 0.0; // 平日
	// 設置每日平均人數

	public void setMaxofPeople(double src) {
		MaxofPeople = src;
	}

	// 取得每日平均人數
	public double getMaxofPeople() {
		return MaxofPeople;
	}

	// 手術房總間數
	protected int RoomAmount = 0;

	// 設置手術房總間數
	public void setRoomAmount(int src) {
		RoomAmount = src;
	}

	// 取得手術房總間數
	public int getRoomAmount() {
		return RoomAmount;
	}

	// 各科別手術時間分布 (包含欄位名稱)
	protected ArrayList<String[]> subject;

	// 設置各科別手術時間分布
	public void setSubjectDistribution(ArrayList<String[]> src) {
		subject = src;
	}

	// 取得各科別手術時間分布
	public ArrayList<String[]> getSubjectDistribution() {
		return subject;
	}

	// 各種麻醉方法之分布 (包含欄位名稱)
	protected ArrayList<String[]> anesthesia;

	// 設置各種麻醉方法之分布 (包含欄位名稱)
	public void setAnesthesiaDistribution(ArrayList<String[]> src) {
		anesthesia = src;
	}

	// 取得各種麻醉方法之分布 (包含欄位名稱)
	public ArrayList<String[]> getAnesthesiaDistribution() {
		return anesthesia;
	}

	// 手術室名稱
	protected String[] RoomName;

	// 設置手術室名稱
	public void setRoomName(String[] src) {
		RoomName = src;
	}

	// 手術室名稱
	public String[] getRoomName() {
		return RoomName;
	}

	// 科別名稱
	protected String[] SubjectName;

	// 設置科別名稱
	public void setSubjectName(String[] src) {
		SubjectName = src;
	}

	// 取得科別名稱
	public String[] getSubjectName() {
		return SubjectName;
	}

	// 麻醉名稱
	protected String[] AnesthesiaName;

	// 設置麻醉名稱
	public void setAnesthesiaName(String[] src) {
		AnesthesiaName = src;
	}

	// 取得麻醉名稱
	public String[] getAnesthesiayName() {
		return AnesthesiaName;
	}

	// 手術房可用數量
	protected int RoomMaxSize = 1;
	// 模擬器 (抵達機率產生)
	protected JSSS sss;
	// 統計等候時間用
	protected JSSS waitTime;
	// 急刀事件產生器
	protected JSSS EO_Eventsss;
	// 各個手術房的工作狀態 TRUE忙碌中 FALSE休息中
	protected boolean[] RoomBusying;
	// 各個手術房的工作狀態 TRUE忙碌中 FALSE休息中
	protected double[] RoomStartTime;
	// 紀錄字串屬性資料陣列
	protected ArrayList<String[]> StringAttributionArr;

	// 麻醉機率表(輪盤法)
	protected double[] AProbability;
	// 科別機率表(輪盤法)
	protected double[] SProbability;
	// 科別分布
	protected Surgery[] SDistribution;
	// 記錄各種緊急程度的手術次數
	protected int R, E, O;
	// 記錄還在等待區的人數
	protected int WE, WO, WR;
	protected int ME, MO, MR;
	protected int DE, DO;
	// 記錄各科別的人數
	protected int[] subjectN;
	// 記錄各種麻醉使用次數
	protected int[] anesthesiaN;

	// 記錄各種事件發生次數
	protected int[] AmountOfEvent;

	// 紀錄加班平均時間
	protected double OverTime;

	// 屬性名稱
	public String[] Attribution = { "時間點", "抵達順序", "事件代碼", "手術級別", "所屬科別", "麻醉名稱", "到達時間", "麻醉開始時間", "麻醉完成時間", "麻醉時長",
			"劃刀時間", "縫合切口", "手術時長", "轉送時長", "清潔時長", "手術房別", "開始等候時間", "等候時長", "病患入室時間", "麻醉結束時間", "轉送時間", "清潔時間" };
	// calendar 擴充欄位
	public String[] StringAttribution = { "申請序號", "病患姓名", "病患身分", "手術名稱", "病歷號", "主治醫師" };

	// SimulationScript 模擬劇本的欄位代號
	public int Time_Code = 1; // "到達時間點"
	public int ID_Code = 2; // "病人代號"
	public int Event_Code = 3; // "事件"
	public int Priority_Code = 4; // "刀別"
	public int Subject_Code = 5; // "所屬科別"
	public int Anesthesia_Code = 6; // "麻醉方式"
	public int ArrivalTime_Code = 7; // "抵達時間"
	public int AnesthesiaBeginTime_Code = 8; // "麻醉開始時間點"
	public int AanesthesiaFinishTime_Code = 9; // "麻醉結束時間點"
	public int AnesthesiaTime_Code = 10; // "麻醉時間長度",
	public int SurgeryBeginTime_Code = 11; // "手術開始時間點"
	public int SurgeryEndTime_Code = 12; // "手術開始時間點"
	public int SurgeryTime_Code = 13; // "手術開始長度"
	public int TransferTimelong_Code = 14; // "轉送時間長度"
	public int CleanTimelong_Code = 15; // "清潔時間長度"
	public int RoomNumber_Code = 16; // "手術房號"
	public int WaitBeginTime_Code = 17; // "開始等待時間點"
	public int WaitTime_Code = 18; // "等待時間長度
	public int InRoomTime_Code = 19; // "入室時間"
	public int AanesthesiaEnd_Code = 20; // "麻醉結束"
	public int TransferTime_Code = 21; // "轉送時間"
	public int CleanTime_Code = 22; // "清潔時間"
	public int ApplyID_Code = 23; // 申請序號
	public int PatientName_Code = 24; // 病患姓名
	public int Identity_Code = 25; // 身分
	public int SurgeryName_Code = 26; // 手術名稱
	public int patientID_Code = 27; // 病歷號
	public int Doctor_Code = 28; // 主治醫師

	public report() {
		OperationFinish = new ArrayList<>();
		OperationUtilization = new ArrayList<>();
		MaxWaiting = new ArrayList<>();
		AverageWaiting = new ArrayList<>();
		Room_OverTime = new ArrayList<>(); 
	}

	/**
	 * 初始化日誌檔
	 * 
	 * @param FileName 檔名
	 * @param src 原有屬性名稱來源
	 * @param src2 擴充屬性名稱來源
	 * @throws IOException 
	 */
	public void setCalender(String[] src, String[] src2) {
		AttributionName = src;
		StrAttributionName = src2;
		
		String[] eventInfo = new String[src.length + src2.length];
		for (int i = 0; i < src.length; i++) 
		{
			eventInfo[i] = src[i];
		}	
		for (int i = 0; i < src2.length; i++) 
		{
			eventInfo[i + src.length] = src2[i];
		}
			
		calender = new ArrayList<>();
		calender.add(eventInfo);
	}
	
	/**
	 * 取得日誌
	 * @return
	 */
	public ArrayList<String[]> getCalender() {
		return calender;
	}
	
	/**
	 * 補上文字註解用
	 * @param text 註解
	 */
	public void markCalender(String text) {
		String[] commentLine = { text };
		calender.add(commentLine);
	}

	/**
	 * 記錄模擬過程
	 *   --因report無法獲得ID位置 所以需要輸入"病人代號(ID_CODE)" 
	 */
	public void addCalender() {
		//紀錄(原有屬性 + 擴充屬性)
		String[] eventInfo = new String[AttributionName.length + StrAttributionName.length];
		
		for (int i = 1; i <= AttributionName.length; i++) {
			
			if (i == Subject_Code) //代換科別名稱(英文縮寫)
			{
				eventInfo[i - 1] = SubjectName[(int) sss.A(Subject_Code)];
			} 
			else if (i == Anesthesia_Code) //代換麻醉名稱(英文縮寫)
			{
				eventInfo[i - 1] = AnesthesiaName[(int) sss.A(Anesthesia_Code)];
			} 
			else if (i == RoomNumber_Code) //代換手術室名稱(英文縮寫)
			{
				int Room = (int) sss.A(RoomNumber_Code);
				if (Room == -1)
				{
					eventInfo[i - 1] = String.valueOf(-1); 
				}
				else
				{
					eventInfo[i - 1] = String.valueOf(RoomName[Room]); 
				}
			} 
			else 
			{
				eventInfo[i - 1] = String.valueOf(sss.A(i)); 
			}
		}
		for (int i = 0; i < StrAttributionName.length; i++) {
			// 因病患代號由1開始計算 因此-1
			eventInfo[i + AttributionName.length] = StringAttributionArr.get((int) sss.A(ID_Code) - 1)[i];
		}
		
		calender.add(eventInfo); 
	}

	/**
	 * 匯出日誌檔
	 * 
	 * @throws IOException
	 */
	public void printCalender(String FileName) throws IOException {
		CSVWriter CSV_calender = new CSVWriter(FileName); 
		for(String[] dataRow: calender) {
			CSV_calender.WriteNext(dataRow); 
		} 
		CSV_calender.close();
	}
	 
	/**
	 * 匯出模擬結果
	 * @throws IOException
	 */
	protected void printReport() throws IOException {
		// 報告總表 
		try(BufferedWriter BW_report = new BufferedWriter(new FileWriter(FN_report))) {
			BW_report.write("排程模式：");
			BW_report.write(ScheduleType.get(QMode)); 
			
			BW_report.write("\n\n模擬時間長度：");
			BW_report.write(String.valueOf(SimulationTime));
			BW_report.write(" ");
			BW_report.write(TimeUnit);
			
			BW_report.write("\n\n手術房總間數：");
			BW_report.write(String.valueOf(RoomAmount));
			
			BW_report.write("\n\n每日平均總人數：");
			BW_report.write(String.valueOf(MaxofPeople));
			BW_report.write("\n\n");
			
			int STATN = sss.getSTATN();
			for (int i = 0; i < STATN; i++) 
			{
				ArrayList<String> info = sss.SUMRY(i + 1);
				if (info != null) 
				{
					for (String text : info)
					{
						BW_report.write(text);
					}
					BW_report.write("\n");
					result.add(info);
				} 
				else 
				{
					BW_report.write("空白 - 因為沒有任何紀錄可進行統計!!\n\n");
				}
			}
			 
			BW_report.flush();
			BW_report.close(); 
		} 
	}

	public void addAllReport() throws IOException {
		addOperationUtilization();
		addOperationFinish();
		addAverageWaiting();
		addMaxWaiting();
		addRoom_OverTime();
	}

	public void resetReport() throws IOException { // 初始化儲存圖表資料的陣列 
		result = new ArrayList<>();

		resetOperationUtilization();
		resetOperationFinish();
		resetAverageWaiting();
		resetMaxWaiting();
		resetRoom_OverTime();
	}
 
	public int getRoomMaxSize() {
		return RoomMaxSize;
	}

	public String getCE() {
		return String.valueOf(E - WE);
	}

	public String getCO() {
		return String.valueOf(O - WO);
	}

	public String getCR() {
		return String.valueOf(R - WR);
	}

	public String getAC() {
		return String.valueOf((E - WE) + (O - WO) + (R - WR));
	}

	public String getMR() {
		return String.valueOf(MR);
	}

	public String getME() {
		return String.valueOf(ME);
	}

	public String getMO() {
		return String.valueOf(MO);
	}

	public ArrayList<String[]> getOperationFinish() {
		return OperationFinish;
	}

	public ArrayList<String[]> getOperationUtilization() {
		return OperationUtilization;
	}

	public ArrayList<String[]> getMaxWaiting() {
		return MaxWaiting;
	}

	public ArrayList<String[]> getAverageWaiting() {
		return AverageWaiting;
	}

	public String getAverageUtilization() {
		// 計算所有手術房的平均利用率
		double average = 0.0;
		if (sss != null) {
			for (int i = 1; i <= RoomMaxSize; i++) {
				average += sss.SSUM(i) / SimulationTime;
			}
			average /= RoomMaxSize;
		}
		return String.format(rounding, average);
	}

	public String getAverageOfOverTime() {
		// 計算手術房的平均加班時間
		double average = 0.0;
		if (OverTime != 0.) {
			average = OverTime;
			average /= RoomMaxSize;
		}
		return String.format(rounding, average);
	}

	// 回傳事件發生的次數
	public String getAmountOfEvent(int index) {
		if (index >= 0 && index < AmountOfEvent.length) {
			return String.valueOf(AmountOfEvent[index]);
		} else {
			Main.log(Level.WARNING, "getAmountOfEvent - range out");
			return null;
		}
	}

	// 回傳等候區的平均等待時間
	public String getAverageWaitTime(int index) {
		if (waitTime == null)
			return "";
		return String.format(rounding, waitTime.SAVG(index));
	}
	
	// -------------------------------------------------------------------------------------
	// 完成手術人數----------------------------------------------------------------------------

	private ArrayList<String[]> OperationFinish;

	public void addOperationFinish() throws IOException {
		String[] temp = new String[5];
		temp[0] = String.valueOf(getRoomMaxSize());
		temp[1] = getCE();
		temp[2] = getCO();
		temp[3] = getCR();
		temp[4] = getAC();
		OperationFinish.add(temp);
	}

	public void resetOperationFinish() { 
		OperationFinish.clear();
		OperationFinish.add(colNameOF);
	}

	// ----------------------------------------------------------------------------------
	// 加班時間----------------------------------------------------------------------------
	private ArrayList<String[]> Room_OverTime;

	public ArrayList<String[]> getRoom_OverTime() {
		return Room_OverTime;
	}

	public void resetRoom_OverTime() {
		String[] RO = {OR_COUNT, "平均加班時間" };
		Room_OverTime.clear();
		Room_OverTime.add(RO);
	}

	public void addRoom_OverTime() throws IOException {
		String[] temp = new String[2];
		temp[0] = String.valueOf(getRoomMaxSize());
		temp[1] = getAverageOfOverTime();
		Room_OverTime.add(temp);
	}

	// ----------------------------------------------------------------------------------------
	// 手術房平均利用率----------------------------------------------------------------------------
	private ArrayList<String[]> OperationUtilization;

	public void resetOperationUtilization() {
		String[] OU = { OR_COUNT, "利用率" };
		OperationUtilization.clear();
		OperationUtilization.add(OU);
	}

	public void addOperationUtilization() throws IOException {
		String[] temp = new String[2];
		temp[0] = String.valueOf(getRoomMaxSize());
		temp[1] = getAverageUtilization();
		OperationUtilization.add(temp);
	}

	// -------------------------------------------------------------------------------------
	// 病患最長等候時間-------------------------------------------------------------------------
	private ArrayList<String[]> MaxWaiting;

	public void resetMaxWaiting() {
		String[] W = { OR_COUNT, "急刀一級", "急刀二級", "常規刀" };
		MaxWaiting.clear();
		MaxWaiting.add(W);
	}

	public void addMaxWaiting() throws IOException {
		String[] temp = new String[4];
		temp[0] = String.valueOf(getRoomMaxSize());
		temp[1] = getME();
		temp[2] = getMO();
		temp[3] = getMR();
		MaxWaiting.add(temp);
	}

	// -------------------------------------------------------------------------------------
	// 病患平均等候時間-------------------------------------------------------------------------
	private ArrayList<String[]> AverageWaiting;

	public void resetAverageWaiting() {
		String[] W = { OR_COUNT, "急刀一級", "急刀二級", "常規刀" };
		AverageWaiting.clear();
		AverageWaiting.add(W);
	}

	public void addAverageWaiting() throws IOException {
		String[] temp = new String[4];
		temp[0] = String.valueOf(getRoomMaxSize());
		temp[1] = getAverageWaitTime(1);
		temp[2] = getAverageWaitTime(2);
		temp[3] = getAverageWaitTime(3);
		AverageWaiting.add(temp);
	}
}