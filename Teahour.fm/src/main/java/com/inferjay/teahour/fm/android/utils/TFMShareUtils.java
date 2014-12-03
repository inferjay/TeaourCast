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

import java.util.HashMap;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.inferjay.teahour.fm.android.R;
import com.inferjay.teahour.fm.android.constant.TFMConstant;


/**
 * <p>Description: </p>
 *
 * @since 2014-8-26 下午2:25:05
 * @author inferjay
 * @version 1.0
 */
public final class TFMShareUtils {
	private static boolean isInit = false;
	
	private static void initShareSDK(Context context) {
		if (!isInit) {
			ShareSDK.initSDK(context, TFMConstant.KEY_SHARE_SDK);
			isInit = true;
		}
	}

	public static void shareToWechatMoments(Context context, String text) {
        if(TextUtils.isEmpty(text))return;
		initShareSDK(context);
		HashMap<String,Object> hashMap = new HashMap<String, Object>();
	    hashMap.put("BypassApproval","true");
	    ShareSDK.setPlatformDevInfo(WechatMoments.NAME,hashMap);
		WechatMoments.ShareParams shareParams = new WechatMoments.ShareParams();
		shareParams.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.qrcode));
		shareParams.setText(text);
		shareParams.setShareType(Platform.SHARE_WEBPAGE);
		Platform wechatMoments = ShareSDK.getPlatform(context, WechatMoments.NAME);
		wechatMoments.share(shareParams);
	}
	
	public static void shareToWeibo(Context context, String text) {
        if(TextUtils.isEmpty(text))return;
		initShareSDK(context);
		SinaWeibo.ShareParams shareParams = new SinaWeibo.ShareParams();
		shareParams.setImageUrl(TFMConstant.URL_TFM_QRCODE);
		shareParams.setText(text);
		Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		weibo.share(shareParams);
	}
	
	public static void shareToTwitter(Context context, String text) {
        if(TextUtils.isEmpty(text))return;
		initShareSDK(context);
		Twitter.ShareParams shareParams = new Twitter.ShareParams();
		shareParams.setText(text);
		Platform twitter = ShareSDK.getPlatform(context, Twitter.NAME);
		twitter.share(shareParams);
	}
	
	public static void shareToFacebook(Context context, String text) {
        if(TextUtils.isEmpty(text))return;
		initShareSDK(context);
		Facebook.ShareParams shareParams = new Facebook.ShareParams();
		shareParams.setText(text);
		Platform facebook = ShareSDK.getPlatform(context, Facebook.NAME);
		facebook.share(shareParams);
	}
}
