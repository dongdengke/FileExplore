package cn.edu.bjtu.fileexplorer.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class ApkUtils {
	private PackageInfo packageInfo;

	/**
	 * 获取存在的apk的图标
	 * 
	 * @param context
	 * @param path
	 * @return
	 */
	public static Drawable loadApkIcon(Context context, String path) {
		PackageManager pm = context.getPackageManager();
		PackageInfo packageInfo = pm.getPackageArchiveInfo(path,
				PackageManager.GET_ACTIVITIES);
		if (packageInfo != null) {
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			applicationInfo.publicSourceDir = path;
			return applicationInfo.loadIcon(pm);
		}
		return null;
	}
}
