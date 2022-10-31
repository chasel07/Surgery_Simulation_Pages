package page4.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SimulationParametersModel implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Qmode = "FIFO";
	// ��N���`����
	private int RoomTotal;
	// �̤j�i�ζ���
	private int RoomMaxN = -1;
	// ��M�̤j�i��
	private int EORoomN = -2; 
	//Rule1_OR_Set  �зǮt40�����e(��0.68�p��)����N[����϶�]���t�Ŷ�
	private ArrayList<String> Rule1_OR_Set;
	//Rule2_OR_Set  �зǮt40������(��0.68�p��)����N[����϶�]���t�Ŷ� 
	private ArrayList<String> Rule2_OR_Set;
	 
	
	public void setQmode(String src) {
		Qmode = src;
	}
	public String getQmode() {
		return Qmode;
	}
	
	public void setRoomTotal(int src) {
		RoomTotal = src;
	}
	public int getRoomTotal() {
		return RoomTotal;
	}
	
	public void setRoomMaxN(int src) {
		RoomMaxN = src;
	}
	public int getRoomMaxN() {
		return RoomMaxN;
	}
	
	public void setEORoomN(int src) {
		EORoomN = src;
	}
	public int getEORoomN() {
		return EORoomN;
	} 
	 
	public void setRule1_OR_Set(ArrayList<String> src) {
		Rule1_OR_Set = src;
	}
	public ArrayList<String> getRule1_OR_Set() {
		return Rule1_OR_Set;
	} 
	 
	public void setRule2_OR_Set(ArrayList<String> src) {
		Rule2_OR_Set = src;
	}
	public ArrayList<String> getRule2_OR_Set() {
		return Rule2_OR_Set;
	} 
}
