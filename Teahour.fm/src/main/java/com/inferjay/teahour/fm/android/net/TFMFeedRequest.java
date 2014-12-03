package com.inferjay.teahour.fm.android.net;

import java.io.UnsupportedEncodingException;

import org.apache.http.protocol.HTTP;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

public class TFMFeedRequest extends StringRequest {

	public TFMFeedRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
	}

	@Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return ParseNetworkResponseHelper.parseRespons(response);
    }
}
