package com.tim.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveFile {
	
	/**
	 * 保存文本文件
	 * 
	 * @param filePath
	 *            文件位置
	 * @param info
	 *            文件内容
	 */
	public static void saveTxtFile(String filePath, String info) {
		FileWriter fw = null;
		try {
			// 如果文件存在，则追加内容；如果文件不存在，则创建文件
			File f = new File(filePath);
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		PrintWriter pw = new PrintWriter(fw);
		pw.println(info);
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
