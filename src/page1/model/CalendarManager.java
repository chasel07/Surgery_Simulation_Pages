package page1.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

public class CalendarManager {
	
	//�@�Ѧ�1440����
	private int Day = 1440; 
	/**
	 * ���������ƥ����O�A
	 * �Ψӫ��w���ը禡�B�z���H��A�n�A�^�����@�بƥ󪺸�ƥX�ӡC
	 */ 
	private String FetchEvent = "3.0";
	/**
	 *  
	 * @param Src 1.0, 2.0, 3.0, 4.0, 5.0 ��@�� 
	 */
	public void SetFetchEvent(String Src) { this.FetchEvent = Src; } 
	public String GetFetchEvent() { return FetchEvent; }
	 
	private File Src;
	public void SetFileSrc(File Src) { this.Src = Src; } 
	public File GetFileSrc() { return Src; }
	
	private String[] ColumnName;
	public String[] GetColumnName() { return ColumnName; }
	
	/**
	 * Calendar �s��}�M����(���t���D�C)
	 */
	private ArrayList<String[]> surgeryRecord;
	public ArrayList<String[]> GetCalendar() { return surgeryRecord; }
	

	public CalendarManager() { 
		
	}
	
	public CalendarManager(File Src) {
		// TODO Auto-generated constructor stub 
		this.Src = Src; 
		LoadCalendar(); 
	}
	
	private void LoadCalendar() {
		CSVReader in = new CSVReader(Src.toString()); 
		ColumnName = in.readNext(); 
		
		surgeryRecord = new ArrayList<String[]>();  
		String[] line; 
		while ((line = in.readNext()) != null) { 
			surgeryRecord.add(line);
		} 
	}
	
	/**
	 * �p�G��ƪ��Y�@�C�X�{��Ƥ��������p�h�|�N����Ʋ��L�C
	 * 
	 * @param Number ���w�έ��@��ӱN��Ƥ���
	 */
	public TreeMap<String, ArrayList<String[]>> GroupByColumn(int Number) {  
		return GroupByColumn(surgeryRecord, Number, FetchEvent); 
	} 
	
	public TreeMap<String, ArrayList<String[]>> GroupByColumn(ArrayList<String[]> Data, int Number) { 
		return GroupByColumn(Data, Number, FetchEvent);
	} 
	
	/**
	 * �̷ӫ��w�����Ӷi�����
	 *  
	 * @param Data
	 * @param Number
	 * @param FetchEvent
	 * @return
	 */
	public TreeMap<String, ArrayList<String[]>> GroupByColumn(ArrayList<String[]> Data, int Number, String FetchEvent) { 
		TreeMap<String, ArrayList<String[]>> Groups = new TreeMap<>();
		
		for(String[] row: Data) { 
			
			if (row.length != ColumnName.length) continue;
			
			if (row[Calendar.EVENT.column()].equals(FetchEvent)) { 
				String room = row[Number];  
				Groups.computeIfAbsent(room, k -> new ArrayList<>()); 
				Groups.get(room).add(row);
			}
		}
		
		return Groups;
	} 
	
	/**
	 *
	 *  �H���J����Ƭ��D�A�i����������
	 *   
	 * @param Col_Start ���_�_�l�ɶ�
	 * @param Col_End ���_�����ɶ�
	 * @return �������G
	 */
	public TreeMap<Integer, ArrayList<String[]>> GroupByDay(int Col_Start, int Col_End) { 
		
		TreeMap<Integer, ArrayList<String[]>> Groups = new TreeMap<>();
		
		for(String[] Record: surgeryRecord) {
			 
			if (Record.length != ColumnName.length) continue;
			
			if (Record[Calendar.EVENT.column()].equals(FetchEvent)) 
			{
				int s = (int)(Double.parseDouble(Record[Col_Start])) / Day;
				int e = (int)(Double.parseDouble(Record[Col_End]))   / Day;
				 
				Groups.computeIfAbsent(s, k -> new ArrayList<>());   
				Groups.get(s).add(Record);  
				
				if(s != e) {
					Groups.computeIfAbsent(e, k -> new ArrayList<>());  
					Groups.get(e).add(Record);  
				} 
			}
		}
		
		return Groups;
	}   
	  
	public ArrayList<String[]> SelectColumnFrom(TreeMap<String, ArrayList<String[]>> data, int col) { 
		
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		for(Entry<String, ArrayList<String[]>> key: data.entrySet()) { 
			ArrayList<String[]> records = key.getValue();
			 
			String[] sub = new String[records.size()]; 
			for(int i=0; i<sub.length; i++) {
				sub[i] = records.get(i)[col];
			} 
			
			result.add(sub);
		}
		
		return result;
	}
	
	public TreeSet<String> CreateSetFrom(ArrayList<String[]> data) {    
		
		TreeSet<String> set = new TreeSet<String>(); 
		
		for(String[] row: data) {
			for(String column: row) set.add(column);  
		}
		
		return set;
	}
}
