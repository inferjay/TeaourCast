package com.inferjay.teahour.fm.android.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class TFMRequestManager {
	
	private static RequestQueue mRequestQueue;
	
	public static void initRequestQueue(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}
	
	public static void addRequest(Request<?> request, Object tag) {
		if (tag != null && tag instanceof String && ((String) tag).trim().length() > 0) {
			request.setTag(tag);
		}
		mRequestQueue.add(request);
	}
	
	public static void cancelRequest(Object tag) {
		mRequestQueue.cancelAll(tag);
	}
}
