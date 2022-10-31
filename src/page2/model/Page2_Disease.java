package page2.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays; 
import java.util.HashMap;

public class Page2_Disease { 
	private HashMap<String, Integer> name_num = new HashMap<>(); // �����U���D�ҹ�����index
	private ArrayList<String> allSurgery = new ArrayList<String>(); // �����Ҧ���N�W��
	private ArrayList<String> allDivision = new ArrayList<String>(); // �����Ҧ���O
	private ArrayList<String> SD = new ArrayList<String>(); // ������N�W��&��O
	private ArrayList<String> Surgery_data = new ArrayList<String>(); // ������N��ƪ���N�W��
	private ArrayList<String> Disease_data = new ArrayList<String>(); // ������N��ƪ���N�W��
	private ArrayList<String> time_hm_data = new ArrayList<String>(); // ������N��ƪ��ɶ�(�p��:����) 
	private ArrayList<Integer> time_m_data = new ArrayList<Integer>(); // ������N��ƪ��ɶ�(����)
	
	private int[][] time;
	private int[] num;
	private int[] max;
	private int[] min;
	private float[] avg; 
	private float[] std;

	
	public Page2_Disease(String srcPath, String exportPath) throws IOException { 
		//Ū����N�ɮ�
		readSurgicalFileBy(srcPath);  
		//�ഫ time_hm_data ���ɶ��榡 (�H���������) 
		timeChange(); 
		//�p��U��N�W�٪��ƶq
		countNum(); 
		//�����C����N�᪺�Ҧ��ɶ�
		recordEachSurgeryTime();
		//�έp(�̤p�� �̤j�� �����ɶ� �зǮt)���ƾ�
		recordStatistics();
		//�ץX�Ҧ��f�w���
		exportAllPatientData(exportPath); 
	}
	
	 
	/**
	 * �����C����N�᪺�Ҧ��ɶ�
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
					// ���ťժ���m�A�N�ɶ���J
					while (time[j][k] != 0) k++; 
					time[j][k] = time_m_data.get(i);
				}
			}
		} 
		 
		for (int i = 0, length = time.length; i < length; i++) { // �N�ɶ��Ѥp�ƨ�j
			Arrays.sort(time[i]);
		} 
	}
	   
	/**
	 * �p��U��N�W�٪��ƶq
	 * 
	 * @return
	 */
	private void countNum() {
		// �������X�����
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
	 * �έp(�̤p�� �̤j�� �����ɶ� �зǮt)���ƾ�
	 */
	private void recordStatistics() {  
		max = new int[allSurgery.size()];  // �����̤j��
		min = new int[allSurgery.size()];  // �����̤p��
		avg = new float[allSurgery.size()]; // ����������
		std = new float[allSurgery.size()]; // �����зǮt
		
		for (int i = 0, length = time.length; i < length; i++)
		{
			max[i] = time[i][time[i].length - 1]; // �̤j��
			min[i] = time[i][0]; // �̤p��
			countAvg(time, avg, i, num);
			int total = countTotal(time, avg, i);
			std[i] = (float) Math.sqrt((double) total / num[i]);
			std[i] = (float) (Math.round(std[i] * 100)) / 100; // �|�ˤ��J��p�Ʋ�2��
		} 
	}
	
	/**
	 * �ץX�Ҧ��f�w���
	 * @throws IOException
	 */
	private void exportAllPatientData(String exportPath) throws IOException {
		
		CSVWriter writer = new CSVWriter(exportPath); // �g�JCSV��

		String[] tittle = { "��O", "��N�W��", "�̤p��", "�̤j��", "�����ɶ�", "�зǮt", "����" };
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
		if (!allDivision.contains(curDivision)) {// ���Ҧ���O
			allDivision.add(curDivision);
		}
	}

	private void FindAllSurgery(ArrayList<String> allSurgery, String curSurgery, String curDivision,
			ArrayList<String> SD) {// ���Ҧ��N���P����ݬ�O
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
		for (int j = 0; j < time[i].length; j++) { // ������
			avg[i] += time[i][j];
		}
		avg[i] = avg[i] / num[i];
		avg[i] = (float) (Math.round(avg[i] * 100)) / 100; // �|�ˤ��J��p�Ʋ�2��

	}
	
	/**
	 *  �ഫ time_hm_data ���ɶ��榡
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
	 * Ū����N�ɮ�
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	private void readSurgicalFileBy(String filePath) throws IOException { 
		CSVReader reader = new CSVReader(filePath);
		
		// Ū�����D�C 
		String[] nextLine = reader.readNext(); 
		for (int i = 0; i < nextLine.length; i++) { 
			// �N�U���D�P��ҹ�����index��J
			name_num.put(nextLine[i], i);
		}

		while ((nextLine = reader.readNext()) != null) 
		{
			if (nextLine[name_num.get("��O")].equals("")) 
			{
				break;
			}
				
			String curDivision = nextLine[name_num.get("��O")];
			String curSurgery = nextLine[name_num.get("��N�W��")];
			String curSurgeryTime = nextLine[name_num.get("��N�ɶ�")];
			Surgery_data.add(curSurgery);
			time_hm_data.add(curSurgeryTime);
			Disease_data.add(curDivision); 
			//���Ҧ���O
			FindAllDivision(allDivision, curDivision);
			//���Ҧ��N���P����ݬ�O
			FindAllSurgery(allSurgery, curSurgery, curDivision, SD);
		}
		reader.close(); 
	}
	 
}
