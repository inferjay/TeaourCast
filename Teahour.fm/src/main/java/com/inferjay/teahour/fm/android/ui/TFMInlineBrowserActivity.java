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
package com.inferjay.teahour.fm.android.ui;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.inferjay.teahour.fm.android.R;
import com.inferjay.teahour.fm.android.constant.TFMConstant;

import org.apache.http.protocol.HTTP;

import butterknife.InjectView;

@SuppressLint("SetJavaScriptEnabled")
public class TFMInlineBrowserActivity extends TFMNoParentNavBaseActivity {
    @InjectView(R.id.pb_webview_load_progress) ProgressBar mProgressBar;
    @InjectView(R.id.wv_common_webview)WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWebViewSettings();
        loadPage();
	}

    private void loadPage() {
        String url = getIntent().getStringExtra(TFMConstant.KEY_COMMON_WEBVIEW_INTENT_DATA);
        if (!TextUtils.isEmpty(url)) {
            if (Patterns.WEB_URL.matcher(url).matches()) {
                webView.loadUrl(url);
            } else {
                webView.loadDataWithBaseURL("", url,"text/html", HTTP.UTF_8, "");
            }
        } else {
            finish();
        }
    }


    @Override
	protected int getContentViewResId() {
		return R.layout.activity_teahour_fm_webview;
	}

	
	private void initWebViewSettings() {
		final WebSettings settings = webView.getSettings();
		// 设置布局算法
		settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		// 设置缓存模式
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 设置使用扩大的窗口
		 settings.setUseWideViewPort(true);
		// 设置预览模式
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true);
		settings.setJavaScriptEnabled(true);

        //设置WebView先不要自动加载图片，等页面finish后再发起图片加载。
        if(Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				mProgressBar.setProgress(newProgress);
				if (newProgress == mProgressBar.getMax()) {
                    mProgressBar.setVisibility(View.GONE);
				}	
			}
		});
		webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!settings.getLoadsImagesAutomatically()) {
                    settings.setLoadsImagesAutomatically(true);
                }
            }

            @Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});
	}

}
