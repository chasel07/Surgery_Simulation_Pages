package page4.model;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import page4.controller.Main;

/**
 * ��¥ΨӲ��ͼ����@��(�}��)�A���e�u����F�ƥ�A�]���i��L�{�����C
 * 
 * @author Chasel
 */
public class SimulationScript {
	 
	//�üƺؤl
	private long current_rnd = 200;
	
	// Calendar ����ݩʦW�� 
	private String[] Attribution = { "�ɶ��I", "��F����", "�ƥ�N�X", "��N�ŧO", "���ݬ�O", "�¾K�W��", "��F�ɶ�", "�¾K�}�l�ɶ�", "�¾K�����ɶ�", "�¾K�ɪ�",
			"���M�ɶ�", "�_�X���f", "��N�ɪ�", "��e�ɪ�", "�M��ɪ�", "��N�ЧO", "�}�l���Ԯɶ�", "���Ԯɪ�", "�f�w�J�Ǯɶ�", "�¾K�����ɶ�", "��e�ɶ�", "�M��ɶ�" };
	private String[] StringAttribution = { "�ӽЧǸ�", "�f�w�m�W", "�f�w����", "��N�W��", "�f����", "�D�v��v" };
	private static final String NODATA = "noData";
	
	private int Time_Code = 1; // "��F�ɶ��I"
	private int ID_Code = 2; // "�f�H�N��"
	private int Event_Code = 3; // "�ƥ�"
	private int Priority_Code = 4; // "�M�O"
	private int Subject_Code = 5; // "���ݬ�O"
	private int Anesthesia_Code = 6; // "�¾K�覡"
	private int ArrivalTime_Code = 7; // "��F�ɶ�"
	private int AnesthesiaBeginTime_Code = 8; // "�¾K�}�l�ɶ��I"
	private int AanesthesiaFinishTime_Code = 9;// "�¾K�����ɶ��I"
	private int AnesthesiaTime_Code = 10; // "�¾K�ɶ�����",
	private int SurgeryBeginTime_Code = 11; // "��N�}�l�ɶ��I"
	private int SurgeryEndTime_Code = 12; // "��N�}�l�ɶ��I"
	private int SurgeryTime_Code = 13; // "��N�}�l����"
	private int TransferTimelong_Code = 14; // "��e�ɶ�����"
	private int CleanTimelong_Code = 15; // "�M��ɶ�����"
	private int RoomNumber_Code = 16; // "��N�и�"
	private int WaitBeginTime_Code = 17; // �}�l���ݮɶ��I
	private int WaitTime_Code = 18; // ���ݮɶ�����
	private int InRoomTime_Code = 19; // �J�Ǯɶ�
	private int AanesthesiaEnd_Code = 20; // �¾K����
	private int TransferTime_Code = 21; // "��e�ɶ�"
	private int CleanTime_Code = 22; // "�M��ɶ�" 
	private int ApplyID_Code = 23; // �ӽЧǸ�
	private int PatientName_Code = 24; // �f�w�m�W
	private int Identity_Code = 25; // ����
	private int SurgeryName_Code = 26; // ��N�W��
	private int patientID_Code = 27; // �f����
	private int Doctor_Code = 28;
	
	/* �@���s�񪺦a�� */
	private ArrayList<String[]> calendar = new ArrayList<>();
	
	// �f����
	private int CurNumber = 0; 
	// ��O���v��(���L�k)
	private double[] Prob_subject;
	// �¾K���v��(���L�k)
	private double[] Prob_anesthesia;
	
	// �r���ݩʰ}�C
	private ArrayList<String[]> StringAttributionArr;
	// �U�@���M�f�w��F�ɶ�
	private double nextEOArrivalTime = Double.MAX_VALUE;
	// �T�{�U�@��f�w�O�_����M
	private boolean EOCheck;
	// �p�⥭���h�[�|�Ӥ@���M�f�w
	private double AvgofEOEventArrivalTime;
	// �f�H��F���ɶ���
	private ArrayList<Double> ArrivalTime = new ArrayList<Double>();
	// �`�W�M�f�H��F���p�ƾ�
	private int RArrivalCount = 0;
	
	// �@�����;�
	private JSSS createScript;
	// �ƥ󲣥;�
	private JSSS ArrivalTimesss;
	// ��M�ƥ󲣥;�
	private JSSS EO_Eventsss;
	// ��N�M�O���;�
	private JSSS RS_priority; 
	// ��O�H�����;�(�^��g��)
	private JSSS RS_Surgery;
	// �U��O��N�ɶ��H�����;�
	private JSSS[] RS_SurgeryTime;
	// �¾K�H�����;�
	private JSSS RS_Anesthesia;
	// �U�����¾K�ɶ����;�
	private JSSS[] RS_AnesthesiaTime;

	/**
	 * �üƺؤl
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
	
	// �����ɶ�����
	private double SimulationTime = 0.0;
	public void setSimulationTime(double src) {
		SimulationTime = src;
	}
	public double getSimulationTime() {
		return SimulationTime;
	}
	
	// �@���]�m���@�Ѫ������H��  ����
	private double DailysetNPeople = 0.0; 
	public void setDailysetNPeople(double src) {
		DailysetNPeople = src;
	}
	public double getDailysetNPeople() {
		return DailysetNPeople;
	}
	
	// �@�Ѫ������H��  ����
	protected double DailyNPeople = 0.0; 
	public void setDailyNPeople(double src) {
		DailyNPeople = src;
	}
	public double getDailyNPeople() {
		return DailyNPeople;
	}

	// �@�ѫ�M�������H��
	protected double EOArrivalDailyAvg;
	public void setEOArrivalDailyAvg(double src) {
		EOArrivalDailyAvg = src;
	}
	public double getEOArrivalDailyAvg() {
		return EOArrivalDailyAvg;
	}

	// �έp��Ƥ��@�Ѫ��̤j��
	protected double DailyMaxNPeople = 0.0;
	public void setDailyMaxNPeople(double src) {
		DailyMaxNPeople = src;
	}
	public double getDailyMaxNPeople() {
		return DailyMaxNPeople;
	}

	// �έp��Ƥ��@�Ѫ��̤p��
	protected double DailyMinNPeople = 0.0;
	public void setDailyMinNPeople(double src) {
		DailyMinNPeople = src;
	}
	public double getDailyMinNPeople() {
		return DailyMinNPeople;
	}

	// �C�p�ɥ������X�ӱ`�W�M�f�w��F
	protected double[] RArrivalAveg;
	public void setAvegTimeofArrival(double[] src) {
		RArrivalAveg = src;
	}
	public double[] getAvegTimeofArrival() {
		return RArrivalAveg;
	}
	
	// �U��O��N�ɶ����� (�]�t���W��)
	protected ArrayList<String[]> dtb_subject;
	public void setSubjectDistribution(ArrayList<String[]> src) {
		dtb_subject = src;
	}
	public ArrayList<String[]> getSubjectDistribution() {
		return dtb_subject;
	}
	
	// �U�س¾K��k������ (�]�t���W��)
	protected ArrayList<String[]> dtb_anesthesia;
	public void setAnesthesiaDistribution(ArrayList<String[]> src) {
		dtb_anesthesia = src;
	}
	public ArrayList<String[]> getAnesthesiaDistribution() {
		return dtb_anesthesia;
	}

	// ��F����
	private int dtb_Arrival = 1;
	public void setDtb_Arrival(int src) {
		dtb_Arrival = src;
	}
	public int getDtb_Arrival() {
		return dtb_Arrival;
	}

	// �a�`��M�@�ůf�w��F�����v
	protected double ESurgery = 0.0;
	public void setESurgery(double src) {
		ESurgery = src;
	}
	public double getESurgery() {
		return ESurgery;
	}

	// �a�`��M�G�ůf�w��F�����v
	protected double OSurgery = 0.0;
	public void setOSurgery(double src) {
		OSurgery = src;
	}
	public double getOSurgery() {
		return OSurgery;
	}
 
	// �`�W�M�f�w�H��
	private double DailyRAveg = 0;
	public void setDailyRAveg(double src) {
		DailyRAveg = src;
	}
	public double getDailyRAveg() {
		return DailyRAveg;
	}
	
	// ��N����e�ɶ�
	private double TransferTimelong = 5.0;
	public void setTransferTime(double TransferTime) {
		this.TransferTimelong = TransferTime;
	}
	public double getTransferTime() {
		return TransferTimelong;
	}

	// ��N�ǲM��ɶ�
	private double CleanTimelong = 5.0;
	public void setCleanTime(double CleanTime) {
		this.CleanTimelong = CleanTime;
	}
	public double getCleanTime() {
		return CleanTimelong;
	}

	// ��N�ǦW��
	private String[] RoomName;
	private void setRoomName(String[] src) {
		RoomName = src;
	}
	public String[] getRoomName() {
		return RoomName;
	}

	// ��O�W��
	private String[] SubjectName;
	public void setSubjectName(String[] src) {
		SubjectName = src;
	}
	public String[] getSubjectName() {
		return SubjectName;
	}

	// �¾K�W��
	private String[] AnesthesiaName;
	public void setAnesthesiaName(String[] src) {
		AnesthesiaName = src;
	}
	public String[] getAnesthesiaName() {
		return AnesthesiaName;
	}

	// ���o�ݩʥN�X
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
/*----------------------------------------------�W�謰���-----�U�謰�禡�B�z-----------------------------------------------------*/
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
	 * Ū�����
	 * 
	 * @param srcFile ��ƨӷ�
	 * @param onlyAttributes �O�_�uŪ������ݩ�
	 * @return �����@��
	 * @throws IOException
	 */ 
	public static SimulationScript FileReader(String srcFile, boolean onlyAttributes) throws IOException {
		CSVReader CSV = new CSVReader(srcFile);

		SimulationScript temp = new SimulationScript();

		//���LKey �C�饭����F�f�w �����ɪ�(��) �üƺؤl �O�_���ۭq�@�� ��e�ɪ� �M��ɪ�
		CSV.readNext();
		//Value
		String[] Line = CSV.readNext();
		//�O�_���ۭq�@��
		boolean Customize = Line[3].equals("1");
		// �ഫ�� View ����ܪ���m���� (�����ɪ� �C�饭���H�� �O�_���ۭq�@�� ��e�ɪ� �M��ɪ� �üƺؤl)
		String[] p = { Line[1], Line[0], Line[3], Line[4], Line[5], Line[2] };
		
		temp.setscript_paramater(p);
		temp.setDailyNPeople(Double.valueOf(Line[0]));
		temp.setSimulationTime(Double.valueOf(Line[1]));
		
		// ��N�ЦW��
		CSV.readNext(); 
		Line = CSV.readNext(); 
		ArrayList<String> tmp = new ArrayList<String>();
		for (String i : Line) {
			if (!i.equals(""))
				tmp.add(i);
		}
		temp.setRoomName(tmp.toArray(new String[tmp.size()]));

		// ��O�W��
		CSV.readNext(); 
		Line = CSV.readNext(); 
		temp.setSubjectName(Line);
		
		// �¾K�W��
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
		temp[0] = "�C�饭����F�f�w"; // "�C�饭����F�f�w"
		temp[1] = "�����ɪ�(��)"; // �����ɪ�
		temp[2] = "�üƺؤl"; // �üƺؤl
		temp[3] = "�O�_���ۭq�@��";
		temp[4] = "��e�ɪ�";
		temp[5] = "�M��ɪ�";

		CSV.WriteNext(temp);

		temp = new String[6];
		temp[0] = String.valueOf(DailysetNPeople); // ����
		temp[1] = String.valueOf(SimulationTime); // �����ɶ�����
		temp[2] = String.valueOf(current_rnd); // �üƺؤl
		temp[3] = "0"; // �O�_���ۭq�@��(�O��1 �_��0)
		temp[4] = String.valueOf(TransferTimelong); // ��e�ɪ�
		temp[5] = String.valueOf(CleanTimelong); // �M��ɪ�

		CSV.WriteNext(temp);

		temp = new String[1];
		temp[0] = "��N�ЦW��";
		CSV.WriteNext(temp);
		CSV.WriteNext(RoomName); // ��N�ǦW��

		temp = new String[1];
		temp[0] = "�Ҧ���O�W��"; // ��O�W��
		CSV.WriteNext(temp); // ��N�ǦW��
		temp = new String[dtb_subject.size() - 1];
		// �Ĥ@�Ӭ����ҦW�� �]���q1�}�l�~�����
		for (int i = 1; i < dtb_subject.size(); i++)
			temp[i - 1] = dtb_subject.get(i)[0];
		CSV.WriteNext(temp); // ��N�ǦW��

		temp = new String[1];
		temp[0] = "�Ҧ��¾K�W��"; // ��O�W��
		CSV.WriteNext(temp); // ��N�ǦW��
		temp = new String[dtb_anesthesia.size() - 1];
		// �Ĥ@�Ӭ����ҦW�� �]���q1�}�l�~�����
		for (int i = 1; i < dtb_anesthesia.size(); i++)
			temp[i - 1] = dtb_anesthesia.get(i)[0];
		CSV.WriteNext(temp); // ��N�ǦW��

		// ���D
		temp = new String[Attribution.length + StringAttribution.length];
		for (int i = 0; i < temp.length; i++) {
			if (i < Attribution.length)
				temp[i] = Attribution[i];
			else
				temp[i] = StringAttribution[i - Attribution.length];
		}
		CSV.WriteNext(temp);
		// ���e
		for (String[] i : calendar)
			CSV.WriteNext(i);

		CSV.close();
	}

	// Construct
	public SimulationScript() {
		
	}

	public SimulationScript(Distribution src) {
		// �f�w�s��
		CurNumber = 0;
		// �C��H��
		DailyNPeople = src.getDailyAveg();
		// �C��]�m�H��
		DailysetNPeople = 0;
		// �C�p�ɥ������X�ӤH��F
		RArrivalAveg = src.getRArrivalAveg();
		// �����C�ѷ|���X�ӫ�M��F
		EOArrivalDailyAvg = src.getEOArrivalDailyAvg();
		// �C��H�Ƴ̤j��
		DailyMaxNPeople = src.getDailyMax();
		// �C��H�Ƴ̤p��
		DailyMinNPeople = src.getDailyMin();
		// �C��`�W�����H��
		DailyRAveg = src.getDailyRAveg();

		// �a�`��M�@�ŵo�;��v
		ESurgery = src.getEArrival();
		// �a�`��M�G�ůf�w�o�;��v
		OSurgery = src.getOArrival();
		// ��N�ǦW��
		RoomName = src.getRoomName();

		// �U���N�ɶ��έp�� (�]�t���W��)
		dtb_subject = src.getSurgeryStatictis();
		// �]�w��O�����;��v��
		Prob_subject = SetProbability(dtb_subject);

		// �U�س¾K�ɶ��έp�� (�]�t���W��)
		dtb_anesthesia = src.getAnesthesiaStatictis();
		// �]�w�¾K�����;��v��
		Prob_anesthesia = SetProbability(dtb_anesthesia);

		// ��l�Ƽ����� �ño��Ҧ��ƥ��F�ɶ�
		createScript = new JSSS();
		// �üƺؤl�ɤW
		createScript.SETSEE((int) current_rnd);

		// ��l��
		// �U�@���M�f�w��F�ɶ�
		nextEOArrivalTime = 0.;
		// �T�{�U�@��f�w�O�_����M
		EOCheck = false;
		// ��M�ƥ󲣥;�
		EO_Eventsss = new JSSS();
		// �]�m�üƺؤl
		EO_Eventsss.SETSEE((int) current_rnd);
		// �`�W�M�H��
		RArrivalCount = 0;

		// �Ω��x�s�r���ݩ�
		StringAttributionArr = new ArrayList<String[]>();

		// �H����ܤ�N
		RS_Surgery = new JSSS();
		// �]�m�üƺؤl
		RS_Surgery.SETSEE((int) current_rnd);
		// �H����N�ɶ�
		RS_SurgeryTime = new JSSS[Prob_subject.length];
		for (int i = 0; i < RS_SurgeryTime.length; i++) {
			RS_SurgeryTime[i] = new JSSS();
			// �]�m�üƺؤl
			RS_SurgeryTime[i].SETSEE((int) current_rnd);
		}

		// �H����ܳ¾K
		RS_Anesthesia = new JSSS();
		// �H���¾K�ɶ�
		RS_AnesthesiaTime = new JSSS[Prob_anesthesia.length];
		for (int i = 0; i < RS_AnesthesiaTime.length; i++) {
			RS_AnesthesiaTime[i] = new JSSS();
			// �]�m�üƺؤl
			RS_AnesthesiaTime[i].SETSEE((int) current_rnd);
		}
		// �H����ܤM�O
		RS_priority = new JSSS();
		// �]�m�üƺؤl
		RS_priority.SETSEE((int) current_rnd);

		// �]�w�ݩ����
		createScript.INIQUE(0, Attribution.length, 0); 
	}

	private void primeSSSrnd() {
		// �üƺؤl�ɤW
		createScript.SETSEE((int) current_rnd);
		// �]�m�üƺؤl
		EO_Eventsss.SETSEE((int) current_rnd);
		// �]�m�üƺؤl
		RS_Surgery.SETSEE((int) current_rnd);
		for (int i = 0; i < RS_SurgeryTime.length; i++) {
			// �]�m�üƺؤl
			RS_SurgeryTime[i].SETSEE((int) current_rnd);
		}
		for (int i = 0; i < RS_AnesthesiaTime.length; i++) {
			// �]�m�üƺؤl
			RS_AnesthesiaTime[i].SETSEE((int) current_rnd);
		}
		// �]�m�üƺؤl
		RS_priority.SETSEE((int) current_rnd);
	}

	private void getAllREventArrivalTime() {
		// �����ɶ�
		double time = 0.;
		// ���������Ѽ�
		int day = 0;
		// �����������p�ɰ϶�
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

		// �]�m�������H�ƻP��ƪ������H�Ƥ��
		double DailyNPeopleRatio = DailysetNPeople / DailyNPeople;

		ArrivalTimesss = new JSSS();
		ArrivalTimesss.SETSEE((int) current_rnd);
		while (time < SimulationTime) {
			// �p��U�ƾ�
			day = (int) time / 1440;
			hour = (int) ((time % 1440) / 60);
			double getarrTime;

			// ���o��F�ɶ� �C�p�ɥ����ݭ��W�����H�Ƥ��
			if (RArrivalAveg[hour] != 0) {
				double s = (RArrivalAveg[hour] * DailyNPeopleRatio);
				s += 0.5;
				s = (int) s;
				getarrTime = ArrivalTimesss.EX(60 / s);
			} else
				getarrTime = 99;

			pt += getarrTime;
			if (pt < 60) // �p�G�ƥ�ɶ��`�M�S���W�L�o�@�p�ɪ��ɶ� �h�O��
			{
				ArrivalTime.add(time + pt);
			} else // �N�U�ƾڭ��m
			{
				pt = 0.;
				time = day * 1440f + (hour + 1f) * 60;
			}
		}
	}

	public void run() {
		// ��s�üƺؤl
		primeSSSrnd();
		// ���o�Ҧ��`�W�M�f�w��F�ɶ�
		getAllREventArrivalTime();
		// ��F�p�G�j��0�~�N����M�f�w
		if (EOArrivalDailyAvg > 0) {
			// �]�m�������H�ƻP��ƪ������H�Ƥ��
			double DailyNPeopleRatio = DailysetNPeople / DailyNPeople;

			// AvgofEventArrivalTime��l�� �����������ɶ�/((�����ɶ�/�@�Ѯɶ�) * �@�Ѥ����������X���M�f�w * �C�饭���H�Ƥ��)
			AvgofEOEventArrivalTime = SimulationTime
					/ ((SimulationTime / 1440) * EOArrivalDailyAvg * DailyNPeopleRatio);
		} else
			AvgofEOEventArrivalTime = 0;

		// ���ͲĤ@�ө�F�ɶ�
		// ���o�Ĥ@�Өƥ�ɶ�
		double firstEvent = ArrivalTime.get(0);
		// �p�C���F�H�Ƭ�0 ������F�ɶ��]�|��0 �]���p�G��0�h�N���|����M��F�h������
		if (EOArrivalDailyAvg > 0)
			nextEOArrivalTime = EO_Eventsss.EX(AvgofEOEventArrivalTime);
		// �p�Ĥ@��f�w����M�f�w�h�ϥΫ�M�f�w����F�ɶ� �B�s�y�U�@��f�w����F�ɶ�
		if (nextEOArrivalTime < firstEvent && EOArrivalDailyAvg > 0 && nextEOArrivalTime < SimulationTime) {
			// �Ы�"�Ĥ@��f�w��F�ɶ�"�s�ƥ� (�_�l�ƥ�N�X�� 1)
			createScript.CREATE(nextEOArrivalTime, CurNumber++);
			// EX�禡�s�@������F�ɶ����j �]���n�N�ɶ��[�W�s�@�ɶ��I
			double time = EO_Eventsss.EX(AvgofEOEventArrivalTime);
			nextEOArrivalTime += time;
			EOCheck = true; // ����M�h�]�mtrue
		} else {
			createScript.CREATE(firstEvent, CurNumber++); // �Ы�"�Ĥ@��f�w��F�ɶ�"�s�ƥ� (�_�l�ƥ�N�X�� 1)
			// �]�Ĥ@�Өƥ󬰱`�W�M�ҥH�N�`�W�M�p�ƾ�++
			RArrivalCount++;
		}

		// �]�m�����ɶ��� SimulationTime ����
		createScript.SIMEND(SimulationTime);

		// ���L�Ĥ@�Өƥ�
		createScript.NEXTEV();
		ARRIVL();
		createScript.DISPOS();

		int ecode;
		do {
			// ���o�ƥ��C�M�椤���ƥ�N�X
			if ((ecode = createScript.NEXTEV()) > 0) {
				addCalender();
				ARRIVL();
			}
			createScript.DISPOS();
		} while (ecode != 0);
	}

	private void ARRIVL() {
		createScript.SETA(Time_Code, (int) createScript.T()); // "��F�ɶ��I"(�@��)
		createScript.SETA(ID_Code, createScript.IDE() + 1f); // "�f�H�N��"�q1�}�l��ҥH+1
		createScript.SETA(Event_Code, createScript.EVC()); // "�ƥ�"
		// �M�O �p�G���M���`�W�M�h�]�m��0 �_�h�M�w��M�@�ũΤG��
		if (EOCheck == false)
			createScript.SETA(Priority_Code, 0);
		else
			createScript.SETA(Priority_Code, getEOPriority());
		createScript.SETA(Subject_Code, getType(Prob_subject, RS_Surgery));// ��O
		createScript.SETA(Anesthesia_Code, getType(Prob_anesthesia, RS_Anesthesia)); // "�¾K�覡"
		createScript.SETA(ArrivalTime_Code, (int) createScript.T()); // "��F�ɶ��I"
		createScript.SETA(AnesthesiaBeginTime_Code, -1); // "�¾K�}�l�ɶ��I"
		createScript.SETA(AanesthesiaFinishTime_Code, -1); // "�¾K�����ɶ��I"
		createScript.SETA(AnesthesiaTime_Code, (int) getAnesthesiaTime()); // "�¾K�ɶ�����"
		createScript.SETA(SurgeryBeginTime_Code, -1); // "��N�}�l�ɶ��I"
		createScript.SETA(SurgeryEndTime_Code, -1); // "��N�����ɶ��I"
		createScript.SETA(SurgeryTime_Code, (int) getSurgeryTime()); // "��N�ɶ�����"
		createScript.SETA(TransferTimelong_Code, (int) TransferTimelong); // "��e�ɶ�����"
		createScript.SETA(CleanTimelong_Code, (int) CleanTimelong); // "�M��ɶ�����"
		createScript.SETA(RoomNumber_Code, -1); // "��N�ǧO"
		createScript.SETA(WaitBeginTime_Code, -1); // "�}�l���Ԯɶ��I"
		createScript.SETA(WaitTime_Code, -1); // "���Ԫ��ɶ�����"
		createScript.SETA(InRoomTime_Code, -1); // �J�Ǯɶ�
		createScript.SETA(AanesthesiaEnd_Code, -1); // �¾K����
		createScript.SETA(TransferTime_Code, -1); // "��e�ɶ�"
		createScript.SETA(CleanTime_Code, -1); // "�M��ɶ�"

		// �H�U���r���ݩ� �]�ݩ�code��1�}�l�p�� �]���q�n-1
		String[] StrAttribution = new String[StringAttribution.length];
		StrAttribution[ApplyID_Code - Attribution.length - 1] = NODATA; // �ӽЧǸ�
		StrAttribution[PatientName_Code - Attribution.length - 1] = NODATA;// �f�w�m�W
		StrAttribution[Identity_Code - Attribution.length - 1] = NODATA; // ����
		StrAttribution[SurgeryName_Code - Attribution.length - 1] = NODATA;// ��N�W��
		// �f����
		StrAttribution[patientID_Code - Attribution.length - 1] = String.valueOf(createScript.A(ID_Code));
		// �D�v��v
		StrAttribution[Doctor_Code - Attribution.length - 1] = NODATA;

		StringAttributionArr.add(StrAttribution); // �N�ݩʥ[�J

		// �p�G�̫�@���F�ɫh���W�[�U�@��f�w
		if ((RArrivalCount + 1) != ArrivalTime.size()) {
			// �p�G�o�@�M����M�h���W�[�`�W�M�ƶq
			if (EOCheck == false)
				RArrivalCount++;
			// �N�ˬd�]�m��false
			EOCheck = false;
			double nextArriveTime = ArrivalTime.get(RArrivalCount);
			// �p�Ĥ@��f�w����M�f�w�h�ϥΫ�M�f�w����F�ɶ� �B�s�y�U�@��f�w����F�ɶ�
			if (nextEOArrivalTime < nextArriveTime && EOArrivalDailyAvg > 0 && nextEOArrivalTime < SimulationTime) {
				double time = EO_Eventsss.EX(AvgofEOEventArrivalTime);
				// �Ы�"�Ĥ@��f�w��F�ɶ�"�s�ƥ� (�_�l�ƥ�N�X�� 1)
				createScript.CREATE(nextEOArrivalTime - (int) createScript.T(), CurNumber++);
				// EX�禡�s�@������F�ɶ����j �]���n�N�ɶ��[�W�s�@�ɶ��I
				nextEOArrivalTime += time;
				// ����M�h�]�mtrue
				EOCheck = true;
			} else
				createScript.CREATE(nextArriveTime - (int) createScript.T(), CurNumber++);
		} else if (nextEOArrivalTime < SimulationTime && EOArrivalDailyAvg > 0) {
			double time = EO_Eventsss.EX(AvgofEOEventArrivalTime);
			// �Ы�"�Ĥ@��f�w��F�ɶ�"�s�ƥ� (�_�l�ƥ�N�X�� 1)
			createScript.CREATE(nextEOArrivalTime - (int) createScript.T(), CurNumber++);
			// EX�禡�s�@������F�ɶ����j �]���n�N�ɶ��[�W�s�@�ɶ��I
			nextEOArrivalTime += time;
			// ����M�h�]�mtrue
			EOCheck = true;
		}
	}

	// �M�w��M�@�ũάO�G��
	private int getEOPriority() {
		double x = RS_priority.RA(); // �H�����ͤ@�ӯB�I��
		double A = ESurgery + OSurgery; // 2�Ӫ����v
		double E = ESurgery / A; // ��M�@��

		if (x < E)
			return 2; // ��M�@�Ŧ^�� 2
		else
			return 1; // ��M�G�Ŧ^�� 1
	}

	// �M�w�ӯf�w�¾K�ɶ�
	private double getAnesthesiaTime() {
		int Anesthesia = (int) createScript.A(Anesthesia_Code) + 1;
		// �¾K��Ƥ���
		String[] AType = dtb_anesthesia.get(Anesthesia);

		String distribute = AType[FindTitle(dtb_anesthesia, "����")];
		double mean = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "������")]);
		double std = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "�зǮt")]);
		double max = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "�̤j��")]);
		double min = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "�̤p��")]);
		double model = Double.parseDouble(AType[FindTitle(dtb_anesthesia, "����")]);

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

			default: // �`�A���G
				AnesthesiaTime = generator.RN(mean, std);
			}
		} while (AnesthesiaTime < 0);

		return AnesthesiaTime;
	}

	// �M�w��O����N�ɶ�
	private double getSurgeryTime() {
		int subject = (int) createScript.A(Subject_Code) + 1;
		// ���o�Ӭ�O���ѼƸ��
		String[] SData = dtb_subject.get(subject);

		String distribute = SData[FindTitle(dtb_subject, "����")];
		double mean = Double.parseDouble(SData[FindTitle(dtb_subject, "������")]);
		double std = Double.parseDouble(SData[FindTitle(dtb_subject, "�зǮt")]);
		double max = Double.parseDouble(SData[FindTitle(dtb_subject, "�̤j��")]);
		double min = Double.parseDouble(SData[FindTitle(dtb_subject, "�̤p��")]);
		double model = Double.parseDouble(SData[FindTitle(dtb_subject, "����")]);

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

			default: // �`�A���G
				SurgeryTime = generator.RN(mean, std);
			}
		} while (SurgeryTime < 0);
		return SurgeryTime;
	}

	// �M�w��O�γ¾K�覡
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

			// �p�G�n��g����쬰��O �h�N��O�W�ٶ�J
			if ((i + 1) == Subject_Code)
				eventInfo[i] = dtb_subject.get((int) createScript.A(Subject_Code) + 1)[0];
			// �p�G�n��g����쬰�¾K �h�N�¾K�W�ٶ�J
			else if ((i + 1) == Anesthesia_Code)
				eventInfo[i] = dtb_anesthesia.get((int) createScript.A(Anesthesia_Code) + 1)[0];
			else
				eventInfo[i] = String.valueOf(createScript.A(i + 1));
		}
		for (int i = 0; i < StringAttribution.length; i++) {
			// �]�f�w�N����1�}�l�p�� �]��-1
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
				Main.log(Level.INFO, Name + " ���䤣��!");
			}
		}

		return NumberofColumn;
	}

	private double[] SetProbability(ArrayList<String[]> Data) {
		int NumberofColumn = FindTitle(Data, "����");

		if (Data == null || Data.size() <= 1 || NumberofColumn == -1) {
			Main.log(Level.INFO, this.toString().concat("-SetProbability �S���i�Ϊ����"));
			return new double[0];
		} else {
			// ��Ƶ���
			int n = Data.size() - 1;
			// �����`�p
			int Total = 0;
			// Data.get(0) �O���D�C�ҥH�� 1 �}�l�p
			for (int i = 1; i < Data.size(); i++) {
				Total += Integer.parseInt(Data.get(i)[NumberofColumn]);
			}

			// ���v�ֿn��
			double[] Table = new double[n];
			// �����`�o�ͦ��Ƭ��s �N��j�a���ӳ��O�@�˪��o�;��v
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
