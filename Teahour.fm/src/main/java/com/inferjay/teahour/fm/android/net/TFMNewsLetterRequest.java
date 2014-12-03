package com.inferjay.teahour.fm.android.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class TFMNewsLetterRequest extends TFMBasePostRequest<String> {

    public TFMNewsLetterRequest(String url, Map<String, String> requestBody, Listener<String> listener,
                                 ErrorListener errorListener) {
        super(url, requestBody, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return ParseNetworkResponseHelper.parseRespons(response);
    }
}
