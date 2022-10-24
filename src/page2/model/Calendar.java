package page1.model;
   
public enum Calendar {
	//��F����
	ID(1), 
	//�ƥ�N�X
	EVENT(2), 
	//��N�ŧO
	TYPE(3), 
	//���ݬ�O
	SUBJECT(4), 
	//�¾K�W��
	ANESTHESIA_NAME(5), 
	//��F�ɶ�
	ARRIVAL_TIME(6), 
	//�¾K�}�l�ɶ�
	ANESTHESIA_START(7), 
	//�¾K�����ɶ�
	ANESTHESIA_FINISH(8), 
	//���M�ɶ�
	SUR_START(10), 
	//�_�X���f
	SUR_END(11), 
	//��N�ɪ�
	SURGERY_TIME(12), 
	//��e�ɪ�
	TRANSFER_TIME(13),
	//�M��ɶ�
	CLEAN_TIME(14), 
	//��N�ЧO
	ROOM(15), 
	//�J�Ǯɶ�
	ENTER(18), 
	//�¾K����
	ANESTHESIA_END(19), 
	//��e�ɶ�
	EXIT(20), 
	//�ӽЧǸ�
	APPLICATION_ID(22), 
	//�f�H�m�W
	NAME(23), 
	//�f�w����
	IDENTITY(24), 
	//��N�W��
	SURGERY_NAME(25), 
	//�D�v��v
	DOCTOR(27);

	private int value; 
	private Calendar(int column) {
		value = column;
	}
	 
	public int column() {
		return value; 
	}
}
