package page3.model;
/*
	SSS - Simulation Subroutine Set. Version 1.50
	Copyright (C) M. A. Pollatschek 1994.  All rights reserved.
	
	Copyright covers all alterations and renditions of this source
	file as for example, but not limited to, OBJect, LIBrary or Quick
	LiBrary files derived this source. Copyright does not extend,
	however, to EXEecutable files derived from this source.
	
	               IMPORTANT NOTE
	
	THIS  FILE,   ITS USE,   OPERATION  AND  SUPPORT  IS  PROVIDED  "AS  IS"
	WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING,BUT
	NOT LIMITED TO,  THE IMPLIED WARRANTIES  OF MERCHANTABILITY AND  FITNESS
	FOR A  PARTICULAR  PURPOSE.   THE  ENTIRE RISK  AS  TO THE  QUALITY  AND
	PERFORMANCE  OF  THIS  FILE  IS  WITH  THE  USER.    IN NO  EVENT  SHALL
	THE AUTHOR AND/OR THE PUBLISHER  BE  LIABLE  FOR ANY  DAMAGES INCLUDING,
	WITHOUT LIMITATION,  ANY  LOST PROFITS, LOST SAVINGS OR OTHER INCIDENTAL
	OR CONSEQUENTIAL  DAMAGES  ARISING  THE  USE  OR  INABILITY  TO USE THIS
	FILE,  EVEN  IF  THE  AUTHOR  AND/OR  THE PUBLISHER  BEEN ADVISED OF THE
	POSSIBILITY  OF SUCH  DAMAGES  OR  FOR  ANY  CLAIM  BY  ANY OTHER PARTY.
*/

import java.io.BufferedWriter; 
import java.io.FileWriter;
import java.io.IOException; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;

import page3.controller.Main;

public class JSSS {  
	
	private String strSystem = "system";
	private String strSched  = "SCHED";
	private String strRemvfq = "REMVFQ";
	private String strRemvfc = "REMVFC";
	private String strQueue  = "QUEUE";
	private String strInista = "INISTA";
	private String strInique = "INIQUE";
	 
	private final double EPSI = 1.5258789e-5; /* 2 to the power of -16 */ 
	
	private int ERR_CODE = 3; 
	private int ERR_RET  = 0; 
	
	private long current_rnd = 1897; 
	private int  ANTI = 0; 
	
	private queue calendar = null; 
	
	private double     tnow            = 0.0; 
	private int        ID_CODE         = 0;
	private int 	   EV_CODE		   = 0;
	
	private int        NQUEUE          = 0; 
	private int        NATTR           = 0; 
	private int        STATN           = 0; 
	
	private queue      []q             = null; 
	private double     []present_entity= null; 
	private double     []prev_entity   = null; 
	private double     zero_entity     = 0.0; 
	private statistics []stat_root     = null; 
	 
	private int f = 0; 		//Using by pre_RN()
	private double t = 0;
	/*********  Function prototypes *************/ 
	public int getSTATN() { return STATN; }
	 
	int SIMERR() {
		int i = ERR_RET;
	    ERR_RET = 0;
	    return(i);
	}
	
	public JSSS() {

	}
	
	
	public JSSS(long random) {
		current_rnd=random;
	}
	
	public void exit(int i){
		throw new UnsupportedOperationException();
	} 
	
	public void error_exit(int error_code, String comes_from) { 
		String[] errorMsg = {
			"In %s there is a current entity already in the system - use DISPOS",
		    "In %s there is no more space in memory - reduce number of entities",
		    "In %s attribute should be a pointer",
		    "In %s queue number is too big or negative - consult INIQUE",
		    "In %s no entity with specified rank and/or queue number",
		    "In %s there is no current entity - use NEXTEV or REMVFx before",
		    "In %s no initialization has been performed - use INIQUE",
		    "In %s statistics index is too big or negative - consult INIQUE",
		    "In %s statistics has already been set - do not use INISTA twice",
		    "In %s summary file cannot be opened",
		    "In %s basic parameters have already been set - do not INIQUE twice",
		    "In %s too large rank for queue or calendar",
		    "In %s parameters should be positive",
		    "In %s parameters should be strictly ordered: min<mode<max",
		    "In %s attribute number is too big or negative - consult INIQUE",
		    "In %s event code should be greater than 1",
		    "In %s no statistic is defined for indicated index - use INISTA"
		};
		
		String msg = ((error_code) > 0 && (error_code) <= errorMsg.length) ? String.format(errorMsg[error_code-1], comes_from): "";
		
		if ((ERR_CODE & 2) == 2) { 
		    Main.log(Level.WARNING, msg);
		}
		if ((ERR_CODE & 1) == 1) {
			exit(error_code);
		}
		else {
			ERR_RET = error_code;
		}
	}
	
	public void SETANT(int y) {  
		if ((y == 0) || (y == 1)) ANTI = y;
	}
	 
	public void SETSEE(int i) {
		if (i > 0) current_rnd = (long)i;
	}
	//-----------------------------------------------------------------------------
	//分布種類------------------------------------------------------------------------
	//1
	public double RA() {
	    final long rnd_a = 293, rnd_c = 57111; 
	    long current = current_rnd * rnd_a + rnd_c;
	    long aaa = current >> 16 ;
	    if (aaa != 0) current -= (aaa << 16);
	    current_rnd = current;
	    
	    double r = (double)current * EPSI;
	    if (!((r > 0.0) && (r < 1.0))) {
	    	if (!(r > 0.0)) r = EPSI;
	    	if (!(r < 1.0)) r = 1.0 - EPSI;
	    } 
	    if (ANTI != 0) r = (1.0 - r);
	    return r;
	}
	//2
	public double UN(double minimum, double maximum) {  
	    double x = minimum + RA() * (maximum - minimum); 
	    return(x);
	}
	//3
	public double EX(double mean) {  
	    //parameters should be positive.
	    if (mean < 0.0) { error_exit(13,"EX"); return(0.0); }
	    double x = - (mean) * Math.log( RA() ) ; 
	    return(x);
	}
	//4
	public double TR(double minimum, double mode, double maximum) {  
	    //parameters should be strictly ordered: min<mode<max.
	    if ((maximum < mode) || (mode < minimum) || (minimum == maximum)) { 
	    	error_exit(14,"TR"); return(0.0); 
	    }
	    if (maximum - minimum < 0.001) {   
	    	return(mode); 
	    }
	    double x = (mode - minimum) / (maximum - minimum); 
	    double r = RA();
	    double y;
	    if (r <= x) y = minimum + Math.sqrt((mode - minimum) * (maximum - minimum) * r);
	    else        y = maximum - Math.sqrt((maximum - mode) * (maximum - minimum) * (1.0 - r));
	     
	    return(y);
	}
	//5
	public int NP(double mean) {  
		//parameters should be positive.
		if (mean <= 0.0) { error_exit(13,"NP"); return 0; }
		double a = Math.exp(-(mean)), r = RA();
		int n = 0;  
		while (a <= r) { 
			++n; 
			r = r * RA();
		} 
		
		return (n);
	}
	//6
	public double ER(double expon_mean, int k) { 
		//parameters should be positive.
		if ((expon_mean <= 0.0) || (k <= 0)) { error_exit(13,"ER"); return(0.0); }
		double a = 0.0;  
		for(int i = 1; i <= k; ++i) {
			a += EX(expon_mean);
		}
		
	    return(a);
	}
	//7
	public double GA(double beta, double alpha) { 
		//parameters should be positive.
		if ((beta <= 0.0) || (alpha <= 0.0)) { error_exit(13,"GA"); return(0.0); }
		double yy = 0;   
		 
		if (alpha > 0.0 && alpha < 1.0) {
			yy = GA1(beta, alpha);
		}
		else if(alpha >= 1.0 && alpha < 5.0) {
			yy = GA2(beta, alpha);
		}
		else if(alpha >= 5.0) {
			yy = GA3(beta, alpha);
		}
		 
		return(yy);
	}
	
	private double GA1(double beta, double alpha) { 
		double x = 1.0, y = 1.0;
		
		while((x + y > 1.0) || (x + y == 0.0)) { 
			x = Math.pow(RA(), (1.0 / alpha)); 
			y = Math.pow(RA(), (1.0 / (1.0 - alpha)));
		}
		
		double w = x / (x + y);
		return (w * EX(1.0) * (beta));  
	}
	
	private double GA2(double beta, double alpha) { 
		int a = (int) Math.floor(alpha); 
		double b = alpha - (double)a;
		double x = (alpha / a) * ER(1.0, a);
		
		if (alpha == 1.0) 
		{ 
			while(RA() > Math.exp(-x)) x = ER(1.0, 1);  
		} 
		else while(RA() > (Math.pow(x / alpha, b) * Math.exp(-b * (x / alpha - 1.0))))
		{
			x = (alpha / a) * ER(1.0, a);
		}
		
		return (x * beta);
	}
	
	private double GA3(double beta, double alpha) { 
		int a = (int) Math.floor(alpha);
		if (RA() >= alpha - a) { 
			return ER(beta, a);
		}
		else { 
			a++; 
			return ER(beta, a);
		}
	}
	
	//8
	public double BE(double theta, double phi) {  
		//parameters should be positive.
		if ((theta <= 0.0) || (phi <= 0.0)) { error_exit(13,"BE"); return(0.0); }
	 
		double g1 = GA(1.0, theta); 
		double g2 = GA(1.0, phi), y; 
		if (g1 + g2 == 0.0) y = BE(theta, phi); 
		else y = g1 / (g1 + g2);
	   
	    return(y);
	}
	//9
	public double pre_RN() {
	     
	    double y; 
	    if (f == 0) {
		    double a = 2.0 * RA() - 1.0;    
		    double b = 2.0 * RA() - 1.0; 
		    double w = a * a + b * b;
		    if ((w == 0.0) || ( w > 1.0 )) y = pre_RN(); 
		    else {
			    t = a * Math.sqrt(-2.0 * Math.log(w) / w);  
		  	    y = b * Math.sqrt(-2.0 * Math.log(w) / w);
			    f = 1;
		    }
	    }
	    else {  
		    f = 0;
		    y = t; 
	    }
	     
	    return(y);
	}
	//10
	public double RN(double mean, double sd) { return(pre_RN() * sd + mean); }
	//11
	public double RL(double mean, double sd) {  
		double x = Math.exp(pre_RN() * sd + mean); 
		return(x);
	}
	//12
	public int BI(int n, double p) {
		//parameters should be positive.
		if ((n < 1) || (p < 0.0) || (p > 1.0)) { 
			error_exit(13,"BI"); 
			return(0); 
		}
		
		int i;
		if ((n > 10) && ((p <= 0.1) || (p >= 0.9))) 
		{
			i = (p < 0.5) ? NP(p * (double)n) : (n - NP((1.0 - p) * (double)n));
		} 
		else if ((n > 10) && ((p * (double)n > 10.0) || ((1.0 - p) * (double)n > 10.0)))
		{ 
			i = (int) Math.floor(RN(p * (double)n, Math.sqrt(p * (1.0 - p) * (double)n)) + 0.5);
		} 
		else {  
			i = BI_OTHER(n, p);
		}
		if (i < 0) i = 0;
		if (i > n) i = n;
		 
		return(i);
	}
	
	private int BI_OTHER(int n, double p) {
		int i = 0; 
		for (int j = 0; j < n; j++)
		{ 
			if (RA() < p) i++;
		}
		return i; 
	}
	
	//13
	public double WE(double beta, double alpha) { 
		//parameters should be positive.
	    if ((alpha <= 0.0) || (beta <= 0.0)) { error_exit(13,"WE"); return(0.0); }
	    
	    double x = beta * Math.pow(-Math.log(RA()), (1.0 / alpha));
	    
	    return(x);
	}
	//14
	public double DP(double [] prob, int nval) { 
		double c = RA();
		int i = 0;
		while ((i < nval - 1) && (c > prob[i])) i++;
		return((double)(i));
	}
	//15
	public double CD(double []x, double []p, int n) {
		double c = RA(); 
		if (c >= p[n - 1]) return(x[n - 1]);
		if (c <= p[0]) return(x[0]);
		int i = 0;
		while ((i < n-2) && (c > p[i+1])) i++;
		return(x[i] + (x[i+1] - x[i]) * ((c - p[i])/(p[i+1] - p[i])));
	}
	
	public statistics initiate_statistics(String name, int if_time_p, int n_cells, double first_limit, double cell_width) {
	    statistics v = new statistics(); 
    	v.headline = name;
    	// 該被設計成布林型態
    	v.time_persistent = (char)if_time_p; 
    	// 是連續時間的時候，禁止作出表格
    	if (if_time_p != 0) n_cells = 0;
    	v.called_already = 0;
    	// 是連續時間的時候，使用現在的時間
    	v.ini_time = (if_time_p != 0) ? tnow : 0.0;
    	v.last_upd_sample_size = v.ini_time;
    	v.last_upd_value = 0.0;
    	v.sum = 0.0;
    	v.min = 0.0;
    	v.max = 0.0;
    	v.sum_of_squares = 0.0;
    	v.fst_cell = null; 
    	if (n_cells != 0) {
    		double a = first_limit;
    		cell c, c_old = null; 
    		for (int i = 0; i <= n_cells; i++) {
    			c = new cell(); 
    			c.upper_limit = a;
    			c.number = 0;
    			a += cell_width;
    			c.next = null;
    			if (i == 0) v.fst_cell = c; else c_old.next = c;
    			c_old = c;
    		}
    	}
    	else v.fst_cell = null;
    
	    return(v);
	}
	
	/**
	 * 初始化手術時間統計表
	 * 
	 * @param n = 1、2、3、4、5... 表示第 n 個統計表
	 * @param name 手術室名稱
	 * @param if_time_p = 1 為連續時間，而 0 為離散時間 (判斷是否為連續時間用)
	 * @param n_cells 儲存格數量
	 * @param fl 第一個分隔值 (起始值) 
	 * @param cw 每個儲存格的間距量 (範圍)
	 * @throws IOException 
	 */
	public void INISTA(int n, String name, int if_time_p, int n_cells, double fl, double cw) throws IOException { 
		//statistics index is too big or negative - consult INIQUE
		if ((n < 1) || (STATN < n)) { error_exit(8, strInista); return; }
		statistics v = stat_root[n - 1];
		//statistics has already been set - do not use INISTA twice
		if (v != null) { error_exit(9,strInista); return; } 
		v = initiate_statistics(name, if_time_p, n_cells, fl, cw);
		//there is no more space in memory - reduce number of entities 
		if (v == null)  { error_exit(2,strInista); return; }
		stat_root[n - 1] = v;
	}
	 
	public void clear_statistics(statistics v) { 
	  	if (v == null) return;
	  	v.called_already = 0;
	  	v.ini_time = (v.time_persistent != 0) ? tnow : 0.0;
	  	v.last_upd_sample_size = v.ini_time;
	  	v.last_upd_value = 0.0;
	  	v.sum = 0.0;
	  	v.min = 0.0;
	  	v.max = 0.0;
	  	v.sum_of_squares = 0.0;
	  	cell c = v.fst_cell;
	  	while (c != null) { c.number = 0; c = c.next; }
	}
	 											
	public int collect_statistics(statistics x, double observation) {
		if (x == null) return(1); 
		if (x.called_already == 0) {
			x.min = observation;
			x.max = observation;
			x.called_already = 1;
		}
		else {
			if (x.min > observation) x.min = observation;
			if (x.max < observation) x.max = observation;
			if (x.time_persistent != 0) {
				// 時間權重 * 數值(物品數量、代號、)
				x.sum += (tnow - x.last_upd_sample_size) * x.last_upd_value;
				// 時間權重 * 數值平方(物品數量、代號、)
				x.sum_of_squares += (tnow - x.last_upd_sample_size) * x.last_upd_value * x.last_upd_value;								
			}
		}
	  
		if (x.time_persistent != 0) { //continue
			x.last_upd_value = observation;
			x.last_upd_sample_size = tnow;
		} 
		else {
			(x.last_upd_sample_size)++; //Discrete
			x.sum            += observation;
			x.sum_of_squares += observation * observation;
		}
		cell c = x.fst_cell;
		while (c != null) {
			if ((observation < c.upper_limit) || (c.next == null)) { 
				(c.number)++; 
				break;
			}
			c = c.next;
		}
		return 0;
	}
	
	public void TALLY(int n, double observation) {
		//statistics index is too big or negative - consult INIQUE.
		if ((n < 1) || (STATN < n)) {
			error_exit(8,strInista); 
			return; 
		}
		//no statistic is defined for indicated index - use INISTA.
		if (collect_statistics(stat_root[n-1], observation) != 0) {
			error_exit(17,"TALLY"); 
			return; 
		}
	}
	
	public ArrayList<String> report_statistics(statistics x) {  
		ArrayList<String> result = new ArrayList<>();
		
		if (x == null) { 
			Main.log(Level.INFO, "No statistics are defined"); 
			result = null;
			return result; 
		} 
		
		if (x.time_persistent != 0) collect_statistics(x, x.last_upd_value); 
		
		String text = x.headline;
		result.add(text);
		
		if (x.last_upd_sample_size == 0) {
			text = " - 沒有任何的服務紀錄";
			result.add(text); 
			return result;
		}
		 
		double n = x.last_upd_sample_size - x.ini_time;
		if (n < 0.0001) return result;
		
		text = (x.time_persistent == 0) ? 
				String.format(" - 目前總計已服務人數為 %.0f 人%n%n", n):
				String.format("for %7.2f time duration%n%n", n);  
		result.add(text);
		 
	    double a = x.sum / n;
	    double s = x.sum_of_squares / n - a * a; 
	    if (s >= 0.0) s = Math.sqrt(s); 
	    else s = 0.0; 
	    text = String.format("  平均手術時間 =%10.2f 分鐘    手術時間標準差 =%10.2f%n%n", a, s);
	    result.add(text);
	    text = String.format("  最短手術時間 =%10.2f 分鐘    最長手術時間   =%10.2f 分鐘%n%n", x.min, x.max); 
	    result.add(text);
	    text = String.format("  總計手術時間 =%10.2f 分鐘    手術房利用率   =%10.2f %%%n%n", x.sum, x.sum / T() * 100); 
	    result.add(text);
	     
	    return printDataDistribution(x.fst_cell, result); 
	} 

	private ArrayList<String> printDataDistribution(cell fst_cell, ArrayList<String> result) { 
		cell c = fst_cell; 
	    if (c == null) {
	    	return result; 
	    }
	    
	    int maxc = 0;
	    while (c != null){ 
		    if (maxc < c.number) maxc = c.number;
		    c = c.next; 
	    }
	    maxc = (maxc + 49) / 50;
	    if (maxc == 0) return new ArrayList<>();
	    
	    c = fst_cell;
	    
	    int i = 1;
	    while (c != null){
		    if (c == fst_cell) { 
		    	result.add(String.format("時間區段#     分隔時間點        發生次數    (一顆 * 代表 %d 次)", maxc) + "\n"); 
		    } 
		    
		    StringBuilder str = new StringBuilder (
	    		(c.next != null) ? 
		    		String.format("%5d   %16.2f   %10d         ", i, c.upper_limit, c.number):
		    		String.format("%5d             inf.      %9d         ", i, c.number)
		    );  
	        for(int j = 0; j < c.number / maxc ; j++) {
	        	str.append("*");
	        }
	        str.append("\n");  
	        
	        result.add(str.toString());
	         
	        i++; 
	        c = c.next;
        }
		
		return result;  
	}
	 
	public void report_statistics(statistics x, BufferedWriter output) throws IOException{
		if (x == null) { 
			output.write("No statistics are defined%n"); 
			return; 
		} 
		
		if (x.time_persistent != 0) {
			collect_statistics(x, x.last_upd_value); 
		}
		 
		if (x.last_upd_sample_size == 0) {
			output.write(x.headline + " - no statistics have been recorded%n"); 
			return;
		}
		 
	    double n = x.last_upd_sample_size - x.ini_time; 
	    output.write(x.headline + " - summary statistics ");
	    output.write(
	    	String.format(x.time_persistent == 0 ? 
	    			"for sample size %4.0f%n" : "for %7.2f time duration%n", n)
	    );  
	     
	    if (n < 0.0001) return;
	    double a = x.sum / n;
	    double s = x.sum_of_squares / n - a * a; 
	    s = (s >= 0.0) ? Math.sqrt(s) : 0.0;
	     
	    output.write(String.format("  Average=%10.4f, standard deviation=%10.4f%n" ,a,s));
	    output.write(String.format("  minimum=%10.4f, maximum           =%10.4f%n" ,x.min ,x.max)); 
	    output.write(String.format("  sum    =%10.4f, Utilization       =%9.2f%%%n",x.sum, x.sum / T() * 100)); 
	    
	    printDataDistribution(x.fst_cell, output);
	} 
	
	private void printDataDistribution(cell fst_cell, BufferedWriter output) throws IOException {   
		cell c = fst_cell;
		
		if (c == null) {
	    	output.write("%n");
	    	return;
	    } 
		
	    int maxc = 0;
	    while (c != null){ 
		    if (maxc < c.number) maxc = c.number;
		    c = c.next; 
	    }
	    maxc = (maxc + 49) / 50;
	    if (maxc == 0) return;
	    
	    c = fst_cell;
	    int i = 1;
	    while (c != null) {
		    if (c == fst_cell) { 
		    	output.write(String.format("cell#   upperLimit   frequency   (one * = %d cases)%n", maxc));  
		    }
		     
	    	output.write((c.next != null) ? 
	    			String.format(" %3d    %10.2f   %9d   ", i,c.upper_limit,c.number) :
	    			String.format(" %3d        inf.     %9d   ", i , c.number)
	    	);
		      
	        for(int j = 0; j < c.number / maxc ; j++) {
	        	output.write("*");
	        }
	        
	        output.write("%n"); 
	        i++; 
	        c = c.next;
        }
	    
	    output.write("%n");  
	}
	
	
	public void CLEARS(int i) { 
		if (stat_root == null) return;
		if (i == 0) for (int j = 0; j < STATN; j++) clear_statistics(stat_root[j]);
		else {
			//statistics index is too big or negative - consult INIQUE.
			if ((i > STATN) || (i < 1)) { error_exit(8,"CLEAR"); return; }
			else clear_statistics(stat_root[i - 1]);
		}
	}
	
	//取得第 i 個統計數據的平均值
	public double SAVG(int i) { //平均值
		//no initialization has been performed - use INIQUE.
		if (stat_root == null) { error_exit(7, strSystem); return(0.0); }
		//statistics index is too big or negative - consult INIQUE.
		if ((i < 1) || (i > STATN)) { error_exit(8,"SAVG"); return(0.0); }
		statistics v = stat_root[i - 1];
		//no statistic is defined for indicated index - use INISTA.
		if (v == null) { error_exit(17,"SAVG"); return(0.0); }
		if (v.time_persistent != 0) collect_statistics(v, v.last_upd_value);
		/**3/1/94**/
		double n = v.last_upd_sample_size - v.ini_time; 
		if (n < 0.0001) return(0.0); else return (v.sum / n);
	}
	
	//取得第 i 個統計數據的標準差
	public double SSTD(int i) {
	  	//no initialization has been performed - use INIQUE.
	  	if (stat_root == null) { error_exit(7,strSystem); return(0.0); }
	  	//statistics index is too big or negative - consult INIQUE.
	  	if ((i < 1) || (i > STATN)) { error_exit(8,"SSTD"); return(0.0); }
	  	statistics v = stat_root[i - 1];
	  	//no statistic is defined for indicated index - use INISTA.
	  	if (v == null) { error_exit(17,"SSTD"); return(0.0); }
	  	if (v.time_persistent != 0) collect_statistics(v, v.last_upd_value);
	  	/**3/1/94**/
	  	double n = v.last_upd_sample_size - v.ini_time;
	  	if (n < 0.0001) return(0.0);
	  	double a = v.sum / n;
	  	double s = v.sum_of_squares / n - a * a;
	  	if (s >= 0.00001) return(Math.sqrt(s)) ;
	  	else return(0.0);
	}
	
	public double SSUM(int i) {
		//no initialization has been performed - use INIQUE.
		if (stat_root == null) { error_exit(7,strSystem); return(0.0); }
		//statistics index is too big or negative - consult INIQUE.
		if ((i < 1) || (i > STATN)) { error_exit(8,"SMAX"); return(0.0); }
		statistics v = stat_root[i - 1];
		//no statistic is defined for indicated index - use INISTA.
		if (v == null) { error_exit(17,"SMAX"); return(0.0); }
		return(v.sum);
	}
	
	//取得第 i 個統計數據的最大值
	public double SMAX(int i) {
		//no initialization has been performed - use INIQUE.
		if (stat_root == null) { error_exit(7,strSystem); return(0.0); }
		//statistics index is too big or negative - consult INIQUE.
		if ((i < 1) || (i > STATN)) { error_exit(8,"SMAX"); return(0.0); }
		statistics v = stat_root[i - 1];
		//no statistic is defined for indicated index - use INISTA.
		if (v == null) { error_exit(17,"SMAX"); return(0.0); }
		return(v.max);
	}
	
	//取得第 i 個統計數據的最小值 
	public double SMIN(int i) { 
		//no initialization has been performed - use INIQUE.
		if (stat_root == null) { error_exit(7,strSystem); return(0.0); }
		//statistics index is too big or negative - consult INIQUE.
		if ((i < 1) || (i > STATN)) { error_exit(8,"SMIN"); return(0.0); }
		statistics v = stat_root[i - 1];
		//no statistic is defined for indicated index - use INISTA.
		if (v == null) { error_exit(17,"SMIN"); return(0.0); }
		return(v.min);
	}

	public double SNUM(int i) {   
		//no initialization has been performed - use INIQUE.
		if (stat_root == null) { error_exit(7,strSystem); return(0.0); }
		//statistics index is too big or negative - consult INIQUE.
		if ((i < 1) || (i > STATN)) { error_exit(8,"SNUM"); return(0.0); }
		statistics v = stat_root[i - 1];
		//no statistic is defined for indicated index - use INISTA.
		if (v == null) { error_exit(17,"SNUM"); return(0.0); }
		return(v.last_upd_sample_size - v.ini_time);
	}
	 
	public void CLEARQ(int i) { 
		if (NQUEUE == 0) return;
		if (i == 0) 
			for (int j = 0; j < NQUEUE; j++) {
				clear_statistics(q[j].q_stat);
				collect_statistics(q[j].q_stat, (double)(q[j].number));
			}
		else {
			//queue number is too big or negative - consult INIQUE.
			if ((i < 0) || (i > NQUEUE)) { error_exit(4,"CLEARQ"); return; }
			clear_statistics(q[i-1].q_stat);
			collect_statistics(q[i-1].q_stat, (double)(q[i-1].number));
		}
	}

	public void RESTOR() { 
	  	CLEARS(0);
	  	CLEARQ(0);
	  	DISPOS();
	  	for (int j = 0; j < NQUEUE; j++)
	  		while(NQ(j) != 0) { REMVFQ(j, 1); DISPOS(); }
	  	while (NC() != 0) { REMVFC(1); DISPOS(); }
	  	tnow = 0.0;
	  	for (int i = 0; i < NATTR; i++) prev_entity[i] = 0.0;
	}
	 
	public double QAVG(int i) { 
		//queue number is too big or negative - consult INIQUE.
		if ((i > NQUEUE) || (i < 1)) { error_exit(4,"QAVG"); return(0.0); }
		statistics v = q[i - 1].q_stat;
		collect_statistics(v, (double)(q[i-1].number));
		double n = v.last_upd_sample_size - v.ini_time;
		if (n < 0.0001) return(0.0); else return(v.sum / n);
	}

	public double QSTD(int i) { 
		//queue number is too big or negative - consult INIQUE.
		if ((i > NQUEUE) || (i < 1)) { error_exit(4,"QSTD"); return(0.0); }
		statistics v = q[i - 1].q_stat;
		collect_statistics(v, (double)(q[i-1].number));
		double n = v.last_upd_sample_size - v.ini_time;
		if (n < 0.0001) return(0.0);
		double a = v.sum / n;  
		double s = v.sum_of_squares / n - a * a;
		if (s >= 0.00001) return(Math.sqrt(s)) ; 
		else return(0.0);
	}
	 
	public double QMAX(int i) { 
		//queue number is too big or negative - consult INIQUE.
		if ((i > NQUEUE) || (i < 1)) { error_exit(4,"QMAX"); return(0.0); }
		statistics v = q[i - 1].q_stat;
		return(v.max);
	}

	public double QMIN(int i) { 
		//queue number is too big or negative - consult INIQUE.
		if ((i > NQUEUE) || (i < 1)) { error_exit(4,"QMIN"); return(0.0); }
		statistics v = q[i - 1].q_stat;
		return(v.min);
	}
	 
	public double QNUM(int i) { 
		//queue number is too big or negative - consult INIQUE.
		if ((i > NQUEUE) || (i < 1)) { error_exit(4,"QNUM"); return(0.0); }
		statistics v = q[i - 1].q_stat;
		collect_statistics(v, (double)(q[i-1].number));
		return(v.last_upd_sample_size - v.ini_time);
	}

	public ArrayList<String> SUMRY(int n) {  
		ArrayList<String> result = null;  
		//no initialization has been performed.
		if (stat_root == null) { 
			error_exit(7,strSystem);  
			return result; 
		}
		//statistics index is too big or negative - consult INIQUE
		if ((n < 1) || (STATN < n)) { 
			error_exit(8,strInista); 
			return result; 
		} 
		
		return report_statistics(stat_root[n-1]); 
	}
	
	public void SUMRY(BufferedWriter output) throws IOException { 
		//no initialization has been performed.
	    if (stat_root == null) { error_exit(7,strSystem); return; }
	    
	    for (int i = 0; i < STATN; i++) {
	    	if (stat_root[i] != null) { 
	    		report_statistics(stat_root[i] , output);
	    	}
	    } 
	    output.flush(); 
	}
	
	public void SUMRY(String fn) throws IOException { 
		//no initialization has been performed.
	    if (stat_root == null) { 
	    	error_exit(7,strSystem); 
	    	return; 
	    } 
	    
	    if (fn.isEmpty()) { 
	    	Main.log(Level.WARNING, "未設定檔名!%n");
	    	return; 
	    }
	    
    	try (BufferedWriter output = new BufferedWriter(new FileWriter(fn)))
    	{
    		for(int i = 0; i < STATN; i++) {
    			if (stat_root[i] != null) { 
		    		report_statistics(stat_root[i] , output);
		    	}
    		}
    		output.flush();
    		output.close();
    	} 
	}
	
	public void SETDEB(int err) { if ((err > -1) && (err < 16)) ERR_CODE = err;}

	public void SETIDE(int id) { ID_CODE = id; }

	public void SETT(double tm) { if(tm > tnow) tnow = tm; }
 
	public void SETA(int i, double v) {
		//there is no current entity - use NEXTEV or REMVFx before.
		if (present_entity == null) { error_exit( 6,"SETA"); return; }
		//attribute number is too big or negative - consult INIQUE.
		if ((NATTR < i) || (i < 1)) { error_exit(15,"SETA"); return; }
		prev_entity[i - 1] = v;
	}
	 
	public void SETAP(double []a) {
		//there is no current entity - use NEXTEV or REMVFx before.
		if (present_entity == null) { error_exit( 6,"SETAP"); return; }
		//attribute should be a pointer.
		if (NATTR != -1)            { error_exit( 3,"SETAP"); return; }
		if (a != null) prev_entity = a;
	}
	
	public int EVC() { return(EV_CODE); }
	
	public int IDE() { return(ID_CODE); }
	
	public double T() { return(tnow); }
	
	public int NCEN() { return((present_entity == null) ? 0 : 1); }

	public double []AP() {
		//there is no current entity - use NEXTEV or REMVFx before.
		if (present_entity == null) { 
			error_exit(6, "AP"); 
			double [] temp = new double[1];
			temp[0] = zero_entity;
			return(temp);
		}
		//attribute should be a pointer.
		if (NATTR != -1) { 
			error_exit( 3,"AP"); 
			double []temp = new double[1];
			temp[0] = zero_entity;
			return(temp);
		}
		return(prev_entity);
	}
	
	/* attribute #i of current entity */
	public double A(int i) {
		//there is no current entity - use NEXTEV or REMVFx before.
		if (present_entity == null) { error_exit( 6,"A"); return(0.0); }
		//attribute number is too big or negative - consult INIQUE.
		if ((NATTR < i) || (i < 1)) { error_exit(15,"A"); return(0.0); }
		return prev_entity[i - 1];
	}
	
	/* dispose current entity 處理掉當前的實體 */
	public void DISPOS() { present_entity = null; }
	
	/**
	 * 取出第一個元素
	 * @param q 佇列來源
	 * @param t 取得優先權(時間、大小、權重...等等)
	 * @return 佇列第一個成員
	 */
	public queue_member remfq(queue q) { 
		if (q == null) return(null);
		queue_member v = q.fst_member; 
		q.fst_member = v.next_member;
		(q.number)--;
		collect_statistics(q.q_stat, (double)q.number);
		return(v);
	}
	 
	public queue_member find_by_rank(int nq, int ir) {  
		if ((nq > NQUEUE) || (nq < 1)) {
			return(null);
		} 
	  	if (q[nq - 1].number < ir) {
	  		return(null);
	  	}
	  	
	  	queue_member m = q[nq - 1].fst_member;
	  	int i = 1;
	  	while ((m != null) && (i < ir)) { i++; m = m.next_member; }
	  	return(m);
	}

	public void SETQDC(int n, String disc) {
		//queue number is too big or negative - consult INIQUE.
		if ((n > NQUEUE) || (n < 1)) { error_exit(4,"SETQDC"); return; }
		
		switch(disc) {
			case "LIFO": case "FIFO": case "BVF": case "SVF": 
				q[n-1].discipline = disc;
				Main.log(Level.INFO, String.format("已將 Queue #%d 已設置為 %s", n, disc)); 
				break;
			default: 
				Main.log(Level.WARNING, String.format("無法將 Queue #%d 已設置為 %s", n, disc));
		}
	}

	public int QDC(int i) {
		//queue number is too big or negative - consult INIQUE.
		if ((i > NQUEUE) || (i < 1)) { error_exit(4,"QDC"); return('F'); }
		return((int)q[i - 1].discipline.charAt(0));
	}
	 
	/**
	 * 尋找佇列成員並取出
	 * @param q
	 * @param v
	 * @return
	 */
	public int remfmid(queue q, queue_member v) { //remove queue_member v
		if (q == null) return(1);  
		if (q.fst_member == null) return(2); 
		if (v == null) return(3);
		
		if (v == q.fst_member) {
			q.fst_member = q.fst_member.next_member;
			(q.number)--;
			collect_statistics(q.q_stat, (double)q.number);
		}
		else {
			queue_member v2 = q.fst_member;
			queue_member v1 = v2.next_member;
			while (v1 != null) {
				if(v1 == v) {
					v2.next_member = v1.next_member;
					(q.number)--;
					collect_statistics(q.q_stat, (double)q.number);
					break;
				}
				v2 = v1;
				v1 = v1.next_member; 
			}
		}
		
		return(0);
	}
	  
	public double AIQ(int nq, int ir, int i) { 
		//attribute number is too big or negative - consult INIQUE.
		if ((NATTR < i) || (i < 1)) { error_exit(15,"AIQ"); return(0.0); }
		queue_member m = find_by_rank(nq, ir);
		//no entity with specified rank and/or queue number.
		if (m == null) { error_exit(5,"AIQ"); return(0.0); }
		return(m.entity[i - 1]);
	}

	double []APIQ(int nq, int ir) { 
		//attribute should be a pointer.
		if (NATTR != -1) { error_exit(3,"APIQ");
			double [] temp = new double[1];
			temp[0] = zero_entity; 
			return(temp);
		}
		queue_member m = find_by_rank(nq, ir);
		//no entity with specified rank and/or queue number.
		if (m == null) { 
			error_exit(5,"APIQ"); 
			double [] temp = new double[1];
			temp[0] = zero_entity; 
			return(temp);
		}
		return(m.entity);
	}

	double PRIQ(int nq, int ir) { 
		//queue number is too big or negative - consult INIQUE.
		if ((nq < 1) || (nq > NQUEUE)) { error_exit(4,"PRIQ"); return(0.0); }
		queue_member m = find_by_rank(nq, ir);
		//no entity with specified rank and/or queue number.
		if (m == null) { error_exit(5,"PRIQ"); return(0.0); }
		return(m.priority);
	}

	public int IDIQ(int nq, int ir) { 
		//queue number is too big or negative - consult INIQUE.
		if ((nq < 1) || (nq > NQUEUE)) { error_exit(4,"IDIQ"); return(0); }
		queue_member m = find_by_rank(nq, ir);
		//no entity with specified rank and/or queue number.
		if (m == null) { error_exit(5,"IDIQ"); return(0); }
		return(m.id);
	}
	 
	public double AIC(int ir, int i) { 
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return(0.0); }
		//attribute number is too big or negative - consult INIQUE.
		if ((i > NATTR) || (i < 1)) { error_exit(15,"AIC"); return(0.0); }
		queue_member m = calendar.fst_member;
		int j = 1;
		while ((m != null) && (j < ir)) { j++; m = m.next_member; }
		//no entity with specified rank and/or queue number. 
		if (m == null) { error_exit(5,"AIC"); return(0.0); }
		//too large rank for queue or calendar.
		if (j != ir) { error_exit(12,"AIC"); return(0.0); }
		if (m.entity == null) return(0.0);
		else return(m.entity[i - 1]);
	}

	public double []APIC(int ir) { 
		double [] temp = new double[1];
		temp[0] = zero_entity; 
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return(temp); }
		//attribute should be a pointer.
		if (NATTR != -1) { error_exit(3,"APIC"); return(temp); }
		queue_member m = calendar.fst_member;
		int j = 1;
		while ((m != null) && (j < ir)) { j++; m = m.next_member; }
		//no entity with specified rank and/or queue number. 
		if (m == null) { error_exit(5,"APIC"); return(temp); }
		//too large rank for queue or calendar.
		if (j != ir) { error_exit(12,"APIC");  return(temp); }
		if (m.entity == null) return(temp);
		else return(m.entity);
	}

	public int IDIC(int ir) { 
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return(0); }
		int i = 1; 
		queue_member m = calendar.fst_member;
		while ((m != null) && (i < ir)) { i++; m = m.next_member; }
		
		if ((m != null) && (i == ir)) {
			return(m.id); 
		}
		else { //too large rank for queue or calendar.
			error_exit(12,"IDIC"); 
			return(0); 
		}
	}
	 
	public double TIC(int ir) { 
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return(0.0); }
		int i = 1;
		queue_member m = calendar.fst_member;
		while ((m != null) && (i < ir)) { i++; m = m.next_member; }
		
		if ((m != null) && (i == ir)) { 
			return(m.priority); 
		}  
		else { //too large rank for queue or calendar.
			error_exit(12,"TIC"); 
			return(0.0); 
		}
	}

	public int NEIC(int ir) { 
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return(0); }
		int i = 1;
		
		queue_member m = calendar.fst_member;
		while ((m != null) && (i < ir)) { 
			i++; 
			m = m.next_member; 
		}
		
		if ((m != null) && (i == ir)) { 
			return(m.code);
		}
		else { //too large rank for queue or calendar.
			error_exit(12,"NEIC"); 
			return(0); 
		}
	}
	   
	public int NQ(int nq) {
		//queue number is too big or negative - consult INIQUE.
		if ((nq > NQUEUE) || (nq < 1)) { error_exit(4,"NQ"); return(0); }
		return(q[nq - 1].number);
	}

	public int NC() {
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return(0); }
		return(calendar.number);
	}
	 
	public void list_calendar() { 
		if (calendar == null) { 
			Main.log(Level.WARNING, "Calendar is not defined.%n"); 
			return; 
		}
		
		String msg = String.format("Time: %6.2f. In the calendar %d entities.%n", tnow, calendar.number);
		queue_member p = calendar.fst_member;
		int n = 1;  
		while(p != null) {
			msg += String.format("#%4d. time:%6.2f  code:%3d  id:%3d%n",n, p.priority, p.code, p.id);
			n++;
			p = p.next_member;
		}
		Main.log(Level.INFO, msg);
		 
		Main.log(Level.INFO, "Press Q to abort, <RETURN> to continue%n");
		Scanner in = new Scanner(System.in); 
		if (in.hasNext()) {
			String qu = in.next();  
			if (qu.equals("Q") || qu.equals("q")) exit(1); 
		} 
		in.close();
	}

	public void REMVFC(int ir) {  
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return; }
		//there is a current entity already in the system - use DISPOS.
		if (present_entity != null) { error_exit(1, strRemvfc); return; }
		queue_member m = calendar.fst_member;
		int j = 1;
		while ((m != null) && (j < ir)) { j++; m = m.next_member; } 
		//no entity with specified rank and/or queue number.
		if (m == null) { error_exit(5,strRemvfc); return; }
		if ((ERR_CODE & 8) == 8) {
			Main.log(Level.WARNING, String.format("Just before removing %d. entity from calendar -%n", ir));
			list_calendar();
		}
		int i = 0;
		 
		if (ir == 1) m = remfq(calendar);
		else i = remfmid(calendar, m);
		
		//no entity with specified rank and/or queue number.
		if (i != 0) { error_exit(5,strRemvfc); return; }
		
		int NEXT_CODE = m.code;
		ID_CODE = m.id;
		if (NEXT_CODE > 1) {
			REMVFC_OTHER_CODE(m);
		}
		else {
			REMVFC_FIRST_CODE();
		} 
	}
	
	private void REMVFC_OTHER_CODE(queue_member m) {
		present_entity = m.entity;
		for (int i = 0; i < NATTR; i++) {
			prev_entity[i] = present_entity[i];
		}
		if (NATTR == -1) {
			prev_entity = present_entity;
		} 
	}
	
	private void REMVFC_FIRST_CODE() {
		if (NATTR > 0) {
			present_entity = new double[NATTR];
			for (int i = 0; i < NATTR; i++) {
				present_entity[i] = prev_entity[i];
			}
		}
		else if ((NATTR == -1) && (prev_entity != null)) {
			present_entity = prev_entity;
		}
		else {
			double [] temp = new double[1];
			temp[0] = zero_entity; 
			present_entity = temp;
		}  
	}
	 
	public void list_queue(queue q) { 
		if (q == null) { 
			Main.log(Level.WARNING, "Queue is not defined%n");
		}
		else {
			list_queue_is_not_null(q);
		}
		 
		Main.log(Level.INFO, "Press Q to abort, <RETURN> to continue%n"); 
		Scanner in = new Scanner(System.in); 
		if (in.hasNext()) {  
			String qu = in.next(); 
			if (qu.equals("Q") || qu.equals("q")) exit(1);
		} 
		in.close();
	}
	
	private void list_queue_is_not_null(queue q) { 
		String msg = String.format("Time: %6.2f. In the queue %d entities. Discipline: %s%n", tnow, q.number, q.discipline);
	 
		queue_member p = q.fst_member;
		int n = 1;
		while (p != null) {
			msg += String.format("#%4d. id:%3d  ", n, p.id); 
			
			if (q.discipline.equals("SVF") || q.discipline.equals("BVF")) 
			{
				msg += String.format("disc.:%6.2f", p.priority); 
			} 
			for (int i = 0; (i < 3)&&(i < NATTR); i++) 
			{
				msg += String.format("  A(%d)=%6.2f", i+1, p.entity[i]);
			} 
			
			msg += "%n";
			n++;  
			p = p.next_member;
		} 
		 
		Main.log(Level.INFO, msg);
	}
	  
	public void REMVFQ(int nq, int ir) { 
		//there is a current entity already in the system - use DISPOS.
		if (present_entity != null) { error_exit(1, strRemvfq); return; }
		queue_member m = find_by_rank(nq, ir);
		//no entity with specified rank and/or queue number.
		if (m == null) { error_exit(5,strRemvfq); return; }
		if ((ERR_CODE & 8) == 8) { 
			Main.log(Level.WARNING, String.format("Just before removing %d. entity from queue #%d -%n", ir, nq));
		    list_queue(q[nq-1]);
		}
		int i = 0;
		 
		if (ir == 1) m = remfq(q[nq-1]);
		else i = remfmid(q[nq-1], m);
		
		//no entity with specified rank and/or queue number.
		if ((m == null) || (i != 0)) { error_exit(5, strRemvfq); return; }
		
		ID_CODE = m.id;
		present_entity = m.entity;
		
		for (i = 0; i < NATTR; i++) prev_entity[i] = present_entity[i];
		if (NATTR == -1) prev_entity = present_entity;
	}

	public void place_v_after_v1(queue_member v, queue_member v1, queue q) {
		//place_v_after_v1(v, null, q)
		if (v1 == null) {
			v.next_member = q.fst_member; //將 v 安排到佇列的最前面
			q.fst_member = v;
		}
		else if (v1.next_member == null) v1.next_member = v;
	  	else {
	  		v.next_member = v1.next_member; //v 欲新增的物件
	  		v1.next_member = v;
	  	}
	}
	 
	@SuppressWarnings("unused")
	public int enter_to_queue(queue q, double []new_member, double value, int code, int id) {
		//enter_to_queue(calendar,present_entity,time,0,0) 
		if (q == null) return(1);
		queue_member v = new queue_member();	//創建新的物體(包含屬性、優先權、物體編號、事件代碼、下一個物體)
		
		if (v == null) return(2);
		v.entity = new_member;		//屬性(項目)
		v.priority = value;			//優先權(時間)
		v.id = id;					//物體識別碼
		v.code = code;				//行為代號
		v.next_member = null;		//鏈結串列
		
		if (q.fst_member == null) q.fst_member = v; 
		else {
			queue_member v1 = q.fst_member;
			
			switch (q.discipline) {  
				case "LIFO": 
					q.fst_member  = v;	//新的佇列開頭
					v.next_member = v1;	//接上原本佇列開頭
					break;
				
				case "BVF":
					enter_to_queue_BVF(q, v, v1, value);
	                break;
	               
	      		case "SVF": 
	      			enter_to_queue_SVF(q, v, v1, value);
	      			break;
	      			
	      		default: /* FIFO */
	      			while(v1.next_member != null) { 
	      				v1 = v1.next_member;
	      			}
	      			v1.next_member = v;
			}
		}
		(q.number)++;
		
		collect_statistics(q.q_stat, (double)q.number);
		
		return(0);
	}
	
	private void enter_to_queue_BVF(queue q, queue_member v, queue_member v1, double value) { 
		//calendar.fst_member < v.priority
		
		//如果'優先權值'較大直接排在最前面
		if (v1.priority < value) {
			place_v_after_v1(v, null, q);
		}
		else {
			queue_member oldv1 = v1;
			while ((v1.priority >= value) && (v1.next_member != null)) {
				//安排到'優先權值'比自己小的人前面
				oldv1 = v1;
				v1 = v1.next_member;
			}
			
			//屬於邊界問題，如果沒有下一個則檢查 當前物體的優先權值 是否大於等於 欲新增物體的優權值，如果沒有就移動到前一個。
			if (v1.next_member == null) {   
				if (v1.priority < value) v1 = oldv1; 
			}
			else v1 = oldv1;
			
			place_v_after_v1(v, v1, q);
       } 
	}
	
	private void enter_to_queue_SVF(queue q, queue_member v, queue_member v1, double value) {  
		//calendar.fst_member >= v.priority
		if (v1.priority >= value) place_v_after_v1(v, null, q); 
        else {
    	   queue_member oldv1 = v1;
    	   while ((v1.priority < value) && (v1.next_member != null)) {
    		   oldv1 = v1; 
    		   v1 = v1.next_member;
    	   }
    	   if (v1.next_member == null) {
    		   if (v1.priority >= value) v1 = oldv1;
    	   } 
    	   else v1 = oldv1;
    	   place_v_after_v1(v, v1, q);
        }  
	}
	
	public void QUEUE(int nq, double PRIORITY) { 
		//there is no current entity - use NEXTEV or REMVFx before.
		if (present_entity == null) { error_exit(6, strQueue); return; }
		//queue number is too big or negative - consult INIQUE.
		if ((nq < 1) || (nq > NQUEUE)) { error_exit(4,strQueue); return; } 
		for (int i = 0; i < NATTR; i++) present_entity[i] = prev_entity[i];
		if (NATTR == -1) present_entity = prev_entity;
		//there is no more space in memory - reduce number of entities.
		if (enter_to_queue(q[nq-1], present_entity, PRIORITY, 0, ID_CODE) != 0) 
			{ error_exit(2,strQueue); return; }
		present_entity = null;
	}
	
	public void SCHED(double time, int ne, int id) {
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return; }
		//there is no current entity - use NEXTEV or REMVFx before.
		if (present_entity == null) { error_exit(6, strSched); return; }
		//event code should be greater than 1.
		if (ne <= 1) { error_exit(16,strSched); return; }
		
		for (int i = 0; i < NATTR; i++) present_entity[i] = prev_entity[i];
		
		if (NATTR == -1) present_entity = prev_entity;
		
		if (time < 0.0) time = 0.0;
		//there is no more space in memory - reduce number of entities.
		if (enter_to_queue(calendar, present_entity, tnow+time, ne, id) != 0)
	    	{ error_exit(2, strSched); return; }
		present_entity = null;
	}

	public void CREATE(double time, int id) {
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return; }
		if (time < 0.0) time = 0.0;
		//there is no more space in memory - reduce number of entities.
		if (enter_to_queue(calendar, present_entity, tnow+time, 1, id) != 0)
	    	{ error_exit(2,"CREATE"); return; }
	}
	
	public void SIMEND(double time) {
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return; }
		if (time < tnow) time = tnow;
		//there is no more space in memory - reduce number of entities. 
		if (enter_to_queue(calendar,present_entity,time,0,0) != 0) { 
			error_exit(2,"SIMEND"); return;
		} 
	}
	
	public queue initialize_queue(String dis, String stat_n) {
	 	queue newQ = new queue(); 
	 	newQ.discipline = dis; 
	 	newQ.number = 0;
	 	newQ.q_stat = (!stat_n.isEmpty()) ? initiate_statistics(stat_n, 1, 0, 0.0, 1.0) : null;
	 	newQ.fst_member = null;
 		collect_statistics(newQ.q_stat, (double)newQ.number);
	 	 
	 	return(newQ);
	}
	 
	@SuppressWarnings("unused")
	public queue[] set_up_queues(int n) {  
		calendar = initialize_queue("SVF", "");
		
		queue [] qu;
		if (n <= 0) {
			qu = new queue[1];
			qu[0] = calendar;
			return qu;
		}
		else {
			qu = new queue[n];
			for(int i = 0; i < n; i++) { 
		  		String head = String.format("Queue No %4d", i + 1);
		  		qu[i] = initialize_queue("FIFO", head);  
		  	} 
		} 
		
	  	return(qu);
	}
		
	public void INIQUE(int max_q, int max_attr, int max_stat) { 
		//basic parameters have already been set - do not INIQUE twice.
		if (calendar != null) { error_exit(11, strInique); return; }
		//parameters should be positive.
		if ((max_q < 0) || (max_attr < -1) || (max_stat < 0)) { error_exit(13,strInique); return; }
		
		NQUEUE = max_q;
		q = set_up_queues(NQUEUE);
		//there is no more space in memory - reduce number of entities.
		if (q == null) { error_exit(2,strInique); return; }
		
		NATTR = max_attr;
		if (NATTR > 0) {
			prev_entity = new double[NATTR];
			Arrays.fill(prev_entity, 0.0);
		}
		
		STATN = max_stat; 
		if (STATN < 1) return;
		stat_root = new statistics[STATN];
		for (int i = 0; i < STATN; i++) stat_root[i] = null;
	}

	public void SHOWQ(int m) {
		if (m == 0) { list_calendar(); return; }
		//queue number is too big or negative - consult INIQUE.
		if ((m < 0) || (m > NQUEUE)) { error_exit(4,"SHOWQ"); return; }
		list_queue(q[m - 1]);
	}
		 
	public int NEXTEV() {   
		if (ERR_RET != 0) return(0);
		//no initialization has been performed - use INIQUE.
		if (calendar == null) { error_exit(7,strSystem); return(0); }
		//there is a current entity already in the system - use DISPOS.
		if (present_entity != null) { error_exit(1,"NEXTEV"); return(0); } 
		//
		if ((ERR_CODE & 4) == 4) { 
			Main.log(Level.WARNING, "Just before NEXTEV: "); 
			list_calendar();
		} 
		
		if (calendar.number == 0) { return(0); }
		  
		queue_member p = remfq(calendar); 
		//no entity with specified rank and/or queue number.
		if (p == null) { error_exit(5,"NEXTEV"); return(0); }
		
		if (p.priority > tnow) tnow = p.priority;
		EV_CODE = p.code;
		ID_CODE = p.id;
		if (EV_CODE > 1) {
			handleOtherEvent(p);
		}
		else if (EV_CODE == 1) 
		{
			handleFirstEvent();
		}
		 
		return(EV_CODE);
	}

	private void handleFirstEvent() {
		if (NATTR > 0) {
			present_entity = new double[NATTR];
			for (int i = 0; i < NATTR; i++) {
				present_entity[i] = prev_entity[i];
			}
		}
		else if ((NATTR == -1) && (prev_entity != null)) 
		{ 
			present_entity = prev_entity;
		}
		else {
			double [] temp = new double[1];
			temp[0] = zero_entity;
			present_entity = temp;
		} 
	}
	
	private void handleOtherEvent(queue_member p) { 
		present_entity = p.entity;
		
		if(present_entity == null) 
		{ 
			Main.log(Level.INFO, "present_entity == null");
		}
		else for (int i = 0; i < NATTR; i++) 
		{
			prev_entity[i] = present_entity[i];
		}
		
		if (NATTR == -1) prev_entity = present_entity; 
	}
	
}
