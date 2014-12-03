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

public final class TFMTimeUtils {
	
	public static String formatTimeToString(long millis) {
		StringBuilder resultBuffer = new StringBuilder();
//		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		if (millis > 0) {
//			hours = (int) (millis / 1000 / ( 60 * 60));
//			minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
//			seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
			
			minutes = (int) (millis / (1000 * 60));
			seconds = (int) ((millis % (1000 * 60)) / 1000);
		}
		
		resultBuffer.append(String.format("%02d", minutes))
		.append(":").append(String.format("%02d", seconds));
		
//		resultBuffer.append(String.format("%02d", hours)).append(":").append(String.format("%02d", minutes))
//				.append(":").append(String.format("%02d", seconds));
		return resultBuffer.toString();
	}
}
