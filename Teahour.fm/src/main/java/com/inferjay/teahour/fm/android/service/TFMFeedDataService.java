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
package com.inferjay.teahour.fm.android.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.inferjay.teahour.fm.android.R;
import com.inferjay.teahour.fm.android.constant.TFMConstant;
import com.inferjay.teahour.fm.android.data.TFMFeedDataSet;
import com.inferjay.teahour.fm.android.data.TFMFeedItem;
import com.inferjay.teahour.fm.android.net.TFMRequestManager;
import com.inferjay.teahour.fm.android.net.TFMFeedRequest;
import com.inferjay.teahour.fm.android.utils.TFMFeedDataParser;
import com.inferjay.teahour.fm.android.utils.TFMShareUtils;


public class TFMFeedDataService {

    private OnLoadFeedDataListener mFeedDataLoadListener;
    
	private TFMFeedDataSet mTFMFeedData;
	
	private static TFMFeedDataService mFeedDataService;

    private boolean isLoadSuccess;

	private TFMFeedDataService(){}
	
	public static TFMFeedDataService getInstance(){
		if (mFeedDataService == null) {
			mFeedDataService = new TFMFeedDataService();
		}
		return mFeedDataService;	
	}
	
    public void loadTFMFeedData() {
        TFMFeedRequest mFeedRequest = new TFMFeedRequest(TFMConstant.URL_TFM_FEED, mSuccesslistener, mErrorListener);
    	TFMRequestManager.addRequest(mFeedRequest, mFeedRequest.hashCode());
    }
    

	public TFMFeedDataSet getFeedData() {
		return mTFMFeedData;
	}

	public void setFeedData(TFMFeedDataSet data) {
		this.mTFMFeedData = data;
	}


	private Listener<String> mSuccesslistener = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			try {
                isLoadSuccess = true;
				mTFMFeedData = TFMFeedDataParser.getTeahourFMFeedData(response);
				mFeedDataLoadListener.OnFeedDataLoadSuccess();
			} catch (IOException e) {
				e.printStackTrace();
                isLoadSuccess = false;
				mFeedDataLoadListener.OnFeedDataLoadError("rss feed data parse error");
			} catch (XmlPullParserException e) {
				e.printStackTrace();
                isLoadSuccess = false;
				mFeedDataLoadListener.OnFeedDataLoadError("rss feed data parse error");
			}

		}
		
	};

	private ErrorListener mErrorListener = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
            isLoadSuccess = false;
			mFeedDataLoadListener.OnFeedDataLoadError(error.getMessage());
		}
	};
	
	public interface OnLoadFeedDataListener {
		public void OnFeedDataLoadSuccess();		
		public void OnFeedDataLoadError(String err);
	}

	public OnLoadFeedDataListener getLoadFeedDataListener() {
		return mFeedDataLoadListener;
	}


	public void setLoadFeedDataListener(OnLoadFeedDataListener listener) {
		mFeedDataLoadListener = listener;
	}
	
	public String findFeedAudioURLByIndex(int index) {
		return mTFMFeedData != null && mTFMFeedData.findFeedItemByIndex(index) != null
				? mTFMFeedData.findFeedItemByIndex(index).getGuid() : "";
	}
	
	public boolean isExistFeedData() {
		return mTFMFeedData != null && mTFMFeedData.isExistFeedItems();
	}
	
	public boolean isExistFeedItem(int index) {
		return findFeedItemByIndex(index) != null;
	}

	public String findFmHostByIndex(int index) {
		return isExistFeedItem(index) ? findFeedItemByIndex(index).getAuthor() : "";
	}

	public TFMFeedItem findFeedItemByIndex(int index) {
		return mTFMFeedData != null ? mTFMFeedData.findFeedItemByIndex(index) : null; 
	}
	

	public String getFeedItemAuthorByIndex(int index) {
		return isExistFeedItem(index) ? findFeedItemByIndex(index).getAuthor() : "";
	}


	public CharSequence getFeedItemKeywordsByIndex(int index) {
		return isExistFeedItem(index) ? findFeedItemByIndex(index).getKeywords() : "";
	}


	public String getFeedItemSummaryByIndex(int index) {
		return isExistFeedItem(index) ? findFeedItemByIndex(index).getSummary().getContent() : "";
	}


	public String getFeedItemMoreResources(int index) {
		String description = isExistFeedItem(index) ? findFeedItemByIndex(index).getDescriptionContent() : "";
		String sectionTag = "<section";
		int start = 0;
		if (!TextUtils.isEmpty(description) && (start = description.indexOf(sectionTag)) > 0) {
			description = description.substring(start); 
		}
		return description;
	}
	
	public ArrayList<String> getFeedItemTitleList() {
		return mTFMFeedData.getFeedItemTitleList();
	}
	
	public int getFeedItemTotalCount() {
		return isExistFeedData() ? mTFMFeedData.getItem().size() : 0;
	}

	
	public String getFeedItemGuestNamesByIndex(int index) {
		
		return isExistFeedData() ? findFeedItemByIndex(index).getGuesteNames() : "";
	}
	
	public List<String> getFeedItemAuthorAvatarURLs(int index) {
		return isExistFeedData() ? mTFMFeedData.getFeedItemAuthorAvatarURLs(index) : new ArrayList<String>();
	}

    public void shareToWeibo(Context context, int index) {
        if(isLoadSuccess && TFMFeedDataService.getInstance().isExistFeedData()) {
            TFMShareUtils.shareToWeibo(context, getShareContentString(context, index));
        } else {
            showNoShareDataToast(context);
        }
    }

    public void shareToWechatMoments(Context context, int index) {
        if (isLoadSuccess && TFMFeedDataService.getInstance().isExistFeedData()) {
            TFMShareUtils.shareToWechatMoments(context, getShareContentString(context, index));
        } else {
            showNoShareDataToast(context);
        }
    }

    private void showNoShareDataToast(Context context) {
        Toast.makeText(context, R.string.text_msg_no_share_data, Toast.LENGTH_SHORT).show();
    }

    public String getShareContentString(Context context, int index){
        StringBuilder contentBuilder = new StringBuilder(context.getString(R.string.text_share_content_titel_prefix));
        String title = TFMFeedDataService.getInstance().getFeedItemTitleList().get(index);
        contentBuilder.append(title);
        String notes = TFMFeedDataService.getInstance().getFeedItemSummaryByIndex(index);
        if (!TextUtils.isEmpty(notes) && notes.length() > TFMConstant.MAX_SHARE_NOTES_LEN) {
            notes = notes.substring(0, TFMConstant.MAX_SHARE_NOTES_LEN);
        }
        contentBuilder.append(notes);
        String link = TFMFeedDataService.getInstance().findFeedItemByIndex(index).getLink();
        contentBuilder.append(link);
        return contentBuilder.toString();
    }
}
