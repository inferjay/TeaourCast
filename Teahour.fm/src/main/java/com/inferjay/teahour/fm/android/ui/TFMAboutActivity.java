package com.inferjay.teahour.fm.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.inferjay.teahour.fm.android.R;

import butterknife.InjectView;
import butterknife.OnClick;

public class TFMAboutActivity extends TFMNoParentNavBaseActivity {

    @InjectView(R.id.iv_about_weibo)ImageView mWeiboImageView;
    @InjectView(R.id.iv_about_twitter)ImageView mTwitterImageView;
    @InjectView(R.id.iv_about_facebook)ImageView mFacebookImageVIew;
	/**
	 * <p>Description: </p>
	 * @return
	 *
	 * @since 2014年9月8日 下午10:52:33
	 * @author inferjay
	 */
	@Override
	protected int getContentViewResId() {
		return R.layout.activity_teahour_fm_about;
	}

    @OnClick({R.id.iv_about_weibo, R.id.iv_about_facebook, R.id.iv_about_twitter})
	public void openSocialHomePage(View view) {
        String url = null;

        switch (view.getId()) {
            case R.id.iv_about_weibo :
               url = getString(R.string.url_weibo);
                break;
            case R.id.iv_about_twitter :
                url = getString(R.string.url_twitter);
                break;
            case R.id.iv_about_facebook :
                url = getString(R.string.url_facebook);
                break;
            default:
                break;

        }
        if (null != url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }
}
