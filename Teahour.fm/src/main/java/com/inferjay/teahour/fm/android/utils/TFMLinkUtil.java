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

import android.content.Context;
import android.content.Intent;

import com.inferjay.teahour.fm.android.constant.TFMConstant;
import com.inferjay.teahour.fm.android.ui.TFMInlineBrowserActivity;


/**
 *
 * @since 2014-8-22 下午8:40:40
 * @author inferjay
 * @version 1.0
 */
public class TFMLinkUtil {

	public static final void openLinkWithInlinBrowser(Context context, String link) {
		Intent intent;
		intent = new Intent(context, TFMInlineBrowserActivity.class);
		intent.putExtra(TFMConstant.KEY_COMMON_WEBVIEW_INTENT_DATA, link);
		context.startActivity(intent);
	}

}
