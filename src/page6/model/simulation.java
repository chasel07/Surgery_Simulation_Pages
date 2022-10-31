package page6.model;
 
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level; 
import java.time.LocalDate;

import page6.controller.Main;

//時間單位：分鐘 
public class simulation extends report {	   
	  
	//calender劇本資料
	private ArrayList<String[]> CalenderData;
	//病歷號
	private int CurNumber = 1; 
	//
	private int ReadCalenderTimes;
	
	//使用者指定的手術室數量
	private int MaxRoomN = 0; 	  	  
	//設置使用者指定的手術室數量
	public void setMaxRoomN(int src) { MaxRoomN = src; }
	//取得使用者指定的手術室數量
	public int getMaxRoomN() { return MaxRoomN; }
	 
	//急刀專屬手術室個數
	private int EORoomN = 0; 	  	  
	//設置急刀專屬間數
	public void setEORoomN(int src) { EORoomN = src; }
	//取得急刀專屬間數
	public int  getEORoomN() { return EORoomN; }
	
	 	
	public simulation() { super(); } 
	
	public simulation(SimulationScript src) { 
		super();
		setParameter(src);
	} 
	
	public simulation(SimulationScript src, SimulationParametersModel model) { 
		super();
		setParameter(src);
		this.QMode = model.getQmode(); 
		this.MaxRoomN = model.getRoomMaxN();  
		this.EORoomN = model.getEORoomN();   
	} 
	 
	
	private boolean isRunable() {
		
		if(EORoomN >= RoomMaxSize)
		{ 
			Main.log(Level.INFO, "至少保留一間可供給常規刀使用！");
			return false; 
		} 
		else if(EORoomN < 0) 
		{
			Main.log(Level.INFO, "急刀專屬間數(EORoomN)必須輸入非負數的值!!"); 
			return false;
		} 
		else if(QMode.equals("BLS") && RoomMaxSize < 6) 
		{ 
			Main.log(Level.INFO, "區塊排程模式下將跳過不足六間的模擬!!"); 
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * 模擬不同手術室數量的手術室使用情形
	 * 
	 * @throws IOException
	 */
	public void run() throws IOException
	{   
		resetReport();
		
		for(RoomMaxSize = 1; RoomMaxSize <= RoomAmount; RoomMaxSize++) 
		{ 	
			if(isRunable())  
			{
				prime();  
				simulateProcess(); 
				summarize();  
			}
		}  
	}   

	/** 
	 * 結算手術房最後的總使用時間 
	 * @throws IOException 
	 */  
	private void summarize() throws IOException { 
		
		for(int i = 0; i < RoomMaxSize; i++) { 
			if(RoomBusying[i] == true)
				sss.TALLY(i + 1, SimulationTime - RoomStartTime[i]);  
		} 
		
		//事件清空
		while(sss.NEXTEV() != 0) {
			sss.DISPOS();
		} 
		
		//結算最後還在等待區的病患等待時間
		markCalender("統計急刀佇列還在等候的人數");
		while(sss.NQ(1) > 0) {
			sss.DISPOS();
			sss.REMVFQ(1, 1);
			sss.SETA(WaitTime_Code, SimulationTime - sss.A(WaitBeginTime_Code));
			switch((int)sss.A(Priority_Code)) {
				case 2: waitTime.TALLY(1, sss.A(WaitTime_Code)); 
						WE++;	//統計急刀一級還在等候的人數
						break;  
				case 1: waitTime.TALLY(2, sss.A(WaitTime_Code));
						WO++; 	//統計急刀二級還在等候的人數
						break; 
				default:
			} 
			addCalender();
		}   
		WR = sss.NQ(2);  
		markCalender("統計常規刀佇列還在等候的人數");
		while(sss.NQ(2) > 0) {  
			sss.DISPOS();
			sss.REMVFQ(2, 1);
			sss.SETA(WaitTime_Code, SimulationTime - sss.A(WaitBeginTime_Code)); 
			waitTime.TALLY(3, sss.A(WaitTime_Code)); 
			addCalender();
		}  
		
		addAllReport();
		
		if(RoomMaxSize == MaxRoomN) { 
			//輸出事件紀錄 (檔名將附加上使用間數及日期) 
			String fileName = String.format("%s/calender_%s_%s.csv", FN_calender, RoomMaxSize, LocalDate.now()); 
			printCalender(fileName); 
			
			printReport(); 
		}
	}


	private void simulateProcess() throws IOException {  
		//event code 
		int ecode; 
		do {
			ecode = sss.NEXTEV();
			if(ecode > 0) { //取得事件佇列清單中的事件代碼						
				AmountOfEvent[ecode - 1]++;  //某事件次數紀錄加一
				switch(ecode) { 
					case 1: ARRIVL(); break;
					case 2: NEXTAC(); break; 
					case 3: STARTA(); break; 
					case 4: ENDACT(); break; 
					case 5: CLEAN(); break; 
					default:
				}						
				sss.DISPOS(); 
			}  	
		} while (ecode != 0);   
	}
 
	/**
	 * 初始化函數 
	 */
	private void prime() throws IOException {    
		//本次執行讀取 Calendar 的次數
		ReadCalenderTimes = 0;
		 
		setCalender(Attribution , StringAttribution);
		
		/*改變作法
		SProbability = SetProbability(subject);		//設定科別的產生機率表
		AProbability = SetProbability(anesthesia);	//設定麻醉的產生機率表
		*/ 
		RoomBusying = new boolean[RoomMaxSize];  //用來判斷手術室是否正在使用中
		Arrays.fill(RoomBusying, false);  //初始化陣列 - false 代表未使用
		
		RoomStartTime = new double[RoomMaxSize]; //用來暫存目前正在手術室中的手術開始時間點
		
		CurNumber = 0;
		R = E = O = WR = WE = WO = MR = ME = MO = DE = DO = 0;
		
		//加班時間
		OverTime = 0.;
		
		AmountOfEvent = new int[5];
		Arrays.fill(AmountOfEvent, 0);
	
		//字串屬性紀錄陣列初始化
		StringAttributionArr = new ArrayList<String[]>();	
		
		//建立模擬器 
		sss = new JSSS();
		//統計等待時間用
		waitTime = new JSSS();	
		/* 急刀產生器初始化
		 * 屬性：是否急刀, 所屬科別, 麻醉方式, 開始等候時間, 等候時間長度, 手術室號碼, 手術總時間 
		 * 1. 急刀佇列
		 * 2. 常規刀佇列
		 * 預設為先進先出
		 */
		EO_Eventsss = new JSSS();	
		
		//若為區塊排程
		if(QMode.equals("BLS"))
		{ 
			//設定佇列數量、屬性欄位數量、手術時間統計表數量
			sss.INIQUE(RoomMaxSize, Attribution.length, RoomMaxSize);
		}
		else 
		{	
			//設定佇列數量、屬性欄位數量、手術時間統計表數量
			sss.INIQUE(2, Attribution.length, RoomMaxSize); 
			sss.SETQDC(1, "BVF"); //設置為最大優先權佇列 (急刀)
			sss.SETQDC(2, QMode); //決定常規刀的排程方式
		}
		
		waitTime.INIQUE(0, 0, 3);
		waitTime.INISTA(1, "急刀一級等候時間統計", 0, 24, 60, 60);  
		waitTime.INISTA(2, "急刀二級等候時間統計", 0, 24, 60, 60);  
		waitTime.INISTA(3, "常規刀等候時間統計" , 0, 24, 60, 60);   
		
		//統計已知的手術室之開刀情形
		for(int i = 0; i < RoomMaxSize; i++) {	
			sss.INISTA(i + 1, RoomName[i], 0, 24, 60, 60);
		}
		
		//第一個事件產生的時間間隔
		double firstEvent = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[Time_Code-1]);
		//創建 "第一位病患抵達時間" 新事件 (起始事件代碼為 1)
		sss.CREATE(firstEvent, CurNumber++);  
		// 設置模擬時間長度為 SimulationTime 分鐘
		sss.SIMEND(SimulationTime); 
	}											 
	
	/**
	 * 抵達事件
	 * 
	 * @throws IOException
	 */
	void ARRIVL() throws IOException {  
		readPatientData();
		
		//統計刀別次數
		switch((int)sss.A(Priority_Code)) {
			case 2: E++; break;
			case 1: O++; break;
			case 0: R++; break;
			default:
		} 
		
		sss.SETA(Event_Code, 1); 
		 
		int RoomNumber = AvailableRoom((int)sss.A(Priority_Code)); 
		if(RoomNumber != -1) {				
			sentToOperatingRoom(RoomNumber);
		}	
		else {   
			arrangeToWaitingArea();
		}	  
	}
	
	void STARTA() throws IOException {  
		//設置事件代碼
		sss.SETA(Event_Code , sss.EVC());	
		//設置現在時間點
		sss.SETA(Time_Code,(int)sss.T());
		//手術時間
		double SurgeryTime = sss.A(SurgeryTime_Code);
		//麻醉結束等待時間(固定5分鐘)+手術時間
		addCalender();
		sss.SCHED(5 + SurgeryTime, 4, (int)sss.A(ID_Code));
	} 
	
	void ENDACT() throws IOException {  
		//設置事件代碼
		sss.SETA(Event_Code , sss.EVC());	
		//設置現在時間點
		sss.SETA(Time_Code,(int)sss.T());
		//等待轉送和清潔時間
		addCalender();
		sss.SCHED(sss.A(TransferTimelong_Code) + sss.A(CleanTimelong_Code), 5, (int)sss.A(ID_Code));   
		
	} 
	 
	void NEXTAC() throws IOException { 
		sss.SETA(Event_Code , sss.EVC());	//設置事件代碼
		int RoomNumber = (int)sss.A(RoomNumber_Code);
		if(RoomNumber == -1) { 	
			//設置現在時間點
			sss.SETA(Time_Code,(int)sss.T());
			sss.SETA(WaitTime_Code, (int)sss.T() - sss.A(WaitBeginTime_Code));//, "等候時間長度"
			sss.SETA(RoomNumber_Code, AvailableRoom((int)sss.A(Priority_Code))); // "設置手術室號碼" 
			//麻醉開始與入室時間的時間點設置(進入手術室時間) 
			sss.SETA(AnesthesiaBeginTime_Code, (int)sss.T());
			sss.SETA(InRoomTime_Code,  (int)sss.T()); 
			//麻醉結束的時間點設置(進入手術室時間+麻醉時間)
			sss.SETA(AanesthesiaFinishTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code));
			//手術開始的時間點設置(進入手術室時間+麻醉時間+麻醉結束等待時間(固定5分鐘))
			sss.SETA(SurgeryBeginTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + 5 );
			//手術結束的時間點設置(進入手術室時間+麻醉時間+手術時間)
			sss.SETA(SurgeryEndTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code));
			//麻醉結束的時間點設置(進入手術室時間+麻醉時間+手術時間+麻醉結束等待時間(固定5分鐘))
			sss.SETA(AanesthesiaEnd_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5);
			//轉送時間的時間點設置(進入手術室時間+麻醉時間+手術時間+麻醉結束等待時間(固定5分鐘)+轉送時間長度)
			sss.SETA(TransferTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5 + sss.A(TransferTimelong_Code));
			//清潔時間的時間點設置(進入手術室時間+麻醉時間+手術時間+麻醉結束等待時間(固定5分鐘)+轉送時間長度+清潔時間長度)
			sss.SETA(CleanTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5 + sss.A(TransferTimelong_Code) + sss.A(CleanTimelong_Code));	
			//統計各種刀別的等待時間 
			switch((int)sss.A(Priority_Code)) {
				case 2: waitTime.TALLY(1, sss.A(WaitTime_Code)); break; //急刀一級
				case 1: waitTime.TALLY(2, sss.A(WaitTime_Code)); break;//急刀二級
				case 0: waitTime.TALLY(3, sss.A(WaitTime_Code)); break; //常規刀
				default:
			} 
		} 		
		addCalender();
		sss.SCHED(sss.A(AnesthesiaTime_Code), 3, (int)sss.A(ID_Code));
	}  
	
	void CLEAN() throws IOException { 
		sss.SETA(Event_Code , sss.EVC());	//設置事件代碼
		//設置現在時間點
		sss.SETA(Time_Code,(int)sss.T());
		int RoomNumber = (int)sss.A(RoomNumber_Code);
		RoomBusying[RoomNumber] = false;
		//得到入房時間與出房時間
		double InRoomTime = sss.A(AnesthesiaBeginTime_Code);
		double OutRoomTime = (int)sss.T();
		int day =(int)( InRoomTime / 1440);
		//判斷是否有加班 如果有加班則將加班時間增加到OverTime
		if(InRoomTime > (day * 1440 + 1020)) 
		{
			OverTime += (OutRoomTime - InRoomTime);
		}
		else if(InRoomTime < (day * 1440 + 1020) && OutRoomTime > ( day * 1440 + 1020)) 
		{
			OverTime += (OutRoomTime - (day * 1440 + 1020));
		}
		else if(InRoomTime < (day * 1440 + 480) && OutRoomTime > ( day * 1440 + 480))
		{
			OverTime += ((day * 1440 + 480) - InRoomTime);
		}
		else if(InRoomTime < (day * 1440 + 480) && OutRoomTime < ( day * 1440 + 480))
		{
			OverTime += (OutRoomTime - InRoomTime);
		}
		
		
		//"紀錄手術花費總時間(含麻醉時間長度、手術時間長度、麻醉結束等待時間)"
		sss.TALLY(RoomNumber + 1, sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5);
		sss.DISPOS();
		if(sss.NQ(1) > 0) { 
			sss.REMVFQ(1, 1);
			if(sss.A(Priority_Code) == 2) DE--;
			if(sss.A(Priority_Code) == 1) DO--;
			//(CLEAN -> NEXTAC) 將急刀等候區的病患 %d 立即送至手術室進行麻醉
			addCalender();
			sss.SCHED(0, 2, (int)sss.A(ID_Code));
		}
		else if(sss.NQ(2) > 0 && RoomNumber >= EORoomN) {     
			sss.REMVFQ(2, 1);  		
			//(CLEAN -> NEXTAC) 將一般等候區的病患 %d 立即送至手術室進行麻醉
			addCalender();
			sss.SCHED(0, 2, (int)sss.A(ID_Code));   
		}
	}  
	
	
	private void readPatientData() {
		int ID = 0;				//病患ID
		int eventcode = 0;  	//事件代碼
		int Priority = 0;		//刀別
		int subject = 0;		//所屬科別
		int anesthesia = 0;		//麻醉方式 5
		//double ArrivalTime = 0;		//"抵達時間點" 暫時不需要 因抵達時間點使用抵達事件時間
		double AnesthesiaBeginTime = 0;	//麻醉開始時間點
		double AanesthesiaFinishTime = 0;//麻醉結束時間點
		double AnesthesiaTime = 0;		//麻醉時間長度
		double SurgeryBeginTime = 0;	//手術開始時間點 10
		double SurgeryEndTime = 0;		//手術結束時間點
		double SurgeryTime = 0;			//手術時間長度
		double TransferTimelong = 0;	//轉送時間
		double CleanTimelong = 0;		//清潔時間
		int RoomNumberData = 0;			//手術房號碼 15
		double WaitBeginTime = 0;		//開始等待時間點
		double WaitTime = 0;			//等待時間長度
		double InRoomTime = 0;			//入室時間
		double AanesthesiaEnd = 0;		//麻醉結束
		double TransferTime = 0;		//轉送時間 20
		double CleanTime = 0;			//清潔時間 
		String ApplyID = "";			//申請序號
		String PatientName = "";		//病患姓名
		String Identity = "";			//身分
		String SurgeryName = "";		//手術名稱 25
		String patientID = "";			//病歷號		
		String Doctor = "";				//主治醫師	
		 
		if((ReadCalenderTimes) < CalenderData.size()) 
		{ 
			ID = CurNumber;
			//代碼從1開始 但資料從0開始 因此要減1
			//讀取事件代碼 因為劇本上的值為浮點數因此需要資料型態轉換
			eventcode = (int)Double.parseDouble(CalenderData.get(ReadCalenderTimes)[Event_Code-1]);
			//讀取刀別 因為劇本上的值為浮點數因此需要資料型態轉換
			Priority = (int)Double.parseDouble(CalenderData.get(ReadCalenderTimes)[Priority_Code-1]);
			//讀取所屬科別
			String Subjectstr = CalenderData.get(ReadCalenderTimes)[Subject_Code-1];
			//找出相對應科別名稱的索引值
			for(int i = 0 ; i < SubjectName.length ; i++) {
				if(SubjectName[i].equals(Subjectstr)) 
				{
					subject = i ;
					break;
				}
			}
			//讀取麻醉方式
			String Anesthesiastr = CalenderData.get(ReadCalenderTimes)[Anesthesia_Code-1];
			//找出相對應麻醉名稱的索引值
			for(int i = 0 ; i < AnesthesiaName.length ; i++)
				if(AnesthesiaName[i].equals(Anesthesiastr)) 
				{
					anesthesia = i ;
					break;
				}
			
			//如果某些值需要為固定預設值 則使用getPresetCalenderValue判斷輸入值是否正確
			
			//讀取麻醉開始時間點
			AnesthesiaBeginTime = getPresetCalenderValue(AnesthesiaBeginTime_Code-1);
			//讀取麻醉結束時間點
			AanesthesiaFinishTime = getPresetCalenderValue(AanesthesiaFinishTime_Code-1);
			//讀取麻醉時間長度
			AnesthesiaTime = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[AnesthesiaTime_Code-1]);
			//讀取手術開始時間點
			SurgeryBeginTime = getPresetCalenderValue(SurgeryBeginTime_Code-1);
			//讀取手術結束時間點
			SurgeryEndTime = getPresetCalenderValue(SurgeryEndTime_Code-1);
			//讀取手術時間長度
			SurgeryTime = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[SurgeryTime_Code-1]);
			//讀取轉送時間
			TransferTimelong = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[TransferTimelong_Code-1]);
			//讀取清潔時間
			CleanTimelong = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[CleanTimelong_Code-1]);
			//讀取手術室號碼
			RoomNumberData = (int)getPresetCalenderValue(RoomNumber_Code-1);
			//讀取開始等待時間點
			WaitBeginTime = getPresetCalenderValue(WaitBeginTime_Code-1);
			//讀取等待時間長度
			WaitTime = getPresetCalenderValue(WaitTime_Code-1);
			//讀取入室時間
			InRoomTime = getPresetCalenderValue(InRoomTime_Code-1);
			//讀取麻醉結束
			AanesthesiaEnd = getPresetCalenderValue(AanesthesiaEnd_Code-1);	
			//讀取轉送時間
			TransferTime = getPresetCalenderValue(TransferTime_Code-1);		
			//讀取清潔時間
			CleanTime = getPresetCalenderValue(CleanTime_Code-1);			
			
			//讀取手術名稱
			SurgeryName = CalenderData.get(ReadCalenderTimes)[SurgeryName_Code-1];
			//讀取申請序號
			ApplyID = CalenderData.get(ReadCalenderTimes)[ApplyID_Code-1];
			//讀取病患姓名
			PatientName = CalenderData.get(ReadCalenderTimes)[PatientName_Code-1];
			//讀取身分
			Identity = CalenderData.get(ReadCalenderTimes)[Identity_Code-1];
			//讀取病歷號
			patientID = CalenderData.get(ReadCalenderTimes)[patientID_Code-1];	
			//讀取主治醫師
			Doctor = CalenderData.get(ReadCalenderTimes)[Doctor_Code-1];	
			
			sss.SETA(Time_Code , (int)sss.T()); 						//"時間點"
			sss.SETA(ID_Code , ID); 									//"病人代號"
			sss.SETA(Event_Code , eventcode); 							//"事件"
			sss.SETA(Priority_Code , Priority); 						//"刀別" 
			sss.SETA(Subject_Code , subject); 							//"所屬科別" 5
			sss.SETA(Anesthesia_Code , anesthesia); 					//"麻醉方式"
			sss.SETA(ArrivalTime_Code , (int)sss.T()); 					//"抵達時間點"
			sss.SETA(AnesthesiaBeginTime_Code , AnesthesiaBeginTime); 	//"麻醉開始時間點"
			sss.SETA(AanesthesiaFinishTime_Code , AanesthesiaFinishTime);//"麻醉結束時間點"
			sss.SETA(AnesthesiaTime_Code, AnesthesiaTime);				//"麻醉時間長度" 10
			sss.SETA(SurgeryBeginTime_Code, SurgeryBeginTime); 			//"手術開始時間點"
			sss.SETA(SurgeryEndTime_Code, SurgeryEndTime); 				//"手術結束時間點"
			sss.SETA(SurgeryTime_Code, SurgeryTime); 					//"手術時間長度"
			sss.SETA(TransferTimelong_Code, TransferTimelong);		 	//"轉送時間長度"
			sss.SETA(CleanTimelong_Code, CleanTimelong); 		 		//"清潔時間長度" 15
			sss.SETA(RoomNumber_Code, RoomNumberData); 					//"手術室號碼"
			sss.SETA(WaitBeginTime_Code, WaitBeginTime); 				//"開始等候時間點" 
			sss.SETA(WaitTime_Code, WaitTime); 							//"等候的時間長度" 
			sss.SETA(InRoomTime_Code, InRoomTime); 						//入室時間
			sss.SETA(AanesthesiaEnd_Code, AanesthesiaEnd); 				//麻醉結束 20
			sss.SETA(TransferTime_Code, TransferTime); 					// "轉送時間"
			sss.SETA(CleanTime_Code, CleanTime); 						// "清潔時間"
			
			//因字串屬性不能放入A之中因此多設置一個字串陣列ArrayList
			//因屬性code由1開始計算 因此從要-1
			String[] StrAttribution = new String[StringAttribution.length];
			StrAttribution[ApplyID_Code - Attribution.length -1] = ApplyID;			//申請序號
			StrAttribution[PatientName_Code - Attribution.length -1] = PatientName;	//病患姓名
			StrAttribution[Identity_Code - Attribution.length -1] = Identity;		//身分 25
			StrAttribution[SurgeryName_Code - Attribution.length -1] = SurgeryName;	//手術名稱
			StrAttribution[patientID_Code - Attribution.length -1] = patientID;		//病歷號
			StrAttribution[Doctor_Code - Attribution.length -1] = Doctor;			//主治醫師 28
			StringAttributionArr.add(StrAttribution);	
			
			//TODO ReadCalenderTimes 直接加1可能導致最後一筆資料沒有被處理到 (尚未確定)
			//如果到最後一筆則不繼續讀取且設定下一位病患抵達時間
			if(++ReadCalenderTimes < CalenderData.size()) 
			{
				double nextArriveTime = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[Time_Code-1]);
				sss.CREATE(nextArriveTime - (int)sss.T(), CurNumber++);
			} 
		} 
	}
	
	
	private int AvailableRoom(int type){	
		int i = (type >= 1) ? 0 : EORoomN;
		//限急刀使用的手術室編號為 0 ~ (EORoomN - 1)
		//不是急刀的就可以從編號 EORoomN 開始找可以使用的手術室
		for(; i < RoomMaxSize; i++) {
			if(!RoomBusying[i]) {
				RoomBusying[i] = true;
				RoomStartTime[i] = (int)sss.T(); 
				return i;
			}
		} 
		return -1;
	}
	 
	private void sentToOperatingRoom(int RoomNumber) { 
		//"設置手術室號碼"
		sss.SETA(RoomNumber_Code, RoomNumber);
		//設置現在時間點
		sss.SETA(Time_Code,(int)sss.T());
		//麻醉開始與入室時間的時間點設置(進入手術室時間) 
		sss.SETA(AnesthesiaBeginTime_Code, (int)sss.T());
		sss.SETA(InRoomTime_Code,  (int)sss.T()); 						
		//麻醉完成的時間點設置(進入手術室時間+麻醉時間)
		sss.SETA(AanesthesiaFinishTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code));
		//手術開始的時間點設置(進入手術室時間+麻醉時間+麻醉結束等待時間(固定5分鐘))
		sss.SETA(SurgeryBeginTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + 5 );
		//手術結束的時間點設置(進入手術室時間+麻醉時間+手術時間)
		sss.SETA(SurgeryEndTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code));
		//麻醉結束的時間點設置(進入手術室時間+麻醉時間+手術時間+麻醉結束等待時間(固定5分鐘))
		sss.SETA(AanesthesiaEnd_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5);
		//轉送時間的時間點設置(進入手術室時間+麻醉時間+手術時間+麻醉結束等待時間(固定5分鐘)+轉送時間長度)
		sss.SETA(TransferTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5 + sss.A(TransferTimelong_Code));
		//清潔時間的時間點設置(進入手術室時間+麻醉時間+手術時間+麻醉結束等待時間(固定5分鐘)+轉送時間長度+清潔時間長度)
		sss.SETA(CleanTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5 + sss.A(TransferTimelong_Code) + sss.A(CleanTimelong_Code));
		
		addCalender();
		//(ARRIVL -> NEXTAC) 將病患送至手術室進行麻醉 
		//沒有等待 開始等候時間點 為預設-1
		//SCHED(時間間隔 事件代碼 病患編號)
		sss.SCHED(0, 2,(int)sss.A(ID_Code)); 
	}
	 
	private void arrangeToWaitingArea() {
		//設置現在時間點
		sss.SETA(Time_Code,(int)sss.T());
		//"開始等候時間點"設置為現在
		sss.SETA(WaitBeginTime_Code, (int)sss.T()); 
		//將急刀病患放入急刀等候區 
		if(sss.A(Priority_Code) >= 1) { 
			switch ((int) sss.A(Priority_Code)) {
				case 2: 
					DE++; 
					if(ME < DE) ME = DE; 
					break;
				case 1: 
					DO++; 
					if(MO < DO) MO = DO;
					break;
				default:
			}
			
			//QUEUE(指定放進哪一個佇列 優先權值)
			sss.QUEUE(1, sss.A(Priority_Code)); 
		}
		else { 
			//將普通病患放入常規刀等候區 
			sss.QUEUE(2, sss.A(SurgeryTime_Code));
			//QUEUE(指定放進哪一個佇列, 優先權值(以手術時間來排))
			if(MR < sss.NQ(2)) MR = sss.NQ(2);
		} 	
	}
	 
	private void setParameter(SimulationScript src) {
		//模擬時間長度
		SimulationTime = src.getSimulationTime();
		//一天的總人數 榮總一天平均有 83.6673058485139 人 
		MaxofPeople = src.getDailyNPeople();  //平日
		/**改變作法不需要
		//榮總急刀一級發生機率
		ESurgery = src.getESurgery(); 
		//榮總急刀二級病患發生機率
		OSurgery = src.getOSurgery(); 
		*/
		//手術室名稱
		RoomName = src.getRoomName(); 
		//手術室總間數
		RoomAmount = RoomName.length;  
		//各科別手術時間統計表 (包含欄位名稱) 
		subject = src.getSubjectDistribution();	 
		//各種麻醉時間統計表  (包含欄位名稱)
		anesthesia = src.getAnesthesiaDistribution(); 
		//科別名稱
		SubjectName = src.getSubjectName() ;
		//麻醉名稱
		AnesthesiaName = src.getAnesthesiaName() ;
		 
		//屬性Code
		Time_Code= src.getTime_Code();								//"到達時間點"
		ID_Code= src.getID_Code();									//"病人代號"
		Event_Code= src.getEvent_Code();							//"事件"
		Priority_Code= src.getPriority_Code();						//"刀別"
		Subject_Code= src.getSubject_Code();						//"所屬科別"
		Anesthesia_Code= src.getAnesthesia_Code();					//"麻醉方式"
		ArrivalTime_Code= src.getArrivalTime_Code();				//"抵達時間"
		AnesthesiaBeginTime_Code = src.getAnesthesiaBeginTime_Code();	//"麻醉開始時間點"
		AanesthesiaFinishTime_Code = src.getAanesthesiaFinishTime_Code();	//"麻醉開始時間點"	// "麻醉結束時間點"
		AnesthesiaTime_Code = src.getAnesthesiaTime_Code();			// "麻醉時間長度", 
		SurgeryBeginTime_Code = src.getSurgeryBeginTime_Code();		// "手術開始時間點"
		SurgeryEndTime_Code = src.getSurgeryEndTime_Code();			// "手術開始時間點"
		SurgeryTime_Code =  src.getSurgeryTime_Code();				// "手術開始長度"
		TransferTimelong_Code = src.getTransferTimelong_Code();		// "轉送時間"
		CleanTimelong_Code = src.getCleanTimelong_Code();			// "清潔時間"
		RoomNumber_Code = src.getRoomNumber_Code();					// "手術房號"
		WaitBeginTime_Code = src.getWaitBeginTime_Code();			//開始等待時間點
		WaitTime_Code = src.getWaitTime_Code();						//等待時間長度
		InRoomTime_Code = src.getInRoomTime_Code();					//入室時間
		AanesthesiaEnd_Code = src.getAanesthesiaEnd();				//麻醉結束
		TransferTime_Code = src.getTransferTime_Code();				// "轉送時間"
		CleanTime_Code = src.getCleanTime_Code();					// "清潔時間"
		ApplyID_Code = src.getApplyID_Code();						//申請序號
		PatientName_Code = src.getPatientName_Code();				//病患姓名
		Identity_Code = src.getIdentity_Code();						//身分
		SurgeryName_Code = src.getSurgeryName_Code();				//手術名稱
		patientID_Code = src.getpatientID_Code();					//"病歷號"
		
		//得到Calender資料
		CalenderData = src.getCalender();   
	}
 
	/**
	 * 抓取calender資料位置 且如果不為-1則填寫-1
	 * @param Code
	 * @return
	 */
	private double getPresetCalenderValue(int Code) 
	{
		//取得數值
		String str = CalenderData.get(ReadCalenderTimes)[Code-1];
		//回傳值
		double Value ;
		//如果不為-1則填寫-1
		if(!str.equals("-1"))
			Value = -1;
		else
			Value = Double.parseDouble(str);
		return Value;  
	} 
	
}
