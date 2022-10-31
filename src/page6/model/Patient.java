package page6.model;

import java.io.Serializable;
  
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ApplicationID; // 申請序號
	private String PatientID; // 病患ID
	private String Identity; // 病患身分
	private String PatientName; // 病人姓名
	private String Section; // 科別
	private String Doctor; // 主治醫師
	private String Room; // 室別
	private String Type; // 類型
	private String SurgeryName; // 手術名稱
	private String AnesthesiaName; // 麻醉名稱
	private int ArrivalTime; // 到達時間
	private int EntryTime; // 入室時間
	private int AnesthesiaStart; // 麻醉開始
	private int AnesthesiaFinish; // 麻醉完成
	private int ScratchTime; // 劃刀時間
	private int SutureTime; // 縫合切口
	private int SurgeryTime; // 手術時長
	private int AnesthesiaEnd; // 麻醉結束
	private int TransferTime; // 轉送時間
	private int CleanTime; // 清潔時間

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
	} // 科別

	public String getDoctor() {
		return Doctor;
	} // 主治醫師

	public String getRoom() {
		return Room;
	} // 室別

	public String getType() {
		return Type;
	} // 類型

	public String getSurgeryName() {
		return SurgeryName;
	} // 手術名稱

	public String getAnesthesiaName() {
		return AnesthesiaName;
	} // 麻醉名稱

	public int getArrivalTime() {
		return ArrivalTime;
	} // 到達時間

	public int getEntryTime() {
		return EntryTime;
	} // 入室時間

	public int getAnesthesiaStart() {
		return AnesthesiaStart;
	} // 麻醉開始

	public int getAnesthesiaFinish() {
		return AnesthesiaFinish;
	} // 麻醉完成

	public int getScratchTime() {
		return ScratchTime;
	} // 劃刀時間

	public int getSutureTime() {
		return SutureTime;
	} // 縫合切口

	public int getSurgeryTime() {
		return SurgeryTime;
	} // 手術時長

	public int getAnesthesiaEnd() {
		return AnesthesiaEnd;
	} // 麻醉結束

	public int getTransferTime() {
		return TransferTime;
	} // 轉送時間

	public int getCleanTime() {
		return CleanTime;
	} // 清潔時間

	public String toString() {
		return "申請序號: " + this.ApplicationID + " 病患ID: " + this.PatientID + " 病患姓名: " + this.PatientName + " 病患身分: "
				+ this.Identity + " 科別:" + this.Section + " 主治醫師: " + this.Doctor + " 室別: " + this.Room + " 類別: "
				+ this.Type + " 手術名稱: " + this.SurgeryName + " 麻醉名稱: " + this.AnesthesiaName + " 到達時間: "
				+ this.ArrivalTime + " 入室時間: " + this.EntryTime + " 麻醉開始: " + this.AnesthesiaStart + " 麻醉完成: "
				+ this.AnesthesiaFinish + " 劃刀時間: " + this.ScratchTime + " 縫合切口: " + this.SutureTime + " 手術時長: "
				+ this.SurgeryTime + " 麻醉結束: " + this.AnesthesiaEnd + " 轉送時間: " + this.TransferTime + " 清潔時間: "
				+ this.CleanTime;
	}

}
