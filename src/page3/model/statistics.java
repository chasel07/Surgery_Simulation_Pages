package page3.model;


//數據統計
public class statistics {
	
	String headline;		  	 //標題
    char   time_persistent; 	 //持續時間
    char   called_already;  	 //是否已呼叫
    double ini_time;			 //初始時間
    double last_upd_sample_size; //最後更新樣本大小的時間
    double last_upd_value;		 //最後更新值的次數
    double min;					 //最小值
    double max;					 //最大值
    double sum;					 //總時間
    double sum_of_squares;		 //平方和，用來計算標準差
    cell   fst_cell;			 //第一個儲存格
    
	public statistics() {
		// Do nothing.
	} 
}
