/**
 *   Copyright 2014 inferjay (http://www.inferjay.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.inferjay.teahour.fm.android.utils;

import android.util.Log;

import com.inferjay.teahour.fm.android.BuildConfig;

public final class TFMLogUtils {
	private static boolean isDebug = BuildConfig.DEBUG;
	
	public static void printLogD(String tag, String msg) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}
	
	public static void printLogW(String tag, String msg) {
		if (isDebug) {
			Log.w(tag, msg);
		}
	}
	
	public static void printLogI(String tag, String msg) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}
	
	public static void printLogE(String tag, String msg) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}
}
