package Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	public final static String filepath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory() + "\\..\\Documents\\ACBA\\log\\errorLogs.txt";

	public static void logError(String err) {
		File f = new File(filepath);
		FileWriter fw = null;
		try {
			if (!f.exists()) {
				f.getParentFile().mkdirs();
			}
			fw = new FileWriter(f, true);
			fw.append(err + System.lineSeparator());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void logError(StackTraceElement[] stackTrace) {
		logError("#######################"+new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+"########################################");
		for (StackTraceElement s : stackTrace) {
			logError(s.toString());
		}
		logError("#################################END Error Stack##############################");

	}
}
