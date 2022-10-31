package page4.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SimulationParametersModel implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Qmode = "FIFO";
	// 手術房總間數
	private int RoomTotal;
	// 最大可用間數
	private int RoomMaxN = -1;
	// 急刀最大可用
	private int EORoomN = -2; 
	//Rule1_OR_Set  標準差40分鐘前(約0.68小時)的手術[黃色區塊]分配空間
	private ArrayList<String> Rule1_OR_Set;
	//Rule2_OR_Set  標準差40分鐘後(約0.68小時)的手術[黃色區塊]分配空間 
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
