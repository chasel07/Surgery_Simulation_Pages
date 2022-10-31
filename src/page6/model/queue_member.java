package page6.model;

public class queue_member { 
    double       priority;		//優先權
    int          code;			//代碼
    int          id;			//識別碼
    double  []	 entity;		//項目
    queue_member next_member;	//下一個queue_member 
    
	public queue_member() {
		// TODO Auto-generated constructor stub
	} 

}
