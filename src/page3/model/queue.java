package page3.model;

public class queue {
	
	String discipline;  /* LIFO,FIFO,BVF,SVF */
    int number;
    statistics q_stat;
    queue_member fst_member;	
    
	public queue() {
		discipline = "";
		number = 0;
		q_stat = null;
		fst_member = null;
	}

}
