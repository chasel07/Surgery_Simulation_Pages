package page6.model;

import java.io.Serializable;
  
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ApplicationID; // �ӽЧǸ�
	private String PatientID; // �f�wID
	private String Identity; // �f�w����
	private String PatientName; // �f�H�m�W
	private String Section; // ��O
	private String Doctor; // �D�v��v
	private String Room; // �ǧO
	private String Type; // ����
	private String SurgeryName; // ��N�W��
	private String AnesthesiaName; // �¾K�W��
	private int ArrivalTime; // ��F�ɶ�
	private int EntryTime; // �J�Ǯɶ�
	private int AnesthesiaStart; // �¾K�}�l
	private int AnesthesiaFinish; // �¾K����
	private int ScratchTime; // ���M�ɶ�
	private int SutureTime; // �_�X���f
	private int SurgeryTime; // ��N�ɪ�
	private int AnesthesiaEnd; // �¾K����
	private int TransferTime; // ��e�ɶ�
	private int CleanTime; // �M��ɶ�

	public Patient(String[] info) {
		ApplicationID = info[Calendar.APPLICATION_ID.column()];
		PatientName = info[Calendar.NAME.column()];
		AnesthesiaEnd = Integer.parseInt(info[Calendar.ANESTHESIA_END.column()]);
		AnesthesiaFinish = Integer.parseInt(info[Calendar.ANESTHESIA_FINISH.column()]);
		AnesthesiaName = info[Calendar.ANESTHESIA_NAME.column()];
		AnesthesiaStart = Integer.parseInt(info[Calendar.ANESTHESIA_START.column()]);
		ArrivalTime = Integer.parseInt(info[Calendar.ARRIVAL_TIME.column()]);
		CleanTime = Integer.parseInt(info[Calendar.CLEAN_TIME.column()]);
		Doctor = info[Calendar.DOCTOR.column()];
		EntryTime = Integer.parseInt(info[Calendar.ENTER.column()]);
		Identity = info[Calendar.IDENTITY.column()];
		PatientID = info[Calendar.ID.column()];
		Room = info[Calendar.ROOM.column()];
		ScratchTime = Integer.parseInt(info[Calendar.SUR_START.column()]);
		Section = info[Calendar.SUBJECT.column()];
		SurgeryName = info[Calendar.SURGERY_NAME.column()];
		SurgeryTime = Integer.parseInt(info[Calendar.SURGERY_TIME.column()]);
		SutureTime = Integer.parseInt(info[Calendar.SUR_END.column()]);
		TransferTime = Integer.parseInt(info[Calendar.TRANSFER_TIME.column()]);
		Type = info[Calendar.TYPE.column()];
	}

	public Patient(Object[] args) {
		 ApplicationID = args[0].toString();
		 PatientID = args[1].toString();
		 PatientName = args[2].toString();
		 Identity = args[3].toString();
		 Section = args[4].toString();
		 Doctor = args[5].toString();
		 Room = args[6].toString();
		 Type = args[7].toString();
		 SurgeryName = args[8].toString();
		 AnesthesiaName = args[9].toString();
		 ArrivalTime = Integer.parseInt(args[10].toString());
		 EntryTime = Integer.parseInt(args[11].toString());
		 AnesthesiaStart = Integer.parseInt(args[12].toString());
		 AnesthesiaFinish = Integer.parseInt(args[13].toString()); 
		 ScratchTime = Integer.parseInt(args[14].toString()); 
		 SutureTime = Integer.parseInt(args[15].toString()); 
		 SurgeryTime = Integer.parseInt(args[16].toString()); 
		 AnesthesiaEnd = Integer.parseInt(args[17].toString());
		 TransferTime = Integer.parseInt(args[18].toString()); 
		 CleanTime = Integer.parseInt(args[19].toString()); 
	}

	public String getApplicationID() {
		return ApplicationID;
	}

	public String getPatientID() {
		return PatientID;
	}

	public String getPatientName() {
		return PatientName;
	}

	public String getIdentity() {
		return Identity;
	}

	public String getSection() {
		return Section;
	} // ��O

	public String getDoctor() {
		return Doctor;
	} // �D�v��v

	public String getRoom() {
		return Room;
	} // �ǧO

	public String getType() {
		return Type;
	} // ����

	public String getSurgeryName() {
		return SurgeryName;
	} // ��N�W��

	public String getAnesthesiaName() {
		return AnesthesiaName;
	} // �¾K�W��

	public int getArrivalTime() {
		return ArrivalTime;
	} // ��F�ɶ�

	public int getEntryTime() {
		return EntryTime;
	} // �J�Ǯɶ�

	public int getAnesthesiaStart() {
		return AnesthesiaStart;
	} // �¾K�}�l

	public int getAnesthesiaFinish() {
		return AnesthesiaFinish;
	} // �¾K����

	public int getScratchTime() {
		return ScratchTime;
	} // ���M�ɶ�

	public int getSutureTime() {
		return SutureTime;
	} // �_�X���f

	public int getSurgeryTime() {
		return SurgeryTime;
	} // ��N�ɪ�

	public int getAnesthesiaEnd() {
		return AnesthesiaEnd;
	} // �¾K����

	public int getTransferTime() {
		return TransferTime;
	} // ��e�ɶ�

	public int getCleanTime() {
		return CleanTime;
	} // �M��ɶ�

	public String toString() {
		return "�ӽЧǸ�: " + this.ApplicationID + " �f�wID: " + this.PatientID + " �f�w�m�W: " + this.PatientName + " �f�w����: "
				+ this.Identity + " ��O:" + this.Section + " �D�v��v: " + this.Doctor + " �ǧO: " + this.Room + " ���O: "
				+ this.Type + " ��N�W��: " + this.SurgeryName + " �¾K�W��: " + this.AnesthesiaName + " ��F�ɶ�: "
				+ this.ArrivalTime + " �J�Ǯɶ�: " + this.EntryTime + " �¾K�}�l: " + this.AnesthesiaStart + " �¾K����: "
				+ this.AnesthesiaFinish + " ���M�ɶ�: " + this.ScratchTime + " �_�X���f: " + this.SutureTime + " ��N�ɪ�: "
				+ this.SurgeryTime + " �¾K����: " + this.AnesthesiaEnd + " ��e�ɶ�: " + this.TransferTime + " �M��ɶ�: "
				+ this.CleanTime;
	}

}
