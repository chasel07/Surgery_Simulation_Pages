package page6.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import page6.controller.Main;
import page6.model.ColorManager;
import page6.model.FileRoute;
import page6.model.Patient;
 

public class Detail extends JFrame{ 
	private static final long serialVersionUID = 1L;
	
	private final int subWidth  = 800; // 取得螢幕的寬度
	private final int subHeight = 300;// 取得螢幕的高度
   
	private String timeFormat = "%02d:%02d:%02d";
	private JLabel[] Tittlelabel = new JLabel[20]; 
	private JLabel[] Valuelabel = new JLabel[20]; 
	  
	private String[] TittlelableName = {"申請序號:","病患ID:","病患姓名:","病患身分:","所屬科別:","主治醫師:","手術房別:","手術級別:","手術名稱:","麻醉名稱:"
			,"到達時間:","入室時間:","麻醉開始:","麻醉完成:","劃刀時間:","縫合時間:","手術時長:","麻醉結束:","轉送完成:","清潔完成:"};
	private String[] ValuelableName = {"*****","****","****","****","****","****","****","****","**************************","**************************"
			,"****","****","****","****","****","****","****","****","****","****"};
	public Detail(Patient p)  {
		super("詳細資料");
		setLayout(new BorderLayout());
		JPanel myp = new JPanel();
		myp.setLayout(new GridBagLayout());
		ValuelableName[0] = p.getApplicationID();
		ValuelableName[1] = p.getPatientID();
		ValuelableName[2] = p.getPatientName();
		ValuelableName[3] = p.getIdentity();
		ValuelableName[4] = p.getSection();
		ValuelableName[5] = p.getDoctor();
		ValuelableName[6] = p.getRoom();
		ValuelableName[7] = p.getType();
		ValuelableName[8] = p.getSurgeryName();
		ValuelableName[9] = p.getAnesthesiaName();

		String tmpStr = String.format(timeFormat,p.getArrivalTime()/1440,p.getArrivalTime()%1440/60,p.getArrivalTime()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[10] = tmpStr;
		tmpStr = String.format(timeFormat, p.getEntryTime() / 1440,p.getEntryTime() %1440/60, p.getEntryTime() % 60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[11] = tmpStr;
		tmpStr = String.format(timeFormat,p.getAnesthesiaStart()/1440,p.getAnesthesiaStart()%1440/60,p.getAnesthesiaStart()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[12] = tmpStr;
		tmpStr = String.format(timeFormat,p.getAnesthesiaFinish()/1440,p.getAnesthesiaFinish()%1440/60,p.getAnesthesiaFinish()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[13] = tmpStr;
		tmpStr = String.format(timeFormat,p.getScratchTime()/1440,p.getScratchTime()%1440/60,p.getScratchTime()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[14] = tmpStr;
		tmpStr = String.format(timeFormat,p.getSutureTime()/1440,p.getSutureTime()%1440/60,p.getSutureTime()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[15] = tmpStr;
		tmpStr = String.format(timeFormat,p.getSurgeryTime()/1440,p.getSurgeryTime()%1440/60,p.getSurgeryTime()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[16] = tmpStr;
		tmpStr = String.format(timeFormat,p.getAnesthesiaEnd()/1440,p.getAnesthesiaEnd()%1440/60,p.getAnesthesiaEnd()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[17] = tmpStr;
		tmpStr = String.format(timeFormat,p.getAnesthesiaEnd()/1440,p.getAnesthesiaEnd()%1440/60,p.getTransferTime()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[18] = tmpStr;
		tmpStr = String.format(timeFormat,p.getCleanTime()/1440,p.getCleanTime()%1440/60,p.getCleanTime()%60);
		if(tmpStr.contains("-1")) tmpStr="";
		ValuelableName[19] = tmpStr;
		
		
		GridBagConstraints c = new GridBagConstraints();
		for(int i=0;i<TittlelableName.length;i++) {
			Tittlelabel[i] = new JLabel(TittlelableName[i]);
		}
		for(int i=0;i<ValuelableName.length;i++) {
			Valuelabel[i] = new JLabel(ValuelableName[i]);
		}
		//申請序號
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[0], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[0], c);
		
		//病患ID
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[1], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 3;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[1], c);
		
		//病患姓名
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 4;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[2], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 5;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[2], c);

		//病患身分
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 6;
		c.gridy = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[3], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 7;
		c.gridy = 0;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[3], c);
		
		//科別
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[4], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[4], c);
		
		//主治醫師
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[5], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 3;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[5], c);
		
		//室別
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 4;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[6], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 5;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[6], c);
		
		//類型
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 6;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[7], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 7;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[7], c);

		//手術名稱
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[8], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[8], c);
		
		//麻醉名稱
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 4;
		c.gridy = 2;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[9], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.gridx = 5;
		c.gridy = 2;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[9], c);
		
		//到達時間
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[10], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[10], c);
		
		//入室時間
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[11], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 3;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[11], c);
		
		//麻醉開始
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 4;
		c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[12], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 5;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[12], c);
		
		//麻醉完成
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 6;
		c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[13], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 7;
		c.gridy = 3;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[13], c);
		
		//劃刀時間
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[14], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 4;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[14], c);
		
		//縫合切口
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 4;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[15], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 3;
		c.gridy = 4;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[15], c);
		
		//手術時長
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 4;
		c.gridy = 4;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[16], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 5;
		c.gridy = 4;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[16], c);
		
		//麻醉結束
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 6;
		c.gridy = 4;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[17], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 7;
		c.gridy = 4;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[17], c);
		
		//轉送時間
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 5;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[18], c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 5;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[18], c);
		
		//清潔時間
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 2;
		c.gridy = 5;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Tittlelabel[19], c);
				
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 3;
		c.gridy = 5;
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(10, 20, 10, 20);
		myp.add(Valuelabel[19], c);
		myp.setBackground(ColorManager.COLOR_WHITE);
		add(myp,BorderLayout.CENTER);
		 
		int h = Toolkit.getDefaultToolkit().getScreenSize().height - subHeight;//取得螢幕的高度
		int w = Toolkit.getDefaultToolkit().getScreenSize().width - subWidth;  //取得螢幕的寬度 
		
		setSize(subWidth, subHeight);  
		setLocation(w/2, h/2);
		setVisible(true);
		try {
			setIconImage(ImageIO.read(new File(FileRoute.IMAGE_LOGO)));
		} catch (IOException e) { 
			Main.log(Level.WARNING, e.getMessage());
		}
		setResizable(false); 
	} 
}
