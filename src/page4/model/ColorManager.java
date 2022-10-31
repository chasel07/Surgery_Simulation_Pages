package page4.model;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ColorManager implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Color COLOR_TITLE = new Color(46, 143, 250); // 框線標題列
	public static final Color COLOR_TABLE = new Color(241, 255, 231); // 選取列前景顏色
	public static final Color COLOR_TABLEHAND_BACKGOUND = new Color(91, 155, 213);
	public static final Color COLOR_TABLEHAND_FOREGOUND = new Color(255, 255, 255);
	public static final Color COLOR_TABLE_GRID = new Color(132, 180, 224);
	public static final Color COLOR_TABLE_FOREGOUND = new Color(0, 0, 0);
	public static final Color COLOR_TABLE_BACKGOUND = new Color(221, 235, 247);
	public static final Color COLOR_TABLE_SELECT_FOREGROUND = new Color(66, 221, 252);
	public static final Color COLOR_TABLE_NSELECT_FOREGROUND = new Color(117, 113, 113);
	public static final Color COLOR_PREVIEWPANEL_BACKGOUND = new Color(213, 230, 248);
	public static final Color COLOR_MENUBAR_BACKGOUND = new Color(41, 105, 166);
	public static final Color COLOR_MENUBAR_BTNHOME = new Color(146, 205, 220);
	public static final Color COLOR_MENUBAR_BTNHOME_TEXT = new Color(23, 53, 93);
	public static final Color COLOR_MENUBAR_TEXT_DARK = new Color(150, 175, 207);
	public static final Color COLOR_MENUBAR_TEXT_LIGHT = new Color(255, 255, 255);
	public static final Color COLOR_WHITE = new Color(255, 255, 255);
	public static final Color COLOR_BLACK = new Color(0, 0, 0);
	public static final Color COLOR_STATUS_BACKGOUND = new Color(192, 207, 226);
	public static final Color COLOR_RESULT_TITLE_BACKGOUND = new Color(90, 166, 234);

	private File source;

	// Background, Foreground
	ArrayList<Color[]> ColorList = new ArrayList<>();

	// 自訂欲顯示的字串
	HashMap<String, Color[]> ColorMap = new HashMap<>();

	/**
	 * @param Source_CSV 檔案來源
	 * @throws IOException
	 */
	public ColorManager(File Source_CSV) throws IOException {
		source = Source_CSV;

		CSVReader in = new CSVReader(source);
		String[] line = in.readNext(); // 標題列

		while ((line = in.readNext()) != null) {

			Color[] BackAndFore = new Color[2];

			for (int i = 0; i < BackAndFore.length; i++) {
				BackAndFore[i] = Color.decode("0x" + line[i]);
			}
			ColorList.add(BackAndFore);

			for (int i = 2; i < line.length; i++) {
				if (line[i].isEmpty())
					continue;
				ColorMap.put(line[i], BackAndFore);
			}

		}

		in.close();
	}

	public Color[] getColor(String Name) {
		return ColorMap.get(Name);
	}

}
