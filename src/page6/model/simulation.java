package page6.model;
 
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level; 
import java.time.LocalDate;

import page6.controller.Main;

//�ɶ����G���� 
public class simulation extends report {	   
	  
	//calender�@�����
	private ArrayList<String[]> CalenderData;
	//�f����
	private int CurNumber = 1; 
	//
	private int ReadCalenderTimes;
	
	//�ϥΪ̫��w����N�Ǽƶq
	private int MaxRoomN = 0; 	  	  
	//�]�m�ϥΪ̫��w����N�Ǽƶq
	public void setMaxRoomN(int src) { MaxRoomN = src; }
	//���o�ϥΪ̫��w����N�Ǽƶq
	public int getMaxRoomN() { return MaxRoomN; }
	 
	//��M�M�ݤ�N�ǭӼ�
	private int EORoomN = 0; 	  	  
	//�]�m��M�M�ݶ���
	public void setEORoomN(int src) { EORoomN = src; }
	//���o��M�M�ݶ���
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
			Main.log(Level.INFO, "�ܤ֫O�d�@���i�ѵ��`�W�M�ϥΡI");
			return false; 
		} 
		else if(EORoomN < 0) 
		{
			Main.log(Level.INFO, "��M�M�ݶ���(EORoomN)������J�D�t�ƪ���!!"); 
			return false;
		} 
		else if(QMode.equals("BLS") && RoomMaxSize < 6) 
		{ 
			Main.log(Level.INFO, "�϶��Ƶ{�Ҧ��U�N���L��������������!!"); 
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * �������P��N�Ǽƶq����N�Ǩϥα���
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
	 * �����N�г̫᪺�`�ϥήɶ� 
	 * @throws IOException 
	 */  
	private void summarize() throws IOException { 
		
		for(int i = 0; i < RoomMaxSize; i++) { 
			if(RoomBusying[i] == true)
				sss.TALLY(i + 1, SimulationTime - RoomStartTime[i]);  
		} 
		
		//�ƥ�M��
		while(sss.NEXTEV() != 0) {
			sss.DISPOS();
		} 
		
		//����̫��٦b���ݰϪ��f�w���ݮɶ�
		markCalender("�έp��M��C�٦b���Ԫ��H��");
		while(sss.NQ(1) > 0) {
			sss.DISPOS();
			sss.REMVFQ(1, 1);
			sss.SETA(WaitTime_Code, SimulationTime - sss.A(WaitBeginTime_Code));
			switch((int)sss.A(Priority_Code)) {
				case 2: waitTime.TALLY(1, sss.A(WaitTime_Code)); 
						WE++;	//�έp��M�@���٦b���Ԫ��H��
						break;  
				case 1: waitTime.TALLY(2, sss.A(WaitTime_Code));
						WO++; 	//�έp��M�G���٦b���Ԫ��H��
						break; 
				default:
			} 
			addCalender();
		}   
		WR = sss.NQ(2);  
		markCalender("�έp�`�W�M��C�٦b���Ԫ��H��");
		while(sss.NQ(2) > 0) {  
			sss.DISPOS();
			sss.REMVFQ(2, 1);
			sss.SETA(WaitTime_Code, SimulationTime - sss.A(WaitBeginTime_Code)); 
			waitTime.TALLY(3, sss.A(WaitTime_Code)); 
			addCalender();
		}  
		
		addAllReport();
		
		if(RoomMaxSize == MaxRoomN) { 
			//��X�ƥ���� (�ɦW�N���[�W�ϥζ��ƤΤ��) 
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
			if(ecode > 0) { //���o�ƥ��C�M�椤���ƥ�N�X						
				AmountOfEvent[ecode - 1]++;  //�Y�ƥ󦸼Ƭ����[�@
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
	 * ��l�ƨ�� 
	 */
	private void prime() throws IOException {    
		//��������Ū�� Calendar ������
		ReadCalenderTimes = 0;
		 
		setCalender(Attribution , StringAttribution);
		
		/*���ܧ@�k
		SProbability = SetProbability(subject);		//�]�w��O�����;��v��
		AProbability = SetProbability(anesthesia);	//�]�w�¾K�����;��v��
		*/ 
		RoomBusying = new boolean[RoomMaxSize];  //�ΨӧP�_��N�ǬO�_���b�ϥΤ�
		Arrays.fill(RoomBusying, false);  //��l�ư}�C - false �N���ϥ�
		
		RoomStartTime = new double[RoomMaxSize]; //�ΨӼȦs�ثe���b��N�Ǥ�����N�}�l�ɶ��I
		
		CurNumber = 0;
		R = E = O = WR = WE = WO = MR = ME = MO = DE = DO = 0;
		
		//�[�Z�ɶ�
		OverTime = 0.;
		
		AmountOfEvent = new int[5];
		Arrays.fill(AmountOfEvent, 0);
	
		//�r���ݩʬ����}�C��l��
		StringAttributionArr = new ArrayList<String[]>();	
		
		//�إ߼����� 
		sss = new JSSS();
		//�έp���ݮɶ���
		waitTime = new JSSS();	
		/* ��M���;���l��
		 * �ݩʡG�O�_��M, ���ݬ�O, �¾K�覡, �}�l���Ԯɶ�, ���Ԯɶ�����, ��N�Ǹ��X, ��N�`�ɶ� 
		 * 1. ��M��C
		 * 2. �`�W�M��C
		 * �w�]�����i���X
		 */
		EO_Eventsss = new JSSS();	
		
		//�Y���϶��Ƶ{
		if(QMode.equals("BLS"))
		{ 
			//�]�w��C�ƶq�B�ݩ����ƶq�B��N�ɶ��έp��ƶq
			sss.INIQUE(RoomMaxSize, Attribution.length, RoomMaxSize);
		}
		else 
		{	
			//�]�w��C�ƶq�B�ݩ����ƶq�B��N�ɶ��έp��ƶq
			sss.INIQUE(2, Attribution.length, RoomMaxSize); 
			sss.SETQDC(1, "BVF"); //�]�m���̤j�u���v��C (��M)
			sss.SETQDC(2, QMode); //�M�w�`�W�M���Ƶ{�覡
		}
		
		waitTime.INIQUE(0, 0, 3);
		waitTime.INISTA(1, "��M�@�ŵ��Ԯɶ��έp", 0, 24, 60, 60);  
		waitTime.INISTA(2, "��M�G�ŵ��Ԯɶ��έp", 0, 24, 60, 60);  
		waitTime.INISTA(3, "�`�W�M���Ԯɶ��έp" , 0, 24, 60, 60);   
		
		//�έp�w������N�Ǥ��}�M����
		for(int i = 0; i < RoomMaxSize; i++) {	
			sss.INISTA(i + 1, RoomName[i], 0, 24, 60, 60);
		}
		
		//�Ĥ@�Өƥ󲣥ͪ��ɶ����j
		double firstEvent = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[Time_Code-1]);
		//�Ы� "�Ĥ@��f�w��F�ɶ�" �s�ƥ� (�_�l�ƥ�N�X�� 1)
		sss.CREATE(firstEvent, CurNumber++);  
		// �]�m�����ɶ����׬� SimulationTime ����
		sss.SIMEND(SimulationTime); 
	}											 
	
	/**
	 * ��F�ƥ�
	 * 
	 * @throws IOException
	 */
	void ARRIVL() throws IOException {  
		readPatientData();
		
		//�έp�M�O����
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
		//�]�m�ƥ�N�X
		sss.SETA(Event_Code , sss.EVC());	
		//�]�m�{�b�ɶ��I
		sss.SETA(Time_Code,(int)sss.T());
		//��N�ɶ�
		double SurgeryTime = sss.A(SurgeryTime_Code);
		//�¾K�������ݮɶ�(�T�w5����)+��N�ɶ�
		addCalender();
		sss.SCHED(5 + SurgeryTime, 4, (int)sss.A(ID_Code));
	} 
	
	void ENDACT() throws IOException {  
		//�]�m�ƥ�N�X
		sss.SETA(Event_Code , sss.EVC());	
		//�]�m�{�b�ɶ��I
		sss.SETA(Time_Code,(int)sss.T());
		//������e�M�M��ɶ�
		addCalender();
		sss.SCHED(sss.A(TransferTimelong_Code) + sss.A(CleanTimelong_Code), 5, (int)sss.A(ID_Code));   
		
	} 
	 
	void NEXTAC() throws IOException { 
		sss.SETA(Event_Code , sss.EVC());	//�]�m�ƥ�N�X
		int RoomNumber = (int)sss.A(RoomNumber_Code);
		if(RoomNumber == -1) { 	
			//�]�m�{�b�ɶ��I
			sss.SETA(Time_Code,(int)sss.T());
			sss.SETA(WaitTime_Code, (int)sss.T() - sss.A(WaitBeginTime_Code));//, "���Ԯɶ�����"
			sss.SETA(RoomNumber_Code, AvailableRoom((int)sss.A(Priority_Code))); // "�]�m��N�Ǹ��X" 
			//�¾K�}�l�P�J�Ǯɶ����ɶ��I�]�m(�i�J��N�Ǯɶ�) 
			sss.SETA(AnesthesiaBeginTime_Code, (int)sss.T());
			sss.SETA(InRoomTime_Code,  (int)sss.T()); 
			//�¾K�������ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�)
			sss.SETA(AanesthesiaFinishTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code));
			//��N�}�l���ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+�¾K�������ݮɶ�(�T�w5����))
			sss.SETA(SurgeryBeginTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + 5 );
			//��N�������ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+��N�ɶ�)
			sss.SETA(SurgeryEndTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code));
			//�¾K�������ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+��N�ɶ�+�¾K�������ݮɶ�(�T�w5����))
			sss.SETA(AanesthesiaEnd_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5);
			//��e�ɶ����ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+��N�ɶ�+�¾K�������ݮɶ�(�T�w5����)+��e�ɶ�����)
			sss.SETA(TransferTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5 + sss.A(TransferTimelong_Code));
			//�M��ɶ����ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+��N�ɶ�+�¾K�������ݮɶ�(�T�w5����)+��e�ɶ�����+�M��ɶ�����)
			sss.SETA(CleanTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5 + sss.A(TransferTimelong_Code) + sss.A(CleanTimelong_Code));	
			//�έp�U�ؤM�O�����ݮɶ� 
			switch((int)sss.A(Priority_Code)) {
				case 2: waitTime.TALLY(1, sss.A(WaitTime_Code)); break; //��M�@��
				case 1: waitTime.TALLY(2, sss.A(WaitTime_Code)); break;//��M�G��
				case 0: waitTime.TALLY(3, sss.A(WaitTime_Code)); break; //�`�W�M
				default:
			} 
		} 		
		addCalender();
		sss.SCHED(sss.A(AnesthesiaTime_Code), 3, (int)sss.A(ID_Code));
	}  
	
	void CLEAN() throws IOException { 
		sss.SETA(Event_Code , sss.EVC());	//�]�m�ƥ�N�X
		//�]�m�{�b�ɶ��I
		sss.SETA(Time_Code,(int)sss.T());
		int RoomNumber = (int)sss.A(RoomNumber_Code);
		RoomBusying[RoomNumber] = false;
		//�o��J�Юɶ��P�X�Юɶ�
		double InRoomTime = sss.A(AnesthesiaBeginTime_Code);
		double OutRoomTime = (int)sss.T();
		int day =(int)( InRoomTime / 1440);
		//�P�_�O�_���[�Z �p�G���[�Z�h�N�[�Z�ɶ��W�[��OverTime
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
		
		
		//"������N��O�`�ɶ�(�t�¾K�ɶ����סB��N�ɶ����סB�¾K�������ݮɶ�)"
		sss.TALLY(RoomNumber + 1, sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5);
		sss.DISPOS();
		if(sss.NQ(1) > 0) { 
			sss.REMVFQ(1, 1);
			if(sss.A(Priority_Code) == 2) DE--;
			if(sss.A(Priority_Code) == 1) DO--;
			//(CLEAN -> NEXTAC) �N��M���԰Ϫ��f�w %d �ߧY�e�ܤ�N�Ƕi��¾K
			addCalender();
			sss.SCHED(0, 2, (int)sss.A(ID_Code));
		}
		else if(sss.NQ(2) > 0 && RoomNumber >= EORoomN) {     
			sss.REMVFQ(2, 1);  		
			//(CLEAN -> NEXTAC) �N�@�뵥�԰Ϫ��f�w %d �ߧY�e�ܤ�N�Ƕi��¾K
			addCalender();
			sss.SCHED(0, 2, (int)sss.A(ID_Code));   
		}
	}  
	
	
	private void readPatientData() {
		int ID = 0;				//�f�wID
		int eventcode = 0;  	//�ƥ�N�X
		int Priority = 0;		//�M�O
		int subject = 0;		//���ݬ�O
		int anesthesia = 0;		//�¾K�覡 5
		//double ArrivalTime = 0;		//"��F�ɶ��I" �Ȯɤ��ݭn �]��F�ɶ��I�ϥΩ�F�ƥ�ɶ�
		double AnesthesiaBeginTime = 0;	//�¾K�}�l�ɶ��I
		double AanesthesiaFinishTime = 0;//�¾K�����ɶ��I
		double AnesthesiaTime = 0;		//�¾K�ɶ�����
		double SurgeryBeginTime = 0;	//��N�}�l�ɶ��I 10
		double SurgeryEndTime = 0;		//��N�����ɶ��I
		double SurgeryTime = 0;			//��N�ɶ�����
		double TransferTimelong = 0;	//��e�ɶ�
		double CleanTimelong = 0;		//�M��ɶ�
		int RoomNumberData = 0;			//��N�и��X 15
		double WaitBeginTime = 0;		//�}�l���ݮɶ��I
		double WaitTime = 0;			//���ݮɶ�����
		double InRoomTime = 0;			//�J�Ǯɶ�
		double AanesthesiaEnd = 0;		//�¾K����
		double TransferTime = 0;		//��e�ɶ� 20
		double CleanTime = 0;			//�M��ɶ� 
		String ApplyID = "";			//�ӽЧǸ�
		String PatientName = "";		//�f�w�m�W
		String Identity = "";			//����
		String SurgeryName = "";		//��N�W�� 25
		String patientID = "";			//�f����		
		String Doctor = "";				//�D�v��v	
		 
		if((ReadCalenderTimes) < CalenderData.size()) 
		{ 
			ID = CurNumber;
			//�N�X�q1�}�l ����Ʊq0�}�l �]���n��1
			//Ū���ƥ�N�X �]���@���W���Ȭ��B�I�Ʀ]���ݭn��ƫ��A�ഫ
			eventcode = (int)Double.parseDouble(CalenderData.get(ReadCalenderTimes)[Event_Code-1]);
			//Ū���M�O �]���@���W���Ȭ��B�I�Ʀ]���ݭn��ƫ��A�ഫ
			Priority = (int)Double.parseDouble(CalenderData.get(ReadCalenderTimes)[Priority_Code-1]);
			//Ū�����ݬ�O
			String Subjectstr = CalenderData.get(ReadCalenderTimes)[Subject_Code-1];
			//��X�۹�����O�W�٪����ޭ�
			for(int i = 0 ; i < SubjectName.length ; i++) {
				if(SubjectName[i].equals(Subjectstr)) 
				{
					subject = i ;
					break;
				}
			}
			//Ū���¾K�覡
			String Anesthesiastr = CalenderData.get(ReadCalenderTimes)[Anesthesia_Code-1];
			//��X�۹����¾K�W�٪����ޭ�
			for(int i = 0 ; i < AnesthesiaName.length ; i++)
				if(AnesthesiaName[i].equals(Anesthesiastr)) 
				{
					anesthesia = i ;
					break;
				}
			
			//�p�G�Y�ǭȻݭn���T�w�w�]�� �h�ϥ�getPresetCalenderValue�P�_��J�ȬO�_���T
			
			//Ū���¾K�}�l�ɶ��I
			AnesthesiaBeginTime = getPresetCalenderValue(AnesthesiaBeginTime_Code-1);
			//Ū���¾K�����ɶ��I
			AanesthesiaFinishTime = getPresetCalenderValue(AanesthesiaFinishTime_Code-1);
			//Ū���¾K�ɶ�����
			AnesthesiaTime = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[AnesthesiaTime_Code-1]);
			//Ū����N�}�l�ɶ��I
			SurgeryBeginTime = getPresetCalenderValue(SurgeryBeginTime_Code-1);
			//Ū����N�����ɶ��I
			SurgeryEndTime = getPresetCalenderValue(SurgeryEndTime_Code-1);
			//Ū����N�ɶ�����
			SurgeryTime = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[SurgeryTime_Code-1]);
			//Ū����e�ɶ�
			TransferTimelong = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[TransferTimelong_Code-1]);
			//Ū���M��ɶ�
			CleanTimelong = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[CleanTimelong_Code-1]);
			//Ū����N�Ǹ��X
			RoomNumberData = (int)getPresetCalenderValue(RoomNumber_Code-1);
			//Ū���}�l���ݮɶ��I
			WaitBeginTime = getPresetCalenderValue(WaitBeginTime_Code-1);
			//Ū�����ݮɶ�����
			WaitTime = getPresetCalenderValue(WaitTime_Code-1);
			//Ū���J�Ǯɶ�
			InRoomTime = getPresetCalenderValue(InRoomTime_Code-1);
			//Ū���¾K����
			AanesthesiaEnd = getPresetCalenderValue(AanesthesiaEnd_Code-1);	
			//Ū����e�ɶ�
			TransferTime = getPresetCalenderValue(TransferTime_Code-1);		
			//Ū���M��ɶ�
			CleanTime = getPresetCalenderValue(CleanTime_Code-1);			
			
			//Ū����N�W��
			SurgeryName = CalenderData.get(ReadCalenderTimes)[SurgeryName_Code-1];
			//Ū���ӽЧǸ�
			ApplyID = CalenderData.get(ReadCalenderTimes)[ApplyID_Code-1];
			//Ū���f�w�m�W
			PatientName = CalenderData.get(ReadCalenderTimes)[PatientName_Code-1];
			//Ū������
			Identity = CalenderData.get(ReadCalenderTimes)[Identity_Code-1];
			//Ū���f����
			patientID = CalenderData.get(ReadCalenderTimes)[patientID_Code-1];	
			//Ū���D�v��v
			Doctor = CalenderData.get(ReadCalenderTimes)[Doctor_Code-1];	
			
			sss.SETA(Time_Code , (int)sss.T()); 						//"�ɶ��I"
			sss.SETA(ID_Code , ID); 									//"�f�H�N��"
			sss.SETA(Event_Code , eventcode); 							//"�ƥ�"
			sss.SETA(Priority_Code , Priority); 						//"�M�O" 
			sss.SETA(Subject_Code , subject); 							//"���ݬ�O" 5
			sss.SETA(Anesthesia_Code , anesthesia); 					//"�¾K�覡"
			sss.SETA(ArrivalTime_Code , (int)sss.T()); 					//"��F�ɶ��I"
			sss.SETA(AnesthesiaBeginTime_Code , AnesthesiaBeginTime); 	//"�¾K�}�l�ɶ��I"
			sss.SETA(AanesthesiaFinishTime_Code , AanesthesiaFinishTime);//"�¾K�����ɶ��I"
			sss.SETA(AnesthesiaTime_Code, AnesthesiaTime);				//"�¾K�ɶ�����" 10
			sss.SETA(SurgeryBeginTime_Code, SurgeryBeginTime); 			//"��N�}�l�ɶ��I"
			sss.SETA(SurgeryEndTime_Code, SurgeryEndTime); 				//"��N�����ɶ��I"
			sss.SETA(SurgeryTime_Code, SurgeryTime); 					//"��N�ɶ�����"
			sss.SETA(TransferTimelong_Code, TransferTimelong);		 	//"��e�ɶ�����"
			sss.SETA(CleanTimelong_Code, CleanTimelong); 		 		//"�M��ɶ�����" 15
			sss.SETA(RoomNumber_Code, RoomNumberData); 					//"��N�Ǹ��X"
			sss.SETA(WaitBeginTime_Code, WaitBeginTime); 				//"�}�l���Ԯɶ��I" 
			sss.SETA(WaitTime_Code, WaitTime); 							//"���Ԫ��ɶ�����" 
			sss.SETA(InRoomTime_Code, InRoomTime); 						//�J�Ǯɶ�
			sss.SETA(AanesthesiaEnd_Code, AanesthesiaEnd); 				//�¾K���� 20
			sss.SETA(TransferTime_Code, TransferTime); 					// "��e�ɶ�"
			sss.SETA(CleanTime_Code, CleanTime); 						// "�M��ɶ�"
			
			//�]�r���ݩʤ����JA�����]���h�]�m�@�Ӧr��}�CArrayList
			//�]�ݩ�code��1�}�l�p�� �]���q�n-1
			String[] StrAttribution = new String[StringAttribution.length];
			StrAttribution[ApplyID_Code - Attribution.length -1] = ApplyID;			//�ӽЧǸ�
			StrAttribution[PatientName_Code - Attribution.length -1] = PatientName;	//�f�w�m�W
			StrAttribution[Identity_Code - Attribution.length -1] = Identity;		//���� 25
			StrAttribution[SurgeryName_Code - Attribution.length -1] = SurgeryName;	//��N�W��
			StrAttribution[patientID_Code - Attribution.length -1] = patientID;		//�f����
			StrAttribution[Doctor_Code - Attribution.length -1] = Doctor;			//�D�v��v 28
			StringAttributionArr.add(StrAttribution);	
			
			//TODO ReadCalenderTimes �����[1�i��ɭP�̫�@����ƨS���Q�B�z�� (�|���T�w)
			//�p�G��̫�@���h���~��Ū���B�]�w�U�@��f�w��F�ɶ�
			if(++ReadCalenderTimes < CalenderData.size()) 
			{
				double nextArriveTime = Double.parseDouble(CalenderData.get(ReadCalenderTimes)[Time_Code-1]);
				sss.CREATE(nextArriveTime - (int)sss.T(), CurNumber++);
			} 
		} 
	}
	
	
	private int AvailableRoom(int type){	
		int i = (type >= 1) ? 0 : EORoomN;
		//����M�ϥΪ���N�ǽs���� 0 ~ (EORoomN - 1)
		//���O��M���N�i�H�q�s�� EORoomN �}�l��i�H�ϥΪ���N��
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
		//"�]�m��N�Ǹ��X"
		sss.SETA(RoomNumber_Code, RoomNumber);
		//�]�m�{�b�ɶ��I
		sss.SETA(Time_Code,(int)sss.T());
		//�¾K�}�l�P�J�Ǯɶ����ɶ��I�]�m(�i�J��N�Ǯɶ�) 
		sss.SETA(AnesthesiaBeginTime_Code, (int)sss.T());
		sss.SETA(InRoomTime_Code,  (int)sss.T()); 						
		//�¾K�������ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�)
		sss.SETA(AanesthesiaFinishTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code));
		//��N�}�l���ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+�¾K�������ݮɶ�(�T�w5����))
		sss.SETA(SurgeryBeginTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + 5 );
		//��N�������ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+��N�ɶ�)
		sss.SETA(SurgeryEndTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code));
		//�¾K�������ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+��N�ɶ�+�¾K�������ݮɶ�(�T�w5����))
		sss.SETA(AanesthesiaEnd_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5);
		//��e�ɶ����ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+��N�ɶ�+�¾K�������ݮɶ�(�T�w5����)+��e�ɶ�����)
		sss.SETA(TransferTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5 + sss.A(TransferTimelong_Code));
		//�M��ɶ����ɶ��I�]�m(�i�J��N�Ǯɶ�+�¾K�ɶ�+��N�ɶ�+�¾K�������ݮɶ�(�T�w5����)+��e�ɶ�����+�M��ɶ�����)
		sss.SETA(CleanTime_Code, (int)sss.T() + sss.A(AnesthesiaTime_Code) + sss.A(SurgeryTime_Code) + 5 + sss.A(TransferTimelong_Code) + sss.A(CleanTimelong_Code));
		
		addCalender();
		//(ARRIVL -> NEXTAC) �N�f�w�e�ܤ�N�Ƕi��¾K 
		//�S������ �}�l���Ԯɶ��I ���w�]-1
		//SCHED(�ɶ����j �ƥ�N�X �f�w�s��)
		sss.SCHED(0, 2,(int)sss.A(ID_Code)); 
	}
	 
	private void arrangeToWaitingArea() {
		//�]�m�{�b�ɶ��I
		sss.SETA(Time_Code,(int)sss.T());
		//"�}�l���Ԯɶ��I"�]�m���{�b
		sss.SETA(WaitBeginTime_Code, (int)sss.T()); 
		//�N��M�f�w��J��M���԰� 
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
			
			//QUEUE(���w��i���@�Ӧ�C �u���v��)
			sss.QUEUE(1, sss.A(Priority_Code)); 
		}
		else { 
			//�N���q�f�w��J�`�W�M���԰� 
			sss.QUEUE(2, sss.A(SurgeryTime_Code));
			//QUEUE(���w��i���@�Ӧ�C, �u���v��(�H��N�ɶ��ӱ�))
			if(MR < sss.NQ(2)) MR = sss.NQ(2);
		} 	
	}
	 
	private void setParameter(SimulationScript src) {
		//�����ɶ�����
		SimulationTime = src.getSimulationTime();
		//�@�Ѫ��`�H�� �a�`�@�ѥ����� 83.6673058485139 �H 
		MaxofPeople = src.getDailyNPeople();  //����
		/**���ܧ@�k���ݭn
		//�a�`��M�@�ŵo�;��v
		ESurgery = src.getESurgery(); 
		//�a�`��M�G�ůf�w�o�;��v
		OSurgery = src.getOSurgery(); 
		*/
		//��N�ǦW��
		RoomName = src.getRoomName(); 
		//��N���`����
		RoomAmount = RoomName.length;  
		//�U��O��N�ɶ��έp�� (�]�t���W��) 
		subject = src.getSubjectDistribution();	 
		//�U�س¾K�ɶ��έp��  (�]�t���W��)
		anesthesia = src.getAnesthesiaDistribution(); 
		//��O�W��
		SubjectName = src.getSubjectName() ;
		//�¾K�W��
		AnesthesiaName = src.getAnesthesiaName() ;
		 
		//�ݩ�Code
		Time_Code= src.getTime_Code();								//"��F�ɶ��I"
		ID_Code= src.getID_Code();									//"�f�H�N��"
		Event_Code= src.getEvent_Code();							//"�ƥ�"
		Priority_Code= src.getPriority_Code();						//"�M�O"
		Subject_Code= src.getSubject_Code();						//"���ݬ�O"
		Anesthesia_Code= src.getAnesthesia_Code();					//"�¾K�覡"
		ArrivalTime_Code= src.getArrivalTime_Code();				//"��F�ɶ�"
		AnesthesiaBeginTime_Code = src.getAnesthesiaBeginTime_Code();	//"�¾K�}�l�ɶ��I"
		AanesthesiaFinishTime_Code = src.getAanesthesiaFinishTime_Code();	//"�¾K�}�l�ɶ��I"	// "�¾K�����ɶ��I"
		AnesthesiaTime_Code = src.getAnesthesiaTime_Code();			// "�¾K�ɶ�����", 
		SurgeryBeginTime_Code = src.getSurgeryBeginTime_Code();		// "��N�}�l�ɶ��I"
		SurgeryEndTime_Code = src.getSurgeryEndTime_Code();			// "��N�}�l�ɶ��I"
		SurgeryTime_Code =  src.getSurgeryTime_Code();				// "��N�}�l����"
		TransferTimelong_Code = src.getTransferTimelong_Code();		// "��e�ɶ�"
		CleanTimelong_Code = src.getCleanTimelong_Code();			// "�M��ɶ�"
		RoomNumber_Code = src.getRoomNumber_Code();					// "��N�и�"
		WaitBeginTime_Code = src.getWaitBeginTime_Code();			//�}�l���ݮɶ��I
		WaitTime_Code = src.getWaitTime_Code();						//���ݮɶ�����
		InRoomTime_Code = src.getInRoomTime_Code();					//�J�Ǯɶ�
		AanesthesiaEnd_Code = src.getAanesthesiaEnd();				//�¾K����
		TransferTime_Code = src.getTransferTime_Code();				// "��e�ɶ�"
		CleanTime_Code = src.getCleanTime_Code();					// "�M��ɶ�"
		ApplyID_Code = src.getApplyID_Code();						//�ӽЧǸ�
		PatientName_Code = src.getPatientName_Code();				//�f�w�m�W
		Identity_Code = src.getIdentity_Code();						//����
		SurgeryName_Code = src.getSurgeryName_Code();				//��N�W��
		patientID_Code = src.getpatientID_Code();					//"�f����"
		
		//�o��Calender���
		CalenderData = src.getCalender();   
	}
 
	/**
	 * ���calender��Ʀ�m �B�p�G����-1�h��g-1
	 * @param Code
	 * @return
	 */
	private double getPresetCalenderValue(int Code) 
	{
		//���o�ƭ�
		String str = CalenderData.get(ReadCalenderTimes)[Code-1];
		//�^�ǭ�
		double Value ;
		//�p�G����-1�h��g-1
		if(!str.equals("-1"))
			Value = -1;
		else
			Value = Double.parseDouble(str);
		return Value;  
	} 
	
}
