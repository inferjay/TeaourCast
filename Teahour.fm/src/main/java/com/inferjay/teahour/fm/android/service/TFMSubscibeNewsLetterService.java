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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.inferjay.teahour.fm.android.constant.TFMConstant;
import com.inferjay.teahour.fm.android.net.TFMNewsLetterRequest;
import com.inferjay.teahour.fm.android.net.TFMRequestManager;
import com.inferjay.teahour.fm.android.utils.TFMLogUtils;

import java.util.HashMap;

public class TFMSubscibeNewsLetterService implements Response.Listener<String>, Response.ErrorListener {

    private OnSubscibeNewsLetterListener mSubscibeNewsLetteListener;

    private static TFMSubscibeNewsLetterService mFeedDataService;

    private TFMSubscibeNewsLetterService(){}

    public static TFMSubscibeNewsLetterService getInstance(){
        if (mFeedDataService == null) {
            mFeedDataService = new TFMSubscibeNewsLetterService();
        }
        return mFeedDataService;
    }

    public void attemptSubscibeNewsLetter(String email) {
        HashMap<String, String> requestBody = new HashMap<String,String>();

        requestBody.put("u", TFMConstant.NEWSLETTER_UID);
        requestBody.put("id", TFMConstant.NEWSLETTER_ID);
        requestBody.put("EMAIL", email);

        TFMNewsLetterRequest request = new TFMNewsLetterRequest(TFMConstant.URL_NEWS_LETTER, requestBody, this, this);
        TFMRequestManager.addRequest(request, request.hashCode());
    }

    @Override
    public void onResponse(String response) {
        TFMLogUtils.printLogE("", response);
        mSubscibeNewsLetteListener.onSubscibeSuccess(response);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
          mSubscibeNewsLetteListener.onSubscibeError(volleyError.getMessage());
    }

    public interface OnSubscibeNewsLetterListener {
        public void onSubscibeSuccess(String success);
        public void onSubscibeError(String error);
    }

    public void setSubscibeNewsLetteListener(OnSubscibeNewsLetterListener listener) {
        this.mSubscibeNewsLetteListener = listener;
    }

}
