package com.inferjay.teahour.fm.android;

import android.app.Application;

import com.inferjay.teahour.fm.android.net.TFMRequestManager;

public class TFMApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		TFMRequestManager.initRequestQueue(getApplicationContext());
	}
}
