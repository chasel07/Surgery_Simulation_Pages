package page1.model;
   
public enum Calendar {
	//抵達順序
	ID(1), 
	//事件代碼
	EVENT(2), 
	//手術級別
	TYPE(3), 
	//所屬科別
	SUBJECT(4), 
	//麻醉名稱
	ANESTHESIA_NAME(5), 
	//到達時間
	ARRIVAL_TIME(6), 
	//麻醉開始時間
	ANESTHESIA_START(7), 
	//麻醉完成時間
	ANESTHESIA_FINISH(8), 
	//劃刀時間
	SUR_START(10), 
	//縫合切口
	SUR_END(11), 
	//手術時長
	SURGERY_TIME(12), 
	//轉送時長
	TRANSFER_TIME(13),
	//清潔時間
	CLEAN_TIME(14), 
	//手術房別
	ROOM(15), 
	//入室時間
	ENTER(18), 
	//麻醉結束
	ANESTHESIA_END(19), 
	//轉送時間
	EXIT(20), 
	//申請序號
	APPLICATION_ID(22), 
	//病人姓名
	NAME(23), 
	//病患身分
	IDENTITY(24), 
	//手術名稱
	SURGERY_NAME(25), 
	//主治醫師
	DOCTOR(27);

	private int value; 
	private Calendar(int column) {
		value = column;
	}
	 
	public int column() {
		return value; 
	}
}
