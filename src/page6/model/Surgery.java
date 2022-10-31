package page6.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Surgery implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private ArrayList<Double> data;
	private ArrayList<Double> err;
	
	public Surgery(String name){ 
		this.name = name;
		data = new ArrayList<>();
		err = new ArrayList<>();
	}
	
	public void addItem(double time){ 
		if(time > 0) data.add(time);
		else err.add(time);
	} 
	 
	public void Distribution(BufferedWriter output) throws IOException {  
		int[] num = new int[20];
		double max = 0;
		double spacing = 0;
		Arrays.fill(num, 0);
		
		if(data.isEmpty()) {
			data.sort(null);
			max = data.get(data.size() - 1);
			spacing = max / 20;
			for(Double i: data) {
				if(i == 0)continue;
				int index = (int)((double)i / spacing);
				if(index > 19)index = 19;
				num[index]++; 
			} 
		}
		 
		output.write(name + "\n");
		output.write("區間編號,區間次數\n");
		for(int i = 0; i < 20; i++) {
			output.write(String.format("{0},{1}%n", (i + 1), num[i]));
		}
		output.write("最長時間," + max + "\n");
		output.write("間距大小," + spacing + "\n"); 
		output.write("異常資料數量," + err.size()); 
		output.write("\n\n\n"); 
		output.flush(); 
	}
}
