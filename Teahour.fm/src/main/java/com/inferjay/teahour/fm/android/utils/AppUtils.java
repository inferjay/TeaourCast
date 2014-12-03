package com.inferjay.teahour.fm.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public final class AppUtils {

	public static String getAppVersionNumberString(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo;
		try {
			packageInfo = packageManager.getPackageInfo(getAppPackgeName(context), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getAppPackgeName(Context context) {	
		return context.getPackageName();
	}
}
