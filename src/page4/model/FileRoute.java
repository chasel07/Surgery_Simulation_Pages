package page4.model;

import java.io.File;

public class FileRoute {
	
	private FileRoute() {
		//Do nothing.
	}
	
	public static final String PATH_ROOT = (new File("")).getAbsolutePath();
	public static final String PATH_RAW = "src/page4/raw";
	public static final String PATH_DATA = "src/page4/data";
	public static final String PATH_DISTRIBUTION = PATH_DATA + "/distribution";
	public static final String PATH_SCRIPT = PATH_DATA + "/script";
	public static final String PATH_CALENDAR = PATH_DATA + "/calender";
	public static final String PATH_STDD = PATH_DATA + "/Standard_Deviation";
	public static final String PATH_TEMP = PATH_DATA + "/distribution/temp.csv";
	public static final String PATH_SURGERY_TEMP = PATH_DATA + "/各術式手術時間(暫存).csv";
	
	public static final String IMAGE_ROOT  = "src/page4/image/";
	public static final String IMAGE_DELETE1  = IMAGE_ROOT + "delet1.png";
	public static final String IMAGE_PREVIEW1 = IMAGE_ROOT + "preview.png";
	public static final String IMAGE_TAP1 = IMAGE_ROOT + "tap.png"; 
	public static final String IMAGE_FAILURE = IMAGE_ROOT + "p4_fail24.png";
	public static final String IMAGE_FINISH = IMAGE_ROOT + "p4_complete24_2.png";
	public static final String IMAGE_DISK = IMAGE_ROOT + "p2_disk100.png";
	public static final String IMAGE_HOME = IMAGE_ROOT + "p1_home25.png";
	public static final String IMAGE_LOGO = IMAGE_ROOT + "p1_logo16.png";
	public static final String IMAGE_LOADING = IMAGE_ROOT + "aloading.gif";
	public static final String IMAGE_LOADING_24 = IMAGE_ROOT + "aloading24.gif";
	public static final String IMAGE_EMPTY = IMAGE_ROOT + "empty.PNG";
	public static final String IMAGE_WORKFLOW = IMAGE_ROOT + "p1_index.png";
	public static final String IMAGE_DELETE = IMAGE_ROOT + "p4_delete30.png";
	public static final String IMAGE_PREVIEW = IMAGE_ROOT + "p4_preview30.png";
	public static final String IMAGE_TAP = IMAGE_ROOT + "p4_tap30.png";

}
