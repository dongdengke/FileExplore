package cn.edu.bjtu.fileexplorer.utils;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {

	@Override
	public int compare(File f1, File f2) {
		// TODO Auto-generated method stub
		// 1 文件夹和文件夹比较
		if (f1.isDirectory() && f2.isDirectory()) {
			return f1.getName().compareToIgnoreCase(f2.getName());
		} else if (f1.isDirectory() && f2.isFile()) {// 2 文件夹和文件比较
			return -1;
		} else if (f1.isFile() && f2.isDirectory()) {// 3 文件和文件夹比较
			return 1;
		} else { // 4 文件和文件比较
			return f1.getName().compareToIgnoreCase(f2.getName());
		}
	}

}
