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

import com.inferjay.teahour.fm.android.R;

import android.app.ActionBar;


/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: inferjay.com </p>
 *
 * @since 2014-8-31 下午2:10:43
 * @author inferjay
 * @version 1.0
 */
public final class TFMActionBarUtils {
	
	public static void setActionBarIcon(ActionBar actionBar) {
		if (actionBar != null) {
			actionBar.setIcon(R.drawable.ic_acitonbar_logo);
		}
	}
}
