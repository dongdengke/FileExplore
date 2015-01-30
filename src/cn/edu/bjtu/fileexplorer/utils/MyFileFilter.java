package cn.edu.bjtu.fileexplorer.utils;

import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		if (!pathname.getName().startsWith(".")) {
			return true;
		}
		return false;
	}
}
