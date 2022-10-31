package page2.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.HashMap;

public class Page2_Disease { 
	private HashMap<String, Integer> name_num = new HashMap<>(); // 紀錄各標題所對應的index
	private ArrayList<String> allSurgery = new ArrayList<String>(); // 紀錄所有手術名稱
	private ArrayList<String> allDivision = new ArrayList<String>(); // 紀錄所有科別
	private ArrayList<String> SD = new ArrayList<String>(); // 紀錄手術名稱&科別
	private ArrayList<String> Surgery_data = new ArrayList<String>(); // 紀錄手術資料的手術名稱
	private ArrayList<String> Disease_data = new ArrayList<String>(); // 紀錄手術資料的手術名稱
	private ArrayList<String> time_hm_data = new ArrayList<String>(); // 紀錄手術資料的時間(小時:分鐘) 
	private ArrayList<Integer> time_m_data = new ArrayList<Integer>(); // 紀錄手術資料的時間(分鐘)
	
	private int[][] time;
	private int[] num;
	private int[] max;
	private int[] min;
	private float[] avg; 
	private float[] std;

	
	public Page2_Disease(String srcPath, String exportPath) throws IOException { 
		//讀取手術檔案
		readSurgicalFileBy(srcPath);  
		//轉換 time_hm_data 的時間格式 (以分鐘為單位) 
		timeChange(); 
		//計算各手術名稱的數量
		countNum(); 
		//紀錄每項手術花的所有時間
		recordEachSurgeryTime();
		//統計(最小值 最大值 平均時間 標準差)等數據
		recordStatistics();
		//匯出所有病患資料
		exportAllPatientData(exportPath); 
	}
	
	 
	/**
	 * 紀錄每項手術花的所有時間
	 * @return
	 */
	private void recordEachSurgeryTime() {  
		time = new int[allSurgery.size()][]; 
		for (int i = 0, length = allSurgery.size(); i < length; i++) {
			time[i] = new int[num[i]];
		}
		
		for (int i = 0, lengthData = Surgery_data.size(); i < lengthData; i++) {
			for (int j = 0, length = allSurgery.size(); j < length; j++) {
				int k = 0;
				if (Surgery_data.get(i).equals(allSurgery.get(j))) 
				{
					// 找到空白的位置，將時間填入
					while (time[j][k] != 0) k++; 
					time[j][k] = time_m_data.get(i);
				}
			}
		} 
		 
		for (int i = 0, length = time.length; i < length; i++) { // 將時間由小排到大
			Arrays.sort(time[i]);
		} 
	}
	   
	/**
	 * 計算各手術名稱的數量
	 * 
	 * @return
	 */
	private void countNum() {
		// 紀錄有幾筆資料
		num = new int[allSurgery.size()];

		for (int i = 0, lengthData = Surgery_data.size(); i < lengthData; i++) {
			for (int j = 0, length = allSurgery.size(); j < length; j++) {
				if (Surgery_data.get(i).equals(allSurgery.get(j))) {
					num[j]++;
					break;
				}
			}
		} 
	}
	
	/**
	 * 統計(最小值 最大值 平均時間 標準差)等數據
	 */
	private void recordStatistics() {  
		max = new int[allSurgery.size()];  // 紀錄最大值
		min = new int[allSurgery.size()];  // 紀錄最小值
		avg = new float[allSurgery.size()]; // 紀錄平均值
		std = new float[allSurgery.size()]; // 紀錄標準差
		
		for (int i = 0, length = time.length; i < length; i++)
		{
			max[i] = time[i][time[i].length - 1]; // 最大值
			min[i] = time[i][0]; // 最小值
			countAvg(time, avg, i, num);
			int total = countTotal(time, avg, i);
			std[i] = (float) Math.sqrt((double) total / num[i]);
			std[i] = (float) (Math.round(std[i] * 100)) / 100; // 四捨五入到小數第2位
		} 
	}
	
	/**
	 * 匯出所有病患資料
	 * @throws IOException
	 */
	private void exportAllPatientData(String exportPath) throws IOException {
		
		CSVWriter writer = new CSVWriter(exportPath); // 寫入CSV檔

		String[] tittle = { "科別", "手術名稱", "最小值", "最大值", "平均時間", "標準差", "次數" };
		writer.WriteNext(tittle);
 
		for (int i = 0, length = allSurgery.size(); i < length; i++) 
		{
			String[] contentRow = { SD.get(i), allSurgery.get(i), String.valueOf(min[i]), String.valueOf(max[i]),
					String.valueOf(avg[i]), String.valueOf(std[i]), String.valueOf(num[i]) };
			
			writer.WriteNext(contentRow);  
		}
		 
		writer.close();
	}

	private void FindAllDivision(ArrayList<String> allDivision, String curDivision) {
		if (!allDivision.contains(curDivision)) {// 找到所有科別
			allDivision.add(curDivision);
		}
	}

	private void FindAllSurgery(ArrayList<String> allSurgery, String curSurgery, String curDivision,
			ArrayList<String> SD) {// 找到所有術式與其所屬科別
		if (!allSurgery.contains(curSurgery)) {
			allSurgery.add(curSurgery);
			SD.add(curDivision);
		}
	}

	private int countTotal(int[][] time, float[] avg, int i) {
		int total = 0;
		for (int j = 0; j < time[i].length; j++) {
			total += (time[i][j] - avg[i]) * (time[i][j] - avg[i]);
		}
		return total;
	}

	private void countAvg(int[][] time, float[] avg, int i, int[] num) {
		for (int j = 0; j < time[i].length; j++) { // 平均數
			avg[i] += time[i][j];
		}
		avg[i] = avg[i] / num[i];
		avg[i] = (float) (Math.round(avg[i] * 100)) / 100; // 四捨五入到小數第2位

	}
	
	/**
	 *  轉換 time_hm_data 的時間格式
	 */
	private void timeChange() {
		for (int i = 0, length = Surgery_data.size(); i < length; i++) 
		{
			String[] time_split = time_hm_data.get(i).split(":");
			if (!time_split[0].isEmpty()) 
			{
				int time_m = Integer.valueOf(time_split[0]) * 60 + Integer.valueOf(time_split[1]);
				time_m_data.add(time_m);
			}
		} 
	}

	  
	/**
	 * 讀取手術檔案
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	private void readSurgicalFileBy(String filePath) throws IOException { 
		CSVReader reader = new CSVReader(filePath);
		
		// 讀取標題列 
		String[] nextLine = reader.readNext(); 
		for (int i = 0; i < nextLine.length; i++) { 
			// 將各標題與其所對應的index放入
			name_num.put(nextLine[i], i);
		}

		while ((nextLine = reader.readNext()) != null) 
		{
			if (nextLine[name_num.get("科別")].equals("")) 
			{
				break;
			}
				
			String curDivision = nextLine[name_num.get("科別")];
			String curSurgery = nextLine[name_num.get("手術名稱")];
			String curSurgeryTime = nextLine[name_num.get("手術時間")];
			Surgery_data.add(curSurgery);
			time_hm_data.add(curSurgeryTime);
			Disease_data.add(curDivision); 
			//找到所有科別
			FindAllDivision(allDivision, curDivision);
			//找到所有術式與其所屬科別
			FindAllSurgery(allSurgery, curSurgery, curDivision, SD);
		}
		reader.close(); 
	}
	 
}
