package page4.model;

import java.io.*; 
import java.util.*; 
import java.util.logging.Logger;

import page4.controller.Main;
 
////////////////////////////////////////////////////////////////////////////////////
// public CSVReader(String FilePath) �Ninput��FilePath�����ɮצ�m��J��BufferedReader��//
// public String[] readnext() ��XString�}�C �N�C�@�C�o�쪺���G�s�J�^�Ǫ��}�C��
// public ArrayList <String[]> readAll() �N��ƥ���Ū���æ^��ArrayList <String[]>
//  
// 
// 
// 
////////////////////////////////////////////////////////////////////////////////////
public class CSVReader { 
	
	private BufferedReader BR; 
	//�s��@�C�����
	private ArrayList<String> line;  
	//��z�X�C�@��r���
	private StringBuilder cell; 
	// �T�{�r��O�_�b���޸��� �p�G��true�N��b�޸��� false�Ϥ�
	Boolean quoteCheck = false; 
	
	private Logger logger;

	public CSVReader(File Src) {
		this(Src.toString());
	}

	public CSVReader(String FilePath) {
		setLogger();

		// �T�{�O�_��J��}
		if (FilePath == null || FilePath.isEmpty()) {
			logger.warning("�S����J�ɮצ�}");
			return;
		}

		int FESerch = FilePath.lastIndexOf("."); // ��M�̫�@��.����m�o�����ɦW��}
		String FileExtension = FilePath.substring(FESerch).toLowerCase();
		// �p�G��CSV�ɮ��~�� ���O�h��ܴ���
		if (!FileExtension.equals(".csv")) {
			logger.warning("���ɮ׫DCSV�ɮ�");
			return;
		}

		try {
			BR = new BufferedReader(new FileReader(FilePath));
		} catch (FileNotFoundException e) {
			logger.warning("���ɮ׫DCSV�ɮ�");
		}
	}

	public void close() throws IOException {
		BR.close();
	}

	public String[] readNext() { 
		String[] output = null; 
		
		try { 
			quoteCheck = false; 
			//�s��@�C�����
			line = new ArrayList<String>();  
			//��z�X�C�@��r���
			cell = new StringBuilder();
			 
			boolean eof = false; 
			// �p�G���O�b "" ���X�{����Ÿ��h����j�� 
			while (!eof) {	
				Character BRchar = getBR();
				
				if(BRchar == null) {
					eof = true; 
				}
				else if (BRchar == ',' && quoteCheck == false) {
					line.add(cell.toString()); 
					cell = new StringBuilder();
				} 
				else if (BRchar == '"') {
					eof = handleDoubleQuotes(); 
				} 
				else if (BRchar == '\n' && quoteCheck == false) {
					break;
				} 
				else if (BRchar == '\r' || BRchar == '\n') {
					continue;
				} 
				else {
					cell.append(BRchar);
				}    
			} 
			
			if(cell.length() != 0) line.add(cell.toString());  
			
			if(line.size() != 0) {
				output = line.toArray(new String[line.size()]);  
			} 
		} 
		catch (IOException e) 
		{
			logger.warning(e.getMessage()); 
		} 
		catch (NullPointerException e) 
		{  
			logger.warning(e.getMessage()); 
		}
		
		return output;
	}

	private boolean handleDoubleQuotes() throws IOException 
	{  	
		if (!quoteCheck) {
			quoteCheck = true;
			return false;
		}
		
		Character check = getBR(); 
		if(check == '"') {
			cell.append(check); 
		}
		else { 
			quoteCheck = false;
			
			line.add(cell.toString()); 
			cell = new StringBuilder(); 
			while (check != ',' && check != '\n') { 
				check = getBR();  
				if(check != null) return true;
			}
		}   
		
		return false;
	}
	 
	public ArrayList<String[]> readAll() {
		ArrayList<String[]> out = new ArrayList<String[]>();
		String[] S;
		while ((S = readNext()) != null) {
			out.add(S);
		}
		return out;
	}

	private Character getBR() throws IOException {
		int checkN = 0;
		checkN = BR.read();
		if (checkN == -1)
			return null;
		else
			return (char) checkN;
	}

	private void setLogger() {
		logger = Logger.getLogger(Main.class.getName());
	}

}