package page2.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;

import page2.controller.Main;
 

//��@�ǦC�� ��X�ɮ׵L�k���[�ѤH�[�� 
//�Ȧ���Ƥ��R���\��

public class Distribution implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// �w�]���W��
	public String Day = "��N��";
	public String Room = "�ǧO";
	public String priority = "����";
	public String subject = "��O";
	public String surStart = "���M�ɶ�";
	public String surEnd = "�_�X���f";
	public String anesthesia = "�¾K";
	public String aneStart = "�¾K�}�l";
	public String aneEnd = "�¾K����";
	public String ArriveTime = "��F�ɶ�";
	public String EntryTime = "�J�Ǯɶ�";
	public String doctor = "�D�v��v";
	public String number = "�f����";

	// ��l���
	private ArrayList<String[]> Raw = new ArrayList<>();

	// ���o��l���
	public ArrayList<String[]> getRaw() {
		return Raw;
	}

	// �]�m��l���
	public void setRaw(ArrayList<String[]> src) {
		Raw = src;
	}

	// ��l������W�٬d��
	private HashMap<String, Integer> ColumnName;

	// ���o��l������W��
	public HashMap<String, Integer> getColumnName() {
		return ColumnName;
	}

	// �]�m��l������W��
	public void setColumnName(String Key, Integer value) {
		ColumnName.put(Key, value);
	}

	// ��l������@���}�C
	private String[] ColumnNameLine;

	// ���o��l������@���}�C
	public String[] getColumnNameLine() {
		return ColumnNameLine;
	}

	// �C��H�Ʋέp
	private double DailyAveg; // �C�饭���H��
	private double DailyMax; // �Y��̰��H��
	private double DailyMin; // �Y��̧C�H��

	private double DailyRAveg; // �Y��`�W�������H��

	public double getDailyRAveg() {
		return DailyRAveg;
	}

	// �������禡
	// initDaily()
	// saveDaily(TreeMap<String, Integer> S)
	// getDailyAveg()
	// getDailyMax()
	// getDailyMin()

	// �C�p�ɩ�F�ɶ��έp
	private double[] ArrivalTimeAveg = new double[24];
	// �������禡
	// initDaily()
	// getArrivalTimeAveg()

	// �C�p�ɱ`�W�M�o�;��v
	private double[] RArrivalAveg = new double[24];

	// ���o�C�p�ɨ�F�H�ƥ���
	public void setRArrivalAveg(double[] src) {
		RArrivalAveg = src;
	}

	// ���o�C�p�ɨ�F�H�ƥ���
	public double[] getRArrivalAveg() {
		return RArrivalAveg;
	}

	// �C�p�ɫ�M�@�ŵo�;��v
	private double[] EArrivalAveg = new double[24];

	public void setEArrivalAveg(double[] src) {
		EArrivalAveg = src;
	}

	public double[] getEArrivalAveg() {
		return EArrivalAveg;
	}

	// �C�p�ɫ�M�G�ŵo�;��v
	private double[] OArrivalAveg = new double[24];

	public void setOArrivalAveg(double[] src) {
		OArrivalAveg = src;
	}

	public double[] getOArrivalAveg() {
		return OArrivalAveg;
	}

	// �C���M�C�饭���H��
	private double EOArrivalDailyAvg;

	public void setEOArrivalDailyAvg(double src) {
		EOArrivalDailyAvg = src;
	}

	// ���o�C���M�C�饭���H��
	public double getEOArrivalDailyAvg() {
		return EOArrivalDailyAvg;
	}
	// �������禡
	// initDaily()
	// getArrivalTimeAveg()

	// �M�O ��N�u���v�έp
	private String[] priority_type;
	//private String[] priority_specify "E", "O"
	private double EArrival; // ��M�@�ŵo�;��v
	private double OArrival; // ��M�G�ŵo�;��v
	// �������禡
	// setPriority_type(String[] priority_type)
	// getPriority_type()
	// setEArrival(double EArrival)
	// setOArrival(double OArrival)
	// getEArrival()
	// getOArrival()
	// initEOArrival()

	// �¾K�ɶ������έp
	private ArrayList<String[]> ADistribution;
	private ArrayList<String[]> AStatistics;

	public void setAnesthesiaDistribution(ArrayList<String[]> src) {
		ADistribution = src;
	}

	public ArrayList<String[]> getAnesthesiaDistribution() {
		return ADistribution;
	}

	public void setAnesthesiaStatictis(ArrayList<String[]> src) {
		AStatistics = src;
	}

	public ArrayList<String[]> getAnesthesiaStatictis() {
		return AStatistics;
	}

	// ��F�H�Ƥ���
	private ArrayList<Integer> PeopleNumber = new ArrayList<Integer>();

	public void setPeopleNumber(ArrayList<Integer> src) {
		PeopleNumber = src;
	}

	public ArrayList<Integer> getPeopleNumber() {
		return PeopleNumber;
	}

	// ��N�ɶ������έp
	private ArrayList<String[]> SDistribution;
	private ArrayList<String[]> SStatistics;

	public void setSurgeryDistribution(ArrayList<String[]> src) {
		SDistribution = src;
	}

	public ArrayList<String[]> getSurgeryDistribution() {
		return SDistribution;
	}

	public void setSurgeryStatictis(ArrayList<String[]> src) {
		SStatistics = src;
	}

	public ArrayList<String[]> getSurgeryStatictis() {
		return SStatistics;
	}

	public String[] getSubjectName() {
		int SubjectCount = SStatistics.size();
		String[] SubjectName = new String[SubjectCount - 1];
		for (int row = 1; row < SubjectCount; row++) {
			SubjectName[row - 1] = SStatistics.get(row)[0];
		}
		return SubjectName;
	}

	// ���o�C�p�ɨ�F�H�ƥ���
	public void setArrivalTimeAveg(double[] ArrivalTimeAveg) {
		this.ArrivalTimeAveg = ArrivalTimeAveg;
	}

	public double[] getArrivalTimeAveg() {
		return ArrivalTimeAveg;
	}
	// ---------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------

	// �D�n�\��禡
	// �ӷ��ɮצW��
	private String FileName_src;

	// �]�w�ӷ��ɮצW��
	public void setSrcFileName(String src) {
		FileName_src = src;
		File f = new File(FileName_src);
		totalBytes = f.length();
	}

	// ���o�ӷ��ɮצW��
	public String getSrcFileName() {
		return FileName_src;
	}

	public Distribution() {
	}

	public Distribution(String src) throws IOException {
		setSrcFileName(src);
		DataLoding();
	}

	private double totalBytes = 0;
	private double gainBytes = 0;

	private void addProgress(String[] src) {
		for (String i : src) {
			gainBytes += i.getBytes().length + 1;
			double Progress = gainBytes / totalBytes * 100;
			Main.setLoadingText(FileName_src, Progress);
		}
	}

	private void DataLoding() throws IOException {
		CSVReader reader = new CSVReader(FileName_src); 
		Main.log(Level.INFO, "�w���J" + FileName_src);

		String[] Line = reader.readNext();
		addProgress(Line);
		// �L�o����r���ðO�����D�C
		ColumnNameLine = FixString.filter(Line, '\n');
		// �Ψӧֳt�M�����W��
		ColumnName = new HashMap<>();
		for (int i = 0; i < Line.length; i++) {
			ColumnName.put(Line[i], i);
		}

		if (ColumnName.containsKey(Day)) {
			Main.log(Level.INFO, "���W�٤w�ǰt");
		}

		// ���J��l���
		Raw = new ArrayList<>();
		while ((Line = reader.readNext()) != null) {
			Raw.add(Line);
			addProgress(Line);
		}

		Main.log(Level.INFO, "���Ū������");
		
		reader.close();
	}

	/**
	 * �]�w��������
	 * 
	 * **�ФU���_�I�d�ݼƭȬ��� �����ӦA�ϥ�system.out.printf()�Ӭd�ݭ�**
	 * 
	 * @param col �������ȶ��X
	 */
	public void setColumn(int[] col) {
		Day = ColumnNameLine[col[0]];
		Room = ColumnNameLine[col[1]];
		priority = ColumnNameLine[col[2]];
		subject = ColumnNameLine[col[3]];
		surStart = ColumnNameLine[col[4]];
		surEnd = ColumnNameLine[col[5]];
		anesthesia = ColumnNameLine[col[6]];
		aneStart = ColumnNameLine[col[7]];
		aneEnd = ColumnNameLine[col[8]];
	}

	public void create() throws IOException {
		initSurgeryRoom();

		initDaily();

		initEOArrival();

		count_Anesthesia();
		preprocessing(anesthesiaGroups);
		ADistribution = printDistribution(anesthesiaGroups);
		AStatistics = printStatistics(anesthesiaGroups);

		count_Surgery();
		preprocessing(surgeryData);
		SDistribution = printDistribution(surgeryData);
		SStatistics = printStatistics(surgeryData);
	}

	public static Distribution loadingFile(String FileName) throws IOException {
		CSVReader csv = new CSVReader(FileName);

		Distribution temp = new Distribution();
		temp.setSrcFileName(FileName);
		
		csv.readNext();
		String[] line = csv.readNext();
		temp.setDailyAveg(Double.parseDouble(line[0]));// �C�饭���H��
		temp.setDailyMax(Double.parseDouble(line[1])); // �C��̰��H��
		temp.setDailyMin(Double.parseDouble(line[2])); // �C��̧C�H��
		temp.setEArrival(Double.parseDouble(line[3])); // ��M�@�ž��v
		temp.setOArrival(Double.parseDouble(line[4])); // ��M�G�ž��v
		temp.setEOArrivalDailyAvg(Double.parseDouble(line[5]));// ��M��F���v
		
		// �`�W�M��F���v
		csv.readNext(); 
		line = csv.readNext();
		double[] r = new double[line.length];
		for (int i = 0; i < r.length; i++)
			r[i] = Double.valueOf(line[i]);
		temp.setRArrivalAveg(r);
		
		// ��M�@�ũ�F���v
		csv.readNext(); 
		line = csv.readNext();
		r = new double[line.length];
		for (int i = 0; i < r.length; i++)
			r[i] = Double.valueOf(line[i]);
		temp.setEArrivalAveg(r);

		// ��M�G�ũ�F���v
		csv.readNext(); 
		line = csv.readNext();
		r = new double[line.length];
		for (int i = 0; i < r.length; i++)
			r[i] = Double.valueOf(line[i]);
		temp.setOArrivalAveg(r);
		
		// ��N�ЦW��
		csv.readNext(); 
		line = csv.readNext();
		temp.setRoomName(line);
		
		// ������F�ɶ�
		csv.readNext();
		line = csv.readNext();
		double[] ATA = new double[line.length];
		for (int i = 0; i < ATA.length; i++)
			ATA[i] = Double.valueOf(line[i]);
		temp.setArrivalTimeAveg(ATA);
		
		// ��N�ɶ�����
		csv.readNext();
		line = csv.readNext();
		int T = Integer.parseInt(line[0]);
		ArrayList<String[]> SD = new ArrayList<String[]>();
		while ((T--) != 0)
			SD.add(csv.readNext());
		temp.setSurgeryDistribution(SD);

		// ��N�ɶ��έp
		csv.readNext();
		line = csv.readNext();
		T = Integer.parseInt(line[0]);
		ArrayList<String[]> SS = new ArrayList<String[]>();
		while ((T--) != 0)
			SS.add(csv.readNext());
		temp.setSurgeryStatictis(SS);

		// �¾K�ɶ�����
		csv.readNext();
		line = csv.readNext();
		T = Integer.parseInt(line[0]);
		ArrayList<String[]> AD = new ArrayList<String[]>();
		while ((T--) != 0)
			AD.add(csv.readNext());
		temp.setAnesthesiaDistribution(AD);

		// �¾K�ɶ��έp
		csv.readNext();
		line = csv.readNext();
		T = Integer.parseInt(line[0]);
		ArrayList<String[]> AS = new ArrayList<String[]>();
		while ((T--) != 0)
			AS.add(csv.readNext());
		temp.setAnesthesiaStatictis(AS);

		csv.close();
		return temp;
	}

	public static void saveAsFile(Distribution data, String FileName) throws IOException {
		CSVWriter csv = new CSVWriter(FileName);

		String[] title = new String[1];

		String[] temp = { "�C�饭���H��", "�C��̰��H��", "�C��̧C�H��", "��M�@�ŵo�;��v", "��M�G�ŵo�;��v", "��M��F���v" };
		csv.WriteNext(temp);
		temp[0] = String.valueOf(data.getDailyAveg());
		temp[1] = String.valueOf(data.getDailyMax());
		temp[2] = String.valueOf(data.getDailyMin());
		temp[3] = String.valueOf(data.getEArrival());
		temp[4] = String.valueOf(data.getOArrival());
		temp[5] = String.valueOf(data.getEOArrivalDailyAvg());
		csv.WriteNext(temp);

		title[0] = "�`�W�M��F���v";
		csv.WriteNext(title);
		double[] r = data.getRArrivalAveg();
		temp = new String[r.length];
		for (int i = 0; i < r.length; i++)
			temp[i] = String.valueOf(r[i]);
		csv.WriteNext(temp);

		title[0] = "��M�@�ũ�F���v";
		csv.WriteNext(title);
		r = data.getEArrivalAveg();
		temp = new String[r.length];
		for (int i = 0; i < r.length; i++)
			temp[i] = String.valueOf(r[i]);
		csv.WriteNext(temp);

		title[0] = "��M�G�ũ�F���v";
		csv.WriteNext(title);
		r = data.getOArrivalAveg();
		temp = new String[r.length];
		for (int i = 0; i < r.length; i++)
			temp[i] = String.valueOf(r[i]);
		csv.WriteNext(temp);
		// ----------------------------------------------------------------
		// ----------------------------------------------------------------
		title[0] = "��N�ЦW��";
		csv.WriteNext(title);
		csv.WriteNext(data.getRoomName());

		title[0] = "������F�ɶ�";
		csv.WriteNext(title);
		double[] ATA = data.getArrivalTimeAveg();
		temp = new String[ATA.length];
		for (int i = 0; i < ATA.length; i++)
			temp[i] = String.valueOf(ATA[i]);
		csv.WriteNext(temp);

		title[0] = "��N�ɶ�����";
		csv.WriteNext(title);
		ArrayList<String[]> SD = data.getSurgeryDistribution();
		title[0] = String.valueOf(SD.size());
		csv.WriteNext(title);
		for (String[] i : SD)
			csv.WriteNext(i);

		title[0] = "��N�ɶ��έp";
		csv.WriteNext(title);
		ArrayList<String[]> SS = data.getSurgeryStatictis();
		title[0] = String.valueOf(SS.size());
		csv.WriteNext(title);
		for (String[] i : SS)
			csv.WriteNext(i);

		title[0] = "�¾K�ɶ�����";
		csv.WriteNext(title);
		ArrayList<String[]> AD = data.getAnesthesiaDistribution();
		title[0] = String.valueOf(AD.size());
		csv.WriteNext(title);
		for (String[] i : AD)
			csv.WriteNext(i);

		title[0] = "�¾K�ɶ��έp";
		csv.WriteNext(title);
		ArrayList<String[]> AS = data.getAnesthesiaStatictis();
		title[0] = String.valueOf(AS.size());
		csv.WriteNext(title);
		for (String[] i : AS)
			csv.WriteNext(i);

		csv.close();
	}

	// ---------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------
	// ��N�ЦW��
	private String[] RoomName;

	public void setRoomName(String[] RoomName) {
		this.RoomName = RoomName;
	}

	public String[] getRoomName() {
		return RoomName;
	}

	private void initSurgeryRoom() throws IOException {
		TreeSet<String> temp = new TreeSet<String>();
		if (ColumnName.containsKey(Room)) {
			int col_room = ColumnName.get(Room);
			for (String[] i : Raw)
				temp.add(i[col_room]);
			// �h���Ŧr��
			temp.remove("");
			// �ϥΤ@���}�C�s��
			RoomName = (String[]) temp.toArray(new String[temp.size()]);
		} else {
			Main.log(Level.INFO, "�L�k�إߤ�N�ǦW��!!");
		}
	}

	/**
	 * �p��U�Ӥ�N���¾K�ɶ��è̷ӳ¾K��������
	 */
	private TreeMap<String, ArrayList<Double>> anesthesiaGroups;

	public void count_Anesthesia() {
		anesthesiaGroups = new TreeMap<>();

		int StartA = this.ColumnName.get(aneStart);
		int EndA = this.ColumnName.get(aneEnd);
		int NameA = this.ColumnName.get(anesthesia);

		for (String[] row : Raw) {
			String name = row[NameA];
			double time = Time.LengthMin(row[StartA], row[EndA]);

			if (name.equals("") || time <= 0) continue;
			
			anesthesiaGroups.computeIfAbsent(name, k -> new ArrayList<Double>());
			anesthesiaGroups.get(name).add(time);
		}
	}

	/**
	 * ��N�ɶ��έp
	 */
	private TreeMap<String, ArrayList<Double>> surgeryData;

	public void count_Surgery() {
		surgeryData = new TreeMap<>();

		int StartS = this.ColumnName.get(surStart);
		int EndS = this.ColumnName.get(surEnd);
		int NameS = this.ColumnName.get(subject);

		for (String[] row : Raw) {
			String name = row[NameS];
			double time = Time.LengthMin(row[StartS], row[EndS]);
			
			if (name.equals("") || time <= 0) continue;

			surgeryData.computeIfAbsent(name, k-> new ArrayList<Double>());
			surgeryData.get(name).add(time);
		}
	}

	/**
	 * �������s��
	 */
	private TreeMap<String, Double> Outliers = new TreeMap<>();

	/**
	 * ��ƫe�B�z(�Ƨ� + �R�����s��)
	 * 
	 * @param src ��ƨӷ�
	 */
	private void preprocessing(TreeMap<String, ArrayList<Double>> src) {
		// ���s�������s��
		Outliers.clear();

		for (Entry<String, ArrayList<Double>> entry : src.entrySet()) {
			ArrayList<Double> values = entry.getValue();
			// ��ƥѤp��j�Ƨ�
			values.sort(null);
			
			Main.log(Level.INFO, entry.getKey() + " ����N�ɶ��w�Ƨ�%n");
			// �D�X���s�Ȭɽu
			int Q1 = (int) (0.25 * values.size());
			int Q3 = (int) (0.75 * values.size());
			double upLimit = 1.5 * (values.get(Q3) - values.get(Q1)) + values.get(Q3);
			Outliers.put(entry.getKey(), upLimit);

			// �R�����s��
			for (int j = values.size() - 1; j >= 0; j--) {
				if (values.get(j) <= upLimit)
					break;
				values.remove(j);
			}
		}
	}

	private ArrayList<String[]> printDistribution(TreeMap<String, ArrayList<Double>> src) {
		ArrayList<String[]> distribution = new ArrayList<>();

		String[] dataRow = { "�϶��s��" };
		distribution.add(dataRow);

		int max_group = 0;
		for (Entry<String, ArrayList<Double>> entry : src.entrySet()) {
			int[] row = getDistribution(entry.getValue());
			if (max_group < row.length)
				max_group = row.length;

			dataRow = new String[row.length + 1];
			dataRow[0] = entry.getKey();
			for (int i = 0; i < row.length; i++) {
				dataRow[i + 1] = String.valueOf(row[i]);
			}

			distribution.add(dataRow);
		}

		dataRow = new String[max_group + 1];
		dataRow[0] = "�϶��s��";
		for (int i = 1; i < dataRow.length; i++) {
			dataRow[i] = String.valueOf(i);
		}

		distribution.set(0, dataRow);
		return distribution;
	}

	public static int[] getDistribution(ArrayList<Double> value) {
		int n = value.size();
		int group = (int) Math.round(1 + 3.322 * Math.log10(n));

		int[] num = new int[group];
		Arrays.fill(num, 0);

		double max = value.get(n - 1);
		double space = max / group;

		for (Double i : value) {
			if (i == 0)
				continue; // ��N�ɶ���0�h���p

			int index = (int) (i / space);
			if (index >= group)
				index = group - 1;
			num[index]++;
		}
		return num;
	}

	public static int[] getDistribution(ArrayList<Double> values, int group, double space) {
		int[] count = new int[group];

		for (Double k : values) {
			int index = (int) (k / space);
			if (index >= group)
				index = group - 1;
			count[index]++;
		}

		return count;
	}

	/**
	 * 
	 * @param src �C�@�C�������A�C�@�欰�έp�ؼ�
	 * @return
	 */
	private ArrayList<String[]> printStatistics(TreeMap<String, ArrayList<Double>> src) {

		ArrayList<String[]> statictis = new ArrayList<>(); // �έp��
		String[] colName = { "�W��", "�̤j��", "�̤p��", "����", "������", "�зǮt", "����", "����", "���s�Ȭɽu" };
		statictis.add(colName);

		for (Entry<String, ArrayList<Double>> entry : src.entrySet()) {
			ArrayList<Double> value = entry.getValue();
			
			int len = value.size();
			double min = 0;
			double max = 0;
			double mode = 0;
			double avg = 0;
			double dif = 0;

			if (len != 0) {
				min = value.get(0);
				max = value.get(len - 1);
				mode = searchMode(value);
				avg = calculateAvg(value);
				dif = calculateStd(value, avg);
			}

			String[] row = new String[colName.length];
			row[0] = entry.getKey();
			row[1] = String.valueOf(max);
			row[2] = String.valueOf(min);
			row[3] = String.valueOf(mode);
			row[4] = String.valueOf(avg);
			row[5] = String.valueOf(dif);
			row[6] = String.valueOf(len);
			row[7] = "";
			row[8] = String.valueOf(Outliers.get(entry.getKey()));
			
			statictis.add(row);
		}
		return statictis;
	}

	private double calculateAvg(ArrayList<Double> numbers) {
		double avg = 0;
		for (Double num : numbers)
			avg += num;
		avg /= numbers.size();
		return avg;
	}

	private double calculateStd(ArrayList<Double> numbers, double avg) {
		double std = 0;
		for (Double num : numbers) {
			std += (num - avg) * (num - avg);
		}
		std = Math.sqrt(std / numbers.size());

		return std;
	}

	/**
	 * ���ơ]mode�^���@�ռƾڤ��X�{���Ƴ̦h���ƾڭȡC
	 * �Ҧp{8�A7�A7�A8�A6�A5�A5�A8�A8�A8}���A�X�{�̦h���O8�A�]�����ƬO8�A���ƥi��O�@�Ӽơ]�ƾڭȡ^�A
	 * ���]�i��O�h�Ӽơ]�ƾڭȡ^�C�Y�ƾڪ��ƾڭȥX�{���ƬۦP�B�L��L�ƾڭȮɡA�h���s�b���ơC
	 * �Ҧp{5�A2�A8�A2�A5�A8}���A2�B5�B8�X�{���ƬۦP�B�S����L�ơA�]�����ƾڤ��s�b���ơC
	 * 
	 * �b�έp�ǤW�A���ƩM�����ơB����������A���O������H���ܼƦ��������Ͷժ����n��T�C �b���������]�`�A�����^���A���Ƭ��p�ȩҦb����m�A�M�����ơB����ƬۦP�C
	 * ���Y�����O���װ��פ����A���ƥi��|�M�����ơB����Ʀ��ܤj���t���C
	 * 
	 * �����������Ƥ��@�w�u���@�ӡA�Y���v��q��Ʃξ��v�K�ר�Ʀbx1, x2�K�K���h���I�����̤j�ȡA�N�|���h�Ӳ��ơA
	 * �̷��ݪ����άO���������ä����A�Ҧ����I���v���ۦP�A�Ҧ����I���O���ơC�Y���v�K�ר�Ʀ��ƭӧ����̤j�ȡA
	 * �@��|�N�o�X�ӷ��ȳ��٬����ơA���s����v�����|�٬��h�p�����]�M��p�ʬۤϡ^�C
	 * 
	 * @param numbers �䤺�e�����Ҭ�����ƨåB�Ѥp��j�ƧǡC
	 * 
	 * @return �^�ǧ�쪺���ơA�p�G���h�Ӳ��ơC
	 */
	private double searchMode(ArrayList<Double> numbers) {
		if (numbers.size() == 0) {
			Main.log(Level.INFO, "Distribution.searchMode - �Ӱ}�C�S������ƭ�!");
			return 0;
		}

		ArrayList<Double> mode = new ArrayList<>();
		double curMode = numbers.get(0);
		if (curMode <= 0) {
			Main.log(Level.INFO, "Distribution.searchMode - �o�{�Ӱ}�C���D����ƪ���!\n");
			return 0;
		}

		int maxAmount = 0;
		int curAmount = 0;

		for (Double num : numbers) {
			if (curMode != num) {
				if (maxAmount < curAmount) {
					mode.clear();
					mode.add(curMode);
					maxAmount = curAmount;
				} else if (maxAmount == curAmount) {
					mode.add(curMode);
				}

				curAmount = 1;
				curMode = num;
			} else {
				curAmount++;
			}
		}

		if (mode.size() <= 0) {
			return 0; 
		}
			
		if (maxAmount * mode.size() < numbers.size()) {
			return mode.get((mode.size() - 1) / 2);
		}
		
		return 0;
	}

	// -----------------------------------------------------------------------------------------------
	// ---------DailyAvegN----------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	private TreeMap<String, Integer> S;
	//private String MaxDay  �Y��̰��H�Ƥ��
	//private String MinDay  �Y��̧C�H�Ƥ��

	public void setDailyAveg(double DailyAveg) {
		this.DailyAveg = DailyAveg;
	}

	public double getDailyAveg() {
		return DailyAveg;
	}

	public void setDailyMax(double DailyMax) {
		this.DailyMax = DailyMax;
	}

	public double getDailyMax() {
		return DailyMax;
	}

	public void setDailyMin(double DailyMin) {
		this.DailyMin = DailyMin;
	}

	public double getDailyMin() {
		return DailyMin;
	}

	private int unrecognizable;

	public int getUnrecognizable() {
		return unrecognizable;
	}

	private void initDaily() {
		S = new TreeMap<String, Integer>();
		DailyAveg = 0.0;
		DailyMax = 0.0;
		DailyMin = Double.MAX_VALUE;

		unrecognizable = 0; 
		
		int DayColumn = getColumnValueBy(Day); 
		int priorityCol = getColumnValueBy(priority); 
		int ArrivalTimeCol = getColumnValueBy(ArriveTime); 
		
		int DailyAmount = 1;
		
		String curDate = ""; // �����o�@����
		String oldcurDate = ""; // �����W�@����
		String PriorityData = "";
		
		// �έp��Ʃ�F�ɶ�
		double[] RArrivalStatistic = new double[24];
		// �έp��M�@�Ũ�F����
		double[] EArrivalStatistic = new double[24];
		// �έp��M�G�Ũ�F����
		double[] OArrivalStatistic = new double[24];
  
		for (String[] Line : Raw) {
			int time = 0;
			// �N�ɶ��ഫ������
			time = CountTime(Line[ArrivalTimeCol]);

			// �p�G�ثe�o�C����������ŭ� �h�N��Ө�U�@��
			if (!Line[DayColumn].equals(""))
				curDate = Line[DayColumn];
			
			PriorityData = Line[priorityCol];
			// �]�Ĥ@�Ӹ�ƨS���W�@�� �ҥH�N���]�m������
			if (oldcurDate.equals(""))
				oldcurDate = Line[DayColumn];

			try {
				// �p�Gtime����1440�B��������h�[�J�έp
				if (time < 1440 && !Time.isWeekend(curDate)) {
					if (PriorityData.equals("E"))
						EArrivalStatistic[time / 60] += 1;
					else if (PriorityData.equals("O"))
						OArrivalStatistic[time / 60] += 1;
					else if (PriorityData.equals("R"))
						RArrivalStatistic[time / 60] += 1;
				}

				// �p�G�s��Ƭ����� �¸�Ƭ�����h�O���¸��
				if (Time.isWeekend(curDate)) {
					if (!Time.isWeekend(oldcurDate)) {
						save(oldcurDate, DailyAmount);
						curDate = Line[DayColumn];
						DailyAmount = 1;
					}
				}
				// �p�G��l���ũάO�M�W�@��Ū������Ƥ@�˫h�ƶq+1
				else if ((Line[DayColumn].equals("") || Line[DayColumn].equals(oldcurDate))) {
					DailyAmount++;
				}
				// �p�¸�ƫD����B���ŦX�H�W����h�����¸��
				else if (!Time.isWeekend(oldcurDate)) {
					save(oldcurDate, DailyAmount);
					curDate = Line[DayColumn];
					DailyAmount = 1;
				}
				// �p�G���ŦX�H�W�Ҧ����p�h��s���
				else {
					curDate = Line[DayColumn];
					DailyAmount = 1;
				}
				
				// �p�G�s��Ƥ����Ů�(�¸�ƦP�@��)�h�N�¸�ƨ�s���s���
				if (!curDate.equals("")) {
					oldcurDate = curDate;
				}

			} catch (ParseException e) {
				unrecognizable++;
			}
		}

		// �p�G�̫�@�Ѥ�������h�O�����
		try {
			if (!Time.isWeekend(curDate))
				save(curDate, DailyAmount);
		} catch (ParseException e) {
			unrecognizable++;
		}

		// �p��C��f�w�Ӽƥ�����
		DailyRAveg = 0;
		EOArrivalDailyAvg = 0.;
		// �p��X�C�p�ɥ����P��M��F�e��
		for (int i = 0; i < RArrivalStatistic.length; i++) {
			EArrivalAveg[i] = (EArrivalStatistic[i] / S.size());

			OArrivalAveg[i] = (OArrivalStatistic[i] / S.size());

			RArrivalAveg[i] = (RArrivalStatistic[i] / S.size());

			EOArrivalDailyAvg += (EArrivalStatistic[i] + OArrivalStatistic[i]);
			DailyAveg += EArrivalAveg[i] + OArrivalAveg[i] + RArrivalAveg[i];
			DailyRAveg += RArrivalAveg[i];
		}
		EOArrivalDailyAvg /= S.size();
	}

	private int getColumnValueBy(String colName) { 
		if (ColumnName.containsKey(colName)) {
			return ColumnName.get(colName);
		}
		return -1;
	}
	
	
	// �N��ƪ��ɶ��ഫ������
	private int CountTime(String str) {
		int time = 0;
		// �p�G�r�ꤺ�e�����ſ�X�ƭ� �_�h��X1440
		if (!str.equals("")) {
			time = ((int) str.charAt(0) - 48) * 10 * 60; // �N�ɶ��ন����
			time = time + ((int) str.charAt(1) - 48) * 60;
			time = time + ((int) str.charAt(3) - 48) * 10;
			time = time + ((int) str.charAt(4) - 48);

			return time;
		} else
			return 1440;
	}

	private void save(String curDate, Integer dailyAmount) {

		if (dailyAmount != 0) {
			PeopleNumber.add(dailyAmount);

			if (DailyMax < dailyAmount) {
				//MaxDay  curDate
				DailyMax = dailyAmount;
			}

			if (DailyMin > dailyAmount) {
				//MinDay  curDate
				DailyMin = dailyAmount;
			}

			S.put(curDate, dailyAmount);
		}
	}

	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------------------
	public void setPriority_type(String[] priority_type) {
		this.priority_type = priority_type;
	}

	public String[] getPriority_type() {
		return priority_type;
	}

	public void setEArrival(double EArrival) {
		this.EArrival = EArrival;
	}

	public double getEArrival() {
		return EArrival;
	}

	public void setOArrival(double OArrival) {
		this.OArrival = OArrival;
	}

	public double getOArrival() {
		return OArrival;
	}

	public void initEOArrival() {
		if (ColumnName.containsKey(priority)) {
			int col_pri = ColumnName.get(priority);
			int E = 0;
			int O = 0;
			int Total = 0;

			for (String[] i : Raw) {
				if (i[col_pri].equals("E"))
					E++;
				else if (i[col_pri].equals("O"))
					O++;
				if (!i[col_pri].isEmpty())
					Total++;
			}

			if (Total == 0) {
				EArrival = OArrival = 0;
			} else {
				EArrival = (double) E / Total;
				OArrival = (double) O / Total;
			}
		} else {
			Main.log(Level.INFO, "Distribution-initEOArrival �䤣�����ӵL�k�إߤM�O���v!!");
		}
	}
}
