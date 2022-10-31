package page5.model;

import java.io.IOException; 
import java.util.ArrayList; 
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
	
	/**
	 * �üƺؤl
	 * @param src
	 */
	public void setcurrent_rnd(long src) {
		current_rnd = src;
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
		//Do nothing.
	}
	public void setCalendar(ArrayList<String[]> src) {
		calendar = src;
	}

	public ArrayList<String[]> getCalender() {
		return calendar;
	}
}
