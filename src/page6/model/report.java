package page6.model;
 
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;

import page6.controller.Main;

public class report {  

	private static final String OR_COUNT = "��N�ж���";
			
	protected static final String[] colNameOF = { OR_COUNT, "��M�@��", "��M�G��", "�`�W�M", "�`�@" };
	
	
	protected static final TreeMap<String, String> ScheduleType = new TreeMap<String, String>(); 
	static {
		ScheduleType.put("BLS", "�϶��Ƶ{");
		ScheduleType.put("FIFO","������A��");
		ScheduleType.put("SVF", "�u�M����");
		ScheduleType.put("BVF", "���M����");
	} 
	
	private String rounding = "%.3f";
	
	public final String FN_calender = FileRoute.PATH_CALENDAR;
	public final String FN_report = FileRoute.PATH_DATA + "/report.txt";
	
	// �ɮ׽s��
	protected int FN_number = 0;
	protected ArrayList<String[]> calender;
	
	protected String[] AttributionName = null;
	protected String[] StrAttributionName = null;
 
	// ��N�Ǩϥα��βέp���i
	protected ArrayList<ArrayList<String>> result;

	/**
	 * ���o�Y����N�Ǫ��ϥα���
	 * @param roomId ��N�ǽs��
	 * @return
	 */
	public String getORUsageReport(int roomId) {
		if(roomId < 0 || roomId >= result.size()) 
		{
			return "�S��������";
		}
		
		StringBuilder text = new StringBuilder(); 
		for(String row: result.get(roomId)) { 
			text.append(row);
		}
		
		return text.toString();
	}
	
	
	/**
	 * ���o�ثe���O������N�ǲM��
	 * @param roomId ��N�ǽs��
	 * @return
	 */
	public ArrayList<String> getORList() {
		ArrayList<String> ORs = new ArrayList<String>();
		
		//�[�J��r�ɤ����ϥΨ쪺��N�нs��
		for(ArrayList<String> info: result) {  
			ORs.add(info.get(0)); 
        }  
		
		return ORs;
	}

	// �����ɶ�����
	protected double SimulationTime = 0.0;

	// �]�m�����ɶ�����
	public void setSimulationTime(double src) {
		SimulationTime = src;
	}

	// ���o�����ɶ�����
	public double getSimulationTime() {
		return SimulationTime;
	}

	// �Ƶ{�Ҧ�
	protected String QMode = "FIFO";

	// �]�m�Ƶ{�Ҧ�
	public void setQMode(String src) {
		QMode = src;
	}

	// ���o�Ƶ{�Ҧ�
	public String getQMode() {
		return QMode;
	}

	// �����ɶ����
	protected String TimeUnit = "����";

	// �]�m�����ɶ����
	public void setTimeUnit(String src) {
		TimeUnit = src;
	}

	// ���o�����ɶ����
	public String getTimeUnit() {
		return TimeUnit;
	}

	// �@�Ѫ��`�H�� �a�`�@�ѥ����� 83.6673058485139 �H
	protected double MaxofPeople = 0.0; // ����
	// �]�m�C�饭���H��

	public void setMaxofPeople(double src) {
		MaxofPeople = src;
	}

	// ���o�C�饭���H��
	public double getMaxofPeople() {
		return MaxofPeople;
	}

	// ��N���`����
	protected int RoomAmount = 0;

	// �]�m��N���`����
	public void setRoomAmount(int src) {
		RoomAmount = src;
	}

	// ���o��N���`����
	public int getRoomAmount() {
		return RoomAmount;
	}

	// �U��O��N�ɶ����� (�]�t���W��)
	protected ArrayList<String[]> subject;

	// �]�m�U��O��N�ɶ�����
	public void setSubjectDistribution(ArrayList<String[]> src) {
		subject = src;
	}

	// ���o�U��O��N�ɶ�����
	public ArrayList<String[]> getSubjectDistribution() {
		return subject;
	}

	// �U�س¾K��k������ (�]�t���W��)
	protected ArrayList<String[]> anesthesia;

	// �]�m�U�س¾K��k������ (�]�t���W��)
	public void setAnesthesiaDistribution(ArrayList<String[]> src) {
		anesthesia = src;
	}

	// ���o�U�س¾K��k������ (�]�t���W��)
	public ArrayList<String[]> getAnesthesiaDistribution() {
		return anesthesia;
	}

	// ��N�ǦW��
	protected String[] RoomName;

	// �]�m��N�ǦW��
	public void setRoomName(String[] src) {
		RoomName = src;
	}

	// ��N�ǦW��
	public String[] getRoomName() {
		return RoomName;
	}

	// ��O�W��
	protected String[] SubjectName;

	// �]�m��O�W��
	public void setSubjectName(String[] src) {
		SubjectName = src;
	}

	// ���o��O�W��
	public String[] getSubjectName() {
		return SubjectName;
	}

	// �¾K�W��
	protected String[] AnesthesiaName;

	// �]�m�¾K�W��
	public void setAnesthesiaName(String[] src) {
		AnesthesiaName = src;
	}

	// ���o�¾K�W��
	public String[] getAnesthesiayName() {
		return AnesthesiaName;
	}

	// ��N�Хi�μƶq
	protected int RoomMaxSize = 1;
	// ������ (��F���v����)
	protected JSSS sss;
	// �έp���Ԯɶ���
	protected JSSS waitTime;
	// ��M�ƥ󲣥;�
	protected JSSS EO_Eventsss;
	// �U�Ӥ�N�Ъ��u�@���A TRUE���L�� FALSE�𮧤�
	protected boolean[] RoomBusying;
	// �U�Ӥ�N�Ъ��u�@���A TRUE���L�� FALSE�𮧤�
	protected double[] RoomStartTime;
	// �����r���ݩʸ�ư}�C
	protected ArrayList<String[]> StringAttributionArr;

	// �¾K���v��(���L�k)
	protected double[] AProbability;
	// ��O���v��(���L�k)
	protected double[] SProbability;
	// ��O����
	protected Surgery[] SDistribution;
	// �O���U�غ��{�ת���N����
	protected int R, E, O;
	// �O���٦b���ݰϪ��H��
	protected int WE, WO, WR;
	protected int ME, MO, MR;
	protected int DE, DO;
	// �O���U��O���H��
	protected int[] subjectN;
	// �O���U�س¾K�ϥΦ���
	protected int[] anesthesiaN;

	// �O���U�بƥ�o�ͦ���
	protected int[] AmountOfEvent;

	// �����[�Z�����ɶ�
	protected double OverTime;

	// �ݩʦW��
	public String[] Attribution = { "�ɶ��I", "��F����", "�ƥ�N�X", "��N�ŧO", "���ݬ�O", "�¾K�W��", "��F�ɶ�", "�¾K�}�l�ɶ�", "�¾K�����ɶ�", "�¾K�ɪ�",
			"���M�ɶ�", "�_�X���f", "��N�ɪ�", "��e�ɪ�", "�M��ɪ�", "��N�ЧO", "�}�l���Ԯɶ�", "���Ԯɪ�", "�f�w�J�Ǯɶ�", "�¾K�����ɶ�", "��e�ɶ�", "�M��ɶ�" };
	// calendar �X�R���
	public String[] StringAttribution = { "�ӽЧǸ�", "�f�w�m�W", "�f�w����", "��N�W��", "�f����", "�D�v��v" };

	// SimulationScript �����@�������N��
	public int Time_Code = 1; // "��F�ɶ��I"
	public int ID_Code = 2; // "�f�H�N��"
	public int Event_Code = 3; // "�ƥ�"
	public int Priority_Code = 4; // "�M�O"
	public int Subject_Code = 5; // "���ݬ�O"
	public int Anesthesia_Code = 6; // "�¾K�覡"
	public int ArrivalTime_Code = 7; // "��F�ɶ�"
	public int AnesthesiaBeginTime_Code = 8; // "�¾K�}�l�ɶ��I"
	public int AanesthesiaFinishTime_Code = 9; // "�¾K�����ɶ��I"
	public int AnesthesiaTime_Code = 10; // "�¾K�ɶ�����",
	public int SurgeryBeginTime_Code = 11; // "��N�}�l�ɶ��I"
	public int SurgeryEndTime_Code = 12; // "��N�}�l�ɶ��I"
	public int SurgeryTime_Code = 13; // "��N�}�l����"
	public int TransferTimelong_Code = 14; // "��e�ɶ�����"
	public int CleanTimelong_Code = 15; // "�M��ɶ�����"
	public int RoomNumber_Code = 16; // "��N�и�"
	public int WaitBeginTime_Code = 17; // "�}�l���ݮɶ��I"
	public int WaitTime_Code = 18; // "���ݮɶ�����
	public int InRoomTime_Code = 19; // "�J�Ǯɶ�"
	public int AanesthesiaEnd_Code = 20; // "�¾K����"
	public int TransferTime_Code = 21; // "��e�ɶ�"
	public int CleanTime_Code = 22; // "�M��ɶ�"
	public int ApplyID_Code = 23; // �ӽЧǸ�
	public int PatientName_Code = 24; // �f�w�m�W
	public int Identity_Code = 25; // ����
	public int SurgeryName_Code = 26; // ��N�W��
	public int patientID_Code = 27; // �f����
	public int Doctor_Code = 28; // �D�v��v

	public report() {
		OperationFinish = new ArrayList<>();
		OperationUtilization = new ArrayList<>();
		MaxWaiting = new ArrayList<>();
		AverageWaiting = new ArrayList<>();
		Room_OverTime = new ArrayList<>(); 
	}

	/**
	 * ��l�Ƥ�x��
	 * 
	 * @param FileName �ɦW
	 * @param src �즳�ݩʦW�٨ӷ�
	 * @param src2 �X�R�ݩʦW�٨ӷ�
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
	 * ���o��x
	 * @return
	 */
	public ArrayList<String[]> getCalender() {
		return calender;
	}
	
	/**
	 * �ɤW��r���ѥ�
	 * @param text ����
	 */
	public void markCalender(String text) {
		String[] commentLine = { text };
		calender.add(commentLine);
	}

	/**
	 * �O�������L�{
	 *   --�]report�L�k��oID��m �ҥH�ݭn��J"�f�H�N��(ID_CODE)" 
	 */
	public void addCalender() {
		//����(�즳�ݩ� + �X�R�ݩ�)
		String[] eventInfo = new String[AttributionName.length + StrAttributionName.length];
		
		for (int i = 1; i <= AttributionName.length; i++) {
			
			if (i == Subject_Code) //�N����O�W��(�^���Y�g)
			{
				eventInfo[i - 1] = SubjectName[(int) sss.A(Subject_Code)];
			} 
			else if (i == Anesthesia_Code) //�N���¾K�W��(�^���Y�g)
			{
				eventInfo[i - 1] = AnesthesiaName[(int) sss.A(Anesthesia_Code)];
			} 
			else if (i == RoomNumber_Code) //�N����N�ǦW��(�^���Y�g)
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
			// �]�f�w�N����1�}�l�p�� �]��-1
			eventInfo[i + AttributionName.length] = StringAttributionArr.get((int) sss.A(ID_Code) - 1)[i];
		}
		
		calender.add(eventInfo); 
	}

	/**
	 * �ץX��x��
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
	 * �ץX�������G
	 * @throws IOException
	 */
	protected void printReport() throws IOException {
		// ���i�`�� 
		try(BufferedWriter BW_report = new BufferedWriter(new FileWriter(FN_report))) {
			BW_report.write("�Ƶ{�Ҧ��G");
			BW_report.write(ScheduleType.get(QMode)); 
			
			BW_report.write("\n\n�����ɶ����סG");
			BW_report.write(String.valueOf(SimulationTime));
			BW_report.write(" ");
			BW_report.write(TimeUnit);
			
			BW_report.write("\n\n��N���`���ơG");
			BW_report.write(String.valueOf(RoomAmount));
			
			BW_report.write("\n\n�C�饭���`�H�ơG");
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
					BW_report.write("�ť� - �]���S����������i�i��έp!!\n\n");
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

	public void resetReport() throws IOException { // ��l���x�s�Ϫ��ƪ��}�C 
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
		// �p��Ҧ���N�Ъ������Q�βv
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
		// �p���N�Ъ������[�Z�ɶ�
		double average = 0.0;
		if (OverTime != 0.) {
			average = OverTime;
			average /= RoomMaxSize;
		}
		return String.format(rounding, average);
	}

	// �^�Ǩƥ�o�ͪ�����
	public String getAmountOfEvent(int index) {
		if (index >= 0 && index < AmountOfEvent.length) {
			return String.valueOf(AmountOfEvent[index]);
		} else {
			Main.log(Level.WARNING, "getAmountOfEvent - range out");
			return null;
		}
	}

	// �^�ǵ��԰Ϫ��������ݮɶ�
	public String getAverageWaitTime(int index) {
		if (waitTime == null)
			return "";
		return String.format(rounding, waitTime.SAVG(index));
	}
	
	// -------------------------------------------------------------------------------------
	// ������N�H��----------------------------------------------------------------------------

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
	// �[�Z�ɶ�----------------------------------------------------------------------------
	private ArrayList<String[]> Room_OverTime;

	public ArrayList<String[]> getRoom_OverTime() {
		return Room_OverTime;
	}

	public void resetRoom_OverTime() {
		String[] RO = {OR_COUNT, "�����[�Z�ɶ�" };
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
	// ��N�Х����Q�βv----------------------------------------------------------------------------
	private ArrayList<String[]> OperationUtilization;

	public void resetOperationUtilization() {
		String[] OU = { OR_COUNT, "�Q�βv" };
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
	// �f�w�̪����Ԯɶ�-------------------------------------------------------------------------
	private ArrayList<String[]> MaxWaiting;

	public void resetMaxWaiting() {
		String[] W = { OR_COUNT, "��M�@��", "��M�G��", "�`�W�M" };
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
	// �f�w�������Ԯɶ�-------------------------------------------------------------------------
	private ArrayList<String[]> AverageWaiting;

	public void resetAverageWaiting() {
		String[] W = { OR_COUNT, "��M�@��", "��M�G��", "�`�W�M" };
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