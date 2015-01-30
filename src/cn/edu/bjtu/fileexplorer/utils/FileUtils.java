package cn.edu.bjtu.fileexplorer.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 文件工具类
 * 
 * @author dong
 * 
 */
public class FileUtils {
	/**
	 * 对文件按照字典的顺序排序
	 * 
	 * @param listFiles
	 * @return
	 */
	public static File[] sortFiles(File[] listFiles) {
		List<File> asList = Arrays.asList(listFiles);
		Collections.sort(asList, new FileComparator());
		File[] array = asList.toArray(new File[asList.size()]);
		return array;
	}
}
